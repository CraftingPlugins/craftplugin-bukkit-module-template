package dev.craftplugins.example;

import dev.craftingplugins.GetDataFolder;
import dev.craftplugins.example.infrastructure.LibraryLoader;
import io.fairyproject.FairyLaunch;
import io.fairyproject.plugin.Plugin;
import org.bukkit.Bukkit;

@FairyLaunch
public class ExamplePlugin extends Plugin implements GetDataFolder {

    @Override
    public void onInitial() {
        requireCraftCore();

        new LibraryLoader().load();
    }

    private void requireCraftCore() {
        String requiredVersion = "1.2.0";
        String[] requiredSplit = requiredVersion.split("\\.");

        int major = Integer.parseInt(requiredSplit[0]);
        int minor = Integer.parseInt(requiredSplit[1]);
        int build = Integer.parseInt(requiredSplit[2]);

        org.bukkit.plugin.Plugin plugin = Bukkit.getPluginManager().getPlugin("CraftCore");
        if (plugin == null)
            throw new IllegalStateException("CraftCore is not installed. Please install CraftCore.");

        String version = plugin.getDescription().getVersion();

        String[] split = version.split("\\.");
        int currentMajor = Integer.parseInt(split[0]);
        int currentMinor = Integer.parseInt(split[1]);
        int currentBuild = Integer.parseInt(split[2]);

        if (currentMajor > major)
            return;

        if (currentMinor > minor)
            return;

        if (currentBuild > build)
            return;

        if (currentMajor < major || currentMinor < minor || currentBuild < build)
            throw new IllegalStateException("CraftCore version " + 1 + "." + minor + "." + build + " or higher is required. Please update CraftCore.");
    }

}