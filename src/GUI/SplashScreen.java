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
* <p> le point de démarrage du projet </p>
* <p>Cette classe se charge pour dessiner l'image de chargement ainsi la durée avant dessiner les outils qu'on aura besoin
* 
* 
*/
public class SplashScreen extends JFrame {
    
	JProgressBar progress; 
	JLabel label;
    
	JPanel boite;
	ImageIcon image;
	JLabel label_image; 
	
	public SplashScreen() {
		super();
		setBounds(300, 200, 500, 300);
		setBackground(Color.white);
		setSize(449,450);
        setUndecorated( true );
        setFocusable( false );
        setEnabled( false );
		boite = new JPanel();
		boite.setBackground(Color.green);
		getContentPane().add(boite);
		image = new ImageIcon( SplashScreen.class.getResource(Parametres.imagepath + "logo.jpg"));
		label_image = new JLabel(image); //creation d'une etiquette
		boite.add(label_image);
		label = new JLabel("Initialisation ...");
		boite.add(label);
		progress = new JProgressBar();
		progresser(progress);
		boite.add(progress);
		setVisible(true);
		
	}
	
	public void avancerProgress(String avancement) {
		progress.setValue(progress.getValue()+progress.getMaximum()/5);
		label.setText(avancement);
	}
	
	
	public  JProgressBar progresser(JProgressBar progress) {
		progress = new JProgressBar(0, 2500); // creation de l'objet
		progress.setValue(0);
		progress.setStringPainted(false);
		return progress;
	}
	

}
