package com.tutorial.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
//import java.awt.image.BufferedImage;
//import java.io.IOException;
import java.util.Random;

//import javax.imageio.ImageIO;

// import com.sun.tools.javac.Main;

public class Game extends Canvas implements Runnable{
	
	private static final long serialVersionUID = 1550691097823471818L;

	public static final int WIDTH = 640, HEIGHT = WIDTH / 12 * 9;
	private Thread thread;
	private boolean running = false;
	
	public static boolean paused = false;
	public static int diff = 0; // normal = 0; hard = 1
	
	private Random r;
	private Handler handler;
	private HUD hud;
	private Spawn spawner;
	private Menu menu;
	private Audio musicObject;
	// private BufferedImage img;
	private Graphics g;
	private Shop shop;
	
	public enum STATE {
		Menu,
		Select,
		Help,
		Shop,
		Game,
		End
	};
	
	public static STATE gameState = STATE.Menu;
	
	// private BufferedImage sprite_sheet = null;
	
	public Game(){
//		try {
//			img = ImageIO.read(Main.class.getClassLoader().getResourceAsStream("sprite.png"));
//			g.drawImage(img, 0, 0, 200, 100, null);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		handler = new Handler();
		hud = new HUD();
		shop = new Shop(handler, hud);
		menu = new Menu(this, handler, hud);
		this.addKeyListener(new KeyInput(handler));
		this.addMouseListener(menu);
		this.addMouseListener(shop);
		
		//AudioPlayer.load();
		String filepath = "ChillingMusic.wav";
		musicObject = new Audio();
		musicObject.playMusic(filepath);
		
		new Window(WIDTH, HEIGHT, "Let's build a Game!", this);
		
		// image upload after window?
		
		spawner = new Spawn(handler, hud);
		r = new Random();
		
		if(gameState == STATE.Game)
		{
			handler.addObject(new Player(WIDTH/2-32, HEIGHT/2-32,ID.Player, handler));
		} else {
			for(int i = 0; i < 20; i++) {
				handler.addObject(new MenuParticle(r.nextInt(WIDTH), r.nextInt(HEIGHT),ID.MenuParticle, handler));
			}
		}
		// for creating the second player
		// handler.addObject(new Player(WIDTH/2+64, HEIGHT/2-32,ID.Player2));
	}
	
	public synchronized void start(){
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop(){
		try{
			thread.join(); // killing/stopping the thread
			running = false;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void run(){
		//init();
		this.requestFocus(); // I don't have to click on the screen to activate the keyInput
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		// int frames = 0;
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				tick();
				delta--;
			}
			if(running)
				render();
			// frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				// System.out.println("FPS: " + frames);
				// frames = 0;
			}
		}
		stop();
	}
	
	private void tick() {
		
		if(gameState == STATE.Game) 
		{
			if(!paused)
			{
				handler.tick();
				hud.tick();
				spawner.tick();
				
				if(HUD.HEALTH <= 0) {
					HUD.HEALTH = 100;
					gameState = STATE.End;
					handler.clearEnemys();
					for(int i = 0; i < 20; i++) {
						handler.addObject(new MenuParticle(r.nextInt(WIDTH), r.nextInt(HEIGHT),ID.MenuParticle, handler));
					}
				}
			}
		} else if (gameState == STATE.Menu || gameState == STATE.End || gameState == STATE.Select || gameState == STATE.Help) {
			menu.tick();
			handler.tick();
		}
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		g = bs.getDrawGraphics();
		
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		if(paused) {
			g.setColor(Color.white);
			g.drawString("PAUSED", 100, 100);
		}
		
		if(gameState == STATE.Game) {
			hud.render(g);
			handler.render(g);
		} else if (gameState == STATE.Shop) {
			shop.render(g);
		} else if (gameState == STATE.Menu || gameState == STATE.Help|| gameState == STATE.End || gameState == STATE.Select) {
			menu.render(g);
			handler.render(g);
		}
		
		g.dispose();
		bs.show();
	}
	
	public static float clamp(float var, float min, float max) {
		if(var >= max) return var = max;
		else if (var <= min) return var = min;
		else return var;
	}
	
	public static void main (String args[]){
		new Game();
	}
}
