package dev.craftplugins.example.infrastructure;

import dev.craftingplugins.GetDataFolder;
import dev.craftingplugins.storage.type.MongoConfigurationElement;
import dev.craftplugins.example.ExamplePlugin;
import io.fairyproject.bukkit.listener.ListenerRegistry;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.configuration.Configuration;

@Configuration
public class PluginConfiguration {

    @InjectableComponent
    public GetDataFolder getDataFolder(ExamplePlugin plugin) {
        return plugin;
    }

    @InjectableComponent
    public ListenerRegistry listenerRegistry() {
        return new PluginListenerRegistry();
    }

    @InjectableComponent
    public MongoConfigurationElement mongoConfigurationElement() {
        return new MongoConfigurationElement(); // TODO: Configure this
    }

}
