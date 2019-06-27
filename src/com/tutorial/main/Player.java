package com.tutorial.main;

import java.awt.Color;
import java.awt.Graphics;
// import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

public class Player extends GameObject {

	Random r = new Random();
	Handler handler;
	
	public Player(int x, int y, ID id, Handler handler) {
		super(x, y, id);
		this.handler = handler;
	}

	public void tick() {
		x += velX;
		y += velY;
		
		x = Game.clamp(x, 0, Game.WIDTH - 48);
		y = Game.clamp(y, 0, Game.HEIGHT - 68);
		
		/* My code
		if(y <= -5) y = Game.HEIGHT - 80;
		if(y >= Game.HEIGHT - 32) y = 1;
		if(x <= 0) x = Game.WIDTH - 35;
		if(x >= Game.WIDTH - 32) x = 0;
		*/
		handler.addObject(new Trail(x, y, ID.Trail, Color.white, 32, 32, 0.15f, handler));
		
		collision();
	}
	
	private void collision() {
		for(int i = 0; i < handler.object.size(); i++) {
			
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ID.BasicEnemy || tempObject.getId() == ID.FastEnemy) {
				if(getBounds().intersects(tempObject.getBounds())) {
					// collision code
					HUD.HEALTH -= 2;
				}
			}
		}
	}

	public void render(Graphics g) {
		//Graphics2D g2d = (Graphics2D) g;
		//g.setColor(Color.white);
		//g2d.draw(getBounds());
		
		if(id == ID.Player) g.setColor(Color.white);
		// else if(id == ID.Player2) g.setColor(Color.red);
		g.fillRect(x, y, 32, 32);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, 32, 32);
	}

}
