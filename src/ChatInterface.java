import javax.swing.*;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatInterface extends JFrame {
    private JTextField textField;
    private JButton sendButton;
    private JSplitPane splitPane;
    private JScrollPane scrollPane;
    private JPanel inputPanel, topPanel, bottomPanel;
    private JTextArea test;

    public ChatInterface() {
        test = new JTextArea();
        initializeComponents();
        initializeFrame();
    }

    private void initializeFrame() {
        this.setVisible(true);
        this.setSize(InterfaceConstants.FRAME_DIMENSION);
    }

    private void initializeComponents() {
        initializeTextField();
        initializeSendButton();
        splitPane = new JSplitPane();

        bottomPanel = new JPanel();
        topPanel = new JPanel();
        scrollPane = new JScrollPane();

        inputPanel = new JPanel();
        scrollPane.setViewportView(inputPanel);

        setPreferredSize(new Dimension(500, 500));

        getContentPane().setLayout(new GridLayout());
        getContentPane().add(splitPane);

        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(430);
        splitPane.setBottomComponent(bottomPanel);
        splitPane.setTopComponent(test);

        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(inputPanel);

        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
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
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void initializeTextField() {
        textField = new JTextField();
        textField.setVisible(true);
        textField.setColumns(InterfaceConstants.TEXT_FIELD_WIDTH);
    }

    public static void main(String[] args) {
        new ChatInterface();
    }

}
