import javax.swing.*;
import java.awt.*;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;

public class ChatInterface extends JFrame {
    private JTextField textField;
    private JButton sendButton;
    public JTextArea messageArea;
    private Client client;

    public ChatInterface(Client client) {
        this.client = client;

        initializeMessageArea();
        initializeComponents();
        initializeFrame();
    }

    private void initializeMessageArea() {
        messageArea = new JTextArea();

        messageArea.setSize(InterfaceConstants.FRAME_DIMENSION);
        messageArea.setVisible(true);
        messageArea.setEditable(false);
    }

    private void initializeFrame() {
        this.setSize(InterfaceConstants.FRAME_DIMENSION);
        this.setVisible(true);
        this.setResizable(false);
    }

    private void initializeComponents() {
        initializeTextField();
        initializeSendButton();

        JPanel bottomPanel = new JPanel();
        JPanel topPanel = new JPanel();
        JPanel inputPanel = new JPanel();

        JSplitPane splitPane = new JSplitPane();
        JScrollPane scrollPane = new JScrollPane();

        scrollPane.setViewportView(inputPanel);

        getContentPane().setLayout(new GridLayout());
        getContentPane().add(splitPane);

        topPanel.add(messageArea);
        scrollPane = new JScrollPane(messageArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(InterfaceConstants.VERTICAL_SPLIT_LOCATION);
        splitPane.setBottomComponent(bottomPanel);
        splitPane.setTopComponent(scrollPane);

        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(inputPanel);

        inputPanel.setMaximumSize(InterfaceConstants.INPUT_PANEL_DIMENSION);
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));

        inputPanel.add(textField);
        inputPanel.add(sendButton);

        pack();
    }

    private void initializeSendButton() {
        sendButton = new JButton();

        sendButton.setVisible(true);
        sendButton.setSize(InterfaceConstants.SEND_BUTTON_DIMENSION);
        sendButton.setText(InterfaceConstants.SEND_BUTTON_LABEL);
        sendButton.addActionListener(e -> {
            sendMessage();
        });
    }

    private void initializeTextField() {
        textField = new JTextField();
        textField.setVisible(true);
        textField.setColumns(InterfaceConstants.TEXT_FIELD_WIDTH);
    }

    private void sendMessage() {
        Message outGoingMessage = assembleMessage();

        ObjectOutputStream outToServer = client.getOutToServerStream();

        try {
            outToServer.writeObject(outGoingMessage);
            outToServer.flush();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Message assembleMessage() {
        String messageText = textField.getText();

        Message outGoingMessage = new Message(MessageType.SENDING_MESSAGE, messageText);

        outGoingMessage.setClientNumber(client.getClientNumber());

        return outGoingMessage;
    }


    public static void main(String[] args) throws InterruptedException {
        ChatInterface c = new ChatInterface(null);
        for(int i=0; i<10; ++i) {
            TimeUnit.SECONDS.sleep(2);
            if(i%2 == 0) {
                c.messageArea.append("Brian: Xerris" + "\n");
            }else {
                c.messageArea.append("Abe: Xerris" + "\n");
            }
        }
    }

}
