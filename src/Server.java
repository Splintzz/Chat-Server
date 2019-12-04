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

    public Server(ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException {
        username = "";
        outputStreams.add(outputStream);
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }
    
    public Server(ObjectInputStream inputStream) throws IOException {
        username = "";
        
    }

    public static void main(String[] args) throws IOException {
        @SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(ServerConstants.PORT);

        while(true) {
            Socket client = serverSocket.accept();
            new Thread(new Server(new ObjectOutputStream(client.getOutputStream()), new ObjectInputStream(client.getInputStream()))).start();
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
        		sendMessage(username + ": " + string);
    		}		
    	} catch (ClassNotFoundException | IOException e) {
    		System.out.println("Error receiving messages.");
    		System.exit(0);
    	}
    }

	private void sendMessage(String string) throws ClassNotFoundException, IOException {    	
    	for (int i = 0; i < outputStreams.size(); i++) {
    		outputStreams.get(i).writeObject(string);
    		outputStreams.get(i).flush();
    	}
	}
	
	private void register() {
		try {
    		String name = (String) inputStream.readObject();
			username = name;
			sendMessage("Welcome "+name+"!");
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}		
    }
}
