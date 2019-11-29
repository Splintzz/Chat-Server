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

    	String message = "";
    	while(true) {
    		try {
    			message = (String) inFromServer.readObject()+"\n";
    			chatInterface.displayMessage(message);	
    						  
			} catch (ClassNotFoundException e) {
				System.out.println("Error receiving message.");	
			} catch (IOException e) {
				System.out.println("Closing...");
				break;
			}
    	}			
		try {
			outToServer.close();
			inFromServer.close();
			clientSocket.close();
			chatInterface.disableInput();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private void register() {
    	Message message = new Message();
    	message.setMessageType(MessageType.PROMPT_FOR_MESSAGE);
    	message.setMessage("Please enter a username: \n");
    	chatInterface.displayMessage(message);   	
    	
    	while (chatInterface.getUserInput() == null) {
    		System.out.print("");
    	}
    	clientUsername = chatInterface.getUserInput();
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
