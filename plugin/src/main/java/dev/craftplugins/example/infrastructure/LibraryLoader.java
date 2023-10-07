package dev.craftplugins.example.infrastructure;

import io.fairyproject.Fairy;
import io.fairyproject.library.Library;
import io.fairyproject.log.Log;

public class LibraryLoader {

    public void load() {
        Log.info("Loading libraries... (This may take a while on first startup)");

        this.loadDatabaseLibrary();
    }

    private void loadDatabaseLibrary() {
        Fairy.getLibraryHandler().loadLibrary(Library.builder()
                .gradle("org.mongodb:mongo-java-driver:3.12.11")
                .build(), true);
    }

}
