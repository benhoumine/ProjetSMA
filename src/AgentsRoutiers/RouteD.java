/******************************************************************************
* UNE ROUTE EST CARACTERISEE PAR :
* --------------------------------
* identite 					:	identite unique de la route
* vitesse autorisee 		:	vitesse autorisee sur cette route (en 1/4 SimuBloc par tour) 
* longueur					: 	longueur de la route (en nombre de SimuBloc) 
* direction   				:	direction de la route = axe de la route 
* 								(0=Horizontale & 1=Verticale)
* capacite de Voitures 		: 	Nombre de voitures admissibles sur la route 
* etat = sature 			: 	signifie que la route est entieremenet remplie 
* extremite0 & extremite1 	: 	sont les extremites de la route 
*      					  		si elle vaut 0, l'extremite est libre 
* 								si elle vaut 1, l'extremite est bloquee
*
* ACCES A UNE ROUTE : 
* ------------------- 
* Pour entrer sur une route, il faut que l'entree de la route soit libre, que ce soit 
* dans un sens comme dans l'autre. ==> d'ou la necessite de la connaissance des extremites
* ainsi que de son etat. 
* 
* 
* MEMOIRE DE LA ROUTE : 
* ---------------------
* >> UTILISATION DE POINTEURS :
* La route etant un element routier, une infrastructure, il vient naturellement de la 
* connecter par des liens fort avec les carrefours d'extremites (pointeurs). 
* En effet, leur position ne varie pas et une fois routes et carrefours construits, ils 
* forment entre eux une unique infrastructure appelee "Reseau Routier"
* 
* >> DIALOGUE ENTRE ELEMENTS ROUTIERS
* Toutefois, Routes et Carrefours ont leur propres comportement et le systeme etant reparti,
* chacun des carrefours et des routes auront leur propre comportement individuel (la route 
* n'a aucun droit sur le comportement d'un carrefour et vice-versa). Mais ils ont connaissance 
* de leur etat! 
* Le dialogue s'effectue par l'envoie de message. Ainsi, chaque agent agira en fonction de
* la reception de message. 
* 
* >> REPRESENTATION "MEMOIRE" DE LA ROUTE
* La route est simplement represente par deux tableaux qui correspondent aux 2 voies possibles.
* Chaque indice d'un des tableau correspond a 1/4 de SimuBloc ou bien "une case d'avancement". 
* on enregistre a chaque deplacement l'etat de la voiture (fourni via les messages) dans les 
* cases d'avancement. Bien entendu, chaque case ne peut posseder qu'une voiture. Autrement dit: 
* "une voiture par case !" 
* 
* >> MEMOIRE DE L'ETAT DES ELEMENTS ROUTIERS
* Les routes ont connaissance de l'etat des voitures grace aux messages que ces derniers 
* envoient. On garde en memoire l'etat des voitures presentent sur la route ainsi que l'etat 
* des 3 carrefours d'extremite.  
*   
* 
*******************************************************************************/

package AgentsRoutiers;
import java.awt.Frame;
import java.util.Vector;

import javax.swing.JOptionPane;

import Gestionnaire.*;
import Village.*;
import GuiSimuTrafic.*;


public class RouteD extends Thread{

    private int identite;
    private int vitesseAutorisee ;
    private int longueur ;
    private int direction ;  		// 0 = axe Horizontal , 1 = axe Vertical
    private int capaciteVehicules ; 
    public  int extremite0 = 0; 	// 0=libre, 1=bloquee 
    public  int extremite1 = 0; 	// 0=libre, 1=bloquee
    private String etat = "ok";  // etat de la route: sature, etc...
    private Voie[] lesVoies = new Voie[2];
    private EtatRoute etatRouteActuel = null ;    
    
    private Carrefour carrefourD ; 
    private Carrefour carrefourF ; 
    
    private EtatCarrefour etatCarrefourDebut = null ;  
    private EtatCarrefour etatCarrefourFin = null ; 
    private EtatVehicule voiture = null ;   
    
    // attachement a un element graphique
    private ElementRoute elementGraphique = null;
 
 	
    /*-----------------------------------------------------------------------
      VARIABLE POUR LA COMMUNICATION AVEC L'ENVIRONNEMENT
     ------------------------------------------------------------------------
     acces en lecture (et eventuellement ecriture) au gestionnaire de route 
     acces en ecriture au gestionnaire de voiture 
     ------------------------------------------------------------------------*/
    
    GestMsgRoute gestMsgRoute = null;
    GestMsgCarrefour gestMsgCarrefour = null; 
    GestMsgVoiture gestMsgVoiture = null ; 
    String message = null ;
    Message messageRecu = null ;
    Message messageAEnvoyer = null;
    Vector msgComplet =  null ; 
 


    /**
     * Permet de construire une route en definissant ses caracteristiques  
     * Et en donnant un lien sur les gestionnaires de messages.  
     * @param ID int					: identifiant unique pour la route.
     * @param speedAgree int 			: vitesse autorisee sur la route (1=valeur normale).
     * @param longueurR int 			: longueur de la route en nombre de SimuBloc (un bloc de route).
     * @param dir int 					: direction de la route (0=Horizontale, 1=Verticale)
     * @param CarrefourDebut Carrefour 	: carrefour en debut de route (le plus haut ou le plus a gauche).
     * @param CarrefourFin Carrefour	: carrefour en fin de route (le plus bas ou le plus ? droite).
     * @param gestR GestMsgRoute 		: gestionnaire de route
     * @param gestV GestMsgVoiture 		: gestionnaire de Voiture 
     * @param gestC GestMsgCarrefour	: gestionnaire de carrefour 
     * 
     */
    
    public RouteD(int ID, int speedAgree, int longueurR, int dir, CarrefourAFeux CarrefourDebut, CarrefourAFeux CarrefourFin,
    		GestMsgRoute gestR, GestMsgVoiture gestV, GestMsgCarrefour gestC,
			ElementRoute elementGraphique) {
        
    	this.identite = ID ;
        this.vitesseAutorisee = speedAgree;
        this.longueur = longueurR ; 
        this.direction = dir ;
        this.capaciteVehicules = 4 * this.longueur; //une case peut accepter 4 voitures
        this.carrefourD = CarrefourDebut; 
        this.carrefourF = CarrefourFin; 
        if(carrefourD != null && carrefourF != null ) {
        	this.etatCarrefourDebut = CarrefourDebut.etatCarrefourActuel;
        	this.etatCarrefourFin = CarrefourFin.etatCarrefourActuel ;
        } 
        
        this.gestMsgRoute = gestR ;
        this.gestMsgVoiture = gestV ;
        this.gestMsgCarrefour = gestC ; 
        this.etatRouteActuel = new EtatRoute(this.identite, this.vitesseAutorisee, etatCarrefourDebut, etatCarrefourFin);
         
        lesVoies[0] = new Voie(0,this.longueur); 
        lesVoies[1] = new Voie(1,this.longueur);
        
        this.elementGraphique = elementGraphique;
        this.elementGraphique.setElementVillageRoute(this);
    }
    
    
    // ACCESSEURS 
    public void setLongueurRoute(int L) {this.longueur = L; this.capaciteVehicules = 4*L;}
    public void setVitesseAgree(int vitesse) {this.vitesseAutorisee = vitesse;}
    public int getID(){return this.identite;}
    public int getLongueurRoute(){return this.longueur;}
    public int getVitesseAgree(){return this.vitesseAutorisee;}
    public int getIdCarrefourD(){return this.carrefourD.identite;}
    public int getIdCarrefourF(){return this.carrefourF.identite;}
    public ElementRoute getElementGraphique(){return this.elementGraphique;}

    public void setCarrefourD (CarrefourAFeux CarrefourDebut) {
        this.carrefourD = CarrefourDebut; 
        this.etatCarrefourDebut = CarrefourDebut.etatCarrefourActuel;          
        this.mettreAjourEtatRoute();   
    }
    
    public void setCarrefourF(CarrefourAFeux CarrefourFin) {
    	this.carrefourF = CarrefourFin;
    	this.etatCarrefourFin = CarrefourFin.etatCarrefourActuel ;
    	this.mettreAjourEtatRoute();
    }
    
    /**
     * Permet de remettre a jour l'etat de la route actuelle afin que l'envoie 
     * de son etat soit correctement pris en compte.  
     */
    public void mettreAjourEtatRoute(){
        if((carrefourF != null) && (carrefourD != null)) {
        	this.etatCarrefourDebut = this.carrefourD.etatCarrefourActuel;
        	this.etatCarrefourFin = this.carrefourF.etatCarrefourActuel ;   
        	this.etatRouteActuel = new EtatRoute(this.identite, this.vitesseAutorisee, etatCarrefourDebut, etatCarrefourFin);
        }
    }
    
    
    /*------------------------------------------------------------------
      				PROGRAMME EXECUTE PAR LA ROUTE
     -------------------------------------------------------------------*/
    public void run(){
    
        while (true){

        	// 1- ATTENDRE MESSAGE 
        	messageRecu = attendreMsg();
        	
        	// 2- RECUPERER LES INFORMATIONS
        	msgComplet = this.recupInfo(messageRecu); 
        	
        	// determiner le message 
        	message = (String)msgComplet.elementAt(0);
        	if(Parametres.debug) System.out.println("ROUTE "+this.identite+ "		:	Message recu : " + message);
        	
        	// 3- TRAITER LES CAS POSSIBLES  
        	
        	if (message == "msg15"){
        		// faire entrer une voiture venant d'un carrefour 
        		if(Parametres.debug) System.err.println("ROUTE "+this.getID()+"		: 	Msg15 recu"); 
        		faireEntrerVehicule(msgComplet);
        	
        	
        	} else if (message == "msg20"){
        		// voiture veut entrer sur carrefour
        		donnerVoitureCarrefour(msgComplet);
        		if(Parametres.debug) System.out.println("ROUTE "+this.identite+"		: 	Demander au carrefour de faire entrer une voiture");

        		
        	} else if (message == "msg13"){
        		// on traite le cas ou une voiture a ete refusee sur un carrefour
        		traiterVoitureRefusee(msgComplet); 
        	
        		
        	} else if (message == "msg14"){
        		// on traite le cas ou une voiture a ete accepte par le carrefour
        		traiterVoitureAcceptee(msgComplet); 
        	
        	
        	} else if (message == "msg23"){
        		// on traite le cas ou la voiture est en arret 
        		VoitureArretee(msgComplet); 
        	
        	
        	} else if (message == "msg24"){
        		// on traite le cas ou la voiture est en train d'avancer 
        		VoitureAvance(msgComplet); 
        	
        	
        	} else if (message == "msg25"){
        		// un vehicule est mort ... ca fait boom 
        		mortVehicule(msgComplet);
        	
        	
        	} else if (message == "msg30"){
        		// l'utilisateur fait entrer un vehicule sur une route  
        		faireEntrerVehicule(msgComplet); 
        	}
        }
    }


    /*----------------------------------------------------------
     * METHODES POUR TRAITER LA COMMUNICATION
     ----------------------------------------------------------*/

    /**
     * Pour envoyer un message a une voiture 
     * @param msg Message
     */
    public void envoyerMessageVoiture(Message msg, int idVehicule){
        synchronized(gestMsgVoiture){
        	gestMsgVoiture.ajouterMsg(idVehicule, msg); 
        	if(Parametres.debug) System.out.println("ROUTE "+ this.identite+ "	:	msg est " + msg.getMessage().elementAt(0)+" envoye a voiture "+idVehicule);
        }
    }
    
    /**
     * Pour envoyer un message a un carrefour connecte a la route
     * @param msg Message: message a envoyer 
     * @param idCarrefour int : identite du carrefour a qui on envoie le message
     * @param direction int : a valeur dans {0,1} avec 0 = axe Horizontal / 1 = axe Verticale
     */
    public  void envoyerMessageCarrefour(Message msg, int idCarrefour, int direction){
        synchronized(gestMsgCarrefour){
        	gestMsgCarrefour.ajouterMsg(idCarrefour, direction, msg); 
        	if(Parametres.debug) System.out.println("ROUTE "+ this.identite+ "	:	Envoie msg :" + msg.getMessage().get(0)+"au carrefour :"+idCarrefour); 
        }
    }
    
    /**
     * Permet a la route d'attendre un message.
     */
    private Message attendreMsg() {
    Message msgRecu = null ; 
    if(Parametres.debug) System.out.println("ROUTE "+this.identite+"		:	Attente d'un message");
    do{
    	synchronized(gestMsgRoute){
    		msgRecu = (Message) gestMsgRoute.recupMsg(this.identite);
    	}
        if (msgRecu == null){
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
            	if(Parametres.debug) System.err.println("ROUTE "+this.identite+"		:	Erreur d'attente d'un message");
            }
        }
    } while(msgRecu == null);
    return msgRecu ;
    }

    /**
     * Permet de recuperer les informations contenues dans le message recu
     * On extrait le message utile et les informations
     * @param msg
     */
    private synchronized Vector recupInfo(Message msg) {
        
    	Vector vectorMsg = msg.getMessage();
        return vectorMsg ; 
    }
    
    
    /*----------------------------------------------------------
     * METHODES POUR TRAITER LE COMPORTEMENT DE LA VOITURE
     *----------------------------------------------------------*/
    
    /**
     * COMPORTEMENT : on fait entrer un vehicule venant d'un carrefour idCarrefour
     * ou demande par l'utilisateur
     * @param idCarrefour int
     */
    private void faireEntrerVehicule(Vector LeMessage){
    	
    	int extremite = 0 ; // extremite initilialise a libre...
    	int voieActu = 0 ;  // voie actuelle initilialise a 0 ...
    	int idCarrefourActu = -1 ;
    	
    	// 1- preparation de l'etat de la route a fournir a la voiture dans le prochain message
        EtatRoute etatRouteActu = new EtatRoute(this.identite, this.vitesseAutorisee, this.etatCarrefourDebut, this.etatCarrefourFin);
    	
        // 2- Recuperation des donnees du message 
    	Vector vectorMsg = LeMessage ;
        Object objet = vectorMsg.elementAt(1);
        
        if (objet.getClass().getName() == ("AgentsRoutiers.EtatVehicule")){
            
        	// recuperer infos sur la voiture que l'utilisateur fait entrer
            voiture = (EtatVehicule) vectorMsg.elementAt(1);
            etatRouteActu.setEtatVehiculeCourant(voiture);
            voieActu = voiture.getVoie();
            if (voieActu == 1){
            	extremite = extremite0; 
            } else if (voieActu == 0){
            	extremite = extremite1;
            } else {
            	if(Parametres.debug) System.err.println("erreur voie actuelle");
            }
            
            
            if(Parametres.debug) System.out.println("ROUTE "+this.identite+"		:	Recuperation de l'EtatVoiture d'identite  " + voiture.getID());
            this.affichVoiture(etatRouteActu);
        
        
        } else if (objet.getClass().getName() == "AgentsRoutiers.EtatCarrefour") {
        
        	// recuperer infos sur carrefours: la voiture vient d'un carrefour !
        	idCarrefourActu = ((EtatCarrefour) objet).getID(); 
        	
        	if(Parametres.debug) System.out.println("ROUTE "+this.identite+"		:	Recuperation de l'EtatCarrefour :");        	
        	
        	if (idCarrefourActu == carrefourD.identite){
        		// Recuperation du carrefour de debut et de la voiture qui rentre 
        		// La voiture ne peut entrer que sur la voie 1 (car elle vient du carrefourD)
        		// L'extremite est alors celle du D�but : extremite0 
        		etatCarrefourDebut = (EtatCarrefour)objet ;
        		voiture = etatCarrefourDebut.getVehicSortant();
        		if(Parametres.debug) System.out.println("ROUTE"+this.identite+" 		:	Voiture "+voiture.getID()+" entre sur la route"+this.identite);
        		voiture.setVoie(1);
        		etatRouteActu.setEtatVehiculeCourant(voiture);
        		voieActu = 1 ; 
        		extremite = extremite0 ; 
                etatRouteActu.setCarrefourDebut(this.etatCarrefourDebut); 
                
        	} else if (idCarrefourActu == carrefourF.identite) {
        	    // recuperation du carrefour de fin et de la voiture qui rentre
        		// la voiture ne peut rentrer que sur la voie 0 (car elle vient du carrefourF)
        		// L'extremite est alors celle de Fin : extremite1
        		etatCarrefourFin = (EtatCarrefour)objet ;
        		voiture = etatCarrefourFin.getVehicSortant();
        		if(Parametres.debug) System.out.println("ROUTE"+this.identite+" 		:	Voiture "+voiture.getID()+" entre sur la route"+this.identite);
        		voiture.setVoie(0);
        		etatRouteActu.setEtatVehiculeCourant(voiture);
        		voieActu = 0;
        		extremite = extremite1 ; 
        		etatRouteActu.setCarrefourFin(this.etatCarrefourFin);
        	
        	} else {
        		if(Parametres.debug) System.err.println("ROUTE "+this.getID()+"		: 	Probleme sur l'identite du carrefour");
        	}
        }
    	
        
    	// 3- Si l'extremite est libre on peut effectivement faire entrer la voiture
		if (extremite != 1){ //entree libre
    		
    		// 3.1- on ajoute la voiture si possible
			this.voiture.setVoie(voieActu); 
			this.voiture.setLieu("route");
			this.voiture.setDirection(voiture.getVoieChoisie());
			if(Parametres.debug) System.out.println("ROUTE "+this.identite+"		: 	La voiture "+voiture.getID()+ " va vers le/la "+voiture.getDirection());
			this.voiture.setVoieProvenance(this.identite);
			this.voiture.setEtatActuel(0); // immobile quand on entre...  
			this.voiture.setPos(1);
            // mise a jour de la direction du vehicule 
            if (this.direction == 0 && voiture.getVoie() ==1) {
            	voiture.setDirection("droite"); 
            }else if (this.direction == 0 && voiture.getVoie() == 0){
            	voiture.setDirection("gauche");
            }else if (this.direction == 1 && voiture.getVoie() == 1){
            	voiture.setDirection("bas");
            }else if (this.direction == 1 && voiture.getVoie() == 0){
            	voiture.setDirection("haut");
            }
    		
            lesVoies[voieActu].ajouterVehicule(this.voiture);
            
    		etatRouteActu.setEtatVehiculeCourant(voiture);
    		
    		this.affichVoiture(etatRouteActu);
    		
    		// 3.2- on actualise l'etat de la route
    		//    on verifie si la route devient saturee 
    		//    l'extremite d'ou rentre la voiture est forcement bloquee
    		if (lesVoies[voieActu].nbVoitureVoie() == lesVoies[voieActu].tailleVoie()) this.etat = "sature";

    		if (voieActu == 1) {
        		extremite0 = 1 ; 
        	} else if (voieActu == 0) {
        		extremite1 = 1 ;
        	} else {
        		if(Parametres.debug) System.err.println("ROUTE "+this.getID()+"		: 	Erreur sur l'identite du carrefour"); 
        	}

        	if (objet.getClass().getName() == "AgentsRoutiers.EtatCarrefour") {
        		// on signal au carrefour l'eventuel succes
        		this.messageAEnvoyer = new Message(10, etatRouteActu); 
        		this.envoyerMessageCarrefour(messageAEnvoyer, idCarrefourActu, this.direction);
        	}
        	
        	// 3.3- on fait entrer la voiture sur la route et on signal la voiture
        	etatRouteActu.setEtatVehiculeSuiv(lesVoies[voieActu].vehiculeSuivant(voiture.getIndex()));
        	
        	if(Parametres.debug) System.out.println("ROUTE "+this.identite+"		:	voiture est sur la voie " + etatRouteActu.getEtatVehiculeCourant().getVoie());
    		this.messageAEnvoyer = new Message(4 , etatRouteActu);
    		this.envoyerMessageVoiture(messageAEnvoyer, voiture.getID()); 
    		if(Parametres.debug) System.out.println("ROUTE "+this.identite+"		:	Envoie du msg4 a voiture " + voiture.getID());
    	
        	
    		// Affichage graphique : 
    		//Pour Ajouter vehicule
    		if(Parametres.debug) System.out.println("ROUTE "+this.identite+" 	:	Ajout de la voiture " + voiture.getID() + " dans le sens " + (1-voiture.getVoie()));
    		/*Object[] possibilities = {"0", "1", "2"};
  			String s = (String)JOptionPane.showInputDialog(
  			                    new Frame(),
  			                    "Complete the sentence:\n",
  			                    "Customized Dialog",
  			                    JOptionPane.PLAIN_MESSAGE,
  			                    null,
  			                    possibilities,
  			                    "1");
  			int typeVehicule =  Integer.parseInt(s);
  			
    		elementGraphique.ajouterVehicule(1-voiture.getVoie(),typeVehicule);*/

    		//elementGraphique.ajouterVehicule(1-voiture.getVoie(), ElementVehicule.VOITURE_NORMALE_0);
    		elementGraphique.ajouterVehicule(1-voiture.getVoie(),Parametres.TypeVoitureParDefaut);

    		
    	} else {
    		// 1Bis- on n'a pas pu faire entrer la voiture sur la route
    		//       on le signal au carrefour si c'est lui qui l'a fait rentrer
    		
    		if (objet.getClass().getName() == "AgentsRoutiers.EtatCarrefour") {
    			if(Parametres.debug) System.out.println("ROUTE "+this.identite+"		: 	Impossible de faire entrer la voiture "+voiture.getID());
        		this.messageAEnvoyer = new Message(11, etatRouteActu); 
        		this.envoyerMessageCarrefour(messageAEnvoyer, idCarrefourActu, this.direction);	
        	}
    	} 
    }
    
    
    
    /**
     * COMPORTEMENT : la voiture desire entrer sur un carrefour
     * @param idCarrefour
     */
    private void donnerVoitureCarrefour(Vector leMessage){
    
    	int voieActu = -1 ; 
    	int idCarrefourCible = -1 ;
    	
    	// 1- Preparation de l'etatRoute a envoyer au carrefour
    	EtatRoute etatRouteActu = new EtatRoute(this.identite, this.vitesseAutorisee, this.etatCarrefourDebut, this.etatCarrefourFin);
    	
    	// 2- Recuperation des donnees : il s'agit de donnees concernant un vehicule, sinon pb !!
    	Vector vectorMsg = leMessage ; 
        Object objet = vectorMsg.elementAt(1);
         
        if (objet.getClass().getName() == ("AgentsRoutiers.EtatVehicule")){

        	// recuperer infos sur la voiture
            voiture = (EtatVehicule) vectorMsg.elementAt(1);
            voiture.setVoieProvenance(this.identite);
            // axe horizontale = 0 et axe verticale = 1  
            // mise a jour de la direction du vehicule 
            if (this.direction == 0 && voiture.getVoie() ==1) {
            	voiture.setDirection("droite"); 
            }else if (this.direction == 0 && voiture.getVoie() == 0){
            	voiture.setDirection("gauche");
            }else if (this.direction == 1 && voiture.getVoie() == 1){
            	voiture.setDirection("bas");
            }else if (this.direction == 1 && voiture.getVoie() == 0){
            	voiture.setDirection("haut");
            }
            
            etatRouteActu.setEtatVehiculeCourant(voiture);
            etatRouteActu.setEtatVehiculeSortant(voiture);
            this.affichVoiture(etatRouteActu);
            if(Parametres.debug) System.out.println("ROUTE "+this.identite+"		: 	vehicule sortant : " + voiture.getID());
            
            voieActu = voiture.getVoie();
            
            
        	if (voiture.getVoie() == 1){
        		idCarrefourCible = this.carrefourF.identite;
        	} else {
        		idCarrefourCible = this.carrefourD.identite;
        	}
        	
        	messageAEnvoyer = new Message(9, etatRouteActu);  
        	envoyerMessageCarrefour(messageAEnvoyer,idCarrefourCible, this.direction);
        	if(Parametres.debug) System.out.println("ROUTE "+this.identite+"		: 	Msg 9 envoye");
        
        } else if (objet.getClass().getName() == "AgentsRoutiers.EtatCarrefour") {
        	if(Parametres.debug) System.err.println("Route "+this.identite+"		:	Erreur sur l'objet contenu dans le message 20!! ==> comportement non reconnu");
        }
    	
    }
    
    
    
   /**
    * COMPORTEMENT : la voiture a ete refusee par le carrefour. 
    * on envoie juste un message a la voiture en question pour 
    * lui signaler une fin de route
    */ 
    private void traiterVoitureRefusee(Vector leMessage){

    	int idCarrefourActu = -1 ; 
    	int voieActu = -1 ;
    	
    	// 1- Preparation de l'etatRoute a envoyer au carrefour
    	EtatRoute etatRouteActu = new EtatRoute(this.identite, this.vitesseAutorisee, this.etatCarrefourDebut, this.etatCarrefourFin);

    	// 2- Recuperation des donnees : il s'agit de donnees concernant le carrefour, sinon pb !!
    	Vector vectorMsg = leMessage ; 
        Object objet = vectorMsg.elementAt(1);
        
        if (objet.getClass().getName() == ("AgentsRoutiers.EtatVehicule")){
        	if(Parametres.debug) System.err.println("ROUTE "+this.identite+"		:	L'objet contenu dans le message n'est pas reconnu pour ce comportement");
        
        
        } else if (objet.getClass().getName() == "AgentsRoutiers.EtatCarrefour") {
        	
        	// recuperer infos sur carrefours
        	idCarrefourActu = ((EtatCarrefour) objet).getID(); 
        	
        	if (idCarrefourActu == carrefourD.identite){ 
        		// La voie concernee est la voie d'o� sortent les voitures pour entrer sur le carrefour 
        		etatCarrefourDebut = (EtatCarrefour)objet ;
                voieActu = 0 ; 
        	
        	} else if (idCarrefourActu == carrefourF.identite) {
        	    etatCarrefourFin = (EtatCarrefour)objet ;
        		voieActu = 1 ;
        	
        	} else {
        		if(Parametres.debug) System.err.println("ROUTE "+this.getID()+"		: 	Probleme sur l'identite du carrefour");
        	}
        	
        	// on envoie un msg a la voiture pour lui signaler une fin de route
        	voiture = lesVoies[voieActu].recupVehicule((this.longueur*4)-1 );
        	
    		voiture.setEtatActuel(0); // en arret
            if (this.direction == 0 && voiture.getVoie() ==1) {
            	voiture.setDirection("droite"); 
            }else if (this.direction == 0 && voiture.getVoie() == 0){
            	voiture.setDirection("gauche");
            }else if (this.direction == 1 && voiture.getVoie() == 1){
            	voiture.setDirection("bas");
            }else if (this.direction == 1 && voiture.getVoie() == 0){
            	voiture.setDirection("haut");
            }
            
            if(Parametres.debug) System.out.println("ROUTE "+this.getID()+"		: 	Voiture "+voiture.getID()+" sur la voie "+voiture.getVoie()+" a l'index "+voiture.getIndex());
    		etatRouteActu.setEtatVehiculeCourant(voiture);
    		
        	messageAEnvoyer = new Message(8, etatRouteActu); 
        	if(Parametres.debug) System.out.println("ROUTE "+this.getID()+"		: 	Voie actu : " + voieActu);
        	envoyerMessageVoiture(messageAEnvoyer, lesVoies[voieActu].recupVehicule((this.longueur*4)-1).getID());
        }
    }
   
    
    
    /**
     * COMPORTEMENT : La voiture 'v' a ete accepte par le carrefour  
     * @param idVoie
     * @param index
     * @param v
     */
    private void traiterVoitureAcceptee(Vector leMessage){

    	int idCarrefourActu = -1 ; 
    	int voieActu = -1 ;
    	
    	// 1- Preparation de l'etatRoute a envoyer au carrefour
    	EtatRoute etatRouteActu = new EtatRoute(this.identite, this.vitesseAutorisee, this.etatCarrefourDebut, this.etatCarrefourFin);

    	// 2- Recuperation des donnees : il s'agit de donnees concernant le carrefour, sinon pb !!
    	Vector vectorMsg = leMessage ; 
        Object objet = vectorMsg.elementAt(1);
         
        if (objet.getClass().getName() == ("AgentsRoutiers.EtatVehicule")){
        	if(Parametres.debug) System.err.println("ROUTE "+this.identite+"		:	Erreur, l'objet recuperee n'est pas reconnu pour ce comportement");
        
        
        } else if (objet.getClass().getName() == "AgentsRoutiers.EtatCarrefour") {
        	
        	// recuperer infos sur carrefours
        	idCarrefourActu = ((EtatCarrefour) objet).getID(); 
        	
        	if(Parametres.debug) System.out.println("ROUTE"+this.identite+"		:	>> id CarrefourF = "+carrefourF.identite); 
        	if(Parametres.debug) System.out.println("ROUTE"+this.identite+"		:	>> id CarrefourD = "+carrefourD.identite);
        	if(Parametres.debug) System.out.println("ROUTE"+this.identite+" 		:	>> id CarrefourActu RECU = "+idCarrefourActu);
        	
        	if (idCarrefourActu == carrefourD.identite){
        		etatCarrefourDebut = (EtatCarrefour)objet ;
        		voieActu = 0 ; // voie par o� sortent les voitures pour entrer sur le prochain carrefour
        	
        	} else if (idCarrefourActu == carrefourF.identite) {
        	    etatCarrefourFin = (EtatCarrefour)objet ;
        		voieActu = 1 ; // voie par o� sortent les voitures pour entrer sur le prochain carrefour
        	
        	} else {
        		if(Parametres.debug) System.err.println("ROUTE "+this.getID()+"		: 	Probleme sur l'identite du carrefour");
        	}
        	
        	// suppression du vehicule en fin de route     
        	lesVoies[voieActu].supprVehicule(this.longueur*4-1);
           
        	if(Parametres.debug) System.out.println("ROUTE "+this.getID()+"		:	Supression du vehicule");
        	
        	// Appel � methode graphique
        	elementGraphique.supprimerVehicule(1-voieActu);
        }
    }

    
    
    /**
     * COMPORTEMENT voiture est en train d'avancer. 
     * On analyse la prochaine situation. 
     * @param idVoie
     * @param v
     */
    private void VoitureAvance(Vector leMessage){
 
    	int voieActu = -1 ;
    	
    	// 1- Preparation de l'etatRoute a envoyer a la voiture
    	EtatRoute etatRouteActu = new EtatRoute(this.identite, this.vitesseAutorisee, this.etatCarrefourDebut, this.etatCarrefourFin);

    	// 2- Recuperation des donnees : il s'agit de donnees concernant un vehicule, sinon pb !!
    	Vector vectorMsg = leMessage ; 
        Object objet = vectorMsg.elementAt(1);
         
        if (objet.getClass().getName() == ("AgentsRoutiers.EtatCarrefour")){
        	if(Parametres.debug) System.err.println("ROUTE "+this.identite+"		:	Erreur, l'objet recuperee n'est pas reconnu pour ce comportement");
        
        
        } else if (objet.getClass().getName() == "AgentsRoutiers.EtatVehicule") {
            
        	// RECUPERER LES INFOS SUR LA VOITURE 
            voiture = (EtatVehicule) vectorMsg.elementAt(1);
            voieActu = voiture.getVoie();
            if(Parametres.debug) System.out.println("ROUTE "+this.identite+"		:	Recuperation de l'EtatVoiture d'identite  " + voiture.getID()+" sur voie "+voieActu);
            
            // MISE A JOUR POSITION VEHICULE 
            // axe horizontale = 0 et axe verticale = 1  
            // mise a jour de la direction du vehicule 
            if (this.direction == 0 && voiture.getVoie() ==1) {
            	voiture.setDirection("droite"); 
            }else if (this.direction == 0 && voiture.getVoie() == 0){
            	voiture.setDirection("gauche");
            }else if (this.direction == 1 && voiture.getVoie() == 1){
            	voiture.setDirection("bas");
            }else if (this.direction == 1 && voiture.getVoie() == 0){
            	voiture.setDirection("haut");
            }

            etatRouteActu.setEtatVehiculeCourant(voiture);
            this.affichVoiture(etatRouteActu);
            
            if (voiture.getPos() <= 4*longueur){
            	
                	// supprimer la voiture de sa position anterieure
                	lesVoies[voieActu].supprVehicule(voiture.getIndex());
                	
                	// mettre a jour sa position
                	voiture.setIndex(voiture.getPos()-1);
                	if(Parametres.debug) System.out.println("ROUTE "+this.identite+"		: 	Index de la voiture "+voiture.getID()+" apres avancer: " + voiture.getIndex());
                	if(!lesVoies[voieActu].positionnerVehicule(voiture.getIndex(), voiture)){
                		if(Parametres.debug) System.err.println("ROUTE "+getID()+"	: 	Le vehicule "+voiture.getID()+"n'a pas pu etre positionne");
                	}	
                	etatRouteActu.setEtatVehiculeCourant(voiture);
            
            } else {
            
            	voiture.setIndex(4*longueur-1);
            	voiture.setPos(longueur*4);
            	if(Parametres.debug) System.out.println("ROUTE "+this.identite+"		: 	Position de la voiture"+voiture.getID()+": "+voiture.getPos());
            	if(!lesVoies[voieActu].positionnerVehicule(voiture.getIndex(), voiture)){
            		if(Parametres.debug) System.err.println("ROUTE "+getID()+"	: 	Le vehicule "+voiture.getID()+"n'a pas pu etre positionne");
            	}	
            	etatRouteActu.setEtatVehiculeCourant(voiture);
            	
            }
    	
            
            // Mettre a jour les extremites pour liberer ou non l'entree sur une voie
            if (voieActu == 0 && lesVoies[voieActu].recupVehicule(0) == null) extremite1 = 0; // l'entree fin de route est liberee
            if (voieActu == 1 && lesVoies[voieActu].recupVehicule(0) == null) extremite0 = 0; // l'entree debut de route est liberee
            if(Parametres.debug) System.out.println("ROUTE "+this.identite+"		: 	Extremite 0 est :"+ extremite0); 
            if(Parametres.debug) System.out.println("ROUTE "+this.identite+"		: 	Extremite 1 est :"+ extremite1);
    	
            
            // ANALYSE DES VOITURES AUTOUR DU VEHICULE 
            etatRouteActu.setEtatVehiculeSuiv(lesVoies[voieActu].vehiculeSuivant(voiture.getIndex()));
            
            if (etatRouteActu.getEtatVehicSuiv() != null) if(Parametres.debug) System.out.println("ROUTE "+this.identite+" 	:	il y a qqun devant");
    	
            if (voiture.getPos() >= (this.longueur*4)){
            	// on est en fin de route
            	voiture.setEtatActuel(0);
            	voiture.setPos(this.longueur*4);
            	voiture.setIndex(voiture.getPos()-1);
            	etatRouteActu.setEtatVehiculeCourant(voiture); 
            	this.messageAEnvoyer = new Message(8,etatRouteActu); 
            
            
            } else if (etatRouteActu.getEtatVehicSuiv() == null){
            	// la voiture est en tete sur la route et n'est pas arrivee en fin de route
            	// on lui signale d'avancer 
            	messageAEnvoyer = new Message(0,etatRouteActu); 
            	    	
            
            } else if ((voiture.getPos() + voiture.getVitesse() + 4) < etatRouteActu.getEtatVehicSuiv().getPos()){
            	// on signale d'avancer (on a de la marge avant de rencontrer la voiture suivante)
            	messageAEnvoyer = new Message(0,etatRouteActu); 
    	
            
            } else if ((voiture.getPos()+voiture.getVitesse()) < etatRouteActu.getEtatVehicSuiv().getPos()){
            	//on peut signaler avancer en signalant un vehicule devant
            	messageAEnvoyer = new Message(1,etatRouteActu);
    		
            
            } else {
            	// on lui dit de s'arreter : a verifier si d'autres cas possibles
            	if(Parametres.debug) System.out.println("ROUTE "+this.getID()+"		: 	Demande l'arret a la voiture "+voiture.getID());
            	messageAEnvoyer = new Message(6,etatRouteActu);
            }
    	
            this.affichVoiture(etatRouteActu);
            elementGraphique.deplacerVehicule(lesVoies[voieActu].rang(voiture.getIndex()), 1-voieActu, voiture.getPos()/2);
            
            // ENVOIE DU MESSAGE 
            if(Parametres.debug) System.out.println("ROUTE "+this.identite+"		: 	Envoie du message "+ (String)(messageAEnvoyer.getMessage().get(0)));
            envoyerMessageVoiture(messageAEnvoyer, voiture.getID());
    	
            // affichage graphique: il faut avancer 
            if(Parametres.debug) System.out.println("ROUTE "+this.identite+"		: 	Rang voiture:"+ lesVoies[voiture.getVoie()].rang(voiture.getIndex()));

        }
    }
    
    /**
     * COMPORTEMENT Voiture en etat Arret: 
     * Soit la voiture est en fin de route; Soit elle est derriere un vehicule;
     * @param v
     */	
    private void VoitureArretee(Vector leMessage){
    	
    	int voieActu = -1 ;
    	
    	// 1- Preparation de l'etatRoute a envoyer a la voiture
    	EtatRoute etatRouteActu = new EtatRoute(this.identite, this.vitesseAutorisee, this.etatCarrefourDebut, this.etatCarrefourFin);

    	// 2- Recuperation des donnees : il s'agit de donnees concernant un vehicule, sinon pb !!
    	Vector vectorMsg = leMessage ; 
        Object objet = vectorMsg.elementAt(1);
         
        if (objet.getClass().getName() == ("AgentsRoutiers.EtatCarrefour")){
        	if(Parametres.debug) System.err.println("ROUTE "+this.identite+"		:	Erreur, l'objet recuperee n'est pas reconnu pour ce comportement");
        
            
        } else if (objet.getClass().getName() == "AgentsRoutiers.EtatVehicule") {

            // RECUPERER INFOS SUR LA VOITURE
            voiture = (EtatVehicule) objet;
            voieActu = voiture.getVoie();
            
            if (this.direction == 0 && voiture.getVoie() ==1) {
            	voiture.setDirection("droite"); 
            }else if (this.direction == 0 && voiture.getVoie() == 0){
            	voiture.setDirection("gauche");
            }else if (this.direction == 1 && voiture.getVoie() == 1){
            	voiture.setDirection("bas");
            }else if (this.direction == 1 && voiture.getVoie() == 0){
            	voiture.setDirection("haut");
            }
            etatRouteActu.setEtatVehiculeCourant(voiture);
            
            // ANALYSE DES VOITURES AUTOUR DU VEHICULE
            etatRouteActu.setEtatVehiculeSuiv(lesVoies[voieActu].vehiculeSuivant(voiture.getIndex()));
            this.affichVoiture(etatRouteActu);
            
        	if (voiture.getPos() == (this.longueur*4)){
        		// on est en fin de route // precaution car normalement traite avant
        		voiture.setEtatActuel(0);
        		etatRouteActu.setEtatVehiculeCourant(voiture);
        		this.messageAEnvoyer = new Message(8,etatRouteActu); 
        	}
        	else if(etatRouteActu.getEtatVehicSuiv() == null){
        		// on est en tete sur la route
        		voiture.setEtatActuel(1); // en train d'avancer 
        		if(Parametres.debug) System.out.println("ROUTE "+this.identite+" 	:	voiture sur voie "+voiture.getVoie());
        		etatRouteActu.setEtatVehiculeCourant(voiture);
        		messageAEnvoyer = new Message(0,etatRouteActu);
        	}
        	else if(etatRouteActu.getEtatVehicSuiv().getPos() > (voiture.getPos()+voiture.getVitesse())){
        		// la voiture etait bloquee, elle peut maintenant avancer
        		voiture.setEtatActuel(0); 
        		etatRouteActu.setEtatVehiculeCourant(voiture);
        		messageAEnvoyer = new Message(0,etatRouteActu);
        	}
        	else {
        		// la voiture est encore bloquee derriere un vehicule
        		messageAEnvoyer = new Message(1, etatRouteActu); 
        	}
        	
        	// on envoie le message 
        	if(Parametres.debug) System.out.println("ROUTE "+this.identite+"		: 	Envoie du "+(String)messageAEnvoyer.getMessage().elementAt(0)+" pour voiture "+ voiture.getID());
        	envoyerMessageVoiture(messageAEnvoyer, voiture.getID()); 

        }    	
    }
    
    
    /**
     * COMPORTEMENT Mort d'un vehicule: 
     * Le vehicule annonce sa mort, on le supprime de la liste
     * @param v
     */
    private void mortVehicule(Vector leMessage){
    	
    	int voieActu = -1 ;
    	
    	// 1- Preparation de l'etatRoute a envoyer a la voiture
    	EtatRoute etatRouteActu = new EtatRoute(this.identite, this.vitesseAutorisee, this.etatCarrefourDebut, this.etatCarrefourFin);

    	// 2- Recuperation des donnees : il s'agit de donnees concernant un vehicule, sinon pb !!
    	Vector vectorMsg = leMessage ; 
        Object objet = vectorMsg.elementAt(1);
         
        if (objet.getClass().getName() == ("AgentsRoutiers.EtatCarrefour")){
        	if(Parametres.debug) System.err.println("ROUTE "+this.identite+"		:	Erreur, l'objet recuperee n'est pas reconnu pour ce comportement");
            
        } else if (objet.getClass().getName() == "AgentsRoutiers.EtatVehicule") {

        	// RECUPERER INFOS SUR LA VOITURE
            voiture = (EtatVehicule) vectorMsg.elementAt(1);
            if(Parametres.debug) System.out.println("ROUTE "+this.identite+"		:	Recuperation de l'EtatVoiture d'identite  " + voiture.getID());
            if(Parametres.debug) System.out.println("ROUTE "+this.identite+"		:	>> Position de la voiture "+ voiture.getID()+ " : " + voiture.getPos());
            
            voieActu = voiture.getVoie();
            if(Parametres.debug) System.out.println("ROUTE "+this.identite+"		:	>> Voie de la voiture "+ voiture.getID()+ " : " + voiture.getVoie());
        	
        	int index = voiture.getIndex(); 
        	lesVoies[voieActu].supprVehicule(index);
        }
    }

    
    private void affichVoiture(EtatRoute etat){
    	if (Parametres.debug){
    		System.out.println("ROUTE "+this.identite+" 	:		#VOITURE");
    		System.out.println("ROUTE "+this.identite+" 	:		>> ID 			:	"+etat.getEtatVehiculeCourant().getID());
    		System.out.println("ROUTE "+this.identite+" 	:		>> Index 		:	"+etat.getEtatVehiculeCourant().getIndex());
    		System.out.println("ROUTE "+this.identite+" 	:		>> Voie			:	"+etat.getEtatVehiculeCourant().getVoie());
    		System.out.println("ROUTE "+this.identite+" 	:		>> Direction		:	"+etat.getEtatVehiculeCourant().getDirection());
    		System.out.println("ROUTE "+this.identite+" 	:		>> Position		:	"+etat.getEtatVehiculeCourant().getPos());
    		System.out.println("ROUTE "+this.identite+" 	:		>> Vitesse 		:	"+etat.getEtatVehiculeCourant().getVitesse());
    	}
    }

}