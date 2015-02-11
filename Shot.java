import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*; 
import java.io.*; 
import javax.imageio.*;
import java.util.*;

public class Shot{
	private double x, y, speed;
	private int type,width,height;
	private Rectangle shotRect;
	private BufferedImage shotPic;
	public Shot(int x, int y, int type){
		this.x=x;
		this.y=y;
		typeChoice(type);
		
		width=5;
		height=11;
		
		shotRect=new Rectangle();
		shotRect.setSize(width,height);
		shotRect.setLocation(x,y); 
	}
	public void typeChoice(int num){
		if (num==1){//if the shot is by the ship it has a different picture and speed
			speed=-10;
			try{
				shotPic=ImageIO.read(new File("images\\ShipShot.png"));
			}
			catch(IOException e){}
		}
		else if (num==0){//if the picture is for the enemy 
			speed=8;//this is the set speed 
			try{//and this is the pic
				shotPic=ImageIO.read(new File("images\\EnemyShot.png"));
			}
			catch(IOException e){}
		}
	}
	//moves the bullet 
	public void move(){
		shotRect.y+=(int)speed;
	}
	//returns the rect size and location that surrouds the bullet
	public Rectangle points(){
		return shotRect;
	}
	//returns the bullets image
	public BufferedImage image(){
		return shotPic;
	}
}