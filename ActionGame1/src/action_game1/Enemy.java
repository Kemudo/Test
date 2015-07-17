package action_game1;

import java.awt.Color;
import java.awt.Graphics;

public class Enemy extends Character{
	private int vx,vy;
	
	Enemy(int sizeX, int sizeY, AnimData animData) {
		super(sizeY, sizeY, animData);
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		x = 100;
		y = 30;
		vx = 2;
		vy = 2;
		this.collision = new CollisionObject(x,y,sizeX,sizeY,CollisionObject.Property.ENEMY);
	}
	Enemy(int sizeX, int sizeY, int x, int y, AnimData animData) {
		super(sizeY, sizeY, animData);
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		vx = 5;
		vy = 5;
		this.x = x;
		this.y = y;
		this.collision = new CollisionObject(x,y,sizeX,sizeY,CollisionObject.Property.ENEMY);
	}
	
	@Override
	public void move(){
		super.move();
		//è„â∫ç∂âEÇ…ìÆÇ©ÇµÇƒÇ›ÇÈ
		x += vx;
		y += vy;
		
		if( x < 0 ){
			x = 0;
			vx *= -1;
		}else if( x > MainPanel.WIDTH-sizeX ){
			x = MainPanel.WIDTH-sizeX;
			vx *= -1;
		}
		if( y < 0 ){
			y = 0;
			vy *= -1;
		}else if( y > MainPanel.HEIGHT-sizeY ){
			y = MainPanel.HEIGHT-sizeY;
			vy *= -1;
		}
		
		if(collision.collideObject != CollisionObject.Property.NO_COLLIDE)
			delete();
	}
	
	@Override
	public void draw(Graphics g){
		g.setColor(Color.RED);
		g.fillRect(x, y, sizeX, sizeY);
	}
}
