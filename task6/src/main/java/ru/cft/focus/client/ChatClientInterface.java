package ru.cft.focus.client;

import lombok.extern.log4j.Log4j2;
import ru.cft.focus.client.exception.ClientNotConnectedException;
import ru.cft.focus.common.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@Log4j2
public class ChatClientInterface {
    private JFrame frame;
    private JTextArea messagePanel;
    private JTextField inputField;
    private JButton sendButton;
    private JTextField serverAddressField;
    private JTextField usernameField;
    private JList<String> participantsList;
    private DefaultListModel<String> participantsModel;
    private JPanel connectionPanel;
    private Client client;

    public ChatClientInterface() {
        frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
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


        participantsModel = new DefaultListModel<>();

        // Добавляем элементы в модель данных
        participantsModel.addElement("Participant 1");
        participantsModel.addElement("Participant 2");
        participantsModel.addElement("Participant 3");

        // Привязываем модель данных к JList
        participantsList.setModel(participantsModel);

        // Добавляем элементы в модель данных
        participantsModel.addElement("Participant 3");
        participantsModel.addElement("Participant 4");
        participantsModel.addElement("Participant 5");

        // Привязываем модель данных к JList
        participantsList.setModel(participantsModel);

        sidePanel.add(participantsScrollPane, BorderLayout.CENTER);

        frame.add(sidePanel, BorderLayout.EAST);

        frame.setVisible(true);
    }

    private void connectToServer() {
        String serverAddress = serverAddressField.getText();
        String username = usernameField.getText();

        client = new Client(username, serverAddress);
        client.connectToServer();
        if (client.isConnected()) {
            connectionPanel.setVisible(false);
            frame.revalidate();
            frame.repaint();
        }
    }

    private void sendMessage() {
        String messageText = inputField.getText();
        if (!messageText.trim().isEmpty() && client != null) {
            try {
                client.sendMessageToServer(messageText);
            } catch (ClientNotConnectedException e) {
                log.error(e.getMessage());
            }
//            messagePanel.append(formattedMessage);
        }
        inputField.setText("");
    }

    private void updateView(Message message) {
        if (message.connect()) {
            DefaultListModel<String> model = new DefaultListModel<>();

            // Добавляем элементы в модель данных
            model.addElement("Participant 1");

            // Привязываем модель данных к JList
            participantsList.setModel(model);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ChatClientInterface();
            }
        });
    }
}