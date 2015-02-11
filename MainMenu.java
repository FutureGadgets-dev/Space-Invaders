//paint component here
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*; 
import java.io.*; 
import javax.imageio.*; 

public class MainMenu extends JPanel implements MouseListener, MouseMotionListener{
	private BufferedImage menuPic=null;
	private JLabel start, highscore, exit;
	private String screenPick;
	
	public MainMenu(){
		try{
			menuPic=ImageIO.read(new File("images\\Start Screen.png"));
		}
		catch (IOException ex){
		}
		addMouseMotionListener(this);
		addMouseListener(this);
		
		screenPick="menu";
		setLayout(null);//so i can movethe jlabels to their current positions
		
		//adds jlabel for starting the game and exitting it
		start=new JLabel("Start");
		start.setFont(new Font("Press Start 2P",Font.BOLD,20));
		start.setForeground(Color.gray);
		start.setBackground(Color.gray);
		start.setBounds(200,280,105,20);//the position and size
		
		exit=new JLabel("Exit");
		exit.setFont(new Font("Press Start 2P",Font.BOLD,20));
		exit.setForeground(Color.gray);
		exit.setBackground(Color.gray);
		exit.setBounds(210,320,85,20);//the position and size
		
		add(start);
		add(exit);
		start.addMouseListener(this);
		exit.addMouseListener(this);
	}
	//changes the color of both jlabels to grey when not hovering or clicking it
	public void changeCol(){
		start.setForeground(Color.gray);
		start.setBackground(Color.gray);
		exit.setForeground(Color.gray);
		exit.setBackground(Color.gray);
	}
	
	public void paintComponent(Graphics g){
		g.drawImage(menuPic,0,0,this);
	}
	
	//when you hover over it it changes the color to white
	public void mouseEntered(MouseEvent e) {
		if (e.getSource()==start){
			changeCol();
			start.setForeground(Color.white);
		}
		else if (e.getSource()==exit){
			changeCol();
			exit.setForeground(Color.white);
		}
		else{
			changeCol();
		}
	}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}    
    public void mouseClicked(MouseEvent e){}  	 
    //when you click on any of the jlabels it changes the state of the game
    public void mousePressed(MouseEvent e){
		if (e.getSource()==start){
			screenPick="game";//starts the game
		}
		else if (e.getSource()==exit){
			System.exit(0);//exits the game
		}	
	}
	
	public String pick(){
		return screenPick;
	}
	public void rePick(){
		screenPick="menu";
	}
	
	public void mouseDragged(MouseEvent e){}
    public void mouseMoved(MouseEvent e){} 
}