package Gestionnaire;
import java.util.*;

import GuiSimuTrafic.Parametres;

/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
* 
* 
*/
public class GestMsgCarrefour {
	private int nbTotalCarrefour ;
	private Vector fileAttenteMsgCarrefour[];
	
	/**
	 * Creation d'un gestionnaire de message pour les carrefours
	 * @param nbCarrefours
	 */
	public GestMsgCarrefour(int nbCarrefours){
		
	   	this.nbTotalCarrefour = nbCarrefours ; 
	   	// chaque carrefour possede deux listes de stockage
	   	this.fileAttenteMsgCarrefour = new Vector[this.nbTotalCarrefour]; 
	   	for (int i=0; i<this.nbTotalCarrefour; i++){
	   		this.fileAttenteMsgCarrefour[i] = new Vector(2);
	   	}
	   	// chaque carrefour possede deux axes : V & H 
	   	// de plus, chaque axe peut enregistrer des messages 
	   	for(int i=0; i<this.nbTotalCarrefour; i++){
            this.fileAttenteMsgCarrefour[i].add(0, new LinkedList()) ;
            this.fileAttenteMsgCarrefour[i].add(1, new LinkedList()) ;
        }
	   	
	}
	
	/*-----------------------------------------------------------------------
	 GESTION DES FILES D'ATTENTE POUR LES MESSAGES A DESTINATION D'UNE ROUTE
	 -----------------------------------------------------------------------*/
                             //MODE FIFO//
 /**
  * Permet de savoir si une file d'attente d'un axe 'direction' du carrefour 'id' est vide.
  * exemple: tester si la liste des messages de l'axe verticale du carrefour id est vide.  
  * @param id int
  * @return boolean
  */
 public synchronized boolean fileAttenteRouteEmpty(int idCarrefour, int direction) {
     return ((LinkedList)(this.fileAttenteMsgCarrefour[idCarrefour].elementAt(direction))).isEmpty(); 
 }

 /**
  * Permet de rajouter un objet dans la file d'attente d'un carrefour 'id'.
  * @param idCarrefour int 	: identite du carrefour
  * @param o Object			: objet a ajouter 
  */
 public synchronized void ajouterMsg(int idCarrefour, int direction, Object o) {
     ((LinkedList)(this.fileAttenteMsgCarrefour[idCarrefour].elementAt(direction))).addFirst(o);
     if(Parametres.debug) System.out.println("-----------GEST--Carrefour "+ idCarrefour +": element ajoute a la file " + direction); 
 }

 /**
  * Permet de recuperer le premier objet insere dans la file d'attente
  * d'un Carrefour 'id', et le supprime de la file apres.
  * @param id int
  * @return Object
  */
 public synchronized Message recupMsg(int idCarrefour, int direction) {
 	if (!((LinkedList)(this.fileAttenteMsgCarrefour[idCarrefour].elementAt(direction))).isEmpty()){
 		if(Parametres.debug) System.out.println("GEST---Carrefour: recuperation d'un message");
         return (Message) (((LinkedList)(this.fileAttenteMsgCarrefour[idCarrefour].elementAt(direction))).removeLast());
     }else{
         return null;
     }
 }

 /**
  * Permet de connaitre la taille de la file d'attente d'un carrefour 'id'.
  * @param id int
  * @return int
  */
 public synchronized int sizeFile(int idCarrefour, int direction){
     return ((LinkedList)(this.fileAttenteMsgCarrefour[idCarrefour].elementAt(direction))).size();
 }

}
