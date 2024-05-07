package com.mano.passwordManager.util;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class CredentialPanel {
    private JPanel panel;
    private JTextField textField;

    public CredentialPanel(JPanel panelIn, JTextField textFieldIn) {
        panel = panelIn;
        textField = textFieldIn;
    }

    public JPanel getJPanel() {
        return panel;
    }

    public JTextField getJTextField() {
        return textField;
    }
}
