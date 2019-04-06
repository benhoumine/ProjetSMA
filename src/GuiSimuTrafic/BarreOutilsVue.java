package GuiSimuTrafic;

import java.awt.GridLayout;
import javax.swing.JPanel;
import GUI.JButtonImage;
import GUI.JLabelOutil;
import Listener.BoutonOutilListener;
import Village.ElementVillage;



/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
* <p> Apres le chargement une boite d'outils sera afficher , alors cette classe qui initialise ses fonctionnalités
* 
*/

public class BarreOutilsVue extends JPanel{

  private int nbBoutons = 7;
  private ReseauRoutier reseau;
  private ElementVillage leVillage;
  private GridLayout layoutOutils ;
  protected JLabelOutil[] mesLabelOutils = new JLabelOutil[10] ;
  protected JButtonImage[] mesBoutons = new JButtonImage[nbBoutons];
  protected String[] nomBoutons = {"simuler", "mouse1", "rmv", "routev2", "road", "voiture", "licence"} ;

  
  public BarreOutilsVue(ReseauRoutier leReseau) {

    layoutOutils = new GridLayout(10,1);
    this.setLayout(layoutOutils);
    this.setSize(122,160);
    this.reseau = leReseau;
    
    // MISE EN PLACE DE LA BARRE DE MENU
    for (int i=0; i<10 ; i++){
      mesLabelOutils[i] = new JLabelOutil( Parametres.imagepath + "bo1.png");
    }

    //Un evenement Listener 
    BoutonOutilListener gestionClik = new BoutonOutilListener( reseau,leVillage,mesBoutons);

    // MISE EN PLACE DES BOUTONS DE LA BARRE D'OUTILS
    for (int j=0; j<mesBoutons.length; j++){
      mesBoutons[j] = new JButtonImage( Parametres.imagepath + nomBoutons[j] + ".png");
      mesBoutons[j].setToolTipText("Outil " + nomBoutons[j]);
      mesBoutons[j].addActionListener(gestionClik);
    }
    //Position Mouse
    mesLabelOutils[1].add(mesBoutons[1]);
    mesBoutons[1].setLocation(20,0);
    
    //Position Remove
    mesLabelOutils[2].add(mesBoutons[2]);
    mesBoutons[2].setLocation(60,0);
    
    //positionRoute
    mesLabelOutils[4].add(mesBoutons[0]);
    mesBoutons[0].setLocation(90,0);
    
    mesLabelOutils[6].add(mesBoutons[3]);
    mesBoutons[3].setLocation(60,0);
    
    mesLabelOutils[7].add(mesBoutons[4]);
    mesBoutons[4].setLocation(20,0);
   
    mesLabelOutils[9].add(mesBoutons[5]);
    mesBoutons[5].setEnabled(false);
    mesBoutons[5].setLocation(5,0);
    mesLabelOutils[9].add(mesBoutons[6]);
    mesBoutons[6].setEnabled(false);
    mesBoutons[6].setLocation(100,0);
    
    for(int i=0; i<10 ; i++){
    	this.add(mesLabelOutils[i]);
    }
    this.setVisible(true);}}
