package net.ultragrav.command.provider.impl.spigot;

import lombok.Getter;
import net.ultragrav.command.provider.impl.EnumProvider;
import org.bukkit.Material;

public final class MaterialProvider extends EnumProvider<Material> {
    @Getter
    private static final MaterialProvider instance = new MaterialProvider();

    private MaterialProvider() {
        super(Material.class);
    }

    @Override
    public String getArgumentDescription() {
        return "material";
    }
}