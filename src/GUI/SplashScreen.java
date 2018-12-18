package GUI;

import java.awt.Color;



import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import GuiSimuTrafic.Parametres;

/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
* 
* 
*/
public class SplashScreen extends JFrame {
    
	JProgressBar progress; // pour creer une barre de chargement
	JLabel label;
	
	public SplashScreen() {
		super();
		setBounds(300, 200, 500, 300);
		setBackground(Color.white);
		setSize(449,450);
        setUndecorated( true );
        setFocusable( false );
        setEnabled( false );
        
		JPanel boite;
		ImageIcon image; // pour stocker notre image
		JLabel label_image; // pour stocker l'image dans une �tiquette
		
		
		int num_progress = 0; // compteur de boucle pour le chargement

    // 	definition d'une boite pour mettre le logo
		boite = new JPanel();
		boite.setBackground(Color.red);
		getContentPane().add(boite);

    // 	on place une image dans un label que l'on place dans notre boite
		image = new ImageIcon( SplashScreen.class.getResource(Parametres.imagepath + "logo.jpg")); // recuperation de l'image
		label_image = new JLabel(image); //creation d'une etiquette
		boite.add(label_image);
				
		label = new JLabel("Initialisation...");
		boite.add(label);
		
    // barre de progression
		progress = new JProgressBar(0, 2500); // creation de l'objet
		progress.setValue(0);
		progress.setStringPainted(false);
		boite.add(progress);
		setVisible(true);
		
	}
	
	public void avancerProgress(String avancement) {
		progress.setValue(progress.getValue()+progress.getMaximum()/5);
		label.setText(avancement);
	}
	
	

}
