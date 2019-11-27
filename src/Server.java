import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Server implements Runnable{
    private String username;
    private Socket client;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    
    private static Queue<String> queue = new LinkedList<String>();
    private static List<Long> threads = new ArrayList<>();

    public Server(Socket socket) throws IOException {
        username = "";
        client = socket;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
    }

    public static void main(String[] args) throws IOException {
        @SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(ServerConstants.PORT);
        queue.add("hi");
        while(true) {
            Socket client = serverSocket.accept();   
            new Thread(new Server(client)).start();          
        }
    }

    @Override
    public void run() {
    	register();

    }
    
    private void receiveMessage() throws ClassNotFoundException, IOException {    	
    	queue.add((String) inputStream.readObject());
    }
    
    private void sendMessageAll() throws ClassNotFoundException, IOException {
    	for (int i = 10; i < (threads.size()+10); i++) {
    		if (Thread.currentThread().getId() == i) {
    			outputStream.writeObject(queue.remove());
    			outputStream.flush();
    		}		
    	}	
	}

	private void register() {
		threads.add(Thread.currentThread().getId());
		try {
    		String name = (String) inputStream.readObject();
			username = name;
			outputStream.writeObject("Welcome "+name+"!");
			outputStream.flush();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}		
    }

}
