 /*************************************************************************
 LE CARREFOUR A FEUX EST CARACTERISE PAR : 
 ----------------------------------
 > Son identite
 > Ses voies disponibles 
 > Les routes qui lui sont reliees
 > Le nombre de voies qu'il possede 
 > Les Feux qu'il possede 
 		--> un feu pour les voies Haut et Bas 		:FeuV
 		--> un feu pour les voies gauche et Droit 	:FeuH
 
 REMARQUE: 
 ---------
 Le carrefour n'a pas de comportement actif a sa creation. C'est la mise en 
 place des feux qui regit son comportement. C'est a dire que les feux attaches 
 au carrefour manipulent ce dernier via des methodes. 
 
 
 LE CARREFOUR POSSEDE DEUX GESTIONS 	
 -----------------------------------------------------------------
 >> GESTION VOIES VERTICALES
 >> GESTION VOIES HORIZONTALES 
 
 >> SI UNE DES VOIES EST EN ATTENTE (FEU ROUGE) ,on refuse tout vehicule  
 >> SI UNE DES VOIES EST ACTIF (FEU VERT) , on aiguille les voitures si la 
 	voie de destination est d'accord
 *************************************************************************/

package AgentsRoutiers;
import Gestionnaire.*;
import GuiSimuTrafic.Parametres;
import Village.*;
import java.util.*;
/**
 * @author Naji kawtar & Soukayna etalbi
 *
 */
public class CarrefourAFeux extends Carrefour {

	/*-----------------------------------------------------------------------
	 GESTION DES FEUX DU CARREFOUR
	 -----------------------------------------------------------------------*/
	protected Feu feuV = null ;  
	protected Feu feuH = null ;  
	protected int dureeRouge = 100; 
	protected int dureeVert = 100; 
	protected int dureeOrange = 20 ;
	
	/*-----------------------------------------------------------------------
	 	MEMOIRE DU CARREFOUR A FEUX POUR LA GESTION DE LA COMMUNICATION
	 -----------------------------------------------------------------------*/ 
 	protected EtatVehicule vehiculeEntrantH = null ; 
 	protected EtatVehicule vehiculeSortantH = null ;  
 	protected EtatVehicule vehiculeEntrantV = null ;  
 	protected EtatVehicule vehiculeSortantV = null ;
 	
 	protected String messageH = null ;
	protected String messageV = null ; 
	protected Message messageRecuH = null ;
	protected Message messageRecuV = null ; 
	protected Message messageAEnvoyerH = null ;
	protected Message messageAEnvoyerV = null ; 
	
	/*-----------------------------------------------------------------------
	 CONSTRUCTION DU CARREFOUR A FEUX ET INITIALISATION
	 -----------------------------------------------------------------------*/
	/**
	 * <p>Permet de creer un carrefour, en specifiant </p>: 
	 * <p>> son identite ,</p> 
	 * <p>> les voies disponibles(id voitures : si valeur inferieur a 0 alors pas de route connecte</p> 
	 * <p>> les gestionnaires utilisees pour la communication ,</p> 
	 * <p>> Et les caracteristiques des feux</p> 
	 */
	public CarrefourAFeux(int ID, RouteD RG, RouteD RH, RouteD RD, RouteD RB, 
			GestMsgRoute gestR, GestMsgVoiture gestV, GestMsgCarrefour gestC,
			int dureeV, int dureeO, int dureeR, ElementCarrefour elementGraphique,
			ElementFeu[] feux){ 
		
		// instanciation
		this.identite = ID ; 
		this.RoadG = RG ; 
		this.RoadH = RH ; 
		this.RoadD = RD ; 
		this.RoadB = RB ; 
		this.gestMsgRoute = gestR ; 
		this.gestMsgVoiture = gestV ; 
		this.gestMsgCarrefour = gestC ;
		this.dureeVert = dureeV ; 
		this.dureeOrange = dureeO ;
		this.dureeRouge = dureeR ;
		this.elementGraphique = elementGraphique;
		this.elementGraphique.setElementVillageCarrefour(this);
		
		// mise a jour 
		this.miseAJourExistenceVoies();
		this.initialisationFeux(feux) ; 

		this.etatCarrefourActuel = new EtatCarrefour(this.identite, this.gauche, this.haut,this.droit, this.bas);
	}

	
	public void mettreAJourCarrefour(){
		miseAJourExistenceVoies();
		this.etatCarrefourActuel = new EtatCarrefour(this.identite, this.gauche, this.haut,this.droit, this.bas);
	}
	
	
	public void mettreEnFonctionnement(){
		this.feuH.start(); 
		this.feuV.start();
		if(Parametres.debug)
			System.out.println("Carrefour"+this.identite+"	: 	Feux en route"); 
	}
	
	private void initialisationFeux(ElementFeu[] feux){
		int voie0=-1,voie1=-1, voie2=-1,voie3=-1 ; 
		if (this.gauche) voie0 = 0;
		if (this.haut)   voie1 = 1;
		if (this.droit)  voie2 = 2;
		if (this.bas)    voie3 = 3; 
		this.feuH = new Feu(this, voie0, voie2, "H" ,"vert", this.dureeVert, this.dureeOrange, this.dureeRouge, feux[0]); 
		this.feuV = new Feu(this, voie1, voie3, "V" ,"rouge", this.dureeVert, this.dureeOrange, this.dureeRouge, feux[1]);		
	}
	
	private void miseAJourExistenceVoies(){
		
		if (this.RoadG != null ) {this.gauche = true;}
		else {this.gauche = false;}
		
		if (this.RoadH != null ) {this.haut = true;}
		else { this.haut = false ;}
		
		if (this.RoadD != null ) {this.droit = true;}
		else { this.droit = false ;}
		
		if (this.RoadB != null ) {this.bas = true;}
		else { this.bas = false ;}
		
	}
	
	
	/*----------------------------------------------------------
	 * METHODES POUR TRAITER LA COMMUNICATION
	 *----------------------------------------------------------*/
	
	/**
	 * <p>Permet de chercher un message dans la liste des messages
	 * correspondant a une voie dans l'axe Verticale. </p>
	 * <p>renvoie le message si la boite aux lettres n'est pas vide,  
	 * renvoie null sinon</p>
	 * @return  
	 */
	public synchronized boolean chercherMsgV(){
		// recherche message pour une voie horizontale ==> 1 
		messageRecuV = (Message) this.gestMsgCarrefour.recupMsg(this.identite, 1); 
		if (messageRecuV == null) return false ; 
		else return true ; 
		
	}
	
	/**
	 * <p>Permet de chercher un message dans la liste des messages 
	 * correspondant a une voie dans l'axe Horizontal.</p>  
	 * <p>renvoie le message si non vide et null sinon.</p>
	 * @return
	 */
	public synchronized boolean chercherMsgH(){
		// recherche message pour une voie horizontale ==> 0 
		messageRecuH = (Message) this.gestMsgCarrefour.recupMsg(this.identite, 0);
		if (messageRecuH == null) return false ; 
		else return true ; 
	}
	
	
	/**
	 * Permet de recuperer les informations contenues dans le message recupere 
	 * provenant d'une voie dans l'axe Horizontal
	 *
	 */
	public synchronized void recupInfoMessageH(){
		
		int idRouteConcerneeH = -1; 
		
		if (messageRecuH != null){
			// on recupere les infos "brutes" du paquet
			Vector vectorMsg = messageRecuH.getMessage();
			this.messageH = (String)vectorMsg.elementAt(0);
			if(Parametres.debug) System.out.println("Carrefour "+this.identite + "	:	Message H recupere: "+ (String)messageRecuH.getMessage().elementAt(0));
			Object objet = vectorMsg.elementAt(1);
			
			// si le paquet est de nature "etat vehicule", on recupere le vehicule entrant
			if (objet.getClass().getName() == "AgentsRoutiers.EtatVehicule"){
				this.vehiculeEntrantH = (EtatVehicule) vectorMsg.elementAt(1);  
				if(Parametres.debug)System.out.println("Carrefour"+this.identite+"	: 	Voiture "+vehiculeEntrantH.getID()+" recu sur carrefour " + this.identite);
				
			// si le paquet est de nature "etat route", on recupere les donnees associees  
			}else if (objet.getClass().getName() == "AgentsRoutiers.EtatRoute"){	
				
				// determiner la voie concernee et recuperer infos sur la route 
				if(Parametres.debug)System.out.println("Carrefour "+this.identite+" 	: 	Recuperation de l'EtatRoute ... ");
				idRouteConcerneeH = (((EtatRoute) vectorMsg.elementAt(1)).getID());
				if(Parametres.debug)System.out.println("Carrefour "+this.identite+" 	:	Route H recuperee: "+idRouteConcerneeH);
				
				// on recupere la voiture selon sa voie de provenance  
				if (RoadG != null){
					if (idRouteConcerneeH == RoadG.getID()){ 
						this.routeG = (EtatRoute) vectorMsg.elementAt(1); 
						// recuperer infos sur la voiture qui veut entrer si il y en a une
						if (this.routeG.getVehiculeSortant() != null){
							this.vehiculeEntrantH = this.routeG.getVehiculeSortant();
						}
					}
				} 
				if (RoadD != null){ 
					if (idRouteConcerneeH == RoadD.getID()){ 
						this.routeD = (EtatRoute) vectorMsg.elementAt(1);
						// recuperer infos sur la voiture qui veut entrer
						if (this.routeD.getVehiculeSortant() != null){
							this.vehiculeEntrantH = this.routeD.getVehiculeSortant();
						}
					}
				} 
			} 
			
		} else {
			if(Parametres.debug)
				System.out.println("Carrefour "+this.identite+" 	: 	Message recu = null"); 
		}
	}
	
	/**
	 * 
	 *
	 */
	public synchronized void recupInfoMessageV(){
		
		int idRouteConcerneeV = -1 ; 
		
		if (messageRecuV != null) {
			Vector vectorMsg = messageRecuV.getMessage();
			this.messageV = (String)vectorMsg.elementAt(0);
			Object objet = vectorMsg.elementAt(1);
			
			if (objet.getClass().getName() == "AgentsRoutiers.EtatVehicule"){
				this.vehiculeEntrantV = (EtatVehicule) vectorMsg.elementAt(1); 
				if(Parametres.debug)System.out.println("Carrefour "+this.identite+ " 	: 	Etat vehicule recupere : voiture recu sur carrefour"); 
				if(Parametres.debug)System.out.println("Carrefour "+this.identite+ " 	: 	Ce vehicule a pour id : " + vehiculeEntrantV.getID());
			
			
			} else if (objet.getClass().getName() == "AgentsRoutiers.EtatRoute"){
				
				// determiner la voie concernee et recuperer infos sur la route 
				if(Parametres.debug)System.out.println("Carrefour "+this.identite+"		: 	Recuperation de l'EtatRoute ... ");
				idRouteConcerneeV = (((EtatRoute) vectorMsg.elementAt(1)).getID());
				if(Parametres.debug)System.out.println("Carrefour "+this.identite+"		: 	Route V recuperee d'id: "+idRouteConcerneeV);
				
				// on recupere la voiture selon sa voie de provenance
				if (RoadH != null){
					if (idRouteConcerneeV == RoadH.getID()){
						this.routeH = (EtatRoute) vectorMsg.elementAt(1);
						// recuperer infos sur la voiture qui veut entrer si il y en a une 
						if (this.routeH.getVehiculeSortant() != null){
							this.vehiculeEntrantV = this.routeH.getVehiculeSortant();
						}
					}
				}
				if (RoadB != null){
					if (idRouteConcerneeV == RoadB.getID()){
						this.routeB = (EtatRoute) vectorMsg.elementAt(1);
						// recuperer infos sur la voiture qui veut entrer si il en a une 
						if (this.routeB.getVehiculeSortant() != null){
							this.vehiculeEntrantV = this.routeB.getVehiculeSortant();
						}
					}
				}
			}  
			
		} else {
			if(Parametres.debug)
				System.out.println("Carrefour "+this.identite+"		: 	Message recu = null"); 
		}
	}
	
	
	/**
	 * Pour envoyer un message a la route sur laquelle on roule
	 * @param msg Message
	 * @param idRoute int
	 */
	public synchronized void envoyerMessageRoute(Message msg, int idRoute){
		synchronized(gestMsgRoute){
			gestMsgRoute.ajouterMsg(idRoute, msg);
		}
	}
	
	
	/**
	 * Pour envoyer un message a la voiture id
	 * @param msg Message
	 * @param idVoiture int
	 */
	public synchronized void envoyerMessageVoiture(Message msg, int idVoiture){
		synchronized(gestMsgVoiture){
			gestMsgVoiture.ajouterMsg(idVoiture,msg);
		}
	}
	
	
	
	/*-------------------------------------------------------------------
	  METHODES POUR TRAITER LE COMPORTEMENT SUR RECEPTION D'UN MESSAGE
	 *-------------------------------------------------------------------*/

	/**
	 * COMPORTEMENT AIGUILLERVOITURE provenant d'une voie HORIZONTALE: 
	 * En fonction de la file choisie et de la voie choisie par le vehicule , 
	 * on aiguille la voiture vers la route de son choix. 
	 * c'est a dire que l'on envoie une requete a la route concernee. 
	 * 
	 */
	public synchronized void aiguillerVoitureH(){
	
		if(Parametres.debug)System.out.println("AIGUILLAGE H par le carrefour "+this.identite);
		this.vehiculeSortantH = this.vehiculeEntrantH ;
		etatCarrefourActuel.setVehicSortant(vehiculeSortantH);
		String choixVoie = this.vehiculeSortantH.getVoieChoisie(); 
		if(Parametres.debug)System.out.println("Carrefour "+this.identite+"		: 	La voie choisie par le vehicule "+vehiculeSortantH.getID()+" est la voie de: "+vehiculeSortantH.getVoieChoisie());
		
		// on demande de faire entrer une voiture sur la route
		
		if (choixVoie == "gauche"){
			
			if (RoadG.extremite1==0){  // libre 	
				this.messageAEnvoyerH = new Message(15,this.etatCarrefourActuel);
				if(Parametres.debug)System.out.println("Carrefour "+this.identite+" 	:	Voiture sortante d'ID ="+etatCarrefourActuel.getVehicSortant().getID());
				envoyerMessageRoute(this.messageAEnvoyerH, this.RoadG.getID());
				envoyerMessageRoute(new Message(14, this.etatCarrefourActuel), vehiculeEntrantH.getVoieProvenance());
				
			} else {
				if(Parametres.debug)System.out.println("Carrefour "+this.identite+"		: 	Route gauche bloquee");
				this.messageAEnvoyerH = new Message(13,this.etatCarrefourActuel);
				envoyerMessageRoute(this.messageAEnvoyerH, vehiculeEntrantH.getVoieProvenance());
			}
		
		} else if (choixVoie == "haut"){
			
			if (RoadH.extremite1 == 0){  // libre 
				this.messageAEnvoyerH = new Message(15,this.etatCarrefourActuel);
				if(Parametres.debug)System.out.println("Carrefour "+this.identite+" 	:	Voiture sortante d'ID ="+etatCarrefourActuel.getVehicSortant().getID());
				envoyerMessageRoute(this.messageAEnvoyerH, this.RoadH.getID());	
				envoyerMessageRoute(new Message(14, this.etatCarrefourActuel), vehiculeEntrantH.getVoieProvenance());
				
			} else {
				if(Parametres.debug)System.out.println("Carrefour "+this.identite+"		: 	Route haut bloquee");
				this.messageAEnvoyerH = new Message(13,this.etatCarrefourActuel);
				envoyerMessageRoute(this.messageAEnvoyerH, vehiculeEntrantH.getVoieProvenance());
			}
			
		} else if (choixVoie == "droit"){
			
			if (RoadD.extremite0 == 0){  // libre
				this.messageAEnvoyerH = new Message(15,this.etatCarrefourActuel);
				if(Parametres.debug)System.out.println("Carrefour "+this.identite+" 	:	Voiture sortante d'ID ="+etatCarrefourActuel.getVehicSortant().getID());
				envoyerMessageRoute(this.messageAEnvoyerH, this.RoadD.getID());
				envoyerMessageRoute(new Message(14, this.etatCarrefourActuel), vehiculeEntrantH.getVoieProvenance());
				if(Parametres.debug)System.out.println("route droite d'ID: "+this.RoadD.getID());
			 	
			} else {
				if(Parametres.debug)System.out.println("Carrefour "+this.identite+"		: 	Route droite bloquee");
				this.messageAEnvoyerH = new Message(13,this.etatCarrefourActuel);
				envoyerMessageRoute(this.messageAEnvoyerH, vehiculeEntrantH.getVoieProvenance());
			}
			
		} else if (choixVoie == "bas"){
			
			if (RoadB.extremite0 == 0){  // libre 
				this.messageAEnvoyerH = new Message(15,this.etatCarrefourActuel);
				if(Parametres.debug)System.out.println("Carrefour "+this.identite+" 	:	Voiture sortante d'ID ="+etatCarrefourActuel.getVehicSortant().getID());
				envoyerMessageRoute(this.messageAEnvoyerH, this.RoadB.getID());
				envoyerMessageRoute(new Message(14, this.etatCarrefourActuel), vehiculeEntrantH.getVoieProvenance());
				
			} else {
				if(Parametres.debug)System.out.println("Carrefour "+this.identite+"		: 	Route bas bloquee");
				this.messageAEnvoyerH = new Message(13,this.etatCarrefourActuel);
				envoyerMessageRoute(this.messageAEnvoyerH, vehiculeEntrantH.getVoieProvenance());
			}
			
		} else { 
			// bloquee : signaler refus a la route srce
			this.messageAEnvoyerH = new Message(13,this.etatCarrefourActuel);
			envoyerMessageRoute(this.messageAEnvoyerH, vehiculeEntrantH.getVoieProvenance()); 
		}
		
		if(Parametres.debug)System.out.println("Carrefour "+this.identite+"		: 	Message"+ (String)messageAEnvoyerH.getMessage().elementAt(0) +" et 14 envoyes");
		
	}

	/**
	 * COMPORTEMENT AIGUILLERVOITURE provenant d'une voie VERTICALE: 
	 * En fonction de la file choisie et de la voie choisie par le vehicule , 
	 * on aiguille la voiture vers la route de son choix. 
	 * c'est a dire que l'on envoie une requete a la route concernee. 
	 * 
	 */
	public synchronized void aiguillerVoitureV(){
	
		int idRouteProvenance = this.vehiculeEntrantV.getVoieProvenance();
		if(Parametres.debug)
			System.out.println(this.vehiculeEntrantV.getVoieProvenance());
		if(Parametres.debug)System.out.println("AIGUILLAGE V du carrefour "+this.identite);
		this.vehiculeSortantV = this.vehiculeEntrantV ;
		etatCarrefourActuel.setVehicSortant(vehiculeSortantV);
		String choixVoie = this.vehiculeSortantV.getVoieChoisie();
		if(Parametres.debug)System.out.println("Carrefour "+this.identite+"		: 	La voie choisie par le vehicule "+vehiculeSortantV.getID()+" est la voie de: "+vehiculeSortantV.getVoieChoisie());

		// on demande de faire entrer une voiture sur la route
		
		if (choixVoie == "gauche"){
			
			if (RoadG.extremite1==0){  // libre 	
				this.messageAEnvoyerV = new Message(15,this.etatCarrefourActuel);
				envoyerMessageRoute(this.messageAEnvoyerV, this.RoadG.getID());
				envoyerMessageRoute(new Message(14, this.etatCarrefourActuel), vehiculeEntrantV.getVoieProvenance());
				
			} else {
				this.messageAEnvoyerV = new Message(13,this.etatCarrefourActuel);
				envoyerMessageRoute(this.messageAEnvoyerV, vehiculeEntrantV.getVoieProvenance());
			}
		
		} else if (choixVoie == "haut"){
			
			if (RoadH.extremite1 == 0){  // libre 
				this.messageAEnvoyerV = new Message(15,this.etatCarrefourActuel);
				envoyerMessageRoute(this.messageAEnvoyerV, this.RoadH.getID());
				envoyerMessageRoute(new Message(14, this.etatCarrefourActuel), vehiculeEntrantV.getVoieProvenance());
				
			} else {
				if(Parametres.debug)System.out.println("Carrefour "+this.identite+" 	: 	Route haut bloquee");
				this.messageAEnvoyerV = new Message(13,this.etatCarrefourActuel);
				envoyerMessageRoute(this.messageAEnvoyerV, vehiculeEntrantV.getVoieProvenance());
			}
			
		} else if (choixVoie == "droit"){
			
			if (RoadD.extremite0 == 0){  // libre 
				this.messageAEnvoyerV = new Message(15,this.etatCarrefourActuel);
				envoyerMessageRoute(this.messageAEnvoyerV, this.RoadD.getID()); 
				envoyerMessageRoute(new Message(14, this.etatCarrefourActuel), vehiculeEntrantV.getVoieProvenance());
				
			} else {
				if(Parametres.debug)System.out.println("Carrefour "+this.identite+"		: 	Route droit bloquee");
				if(Parametres.debug)System.out.println("Carrefour "+this.identite+"		:	id de la route de provenance de la voiture = "+vehiculeEntrantV.getVoieProvenance());
				this.messageAEnvoyerV = new Message(13,this.etatCarrefourActuel);
				envoyerMessageRoute(this.messageAEnvoyerV, vehiculeEntrantV.getVoieProvenance());
			}
			
		} else if (choixVoie == "bas"){
			
			if (RoadB.extremite0 == 0){  // libre 
				this.messageAEnvoyerV = new Message(15,this.etatCarrefourActuel);
				envoyerMessageRoute(this.messageAEnvoyerV, this.RoadB.getID());
				envoyerMessageRoute(new Message(14, this.etatCarrefourActuel), vehiculeEntrantV.getVoieProvenance());
				
			} else {
				if(Parametres.debug)System.out.println("Carrefour "+this.identite+"		: 	Route bas bloquee");
				this.messageAEnvoyerV = new Message(13,this.etatCarrefourActuel);
				envoyerMessageRoute(this.messageAEnvoyerV, vehiculeEntrantV.getVoieProvenance());
			}
			
		} else { 
			// bloquee : signaler refus a la route srce
			if(Parametres.debug)System.out.println("Carrefour "+this.identite+"		: 	REFUS DE VOITURE, ALLER SAVOIR POURQUOI ;) ==> peut etre un mauvais choix de voie");
			this.messageAEnvoyerV = new Message(13,this.etatCarrefourActuel);
			envoyerMessageRoute(this.messageAEnvoyerV, vehiculeEntrantV.getVoieProvenance()); 
		}
		
		if(Parametres.debug)System.out.println("Carrefour "+this.identite+"		: 	message"+ (String)messageAEnvoyerV.getMessage().elementAt(0) +"envoye");
		
	}
	
	/**
	 * Signal a la route qui voulait faire entrer un voiture que cela n'est pas 
	 * possible. 
	 * @param idRoute : identite de la route a qui le carrefour repond 
	 */
	
	public synchronized void refuserVoiture(int idRoute){
		Message refus = new Message(13, etatCarrefourActuel); 
		envoyerMessageRoute(refus, idRoute); 
	}
	
	
	
	/**
	 * la route a refuse une voiture 
	 * @param idRoute : identite de la route a qui le carrefour repond
	 */
	public synchronized void routeRefusVoiture(int idRoute){
		// Carrefour a demande de faire entrer une voiture sur une route, 
		// mais elle refuse
		// ALGO CHOISI : on renvoie un message voiture refusee
		if(Parametres.debug) 
			System.out.println("Carrefour " + this.identite+"	: 	id de mon etat actuel : "+etatCarrefourActuel.getID());
		mettreAJourCarrefour();
		Message refus = new Message(13, etatCarrefourActuel); 
		envoyerMessageRoute(refus, idRoute); 
	}
		
	public Feu getFeuH() { 
		return feuH;
	}
	
	public Feu getFeuV() {
		return feuV;
	}

}