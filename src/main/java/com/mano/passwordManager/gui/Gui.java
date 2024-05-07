package com.mano.passwordManager.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.mano.passwordManager.util.Manager;
import com.mano.passwordManager.util.CredentialPanel;

public class Gui implements UserInterface {

    private JFrame frame;
    private JPanel actionsPanel;
    private JPanel domainButtonPanel;
    private JPanel contentPanel;
    // private CardLayout cardLayout;

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

        domainButtonPanel = new JPanel();
        domainButtonPanel.setLayout(new BoxLayout(domainButtonPanel, 1));
        domainButtonPanel.setBackground(Color.GRAY);

        contentPanel = new JPanel();
        contentPanel.setBackground(Color.LIGHT_GRAY);
        contentPanel.setLayout(new CardLayout());

        JScrollPane verticalScroll = new JScrollPane(contentPanel);
        verticalScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        Manager manager = new Manager("src/test/json/testPassWords.json", keyIn);
        Map<String, Map<String, String>> domains = manager.getDomains();

        for (Map.Entry<String, Map<String, String>> domain : domains.entrySet()) {
            String domain_name = domain.getKey();
            Map<String, String> credentials = domain.getValue();
            addButtonForDomain(domain_name, credentials, manager);
        }

        JPanel titlePanel = new JPanel();
        JLabel title = new JLabel("Password Vault");
        title.setFont(fontBold);
        title.setBorder(new EmptyBorder(5, 0, 5, 0));
        titlePanel.add(title);

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(domainButtonPanel, BorderLayout.WEST);
        containerPanel.add(verticalScroll, BorderLayout.CENTER);

        createActionsPanel(manager, frame);
        JPanel leftSidePanel = new JPanel(new BorderLayout());
        leftSidePanel.add(actionsPanel, BorderLayout.NORTH);
        leftSidePanel.add(containerPanel, BorderLayout.CENTER);

        JPanel rightSidePanel = new JPanel(new BorderLayout());
        rightSidePanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        rightSidePanel.add(titlePanel, BorderLayout.NORTH);
        rightSidePanel.add(contentPanel);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(leftSidePanel, BorderLayout.WEST);
        frame.getContentPane().add(rightSidePanel);

        frame.setVisible(true);
    }

    @Override
    public void update() {
        Manager manager = new Manager("src/test/json/testPassWords.json", key);
        Map<String, Map<String, String>> domains = manager.getDomains();

        contentPanel.removeAll();
        domainButtonPanel.removeAll();

        for (Map.Entry<String, Map<String, String>> domain : domains.entrySet()) {
            String domain_name = domain.getKey();
            Map<String, String> credentials = domain.getValue();
            addButtonForDomain(domain_name, credentials, manager);
        }

        contentPanel.revalidate();
        contentPanel.repaint();
        domainButtonPanel.revalidate();
        domainButtonPanel.repaint();
    }

    @Override
    public void close() {
        frame.dispose();
    }

    private JButton createQuitButton(JFrame frameToQuit) {
        JButton quitButton = new JButton("Quit");
        quitButton.setFont(fontBold);
        quitButton.setFocusPainted(false);
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameToQuit.dispose();
            }
        });
        return quitButton;
    }

    private void addButtonForDomain(String domain,
            final Map<String, String> credentials,
            Manager manager) {

        JButton domaiButton = new JButton(domain.strip());
        domaiButton.setFont(fontPlain);
        domaiButton.setBackground(Color.WHITE);
        domaiButton.setFocusPainted(false);
        domaiButton.setVerticalTextPosition(SwingConstants.CENTER);
        domaiButton.setHorizontalTextPosition(SwingConstants.CENTER);
        domaiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panelContentPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.insets = new Insets(5, 5, 5, 5);

                for (Map.Entry<String, String> credential : credentials.entrySet()) {
                    String username = credential.getKey();
                    String password = manager.getCredentials(domain, username);
                    JLabel label = new JLabel(
                            "<html><b>Username:</b> " + username + "<br><b>Password:</b> " + password + "</html>");
                    label.setFont(fontPlain);
                    ImageIcon staticIcon = new ImageIcon("assets/trash_static.gif");
                    ImageIcon gifIcon = new ImageIcon("assets/trash.gif");
                    JButton delete = new JButton(staticIcon);
                    delete.setBackground(Color.WHITE);
                    delete.setFocusPainted(false);
                    delete.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            delete.setIcon(gifIcon);
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            delete.setIcon(staticIcon);
                        }
                    });
                    delete.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            panelContentPanel.remove(label);
                            manager.removeCredentials(domain, username);
                            panelContentPanel.remove(delete);
                            manager.saveToFile();
                            update();
                        }
                    });
                    gbc.anchor = GridBagConstraints.WEST;
                    panelContentPanel.add(label, gbc);
                    gbc.gridx++;
                    panelContentPanel.add(delete, gbc);
                    gbc.anchor = GridBagConstraints.EAST;
                    gbc.gridy++;
                    gbc.gridx = 0;
                }
                contentPanel.removeAll();
                JScrollPane verticalScroll = new JScrollPane(panelContentPanel);
                verticalScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                verticalScroll.setBorder(BorderFactory.createEmptyBorder());
                contentPanel.add(verticalScroll, domain);
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });
        domainButtonPanel.add(domaiButton);
    }

    private void createActionsPanel(Manager manager, JFrame frameToQuit) {
        JButton plusButton = createAddCredentialButton(manager);
        JButton quitButton = createQuitButton(frameToQuit);

        actionsPanel = new JPanel();
        actionsPanel.add(plusButton);
        actionsPanel.add(quitButton);
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
                addCredentialButton.setFocusPainted(false);
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
                                    "One of the enteries is empty!",
                                    "",
                                    JOptionPane.ERROR_MESSAGE);
                        }

                    }
                });

                JButton quitButton = createQuitButton(addCredentialsFrame);

                JPanel addCredentialsButtonsPanel = new JPanel();
                addCredentialsButtonsPanel.setBackground(Color.GRAY);
                addCredentialsButtonsPanel.add(addCredentialButton);
                addCredentialsButtonsPanel.add(quitButton);

                addCredentialsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 424242, 5));
                addCredentialsPanel.add(domainPanel);
                addCredentialsPanel.add(userPanel);
                addCredentialsPanel.add(passPanel);
                addCredentialsPanel.add(addCredentialsButtonsPanel);

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
