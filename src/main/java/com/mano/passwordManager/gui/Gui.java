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

    private Font fontPlain = new Font("JetBrainsMono NF", Font.PLAIN, 20);
    private Font fontBold = new Font("JetBrainsMono NF", Font.BOLD, 20);
    private Color colorBlack = new Color(34, 40, 49);
    private Color colorGray = new Color(49, 54, 63);
    private Color colorTeal = new Color(118, 171, 174);
    private Color colorWhite = new Color(238, 238, 238);
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
        domainButtonPanel.setBackground(colorGray);

        contentPanel = new JPanel();
        contentPanel.setBackground(colorBlack);
        contentPanel.setLayout(new CardLayout());

        JScrollPane verticalScroll = new JScrollPane(contentPanel);
        verticalScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        verticalScroll.getVerticalScrollBar().setUnitIncrement(16);

        Manager manager = new Manager("src/test/json/testPassWords.json", keyIn);
        Map<String, Map<String, String>> domains = manager.getDomains();

        for (Map.Entry<String, Map<String, String>> domain : domains.entrySet()) {
            String domain_name = domain.getKey();
            Map<String, String> credentials = domain.getValue();
            addButtonForDomain(domain_name, credentials, manager);
        }

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(colorGray);
        JLabel title = new JLabel("Password Vault");
        title.setFont(fontBold);
        title.setBorder(new EmptyBorder(5, 0, 5, 0));
        title.setForeground(colorWhite);
        titlePanel.add(title);

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(colorGray);
        containerPanel.add(domainButtonPanel, BorderLayout.WEST);
        containerPanel.add(verticalScroll, BorderLayout.CENTER);

        createActionsPanel(manager, frame);
        JPanel leftSidePanel = new JPanel(new BorderLayout());
        leftSidePanel.setBackground(colorGray);
        leftSidePanel.add(actionsPanel, BorderLayout.NORTH);
        leftSidePanel.add(containerPanel, BorderLayout.CENTER);

        JPanel rightSidePanel = new JPanel(new BorderLayout());
        rightSidePanel.setBackground(colorGray);
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
        quitButton.setBackground(colorTeal);
        quitButton.setForeground(colorWhite);
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
        domaiButton.setBackground(colorTeal);
        domaiButton.setFocusPainted(false);
        domaiButton.setForeground(colorWhite);
        domaiButton.setVerticalTextPosition(SwingConstants.CENTER);
        domaiButton.setHorizontalTextPosition(SwingConstants.CENTER);
        domaiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panelContentPanel = new JPanel(new GridBagLayout());
                panelContentPanel.setBackground(colorBlack);
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.insets = new Insets(5, 5, 5, 5);

                for (Map.Entry<String, String> credential : credentials.entrySet()) {
                    domainButtonAction(domain,
                            credential,
                            manager,
                            panelContentPanel,
                            gbc);
                }
                contentPanel.removeAll();
                JScrollPane verticalScroll = new JScrollPane(panelContentPanel);
                verticalScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                verticalScroll.getVerticalScrollBar().setUnitIncrement(16);
                verticalScroll.setBorder(BorderFactory.createEmptyBorder());
                contentPanel.add(verticalScroll, domain);
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });
        domainButtonPanel.add(domaiButton);
    }

    private void domainButtonAction(String domain,
            Map.Entry<String, String> credential,
            Manager manager,
            JPanel panelContentPanel,
            GridBagConstraints gbcMainPanel) {
        JPanel credentialPanel = new JPanel(new GridBagLayout());
        credentialPanel.setBackground(colorBlack);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        String username = credential.getKey();
        String password = manager.getCredentials(domain, username);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(fontBold);
        usernameLabel.setForeground(colorWhite);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(fontBold);
        passwordLabel.setForeground(colorWhite);

        JTextField usernameEntry = new JTextField(username);
        usernameEntry.setEditable(false);
        usernameEntry.setFont(fontPlain);
        JTextField passwordEntry = new JTextField(password);
        passwordEntry.setEditable(false);
        passwordEntry.setFont(fontPlain);

        credentialPanel.add(usernameLabel, gbc);
        gbc.gridx += 1;
        credentialPanel.add(usernameEntry, gbc);
        gbc.gridx = 0;
        gbc.gridy += 1;
        credentialPanel.add(passwordLabel, gbc);
        gbc.gridx += 1;
        credentialPanel.add(passwordEntry, gbc);

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
                panelContentPanel.remove(credentialPanel);
                manager.removeCredentials(domain, username);
                panelContentPanel.remove(delete);
                manager.saveToFile();
                update();
            }
        });
        gbcMainPanel.anchor = GridBagConstraints.WEST;
        panelContentPanel.add(credentialPanel, gbcMainPanel);
        gbcMainPanel.gridx++;
        panelContentPanel.add(delete, gbcMainPanel);
        gbcMainPanel.anchor = GridBagConstraints.EAST;
        gbcMainPanel.gridy++;
        gbcMainPanel.gridx = 0;
    }

    private void createActionsPanel(Manager manager, JFrame frameToQuit) {
        JButton plusButton = createAddCredentialButton(manager);
        JButton quitButton = createQuitButton(frameToQuit);

        actionsPanel = new JPanel();
        actionsPanel.setBackground(colorGray);
        actionsPanel.add(plusButton);
        actionsPanel.add(quitButton);
    }

    private JButton createAddCredentialButton(Manager manager) {
        JButton plusButton = new JButton("+");
        plusButton.setFont(fontPlain);
        plusButton.setFocusPainted(false);
        plusButton.setBackground(colorTeal);
        plusButton.setForeground(colorWhite);
        plusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCredentialButtonAction(manager);
            }
        });

        return plusButton;
    }

    private void addCredentialButtonAction(Manager manager) {

        JFrame addCredentialsFrame = new JFrame("Add Credential");
        JPanel addCredentialsPanel = new JPanel();
        addCredentialsPanel.setBackground(colorBlack);

        CredentialPanel domainCredentialPanel = createCredentialPanel("Domain");
        CredentialPanel userCredentialPanel = createCredentialPanel("Username");
        CredentialPanel passCredentialPanel = createCredentialPanel("Password");

        JPanel domainPanel = domainCredentialPanel.getJPanel();
        domainPanel.setBackground(colorBlack);
        JPanel userPanel = userCredentialPanel.getJPanel();
        userPanel.setBackground(colorBlack);
        JPanel passPanel = passCredentialPanel.getJPanel();
        passPanel.setBackground(colorBlack);

        JButton addCredentialButton = new JButton("Add");
        addCredentialButton.setFont(fontBold);
        addCredentialButton.setFocusPainted(false);
        addCredentialButton.setBackground(colorTeal);
        addCredentialButton.setForeground(colorWhite);
        addCredentialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String domain = domainCredentialPanel.getJTextField().getText();
                String username = userCredentialPanel.getJTextField().getText();
                String password = passCredentialPanel.getJTextField().getText();
                if (!domain.equals("") && !username.equals("") && !password.equals("")) {
                    manager.addCredentials(domain, username, password);
                    if (manager.overrideFlag == 1) {
                        int dialogButton = JOptionPane.YES_NO_OPTION;
                        int dialogResult = JOptionPane.showConfirmDialog(frame,
                                "Would You Like to override the previous password?", "Warning", dialogButton);
                        if (dialogResult == JOptionPane.YES_OPTION) {
                            manager.addCredentials(domain, username, password);
                        } else {
                            manager.overrideFlag = 0;
                        }
                    }
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
        addCredentialsButtonsPanel.setBackground(colorBlack);
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

    private CredentialPanel createCredentialPanel(String credential) {
        JTextField credentialTextField = new JTextField();
        credentialTextField.setFont(fontPlain);
        credentialTextField.setColumns(20);
        JLabel credentialLabel = new JLabel(credential + ": ");
        credentialLabel.setFont(fontBold);
        credentialLabel.setForeground(colorWhite);
        JPanel credentialPanel = new JPanel();
        credentialPanel.add(credentialLabel);
        credentialPanel.add(credentialTextField);

        return new CredentialPanel(credentialPanel, credentialTextField);
    }
}
