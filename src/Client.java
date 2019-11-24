import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {
    private String clientUsername;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;
    private Socket clientSocket;
    private ChatInterface chatInterface;

    public Client() {
        try {
            clientSocket = new Socket(InetAddress.getByName(ServerConstants.IP), ServerConstants.PORT);
            outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
            inFromServer = new ObjectInputStream(clientSocket.getInputStream());
            chatInterface = new ChatInterface(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        Thread thread = new Thread(new Client());
        thread.start();
    }

    @Override
    public void run() {
    	register();
    	try {
			outToServer.writeObject(clientUsername);
			outToServer.flush();
		} catch (IOException e) {			
			e.printStackTrace();
		}
    }

    private void register() {
    	Message message = new Message();
    	message.setMessageType(MessageType.PROMPT_FOR_MESSAGE);
    	message.setMessage("Please enter a username: ");
    	chatInterface.displayMessage(message);   	
    	
    	while (chatInterface.getUserInput() == null) {
    		System.out.print("");
    	}
    	clientUsername = chatInterface.getUserInput();
    	System.out.println(clientUsername);
        //interface pops up a menu prompting for username
        //username is set here
        //username is sent to server to verify if the name is taken or not
    }

    private void receiveMessage() {
        try {
            Message receivedMessage = (Message) inFromServer.readObject();

            displayMessage(receivedMessage);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayMessage(Message receivedMessage) {
        chatInterface.displayMessage(this, receivedMessage);
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public ObjectOutputStream getOutToServerStream() {
        return outToServer;
    }

    public void setOutToServerStream(ObjectOutputStream outToServer) {
        this.outToServer = outToServer;
    }

    public ObjectInputStream getInFromServerStream() {
        return inFromServer;
    }

    public void setInFromServerStream(ObjectInputStream inFromServer) {
        this.inFromServer = inFromServer;
    }
}
