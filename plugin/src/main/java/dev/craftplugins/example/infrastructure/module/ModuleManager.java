package dev.craftplugins.example.infrastructure.module;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.craftplugins.example.ExamplePlugin;
import io.fairyproject.Fairy;
import io.fairyproject.container.ContainerContext;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PostInitialize;
import io.fairyproject.container.node.ContainerNode;
import io.fairyproject.container.node.loader.ContainerNodeLoader;
import io.fairyproject.container.node.scanner.ContainerNodeClassScanner;
import io.fairyproject.util.CC;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

@InjectableComponent
public class ModuleManager {

    private static final Gson GSON = new Gson();
    private final ContainerContext context;
    private final ExamplePlugin plugin;
    @Getter
    private final List<Module> modules;
    private final Path path;

    public ModuleManager(ExamplePlugin plugin, ContainerContext context) {
        this.plugin = plugin;
        this.context = context;
        this.modules = new ArrayList<>();
        this.path = plugin.getDataFolder().resolve("modules");
    }

    @PostInitialize
    public void onPostInitialize() throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            return;
        }
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path, "*.jar")) {
            directoryStream.forEach(path -> {
                Module module;

                try {
                    module = this.loadPath(path);
                } catch (Exception e) {
                    e.printStackTrace();
                    Bukkit.getConsoleSender().sendMessage(CC.RED + "[Fairy] Failed to load module from jar " + path + ". (is it out-dated?)");
                    return;
                }

                if (module == null)
                    return;

                this.modules.add(module);
                Bukkit.getConsoleSender().sendMessage(CC.YELLOW + "[Fairy] Loaded module " + CC.WHITE + module.getName() + CC.YELLOW + ".");
            });
        }
    }

    private Module loadPath(Path path) throws IOException {
        Fairy.getPlatform().getClassloader().addPath(path);

        JsonObject jsonObject;
        try (JarFile jarFile = new JarFile(path.toFile())) {
            ZipEntry entry = jarFile.getEntry("module.json");
            if (entry == null) {
                Bukkit.getConsoleSender().sendMessage(CC.RED + "[Fairy] Jar " + path.getFileName() + " does not have a module.json file!");
                return null;
            }

            try (InputStreamReader reader = new InputStreamReader(jarFile.getInputStream(entry))) {
                jsonObject = GSON.fromJson(reader, JsonObject.class);
            }
        }

        Module module = new Module(
                jsonObject.get("name").getAsString(),
                jsonObject.get("description").getAsString()
        );

        String name = getFileName(path.getFileName().toString());
        ContainerNode node = ContainerNode.create(this.plugin.getName() + ":" + name);
        ContainerNodeClassScanner classScanner = new ContainerNodeClassScanner(context, this.plugin.getName() + ":" + name, node);
        classScanner.getClassLoaders().add(this.plugin.getPluginClassLoader());
        classScanner.getClassPaths().add("*");
        classScanner.getUrls().add(path.toUri().toURL());
        classScanner.scan();

        new ContainerNodeLoader(context, node).load();

        return module;
    }

    private String getFileName(String fileName) {
        if (fileName == null) {
            return null;
        }
        int extensionPos = fileName.lastIndexOf('.');
        int lastUnixPos = fileName.lastIndexOf('/');
        int lastWindowsPos = fileName.lastIndexOf('\\');
        int lastSeparator = Math.max(lastUnixPos, lastWindowsPos);
        int index = lastSeparator > extensionPos ? -1 : extensionPos;
        if (index == -1) {
            return fileName;
        } else {
            return fileName.substring(0, index);
        }
    }

}
