package main;
// A Java program for a Server 
import java.net.*;

import javax.swing.JOptionPane;

import java.io.*; 
  
public class Server extends Thread
{ 
    //initialize socket and input stream 
    private ServerSocket    server   = null; 
    public boolean acceptingConnections = false;
    private Main m;
    volatile public GameType runningGame = null;
    volatile public boolean gameRunning = false;
    volatile public boolean inGameStartUp = false;
    
    // constructor with port 
    public Server(Main m) throws IOException
    { 
    	this.m = m;
    }
    
    public void startRunningGame() {
    	m.gameMenu.startWarGame();
    	gameRunning = true;
    	inGameStartUp = false;
    }
    
    public void stopRunningGame() {
    	gameRunning = false;
    }
    
    @Override
    public void run() {
    	System.out.println("Trying to host on Port: " + Main.port);
        // server is listening on given port
    	ServerSocket ss;
    	try {	
    		ss = new ServerSocket(Main.port);
    		
            // running infinite loop for getting 
            // client request 
    		System.out.println("Server loaded.");
            while (Main.serverOn)  
            { 
                Socket s = null; 
                
                try 
                { 
                    // socket object to receive incoming client requests 
                	ss.setSoTimeout(500);
                    s = ss.accept(); 
                      
                    System.out.println("A new client is connected : " + s); 
                      
                    // obtaining input and out streams 
                    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream()); 
                    ObjectInputStream in = new ObjectInputStream(s.getInputStream()); 
                   
                      
                    if(gameRunning == true) {
                    	//String received = dis.readUTF(); //Get entrance information from client.
                    	System.out.println("A game is running. The client must be disconnected");
                    	//dos.writeUTF("ERROR:103");
                    	continue;
                    }
                    
                    System.out.println("Assigning new thread for this client"); 
      
                    // create a new thread object 
                    ClientHandler ch = new ClientHandler(m, s, in, out); 
                    Thread t = ch;
                    Main.addClient(ch);
                    // Invoking the start() method 
                    t.start(); 
                      
                } 
                catch(SocketTimeoutException e) {
                
                }
                catch (IOException e){ 
                	e.printStackTrace();
                	ss.close();
                	ss = null;
                    s.close(); 
                } 
            } 
            ss.close();
    		
		} catch (Exception e) {
			LoadState.Load(m, GameState.SERVER_SETUP);
			System.out.println("Failed to Bind Port. (Is there another server running?)");
			JOptionPane.showMessageDialog(Main.frame, "Failed to Bind Port. (Is there another server running?)", Main.TITLE, JOptionPane.INFORMATION_MESSAGE);
			//ss = null;
			m.closeServer();
		} 
    }
} 