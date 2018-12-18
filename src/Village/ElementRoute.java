package Village;
import AgentsRoutiers.RouteD;
import java.util.*;
import javax.swing.JLayeredPane;


import java.awt.Point;

import GuiSimuTrafic.Parametres;
import GuiSimuTrafic.SimuTraficBloc;

/**
 * @author Naji kawtar & Soukayna etalbi
 *
 */
public class ElementRoute extends ElementGenerique {

	private static final int TAILLE_BORDS = 20;
	private static final int TAILLE_BANDE = 19;
	
	private Vector elementsGraphiques = new Vector();
	private JLayeredPane paneauVoiture;
	private LinkedList[] vehicules = new LinkedList[2];
	private String orientation; 
	
	private ElementCarrefour[] Carrefours = new ElementCarrefour[2]; 
	
	private RouteD elementVillageRoute = null;
	
	public ElementRoute(SimuTraficBloc bloc, Vector listeCases, JLayeredPane paneauVoitures) {
		
		int Ligne = bloc.getLigne();
		int Colone = bloc.getColone();
				
		this.paneauVoiture = paneauVoitures;
		this.orientation = bloc.getOrientation();
		
		vehicules[1] = new LinkedList();
		vehicules[0] = new LinkedList();
		
		if(bloc.getOrientation() == "H") {			
			while(bloc.getEtat() != "carrefour") {
				this.elementsGraphiques.add(bloc);
				bloc.setElementVillage(this);
				bloc = ((SimuTraficBloc) ((Vector) listeCases.get(Ligne)).get(++Colone));				
			}
		} else if(bloc.getOrientation() == "V") {
			while(bloc.getEtat() != "carrefour") {
				this.elementsGraphiques.add(bloc);
				bloc.setElementVillage(this);
				bloc = ((SimuTraficBloc) ((Vector) listeCases.get(++Ligne)).get(Colone));
			}
		}		
		//this.ajouterVehicule(0,ElementVehicule.VOITURE_NORMALE_0);
		//VoitureRunner thread1 = new VoitureRunner(this, this.getVehicules(0,0), true);
		//thread1.start();
	}
	
	/**
	 * Permet d'ajouter un morceau ? la route
	 * @param bloc bloc ? ajouter ? la route 
	 */
	public void ajouterMorceau(SimuTraficBloc bloc) {
		elementsGraphiques.add(bloc);
		bloc.setElementVillage(this);
	}
	
	/**
	 * Permet l'ajout d'un carrefour ? une route
	 * @param position 0 = gauche o? haut, 1 = droite o? bas
	 * @param carrefour carrefour ? ajouter
	 */
	public void ajouterCarrefour(int position, ElementCarrefour carrefour) {
		Carrefours[position] = carrefour;
	}
	
	
	/**
	 * @return Returns the carrefours.
	 * @param lequel : 1 ou 2
	 */
	public ElementCarrefour getCarrefour(int lequel) {
		return Carrefours[lequel];
	}
	
	/**
	 * Permet d'ajouter un v?hicule sur la route
	 * @param sens sens de circulation
	 * @param vehicule le v?hicule ? ajouter
	 */
	
	public synchronized void ajouterVehicule(int sens, int typeVehicule) {
		ElementVehicule nouveauVehicule;
		Point location = new Point();
		if(orientation == "H") {			
			nouveauVehicule = new ElementVehicule(typeVehicule, ElementVehicule.DIRECTION_HORIZONTALE, sens);
			
			
			if(sens == 0) {
				location.y = (int) ((SimuTraficBloc) elementsGraphiques.get(0)).getLocation().getY()+(TAILLE_BORDS+TAILLE_BANDE)*SimuTraficBloc.getEchelle()/100;
				location.x = (int) ((SimuTraficBloc) elementsGraphiques.get(0)).getLocation().getX();
			} else {
				location.y = (int) ((SimuTraficBloc) elementsGraphiques.get(0)).getLocation().getY()+ (TAILLE_BORDS)*SimuTraficBloc.getEchelle()/100;
				location.x = (int) ((SimuTraficBloc) elementsGraphiques.get(elementsGraphiques.size()-1)).getLocation().getX() + SimuTraficBloc.getTaille()*SimuTraficBloc.getEchelle()/100  - nouveauVehicule.getIcon().getIconWidth();
			}
			
		} else {
			nouveauVehicule = new ElementVehicule(typeVehicule, ElementVehicule.DIRECTION_VERTICAL, sens);
						
			if(sens == 0) {
				location.x = (int) ((SimuTraficBloc) elementsGraphiques.get(0)).getLocation().getX()+TAILLE_BORDS*SimuTraficBloc.getEchelle()/100;
				location.y = (int) ((SimuTraficBloc) elementsGraphiques.get(0)).getLocation().getY();
			} else {
				location.x = (int) ((SimuTraficBloc) elementsGraphiques.get(0)).getLocation().getX()+ SimuTraficBloc.getTaille()*SimuTraficBloc.getEchelle()/100 - (TAILLE_BORDS+TAILLE_BANDE)*SimuTraficBloc.getEchelle()/100;
				location.y = (int) ((SimuTraficBloc) elementsGraphiques.get(elementsGraphiques.size()-1)).getLocation().getY()+ SimuTraficBloc.getTaille()*SimuTraficBloc.getEchelle()/100 - nouveauVehicule.getIcon().getIconHeight();
			}
			
		}
		paneauVoiture.add(nouveauVehicule, 0);
		vehicules[sens].addLast(nouveauVehicule);
		if(Parametres.debug) System.out.println("Ajout de Voiture dans le sens :" +sens +"position = "+ vehicules[sens].size());
		nouveauVehicule.setLocation(location);
		
	}
	/**
	 * Permet de r?cup?rer le premier v?hicule de la liste
	 * @param sens sens de circulation
	 */
	public synchronized void supprimerVehicule(int sens) {
		// On le fait disparaitre...
		((ElementVehicule) vehicules[sens].getFirst()).setLocation(-100,150);
		((ElementVehicule) vehicules[sens].getFirst()).setVisible(false);
		((ElementVehicule) vehicules[sens].getFirst()).repaint();
		vehicules[sens].removeFirst();
	}
	
	/**
	 *
	 * @param position la position du v?hicule dans la fille
	 * @param sens le sens de circulation 
	 * @return le v?hicule demand?
	 */
	
	public synchronized ElementVehicule getVehicules(int position, int sens) {
		return (ElementVehicule) vehicules[sens].get(position);
	}
	
	/**
	 * Connaitre le nombre de v?hicules sur une route dans un sens donn?
	 * @param sens : sens de circulation
	 * @return nombre de v?hicules dans le sens demand?
	 */
	public synchronized int getNombreVehicules(int sens) {
		return vehicules[sens].size();
	}
	
	/**
	 * Connaitre le nombre de v?hicules sur une route
	 * @return nombre de v?hicules sur une route
	 */
	
	public synchronized int getNombreVehicules() {
		return vehicules[1].size() + vehicules[0].size();
	}
	
	public synchronized void deplacerVehicule(int position, int sens, int offset) {
		Point location = new Point();		
		ElementVehicule leVehicule = (ElementVehicule) vehicules[sens].get(position);
				
		leVehicule.setOffset(offset);
		
		if(orientation == "H") {
			
			if(sens == 0) {
				location.y = (int) ((SimuTraficBloc) elementsGraphiques.get(0)).getLocation().getY()+(TAILLE_BORDS+TAILLE_BANDE)*SimuTraficBloc.getEchelle()/100;
				location.x = (int) ((SimuTraficBloc) elementsGraphiques.get(0)).getLocation().getX()+offset*SimuTraficBloc.getTaille()/2*SimuTraficBloc.getEchelle()/100;
			} else {
				location.y = (int) ((SimuTraficBloc) elementsGraphiques.get(0)).getLocation().getY()+ (TAILLE_BORDS)*SimuTraficBloc.getEchelle()/100;
				location.x = (int) ((SimuTraficBloc) elementsGraphiques.get(elementsGraphiques.size()-1)).getLocation().getX() + (SimuTraficBloc.getTaille()-offset*SimuTraficBloc.getTaille()/2)*SimuTraficBloc.getEchelle()/100  - leVehicule.getIcon().getIconWidth();
			}
			
		} else {
			
			if(sens == 0) {
				location.x = (int) ((SimuTraficBloc) elementsGraphiques.get(0)).getLocation().getX()+(TAILLE_BORDS)*SimuTraficBloc.getEchelle()/100;
				location.y = (int) ((SimuTraficBloc) elementsGraphiques.get(0)).getLocation().getY()+(offset*SimuTraficBloc.getTaille()/2)*SimuTraficBloc.getEchelle()/100;
			} else {
				location.x = (int) ((SimuTraficBloc) elementsGraphiques.get(0)).getLocation().getX()+ SimuTraficBloc.getTaille()*SimuTraficBloc.getEchelle()/100 - (TAILLE_BORDS+TAILLE_BANDE)*SimuTraficBloc.getEchelle()/100;
				location.y = (int) ((SimuTraficBloc) elementsGraphiques.get(elementsGraphiques.size()-1)).getLocation().getY()+ (SimuTraficBloc.getTaille()-(offset*SimuTraficBloc.getTaille()/2))*SimuTraficBloc.getEchelle()/100 - leVehicule.getIcon().getIconHeight();
			}
		}
		
		leVehicule.setLocation(location);
	}
	
	public int getLongeur() {
		return elementsGraphiques.size();
	}
	
	public int getDirection() {
		return (orientation == "H") ? 0 : 1;
	}
	
	
	/**
	 * @return Returns the elementVillageRoute.
	 */
	public RouteD getElementVillageRoute() {
		return elementVillageRoute;
	}

	/**
	 * @param elementVillageRoute The elementVillageRoute to set.
	 */
	public void setElementVillageRoute(RouteD elementVillageRoute) {
		this.elementVillageRoute = elementVillageRoute;
	}
	
	public synchronized void actualiserImage() {
		for(int i=0; i<vehicules[0].size(); i++) {						
			ElementVehicule leVehicule = ((ElementVehicule) vehicules[0].get(i));			
			leVehicule.actualiserImage();			
			deplacerVehicule(i,leVehicule.getSens(), leVehicule.getOffset());
		}
		
		for(int i=0; i<vehicules[1].size(); i++) {
			ElementVehicule leVehicule = ((ElementVehicule) vehicules[1].get(i));
			leVehicule.actualiserImage();
			deplacerVehicule(i,leVehicule.getSens(), leVehicule.getOffset());
		}
	}

}

class VoitureRunner extends Thread {
	
	ElementVehicule voiture = null;
	ElementRoute route = null;
	boolean hautbas = false;
	int i = 0;
	
	public VoitureRunner(ElementRoute route, ElementVehicule voiture, boolean hautbas) {
		this.route = route;
		this.voiture=voiture;
		this.hautbas = hautbas;
	}
	
	public void run() {
	
		while(true) {
			try {
				sleep(100);
				
			} catch (Exception e) {
				
			}
			if(hautbas)
			route.deplacerVehicule(0, 0, i++);
			else 
			route.deplacerVehicule(0, 1, i++);
			
			if(Parametres.debug) System.out.println("Position Voiture: " + voiture.getLocation());
		
		}
	}
	

}
