
package Gestionnaire;
import java.util.*;

import GuiSimuTrafic.Parametres;

/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
* 
* 
*/

public class GestMessage {

    private int capaciteVoiture ;
    private ArrayList listeMsgVoiture ;
    private int nbTotalRoutes ;
    private LinkedList fileAttenteMsgRoute[];
    private int nbTotalCarrefour ;

    public GestMessage(int capaciteVehic, int nbCarrefour, int nbRoutes) {

        // taille pour accepter 'capaciteVoiture' voiture
        this.capaciteVoiture = capaciteVehic ;
        this.listeMsgVoiture = new ArrayList(this.capaciteVoiture);
        
        this.nbTotalRoutes = nbRoutes ;
        this.fileAttenteMsgRoute = new LinkedList[this.nbTotalRoutes];
        // chaque route poss?de une file d'attente en mode FIFO
        for(int i=0; i<this.nbTotalRoutes; i++){
            this.fileAttenteMsgRoute[i] = new LinkedList();
        }

        // [? d?finir pour le carrefour]
        this.nbTotalCarrefour = nbCarrefour ;
    }


    /*-------------------------------------------------------------------
      GESTION D'UNE LISTE POUR LES MESSAGES A DESTINATION D'UNE VOITURE
      -------------------------------------------------------------------*/

    // pour ajouter un message a destination d'une voiture (id)
    public synchronized boolean ajouterMsgVoiture(int idVoiture, Message msg){
        int taille = this.listeMsgVoiture.size();
        if(Parametres.debug)
        	System.out.println("----------------taille listeArray voiture = " + taille);
        int difference = idVoiture - taille ;
        if(Parametres.debug)
        	System.out.println("----------------difference listeArray voiture = " + difference);
        synchronized(this.listeMsgVoiture){
        	if ((idVoiture+1) > taille) {
        		this.listeMsgVoiture.ensureCapacity(taille+difference+1); // on assure une capacit? minimale        	
        	}
//        if (listeMsgVoiture.get(idVoiture) == null) {
            this.listeMsgVoiture.add(idVoiture, msg);
    //        this.listeMsgVoiture.trimToSize(); // on adapte la taille de la liste
//            System.out.println("Message pour la voiture " + idVoiture + " est ajout? ? la liste");
  //          System.out.println("-----------------taille listeArray voiture = " + listeMsgVoiture.size());
        }
        return true;
  //          return true ; // op?ration r?ussie
    //    } else {
      //      return false ; // op?ration echou?e
      //  }
    }

    // pour retirer un message de la liste
    public synchronized void retirerMsgVoiture(int idVoiture) {
        synchronized(this.listeMsgVoiture){
        	this.listeMsgVoiture.remove(idVoiture);
        	if(Parametres.debug)
        		System.out.println("Message de la voiture " + idVoiture + " est retir? de la liste");
        }
    }

    // pour vider toute la liste des messages
    public synchronized void viderListeVoiture(){
    	synchronized(this.listeMsgVoiture){
    		this.listeMsgVoiture.clear();
    		if(Parametres.debug)
    			System.out.println("liste de message pour les voitures ... vid?e");
    	}
    }

    // pour recuperer un message a destination d'une voiture identifi? par son id
    public synchronized Message recupMsgVoiture(int id){
    	synchronized(this.listeMsgVoiture){
    		if ((!listeMsgVoiture.isEmpty()) && (listeMsgVoiture.size()>id)){
    			if(Parametres.debug)
    				System.out.println("Recuperation du message pour la voiture: " + id);
    			return ((Message) listeMsgVoiture.get(id)); 
    		} else {
    			return null; 
    		}
    	}
    }
    /*-----------------------------------------------------------------------
      GESTION DES FILES D'ATTENTE POUR LES MESSAGES A DESTINATION D'UNE ROUTE
      -----------------------------------------------------------------------*/
                                //MODE FIFO//
    /**
     * Pour savoir si une file d'attente d'une route 'id' est vide.
     * @param id int
     * @return boolean
     */
    public synchronized boolean fileAttenteRouteEmpty(int id) {
        return this.fileAttenteMsgRoute[id].isEmpty();
    }

    /**
     * Pour rajouter un objet dans la file d'attente d'une route 'id'.
     * @param id int
     * @param o Object
     */
    public synchronized void ajouterMsgRoute(int id, Object o) {
        this.fileAttenteMsgRoute[id].addFirst(o);
        if(Parametres.debug)
        	System.out.println("--------------------Route " + id +": ?l?ment ajout? ? la file "); 
    }

    /**
     * Pour récupérer le premier objet inséré dans la file d'attente
     * d'une route 'id'.
     * @param id int
     * @return Object
     */
    public synchronized Message recupMsgRoute(int id) {
    	if (!this.fileAttenteMsgRoute[id].isEmpty()){
    		if(Parametres.debug)
    			System.out.println("Route" + id + ": récupération d'un message");
            return ((Message) this.fileAttenteMsgRoute[id].removeLast());
        }else{
            return null;
        }
    }

    /**
     * Pour conna?tre la taille de la file d'attente d'une route 'id'.
     * @param id int
     * @return int
     */
    public synchronized int sizeFileRoute(int id){
        return this.fileAttenteMsgRoute[id].size();
    }
}
