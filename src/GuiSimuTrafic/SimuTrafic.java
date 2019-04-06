package GuiSimuTrafic;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import GUI.*;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
* 
* 
*/

public class SimuTrafic extends JFrame {

  private FlowLayout gestionPrincipal = null;
  private JPanel conteneurPrincipal = null ;
  JMenu information ,Aide; 
  JMenuItem About , help ; 
  private SplashScreen leSplash;
  private JMenuBar menu;
  protected int largeurFen = 800 ;
  protected int hauteurFen = 550 ;

  public SimuTrafic() {

    super("PROJET SYSTEME MULTI-AGENTS BENHOUMINE/Bane - ISIMA F2");
    this.menu  = new JMenuBar();
    //Information Menu
    information = new JMenu("Information");
    menu.add(information);
    Aide = new JMenu("Aide");
    About  = new JMenuItem("About");
    help = new JMenuItem("help");
    Aide.add(About);
    Aide.add(help);
    menu.add(Aide);
    this.setJMenuBar(menu);
    
    //Action Listener Pour Le menu
   help.addActionListener(new ActionListener() {
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new JOptionPane().showMessageDialog(null, "Cette Application est developpé par BENHOUMINE Abdelkhalek et mamado bane"
    			+ " Dans le Cadre du projet De SMA (ISIMA) Sous l'encadrement Du Mr David HILL\n"
    			+ " Cette application sera disponible sur le git \n"
    			+ " Le nom du groupe doit etre mentionner d'apres chaque utilisation de cette application", "Attention", JOptionPane.INFORMATION_MESSAGE);
	}
   });
    
    leSplash = new SplashScreen();
    leSplash.avancerProgress("Chargement de l'environnement...");
    this.setBackground(Parametres.couleurFond);
    this.setSize(largeurFen, hauteurFen);
    //this.setDefaultLookAndFeelDecorated(true);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setResizable(false);
    
    
    // CREATION DES CONTENEURS
    conteneurPrincipal = new JPanel() ;
    //conteneurPrincipal.setBorder(new BorderFactory()
    conteneurPrincipal.setBackground(Parametres.couleurFond);
    Box boites[] = new Box[3] ;

    boites[0] = Box.createVerticalBox();
    boites[1] = Box.createVerticalBox();
    boites[2] = Box.createVerticalBox();

   
    leSplash.avancerProgress("Chargement des elements de simulation...");
   // BARRE DE SIMULATION
   /*BarreSimulation maBarreSimu = new BarreSimulation(); */
   
   // ZONE D'AFFCHAGE DU RESEAU ROUTIER
   ReseauRoutier reseauRoutier = new ReseauRoutier(150) ; 
   
   // BARRE D'OUTILS
   BarreOutilsVue maBarreOutils = new BarreOutilsVue(reseauRoutier);
   
   leSplash.avancerProgress("Chargement du contexte ...");
   // BARRE DE TITRE
   //BarreTitre maBarreTitre = new BarreTitre(reseauRoutier);

   // MISE EN PAGE
   gestionPrincipal = new FlowLayout(FlowLayout.LEFT);
   conteneurPrincipal.setLayout(gestionPrincipal);
   leSplash.avancerProgress("Mise en place des comportements...");
   // ASSEMBLAGE DES ELEMENTS
   boites[0].add(Box.createVerticalGlue());
    boites[1].add(maBarreOutils);
    //boites[1].add(maBarreTitre);
    conteneurPrincipal.add(boites[0]) ;
    conteneurPrincipal.add(boites[1]) ;
    conteneurPrincipal.add(boites[2]) ;


    boites[0].add(Box.createVerticalGlue());
    boites[0].setPreferredSize(new Dimension(800,550));
    boites[0].add(reseauRoutier);
 //   boite[2].add(maBarreSimu);

    setContentPane(conteneurPrincipal);
    leSplash.avancerProgress("Initilisation terminee");
 
   
    this.pack();    
    this.setLocation(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width/2 - this.getWidth()/2, java.awt.Toolkit.getDefaultToolkit().getScreenSize().height/2 - this.getHeight()/2);
    this.setVisible(true);
    leSplash.setVisible(false);

  }


  public static void main(String[] args) {

  		
  		SimuTrafic monSimuRoutier = new SimuTrafic() ;
  		//monSimuRoutier.show();
  }

}
