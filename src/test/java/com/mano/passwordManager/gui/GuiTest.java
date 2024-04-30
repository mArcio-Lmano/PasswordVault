package com.mano.passwordManager.gui;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.mano.passwordManager.util.Manager;

/**
 * Unit test for simple storage Class
 */
public class GuiTest {
    @Test
    public void testInitialize() {
        Gui gui = new Gui();
        gui.initialize();
    }
}
