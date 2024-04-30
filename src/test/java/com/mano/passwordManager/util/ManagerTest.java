package com.mano.passwordManager.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Unit test for simple manager Class
 */
public class ManagerTest {
    @Test
    public void testNewManager() {
        // NOTE: No file "testSave.json" is saved
        Manager manager = new Manager("src/test/json/testSave.json");
        Integer numOfBytes = manager.testKeys();

        assertSame(32, numOfBytes);
    }

    @Test
    public void testEncriptions() {
        // NOTE: No file "testSave.json" is saved
        Manager manager = new Manager("src/test/json/testSave.json");

        assertEquals("test", manager.testEncription("test"));
        assertEquals("JavaIsNotAnHardLanguage",
                manager.testEncription("JavaIsNotAnHardLanguage"));
        assertEquals("Surf Curse", manager.testEncription("Surf Curse"));
    }

    @Test
    public void testAddDomain() {
        // NOTE: No file "testSave.json" is saved
        Manager manager = new Manager("src/test/json/testSave.json");
        manager.addCredentials("FaryTail", "SnowWhite", "Happy");
        manager.addCredentials("FaryTail", "Darling", "1243125156141");
        manager.addCredentials("Rust", "Quantum", "ADAPT-VQC");

        assertEquals("Happy", manager.getCredentials("FaryTail", "SnowWhite"));
        assertEquals("1243125156141",
                manager.getCredentials("FaryTail", "Darling"));
        assertEquals("ADAPT-VQC", manager.getCredentials("Rust", "Quantum"));
    }

    @Test
    public void testLoadFile() {
        // NOTE: No file "testSave.json" is saved
        Manager manager = new Manager("src/test/json/testSave.json");

        assertEquals("Happy", manager.getCredentials("FaryTail", "SnowWhite"));
        assertEquals("1243125156141",
                manager.getCredentials("FaryTail", "Darling"));
        assertEquals("ADAPT-VQC", manager.getCredentials("Rust", "Quantum"));
    }

    @Test
    public void testSaveToFile() {
        Manager manager = new Manager("src/test/json/testPassWords.json");

        manager.addCredentials("SouthPark", "Kenny", "Orange");
        manager.addCredentials("Python", "Shor", "RSA");
        manager.addCredentials("Python", "OOP", "Class");
        manager.addCredentials("Python", "Grove", "Quantum");
        manager.addCredentials("FaryTail", "SnowWhite", "Happy");
        manager.addCredentials("FaryTail", "Darling", "1243125156141");
        manager.addCredentials("Rust", "Quantum", "ADAPT-VQC");

        assertEquals("Happy", manager.getCredentials("FaryTail", "SnowWhite"));
        assertEquals("1243125156141",
                manager.getCredentials("FaryTail", "Darling"));
        assertEquals("ADAPT-VQC", manager.getCredentials("Rust", "Quantum"));

        manager.saveToFile();
    }
}
