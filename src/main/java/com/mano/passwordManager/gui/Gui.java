package com.mano.passwordManager.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
// import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mano.passwordManager.util.Manager;

public class Gui implements UserInterface {

    private JFrame frame;
    private JPanel buttonPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    @Override
    public void initialize() {
        frame = new JFrame("Password Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buttonPanel = new JPanel(new GridLayout(0, 2, 10, 20));
        buttonPanel.setBackground(Color.GRAY);
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.LIGHT_GRAY);
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        Manager manager = new Manager("src/test/json/testPassWords.json");
        Map<String, Map<String, String>> domains = manager.getDomains();

        for (Map.Entry<String, Map<String, String>> domain : domains.entrySet()) {
            String domain_name = domain.getKey();
            Map<String, String> credentials = domain.getValue();
            addButton(domain_name, credentials);
        }

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(buttonPanel, BorderLayout.WEST);
        frame.getContentPane().add(contentPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void addButton(String buttonText, final Map<String, String> credentials) {
        JButton button = new JButton(buttonText);
        button.setFont(new Font("AppleGothic", Font.BOLD, 14));
        button.setBackground(Color.WHITE);
        button.setBorderPainted(true);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panelContentPanel = new JPanel(new GridLayout(0, 1));
                for (Map.Entry<String, String> credential : credentials.entrySet()) {
                    String username = credential.getKey();
                    String password = credential.getValue();
                    JLabel label = new JLabel("Username: " + username + ", Password: " + password);
                    label.setFont(new Font("AppleGothic", Font.BOLD, 14));
                    panelContentPanel.add(label);
                }

                contentPanel.add(panelContentPanel, buttonText);
                contentPanel.validate();
                cardLayout.show(contentPanel, buttonText);
            }
        });
        buttonPanel.add(button);

    }

    @Override
    public void update() {
        // TODO: MAke the UPDATE LOGIC IF NEEDED
    }

    @Override
    public void close() {
        frame.dispose();
    }
}
