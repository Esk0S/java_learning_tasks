package ru.cft.focus;

import ru.cft.focus.exception.ClientNotConnectedException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ChatClientInterface implements ConnectionListener {
    private JFrame frame;
    private JTextArea messagePanel;
    private JTextField inputField;
    private JButton sendButton;
    private JTextField serverAddressField;
    private JTextField usernameField;
    private JList<String> participantsList;
    private JPanel connectionPanel;
    private Client client;
    private static final org.apache.logging.log4j.Logger log
            = org.apache.logging.log4j.LogManager.getLogger(ChatClientInterface.class);

    public ChatClientInterface() {
        frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());

        messagePanel = new JTextArea();
        messagePanel.setEditable(false);

        JScrollPane messageScrollPane = new JScrollPane(messagePanel);
        frame.add(messageScrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        inputField = new JTextField();
        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        inputPanel.add(inputField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        inputPanel.add(sendButton, BorderLayout.EAST);

        frame.add(inputPanel, BorderLayout.SOUTH);

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(200, frame.getHeight()));

        connectionPanel = new JPanel();
        connectionPanel.setLayout(new GridLayout(3, 2));

        JLabel serverLabel = new JLabel("Server Address:");
        serverAddressField = new JTextField();
        JLabel nameLabel = new JLabel("Your Name:");
        usernameField = new JTextField();
        JButton connectButton = new JButton("Connect");
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });

        connectionPanel.add(serverLabel);
        connectionPanel.add(serverAddressField);
        connectionPanel.add(nameLabel);
        connectionPanel.add(usernameField);
        connectionPanel.add(connectButton);

        sidePanel.add(connectionPanel, BorderLayout.NORTH);

        participantsList = new JList<>();
        JScrollPane participantsScrollPane = new JScrollPane(participantsList);

        sidePanel.add(participantsScrollPane, BorderLayout.CENTER);

        frame.add(sidePanel, BorderLayout.EAST);

        frame.setVisible(true);
    }

    private void connectToServer() {
        String serverAddress = serverAddressField.getText();
        String username = usernameField.getText();

        client = new Client(username, serverAddress);
        client.addMessageListener(new MessageListener() {
            @Override
            public void onReceivedMessage(String message) {
                addMessage(message);
            }
        });
        client.addErrorListener(new ErrorListener() {
            @Override
            public void onError(String error) {
                showError(error);
            }
        });
        client.addConnectionListener(this);
        client.addParticipantsListener(new ParticipantsListener() {
            @Override
            public void updateParticipants(List<String> participants) {
                updateParticipantsList(participants);
            }
        });
        client.connectToServer();
    }

    @Override
    public void onConnected() {
        SwingUtilities.invokeLater(() -> {
            connectionPanel.setVisible(false);
            frame.revalidate();
            frame.repaint();
        });
    }

    private void sendMessage() {
        String messageText = inputField.getText();
        if (!messageText.trim().isEmpty() && client != null) {
            try {
                client.sendMessageToServer(messageText);
            } catch (ClientNotConnectedException e) {
                log.error(e.getMessage());
            }
        }
        inputField.setText("");
    }

    private void addMessage(String message) {
        messagePanel.append(message + "\n");
    }

    private void updateParticipantsList(List<String> participants) {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String participant : participants) {
            model.addElement(participant);
        }
        participantsList.setModel(model);
    }

    public void showError(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

}