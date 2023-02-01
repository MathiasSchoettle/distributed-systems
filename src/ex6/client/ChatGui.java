package ex6.client;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Random;
import java.util.UUID;

public class ChatGui extends JFrame implements ActionListener, WindowListener, IMessageGui {

    private final JTextField input;
    private final JButton shutdown;
    private JButton login;
    private JTextField hostField, bindingNameField, usernameField;
    private final Style defaultStyle;
    private final Style userHeadlineStyle;
    private final Style adminStyle;
    private final StyledDocument document;
    private String registryHost = "localhost", username = UUID.randomUUID().toString(), bindingName = "ChatServer";
    private final LoginDialog dialog;
    private final IMessageSender sender;
    private String lastMessageFrom = null;

    public ChatGui(IMessageSender sender) {
        super("OTH-Chat-Client (RMI-Version)");
        this.sender = sender;

        JButton ok = new JButton("Send");
        this.shutdown = new JButton("Shutdown");
        this.input = new JTextField();
        JTextPane output = new JTextPane();

        output.setEditable(false);
        output.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        output.setSize(new Dimension(250, 500));
        this.document = output.getStyledDocument();
        this.defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        Style regularStyle = this.document.addStyle("regular", this.defaultStyle);
        StyleConstants.setFontFamily(regularStyle, "SansSerif");
        StyleConstants.setFontSize(regularStyle, 12);
        StyleConstants.setLineSpacing(regularStyle, 0.05f);
        StyleConstants.setForeground(regularStyle, Color.BLACK);
        StyleConstants.setBackground(regularStyle, Color.white);
        StyleConstants.setAlignment(regularStyle, StyleConstants.ALIGN_LEFT);

        this.userHeadlineStyle = this.document.addStyle("userHeadline", this.defaultStyle);
        StyleConstants.setFontFamily(this.userHeadlineStyle, "Arial");
        StyleConstants.setFontSize(this.userHeadlineStyle, 9);
        StyleConstants.setLineSpacing(this.userHeadlineStyle, 0.2f);
        StyleConstants.setForeground(this.userHeadlineStyle, Color.WHITE);
        StyleConstants.setBackground(this.userHeadlineStyle, Color.GRAY);
        StyleConstants.setAlignment(this.userHeadlineStyle, StyleConstants.ALIGN_RIGHT);

        this.adminStyle = this.document.addStyle("adminStyle", this.userHeadlineStyle);
        StyleConstants.setFontFamily(this.adminStyle, "Arial");
        StyleConstants.setFontSize(this.adminStyle, 9);
        StyleConstants.setLineSpacing(this.adminStyle, 0.2f);
        StyleConstants.setForeground(this.adminStyle, Color.GRAY);
        StyleConstants.setBackground(this.adminStyle, Color.WHITE);
        StyleConstants.setAlignment(this.adminStyle, StyleConstants.ALIGN_RIGHT);

        JPanel lowerPanel = new JPanel(new BorderLayout());
        lowerPanel.add(this.input, BorderLayout.NORTH);
        lowerPanel.add(ok, BorderLayout.SOUTH);

        ok.addActionListener(this);
        this.shutdown.addActionListener(this);

        this.add(new JScrollPane(output), BorderLayout.CENTER);
        this.add(lowerPanel, BorderLayout.SOUTH);

        this.setSize(new Dimension(500, 400));
        this.setVisible(true);
        this.addWindowListener(this);

        this.dialog = new LoginDialog(this, "Login", true);
        this.dialog.setVisible(true);
        if (this.username != null && !"".equals(this.username))
            sender.openChatConnection(this.username, this.registryHost, this.bindingName, this);
        else
            System.exit(NORMAL);
        this.setTitle(this.getTitle() + " - " + this.username);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this.shutdown)
            System.exit(NORMAL);
        else {
            String text = this.input.getText();
            this.input.setText("");
            int pos = this.document.getLength();
            try {
                this.document.insertString(pos, text + "\n", this.document.getStyle("regular"));
                this.document.setParagraphAttributes(pos, (text + "\n").length(), this.document.getStyle("regular"), false);
                this.sender.sendChatMessage(text);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
            this.lastMessageFrom = null;
        }
    }

    @Override
    public synchronized void showNewMessage(String user, String message) {
        Style userStyle = this.document.getStyle(user);

        if (userStyle == null) {
            userStyle = this.document.addStyle(user, this.defaultStyle);
            StyleConstants.setForeground(userStyle, new Color(new Random().nextInt(240), new Random().nextInt(240), new Random().nextInt(240)));
            StyleConstants.setFontFamily(userStyle, "SansSerif");
            StyleConstants.setFontSize(userStyle, 12);
            StyleConstants.setLineSpacing(userStyle, 0.05f);
            StyleConstants.setAlignment(userStyle, StyleConstants.ALIGN_RIGHT);
        }

        if (!user.equals(this.lastMessageFrom))
            this.insertString(user + ":", this.userHeadlineStyle);
        this.insertString(message, userStyle);
        this.lastMessageFrom = user;
    }

    @Override
    public void showAdminMessage(String message) {
        this.insertString(message, this.adminStyle);
        this.lastMessageFrom = null;
    }


    private void insertString(String message, Style style) {
        int pos = this.document.getLength();
        try {
            this.document.insertString(pos, message + "\n", style);
            this.document.setParagraphAttributes(pos, (message + "\n").length(), style, false);
        } catch (BadLocationException ignored) {
        }
    }

    class LoginDialog extends JDialog {

        public LoginDialog(JFrame parent, String title, boolean modal) {
            super(parent, title, modal);
            JPanel panel = new JPanel(new GridLayout(3, 2));
            panel.add(new JLabel("Registry host:"));
            ChatGui.this.hostField = new JTextField();
            ChatGui.this.hostField.setEditable(true);
            ChatGui.this.hostField.setFocusable(true);
            ChatGui.this.hostField.setColumns(12);
            ChatGui.this.hostField.setText("localhost");
            panel.add(ChatGui.this.hostField);

            panel.add(new JLabel("Binding name:"));
            ChatGui.this.bindingNameField = new JTextField();
            ChatGui.this.bindingNameField.setEditable(true);
            ChatGui.this.bindingNameField.setColumns(12);
            ChatGui.this.bindingNameField.setText("ChatServer");
            panel.add(ChatGui.this.bindingNameField);

            panel.add(new JLabel("Username:"));
            ChatGui.this.usernameField = new JTextField();
            ChatGui.this.usernameField.setEditable(true);
            ChatGui.this.usernameField.setColumns(12);
            ChatGui.this.usernameField.setText(ChatGui.this.username);
            panel.add(ChatGui.this.usernameField);

            this.add(panel, BorderLayout.CENTER);
            ChatGui.this.login = new JButton("Login");
            ChatGui.this.login.addActionListener(e -> {
                ChatGui.this.registryHost = ChatGui.this.hostField.getText();
                ChatGui.this.bindingName = ChatGui.this.bindingNameField.getText();
                ChatGui.this.username = ChatGui.this.usernameField.getText();
                ChatGui.this.dialog.dispose();
            });
            this.add(ChatGui.this.login, BorderLayout.SOUTH);
            this.pack();
            ChatGui.this.hostField.requestFocus();
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        this.sender.closeChatConnection();
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }


    @Override
    public void windowIconified(WindowEvent e) {
    }


    @Override
    public void windowDeiconified(WindowEvent e) {
    }


    @Override
    public void windowActivated(WindowEvent e) {
    }


    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
