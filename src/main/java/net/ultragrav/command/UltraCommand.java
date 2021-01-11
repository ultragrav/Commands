package net.ultragrav.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;
import net.ultragrav.command.registry.RegistryManager;
import net.ultragrav.command.wrapper.chat.TextUtil;
import net.ultragrav.command.wrapper.player.UltraPlayer;
import net.ultragrav.command.wrapper.sender.UltraSender;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public abstract class UltraCommand {
    // ----------------------------------- //
    // COMMAND INFORMATION
    // ----------------------------------- //
    @Getter
    protected List<String> aliases = new ArrayList<>();
    protected Set<UltraCommand> children = Sets.newHashSet();
    @Getter
    @Setter
    protected UltraCommand parent;
    protected String description = "";
    protected boolean allowConsole = true;
    @Setter
    @Getter
    protected boolean requirePermission = true;
    // ----------------------------------- //
    // COMMAND EXECUTION
    // ----------------------------------- //
    protected UltraSender sender;
    protected List<String> args;
    private List<Parameter<?>> parameters = new ArrayList<>();

    @Getter
    private String helpFormat = "/<cmd> <args>";
    @Getter
    private String helpHeader = "&cUsage:";
    @Getter
    private String helpFooter = "";

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

                Set<UltraCommand> matches = this.getChildren(label);

                if (matches.size() == 1) {
                    UltraCommand command = matches.iterator().next();
                    args.remove(0);
                    command.execute(sender, args);
                    return; // Don't perform the help on this command if a match is found
                } // If the subcommand isn't found, call this.preform() since it sends help
            }

            if (args.size() < getRequiredParameterCount() || args.size() > parameters.size()) {
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
        if (helpHeader != null)
            tell(helpHeader);
        getHelp().forEach(this::tell);
        if (helpFooter != null)
            tell(helpFooter);
    }

    public List<String> getHelp() {
        return getHelp(helpFormat);
    }

    public List<String> getHelp(String format) {
        List<String> ret = new ArrayList<>();
        if (hasChildren()) {
            for (UltraCommand cmd : children) {
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

    public void setHelpFooter(String helpFooter) {
        this.helpFooter = helpFooter;
        this.getChildren().forEach(c -> c.setHelpFooter(helpFooter));
    }

    public void setHelpFormat(String helpFormat) {
        this.helpFormat = helpFormat;
        this.getChildren().forEach(c -> c.setHelpFormat(helpFormat));
    }

    public void setHelpHeader(String helpHeader) {
        this.helpHeader = helpHeader;
        this.getChildren().forEach(c -> c.setHelpHeader(helpHeader));
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
            UltraCommand child = this.getChild(args.get(0));
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

        if (this.noParameterForIndex(index)) return Collections.emptyList();
        UltraProvider<?> provider = this.getParameters().get(index).getProvider();

        return provider.tabComplete(args.get(index));
    }

    // ----------------------------------- //
    // ARGUMENTS
    // ----------------------------------- //
    public <T> T getArgument(int index) {
        if (this.noParameterForIndex(index))
            throw new IllegalArgumentException(index + " is out of range. Parameters size: " + this.getParameters().size());

        Parameter<T> parameter = (Parameter<T>) this.getParameters().get(index);

        if (!this.isArgSet(index) && parameter.isDefaultValueSet()) return parameter.getDefaultValue();


        String arg = null;
        if (this.isArgSet(index)) arg = this.getArgs().get(index);

        return parameter.getProvider().convert(arg);
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

    // Add parameter
    protected <T> Parameter<T> addParameter(Parameter<T> parameter) {
        this.getParameters().add(parameter);
        return parameter;
    }

    protected <T> Parameter<T> addParameter(T defaultValue, UltraProvider<T> provider, String name, String description) {
        return this.addParameter(new Parameter<>(defaultValue, provider, name, description));
    }

    protected <T> Parameter<T> addParameter(UltraProvider<T> provider, String name, String description) {
        return this.addParameter(new Parameter<>(provider, name, description));
    }

    protected <T> Parameter<T> addParameter(UltraProvider<T> provider, String name) {
        return this.addParameter(new Parameter<>(provider, name));
    }

    protected <T> Parameter<T> addParameter(T defaultValue, UltraProvider<T> provider, String name) {
        return this.addParameter(new Parameter<>(defaultValue, provider, name));
    }

    protected <T> Parameter<T> addParameter(UltraProvider<T> provider) {
        return this.addParameter(new Parameter<>(provider));
    }

    protected <T> Parameter<T> addParameter(T defaultValue, UltraProvider<T> provider) {
        return this.addParameter(new Parameter<>(defaultValue, provider));
    }

    protected boolean noParameterForIndex(int index) {
        if (index < 0) return true;
        return this.getParameters().size() <= index;
    }


    // Add aliases

    protected void addAlias(String alias) {
        this.aliases.add(alias.toLowerCase());
    }

    private void addAliases(String... aliases) {
        this.aliases.addAll(Arrays.stream(aliases).map(String::toLowerCase).collect(Collectors.toList()));
    }

    // Add subcommands

    public void addChildren(UltraCommand... commands) {
        this.children.addAll(Arrays.asList(commands));
        for (UltraCommand command : commands) {
            command.setParent(this);
            command.setHelpHeader(this.helpHeader);
            command.setHelpFooter(this.helpFooter);
            command.setHelpFormat(this.helpFormat);
        }
    }

    // ----------------------------------- //
    // UTILS
    // ----------------------------------- //

    protected void returnTell(String message) {
        throw new CommandException(message);
    }

    protected void tell(String message) {
        sender.sendMessage(TextUtil.comp(message));
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

    // ----------------------------------- //
    // COMMAND SETTINGS
    // ----------------------------------- //


    public String getDescription() {
        return this.description;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    public boolean hasChildren() {
        return !this.children.isEmpty();
    }

    public List<Parameter<?>> getParameters() {
        return parameters;
    }

    public void setParameters(final List<Parameter<?>> parameters) {
        this.parameters = parameters;
    }

    public Set<UltraCommand> getChildren() {
        return children;
    }

    public UltraSender getSender() {
        return sender;
    }

    public List<String> getArgs() {
        return args;
    }

    public boolean isAllowConsole() {
        return allowConsole;
    }

    public void setAllowConsole(final boolean allowConsole) {
        this.allowConsole = allowConsole;
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