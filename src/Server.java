import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.sun.imageio.spi.OutputStreamImageOutputStreamSpi;

public class Server implements Runnable{
    private String username;    
    private boolean activeThread = true;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    
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
        outputStream = outputStreams.get(outputStreams.size() - 1);
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
    		while (true && activeThread == true) {
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
    
    private void disconnect() throws IOException {
    	outputStream.writeObject("Disconnected!");
		outputStream.flush();
		
		for (int i = 0; i < outputStreams.size(); i++) {
			if (outputStreams.get(i) == outputStream) {
				outputStreams.remove(i);
				break;
			}
		}
		inputStream.close();
		outputStream.close();
		activeThread = false;
	}
    
    private void receiveMessage() {
    	try {
    		String string = (String) inputStream.readObject();
    		if (string.equals(".")) {
    			disconnect();
    		}
    		else {
    			System.out.println("");
        		queue.add(username + ": " + string);
        		System.out.println("");
    		}		
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
