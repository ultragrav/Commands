package net.ultragrav.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Getter
public abstract class UltraCommand {
    private static final String DEFAULT_HELP_FORMAT = "/<cmd> <args>";
    private static final String DEFAULT_HELP_HEADER = "&cUsage:";
    private static final String DEFAULT_HELP_FOOTER = "";

    // ----------------------------------- //
    // COMMAND INFORMATION
    // ----------------------------------- //
    protected final List<String> aliases = new ArrayList<>();
    protected final Set<UltraCommand> children = Sets.newHashSet();
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

    // ----------------------------------- //
    // COMMAND EXECUTION
    // ----------------------------------- //
    protected UltraSender sender;
    protected List<String> args;
    @Setter
    private List<Parameter<?>> parameters = new ArrayList<>();

    @Setter
    private String helpFormat = null;
    @Setter
    private String helpHeader = null;
    @Setter
    private String helpFooter = null;

    /**
     * @param sender The person that executed this command either a player or the console.
     * @param args   The arguments that are bound for this command.
     */
    public void execute(UltraSender sender, List<String> args) {
        this.sender = sender;
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
                    args.remove(0);
                    child.execute(sender, args);
                    return; // Don't perform the help on this command if a match is found
                } // If the subcommand isn't found, call this.preform() since it sends help
            }

            if (
                    checkParams &&
                            (args.size() < getRequiredParameterCount() || args.size() > parameters.size()) &&
                            !(parameters.size() > 0 && parameters.get(parameters.size() - 1).isVarArg() && args.size() >= parameters.size() - 1)) {
                sendHelp();
                throw new CommandException("");
            }

            this.perform();
        } catch (CommandException exception) {
            // - This is our main exception to catch all of the little things mostly for parsing.
            tell(exception.getMessage());
        } finally {
            // Clean up params.
            sender = null;
            args = null;
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
            tell(getHelpHeader());
        getHelp().forEach(this::tell);
        if (getHelpFooter() != null)
            tell(getHelpFooter());
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
        ret.add(format.replace("<cmd>", getFullCommand())
                .replace("<desc>", getDescription())
                .replace("<args>", getParameterDescriptions())
                .replace("<params>", getParameterDescriptions())
                .replace("<perm>", getPermission()));
        return ret;
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
        if (requirePermission && !sender.hasPermission(getPermission()))
            return new ArrayList<>();
        List<String> compl = new ArrayList<>();
        if (hasChildren()) {
            compl = getTabCompletionsSubCommand(sender, args);
        }
        if (compl.isEmpty()) {
            compl = getTabCompletionsArguments(sender, args);
        }
        return compl;
    }

    private List<String> getTabCompletionsSubCommand(UltraSender sender, List<String> args) {
        if (args.size() != 1) {
            UltraCommand child = this.matchExactChild(args.get(0));
            if (child == null)
                return Collections.EMPTY_LIST;

            args.remove(0);
            return child.getTabCompletions(sender, args);
        }

        String label = args.get(0).toLowerCase();
        List<String> ret = Lists.newArrayList();

        for (UltraCommand children : this.children) {
            for (String alias : children.aliases) {
                if (alias.toLowerCase().startsWith(label)) {
                    ret.add(alias);
                }
            }
        }

        return ret;
    }

    private List<String> getTabCompletionsArguments(UltraSender sender, List<String> args) {
        int index = args.size() - 1;

        if (this.noParameterForIndex(index)) {
            Parameter<?> param;
            if (!this.parameters.isEmpty() && (param = this.parameters.get(this.parameters.size() - 1)).isVarArg()) {
                return param.getProvider().tabComplete(args.get(index), sender);
            } else {
                return Collections.emptyList();
            }
        }

        UltraProvider<?> provider = this.getParameters().get(index).getProvider();

        return provider.tabComplete(args.get(index), sender);
    }

    // ----------------------------------- //
    // ARGUMENTS
    // ----------------------------------- //
    public <T> T getArgument(int index) {
        if (this.noParameterForIndex(index))
            throw new IllegalArgumentException(index + " is out of range. Parameters size: " + this.getParameters().size());

        Parameter<?> parameterU = this.getParameters().get(index);

        if (index + 1 == this.getParameters().size()) {
            if (parameterU.isVarArg()) {
                List ret = new ArrayList<>();
                for (int i = index; i < this.args.size(); i++) {
                    ret.add(parameterU.convert(args.get(i), sender));
                }
                return (T) ret;
            }
        }

        Parameter<T> parameter = (Parameter<T>) parameterU;

        if (!this.isArgSet(index) && parameter.isDefaultValueSet()) return parameter.getDefaultValue();

        String arg = null;
        if (this.isArgSet(index)) arg = this.getArgs().get(index);

        return parameter.convert(arg, sender);
    }

    private boolean isArgSet(int idx) {
        if (idx < 0) return false;
        if (idx + 1 > this.args.size()) return false;
        return this.args.get(idx) != null;
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
        Set<UltraCommand> ret = Sets.newHashSet();
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