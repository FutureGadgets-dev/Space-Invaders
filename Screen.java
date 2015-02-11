//SIMPLE GAME ASSIGNMENT:
//Space Invaders
//Ahmad Kashash

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*; 
import java.io.*; 
import javax.imageio.*;
import java.util.*;
import javax.swing.Timer;

public class Screen extends JFrame implements ActionListener{
	private Timer myTimer;
	private GamePanel game;
	private MainMenu menu;
	private int points, stateNum;
	private String state;
	private Container pane; 
	public Screen(){
		super("Space Invaders");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setsan icon forthe window
		this.setIconImage(new ImageIcon("images\\space invader 2.png").getImage());
		setSize(525,550);
		//container for the jpanels
		pane=this.getContentPane();
		state="menu";
		stateNum=0;
		
		menu=new MainMenu();
		game=new GamePanel(this);
		
		myTimer = new Timer(20, this);
		start();
		
		pane.add(menu,BorderLayout.CENTER);
		setResizable(false);
		setVisible(true);
	}
	public void start(){
		myTimer.start();
	}
	public void actionPerformed(ActionEvent evt){
		if (state.equals("game")){
			if (stateNum==0){
				game.reset();
				pane.removeAll();//removes the jpanel that was in the container
				pane.add(game,BorderLayout.CENTER);//adds the game jpanel to display instead
				stateNum=1;
			}
			changePane(game.gameOver());
			game.run();
		}
		if (state.equals("menu")){
			if (stateNum==0){
				menu.rePick();
				pane.removeAll();//removes everything from the jpanel container
				pane.add(menu,BorderLayout.CENTER);//adds the menu jpanel for display instead
				stateNum=1;
			}
			changePane(menu.pick());
		}	
		pane.validate();
		pane.repaint();
	}
	//changes the state of the game from menu or game depending on what was passed
	public void changePane(String st){
		if (!state.equals(st)){
			stateNum=0;
			state=st;
		}	
	}
	
	public static void main(String[]args){
		Screen frame=new Screen();
	}
}