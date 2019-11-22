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
            chatServer.addOutputStream(new ObjectOutputStream(client.getOutputStream()));
            chatServer.addInputStream(new ObjectInputStream(client.getInputStream()));
        }
    }
    
    public void addInputStream(ObjectInputStream inputStream) {
    	inputStreams.add(inputStream);
    }
    
    public void addOutputStream(ObjectOutputStream outputStream) {
    	outputStreams.add(outputStream);
    }

    public void addClient(Socket client) {
        clients.add(client);
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

    @Override
    public void run() {

    }
}
