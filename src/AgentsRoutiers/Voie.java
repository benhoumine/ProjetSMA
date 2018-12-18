package AgentsRoutiers;

import GuiSimuTrafic.Parametres;

/**
 * Voie caract�ris� par un tableau de dimension fini qui permet de conna�tre la position
 * des voitures sur la route
 * @author Naji kawtar & Soukayna etalbi 
 */
public class Voie {
	
	private EtatVehicule voie[] ;
	private int ID ;	// 1 = vers la droite ou le haut
						// 0 = vers la gauche ou le bas 
	private int longueur ; 
	
	public Voie(int idVoie, int longueurRoute){
		this.ID = idVoie; 
		 this.voie = new EtatVehicule[longueurRoute*4];
		 this.longueur = longueurRoute;
	}
	
	
	
    /**
 	 * Permet de savoir si une voie 'id' de la route est vide 
 	 * @param id intVoie
 	 * @return boolean
 	 */
 	public synchronized boolean voieEmpty() {
 		
 		// analyse de la voie
 		for (int i=0;i<this.longueur*4;i++){
 			if (voie[i] != null) return false ; 
 		}
 		return true; 
 	}

 	
 	/**
 	 * Permet de rajouter un vehicule debut de voie
 	 * @param v EtatVehicule
 	 * @return boolean
 	 */
 	public synchronized boolean ajouterVehicule(EtatVehicule v) {
 		boolean reussite = false ; 
 		
 		// on rajoute le vehicule en debut de voie
 		if (voie[0] == null ) {
 			voie[0] = v ;
 			reussite = true ; 
 			if(Parametres.debug) System.out.println("Le vehicule "+v.getID()+ " est ajoute a la voie");
 		}
 		
 		// on met a jour l'index pour le vehicule
 		if (reussite) v.setIndex(0);
 		return reussite ; 
 	}
 	
 	/**
 	 * Permet de positionner un vehicule 'v' a un endroit 'index' dans la voie
 	 * @param index
 	 * @param v
 	 * @return
 	 */
 	public synchronized boolean positionnerVehicule(int index, EtatVehicule v){
 		boolean reussite = false ;
 		 
 		if (voie[index] == null ) {
 			voie[index] = v ;
 			reussite = true ;
 			if(Parametres.debug) System.out.println("Le vehicule "+v.getID()+" est place a l'index "+ index+" de la voie");
 		}
 		
 		return reussite ;
 	}
 	
 	/**
 	 * Permet de recuperer un vehicule d'une voie a l'index qu'il occupe
 	 * @param idVoie int
 	 * @param index int
 	 * @return Object
 	 */
 	public synchronized EtatVehicule recupVehicule(int index) {
 		return voie[index];
 	}
 	
 	/**
 	 * Permet de supprimer un vehicule situe a la position 'index' d'une voie 'idVoie'
 	 * @param idVoie
 	 * @param index
 	 */
 	public synchronized void supprVehicule (int index) {
 		
 		voie[index] = null ; 
 		if(Parametres.debug) System.out.println("Je supprime la voiture a l'index :"+index); 
 	
 	}
 	
 	
 	/**
 	 * renvoie l'etat du vehicule qui suit le vehicule situe a l'index 'index'
 	 * dans la voie 'idVoie'
 	 * @return
 	 */
 	public synchronized EtatVehicule vehiculeSuivant(int index){
 	
 		int i = index+1;
 		
 		if(i == longueur*4)	return null;
 		
 		while ((voie[i] == null) && (i < longueur*4-1)) {i++;}
 		return voie[i]; 
 		
 	}
 	
 	
 	/**
 	 * Permet de connaitre le nombre de voiture sur une voie 'idVoie' de la route
 	 * @param id int
 	 * @return int
 	 */
 	public synchronized int nbVoitureVoie(){
 		
 		int i = 0 ;
 		int compteur = 0 ;
 		
 		synchronized(voie){
 			while(i < voie.length){
 				if (voie[i] != null)
 					compteur ++ ; 
 				i++; 
 			}
 		}
 		return compteur ; 
 			
 	}
    
 	/**
 	 * Permet de connaitre la taille d'une voie (nb de voitures admissibles)
 	 * @return
 	 */
 	public synchronized int tailleVoie(){
 		 
 			return voie.length ; 
 		
 	}
 	
 	
 	/**
 	 * Permet de savoir en quelle position se trouve une voiture par rapport aux autres
 	 * @param idVoie
 	 * @param indexVehicule
 	 * @return
 	 */
 	public synchronized int rang(int indexVehicule){
 		
 		int rang = 0 ; 
 		
 		for(int i = indexVehicule+1; i< voie.length; i++){
 			if(voie[i] !=null){
 				rang ++ ; 
 			}
 		}
 			
 		return rang ; 
 		
 	}
 	
	
}
