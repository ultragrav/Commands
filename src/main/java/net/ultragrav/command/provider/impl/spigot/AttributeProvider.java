package net.ultragrav.command.provider.impl.spigot;

import lombok.Getter;
import net.ultragrav.command.provider.impl.EnumProvider;
import org.bukkit.attribute.Attribute;

public class AttributeProvider extends EnumProvider<Attribute> {
    @Getter
    private static AttributeProvider instance = new AttributeProvider();

    private AttributeProvider() {
        super(Attribute.class, true);
    }

    @Override
    public String getArgumentDescription() {
        return "attribute";
    }
}
