package Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import GUI.JButtonImage;
import GuiSimuTrafic.Parametres;
import GuiSimuTrafic.ReseauRoutier;
import Village.ElementVillage;
/***
 * 
 * @author BENHOUMINE Abdelkhalek & MAMADOU Bane
 * 
 * </p>Le but de cette classe est de gérer les évènements et elle est inheritable. </p>
 * 
 */
public final class BoutonOutilListener implements ActionListener {
	
	  protected Logger logger = LoggerFactory.getLogger(Object.class);
	  
	  private int nbBoutons = 10 ; 
	  private ElementVillage leVillage;		
	  private ReseauRoutier reseau;
	  protected JButtonImage[] mesBoutons ; 
	  /**
	   * 
	   * Constructeur pour initialiser les objets de la classe mére
	   * @param reseau
	   * @param leVillage
	   * @param mesBoutons
	   */
		public BoutonOutilListener(ReseauRoutier reseau,ElementVillage leVillage,JButtonImage[] mesBoutons) {
			this.leVillage = leVillage;
			this.mesBoutons = mesBoutons;
			this.reseau = reseau ; 
		}
		
		/**
		 * Constructeur par défaut pour initialiser les attributs
		 */
		public BoutonOutilListener(){
			mesBoutons = new JButtonImage[nbBoutons];
		}

		/**
		 * 
		 * Pour la gestion de la click
		 * @param evenement : qui contient les informations sur celui qui a lancé l'évenement
		 * button[0] : la button simuler 
		 * button[1] : select : curseur souris
		 * button[2] : supprimer
		 * button[3] : choisir la route 
		 * button[4] : choisir le carrfour
		 * button[5] : choisir la voiture 
		 * button[6] : choisir la licence
		 */
	  public void actionPerformed (ActionEvent evenement){		  
	  	if (evenement.getSource() == mesBoutons[0]) {
	      leVillage = new ElementVillage(reseau);
	  	  leVillage.genererStructureSimulation();
	  	  leVillage.demarrerSimulateur();
	  	  reseau.setVillage(leVillage);
	  	  Parametres.isUsed = "select";
	  	  mesBoutons[1].setEnabled(false);
	  	  mesBoutons[2].setEnabled(false);
	  	  mesBoutons[3].setEnabled(false);
	  	  mesBoutons[4].setEnabled(false);
	  	  mesBoutons[5].setEnabled(true);
	  	  mesBoutons[6].setEnabled(true); 
	  	} else if (evenement.getSource() == mesBoutons[1]) {
	  		Parametres.isUsed = "select";
	  		if(Parametres.debug)
	  			logger.debug("select");
	  	} else if (evenement.getSource() == mesBoutons[2]){
	  		Parametres.isUsed = "suppr" ;
	  		if(Parametres.debug)
	  			logger.debug("suppr");
	  	}  else if (evenement.getSource() == mesBoutons[3]) {
	    	Parametres.isUsed = "route";
	    	if(Parametres.debug)
	    		logger.debug("route");
	    } else if (evenement.getSource() == mesBoutons[4]) {
	    	Parametres.isUsed = "carrefour" ;
	    	if(Parametres.debug)
	    		logger.debug("carrefour");
	    } else if (evenement.getSource() == mesBoutons[5]) {   	
	           	Parametres.isUsed = "voiture" ;
	    	if(Parametres.debug)
	    		logger.debug("voiture");
	    } else if (evenement.getSource() == mesBoutons[6]) {
	       	Parametres.isUsed = "essence";
	       	// pour lancer le dialogue
	    	new JOptionPane().showMessageDialog(null, "Cette Application est developpé par BENHOUMINE Abdelkhalek et mamado bane"
	    			+ " Dans le Cadre du projet De SMA (ISIMA) Sous l'encadrement Du Mr David HILL\n"
	    			+ " Cette application sera disponible sur le git \n"
	    			+ " Le nom du groupe doit etre mentionner d'apres chaque utilisation de cette application", "Attention", JOptionPane.WARNING_MESSAGE);
	    	if(Parametres.debug)
	    		logger.debug("Essence");
	    }

	  }
	  
	  public int getNombreButton() {
		  return this.getNombreButton();
	  }
	}