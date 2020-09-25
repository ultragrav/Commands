package net.ultragrav.command;

import net.ultragrav.command.provider.UltraProvider;

public class Parameter<T> {
	public static final String DEFAULT_DESC_DEFAULT = null;
	public static final Object DEFAULT_VALUE_DEFAULT = null;

	protected UltraProvider<T> provider;
	protected String name = null;
	protected String defaultDesc = null;
	protected T defaultValue = null;
	protected boolean defaultValueSet = false;

	public Parameter(T defaultValue, UltraProvider<T> type, String name, String defaultDesc) {
		// Null checks
		if (type == null) throw new IllegalArgumentException("type mustn't be null");
		if (name == null) throw new IllegalArgumentException("name mustn't be null");

		this.setProvider(type);
		this.setName(name);
		this.setDefaultDesc(defaultDesc);
		this.defaultValueSet = true;
		this.setDefaultValue(defaultValue);
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
		return this.isDefaultValueSet();
	}

	public boolean isOptional() {
		return !this.isRequired();
	}


	// ----------------------------------- //
	// GET
	// ----------------------------------- //

	public UltraProvider<T> getProvider() {
		return provider;
	}

	public void setProvider(final UltraProvider<T> provider) {
		this.provider = provider;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDefaultDesc() {
		return defaultDesc;
	}

	public void setDefaultDesc(final String defaultDesc) {
		this.defaultDesc = defaultDesc;
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(final T defaultValue) {
		this.defaultValue = defaultValue;
		this.defaultValueSet = true;
	}

	public boolean isDefaultValueSet() {
		return defaultValueSet;
	}
}