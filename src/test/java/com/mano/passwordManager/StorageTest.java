package com.mano.passwordManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Map;

import org.junit.Test;

/**
 * Unit test for simple storage Class
 */
public class StorageTest {
    @Test
    public void testStorageSave() {
        Manager manager = new Manager();
        manager.addCredentials("FaryTail", "SnowWhite", "Happy");
        manager.addCredentials("FaryTail", "Darling", "1243125156141");
        manager.addCredentials("Rust", "Quantum", "ADAPT-VQC");

        Storage store1 = new Storage("src/test/testSave.json");
        store1.savePasswordsToStorage(manager.getDomains());

        Storage store2 = new Storage("testSave.json");

        assertEquals(true, store1.fileExists);
        assertEquals(false, store2.fileExists);
    }

    @Test
    public void testStorageRead() {
        Storage store1 = new Storage("src/test/testSave.json");
        Map<String, Map<String, String>> domains = store1.readPasswordsFromStorage();

        Storage store2 = new Storage("testRead.json");
        store2.savePasswordsToStorage(domains);
    }
}
