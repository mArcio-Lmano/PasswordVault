package com.mano.passwordManager.storage;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.mano.passwordManager.util.Manager;

/**
 * Unit test for simple storage Class
 */
public class StorageTest {
    @Test
    public void testStorageSave() {
        // NOTE: No file named "testStorage.json" is saved
        Manager manager = new Manager("src/test/json/testStorage.json", "key");
        manager.addCredentials("FaryTail", "SnowWhite", "Happy");
        manager.addCredentials("FaryTail", "Darling", "1243125156141");
        manager.addCredentials("Rust", "Quantum", "ADAPT-VQC");

        Storage store1 = new Storage("src/test/json/testSave.json");
        store1.savePasswordsToStorage(manager.getDomains());

        // NOTE: No file named "testSave.json" is saved
        Storage store2 = new Storage("src/test/json/testSaveNop.json");

        assertEquals(true, store1.fileExists);
        assertEquals(false, store2.fileExists);
    }

    @Test
    public void testStorageRead() {
        Storage store1 = new Storage("src/test/json/testSave.json");
        Map<String, Map<String, String>> domains = store1.readPasswordsFromStorage();

        Storage store2 = new Storage("src/test/json/testRead.json");
        store2.savePasswordsToStorage(domains);
    }
}
