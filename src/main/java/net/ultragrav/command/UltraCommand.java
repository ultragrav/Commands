package net.ultragrav.command;

import lombok.Getter;
import lombok.Setter;
import net.ultragrav.chat.components.Component;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;
import net.ultragrav.command.registry.RegistryManager;
import net.ultragrav.command.wrapper.chat.TextUtil;
import net.ultragrav.command.wrapper.player.UltraPlayer;
import net.ultragrav.command.wrapper.sender.UltraSender;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Getter
public abstract class UltraCommand {
    private static final String DEFAULT_HELP_FORMAT = "&8/&7<cmd> &f<args>";
    private static final String DEFAULT_HELP_HEADER = "   &8&l&m------&c&l Usage &8&l&m------";
    private static final String DEFAULT_HELP_FOOTER = "";
    private static final String DEFAULT_ERROR_MESSAGE = "&cAn error occurred, please report this to an administrator.";

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    // ----------------------------------- //
    // COMMAND INFORMATION
    // ----------------------------------- //
    protected final List<String> aliases = new ArrayList<>();
    protected final Set<UltraCommand> children = new HashSet<>();
    @Setter
    protected UltraCommand parent;
    @Setter
    protected String description = "";
    @Setter
    protected boolean allowConsole = true;
    @Setter
    protected boolean requirePermission = true;
    @Setter
    protected boolean checkParams = true;
    @Setter
    protected boolean async = false;

    // ----------------------------------- //
    // COMMAND EXECUTION
    // ----------------------------------- //
    protected UltraSender sender;
    protected String label;
    protected List<String> args;
    protected List<Object> convertedArgs = new ArrayList<>();
    @Setter
    private List<Parameter<?>> parameters = new ArrayList<>();

    @Setter
    private String helpFormat = null;
    @Setter
    private String helpHeader = null;
    @Setter
    private String helpFooter = null;
    @Setter
    private String errorMessage = null;

    /**
     * @param sender The person that executed this command either a player or the console.
     * @param args   The arguments that are bound for this command.
     */
    public void execute(final UltraSender sender, final String cmd, final List<String> args) {
        Runnable task = () -> {
            this.sender = sender;
            this.label = cmd;
            this.args = args;

            try {
                if (isRequirePermission() && !sender.hasPermission(getPermission())) {
                    throw new CommandException("§cYou do not have permission to execute this command.");
                }

                if (!isAllowConsole() && getPlayer() == null)
                    throw new CommandException("Sorry, console is not allowed to execute this command");

                preConditions();

                if (hasChildren() && args.size() > 0) {
                    String label = args.get(0);

                    UltraCommand child = matchExactChild(label);
                    if (child != null) {
                        // Avoid modifying args, since it might be unmodifiable
                        List<String> copy = new ArrayList<>(args);
                        copy.remove(0);
                        child.execute(sender, label, copy);
                        return; // Don't perform the help on this command if a match is found
                    } // If the subcommand isn't found, call this.preform() since it sends help
                }

                if (checkParams) {
                    try {
                        parseArgs();
                    } catch (CommandException e) {
                        sendHelp();
                        throw e;
                    }
                }

                try {
                    this.perform();
                } catch (Exception e) {
                    if (e instanceof CommandException) {
                        throw e;
                    } else {
                        System.out.println("An exception occurred while handling a command: " + getFullCommand() + " " + String.join(" ", args));
                        e.printStackTrace();
                        tell(getErrorMessage());
                    }
                }
            } catch (CommandException exception) {
                // - This is our main exception to catch all the little things mostly for parsing.
                tell(exception.getMessage());
            } finally {
                // Clean up params.
                this.sender = null;
                this.args = null;
            }
        };

        if (async) {
            executor.submit(task);
        } else {
            task.run();
        }
    }

    protected void preConditions() {
    }

    /**
     * By default it will execute the help command.
     * To get an impl for a sub command
     * you will need to override this method.
     */
    protected void perform() {
        sendHelp();
    }

    public String getFullCommand() {
        if (parent == null) {
            return getAliases().get(0);
        } else {
            return parent.getFullCommand() + " " + getAliases().get(0);
        }
    }

    public String getParameterDescriptions() {
        List<String> params = new ArrayList<>();
        for (Parameter<?> param : parameters) {
            String name = param.getName();
            if (param.isOptional()) {
                name = "[" + name + "]";
            } else {
                name = "<" + name + ">";
            }
            params.add(name);
        }
        return String.join(" ", params);
    }

    public void sendHelp() {
        if (getHelpHeader() != null)
            tell(formatHelpHeadFoot(getHelpHeader()));
        getHelp().forEach(this::tell);
        if (getHelpFooter() != null)
            tell(formatHelpHeadFoot(getHelpFooter()));
    }

    public List<String> getHelp() {
        return getHelp(getHelpFormat());
    }

    public List<String> getHelp(String format) {
        List<String> ret = new ArrayList<>();
        if (hasChildren()) {
            for (UltraCommand cmd : children) {
                if (cmd.requirePermission && !sender.hasPermission(cmd.getPermission()))
                    continue;
                cmd.sender = sender;
                ret.addAll(cmd.getHelp(format));
            }
        }
        ret.add(format.replaceAll("<cmd>", getFullCommand())
                .replaceAll("<desc>", getDescription())
                .replaceAll("<args>", getParameterDescriptions())
                .replaceAll("<params>", getParameterDescriptions())
                .replaceAll("<perm>", getPermission()));
        return ret;
    }

    private String formatHelpHeadFoot(String msg) {
        return msg.replaceAll("<alias>", getAliases().get(0));
    }

    public String getHelpFooter() {
        if (this.helpFooter != null) return this.helpFooter;
        if (parent != null) return parent.getHelpFooter();
        return DEFAULT_HELP_FOOTER;
    }

    public String getHelpHeader() {
        if (this.helpHeader != null) return this.helpHeader;
        if (parent != null) return parent.getHelpHeader();
        return DEFAULT_HELP_HEADER;
    }

    public String getHelpFormat() {
        if (this.helpFormat != null) return this.helpFormat;
        if (parent != null) return parent.getHelpFormat();
        return DEFAULT_HELP_FORMAT;
    }

    public String getErrorMessage() {
        if (this.errorMessage != null) return this.errorMessage;
        if (parent != null) return parent.getErrorMessage();
        return DEFAULT_ERROR_MESSAGE;
    }

    public String getPermission() {
        return (parent == null ? "" : parent.getPermission() + ".") + aliases.get(0);
    }

    // ----------------------------------- //
    // TAB COMPLETION
    // ----------------------------------- //

    /**
     * Will either return the sub command label completions, or will gather the arguments.
     *
     * @param sender The person that has executed the command.
     * @param args   The arguments provided.
     * @return Will return a list that will be sent to a player.
     */
    public List<String> getTabCompletions(UltraSender sender, List<String> args) {
        args = new ArrayList<>(args); // Avoid modifying original
        if (requirePermission && !sender.hasPermission(getPermission()))
            return new ArrayList<>();
        List<String> compl = null;
        if (hasChildren()) {
            compl = getTabCompletionsSubCommand(sender, args);
        }
        if (compl == null) {
            compl = getTabCompletionsArguments(sender, args);
        }
        return compl;
    }

    private List<String> getTabCompletionsSubCommand(UltraSender sender, List<String> args) {
        if (args.size() != 1) {
            UltraCommand child = this.matchExactChild(args.get(0));
            if (child == null || !sender.hasPermission(child.getPermission()))
                return null; // Tab completion failed (no child)

            args.remove(0);
            return child.getTabCompletions(sender, args);
        }

        String label = args.get(0).toLowerCase();
        List<String> ret = new ArrayList<>();

        for (UltraCommand child : this.children) {
            if (child.requirePermission && !sender.hasPermission(child.getPermission())) continue;
            for (String alias : child.aliases) {
                if (alias.toLowerCase().startsWith(label)) {
                    ret.add(alias);
                }
            }
        }

        return ret;
    }

    private List<String> getTabCompletionsArguments(UltraSender sender, List<String> args) {
        List<String> argsCopy = new ArrayList<>(args);
        List<String> argsCopy2 = new ArrayList<>(argsCopy);
        for (Parameter<?> param : parameters) {
            if (argsCopy.size() == 1) {
                return param.getProvider().tabComplete(argsCopy2, sender);
            }

            try {
                param.check(this);
                param.convert(argsCopy, sender);
            } catch (CommandException ignored) {
            }

            if (argsCopy.isEmpty()) {
                return param.getProvider().tabComplete(argsCopy2, sender);
            }
            argsCopy2 = argsCopy2.subList(argsCopy2.size() - argsCopy.size(), argsCopy2.size());
        }
        return Collections.emptyList();
    }

    // ----------------------------------- //
    // ARGUMENTS
    // ----------------------------------- //
    private void parseArgs() {
        if (parameters.isEmpty()) {
            if (!args.isEmpty()) {
                throw new CommandException("Unexpected arguments.");
            }
            return;
        }

        List<String> argsCopy = new ArrayList<>(args);
        this.convertedArgs = new ArrayList<>(parameters.size());
        for (Parameter<?> param : parameters) {
            if (argsCopy.isEmpty()) {
                if (param.isOptional()) {
                    convertedArgs.add(param.getDefaultValue());
                } else if (param == parameters.get(parameters.size() - 1) && param.isVarArg()) {
                    convertedArgs.add(new ArrayList<>());
                    return;
                } else {
                    throw new CommandException("Missing required argument: " + param.getName());
                }
            } else {
                param.check(this);
                convertedArgs.add(param.convert(argsCopy, sender));
            }
        }
        Parameter<?> lastParam = parameters.get(parameters.size() - 1);
        if (lastParam.isVarArg()) {
            Object last = this.convertedArgs.get(parameters.size() - 1);
            List<Object> varArgs = new ArrayList<>();
            varArgs.add(last);
            while (!argsCopy.isEmpty()) {
                varArgs.add(lastParam.convert(argsCopy, sender));
            }
            this.convertedArgs.set(parameters.size() - 1, varArgs);
        }
    }

    public <T> T getArgument(int index) {
        return (T) convertedArgs.get(index);
    }

    private boolean isArgSet(int idx) {
        if (idx < 0) return false;
        if (idx + 1 > this.convertedArgs.size()) return false;
        return this.convertedArgs.get(idx) != this.parameters.get(idx).provider.defaultNullValue();
    }

    // ----------------------------------- //
    // COMMAND SETTINGS
    // ----------------------------------- //
    protected int getRequiredParameterCount() {
        int ret = 0;

        for (Parameter<?> parameter : this.getParameters()) {
            if (parameter.isRequired()) ret++;
        }

        return ret;
    }

    /**
     * Add a parameter
     *
     * @param parameter Parameter
     * @param <T>       Type
     * @return {@code parameter}
     */
    protected <T> Parameter<T> addParameter(Parameter<T> parameter) {
        this.getParameters().add(parameter);
        return parameter;
    }

    /**
     * Create a parameter builder
     *
     * @param provider
     * @param <T>
     * @return Parameter.Builder for provider
     */
    protected <T> Parameter.Builder<T> parameterBuilder(UltraProvider<T> provider) {
        return new Parameter.Builder<T>(provider, this::addParameter);
    }

    /**
     * @deprecated use {@link #addParameter(Parameter)} with {@link Parameter#builder(UltraProvider)}
     */
    @Deprecated
    protected <T> Parameter<T> addParameter(T defaultValue, UltraProvider<T> provider, String name, String description, boolean varArgs) {
        return this.addParameter(Parameter.builder(provider)
                .defaultValue(defaultValue)
                .name(name).desc(description)
                .varArg(varArgs).build()
        );
    }

    /**
     * @deprecated use {@link #addParameter(Parameter)} with {@link Parameter#builder(UltraProvider)}
     */
    @Deprecated
    protected <T> Parameter<T> addParameter(T defaultValue, UltraProvider<T> provider, String name, String description) {
        return this.addParameter(Parameter.builder(provider)
                .defaultValue(defaultValue)
                .name(name).desc(description)
                .build()
        );
    }

    /**
     * @deprecated use {@link #addParameter(Parameter)} with {@link Parameter#builder(UltraProvider)}
     */
    @Deprecated
    protected <T> Parameter<T> addParameter(UltraProvider<T> provider, String name, String description, boolean varArgs) {
        return this.addParameter(Parameter.builder(provider)
                .name(name).desc(description)
                .varArg(varArgs).build()
        );
    }

    /**
     * @deprecated use {@link #addParameter(Parameter)} with {@link Parameter#builder(UltraProvider)}
     */
    @Deprecated
    protected <T> Parameter<T> addParameter(UltraProvider<T> provider, String name, String description) {
        return this.addParameter(Parameter.builder(provider)
                .name(name).desc(description)
                .build()
        );
    }

    /**
     * @deprecated use {@link #addParameter(Parameter)} with {@link Parameter#builder(UltraProvider)}
     */
    @Deprecated
    protected <T> Parameter<T> addParameter(UltraProvider<T> provider, String name) {
        return this.addParameter(Parameter.builder(provider)
                .name(name).build()
        );
    }

    /**
     * @deprecated use {@link #addParameter(Parameter)} with {@link Parameter#builder(UltraProvider)}
     */
    @Deprecated
    protected <T> Parameter<T> addParameter(T defaultValue, UltraProvider<T> provider, String name) {
        return this.addParameter(Parameter.builder(provider)
                .defaultValue(defaultValue)
                .name(name).build()
        );
    }

    /**
     * @deprecated use {@link #addParameter(Parameter)} with {@link Parameter#builder(UltraProvider)}
     */
    @Deprecated
    protected <T> Parameter<T> addParameter(UltraProvider<T> provider, boolean varArgs) {
        return this.addParameter(Parameter.builder(provider).varArg(varArgs).build());
    }

    /**
     * @deprecated use {@link #addParameter(Parameter)} with {@link Parameter#builder(UltraProvider)}
     */
    @Deprecated
    protected <T> Parameter<T> addParameter(UltraProvider<T> provider) {
        return this.addParameter(Parameter.builder(provider).build());
    }

    /**
     * @deprecated use {@link #addParameter(Parameter)} with {@link Parameter#builder(UltraProvider)}
     */
    @Deprecated
    protected <T> Parameter<T> addParameter(T defaultValue, UltraProvider<T> provider) {
        return this.addParameter(Parameter.builder(provider).defaultValue(defaultValue).build());
    }

    protected boolean noParameterForIndex(int index) {
        if (index < 0) return true;
        return this.getParameters().size() <= index;
    }


    // Add aliases

    protected void addAlias(String alias) {
        if (!this.aliases.contains(alias.toLowerCase()))
            this.aliases.add(alias.toLowerCase());
    }

    protected void addAliases(String... aliases) {
        this.aliases.addAll(Arrays.stream(aliases).map(String::toLowerCase).filter(alias -> !this.aliases.contains(alias)).collect(Collectors.toList()));
    }

    // Add subcommands

    public void addChildren(UltraCommand... commands) {
        for (UltraCommand command : commands) {
            this.children.add(command);
            command.setParent(this);
        }
    }

    // ----------------------------------- //
    // UTILS
    // ----------------------------------- //

    /**
     * Send a message and return
     *
     * @param message Message
     * @deprecated Prefer using tell and the return to allow IDEs to read code flow!
     */
    @Deprecated
    protected void returnTell(String message) {
        throw new CommandException(message);
    }

    protected void tell(String message) {
        sender.sendMessage(TextUtil.comp(message));
    }

    protected void tell(Component comp) {
        sender.sendMessage(comp);
    }

    protected UltraPlayer getPlayer() {
        return isPlayer() ? (UltraPlayer) sender : null;
    }

    protected boolean isPlayer() {
        return sender.isPlayer();
    }

    public Set<UltraCommand> getChildren(String label) {
        Set<UltraCommand> ret = new HashSet<>();
        label = label.toLowerCase();
        // - Go though each command.
        for (UltraCommand command : this.children) {
            for (String alias : command.getAliases()) {
                if (alias.equalsIgnoreCase(label)) {
                    return Collections.singleton(command);
                }
                if (ret.contains(command)) continue;

                ret.add(command);
            }
        }
        return ret;
    }

    public boolean hasChildren() {
        return !this.children.isEmpty();
    }

    protected UltraCommand matchExactChild(String label) {
        for (UltraCommand cmd : getChildren()) {
            if (cmd == null)
                continue;
            for (String str : cmd.getAliases()) {
                if (label.equalsIgnoreCase(str)) {
                    return cmd;
                }
            }
        }
        return null;
    }

    protected UltraCommand getChild(String label) {
        Set<UltraCommand> children = this.getChildren(label);

        if (children.isEmpty()) return null;
        if (children.size() > 1) return null;
        return children.iterator().next();
    }

    public void register() {
        RegistryManager.register(this);
    }
}