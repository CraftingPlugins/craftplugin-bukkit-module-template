package dev.craftplugins.example.infrastructure;

import io.fairyproject.Fairy;
import io.fairyproject.library.Library;
import io.fairyproject.library.relocate.Relocation;
import io.fairyproject.log.Log;

public class LibraryLoader {


    public void load() {
        Log.info("Loading libraries... (This may take a while on first startup)");

        this.loadDatabaseLibrary();
        this.loadConfigurateLibrary();
    }

    private void loadDatabaseLibrary() {
        Fairy.getLibraryHandler().loadLibrary(Library.builder()
                .gradle("org.mongodb:mongo-java-driver:3.12.11")
                .build(), true);
    }

    private void loadConfigurateLibrary() {
        Fairy.getLibraryHandler().loadLibrary(Library.builder()
                .gradle("org{}spongepowered:configurate-core:4.1.2")
                .build(), true, Relocation.of("org{}spongepowered{}configurate", "dev.ghast.bossraid.libs.configurate"));
        Fairy.getLibraryHandler().loadLibrary(Library.builder()
                .gradle("org{}spongepowered:configurate-yaml:4.1.2")
                .build(), true, Relocation.of("org{}spongepowered{}configurate", "dev.ghast.bossraid.libs.configurate"));
    }

}
