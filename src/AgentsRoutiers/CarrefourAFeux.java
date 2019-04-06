package AgentsRoutiers;
//presque

import Gestionnaire.*;
import GuiSimuTrafic.Parametres;
import Village.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author BENHOUMINE Abdelkhalek & BANE Mamadou
 * 
 * 
 */
public class CarrefourAFeux extends Carrefour {
	//carrefour
	private Logger logger = LoggerFactory.getLogger(CarrefourAFeux.class);
	
	protected Feu feuV = null ;  
	protected Feu feuH = null ;  
	protected int dureeRouge = 100; 
	protected int dureeVert = 100; 
	protected int dureeOrange = 20 ;
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
 * Constructeur de carrefour 
 * @param ID : id du carrefour
 * @param RG : route gauche 
 * @param RH : route haut
 * @param RD : route droite 
 * @param RB : route bas 
 * @param gestR : pour voir l'etat de la route
 * @param gestV : pour voir l'etat de la voiture
 * @param gestC : pour voir l'etat du carrefour
 * @param dureeV : durée du feu vert 
 * @param dureeO : durée du feu orange 
 * @param dureeR : durée du feu rouge
 */
	public CarrefourAFeux(int ID, RouteD RG, RouteD RH, RouteD RD, RouteD RB, GestMsgRoute gestR, GestMsgVoiture gestV, GestMsgCarrefour gestC, int dureeV, int dureeO, int dureeR, ElementCarrefour elementGraphique,ElementFeu[] feux){ 
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
		this.IsRoutUpdated();//Verifier les route modifier (Mise à jour)
		this.initialisationFeux(feux) ; 
		this.etatCarrefourActuel = new EtatCarrefour(this.identite, this.gauche, this.haut,this.droit, this.bas);
	}

	public void mettreAJourCarrefour(){
		IsRoutUpdated();
		this.etatCarrefourActuel = new EtatCarrefour(this.identite, this.gauche, this.haut,this.droit, this.bas);
	}
	
	
	public void mettreEnFonctionnement(){
		this.feuH.start(); 
		this.feuV.start();
		if(Parametres.debug)
			logger.debug("Carrefour"+this.identite+"	: 	Feux en route"); 
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
	
	private void IsRoutUpdated()
	{
		this.gauche = (this.RoadG != null) ; 
		this.haut = (this.RoadH != null);
		this.droit = (this.RoadD != null);
		this.bas =  (this.RoadB != null);
	}
	
	
	/*----------------------------------------------------------
	 * METHODES POUR TRAITER LA COMMUNICATION
	 *----------------------------------------------------------*/
/**
 * 
 * pour faire une recherche dans la liste de messages
 * 1 : voie Verticale
 * @return boolean
 */
	public synchronized boolean chercherMsgV(){
		int voieVerticale= 1 ; 
		messageRecuV = (Message) this.gestMsgCarrefour.recupMsg(this.identite, voieVerticale); 
		return (messageRecuV != null);  
		
	}
	
	/**
	 * 
	 * pour faire une recherche dans la liste de messages
	 * 1 : voie Horizontale
	 * @return boolean
	 */
	public synchronized boolean chercherMsgH(){
		int voieHorizontale = 0; 
		messageRecuH = (Message) this.gestMsgCarrefour.recupMsg(this.identite,voieHorizontale);
		return (messageRecuH!=null);
	}
	/**
	 * Permet de recuperer les informations contenues dans le message recupere 
	 * provenant d'une voie dans l'axe Horizontal
	 *
	 */

	/**
	 * Cette méthode pour récuperer les informations d'un message 
	 */
	public synchronized void recupInfoMessageH(){
		
		int idRouteConcerneeH = -1; 
		
		if (messageRecuH != null){
			Vector messages = messageRecuH.getMessage();
			this.messageH = (String)messages.elementAt(0);
			
			if(Parametres.debug) logger.debug("Carrefour "+this.identite + "	:	Message H recupere: "+ (String)messageRecuH.getMessage().elementAt(0));
			
			//Recuperer le message 
			Object objet = messages.elementAt(1);
		
			if (objet.getClass().getName().equals("AgentsRoutiers.EtatVehicule")){
				this.vehiculeEntrantH = (EtatVehicule) objet ;  
				if(Parametres.debug)
					logger.debug("Carrefour ("+this.identite+") : Voiture "+vehiculeEntrantH.getID()+" reçu  ");
			}else if (objet.getClass().getName().equals("AgentsRoutiers.EtatRoute")){	
				if(Parametres.debug)
					logger.debug("Etat de Carrefour ("+this.identite+")");
					idRouteConcerneeH = (((EtatRoute) objet).getID());
				if(Parametres.debug)
					logger.debug("Carrefour ("+this.identite+") : RH: "+idRouteConcerneeH);
				if (RoadG != null){
					if (idRouteConcerneeH == RoadG.getID()){ 
						this.routeG = (EtatRoute) objet; 
						if (this.routeG.getVehiculeSortant() != null){
							this.vehiculeEntrantH = this.routeG.getVehiculeSortant();
						}
					}
				} 
				if (RoadD != null){ 
					if (idRouteConcerneeH == RoadD.getID()){ 
						this.routeD = (EtatRoute) objet;
						if (this.routeD.getVehiculeSortant() != null){
							this.vehiculeEntrantH = this.routeD.getVehiculeSortant();
						}
					}
				} 
			} 
			
		} else {
			if(Parametres.debug)
				logger.debug("Carrefour "+this.identite+" 	: 	Message recu = null"); 
		}
	}

	/**
	*
	*
	*
	*/
	public synchronized void recupInfoMessageV(){
		
		int idRouteConcerneeV = -1 ; 
		
		if (messageRecuV != null) {
			Vector messages = messageRecuV.getMessage();
			this.messageV = (String)messages.elementAt(0);
			Object objet = messages.elementAt(1);
			
			if (objet.getClass().getName().equals("AgentsRoutiers.EtatVehicule")){
				this.vehiculeEntrantV = (EtatVehicule)objet; 
				
				if(Parametres.debug)logger.debug("Carrefour "+this.identite+ " 	: 	Etat vehicule recupere : voiture recu sur carrefour"); 
				if(Parametres.debug)logger.debug("Carrefour "+this.identite+ " 	: 	Ce vehicule a pour id : " + vehiculeEntrantV.getID());
			
			
				//pour recupérer les informations de l'état et le voie de la route
			} else if (objet.getClass().getName().equals("AgentsRoutiers.EtatRoute")){
				idRouteConcerneeV = (((EtatRoute) objet).getID());
					//Route Haut
				if (RoadH != null){
					if (idRouteConcerneeV == RoadH.getID()){
						this.routeH = (EtatRoute) objet;
						// recuperer infos sur la voiture qui veut entrer si il y en a une 
						if (this.routeH.getVehiculeSortant() != null){
							this.vehiculeEntrantV = this.routeH.getVehiculeSortant();
						}}}
				//Route bas
				if (RoadB != null){
					if (idRouteConcerneeV == RoadB.getID()){
						this.routeB = (EtatRoute)objet;
						if (this.routeB.getVehiculeSortant() != null){
							this.vehiculeEntrantV = this.routeB.getVehiculeSortant();
						}}}}  
			} else {
				if(Parametres.debug)
					logger.debug("Carrefour "+this.identite+"		: 	Message recu = null"); 
			}}
/**
 * 
 * Pour envoyer un message à l'agent Route identifée par un id 
 * @param msg : message
 * @param idRoute : id de la route
 */
	public synchronized void envoyerMessageRoute(Message msg, int idRoute){
		synchronized(gestMsgRoute){
			gestMsgRoute.ajouterMsg(idRoute, msg);
		}}
	/**
	 * 
	 * Pour envoyer un messgage à la voiture de l'id idVoiture
	 * @param msg : le message
	 * @param idVoiture : id de la voiture
	 */
	public synchronized void envoyerMessageVoiture(Message msg, int idVoiture){
		synchronized(gestMsgVoiture){
			gestMsgVoiture.ajouterMsg(idVoiture,msg);
		}}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*-------------------------------------------------------------------
	  METHODES POUR TRAITER LE COMPORTEMENT SUR RECEPTION D'UN MESSAGE
	 *-------------------------------------------------------------------*/


	public synchronized void aiguillerVoitureH(){
	
		if(Parametres.debug)logger.debug("AIGUILLAGE H par le carrefour "+this.identite);
		this.vehiculeSortantH = this.vehiculeEntrantH ;
		etatCarrefourActuel.setVehicSortant(vehiculeSortantH);
		String choixVoie = this.vehiculeSortantH.getVoieChoisie(); 
		if(Parametres.debug)logger.debug("Carrefour "+this.identite+"		: 	La voie choisie par le vehicule "+vehiculeSortantH.getID()+" est la voie de: "+vehiculeSortantH.getVoieChoisie());
		
		// on demande de faire entrer une voiture sur la route
		
		if (choixVoie == "gauche"){
			
			if (RoadG.extremite1==0){  // libre 	
				this.messageAEnvoyerH = new Message(15,this.etatCarrefourActuel);
				if(Parametres.debug)logger.debug("Carrefour "+this.identite+" 	:	Voiture sortante d'ID ="+etatCarrefourActuel.getVehicSortant().getID());
				envoyerMessageRoute(this.messageAEnvoyerH, this.RoadG.getID());
				envoyerMessageRoute(new Message(14, this.etatCarrefourActuel), vehiculeEntrantH.getVoieProvenance());
				
			} else {
				if(Parametres.debug)logger.debug("Carrefour "+this.identite+"		: 	Route gauche bloquee");
				this.messageAEnvoyerH = new Message(13,this.etatCarrefourActuel);
				envoyerMessageRoute(this.messageAEnvoyerH, vehiculeEntrantH.getVoieProvenance());
			}
		
		} else if (choixVoie == "haut"){
			
			if (RoadH.extremite1 == 0){  // libre 
				this.messageAEnvoyerH = new Message(15,this.etatCarrefourActuel);
				if(Parametres.debug)logger.debug("Carrefour "+this.identite+" 	:	Voiture sortante d'ID ="+etatCarrefourActuel.getVehicSortant().getID());
				envoyerMessageRoute(this.messageAEnvoyerH, this.RoadH.getID());	
				envoyerMessageRoute(new Message(14, this.etatCarrefourActuel), vehiculeEntrantH.getVoieProvenance());
				
			} else {
				if(Parametres.debug)logger.debug("Carrefour "+this.identite+"		: 	Route haut bloquee");
				this.messageAEnvoyerH = new Message(13,this.etatCarrefourActuel);
				envoyerMessageRoute(this.messageAEnvoyerH, vehiculeEntrantH.getVoieProvenance());
			}
			
		} else if (choixVoie == "droit"){
			
			if (RoadD.extremite0 == 0){  // libre
				this.messageAEnvoyerH = new Message(15,this.etatCarrefourActuel);
				if(Parametres.debug)logger.debug("Carrefour "+this.identite+" 	:	Voiture sortante d'ID ="+etatCarrefourActuel.getVehicSortant().getID());
				envoyerMessageRoute(this.messageAEnvoyerH, this.RoadD.getID());
				envoyerMessageRoute(new Message(14, this.etatCarrefourActuel), vehiculeEntrantH.getVoieProvenance());
				if(Parametres.debug)logger.debug("route droite d'ID: "+this.RoadD.getID());
			 	
			} else {
				if(Parametres.debug)logger.debug("Carrefour "+this.identite+"		: 	Route droite bloquee");
				this.messageAEnvoyerH = new Message(13,this.etatCarrefourActuel);
				envoyerMessageRoute(this.messageAEnvoyerH, vehiculeEntrantH.getVoieProvenance());
			}
			
		} else if (choixVoie == "bas"){
			
			if (RoadB.extremite0 == 0){  // libre 
				this.messageAEnvoyerH = new Message(15,this.etatCarrefourActuel);
				if(Parametres.debug)logger.debug("Carrefour "+this.identite+" 	:	Voiture sortante d'ID ="+etatCarrefourActuel.getVehicSortant().getID());
				envoyerMessageRoute(this.messageAEnvoyerH, this.RoadB.getID());
				envoyerMessageRoute(new Message(14, this.etatCarrefourActuel), vehiculeEntrantH.getVoieProvenance());
				
			} else {
				if(Parametres.debug)logger.debug("Carrefour "+this.identite+"		: 	Route bas bloquee");
				this.messageAEnvoyerH = new Message(13,this.etatCarrefourActuel);
				envoyerMessageRoute(this.messageAEnvoyerH, vehiculeEntrantH.getVoieProvenance());
			}
			
		} else { 
			// bloquee : signaler refus a la route srce
			this.messageAEnvoyerH = new Message(13,this.etatCarrefourActuel);
			envoyerMessageRoute(this.messageAEnvoyerH, vehiculeEntrantH.getVoieProvenance()); 
		}
		
		if(Parametres.debug)logger.debug("Carrefour "+this.identite+"		: 	Message"+ (String)messageAEnvoyerH.getMessage().elementAt(0) +" et 14 envoyes");
		
	}


	public synchronized void aiguillerVoitureV(){
	
		int idRouteProvenance = this.vehiculeEntrantV.getVoieProvenance();
		if(Parametres.debug)
			logger.debug(String.valueOf(this.vehiculeEntrantV.getVoieProvenance()));
		if(Parametres.debug)logger.debug("AIGUILLAGE V du carrefour "+this.identite);
		this.vehiculeSortantV = this.vehiculeEntrantV ;
		etatCarrefourActuel.setVehicSortant(vehiculeSortantV);
		String choixVoie = this.vehiculeSortantV.getVoieChoisie();
		if(Parametres.debug)logger.debug("Carrefour "+this.identite+"		: 	La voie choisie par le vehicule "+vehiculeSortantV.getID()+" est la voie de: "+vehiculeSortantV.getVoieChoisie());

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
				if(Parametres.debug)logger.debug("Carrefour "+this.identite+" 	: 	Route haut bloquee");
				this.messageAEnvoyerV = new Message(13,this.etatCarrefourActuel);
				envoyerMessageRoute(this.messageAEnvoyerV, vehiculeEntrantV.getVoieProvenance());
			}
			
		} else if (choixVoie == "droit"){
			
			if (RoadD.extremite0 == 0){  // libre 
				this.messageAEnvoyerV = new Message(15,this.etatCarrefourActuel);
				envoyerMessageRoute(this.messageAEnvoyerV, this.RoadD.getID()); 
				envoyerMessageRoute(new Message(14, this.etatCarrefourActuel), vehiculeEntrantV.getVoieProvenance());
				
			} else {
				if(Parametres.debug)logger.debug("Carrefour "+this.identite+"		: 	Route droit bloquee");
				if(Parametres.debug)logger.debug("Carrefour "+this.identite+"		:	id de la route de provenance de la voiture = "+vehiculeEntrantV.getVoieProvenance());
				this.messageAEnvoyerV = new Message(13,this.etatCarrefourActuel);
				envoyerMessageRoute(this.messageAEnvoyerV, vehiculeEntrantV.getVoieProvenance());
			}
			
		} else if (choixVoie == "bas"){
			
			if (RoadB.extremite0 == 0){  // libre 
				this.messageAEnvoyerV = new Message(15,this.etatCarrefourActuel);
				envoyerMessageRoute(this.messageAEnvoyerV, this.RoadB.getID());
				envoyerMessageRoute(new Message(14, this.etatCarrefourActuel), vehiculeEntrantV.getVoieProvenance());
				
			} else {
				if(Parametres.debug)logger.debug("Carrefour "+this.identite+"		: 	Route bas bloquee");
				this.messageAEnvoyerV = new Message(13,this.etatCarrefourActuel);
				envoyerMessageRoute(this.messageAEnvoyerV, vehiculeEntrantV.getVoieProvenance());
			}
			
		} else { 
			// bloquee : signaler refus a la route srce
			if(Parametres.debug)logger.debug("Carrefour "+this.identite+"		: 	REFUS DE VOITURE, ALLER SAVOIR POURQUOI ;) ==> peut etre un mauvais choix de voie");
			this.messageAEnvoyerV = new Message(13,this.etatCarrefourActuel);
			envoyerMessageRoute(this.messageAEnvoyerV, vehiculeEntrantV.getVoieProvenance()); 
		}
		
		if(Parametres.debug)logger.debug("Carrefour "+this.identite+"		: 	message"+ (String)messageAEnvoyerV.getMessage().elementAt(0) +"envoye");
		
	}
	

	public synchronized void refuserVoiture(int idRoute){
		Message refus = new Message(13, etatCarrefourActuel); 
		envoyerMessageRoute(refus, idRoute); 
	}
	

	public synchronized void routeRefusVoiture(int idRoute){
		// Carrefour a demande de faire entrer une voiture sur une route, 
		// mais elle refuse
		// ALGO CHOISI : on renvoie un message voiture refusee
		if(Parametres.debug) 
			logger.debug("Carrefour " + this.identite+"	: 	id de mon etat actuel : "+etatCarrefourActuel.getID());
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