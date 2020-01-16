package main;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextField;

import games.War;
import menus.GameMenu;
import menus.HostingMenu;
import menus.SetupMenu;

public class Main extends Canvas implements Runnable {

	//Dev control panel
	public static double version = 0.5;
	public static boolean devMode = false;
	public final static String TITLE = "Card Party Server - v" + Double.toString(version) + " - Created By Alex Gray - 2019";
	public boolean vSyncToggle = true;
	public static final int FONT_SIZE = 35;
	public static final int BUTTON_WIDTH = 350;
	public static final int BUTTON_HEIGHT = 81;
	//End dev control panel
	
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 858;
	public static final int HEIGHT = 480;
	public static boolean running;
	static JFrame frame;
	private Thread thread;
	private Thread serverThread;
	private int userRefreshRate = 30;
	private int currentTicks;
	private int currentFrames;
	public static boolean displayFPS = true;

	public static JTextField portField;
	public static JTextField maxPlayersField;
	
	public static boolean serverOn = false;
	
	private BufferedImage icon = null;
	private BufferedImage background;
	
	//Default server connections
	public static int port = 5056; //By default it will host on 5056
	public static int maxPlayers = 4; //By default the max players will be 4
	public static int numberAI = 0;
	
	
	public Thread theThread = null;
	
	public static Server theServer = null;
	
	public static GameState gs = GameState.SERVER_SETUP;
	//public static GameType currentGame = null;
	
	//Mouse Stuff
	private boolean clicked = false;
	private boolean mouseOnScreen = false;
	public static boolean mouseHeld = false;
	Point mouseLoc = new Point(0, 0);
	int clickType = 0;
	
	//Menus
	public SetupMenu setupMenu;
	public HostingMenu hostingMenu;
	public GameMenu gameMenu;
	
	public static ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
	
	//Loading / Main Methods
	
	public void init() {
		requestFocus();
		
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			icon = loader.loadImage("/icon.png");
			background = loader.loadImage("/background.png");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		frame.setIconImage(icon);
		
		//addKeyListener(new KeyInput(this));
		addMouseListener(new MouseInput(this));
		addMouseMotionListener(new MouseMove(this));
		
		LoadState.Load(this, GameState.SERVER_SETUP);
		
		//Getting display refresh rate
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();

		
	}
	
	private synchronized void start() {
		if(running) return;
		System.out.println("Start");
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	private synchronized void stop() {
		if(running) return;
		closeServer();
		System.out.println("Shutting down server");
		try { theThread.join(); } catch (InterruptedException e) { e.printStackTrace(); }
		try { thread.join(); } catch (InterruptedException e) { e.printStackTrace(); }
		running = false;
		System.exit(0);
	}
	
	public static void main(String[] args) {
		Main m = new Main();
		m.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		m.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		m.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		frame = new JFrame(m.TITLE);
		frame.setResizable(false);
		GenerateTextFields();
		frame.add(maxPlayersField);
		frame.add(portField);
		frame.add(m);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		m.start();
		
		//Check for closing of window
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	running = false;
		    }
		});
	}
	
	public void run() {	
		init();
		long lastTime = System.nanoTime();
		final double amountOfTicks = 20.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		
		double frameDelta = 0;
		double frameNS = 1000000000 / userRefreshRate;
		
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			frameDelta += (now - lastTime) / frameNS;
			lastTime = now;
			if(delta >= 1) {
				tick();
				updates++;
				delta--;
			}
			
			if(vSyncToggle == true) {
				if(frameDelta >= 1) {
					render();
					frames++;
					frameDelta--;
				}
			}
			else {
				render();
				frames++;
				frameDelta = 0;
			}
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				currentTicks = updates;
				currentFrames = frames;
				updates = 0;
				frames = 0;
			}

		}
		stop();
	}
	
	private void tick() {
		CheckConnections();
		Tick.ticking(this);
	}
	
	private void render() {
		frame.pack();
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB), 0, 0, getWidth(), getHeight(), this);
		////////////////////
		//Draw the background
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.drawImage(background, 0, 0, Color.BLACK, null);
		
		//Render the scene
		
		Render.rendering(this, g);
		
		////////////////////
		g.dispose();
		bs.show();
		
	}
	
	public void startServer() {
		serverOn = true;
		try {
			theServer = new Server(this);
			theThread = theServer;
			theThread.start();
			Main.theServer.runningGame = GameType.WAR;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("I think we failed to load the server?. Revisit this in the main class at startServer() lmao.");
		}
	}
	
	public void closeServer() {
		for(ClientHandler c : clients) {
			c.closeServer();
		}
		
		serverOn = false;
		theServer = null;
		theThread = null;
	}
	
	public static ArrayList<ClientHandler> getClients() {
		return clients;
	}
	
	public static void addClient(ClientHandler ch) {
		clients.add(ch);
	}
    
    public static int GetNumConnections() {
    	return clients.size();
    }
    
    public static void CheckConnections() {
    	for (ClientHandler ch : clients) {
    		if(ch.isAlive() == false) {
    			clients.remove(ch);
    			System.out.println("Client removed!");
    			break;
    		}
    	}
    }
    
	public static void GenerateTextFields() {		
		//Port Field
		portField = new JTextField();
		portField.setFont(new Font("Arial", FONT_SIZE, FONT_SIZE));
		portField.setBounds((Main.WIDTH / 5 * 3) - 50, (Main.HEIGHT / 6 * 2) - 50, 100, 50);
		portField.setText(String.valueOf(port));
		portField.setVisible(false);
		
		//Max players Field
		maxPlayersField = new JTextField();
		maxPlayersField.setFont(new Font("Arial", FONT_SIZE, FONT_SIZE));
		maxPlayersField.setBounds((WIDTH / 5) * 3 - 50, (Main.HEIGHT / 6 * 3) - 60, 50, 50);
		maxPlayersField.setText(String.valueOf(maxPlayers));
		maxPlayersField.setVisible(false);
	}
	
	public void SetAllTextFieldsTrue() {
		maxPlayersField.setVisible(true);
		maxPlayersField.revalidate();
		maxPlayersField.repaint();
		
		portField.setVisible(true);
		portField.revalidate();
		portField.repaint();
	}
	
	public void SetAllTextFieldsFalse() {
		maxPlayersField.setVisible(false);
		portField.setVisible(false);
		//frame.add(this);
		//frame.pack();
	}
	
	public void kickPlayer(String username) {
		for(ClientHandler ch : clients) {
			if(ch.getPlayer().getUserName().equals(username)) ch.kickPlayer();
		}
	}
	
	/*
	 * To determine if the given rectangle overlaps with the mouse location
	 */
	public boolean isHovering(Rectangle r) {
		if(getMousePosition() == null) return false;
		
		try {
			if(r.contains(getMousePosition())) return true;	
			return false;
		}
		catch(NullPointerException e){
			return false;
		}
	}
	
	public int GetCurrentTicks() { return currentTicks; }
	public int GetCurrentFrames() { return currentFrames; }
	public boolean getClicked() { return clicked; }
	public void setClicked(boolean clicked) { this.clicked = clicked; }
	public Point getMouseLoc() { return mouseLoc; }
	public void setMouseOnScreen(boolean mouseOnScreen) { this.mouseOnScreen = mouseOnScreen; }
	public boolean getMouseOnScreen() { return mouseOnScreen; }
	public SetupMenu getSetupMenu() { return setupMenu; }
	public HostingMenu getHostingMenu() { return hostingMenu; }
	public GameMenu getGameMenu() { return gameMenu; }
	public double getVersion() { return version; }
}
