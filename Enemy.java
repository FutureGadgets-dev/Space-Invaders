import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*; 
import java.io.*; 
import javax.imageio.*;
import java.util.*;

public class Enemy{
	private BufferedImage enemyPic=null;
	
	private int width,height, pts, ydist;
	public int x,y,lives,dir;
	
	int [] heights={26,15};
	
	private Rectangle enemyRect;
	boolean dirChange;
	private GamePanel pnl;
	
	//this is the list in which the enemy randomly picks to shoot or not
	private int[] list=new int[500];
	
	Random rand;
	
	//sets up the formation 
	public Enemy(int num, int xPos, int yPos, GamePanel scn){
		x=35+70*xPos;
		y=20+30*yPos;
		//if the random pick falls at the 49th spot it has a value of one and therfore it shoots 
		list[50]=1;
		dir=1;
		giveAtt(num);
		pnl=scn;
		rand = new Random();
	}
	// gives the enemy ship its attributes
	public void giveAtt(int n){
		for (int i=1; i<3;i++){
			if (n==i){//depending on the number(type)
				pts=i*10;//its life is different in multiples of ten
				lives=i;//it has different amount of lives
				try{//as well as different pictures
					enemyPic=ImageIO.read(new File("images\\Enemy"+i+".png"));
				}
				catch (IOException e){}	
				width=35;
				height=heights[i-1];
				enemyRect=new Rectangle();
				enemyRect.setSize(width,height);
				enemyRect.setLocation(x,y);
			}
		}
	}
	// changes the direction it is headed in 
	public void changeDir(){
		dir*=-1;
		ydist=15;
	}
	// moves the enemies sideways and down
	public boolean move (){
		dirChange=false;
		enemyRect.x+=dir;
		if (enemyRect.x<=0){
			enemyRect.x=0;
			dirChange=true;
		} 
		else if (enemyRect.x>=pnl.getWidth()-enemyRect.width){
			enemyRect.x=pnl.getWidth()-enemyRect.width;
			dirChange=true;
		}
		if (ydist>0){
			enemyRect.y+=1;
			ydist--; 	
		}
		return dirChange;	
	}
	//shoots based on random chance  
	public void shoot(ArrayList<Shot>shotlist){
		int len=list.length;
		int n=rand.nextInt(len);//gets a random number the size of the list of shot choices
		int shotyn=list[n];//if the shot is at the 49th spot it has a value of 1
		//if the random number is one it shoots by adding the shot to a shot arraylist in 
		//the gamepanel class
		if (shotyn==1){
			shotlist.add(new Shot(enemyRect.x+(int)(enemyRect.getWidth()/2),enemyRect.y,0));
		}
	}
	public int getPointsForKill(){
		return pts;
	}
	public BufferedImage image(){
		return enemyPic;
	}
	public Rectangle points(){
		return enemyRect;
	}
}