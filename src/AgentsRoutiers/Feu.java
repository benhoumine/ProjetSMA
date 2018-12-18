package AgentsRoutiers;

import GuiSimuTrafic.Parametres;
import Village.ElementFeu;

/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
*/

public class Feu extends Thread{

	private String etat ; 
	private int dureeRouge ;
	private int dureeVert ; 
	private int dureeOrange ;
	private int voie1 = -1 ; // inexistant par d?faut sur la voie 1 
	private int voie2 = -1 ; 
	private String direction = ""; 
	private CarrefourAFeux monCarrefour = null; 
	private ElementFeu feuxGraphiques;
	
	private int compteur = 0; 
	
	// CONSTRUCTION DU FEU
	/**
	 * Description: un feu est associe a deux voies : (Gauche & Droite) ou (haut & bas) 
	 * @param carrefourAssocie CarrefourAFeux : carrefour auquel est associe le feu.
	 * @param numVoie1 int : numero de la voie 1 surlaquelle le feu se trouve (0=gauche, 1=haut, 2=droit,3=bas). 
	 * @param numVoie2 int : numero de la voie 1 surlaquelle le feu se trouve. 
	 * @param orientation String : placemement des feux (Verticale / Horizontale).
	 * @param etatDuFeu String : etat (couleur) initiale du feu : Rouge, vert ...
	 * @param dureeV int : duree du feu Vert 
	 * @param dureeO int : duree du feu Orange
	 * @param dureeR int : duree du feu Rouge
	 */
	public Feu(CarrefourAFeux carrefourAssocie, int numVoie1, int numVoie2, String orientation,
			String etatDuFeu, int dureeV, int dureeO, int dureeR, ElementFeu feuGraphique){
		
		this.monCarrefour = carrefourAssocie ;
		if (numVoie1 != -1) this.voie1 = numVoie1 ; 
		if (numVoie2 != -1) this.voie2 = numVoie2 ;
		this.direction = orientation; 
		this.etat = etatDuFeu ; 
		feuGraphique.changerEtat(this.etat);
		this.dureeVert = dureeV ;
		this.dureeOrange = dureeO ; 
		this.dureeRouge = dureeR ;
		this.feuxGraphiques = feuGraphique;

	}
	

	
	public int getDureeOrange() {
		return dureeOrange;
	}
	public void setDureeOrange(int dureeOrange) {
		this.dureeOrange = dureeOrange;
	}
	public int getDureeRouge() {
		return dureeRouge;
	}
	public void setDureeRouge(int dureeRouge) {
		this.dureeRouge = dureeRouge;
	}
	public int getDureeVert() {
		return dureeVert;
	}
	public void setDureeVert(int dureeVert) {
		this.dureeVert = dureeVert;
	}
	public String getDirection(){
		return this.direction; 
	}
	
	//protected Message reception = new Message(0, null);
	//protected String message = "" ;
	
	
	/* COMPORTEMENT DU FEU : on analyse l'etat du feu et on attend d'une duree
	   correspondant a la couleur du feu puis on change d'etat */
	
	public void run() {
		while (true) {
			
	 		/*--------------------------------------------------------
	 		 * 			FEU A L'ETAT ROUGE  
	 		 ---------------------------------------------------------*/
			if (this.etat == "rouge"){ 
				//System.out.println("Carrefour "+this.monCarrefour.identite+": FEU "+this.direction+" ROUGE"); 
				if (compteur != this.dureeRouge) {
					
					if (this.direction == "V") {
						
						// 1- Verifier si une voiture veut entrer et l'aiguiller	
						boolean messageRecu = false;  
						messageRecu = monCarrefour.chercherMsgV() ;
						
						if (messageRecu == true){
							// 2- Recuperer les informations du message
							monCarrefour.recupInfoMessageV() ;
						
							// 3- Traiter les cas possibles 
							if (monCarrefour.messageV == "msg9"){
								if(Parametres.debug)
									System.out.println("FEU V 	:	rouge - msg9recu");
								// la route demande d'aiguiller, mais on est rouge,  
								//on refuse la voiture: on renvoie un message refuser
								monCarrefour.routeRefusVoiture(monCarrefour.vehiculeEntrantV.getVoieProvenance()); 
								if(Parametres.debug)
									System.out.println("carrefour: feu rouge: je refuse une voiture");
							} else {
								if(Parametres.debug)
									System.out.println("carrefour: message recu, on fait rien"); 
							}
						}
						
					} else if (this.direction == "H"){
						
						// 1- verifier si une voiture veut entrer et l'aiguiller	
						boolean messageRecu = false;  
						messageRecu = monCarrefour.chercherMsgH() ;
						
						if (messageRecu == true){
							// 2- Recuperer les informations du message
							monCarrefour.recupInfoMessageH() ;
						
							// 3- Traiter les cas possibles	
							if (monCarrefour.messageH == "msg9"){
								if(Parametres.debug)
									System.out.println("Feu H 	:	rouge - msg9recu");
								// la route demande d'aiguiller, mais on est rouge,  
								//on refuse la voiture: on renvoie un message refuser
								monCarrefour.refuserVoiture(monCarrefour.vehiculeEntrantH.getVoieProvenance()); 
								if(Parametres.debug) System.out.println("carrefour: feu rouge: je refuse une voiture");
							} else { 
								if(Parametres.debug) System.out.println("carrefour: message recu, mais on fait rien"); 
							}
						}
					}
					
					try{
						sleep(100); 
					} catch (InterruptedException e){
						
					}
					compteur ++ ;
					
				} else {

					compteur = 0 ; 

					try{
						sleep(100); 
					} catch (InterruptedException e){
						
					}
					this.etat = "vert";
					feuxGraphiques.changerEtat(this.etat);
				}
			}
			

			/*--------------------------------------------------------
		 	 * 			FEU A L'ETAT ORANGE
			 ---------------------------------------------------------*/	
				
			else if (this.etat == "orange"){
			//	System.out.println("Carrefour "+this.monCarrefour.identite+": FEU "+this.direction+" ORANGE");
				try{
					sleep(dureeOrange);
				}catch(InterruptedException e){
					if(Parametres.debug) System.out.println("Erreur attente orange"); 
				}
				
				try{
					sleep(100); 
				} catch (InterruptedException e){
					
				}
				
				this.etat = "rouge"; 
				feuxGraphiques.changerEtat(this.etat);
				
				/*
				//synchronized(this.monCarrefour.feuV){
					//synchronized(this.monCarrefour.feuH){
						if (this.getDirection() == "H") this.monCarrefour.feuV.notifyAll();
						if (this.getDirection() == "V") this.monCarrefour.feuH.notifyAll();
					//}
				//}
				*/
			}
			
			
			/*--------------------------------------------------------
	 		 * 			FEU A L'ETAT VERT
	 		 ---------------------------------------------------------*/
			else if (this.etat == "vert"){
				
			//	System.out.println("Carrefour "+this.monCarrefour.identite+": FEU "+this.direction+" VERT");
				if (compteur != this.dureeVert) {
					
					if (this.direction == "V") {
					
						// 1- Verifier si une voiture veut entrer et l'aiguiller	
						boolean messageRecu = false;  
						messageRecu = monCarrefour.chercherMsgV() ;
					
						if (messageRecu == true){
							// 2- Recuperer les informations du message
							monCarrefour.recupInfoMessageV() ;
						
							// 3- Traiter les cas possibles 
							if (monCarrefour.messageV == "msg9"){
								if(Parametres.debug) System.out.println("Feu V 	:	vert - msg9recu");
								// On aiguille la voiture 
								monCarrefour.aiguillerVoitureV();  
							
							} else { 
								if(Parametres.debug) System.out.println("carrefour: message recu, mais on fait rien"); 
							}
						}
						
					} else if (this.direction == "H"){
					
						// 1- Verifier si une voiture veut entrer et l'aiguiller	
						boolean messageRecu = false;  
						messageRecu = monCarrefour.chercherMsgH() ;
						
						if (messageRecu == true){
							// 2- Recuperer les informations du message
							monCarrefour.recupInfoMessageH() ;
					
							// 3- Traiter les cas possibles	
							if (monCarrefour.messageH == "msg9"){
								if(Parametres.debug) System.out.println("Feu H 	:	vert - msg9recu");
								// On aiguille la voiture
								if(Parametres.debug) System.out.println("msg9 recu");
								monCarrefour.aiguillerVoitureH(); 
							
							} else { 
								if(Parametres.debug) System.out.println("carrefour "+ monCarrefour.identite+": message recu, on fait rien"); 
							}
						}
					}
					
					try{
						sleep(100); 
					} catch (InterruptedException e){
						
					}
					compteur ++ ; 
					
				} else {
					// reinitialisation du compteur et changement d'?tat 
					compteur = 0 ; 
					try{
						sleep(100); 
					} catch (InterruptedException e){
						
					}
					this.etat = "orange";
					feuxGraphiques.changerEtat(this.etat);
				}

			}
		}
	}
	
}
			
			
			/*
			// si  premi?re voie est non vide
			if ((voie1 != -1) && (voie2 != -1)){
				if (!monCarrefour.listeVehiculesEmpty(voie1)){
					monCarrefour.aiguillerVoiture(voie1);
					if(!monCarrefour.listeVehiculesEmpty(voie2)){
						monCarrefour.aiguillerVoiture(voie2);
					}
					
				}// si la deuxi?me voie est non vide
				else if (!monCarrefour.listeVehiculesEmpty(voie2)){
					monCarrefour.aiguillerVoiture(voie2);
				}
			}	
			
			else if((voie1 == -1) && (voie2 != -1)){
				System.out.println("num de voie2: " + voie2); 
				if(!monCarrefour.listeVehiculesEmpty(voie2)){
					monCarrefour.aiguillerVoiture(voie2);
				}
			}
			else if((voie1 != -1) && (voie2 == -1)){
				if(!monCarrefour.listeVehiculesEmpty(voie1)){
					monCarrefour.aiguillerVoiture(voie1);
				}
			}
			*/