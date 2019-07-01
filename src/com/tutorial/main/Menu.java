package com.tutorial.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import com.tutorial.main.Game.STATE;

public class Menu extends MouseAdapter {
	
	// private Game game;
	private Handler handler;
	private HUD hud;
	private Random r = new Random();
	private Audio musicObject = new Audio();
	String filepath = "robot_blip.wav";
	
	public Menu(Game game, Handler handler, HUD hud) {
		// this.game = game;
		this.handler = handler;
		this.hud = hud;
	}
	
	public void mousePressed(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		
		if (Game.gameState == STATE.Menu) {
			// Play button
			if(mouseOver(mx, my, 210, 150, 200, 64)) {
				Game.gameState = STATE.Select;
				
				musicObject.playSound(filepath);
				return;
			}
			// Help button
			if(mouseOver(mx, my, 210, 250, 200, 64)) {
				Game.gameState = STATE.Help;
				musicObject.playSound(filepath);
			}
			// Quit button
			if(mouseOver(mx, my, 210, 350, 200, 64)) {
				System.exit(1);
			}
		}
		
		if (Game.gameState == STATE.Select) {
			// normal button
			if(mouseOver(mx, my, 210, 150, 200, 64)) {
				Game.gameState = STATE.Game;
				handler.addObject(new Player(Game.WIDTH/2-32, Game.HEIGHT/2-32,ID.Player, handler));
				handler.clearEnemys();
				handler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50),ID.BasicEnemy, handler));
				
				Game.diff = 0;
				
				musicObject.playSound(filepath);
			}
			// hard button
			if(mouseOver(mx, my, 210, 250, 200, 64)) {
				Game.gameState = STATE.Game;
				handler.addObject(new Player(Game.WIDTH/2-32, Game.HEIGHT/2-32,ID.Player, handler));
				handler.clearEnemys();
				handler.addObject(new HardEnemy(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50),ID.BasicEnemy, handler));
				
				Game.diff = 1;
				
				musicObject.playSound(filepath);
			}
			// back button
			if(mouseOver(mx, my, 210, 350, 200, 64)) {
				Game.gameState = STATE.Menu;
				musicObject.playSound(filepath);
				return;
			}
		}
		
		// Back button
		if (Game.gameState == STATE.Help) {
			if(mouseOver(mx, my, 210, 350, 200, 64)) {
				Game.gameState = STATE.Menu;
				musicObject.playSound(filepath);
				return;
			}
		}
		// End button
		if (Game.gameState == STATE.End) {
			if(mouseOver(mx, my, 210, 350, 200, 64)) {
				Game.gameState = STATE.Menu;
				musicObject.playSound(filepath);
				hud.setLevel(1);
				hud.setScore(0);
			}
		}
	}
	public void mouseReleased(MouseEvent e) {
		
	}
	private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
		if(mx > x && mx < x + width) {
			if(my > y && my < y + height) {
				return true;
			} else return false;
		} else return false;
	}
	
	public void tick() {
		
	}
	public void render(Graphics g) {
		if(Game.gameState == STATE.Menu) {
			Font fnt = new Font("arial", 1, 50);
			Font fnt2 = new Font("arial", 1, 30);
			
			g.setFont(fnt);
			g.setColor(Color.white);
			g.drawString("Wave", 240, 70);
			
			g.setFont(fnt2);
			g.drawRect(210, 150, 200, 64);
			g.drawString("Play", 280, 190);
			
			g.drawRect(210, 250, 200, 64);
			g.drawString("Help", 280, 290);

			g.drawRect(210, 350, 200, 64);
			g.drawString("Quit", 280, 390);
		} else if (Game.gameState == STATE.Help) {
			
			Font fnt = new Font("arial", 1, 50);
			Font fnt2 = new Font("arial", 1, 30);
			Font fnt3 = new Font("arial", 1, 20);
			
			g.setFont(fnt);
			g.setColor(Color.white);
			g.drawString("Help", 260, 70);
			
			g.setFont(fnt3);
			g.drawString("Use WASD keys to move player and dodge enemies", 70, 200);
			
			g.setFont(fnt2);
			g.drawRect(210, 350, 200, 64);
			g.drawString("Back", 275, 390);
		} else if (Game.gameState == STATE.End) {
			
			Font fnt = new Font("arial", 1, 50);
			Font fnt2 = new Font("arial", 1, 30);
			Font fnt3 = new Font("arial", 1, 20);
			
			g.setFont(fnt);
			g.setColor(Color.white);
			g.drawString("Game Over", 180, 70);
			
			g.setFont(fnt3);
			g.drawString("You lost with a score of: " + hud.getScore(), 185, 200);
			
			g.setFont(fnt2);
			g.drawRect(220, 350, 200, 64);
			g.drawString("Try Again", 253, 390);
		} else if (Game.gameState == STATE.Select) {
			Font fnt = new Font("arial", 1, 50);
			Font fnt2 = new Font("arial", 1, 30);
			
			g.setFont(fnt);
			g.setColor(Color.white);
			g.drawString("Select Difficulty", 120, 70);
			
			g.setFont(fnt2);
			g.drawRect(210, 150, 200, 64);
			g.drawString("Normal", 280, 190);
			
			g.drawRect(210, 250, 200, 64);
			g.drawString("Hard", 280, 290);

			g.drawRect(210, 350, 200, 64);
			g.drawString("Back", 280, 390);
		}
	}	
}
