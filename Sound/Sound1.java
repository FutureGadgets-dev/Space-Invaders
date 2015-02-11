/*
 * AudioClip only supports .wav, .au, .mid and .aiff
 */

import javax.swing.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.sound.sampled.AudioSystem;

public class Sound1 extends JFrame implements ActionListener {
    JButton play = new JButton("play");
    AudioClip back;
    AudioClip dog;
    
    public Sound1() {
		super("Title Bar");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		play.addActionListener(this);
		back = Applet.newAudioClip(getClass().getResource("spacemusic.au"));
		dog = Applet.newAudioClip(getClass().getResource("dog.wav"));
		
		back.loop();
		add(play);
		pack();
		setVisible(true);
	}


    public void actionPerformed(ActionEvent evt) {
		dog.play();
    }

    public static void main(String[] arguments) {
		Sound1 frame = new Sound1();
    }
}
