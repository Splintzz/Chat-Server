import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Server implements Runnable{
    private String username;
    private ObjectInputStream inputStream;
    
    private static List<ObjectOutputStream> outputStreams = new ArrayList<ObjectOutputStream>();   
    private static Queue<String> queue = new LinkedList<String>();

    public Server(ObjectOutputStream outputStream) throws IOException {
        username = "";
        inputStream = null;
        outputStreams.add(outputStream);
    }
    
    public Server(ObjectInputStream inputStream) throws IOException {
        username = "";
        this.inputStream = inputStream;
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
    	if (inputStream != null) {
    		register();
    		while (true) {
        		System.out.print("");
				receiveMessage();
    		}
    	}  	
    	else {
    		while (true) {
    			System.out.print("");
    	    	try {
	    			if (!queue.isEmpty())
	    				sendMessage();
    	    	} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
    	    	}			
    		}
    	}
    }
    
    private void receiveMessage() {
    	try {
    		String string = (String) inputStream.readObject();
    		System.out.println("");
    		queue.add(username + ": " + string);
    		System.out.println("");
    	} catch (ClassNotFoundException | IOException e) {
    		System.out.println("Error receiving messages.");
    		System.exit(0);
    	}
    }
    
    private void sendMessage() throws ClassNotFoundException, IOException {    	
    	String string = "";
    	if (!queue.isEmpty())
    		string = queue.remove();
    	for (int i = 0; i < outputStreams.size(); i++) {
    		outputStreams.get(i).writeObject(string);
    		outputStreams.get(i).flush();
    	}
	}

	private void register() {

		try {
    		String name = (String) inputStream.readObject();
			username = name;
			queue.add("Welcome "+name+"!");
			System.out.println("");
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}		
    }

}
