import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*; 
import java.io.*; 
import javax.imageio.*;
import java.util.*;

public class Ship{
	private BufferedImage spaceship=null;
	private BufferedImage smallship=null;
	private Rectangle shipRect;
	public int lives;
	private int weapons, width,height,count, velocity;
	private GamePanel pnl;
	private boolean dead;
	public Ship(GamePanel scn){
		try{
			spaceship=ImageIO.read(new File("images\\ship.png"));
			smallship=ImageIO.read(new File("images\\smallship.png"));
		}
		catch (IOException e){
		}
		lives=3;
		velocity=5;
		dead=false;
		pnl=scn;
		width=35;
		height=22;
		shipRect=new Rectangle();
		shipRect.setSize(width,height);
		shipRect.setLocation(243,480);
	}
	
	public void resetVelocity(){
		velocity=0;
	}
	public void newVelocity(){
		velocity=5;
	}
	
	//moves the ship and/or shoots when specific keys in gamapanel class is clicked
	public void moveRight(){
		shipRect.x+=velocity;
		shipRect.x=Math.min(pnl.getWidth()-width,shipRect.x);
	}
	public void moveLeft(){
		shipRect.x-=velocity;
		shipRect.x=Math.max(0,shipRect.x);
	}
	public void shoot(ArrayList<Shot>list){
		if (count==0){//adds the shot to an arraylist in gamepanel class when it is able to shoot again
			list.add(new Shot(shipRect.x+(int)(shipRect.getWidth()/2),shipRect.y-6,1));
			count=20;
		}
	}
	//counts down till next time it can shoot
	public void counterDown(){
		if (count>0){
			count--;
		}
	}
	//reduces its life when it is shot
	public void subLife(){
		lives--;
		if (lives<=0){
			lives=0;
		}
	}
	public Rectangle points(){
		return shipRect;
	}
	public BufferedImage image(){
		return spaceship;
	}
	public int lives(){
		return lives;
	}
	public BufferedImage lifeIcon(){
		return smallship;
	}
}