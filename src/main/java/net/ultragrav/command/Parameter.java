package net.ultragrav.command;

import lombok.*;
import net.ultragrav.command.provider.UltraProvider;

import java.lang.reflect.ParameterizedType;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Parameter<T> {
	public static final String DEFAULT_DESC_DEFAULT = null;
	public static final Object DEFAULT_VALUE_DEFAULT = null;

	@Setter protected UltraProvider<T> provider;
	@Setter protected String name = null;
	@Setter protected String defaultDesc = null;
	protected T defaultValue = null;
	protected boolean defaultValueSet;
	@Setter protected boolean varArg;

	public Parameter(T defaultValue, UltraProvider<T> type, String name, String defaultDesc, boolean varArgs) {
		// Null checks
		if (type == null) throw new IllegalArgumentException("type mustn't be null");
		if (name == null) throw new IllegalArgumentException("name mustn't be null");

		this.setProvider(type);
		this.setName(name);
		this.setDefaultDesc(defaultDesc);
		this.setDefaultValue(defaultValue);
		this.setVarArg(varArgs);
	}

	public Parameter(T defaultValue, UltraProvider<T> type, String name, String defaultDesc) {
		this(defaultValue, type, name, defaultDesc, false);
	}

	@SuppressWarnings("unchecked")
	public Parameter(UltraProvider<T> type, String name, String defaultDesc, boolean varArgs) {
		this((T) DEFAULT_VALUE_DEFAULT, type, name, defaultDesc, varArgs);

		// In fact the default value is not set.
		this.defaultValueSet = false;
	}

	@SuppressWarnings("unchecked")
	public Parameter(UltraProvider<T> type, String name, String defaultDesc) {
		this((T) DEFAULT_VALUE_DEFAULT, type, name, defaultDesc);

		// In fact the default value is not set.
		this.defaultValueSet = false;
	}

	public Parameter(UltraProvider<T> type, String name) {
		this(type, name, DEFAULT_DESC_DEFAULT);
	}

	public Parameter(T defaultValue, UltraProvider<T> type, String name) {
		this(defaultValue, type, name, DEFAULT_DESC_DEFAULT);
	}

	public Parameter(UltraProvider<T> type, boolean varArgs) {
		this(type, type.getArgumentDescription(), DEFAULT_DESC_DEFAULT, varArgs);
	}

	public Parameter(UltraProvider<T> type) {
		this(type, type.getArgumentDescription(), DEFAULT_DESC_DEFAULT);
	}

	public Parameter(T defaultValue, UltraProvider<T> type) {
		this(defaultValue, type, type.getArgumentDescription());
	}


	// ----------------------------------- //
	// CHECK
	// ----------------------------------- //

	public boolean isRequired() {
		return !this.isDefaultValueSet();
	}

	public boolean isOptional() {
		return !this.isRequired();
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

		public Builder<T> varArg(boolean varArg) {
			this.varArg = varArg;
			return this;
		}

		public Parameter<T> build() {
			return new Parameter<>(provider, name, defaultDesc, defaultValue, defaultValueSet, varArg);
		}
	}
}