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
    	
    	ObjectInputStream inputStream = inputStreams.get(numberOfClients - 1);

		try {
			usernames.add((String) inputStream.readObject());
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		
		System.out.println(usernames.get(0));
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

    private void echoMessage(Message message) {
        try {
            for (int client = 0; client < clients.size(); ++client) {
                ObjectOutputStream outToClient = (ObjectOutputStream) (clients.get(client).getOutputStream());
                outToClient.writeObject(message);
                outToClient.flush();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
