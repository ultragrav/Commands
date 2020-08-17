package net.ultragrav.command.provider.impl.spigot;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;
import org.bukkit.Material;

import java.util.List;

public final class MaterialProvider extends UltraProvider<Material> {
    @Getter
    private static final MaterialProvider instance = new MaterialProvider();

    @Override
    public Material convert(@NonNull final String toConvert) throws CommandException {
        try {
            return Material.valueOf(toConvert);
        } catch (NumberFormatException exception) {
            throw new CommandException("Required: material, provided '" + toConvert + "'");
        }
    }

    @Override
    public List<String> tabComplete(@NonNull final String toComplete) {
        List<String> toSend = Lists.newArrayList();

        for (Material mat : Material.values())
            if (mat.name().toLowerCase().startsWith(toComplete))
                toSend.add(mat.name());

        return toSend;
    }

    @Override
    public String getArgumentDescription() {
        return "material";
    }
}