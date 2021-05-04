package net.ultragrav.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.ultragrav.command.parameters.ParameterCondition;
import net.ultragrav.command.provider.UltraProvider;
import net.ultragrav.command.wrapper.sender.UltraSender;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Parameter<T> {
    public static final String DEFAULT_DESC_DEFAULT = null;
    public static final Object DEFAULT_VALUE_DEFAULT = null;

    @Setter
    protected UltraProvider<T> provider;
    @Setter
    protected String name = null;
    @Setter
    protected String defaultDesc = null;
    protected T defaultValue = null;
    protected boolean defaultValueSet;
    @Setter
    protected boolean varArg;

    protected List<ParameterCondition<T>> conditions;

    // ----------------------------------- //
    // CHECK
    // ----------------------------------- //

    public boolean isRequired() {
        return !this.isOptional();
    }

    public boolean isOptional() {
        return this.isDefaultValueSet();
    }

    public T convert(String str, UltraSender sender) {
        T obj = provider.convert(str, sender);

        for (ParameterCondition<T> condition : conditions) {
            condition.confirm(obj);
        }

        return obj;
    }

    // ----------------------------------- //
    // GET
    // ----------------------------------- //

    public void setDefaultValue(final T defaultValue) {
        this.defaultValue = defaultValue;
        this.defaultValueSet = true;
    }

    public static <T> Builder<T> builder(UltraProvider<T> provider) {
        return new Builder<>(provider);
    }

    public static class Builder<T> {
        private UltraProvider<T> provider;
        private String name;
        private String defaultDesc = DEFAULT_DESC_DEFAULT;
        private T defaultValue = (T) DEFAULT_VALUE_DEFAULT;
        private boolean defaultValueSet = false;
        private boolean varArg = false;
        private List<ParameterCondition<T>> conditions = new ArrayList<>();

        public Builder(UltraProvider<T> provider) {
            this.provider = provider;
            this.name = provider.getArgumentDescription();
        }

        public Builder<T> name(String name) {
            this.name = name;
            return this;
        }

        public Builder<T> desc(String defaultDesc) {
            this.defaultDesc = defaultDesc;
            return this;
        }

        public Builder<T> defaultValue(T def) {
            this.defaultValue = def;
            this.defaultValueSet = true;
            return this;
        }

        /**
         * Add a condition
         *
         * @param condition Condition
         * @param message   Message if condition is not met
         * @return {@code this}
         */
        public Builder<T> addCondition(Function<T, Boolean> condition, String message) {
            conditions.add(new ParameterCondition<>(condition, message));
            return this;
        }

        /**
         * Set whether this Parameter is a vararg
         * Note: This only works on the last parameter of a command
         *
         * @param varArg Is vararg
         * @return {@code this}
         */
        public Builder<T> varArg(boolean varArg) {
            this.varArg = varArg;
            return this;
        }

        public Parameter<T> build() {
            return new Parameter<>(provider, name, defaultDesc, defaultValue, defaultValueSet, varArg, conditions);
        }
    }
}