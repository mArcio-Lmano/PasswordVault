package com.mano.passwordManager.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.mano.passwordManager.util.Manager;

public class Gui implements UserInterface {

    private JFrame frame;
    private JPanel buttonPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    @Override
    public void initialize(String key) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        UIManager.put("swing.aatext", true);
        UIManager.put("AATextPropertyKey", true);

        frame = new JFrame("Password Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, 1));
        buttonPanel.setBackground(Color.GRAY);

        contentPanel = new JPanel();
        contentPanel.setBackground(Color.LIGHT_GRAY);

        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        JScrollPane verticalScroll = new JScrollPane(contentPanel);
        verticalScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        Manager manager = new Manager("src/test/json/testPassWords.json", key);
        // BUG: Decrition not working the way this was defined
        Map<String, Map<String, String>> domains = manager.getDomains();

        for (Map.Entry<String, Map<String, String>> domain : domains.entrySet()) {
            String domain_name = domain.getKey();
            Map<String, String> credentials = domain.getValue();
            addButton(domain_name, credentials, manager);
        }
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(buttonPanel, BorderLayout.WEST);
        containerPanel.add(verticalScroll, BorderLayout.CENTER);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(containerPanel, BorderLayout.WEST);
        frame.getContentPane().add(contentPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    @Override
    public void update() {
        // TODO: MAke the UPDATE LOGIC IF NEEDED
    }

    @Override
    public void close() {
        frame.dispose();
    }

    private void addButton(String domain,
            final Map<String, String> credentials,
            Manager manager) {
        JButton button = new JButton(domain.strip());
        button.setFont(new Font("JetBrainsMono NF", Font.BOLD, 20));
        button.setBackground(Color.WHITE);
        button.setBorderPainted(true);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setHorizontalTextPosition(SwingConstants.CENTER);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panelContentPanel = new JPanel(new GridLayout(0, 1));
                for (Map.Entry<String, String> credential : credentials.entrySet()) {
                    String username = credential.getKey();
                    String password = manager.getCredentials(domain, username);
                    // String password = credential.getValue();
                    JLabel label = new JLabel(
                            "<html><b>Username:</b> " + username + "<br><b>Password:</b> " + password + "</html>");
                    label.setFont(new Font("JetBrainsMono NF", Font.PLAIN, 20));
                    panelContentPanel.add(label);
                }

                contentPanel.add(panelContentPanel, domain);
                contentPanel.revalidate();
                cardLayout.show(contentPanel, domain);
            }
        });
        buttonPanel.add(button);

    }
}
