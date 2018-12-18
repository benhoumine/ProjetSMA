package Gestionnaire;
import java.util.*;

import GuiSimuTrafic.Parametres;

/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
* 
* 
*/

public class GestMsgRoute {
	
	private int nbTotalRoutes ;
	private LinkedList fileAttenteMsgRoute[];	

	/**
	 * Gestionnaire pour enregistrer les messages ajoutes pas les voitures, 
     * et que lisent les routes
	 * @param nbRoutes
	 */
	public GestMsgRoute(int nbRoutes){
		
		this.nbTotalRoutes = nbRoutes ;
        this.fileAttenteMsgRoute = new LinkedList[this.nbTotalRoutes];
        
        // chaque route possede une file d'attente en mode FIFO
        for(int i=0; i<this.nbTotalRoutes; i++){
            this.fileAttenteMsgRoute[i] = new LinkedList();
        }

	}
	
	
	
	/*-----------------------------------------------------------------------
	 GESTION DES FILES D'ATTENTE POUR LES MESSAGES A DESTINATION D'UNE ROUTE
	 -----------------------------------------------------------------------*/
                              //MODE FIFO//
  /**
   * Permet de savoir si une file d'attente d'une route 'id' est vide.
   * @param id int
   * @return boolean
   */
  public synchronized boolean fileAttenteRouteEmpty(int id) {
      return this.fileAttenteMsgRoute[id].isEmpty();
  }

  /**
   * Permet de rajouter un objet dans la file d'attente d'une route 'id'.
   * @param id int
   * @param o Object
   */
  public synchronized void ajouterMsg(int id, Object o) {
      this.fileAttenteMsgRoute[id].addFirst(o);
      if(Parametres.debug)
      	System.out.println("GEST MSG Route "+id+"		: 	Element ajoute a la file "); 
  }

  /**
   * Permet de recuperer le premier objet insere dans la file d'attente
   * d'une route 'id'.
   * @param id int
   * @return Object
   */
  public synchronized Message recupMsg(int id) {
  	if (!this.fileAttenteMsgRoute[id].isEmpty()){
    //  	System.out.println("GEST MSG Route "+id+"		: recuperation d'un message");
          return ((Message) this.fileAttenteMsgRoute[id].removeLast());
      }else{
          return null;
      }
  }

  /**
   * Permet de connaitre la taille de la file d'attente d'une route 'id'.
   * @param id int
   * @return int
   */
  public synchronized int sizeFile(int id){
      return this.fileAttenteMsgRoute[id].size();
  }

}
