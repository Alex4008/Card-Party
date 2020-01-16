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
import java.net.ConnectException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import graphics.BufferedImageLoader;
import graphics.Sprite;
import input.Button;
import input.MouseInput;
import input.MouseMove;
import menus.ConnectionMenu;
import menus.MainMenu;
import menus.MultiMenu;
import menus.SingleMenu;
import menus.UnoMenu;
import menus.WarMenu;
import multiPlayerGames.Opponent;

public class Main extends Canvas implements Runnable {

	//Dev control panel
	public static double version = 0.5;
	public static boolean devMode = false;
	public static boolean enableMultiplayer = true;
	//public final static String TITLE = "Card Party Client - v" + Double.toString(version) + " DEV BUILD 2 - Created By Alex Gray - 2019";
	public final static String TITLE = "Card Party Client - v" + Double.toString(version) + " - Created By Alex Gray - 2019";
	public boolean vSyncToggle = false;
	public static final int FONT_SIZE = 35;
	public static final int CARD_WIDTH = 70;
	public static final int CARD_HEIGHT = 95;
	public static final int CARDS_FLIPPED_WAR = 3;
	//End dev control panel
	
	//private static final long serialVersionUID = 1L;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static boolean running;
	public static JFrame frame;
	private Thread thread;
	private int userRefreshRate = 30;
	private int currentTicks;
	private int currentFrames;
	public static boolean displayFPS = true;

	public static JTextField userNameField;
	public static JTextField ipField;
	public static JTextField portField;
	
	private BufferedImage icon = null;
	private BufferedImage background;
	private BufferedImage spriteSheet;
	private BufferedImage unoSpriteSheet;
	public static BufferedImage cardBack;
	public static BufferedImage unoCardBack;
	public static BufferedImage coin;
	private Sprite sprites;
	
	static Main m;
	
	//Server Connection Info
	public static String ip = "localhost"; //By default it will connect to localhost
	public static int port = 5056; //By default it will connect to 5056
	public static String userName = "Player";
	
	public static Client theClient = null;
	static Thread theThread = null;
	
	//Menus
	public MainMenu mainMenu;
	public SingleMenu singleMenu;
	public ConnectionMenu connectionMenu;
	public MultiMenu multiMenu;
	public WarMenu warMenu;
	public UnoMenu unoMenu;
	
	//Mouse Stuff
	private boolean clicked = false;
	private boolean mouseOnScreen = false;
	public static boolean mouseHeld = false;
	public Point mouseLoc = new Point(0, 0);
	public int clickType = 0;
	
	public static GameState gs = GameState.MAIN_MENU;
	public static GameType currentGame = null;
	
	public static boolean connect = true;
	
	public static BufferedImage unoArrow;
	
	ArrayList<Thread> theThreads;
	
	//Loading / Main Methods
	
	public void init() {
		requestFocus();
		
		theThreads = new ArrayList<Thread>();
		
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			spriteSheet = loader.loadImage("/card_sheet.png");
			unoSpriteSheet = loader.loadImage("/unoSpriteSheet.png");
			background = loader.loadImage("/background.png");
			icon = loader.loadImage("/icon.png");
			cardBack = loader.loadImage("/cardBack.png");
			unoCardBack = loader.loadImage("/unoCardBack.png");
			unoArrow = loader.loadImage("/arrow.png");
			coin = loader.loadImage("/token.png");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		setSprites(new Sprite(this)); //Load all the sprites into memory from the sprite sheet
		
		frame.setIconImage(icon);
		
		//addKeyListener(new KeyInput(this));
		addMouseListener(new MouseInput(this));
		addMouseMotionListener(new MouseMove(this));
		
		LoadState.Load(this, GameState.MAIN_MENU);
		/*
		//Getting display refresh rate
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();

		for (int i = 0; i < gs.length; i++) {
			DisplayMode dm = gs[i].getDisplayMode();

			userRefreshRate = dm.getRefreshRate();
			if (userRefreshRate == DisplayMode.REFRESH_RATE_UNKNOWN) {
				System.out.println("Unknown refresh rate. Leaving it at default: " + userRefreshRate); 
			}
		}
		*/
		
	}
	
	private synchronized void start() {
		if(running) return;
		System.out.println("Start");
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	private synchronized void stop() {
		
		//Close connection thread
		connect = false;
		if(theThread != null) try { theThread.join(); } catch (InterruptedException e) { e.printStackTrace(); }

		//Close main thread
		//try { thread.join(); } catch (InterruptedException e) { e.printStackTrace(); }

		System.exit(0);
	}
	
	public static void main(String[] args) {
		m = new Main();
		m.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		m.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		m.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		frame = new JFrame(Main.TITLE);
		frame.setResizable(false);
		GenerateTextFields();
		frame.add(userNameField);
		frame.add(ipField);
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
		
		if(Main.TITLE.contains("DEV") && devMode == false) {
			//This is a dev version, Say such upon loading in
			JOptionPane.showMessageDialog(Main.frame, "You are running a development version! Thanks for testing it out!", Main.TITLE, JOptionPane.INFORMATION_MESSAGE);
		}
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
			
			if(frameDelta >= 1) {
				render();
				frames++;
				frameDelta--;
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
		Tick.ticking(this);
		clicked = false;
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
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.drawImage(background, 0, 0, Color.BLACK, null);
		
		Font n = new Font("Arial", 20, 20);
		g.setFont(n);
		g.setColor(Color.BLACK);
		
		if(Main.TITLE.contains("DEV")) {
			g.drawString("Development Version", 5, Main.HEIGHT - 5);
		}
		if(devMode == true) {
			g.drawString("DEV MODE ACTIVE", 5, Main.HEIGHT - 5);
		}
		
		//Render the scene
		Render.rendering(this, g);
		
		////////////////////
		g.dispose();
		bs.show();
		
	}
	
	//Returns true if connected. False if failed.
	public boolean StartConnection() {
		
		if(enableMultiplayer == false) {
			LoadState.Load(m, GameState.MAIN_MENU);
			JOptionPane.showMessageDialog(Main.frame, "Multiplayer is not enabled on this development version!", Main.TITLE, JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		
        System.out.println("Creating new thread for server connection"); 
        connect = true;
        // create a new thread object 
        theClient = new Client(this);
        if(theThread != null && theThread.isAlive()) System.out.println("FUCK FUCK FUCK FUCK");
        theThread = theClient;
        // Invoking the start() method 
        theThread.start();
        
        try {
			Thread.sleep(1000);
			if(theThread != null && theThread.isAlive()) return true;
			else return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void endConnection() {
		System.out.println("Ending server thread");
		//theClient.closeConnection();
		connect = false;
		//theClient = null;
		//theThread = null;
	}
	
	public static void GenerateTextFields() {		
		//Ip Field
		ipField = new JTextField();
		ipField.setFont(new Font("Arial", FONT_SIZE, FONT_SIZE));
		ipField.setBounds((WIDTH / 5) * 3 - 100, HEIGHT / 6 * 2 - 50, 250, 50);
		ipField.setText(ip);
		ipField.setVisible(false);
		
		//Port Field
		portField = new JTextField();
		portField.setFont(new Font("Arial", FONT_SIZE, FONT_SIZE));
		portField.setBounds((WIDTH / 5) * 3  + 50, HEIGHT / 6 * 3 - 50, 100, 50);
		portField.setText((String.valueOf(port)));
		portField.setVisible(false);
		
		//Username Field
		userNameField = new JTextField();
		userNameField.setFont(new Font("Arial", FONT_SIZE, FONT_SIZE));
		userNameField.setBounds((WIDTH / 5) * 3 - 150, HEIGHT / 6 * 4 - 50, 300, 50);
		userNameField.setText("Player");
		userNameField.setVisible(false);
	}
	
	public void SetAllTextFieldsTrue() {
		userNameField.setVisible(true);
		userNameField.revalidate();
		userNameField.repaint();
		//frame.add(userNameField);
		
		ipField.setVisible(true);
		ipField.revalidate();
		ipField.repaint();
		//frame.add(ipField);
		
		portField.setVisible(true);
		portField.revalidate();
		portField.repaint();
		//frame.add(portField);
		//frame.add(this);
		//frame.pack();
	}
	
	public void SetAllTextFieldsFalse() {
		userNameField.setVisible(false);
		ipField.setVisible(false);
		portField.setVisible(false);
		//frame.add(this);
		//frame.pack();
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
	
	/*
	 * To load an image
	 */
	public BufferedImage loadImage(String path) {
		BufferedImageLoader loader = new BufferedImageLoader();
		
		try {
			BufferedImage image = loader.loadImage(path);
			return image;
		}
		catch(IOException e) { e.printStackTrace(); }
		return null; //Only happens if failed
	}
	
	public BufferedImage getDefaultSpriteSheet() { return spriteSheet; }
	public BufferedImage getUnoSpriteSheet() { return unoSpriteSheet; }
	public void setSprites(Sprite sprites) { this.sprites = sprites; }
	public Sprite getSprites() { return sprites; }
	public int GetCurrentTicks() { return currentTicks; }
	public int GetCurrentFrames() { return currentFrames; }
	public MainMenu getMainMenu() { return mainMenu; }
	public SingleMenu getSinglePlayerMenu() { return singleMenu; }
	public MultiMenu getMultiplayerMenu() { return multiMenu; }
	public ConnectionMenu getConnectionMenu() { return connectionMenu; }
	public WarMenu getWarMenu() { return warMenu; }
	public UnoMenu getUnoMenu() { return unoMenu; }
	public boolean getClicked() { return clicked; }
	public void setClicked(boolean clicked) { this.clicked = clicked; }
	public Point getMouseLoc() { return mouseLoc; }
	public void setMouseOnScreen(boolean mouseOnScreen) { this.mouseOnScreen = mouseOnScreen; }
	public boolean getMouseOnScreen() { return mouseOnScreen; }
	public static void closeGame() { running = false; }
	public Client getClient() { return theClient; }
	public double getVersion() { return version; }
}
