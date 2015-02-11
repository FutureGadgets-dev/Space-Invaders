import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*; 
import java.io.*; 
import javax.imageio.*;
import java.util.*;
import java.applet.*;
import javax.sound.sampled.AudioSystem;

public class GamePanel extends JPanel implements KeyListener{
	private BufferedImage enemyPic, shipPic, shotPic, iconPic;//all pics
	private Ship myShip;
	private int ex,ey,sx,sy,esx,esy,ssx,ssy, waveNum, enemyType;
	private boolean []keys;
	private boolean visible;
	private Screen screen;
	private PrintWriter outFile;
	private Scanner inFile=null;
	//enemy's formation andtype
	private int [][] enemy= 
		{{1,1,1,1,1,1,1},
		{1,1,1,1,1,1,1},
		{1,1,1,1,1,1,1},
		{1,1,1,1,1,1,1},
		{1,1,1,1,1,1,1}};
	private ArrayList<Enemy>enemies=new ArrayList<Enemy>();
	private ArrayList<Shot>eShots=new ArrayList<Shot>();
	private ArrayList<Shot>sShots=new ArrayList<Shot>();
	private JLabel score, waveWord, highscore;
	private AudioClip back, expl;
	public String points, sP, highPoints;
	public GamePanel(Screen scn){
		sP="game";
		screen=scn;
		keys = new boolean[KeyEvent.KEY_LAST+1];
		
		//the original score is zero thewave is 0
		points="00000";
		waveNum=0;
		//thehighscore isset to theone in the .txt file
		readFile();
		
		//the score, the wave number, and the highscore
		score=new JLabel(points);
		score.setFont(new Font("Press Start 2P",Font.BOLD,14));
		score.setForeground(Color.white);
		score.setBackground(Color.lightGray);
		add(score);
		
		waveWord=new JLabel("Wave "+waveNum);
		waveWord.setFont(new Font("Press Start 2P",Font.BOLD,18));
		waveWord.setForeground(Color.white);
		waveWord.setBackground(Color.lightGray);
		add(waveWord);
		
		highscore=new JLabel(highPoints);
		highscore.setFont(new Font("Press Start 2P",Font.BOLD,14));
		highscore.setForeground(Color.white);
		highscore.setBackground(Color.lightGray);
		add(highscore);
		
		//music and sound
		back = Applet.newAudioClip(getClass().getResource("BACKGROUND.wav"));
		expl = Applet.newAudioClip(getClass().getResource("EXPLOSION.wav"));
		
		back.loop();
		
		//makes a new ship
		myShip=new Ship(this);
		shipPic=myShip.image();
		
		addKeyListener(this);
	}
	
	
	public void addNotify() {
        super.addNotify();
        requestFocus();
        screen.start();
    }
    
    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }
    
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
	
	//adds the enemies in their formation and type
	public void addEnemies(){
		for (int y=0;y<enemy.length;y++){
			for (int x=0;x<enemy[y].length;x++){
				enemies.add(new Enemy(enemy[y][x],x,y,this));
			}
		}
	}
	//how the enemies move
	public void moveEnemies(){
		for (int i=0;i<enemies.size();i++){
			if (enemies.get(i).move()==true){
				changeEnemyDir();
				i=0;
			}
		}
	}
	//howtheenemies change direction when they hit the wall
	public void changeEnemyDir(){
		for (int i=0;i<enemies.size();i++){
			//for each of the enemies
			enemies.get(i).changeDir();
		}
	}
	//the enemies shoot based on a list and random chang==ce in the enemy class
	public void enemyShoot(){
		for (int i=0;i<enemies.size();i++){
			enemies.get(i).shoot(eShots);//add the shot to the enemy shot arraylist
		}
	}
	//draws the enemies and modifies the speed when there is only one left 
	public void drawEnemies(Graphics g){
		for (int i=0;i<enemies.size();i++){
			enemyPic=enemies.get(i).image();
			ex=enemies.get(i).points().x;
			ey=enemies.get(i).points().y;
			g.drawImage(enemyPic,ex,ey,this);
			if (enemies.size()<=1){
				if (enemies.get(i).dir<10 && enemies.get(i).dir>-10 ){
					enemies.get(i).dir*=10;//makes the enemy faster
				}
			}
		}
	}
	//checks if the enemy collides with a bullet from the spaceship bullets 
	public void checkEnemyHit(){
		for (int y=0;y<sShots.size();y++){
			for (int x=0;x<enemies.size();x++){
				Rectangle rect1=enemies.get(x).points();
				Rectangle rect2=sShots.get(y).points();
				BufferedImage pic1=enemies.get(x).image();
				BufferedImage pic2=sShots.get(y).image();
				//uses rect area collision and rgb color
				if (shipCollision(rect1,rect2,pic1,pic2)==true){
					enemies.get(x).lives--;//only remove the enemy from the arraylist when his life is zero 
					if (enemies.get(x).lives<=0){
						//gets the points of the type of ship and adds it to the total score 
						addPoints(enemies.get(x).getPointsForKill());
						enemies.remove(x);//removes the enemy 
					}
					sShots.remove(y);//removes the shot no matter what
					expl.play();//explosion sound
					break;
				}
			}
		}
	}
	public ArrayList<Enemy> removeEnemies(){
		return new ArrayList<Enemy>();//removes all enemies from the arraylist by making a new one
	}
	public String gameOver(){
		// if the ship passes the ships position itsgame over
		for (int i=0;i<enemies.size();i++){  
			Rectangle enRect=enemies.get(i).points();
			Rectangle shRect=myShip.points();
			if (enRect.y+enRect.height>=shRect.y){
				sP="menu";	
			}
		}
		//if the ships life is zero then its also game over 
		if (myShip.lives()<=0){
			sP="menu";
		}
		return sP;
	}	
	
	//reads the highscore file
	public void readFile(){
		try{
    		inFile=new Scanner(new FileReader("highscore.txt"));
    	}
    	catch(IOException ex){}
		highPoints=inFile.nextLine();
		//if the score is higher than the highscore then it changes it, but only after you die so it wont lag
		if (Integer.parseInt(points)>=Integer.parseInt(highPoints)){
			if (myShip.lives<=0){
				updateScore();//changes the score
			}
		}
	}
	//updates the score only after you die because it starts to lag if it keeps checking and writing
	public void updateScore(){
		highPoints=points;
		points="00000";
		highscore.setText(highPoints);
		try{
			outFile=new PrintWriter(new BufferedWriter(new FileWriter("highscore.txt")));
			outFile.print("");
			outFile.print(highPoints);//writes the score
			outFile.close();
		}
		catch (IOException ex){}
	}
	//changes the wave number in the jlabel as well as changing the enemy types 
	public void waveWin(){
		if (enemies.size()==0){
			for (int i=0;i<eShots.size();i++){
				eShots.remove(i);
			}
			for (int i=0;i<sShots.size();i++){
				sShots.remove(i);
			}
			waveNum++;
			waveWord.setText("Wave"+waveNum);
			addEnemies();//adds more enemies for nextwave
			enemyType=Math.min((int)Math.floor(waveNum/2),4);
			Arrays.fill(enemy[enemyType],2);//changes the whole line to the other typr of enemy
		}
	}
	//resets all values so it will be like a new game
	public void reset(){
		sP="game";
		points="00000";
		score.setText(points);
		waveNum=0;
		for (int i=0;i<enemy.length;i++){
			Arrays.fill(enemy[i],1);
		}
		enemies=removeEnemies();
		myShip=new Ship(this);
		setFocusable(true);
		myShip.resetVelocity();
		myShip.newVelocity();
	}
	//adds the points then displays them
	public void addPoints(int n){
		int num=Integer.parseInt(points);
		num+=n;
		points="";
		String newPoints=Integer.toString(num);
		for (int i=0;i<5-newPoints.length();i++){
			points+="0";
		}
		points+=newPoints;
		score.setText(points);//displays the changes in the jlabel
	}
	
	public void moveShip(){
		//controls for the ship
		//counterdown for the delay between shots
		myShip.counterDown();
		if(keys[KeyEvent.VK_RIGHT] ){
			myShip.moveRight();
		}
		if(keys[KeyEvent.VK_LEFT] ){
			myShip.moveLeft();
		}
		sx=myShip.points().x;
		sy=myShip.points().y;
		if(keys[KeyEvent.VK_SPACE] ){
			myShip.shoot(sShots);
		}
	}
	//if the ship collides with enemy bullets it subtracts from its life and removes the shot
	public void checkShipHit(){
		for (int i=0;i<eShots.size();i++){
			Rectangle rect1=myShip.points();
			Rectangle rect2=eShots.get(i).points();
			BufferedImage pic1=myShip.image();
			BufferedImage pic2=eShots.get(i).image();
			if (shipCollision(rect1,rect2,pic1,pic2)==true){
				myShip.subLife();// subtracts the remaining lives from the ship by 1
				eShots.remove(i);
				expl.play();
				break;
			}
		}
	}
	//draws the ship
	public void drawShip(Graphics g){
		g.drawImage(shipPic,sx,sy,this);
		for (int i=0;i<myShip.lives();i++){
			iconPic=myShip.lifeIcon();
			g.drawImage(iconPic,20*i+15,5,this);
		}
	}
	
	//moves all the shotsfrom both arraylists 
	public void moveShots(){
		for (int i=0;i<eShots.size();i++){
			eShots.get(i).move();
		}
		for (int i=0;i<sShots.size();i++){
			sShots.get(i).move();
		}
	} 
	//removes the shots if they get out of the screen 
	public void removeShots(){
		for (int i=0;i<eShots.size();i++){
			if (eShots.get(i).points().y>screen.getHeight()){
				eShots.remove(i);
			}
		}
		for (int i=0;i<sShots.size();i++){
			if (sShots.get(i).points().y<0){
				sShots.remove(i);
			}
		}
	}
	//draws the shots 
	public void drawShots(Graphics g){
		for (int i=0;i<eShots.size();i++){
			shotPic=eShots.get(i).image();
			esx=eShots.get(i).points().x;
			esy=eShots.get(i).points().y;
			g.drawImage(shotPic,esx,esy,this);
		}
		for (int i=0;i<sShots.size();i++){
			shotPic=sShots.get(i).image();
			ssx=sShots.get(i).points().x;
			ssy=sShots.get(i).points().y;
			g.drawImage(shotPic,ssx,ssy,this);
		}
	}	
	
	// runs all the methods that we want to use for the game 
	public void run(){
		readFile();
		moveShip();
		moveEnemies();
		enemyShoot();
		moveShots();
		checkEnemyHit();
		checkShipHit();
		removeShots();
		waveWin();
	}
	@Override
	//paints all the parts from the previous draw"whatever"() methods
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.black);
		g.fillRect(0,0,getWidth(),getHeight());
		drawEnemies(g);
		drawShip(g);
		drawShots(g);
		
	}
	
	public boolean shipCollision(Rectangle shipRect, Rectangle shotRect, BufferedImage picOfShip, BufferedImage picOfShot) {
        // Check if the boundires intersect
        if (shotRect.intersects(shipRect)) {
            // Calculate the collision overlap
            Rectangle bounds = getArea(shotRect, shipRect);
            if (!bounds.isEmpty()) {
                // Check all the pixels in the collision overlapping to determine if they hit accurately 
                //and not only on the general rectangle
                for (int x = bounds.x; x < bounds.x + bounds.width; x++) {
                    for (int y = bounds.y; y < bounds.y + bounds.height; y++) {
                        return collision(x, y, shotRect,shipRect,picOfShot , picOfShip ); 
                    }
                }
            }
        }
        return false;
    }
	
	public Rectangle getArea(Rectangle rect1, Rectangle rect2) {
		//returns the area of the rects so we can see if they overlap
        Area a1 = new Area(rect1);
        Area a2 = new Area(rect2);
        a1.intersect(a2);
        return a1.getBounds();
    }
	
	public boolean collision(int x, int y, Rectangle Rect1, Rectangle Rect2, BufferedImage pic1, BufferedImage pic2) {
        boolean collision = false;

        int pic1Pixel = pic1.getRGB(x - Rect1.x, y - Rect1.y);
        int pic2Pixel = pic2.getRGB(x - Rect2.x, y - Rect2.y);
        
        //if the shot (from the ship) hits the white enemiesthen there is a colision
        if (((pic1Pixel>>16)&0xFF)==255 && ((pic1Pixel >> 8) & 0xFF)==255 && ((pic1Pixel) & 0xFF)==255){
        	collision=true;
        }
        //if the shot (from the enemies) hits something green like the ship then there is a colision
        if(((pic2Pixel>>16)&0xFF)==0 && ((pic2Pixel >> 8) & 0xFF)==255 && ((pic2Pixel) & 0xFF)==0){
        	collision=true;
        }
        return collision;
    }
}