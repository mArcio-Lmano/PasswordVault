package com.mano.passwordManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Unit test for simple manager Class
 */
public class ManagerTest {
    @Test
    public void testNewManager() {
        Manager manager = new Manager();
        Integer numOfBytes = manager.testKeys();

        assertSame(32, numOfBytes);
    }

    @Test
    public void testEncriptions() {
        Manager manager = new Manager();

        assertEquals("test", manager.testEncription("test"));
        assertEquals("JavaIsNotAnHardLanguage",
                manager.testEncription("JavaIsNotAnHardLanguage"));
        assertEquals("Surf Curse", manager.testEncription("Surf Curse"));
    }

    @Test
    public void testAddDomain() {
        Manager manager = new Manager();
        manager.addCredentials("FaryTail", "SnowWhite", "Happy");
        manager.addCredentials("FaryTail", "Darling", "1243125156141");
        manager.addCredentials("Rust", "Quantum", "ADAPT-VQC");

        assertEquals("Happy", manager.getCredentials("FaryTail", "SnowWhite"));
        assertEquals("1243125156141",
                manager.getCredentials("FaryTail", "Darling"));
        assertEquals("ADAPT-VQC", manager.getCredentials("Rust", "Quantum"));
    }
}
