import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable{
    private List<String> usernames;
    private List<Socket> clients;
    private List<ObjectOutputStream> outputStreams;
    private List<ObjectInputStream> inputStreams;

    private int numberOfClients;

    public Server() {
        usernames = new ArrayList<>();
        clients = new ArrayList<>();
        outputStreams = new ArrayList<>();
        inputStreams = new ArrayList<>();

        numberOfClients = 0;
    }

    public static void main(String[] args) throws IOException {
        @SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(ServerConstants.PORT);
        Server chatServer = new Server();
        Thread serverThread = new Thread(chatServer);
        serverThread.start();
        
        while(true) {
            Socket client = serverSocket.accept();
            chatServer.addClient(client);
        }
    }

    @Override
    public void run() {
    	while (numberOfClients == 0) {
    		System.out.print("");
    	}
    	int connectedClients = 0;
    	String str = "";
    	
    	while (true) {
    		if (numberOfClients > connectedClients) {	//temporary solution
    			connectedClients++;
    			System.out.println("registering");
    			register();
    		}
    		try {
        		str = (String) inputStreams.get(numberOfClients - 1).readObject();	//blocks for input
    			System.out.println(str);
        		echoMessage(str);
    		} catch (ClassNotFoundException | IOException e) {
    			e.printStackTrace();
    		}		
    	}
    		
    }
    
    private void register() {	
		try {
			String name = ((String) inputStreams.get(numberOfClients - 1).readObject());
			usernames.add(name);
			outputStreams.get(numberOfClients - 1).writeObject("Welcome "+name+"!");
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}		
    }

    public void addClient(Socket client) throws IOException {
        clients.add(client);
    	inputStreams.add(new ObjectInputStream(client.getInputStream()));
    	outputStreams.add(new ObjectOutputStream(client.getOutputStream()));
        ++numberOfClients; 
    }

    private Object receiveMessage() {
        Object receivedObject = null;

        try {

        }catch (Exception e) {
            e.printStackTrace();
        }

        return receivedObject;
    }

    private void echoMessage(String message) {
        try {
            for (int client = 0; client < clients.size(); client++) {
                outputStreams.get(client).writeObject(message);
                outputStreams.get(client).flush();               
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
