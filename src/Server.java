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
    private static String message = "null";

    public Server(ObjectOutputStream outputStream) throws IOException {
        username = "";
        inputStream = null;
        this.outputStream = outputStream;
    }
    
    public Server(ObjectInputStream inputStream) throws IOException {
        username = "";
        this.inputStream = inputStream;
        outputStream = null;
    }

    public static void main(String[] args) throws IOException {
        @SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(ServerConstants.PORT);

        while(true) {
            Socket client = serverSocket.accept();
            new Thread(new Server(new ObjectOutputStream(client.getOutputStream()))).start();
            new Thread(new Server(new ObjectInputStream(client.getInputStream()))).start();
        }
    }

    @Override
    public void run() {
    	if (outputStream == null) {
    		register();
    	}

    	while (true) {
	    	try {
	    		System.out.print("");
	    		if (outputStream == null)
	    			receiveMessage();
	    		else if (inputStream == null)
	    			sendMessage();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
    	}

    }
    
    private void receiveMessage() throws ClassNotFoundException, IOException {    	
    	queue.add(username+ ": " + (String) inputStream.readObject());    	
    }
    
    private void sendMessage() throws ClassNotFoundException, IOException {
    	synchronized(this) {
    		if (!queue.isEmpty()) {
    			message = queue.peek();
    		}
    	} 	
    	synchronized(this) {
    		if (!queue.isEmpty()) {
    			outputStream.writeObject(message);
    			outputStream.flush();
    		}
    	} 		
    	synchronized(this) {
    		if (!queue.isEmpty()) {
    			queue.remove();
    		}
    	} 	

	}

	private void register() {
		threads.add(Thread.currentThread().getId());
		try {
    		String name = (String) inputStream.readObject();
			username = name;
			queue.add("Welcome "+name+"!");
			System.out.println("Registered!");
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}		
    }

}
