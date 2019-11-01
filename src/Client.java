import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements Runnable{
    private int clientNumber;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;
    private Socket clientSocket;

    public Client() {
        try {
            clientSocket = new Socket(InetAddress.getByName(ServerConstants.IP), ServerConstants.PORT);
            outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
            inFromServer = new ObjectInputStream(clientSocket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new Client());
        thread.start();
    }

    @Override
    public void run() {

    }


    public int getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(int clientNumber) {
        this.clientNumber = clientNumber;
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
