package GuiSimuTrafic;


import GUI.*;

import javax.swing.*;

import Village.ElementVillage;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
* 
* 
*/

public class BarreOutilsVue extends JPanel{

  private int nbBoutons = 8 ;
  private ReseauRoutier reseau;
  private ElementVillage leVillage;
  private GridLayout layoutOutils ;
  protected JLabelOutil[] mesLabelOutils = new JLabelOutil[10] ;
  protected JButtonImage[] mesBoutons = new JButtonImage[nbBoutons];
  protected String[] nomBoutons = {"simuler", "select", "suppr", "zoom", "route", "carrefour", "voiture", "essence"} ;

  public BarreOutilsVue(ReseauRoutier leReseau) {

    layoutOutils = new GridLayout(10,1);
    this.setLayout(layoutOutils);
    this.setSize(122,160);
    this.reseau = leReseau;
    
    // MISE EN PLACE DE LA BARRE DE MENU
    for (int i=0; i<10 ; i++){
      mesLabelOutils[i] = new JLabelOutil( Parametres.imagepath + "bo"+ (i+1) + ".png");
    }

    // definition d'un gestionnaire d'?coute d'?v?n?ments
    BoutonOutilListener gestionClik = new BoutonOutilListener();

    // MISE EN PLACE DES BOUTONS DE LA BARRE D'OUTILS
    for (int j=0; j<mesBoutons.length; j++){
      mesBoutons[j] = new JButtonImage( Parametres.imagepath + nomBoutons[j] + ".png");
      mesBoutons[j].setToolTipText("Outil " + nomBoutons[j]);
      mesBoutons[j].addActionListener(gestionClik);
    }

    // AGENCEMENT DES ELEMENTS DE LA BARRE D'OUTILS

    mesLabelOutils[5].add(mesBoutons[0]);
    mesBoutons[0].setLocation(80,0);
    mesLabelOutils[2].add(mesBoutons[1]);
    mesBoutons[1].setLocation(30,0);
    mesLabelOutils[3].add(mesBoutons[2]);
    mesBoutons[2].setLocation(50,0);
    mesLabelOutils[4].add(mesBoutons[3]);
    mesBoutons[3].setLocation(70,0);
    mesLabelOutils[6].add(mesBoutons[4]);
    mesBoutons[4].setLocation(95,0);
    mesLabelOutils[7].add(mesBoutons[5]);
    mesBoutons[5].setLocation(65,0);
    mesLabelOutils[8].add(mesBoutons[6]);
    mesBoutons[6].setEnabled(false);
    mesBoutons[6].setLocation(5,0);
    mesLabelOutils[8].add(mesBoutons[7]);
    mesBoutons[7].setEnabled(false);
    mesBoutons[7].setLocation(65,0);

    
    
    for(int i=0; i<10 ; i++){
      this.add(mesLabelOutils[i]);
    }

    this.setVisible(true);

}

  /**
   * <p>Title: ?couteur d'?v?nement sur un bouton </p>
   * <p>Description: action r?aliser lorsque l'on clique sur l'un des boutons de la barre d'outils</p>
   * @author Fok A.C Fr?d?ric & Van Wambeke Nicolas
   * @version 1.0
   */
private class BoutonOutilListener implements ActionListener {

  // gestion des ?venements de boutons
  public void actionPerformed (ActionEvent evenement){

  	if (evenement.getSource() == mesBoutons[0]) {
      leVillage = new ElementVillage(reseau);
  	  leVillage.genererStructureSimulation();
  	  leVillage.demarrerSimulateur();
  	  reseau.setVillage(leVillage);
  	  Parametres.isUsed = "select";
  	  mesBoutons[0].setEnabled(false);
  	  mesBoutons[2].setEnabled(false);
  	  mesBoutons[4].setEnabled(false);
  	  mesBoutons[5].setEnabled(false);
  	  mesBoutons[6].setEnabled(true);
  	  mesBoutons[7].setEnabled(true); 
  	} else if (evenement.getSource() == mesBoutons[1]) {
  		Parametres.isUsed = "select";
  		if(Parametres.debug)
  			System.out.println("select");
  	} else if (evenement.getSource() == mesBoutons[2]){
  		Parametres.isUsed = "suppr" ;
  		if(Parametres.debug)
  			System.out.println("suppr");
  	} else if (evenement.getSource() == mesBoutons[3]) {
  		Parametres.isUsed = "zoom";
  		if(Parametres.debug)
  			System.out.println("zoom");
  	} else if (evenement.getSource() == mesBoutons[4]) {
    	Parametres.isUsed = "route";
    	if(Parametres.debug)
    		System.out.println("route");
    } else if (evenement.getSource() == mesBoutons[5]) {
    	Parametres.isUsed = "carrefour" ;
    	if(Parametres.debug)
    		System.out.println("carrefour");
    } else if (evenement.getSource() == mesBoutons[6]) {   	
    	Parametres.isUsed = "voiture" ;
    	if(Parametres.debug)
    		System.out.println("voiture");
    } else if (evenement.getSource() == mesBoutons[7]) {
    	Parametres.isUsed = "essence";
    	if(Parametres.debug)
    		System.out.println("Essence");
    }

  }
}

}
