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

    private ServerSocket chatServerSocket;

    private int numberOfClients;

    public Server() {
        try {
            chatServerSocket = new ServerSocket(ServerConstants.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        usernames = new ArrayList<>();
        clients = new ArrayList<>();
        outputStreams = new ArrayList<>();
        inputStreams = new ArrayList<>();

        numberOfClients = 0;
    }

    public static void main(String[] args) {
        Server chatServer = new Server();

        while(true) {
            Socket client = chatServer.acceptNewClient();

            chatServer.addClient(client);
        }
    }

    public void addClient(Socket client) {
        clients.add(client);
    }

    public Socket acceptNewClient() {
        Socket client = null;
        try {
            client = chatServerSocket.accept();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            return client;
        }
    }

    private Object receiveMessage() {
        Object receivedObject = null;

        try {
            receivedObject = inputStreams.get(0).readObject();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            return receivedObject;
        }
    }

    @Override
    public void run() {

    }
}
