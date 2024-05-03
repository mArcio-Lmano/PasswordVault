package com.mano.passwordManager.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.mano.passwordManager.util.Manager;

public class Gui implements UserInterface {

    private JFrame frame;
    private JPanel actionsPanel;
    private JPanel buttonPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    private Font fontPlain = new Font("JetBrainsMono NF", Font.PLAIN, 20);
    private Font fontBold = new Font("JetBrainsMono NF", Font.BOLD, 20);
    private String key;

    @Override
    public void initialize(String keyIn) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        UIManager.put("swing.aatext", true);
        UIManager.put("AATextPropertyKey", true);

        key = keyIn;
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

        Manager manager = new Manager("src/test/json/testPassWords.json", keyIn);
        Map<String, Map<String, String>> domains = manager.getDomains();

        for (Map.Entry<String, Map<String, String>> domain : domains.entrySet()) {
            String domain_name = domain.getKey();
            Map<String, String> credentials = domain.getValue();
            addButtonForDomain(domain_name, credentials, manager);
        }
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(buttonPanel, BorderLayout.WEST);
        containerPanel.add(verticalScroll, BorderLayout.CENTER);

        createActionsPanel(manager);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(actionsPanel, BorderLayout.NORTH);
        frame.getContentPane().add(containerPanel, BorderLayout.WEST);
        frame.getContentPane().add(contentPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    @Override
    public void update() {
        Manager manager = new Manager("src/test/json/testPassWords.json", key);
        Map<String, Map<String, String>> domains = manager.getDomains();

        buttonPanel.removeAll();

        for (Map.Entry<String, Map<String, String>> domain : domains.entrySet()) {
            String domain_name = domain.getKey();
            Map<String, String> credentials = domain.getValue();
            addButtonForDomain(domain_name, credentials, manager);
        }

        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    @Override
    public void close() {
        frame.dispose();
    }

    private void addButtonForDomain(String domain,
            final Map<String, String> credentials,
            Manager manager) {

        JButton button = new JButton(domain.strip());
        button.setFont(fontPlain);
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
                    JLabel label = new JLabel(
                            "<html><b>Username:</b> " + username + "<br><b>Password:</b> " + password + "</html>");
                    label.setFont(fontPlain);
                    panelContentPanel.add(label);
                }

                contentPanel.add(panelContentPanel, domain);
                contentPanel.revalidate();
                cardLayout.show(contentPanel, domain);
            }
        });
        buttonPanel.add(button);

    }

    private void createActionsPanel(Manager manager) {
        JButton plusButton = createAddCredentialButton(manager);
        JButton minusButton = new JButton("-");
        minusButton.setFont(fontPlain);
        minusButton.setFocusPainted(false);

        actionsPanel = new JPanel();
        actionsPanel.add(plusButton);
        actionsPanel.add(minusButton);
    }

    private JButton createAddCredentialButton(Manager manager) {
        JButton plusButton = new JButton("+");
        plusButton.setFont(fontPlain);
        plusButton.setFocusPainted(false);
        plusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame addCredentialsFrame = new JFrame("Add Credential");
                JPanel addCredentialsPanel = new JPanel();
                addCredentialsPanel.setBackground(Color.GRAY);

                CredentialPanel domainCredentialPanel = createCredentialPanel("Domain");
                CredentialPanel userCredentialPanel = createCredentialPanel("Username");
                CredentialPanel passCredentialPanel = createCredentialPanel("Password");

                JPanel domainPanel = domainCredentialPanel.getJPanel();
                JPanel userPanel = userCredentialPanel.getJPanel();
                JPanel passPanel = passCredentialPanel.getJPanel();

                JButton addCredentialButton = new JButton("Add");
                addCredentialButton.setFont(fontBold);
                addCredentialButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String domain = domainCredentialPanel.getJTextField().getText();
                        String username = userCredentialPanel.getJTextField().getText();
                        String password = passCredentialPanel.getJTextField().getText();
                        if (!domain.equals("") && !username.equals("") && !password.equals("")) {
                            manager.addCredentials(domain, username, password);
                            manager.saveToFile();
                            addCredentialsFrame.dispose();
                            update();

                        } else {
                            JOptionPane.showMessageDialog(frame,
                                    "Eggs are not supposed to be green.",
                                    "Inane error",
                                    JOptionPane.ERROR_MESSAGE);
                        }

                    }
                });

                addCredentialsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 424242, 5));
                addCredentialsPanel.add(domainPanel);
                addCredentialsPanel.add(userPanel);
                addCredentialsPanel.add(passPanel);
                addCredentialsPanel.add(addCredentialButton);

                addCredentialsFrame.getContentPane().add(addCredentialsPanel);
                addCredentialsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                addCredentialsFrame.setVisible(true);

            }
        });

        return plusButton;
    }

    private CredentialPanel createCredentialPanel(String credential) {
        JTextField credentialTextField = new JTextField();
        credentialTextField.setFont(fontPlain);
        JLabel credentialLabel = new JLabel(credential + ": ");
        credentialLabel.setFont(fontBold);
        credentialTextField.setColumns(20);
        JPanel credentialPanel = new JPanel();
        credentialPanel.add(credentialLabel);
        credentialPanel.add(credentialTextField);

        return new CredentialPanel(credentialPanel, credentialTextField);
    }
}

class CredentialPanel {
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
