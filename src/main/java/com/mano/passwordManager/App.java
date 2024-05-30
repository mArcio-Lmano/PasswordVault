package com.mano.passwordManager;

import java.awt.GraphicsEnvironment;

import com.mano.passwordManager.gui.Gui;
import com.mano.passwordManager.util.Login;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        Login login = new Login();
        if (login.key == null) {
            System.out.println("Closing App, Wrong Password");
            return;
        }

        Gui gui = new Gui();

        gui.initialize(login.key);
    }
}
