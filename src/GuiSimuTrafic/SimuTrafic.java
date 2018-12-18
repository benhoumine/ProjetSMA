package GuiSimuTrafic;
import java.awt.Dimension;
import java.awt.FlowLayout;
import GUI.*;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;


/**
*
* <p>Title: SimuTrafic</p>
* <p>Description: Classe principale qui cr?e l'IHM </p>
* @author Naji kawtar & Soukayna etalbi
* @version 1.0
* <p> Date: 15/01/2018 </p>
*/

public class SimuTrafic extends JFrame {

  private FlowLayout gestionPrincipal = null;
  private JPanel conteneurPrincipal = null ;
  private SplashScreen leSplash;
  
  protected int largeurFen = 800 ;
  protected int hauteurFen = 550 ;

  public SimuTrafic() {

    super("PROJET SYSTEME MULTI-AGENTS");
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
    boites[0].add(maBarreOutils);
    boites[1].add(Box.createVerticalGlue());
    //boites[1].add(maBarreTitre);
    conteneurPrincipal.add(boites[0]) ;
    conteneurPrincipal.add(boites[1]) ;
    conteneurPrincipal.add(boites[2]) ;


    boites[1].add(Box.createVerticalGlue());
    boites[1].setPreferredSize(new Dimension(600,550));
    boites[1].add(reseauRoutier);
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
  }

}
