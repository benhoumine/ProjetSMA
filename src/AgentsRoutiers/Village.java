
package AgentsRoutiers;
/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
*/

import Gestionnaire.*;
import GuiSimuTrafic.Parametres;

import java.util.*;

import Village.ElementCarrefour;
import Village.ElementRoute;
import Village.ElementVillage;
//import GuiSimuTrafic.*;

public class Village extends Thread {


	private ThreadGroup groupeVillage ;
    private ThreadGroup groupeRoute ;
    private ThreadGroup groupeVoiture ;
    private ThreadGroup groupeCarrefour ;
/*
    private RouteD route0 ;
    private RouteD route1 ;
    private RouteD route2 ;
    private RouteD route3 ;
    
    private CarrefourAFeux carrefour0; 
    private CarrefourAFeux carrefour1;
    private CarrefourAFeux carrefour2;
    private CarrefourAFeux carrefour3;
*/    
    GestMsgRoute gestCommRoute ; 
    GestMsgVoiture gestCommVoiture ; 
    GestMsgCarrefour gestCommCarrefour ; 
    
    private Voiture mesVoitures[] = new Voiture[10];

    Message msgAEnvoyer = null ; 
/*    
    public Village() {

        // D?finition des groupes de threads
        this.groupeVillage = new ThreadGroup("village");
        this.groupeRoute = new ThreadGroup(groupeVillage, "route");
        this.groupeVoiture = new ThreadGroup(groupeVillage, "voiture");
        this.groupeCarrefour = new ThreadGroup(groupeVillage, "carrefour");

        // CREATION D'UN RESEAU EN "ANNEAU" (4CARREFOURS ET 4ROUTES)
        
        
        // Cr?ation du gestionnaire de message pour la communication
        gestCommRoute = new GestMsgRoute(4); // 4 routes...
        gestCommVoiture = new GestMsgVoiture(15); //5 voitures au d?but...
        gestCommCarrefour = new GestMsgCarrefour(4); 

        // Cr?ation des threads de l'environnement Routier

		// LES CARREFOURS 
		carrefour0 = new CarrefourAFeux(0,null,null,route1,route0,gestCommRoute,gestCommVoiture,gestCommCarrefour,200,100,200);
		carrefour1 = new CarrefourAFeux(1,route1,null,null,route2,gestCommRoute,gestCommVoiture,gestCommCarrefour,200,200,200);
		carrefour2 = new CarrefourAFeux(2,route3,route2,null,null,gestCommRoute,gestCommVoiture,gestCommCarrefour,200,200,200);
		carrefour3 = new CarrefourAFeux(3,null,route0,route3,null,gestCommRoute,gestCommVoiture,gestCommCarrefour,200,200,200);

        // LES ROUTES 
        route0 = new RouteD(0,1,3,1,carrefour0,carrefour3,"haut", "bas", gestCommRoute, gestCommVoiture, gestCommCarrefour);
        route1 = new RouteD(1,1,3,0,carrefour0,carrefour1,"gauche", "droite", gestCommRoute, gestCommVoiture, gestCommCarrefour); 
        route2 = new RouteD(2,1,3,1,carrefour1,carrefour2,"haut", "bas", gestCommRoute, gestCommVoiture, gestCommCarrefour);
		route3 = new RouteD(3,1,3,0,carrefour3,carrefour2,"gauche", "droite", gestCommRoute, gestCommVoiture, gestCommCarrefour);
		
		// MISE A JOUR LIENS 
		carrefour0.setRoadB(route0); 
		System.out.println("identit? de la voie B du carrefour0: "+carrefour0.RoadB.getID());
		carrefour0.setRoadD(route1);
		carrefour1.setRoadG(route1);
		System.out.println("identit? de la voie G du carrefour1: "+carrefour1.RoadG.getID());
		carrefour1.setRoadB(route2); 
		carrefour2.setRoadH(route2);  
		carrefour2.setRoadG(route3);
		carrefour3.setRoadD(route3); 
		carrefour3.setRoadH(route0);
		carrefour0.mettreAJourEtatCarrefour();
		carrefour1.mettreAJourEtatCarrefour(); 
		carrefour2.mettreAJourEtatCarrefour(); 
		carrefour3.mettreAJourEtatCarrefour(); 
		
		route0.mettreAjourEtatRoute(); 
		route1.mettreAjourEtatRoute(); 
		route2.mettreAjourEtatRoute(); 
		route3.mettreAjourEtatRoute(); 
		
		// LES VOITURES 
		for (int i=0; i<10; i++){ 
        	mesVoitures[i] = new Voiture(i, "toto", "droite", gestCommRoute, gestCommVoiture,-1);
        	mesVoitures[i].etatVehiculeActuel.setVoieChoisie("droit");
        	mesVoitures[i].etatVehiculeActuel.setVoie(1);
        } 
        
    }
      
    */

    	   	    	
    Vector listeRoutes = new Vector();
    Vector listeCarrefours = new Vector();
    
    public Village(ElementVillage leReseau) {
    	gestCommCarrefour = new GestMsgCarrefour(leReseau.getListeCarrefours().size());
    	gestCommRoute = new GestMsgRoute(leReseau.getListeRoutes().size());
    	gestCommVoiture = new GestMsgVoiture(150);
    	
    	Vector listeCarrefoursGraph = leReseau.getListeCarrefours();
    	Vector listeRoutesGraph = leReseau.getListeRoutes();
    	
    	/* On cr?e maintenant toutes les routes de la simulation */
    	for(int i=0; i<listeRoutesGraph.size(); i++) {
    		listeRoutes.add(new RouteD(	i,
    				1,
					((ElementRoute) listeRoutesGraph.get(i)).getLongeur(), 
					((ElementRoute) listeRoutesGraph.get(i)).getDirection(),
					null,
					null,
					gestCommRoute, 
					gestCommVoiture, 
					gestCommCarrefour, 
					(ElementRoute) listeRoutesGraph.get(i)
    		));
    		
    	}
    	
    	/* On cr?e maintenant tous les carrefours de la simulation */
    	for(int i=0; i<listeCarrefoursGraph.size(); i++) {
    		listeCarrefours.add(new CarrefourAFeux(  i,
    				null,
					null,
					null,
					null, 
					gestCommRoute,
					gestCommVoiture,
					gestCommCarrefour,
					20,
					10,
					30,
					(ElementCarrefour) listeCarrefoursGraph.get(i),
					((ElementCarrefour) listeCarrefoursGraph.get(i)).getFeux()
    		));
    	}
    	
    	/* On connecte les Routes aux carrefours */
    	for(int i=0; i<listeCarrefours.size(); i++) {
    		CarrefourAFeux leCarrefour = ((CarrefourAFeux) listeCarrefours.get(i));
    		for(int j=0;j<4;j++) {				
    			if(((ElementCarrefour) leCarrefour.getElementGraph()).getRoute(j) != null){				
    				switch (j) {
    				case 0 :
    					if(Parametres.debug)
    						System.out.println(((RouteD) ((ElementRoute) ((ElementCarrefour) ((Carrefour) listeCarrefours.get(i)).getElementGraph()).getRoute(j)).getElementVillageRoute()).getID());
    					leCarrefour.setRoadH( ((RouteD) ((ElementRoute) ((ElementCarrefour) ((Carrefour) listeCarrefours.get(i)).getElementGraph()).getRoute(j)).getElementVillageRoute()));
    					leCarrefour.mettreAJourCarrefour();    					
    					break;
    				case 1 :
    					leCarrefour.setRoadD( ((RouteD) ((ElementRoute) ((ElementCarrefour) ((Carrefour) listeCarrefours.get(i)).getElementGraph()).getRoute(j)).getElementVillageRoute()));
    					leCarrefour.mettreAJourCarrefour();    					
    					break;
    				case 2:
    					leCarrefour.setRoadB( ((RouteD) ((ElementRoute) ((ElementCarrefour) ((Carrefour) listeCarrefours.get(i)).getElementGraph()).getRoute(j)).getElementVillageRoute()));
    					leCarrefour.mettreAJourCarrefour();    					
    					break;
    				case 3:
    					leCarrefour.setRoadG( ((RouteD) ((ElementRoute) ((ElementCarrefour) ((Carrefour) listeCarrefours.get(i)).getElementGraph()).getRoute(j)).getElementVillageRoute()));
    					leCarrefour.mettreAJourCarrefour();    					    					
    					break;
    				}				
    			}
    		}			
    	}
    	
    	/* On connecte les Carrefours aux routes */
    	for(int i=0; i<listeRoutes.size();i++) {
    		RouteD laRoute = (RouteD) listeRoutes.get(i);
    		for(int j=0; j<2; j++) {
    			if(((ElementRoute) laRoute.getElementGraphique()).getCarrefour(j) != null) {
    				switch(j) {
    				case 0:    						
    					laRoute.setCarrefourD(((CarrefourAFeux) ((ElementCarrefour) ((ElementRoute) ((RouteD) listeRoutes.get(i)).getElementGraphique()).getCarrefour(j)).getElementVillageCarrefour()));    					
    					break;
    				case 1:
    					laRoute.setCarrefourF(((CarrefourAFeux) ((ElementCarrefour) ((ElementRoute) ((RouteD) listeRoutes.get(i)).getElementGraphique()).getCarrefour(j)).getElementVillageCarrefour()));    					
    					break;
    				}
    			}
    		}
    	}
    	afficherStructure();
    }    	    	    	
    

    private void afficherStructure() {    	
    	for (int i=0; i<listeCarrefours.size(); i++) {
    		CarrefourAFeux leCarrefour = ((CarrefourAFeux) listeCarrefours.get(i));
    		if(Parametres.debug) {
    			System.out.println("Identite du carrefour " + i + " : " + leCarrefour.identite);
    			System.out.println("Route Gauche : " + leCarrefour.getVoieG());
    			System.out.println("Route Haute : " + leCarrefour.getVoieH());
    			System.out.println("Route Droite : " + leCarrefour.getVoieD());
    			System.out.println("Route Bas : " + leCarrefour.getVoieB());
    			System.out.println("");
    		}
    	}
    	for(int i=0; i<listeRoutes.size(); i++) {
    		RouteD laRoute = ((RouteD) listeRoutes.get(i));
    		if(Parametres.debug) {
    			System.out.println("Identite de la route " + i + " : " + laRoute.getID());
    			System.out.println("Carrefour Debut: " + laRoute.getIdCarrefourD());
    			System.out.println("Carrefour Fin : " +  laRoute.getIdCarrefourF());
    			System.out.println("");
    		}
    	}
    }
    
    
    public void run(){
        int i = 1; 
        int k = 0;
    	while (true){
    		if (i==1){ 
    			
    			/*for (int p=0; p<8; p++){ 
    	        	mesVoitures[p] = new Voiture(p, "toto", "droite", gestCommRoute, gestCommVoiture,-1);
    	        	mesVoitures[p].etatVehiculeActuel.setVoieChoisie("droit");
    	        	mesVoitures[p].etatVehiculeActuel.setVoie(1);
    	        } */
    			
    			for(int n=0; n<listeRoutes.size(); n++) {
    				((RouteD) listeRoutes.get(n)).start();
    			}
    			
    			for(int j=0; j<listeCarrefours.size(); j++) {
    				((CarrefourAFeux) listeCarrefours.get(j)).mettreEnFonctionnement();  
    			}
    			
    			/*for(int j=0; j<8; j++){
    				mesVoitures[j].start();
    			}*/
    			i++;
    		}
			try{
				sleep(500);
			}catch (InterruptedException e){
				if(Parametres.debug) System.out.println("blabla");
			}
			
    		/*if(k<8){
    			msgAEnvoyer = new Message(30,mesVoitures[k].etatVehiculeActuel); 
    			gestCommRoute.ajouterMsg(0,msgAEnvoyer); 
    			k++; 
    		}*/
            
    	}
    }
    
    public void ajouterVehicule(CarrefourAFeux leCarrefour) {
    	Voiture laVoiture = new Voiture(leCarrefour.getIdentite(), "toto", "droite", gestCommRoute, gestCommVoiture,-1);
    	laVoiture.etatVehiculeActuel.setVoieChoisie("droit");
    	
    	laVoiture.start();
    	
    	if(leCarrefour.getRoadD() != null) {
    		laVoiture.etatVehiculeActuel.setVoie(1);
    		msgAEnvoyer = new Message(30, laVoiture.etatVehiculeActuel);
    		gestCommRoute.ajouterMsg(leCarrefour.getRoadD().getID(),msgAEnvoyer);
    	} else if(leCarrefour.getRoadG() != null) {
    		laVoiture.etatVehiculeActuel.setVoie(0);
    		msgAEnvoyer = new Message(30, laVoiture.etatVehiculeActuel);
    		gestCommRoute.ajouterMsg(leCarrefour.getRoadG().getID(),msgAEnvoyer);
    	} else if(leCarrefour.getRoadH() != null) {
    		laVoiture.etatVehiculeActuel.setVoie(0);
    		msgAEnvoyer = new Message(30, laVoiture.etatVehiculeActuel);
    		gestCommRoute.ajouterMsg(leCarrefour.getRoadH().getID(),msgAEnvoyer);
    	} else if(leCarrefour.getRoadB() != null) {
    		laVoiture.etatVehiculeActuel.setVoie(1);
    		msgAEnvoyer = new Message(30, laVoiture.etatVehiculeActuel);
    		gestCommRoute.ajouterMsg(leCarrefour.getRoadB().getID(),msgAEnvoyer);
    	}
    }

    /**
     * pour mettre en pause la simulation
     */
    private void pause() {
        this.groupeVillage.suspend();
    }

    /**
     * pour reprendre la simulation
     */
    private void lecture() {
        this.groupeVillage.resume();
    }

    /**
     * pour stopper la simulation
     */
    private void arret() {
        this.groupeVillage.stop();
    }

    /**
     * pour mettre uniquement les voitures en pause ;)
     */
    private void suspendreVoiture(){
        this.groupeVoiture.suspend();
    }

}
