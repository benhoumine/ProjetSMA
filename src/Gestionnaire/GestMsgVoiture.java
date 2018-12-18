package Gestionnaire;
import java.util.LinkedList;

import GuiSimuTrafic.Parametres;

/**
*
* <p>Titre: Gestionnaire de Messages Pour les voitures</p>
* <p>Description: Permet de gerer l'envoie et l'acces aux messages pour les voitures</p>
* @author Naji kawtar & Soukayna etalbi
* @version 1.0
*/
public class GestMsgVoiture {
	
	private int capaciteVoiture ;
    //private ArrayList listeMsgVoiture ;
	//private int nbTotalRoutes ;
	private LinkedList fileMsgVoiture[] ;	
    
    /**
     * Gestionnaire pour enregistrer les messages ajoutes par les routes, 
     * et que lisent les vehicules
     * @param capaciteVehic
     */
    public GestMsgVoiture(int capaciteVehic){
    	
    	this.capaciteVoiture = capaciteVehic ;
        this.fileMsgVoiture = new LinkedList[this.capaciteVoiture];
        
        // chaque voiture possede une file d'attente en mode FIFO
        for(int i=0; i<this.capaciteVoiture; i++){
            this.fileMsgVoiture[i] = new LinkedList();
        }
    	
    }
    
    
    /*-------------------------------------------------------------------
     GESTION D'UNE LISTE POUR LES MESSAGES A DESTINATION D'UNE VOITURE
     -------------------------------------------------------------------*/

    /**
     * permet d'ajouter un message a destination d'une voiture 
     * identifie par son id
     * @param idVoiture int
     * @param msg Message
     * @return boolean
     */
    public synchronized boolean ajouterMsg(int idVoiture, Message msg){
    	
        this.fileMsgVoiture[idVoiture].addFirst(msg);
        if(Parametres.debug)
        	System.out.println("GEST MSG Voiture "+idVoiture+"		: 	Element ajoute a la file "); 
        
    	return true;
    }
    

    /**
     * Permet de retirer un message de la liste 
     * @param idVoiture int 
     * @return void
     */
    public synchronized void retirerMsg(int idVoiture) {
      synchronized(this.fileMsgVoiture[idVoiture]){
      	this.fileMsgVoiture[idVoiture].removeLast();
      	if(Parametres.debug)
      		System.out.println("GEST MSG VOITURE 	: 	Message de la voiture " + idVoiture + " est retire de la liste");
      }
  }

  /**
   * Permet de recuperer un message a destination d'une voiture
   * identifie par son identite 
   * @param id int 
   */
  public synchronized Message recupMsg(int id){
  	
  	synchronized(this.fileMsgVoiture[id]){
  		if(Parametres.debug)
  			System.out.println("GEST MSG VOITURE 	:	voiture"+id+" cherche un msg");
  		if (!this.fileMsgVoiture[id].isEmpty()){
  			if(Parametres.debug)
  				System.out.println("GEST MSG VOITURE 	: 	Recuperation du message pour la voiture: " + id);
  			return ((Message) this.fileMsgVoiture[id].getLast()); 
  		} else {
  			return null; 
  		}
  	}
  }

}