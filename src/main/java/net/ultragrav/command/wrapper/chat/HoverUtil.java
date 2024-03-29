package net.ultragrav.command.wrapper.chat;

import net.ultragrav.chat.events.HoverEvent;

public class HoverUtil {
    /**
     * Generate a text hover
     *
     * @param str Text
     * @return Hover event with text
     */
    public static HoverEvent text(String str) {
        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextUtil.comp(str));
    }

//    /**
//     * Generate an entity hover
//     *
//     * @param entity Entity
//     * @return Hover event with entity
//     */
//    public static HoverEvent entity(Entity entity) {
//        return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new ComponentBuilder("{type:\"" + entity.getType().getName() + "\", id:\"" + entity.getUniqueId() + "\"}").create());
//    }

//    /**
//     * Generate cross-version item hover
//     * Note: This only shows name and lore,
//     * enchantments and attributes may not
//     * be shown
//     *
//     * @param item Item
//     * @return Hover event with item
//     */
//    public static HoverEvent itemNew(ItemStack item) {
//        net.minecraft.server.v1_12_R1.ItemStack it = CraftItemStack.asNMSCopy(item);
//        String text = it.getName();
//        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
//            text += "\n" + String.join("\n", item.getItemMeta().getLore());
//        }
//        return text(text);
//    }
}
