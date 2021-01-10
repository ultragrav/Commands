package net.ultragrav.command.registry;

@SuppressWarnings("unchecked")
public class RegistryManager {
    private static Registry activeRegistry;

    private static Registry createRegistry() {
        String rgn = RegistryManager.class.getPackage().getName();
        try {
            Class.forName("org.bukkit.Bukkit");
            Class<Registry> spReg = (Class<Registry>) Class.forName(rgn + ".spigot.RegistrySpigot");
            return spReg.newInstance();
        } catch (ClassNotFoundException ignored) {
        } catch (IllegalAccessException | InstantiationException e) {
            System.out.println("Could not instantiate Spigot registry");
            e.printStackTrace();
        }
        try {
            Class.forName("net.md_5.bungee.api.ProxyServer");
            Class<Registry> spReg = (Class<Registry>) Class.forName(rgn + ".bungee.RegistryBungee");
            return spReg.newInstance();
        } catch (ClassNotFoundException ignored) {
        } catch (IllegalAccessException | InstantiationException e) {
            System.out.println("Could not instantiate Bungee registry");
            e.printStackTrace();
        }
        try {
            Class.forName("com.velocitypowered.api.proxy.ProxyServer");
            Class<Registry> spReg = (Class<Registry>) Class.forName(rgn + ".velocity.RegistryVelocity");
            return (Registry) spReg.getField("instance").get(null);
        } catch (ClassNotFoundException ignored) {
        } catch (IllegalAccessException | NoSuchFieldException e) {
            System.out.println("Could not instantiate Bungee registry");
            e.printStackTrace();
        }
        throw new RuntimeException("Could not create a registry in the current environment, are you on a supported platform?");
    }

    public static Registry getCurrentRegistry() {
        if (activeRegistry == null) {
            activeRegistry = createRegistry();
        }
        return activeRegistry;
    }
}
