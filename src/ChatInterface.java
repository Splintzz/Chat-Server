import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class ChatInterface extends JFrame {
    private JTextField textField;
    private JButton sendButton;
    public JTextArea messageArea;
    private Client client;
    
    private String userInput;

    public ChatInterface(Client client) {
        this.client = client;
        userInput = null;

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
			try {
				sendMessage();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		  
        });
    }

    private void initializeTextField() {
        textField = new JTextField();
        textField.setVisible(true);
        textField.setColumns(InterfaceConstants.TEXT_FIELD_WIDTH);
    }

    private void sendMessage() throws IOException {
        Message outGoingMessage = assembleMessage();	
        userInput = outGoingMessage.getMessage();       
        textField.setText("");
       
        client.getOutToServerStream().writeObject(userInput);
        client.getOutToServerStream().flush();
    }

    private Message assembleMessage() {
        String messageText = textField.getText();

        Message outGoingMessage = new Message(MessageType.SENDING_MESSAGE, messageText);
        
        if (client != null)
        	outGoingMessage.setClientUsername(client.getClientUsername());

        return outGoingMessage;
    }
    
    public void disableInput() {
    	textField.setVisible(false);
    	sendButton.setVisible(false);
    }
    
    public void setUserInput(String input) {
    	userInput = input;
    }
    
    public String getUserInput() {
       return userInput;
    }

    public void displayMessage(Client client, Message message) {
        messageArea.append(client.getClientUsername() + ": " + message.getMessage());
    }
    
    public void displayMessage(Message message) {
        messageArea.append(message.getMessage());
    }
    
    public void displayMessage(String message) {
    	messageArea.append(message);
    }

    public static void main(String[] args) throws InterruptedException {
    }

}
