/******************************************************************************
* UNE VOITURE EST CARACTERISEE PAR :
* ----------------------------------
* -> son etat actuel     : qui permet a tout autre vehicule de recuperer
*                          certaines informations sur lui.
*
* -> son identite        : c'est a l'utilisateur de lui donner un numero
*                          unique a son instanciation.
*
* -> le lieu             : lieu ou se trouve la voiture
*
* -> le type de conduite : le type de conducteur au volant de la voiture
*
* -> la position         : La position relative de la voiture par rapport
*                          au debut de la route surlaquelle elle se trouve.
*                          Se mesure en nombre de case
*                              --> (une case = 1/4 SimuTraficBloc)
*
* -> la vitesse          : Se mesure en nombre de case par tour
*
* -> etatActuel          : 0 = arret, 1 = avance, 2 = mort
*
* LA VOITURE POSSEDE UNE MEMOIRE POUR AGIR AVEC SON ENVIRONNEMENT:
* ----------------------------------------------------------------
* On instancie deux classes <etatVehicule> et <etatCarrefour> qui permettent
* de connaitre l'etat des vehicules qui precedent la voiture ainsi que l'etat
* du prochain carrefour sur lequel arrive la voiture
*
*******************************************************************************/

package AgentsRoutiers;
import java.util.Random;
import java.util.Vector;

import Gestionnaire.GestMsgRoute;
import Gestionnaire.GestMsgVoiture;
import Gestionnaire.Message;
import GuiSimuTrafic.Parametres;


/**
 *
 * <p>Titre	: 	Voiture </p>	
 * @author Naji kawtar & Soukayna etalbi
 * @version 	1.0
 * ETAT    	: 	les comportements 'AVANCER' ET 'ARRET OBLIGATOIRE' fonctionnent
 * A FAIRE 	: 	les comportements <Fin route> et autres 
 */


public class Voiture extends Thread{

    /*------------------------------------------------
     CARACTERISTIQUE D'UN VEHICULE -valeur par defaut-
     -------------------------------------------------*/
    EtatVehicule etatVehiculeActuel = null;
    private String lieu = "carrefour" ;
    private String typeConduite = "sociable";
    private String direction = "bas";
    private String nom = "";
    private int identite = 0 ;
    private int position = 0 ;
    private int vitesse  = 1 ;
    private int etatActuel = 0 ;
    private int voie ; 
    
    /*----------------------------------------------------------------
      VARIABLE POUR LA COMMUNICATION AVEC L'ENVIRONNEMENT
     -----------------------------------------------------------------*/

    /* acces en ecriture au gestionnaire de msg pour route
       acces en lecture au gestionnaire de msg pour voiture */
    GestMsgRoute gestMsgRoute = null ;
    GestMsgVoiture gestMsgVoiture = null; 
    String message = null ;
    Message messageRecu = null ;
    Message messageAEnvoyer = null;


    /*----------------------------------------------------------------
     MEMOIRE DE LA VOITURE: peu d'elements a retenir et peu de memoire
     -----------------------------------------------------------------*/
    // une structure modifiable pour connaitre l'etat du vehicule suivant
    private EtatVehicule vehiculeSuiv = null ;

    // une structure modifiable pour connaitre l'etat du vehicule precedent
    private EtatVehicule vehiculePrec = null ; 

    // pour connaitre l'etat du prochain carrefour
    private EtatCarrefour prochainCarrefour = null;

    // pour recueillir les informations de la route
    private EtatRoute routeActuelle = null;

    // g�n�rateur al�atoire pour les d�cisions 
	private Random alea = new Random() ; 

	
	
    /*---------------------------
      CONSTRUCTEUR D'UNE VOITURE
      ---------------------------*/

    // voiture par defaut
    /**
     * @param id int 				: identite du vehicule 
     * @param name String 			: nom de la voiture
     * @param gestR GestMsgRoute	: gestionnaire Route pour la communication
     * @param gestV gestMsgVoiture	: gestionnaire Voiture pour la communcation 
     * @param voieActu int 			: voie sur la quelle se trouve la voiture initialement.
     * 									1 = de gauche(ou haut) a droite(ou bas)
     * 									0 = de droite(ou bas) a gauche (ou haut)     
     */
    public Voiture(int id, String name, String direction, GestMsgRoute gestR, GestMsgVoiture gestV, int voieActu){
        this.identite = id ;
        this.nom = name ; 
        this.direction = direction;
        this.voie = voieActu; 
        this.etatVehiculeActuel = new EtatVehicule(this.position,
                                                   this.vitesse,
                                                   this.identite,
                                                   this.etatActuel,
												   this.direction,
												   this.nom,
                                                   this.voie);
        this.gestMsgRoute = gestR ;
        this.gestMsgVoiture = gestV ; 
    }

    // voiture entierement parametree
    public Voiture(int id,
    			   String name,
                   String lieu,
                   String typeConducteur,
                   int position,
                   String direction,
				   int voieActu , 
                   int vitesse,
                   int etatActuel,
                   GestMsgRoute gestR,
				   GestMsgVoiture gestV) {
        this.identite = id;
        this.nom = name; 
        this.lieu = lieu;
        this.typeConduite = typeConducteur;
        this.position = position;
        this.direction = direction;
        this.vitesse = vitesse;
        this.voie = voieActu; 
        this.etatActuel = etatActuel;
        this.etatVehiculeActuel = new EtatVehicule(this.position,
                                                   this.vitesse,
                                                   this.identite,
                                                   this.etatActuel,
												   this.direction,
												   this.nom, 
												   this.voie);
        this.gestMsgRoute = gestR ;
        this.gestMsgVoiture = gestV ; 
    }

    // ACCESSEURS
    public int getID() { return this.identite; }
    public int getPos() { return this.position; }
    public int getVitesse() { return this.vitesse; }
    public int getEtat() { return this.etatActuel; }
    public String getTypeConducteur() { return this.typeConduite; }


    /*-----------------------------------------------------
     *               COMPORTEMENT DE LA VOITURE
     *-----------------------------------------------------*/

    public void run() {

        while (true) {
            // 1- Se mettre en attente des infos sur la route
            messageRecu = attendreMsg();

            // 2- Recuperer les informations du message
            recupInfo(messageRecu) ;

            // 3- Traiter les cas possibles de messages recus
            if (message == "msg6"){
            	if(Parametres.debug) System.out.println("Voiture "+this.identite+"	: 	msg6 recu ==> ARRET obligatoire");
                arretObligatoire();
            }
            else if (message == "msg0"){
            	if(Parametres.debug) System.out.println("Voiture "+this.identite+"	: 	msg0 recu ==> AVANCER");
            	avancer();
            }
            else if (message == "msg8"){
            	if(Parametres.debug) System.out.println("Voiture "+this.identite+"	: 	msg8 recu ==> FIN ROUTE");
            	finRoute();
            }
            else if (message == "msg1"){
            	// on s'approche d'une voiture : on avance en fonction de celle-ci
            	if(Parametres.debug) System.out.println("Voiture "+this.identite+"	: 	msg1 recu ==> voiture devant !!");
            	voitureDevant(); 
            }
            else if (message == "msg2"){
            	// un vehicule s'approche du vehicule actuel
            	//on ne fait rien pour le moment 
            }
            else if (message == "msg3"){
            	// voiture devant & derriere
            	// on fait rien pour le moment.
            }
            else if (message == "msg4"){
            	entrerSurRoute(); 
            }
            else if (message == "msg5"){
            	// ...BOOM ...
            	if(Parametres.debug) System.out.println("Voiture "+this.identite+"	: 	msg5 recu ==> ...BOOM...");
            	boom(); 
            }
            else {
            	if(Parametres.debug) System.out.println("Voiture "+this.identite+"	: 	...message inconnu... = " + message);
            }
        }
    }

    /**********************************************************
     * METHODES POUR TRAITER LA COMMUNICATION
     **********************************************************/

    /**
     * Permet de se mettre en attente d'un message 
     * [On consulte sa liste de message] 
     */
    private Message attendreMsg() {
        Message msgRecu = null;
        if(Parametres.debug) System.out.println("Voiture "+this.identite+"	: 	Attente d'un message");
        do{
        	synchronized(gestMsgVoiture){
        		msgRecu = this.gestMsgVoiture.recupMsg(this.identite);
        	}
            if (msgRecu == null){
                try {
                    Thread.sleep(10);
                }
                catch (InterruptedException e) {
                	if(Parametres.debug) System.err.println("Voiture	"+this.identite+"	: 	Erreur d'attente d'un message");
                }
            }
        } while(msgRecu == null);
        synchronized(gestMsgVoiture){
        	this.gestMsgVoiture.retirerMsg(this.identite);
        	if(Parametres.debug) System.out.println("Voiture "+this.identite+"	: 	message retire de la liste");
        }
       return msgRecu ;
    }

    /**
     * Permet de recuperer les informations contenues dans le message recu
     * On extrait le message utile et les informations sur l'etat de la route
     * @param msg : message dont on recupere les informations.
     */
    private synchronized void recupInfo(Message msg) {
        Vector vectorMsg = msg.getMessage();
        message = (String)vectorMsg.elementAt(0);
        if(Parametres.debug) System.out.println("voiture "+this.identite+"	:	Message recu = "+ message);
        Object objet = vectorMsg.elementAt(1);
        if (objet.getClass().getName() == "AgentsRoutiers.EtatRoute"){
            
        	// recuperer infos sur la route
        	if(Parametres.debug) System.out.println("Voiture "+this.getID()+"	: 	Recuperation de l'EtatRoute ... ");
            routeActuelle = (EtatRoute) vectorMsg.elementAt(1);
            
            // recuperer info sur notre etat ==> actualisation 
            this.etatVehiculeActuel = routeActuelle.getEtatVehiculeCourant();
            if(Parametres.debug) System.out.println("Voiture "+this.getID()+"	: 	Je suis sur la voie " + etatVehiculeActuel.getVoie());
            this.miseAjourVehiculeActuel(); 
            
            // recuperer infos sur le prochain carrefour
            if(Parametres.debug) System.out.println("Voiture "+this.getID()+"	: 	Recuperation de l'Etat du prochain Carrefour.");
            if (this.voie == 1){
            	prochainCarrefour = routeActuelle.getEtatCarrefourFin(); // correspond a 1 (on se dirige vers la droite le ou bas)
            	if(Parametres.debug) System.out.println("Voiture"+this.getID()+"		: 	Prochain Carrefour est d'identite: " + prochainCarrefour.getID());
            }
            else {
            	prochainCarrefour = routeActuelle.getEtatCarrefourDebut(); // correspond a 0 (on se dirige vers gauche ou haut)
            	if(Parametres.debug) System.out.println("Voiture"+this.getID()+"		: 	Prochain Carrefour est d'identite: " + prochainCarrefour.getID());
            }
        
        }else{
        	if(Parametres.debug) System.err.println("Voiture" + this.getID() + "		Message recupere inconnu : ne vient pas d'une route..."); 
        }
    }

    
    /**
     * Pour envoyer un message a la route sur laquelle on roule
     * @param msg Message
     */
    private void envoyerMessageRoute(Message msg){
        int idRoute = this.routeActuelle.getID();
        synchronized(gestMsgRoute){
        	gestMsgRoute.ajouterMsg(idRoute,msg);
        }
    }


    /**********************************************************
     * METHODES POUR TRAITER LE COMPORTEMENT DE LA VOITURE
     **********************************************************/
    
    /**
     * COMPORTEMENT ARRET OBLIGATOIRE
     */
    private void arretObligatoire(){
        this.etatActuel = 0 ;
        this.miseAJourEtatVehicule(); 
        messageAEnvoyer = new Message(23, etatVehiculeActuel) ;
        envoyerMessageRoute(messageAEnvoyer);
        if(Parametres.debug) System.out.println("Voiture "+this.getID()+"	:	Je suis arrete"); 
    }

    /**
     * COMPORTEMENT AVANCER
     */
    private void avancer(){

    	// on avance
        this.etatActuel = 1;
        this.position = this.position + this.vitesse ; 
        this.miseAJourEtatVehicule(); 
        this.afficherVehicule(); // DEBUG
        
        // on envoie un msg a la route pour lui signaler notre avancement
        messageAEnvoyer = new Message(24,this.etatVehiculeActuel);
        envoyerMessageRoute(messageAEnvoyer);
        if(Parametres.debug) System.out.println("Voiture "+this.getID()+"	: 	Msg24 envoye a la route"); 
        
    }

    
    /**
     * COMPORTEMENT FIN DE ROUTE
     */
    private void finRoute(){
        
    	// 1- mise a jour
        this.etatActuel = 0 ;
        this.etatVehiculeActuel.setEtatActuel(0);
        this.afficherVehicule(); // DEBUG
        
        // 2- choisir une voie a prendre au prochain carrefour
        if(Parametres.debug)System.out.println("Voiture "+this.identite+"	: 	Le prochain carrefour est : "+this.prochainCarrefour.getID());
        String choixVoie = choisirVoie(this.prochainCarrefour.getG(),
                                       this.prochainCarrefour.getH(),
                                       this.prochainCarrefour.getD(),
                                       this.prochainCarrefour.getB()); 
        etatVehiculeActuel.setVoieChoisie(choixVoie);
        
        // 3- preparer le message pour la route
        messageAEnvoyer = new Message(20, this.etatVehiculeActuel);
        
        // 4- envoyer le message
        envoyerMessageRoute(messageAEnvoyer);
        if(Parametres.debug) System.out.println("Voiture "+this.identite+"	: 	Msg20 envoye");
        if(Parametres.debug)System.out.println("Voiture "+this.identite+"	: 	Je suis en fin de route, je suis donc arrete");
        
    }
    
    /**
     * Permet de choisir aleatoirement une voie a prendre au prochain carrefour
     * @param G boolean : voie Gauche libre? 
     * @param H boolean : voie Haut libre? 
     * @param D boolean : voie Droit libre?
     * @param B boolean : voie Bas libre?
     * @return String 	: {"gauche" ou "droit" ou "haut" ou "bas"}
     */
    private String choisirVoie(boolean G, boolean H, boolean D, boolean B){

    	String voiePossibles[] = {"gauche", "haut", "droit", "bas"};
    	int valeurInterdite = -1 ;
        boolean voieCorrecte = false;
    	boolean gauche = G ; 
    	boolean haut = H ; 
    	boolean droit = D ; 
    	boolean bas = B ; 
    	int choix ;
    	this.direction = routeActuelle.getEtatVehiculeCourant().getDirection();
    	
    	if (this.direction == "gauche") {	
    		if(Parametres.debug) System.out.println("voiture dans la direction ---> gauche");
    		droit = false ;
    		valeurInterdite = 2;}
    	
    	if (this.direction == "haut") {
    		if(Parametres.debug) System.out.println("voiture dans la direction ---> haut");
    		bas = false ;
    		valeurInterdite = 3;}
    	
    	if (this.direction == "droit") {
    		if(Parametres.debug) System.out.println("voiture dans la direction ---> droite");
    		gauche = false ;
    		valeurInterdite = 0;}
    	
    	if (this.direction == "bas"){
    		if(Parametres.debug) System.out.println("voiture dans la direction ---> bas");
    		haut = false ;
    		valeurInterdite = 1;}
    	
    	
    	do {
    		int choice = alea.nextInt(4); // de 0 a 3 valeurs possibles car 4 voies
    		if(Parametres.debug) System.out.println("choix 	: " +choice);
    	    choix = choice ;
            // on verifie si la voie associee est empruntable
            if (choix == valeurInterdite) voieCorrecte = false; 
            else {
            	switch(choix){
                	case 0: if (gauche != false) voieCorrecte = true ; 
                			break;
                	case 1: if (haut != false) voieCorrecte = true ;
                			break;
                	case 2: if (droit != false) voieCorrecte = true ;
                			break;
                	case 3: if (bas != false) voieCorrecte = true ;
                    		break;
                    default : voieCorrecte = false ; 
            	}
            }
        } while (voieCorrecte == false);
        
    	if(Parametres.debug) System.err.println("Voiture "+this.identite+" 	:	direction initiale = "+this.direction);
        return voiePossibles[choix];
    }

    
    /**
     * COMPORTEMENT QUAND UNE VOITURE EST DEVANT 
     * RELATIVEMENT PROCHE DE NOTRE POSITION
     *
     */
    private void voitureDevant(){
        
    	// 1- Mise a jour des donnees & Analyse de la situation
        int posSuiv=0;
        int distApres=0 ; 
        this.vehiculeSuiv = this.routeActuelle.getEtatVehicSuiv();
        if (vehiculeSuiv != null) {
        	posSuiv = this.vehiculeSuiv.getPos();
        	if(Parametres.debug)System.out.println("voiture "+this.identite+"	:	le vehicule suivant a pour id "+vehiculeSuiv.getID());
        }
        distApres = posSuiv - this.etatVehiculeActuel.getPos();
        if(Parametres.debug)System.out.println("voiture "+this.identite+"	:	le vehicule "+etatVehiculeActuel.getID()+" est a la position "+etatVehiculeActuel.getPos());
        
        // 2- si la voiture devant est a l'arret, on s'arrete si on est trop proche  
        //    sinon on avance en ajustant la vitesse  
        if (this.vehiculeSuiv.getEtatActuel() == 0) {
        	
        	if (this.vitesse >= distApres) {
        		// on s'arrete juste derriere le vehicule 
        		// car on roule trop vite pour avancer
        		this.position = this.vehiculeSuiv.getPos() - 1 ; 
        		this.etatActuel = 0 ; 
        		this.miseAJourEtatVehicule();
        		this.afficherVehicule(); // DEBUG
        		
        		// on prepare le message: je m'arrete
        		messageAEnvoyer = new Message(23, this.etatVehiculeActuel);         	
        	} else {
        		// on avance d'une position proportionnelle a la vitesse 
        		this.position = this.position + vitesse ;
        		this.etatActuel = 1 ;
        		this.miseAJourEtatVehicule();
        		this.afficherVehicule(); // DEBUG
        		
        		// on prepare le message : j'avance 
                messageAEnvoyer = new Message(24, etatVehiculeActuel);
        	}
        	
        } else { 
        	
        	// on avance proportionnellement a la vitesse du conducteur devant
        	if (this.vehiculeSuiv.getVitesse() < this.vitesse){
        		// on se cale sur la vitesse du vehicule qui suit
        		this.vitesse = this.vehiculeSuiv.getVitesse(); 
        		this.position =  this.position + this.vitesse ;  
        		this.miseAJourEtatVehicule();  
	        } else {
	        	// on avance ne change pas sa vitesse 
	        	this.position = this.position + this.vitesse ; 
	        	this.miseAJourEtatVehicule(); 
	        }
    		// on prepare le message : j'avance
        	this.afficherVehicule(); // DEBUG
            messageAEnvoyer = new Message(24, etatVehiculeActuel);
        	
        }	 
        
        // 3- on Envoie un message a la route pour lui signaler le nouvel etat actuel
        this.envoyerMessageRoute(messageAEnvoyer); 
        
    }
    
    /**
     * COMPORTEMENT ENTREE SUR ROUTE. 
     * Permet de traiter l'entree d'un vehicule sur une route 
     *
     */
    private void entrerSurRoute(){
    	
    	this.vitesse = this.routeActuelle.getVitesseAutorisee(); 
    	this.position = 1 ; 
    	this.etatActuel = 0 ; 
    	this.lieu = "route";
    	this.voie = this.routeActuelle.getEtatVehiculeCourant().getVoie(); 
    	this.miseAJourEtatVehicule(); 
   		this.afficherVehicule(); // DEBUG
   		
    	this.messageAEnvoyer = new Message(23,this.etatVehiculeActuel);
    	if(Parametres.debug) System.out.println("voiture "+this.identite+"	:	Msg 23 envoye a route: "+ this.routeActuelle.getID()); 
    	this.envoyerMessageRoute(messageAEnvoyer);
    
    }
    
    /**
     * COMPORTEMENT BOOM: la voiture est morte (accidents, autres...)
     *
     */
    private void boom(){
    	this.etatActuel = 0; 
    	this.vitesse = 0; 
    	this.lieu = "ciel";
    	this.miseAJourEtatVehicule();
    	this.messageAEnvoyer = new Message(25,this.etatVehiculeActuel); 
    	this.envoyerMessageRoute(messageAEnvoyer);
    	this.stop(); 
    }
    
    /**
     * Permet de mettre a jour l'ETAT du vehicule actuel a savoir: 
     * la direction, la position, l'etat actuel, la vitesse, le type de conduite,
     * la voie surlaquelle on roule, et le nom : 
     * afin de transmettre les informations � la route
     *
     */
    private void miseAJourEtatVehicule(){
    	this.etatVehiculeActuel.setDirection(this.direction); 
    	this.etatVehiculeActuel.setPos(this.position);
    	this.etatVehiculeActuel.setEtatActuel(this.etatActuel); 
    	this.etatVehiculeActuel.setVitesse(this.vitesse); 
    	this.etatVehiculeActuel.setTypeDeConduite(this.typeConduite); 
    	this.etatVehiculeActuel.setVoie(this.voie); 
    	this.etatVehiculeActuel.setNom(this.nom);
    }
    
    /**
     * Permet de mettre a jour le vehicule actuel, a savoir: 
     * le lieu, le type de conduite, la direction, le nom, la position, la vitesse
     * l'etat actuel, la voie sur laquelle on roule :  
     * afin de recuperer les informations sur notre etat actuel
     *
     */
    private void miseAjourVehiculeActuel(){
        this.lieu = etatVehiculeActuel.getLieu(); 
        this.typeConduite = etatVehiculeActuel.getTypeDeConduite();
        this.direction = etatVehiculeActuel.getDirection(); 
        this.vitesse  = etatVehiculeActuel.getVitesse() ;
        this.etatActuel = etatVehiculeActuel.getEtatActuel() ;
        this.voie = etatVehiculeActuel.getVoie();
    }
    
    /**
     * Pour verifier les donnees recuperees 
     *
     */
    private void afficherVehicule(){
    	if(Parametres.debug) System.out.println("Voiture "+this.identite+"	:		>> direction  	: "+etatVehiculeActuel.getDirection());; 
    	if(Parametres.debug) System.out.println("Voiture "+this.identite+"	:		>> position    	: "+etatVehiculeActuel.getPos());
    	if(Parametres.debug) System.out.println("Voiture "+this.identite+"	:		>> etat actuel	: "+etatVehiculeActuel.getEtatActuel());
    	if(Parametres.debug) System.out.println("Voiture "+this.identite+"	:		>> vitesse 	: "+etatVehiculeActuel.getVitesse()); 
    	if(Parametres.debug) System.out.println("Voiture "+this.identite+"	:		>> typeconduite	: "+etatVehiculeActuel.getTypeDeConduite()); 
    	if(Parametres.debug) System.out.println(" Voiture "+this.identite+"	:		>> voie 	: "+etatVehiculeActuel.getVoie());
    	if(Parametres.debug) System.out.println("Voiture "+this.identite+"	:		>> nom 		: "+etatVehiculeActuel.getNom());
    }
}
