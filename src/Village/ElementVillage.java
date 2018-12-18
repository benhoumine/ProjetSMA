 package Village;

import java.util.*;

import AgentsRoutiers.Village;
import GuiSimuTrafic.*;

/**
 * @author Naji kawtar & Soukayna etalbi
 */
public class ElementVillage {
	
	private Vector listeCarrefours = new Vector();
	private Vector listeRoutes = new Vector();
	private Vector listeVehicules = new Vector();
	private Village leVillage = null;
	
	public ElementVillage(ReseauRoutier reseau) {
		
		Vector listeCases = reseau.getListeCases();
		int maxI = listeCases.size();		
		
		// Premiere passe: on cr?e les objets Route et Carrefour
		for(int i=0; i<maxI; i++) {
			int maxJ = ((Vector) listeCases.get(i)).size();
			
			for (int j=0; j<maxJ; j++) {
				SimuTraficBloc element = bloc(listeCases, i, j);				
				
				if(element.getEtat() == "carrefour") {
					if(element.getElementVillage() == null) {
						ElementCarrefour elementTemp = new ElementCarrefour(element, reseau.getMetaBoite());
						element.setElementVillage(elementTemp);
						listeCarrefours.add(elementTemp);
					}
				} else if(element.getEtat() == "route") {
					if(element.getElementVillage() == null) {						
						listeRoutes.add(new ElementRoute(element, listeCases, reseau.getMetaBoite()));
					}
				}
			}
		}
		
		// Deuxi?mme passe: on les relie entre eux
		
		for(int i=0; i<maxI; i++) {
			int maxJ = ((Vector) listeCases.get(i)).size();
			
			for (int j=0; j<maxJ; j++) {
				SimuTraficBloc element = bloc(listeCases, i, j);
				
				if(element.getEtat() == "carrefour") {
					if(element.getElementVillage() == null) {
						System.err.println("Erreur parsing village");
					} else {
						brancherCarrefour(listeCases, element);
						changerAspect(element);
					}
				}
			}
		}									
	}

	private SimuTraficBloc bloc(Vector listeCases, int i, int j) {
		return ((SimuTraficBloc)((Vector) listeCases.get(i)).get(j));
	}
	
	private void brancherCarrefour(Vector listeCases, SimuTraficBloc element) {
		int Ligne = element.getLigne();
		int Colone = element.getColone();
		
		if (Ligne - 1 >= 0) {
			if (bloc(listeCases,Ligne-1,Colone).getElementVillage() != null) {
				((ElementCarrefour)element.getElementVillage()).setRoute(0, (ElementRoute) bloc(listeCases,Ligne-1,Colone).getElementVillage());
				((ElementRoute) bloc(listeCases,Ligne-1,Colone).getElementVillage()).ajouterCarrefour(1, (ElementCarrefour) element.getElementVillage());
			}
		}
		if (Ligne + 1 < listeCases.size()) {
			if (bloc(listeCases,Ligne+1,Colone).getElementVillage() != null) {
				((ElementCarrefour)element.getElementVillage()).setRoute(2, (ElementRoute) bloc(listeCases,Ligne+1,Colone).getElementVillage());
				((ElementRoute) bloc(listeCases,Ligne+1,Colone).getElementVillage()).ajouterCarrefour(0, (ElementCarrefour) element.getElementVillage());
			}
		}
		if (Colone - 1 >= 0) {
			if(bloc(listeCases,Ligne,Colone-1).getElementVillage() != null) {
				((ElementCarrefour)element.getElementVillage()).setRoute(3, (ElementRoute) bloc(listeCases,Ligne,Colone-1).getElementVillage());
				((ElementRoute) bloc(listeCases,Ligne,Colone-1).getElementVillage()).ajouterCarrefour(1, (ElementCarrefour) element.getElementVillage());
			}
		}
		if (Colone + 1 < listeCases.size()) {
			if(bloc(listeCases,Ligne,Colone+1).getElementVillage() != null) {
				((ElementCarrefour)element.getElementVillage()).setRoute(1, (ElementRoute) bloc(listeCases,Ligne,Colone+1).getElementVillage());
				((ElementRoute) bloc(listeCases,Ligne,Colone+1).getElementVillage()).ajouterCarrefour(0, (ElementCarrefour) element.getElementVillage());
			}
		}	
	}
	
	/**
	 * Cette m?thode change l'aspect du SimuTraficBloc pour refl?ter sa topologie r?elle
	 * @param element SimuTraficBloc ? changer
	 */
	
	private void changerAspect(SimuTraficBloc element) {
		ElementCarrefour elementCarrefour = (ElementCarrefour) element.getElementVillage();
		boolean[] route = new boolean[4];
		
		for(int i=0;i<4;i++)
			route[i] = (elementCarrefour.getRoute(i) != null);
		
		if(route[0] && route[1] && route[2] && route[3]) {
			element.setOritentation("");
		} else if(route[0] && route[1] && route[2]) {
			element.setOritentation("3D");	
		} else if(route[1] && route[2] && route[3]) {
			element.setOritentation("3B");			
		} else if(route[0] && route[3] && route[2]) {
			element.setOritentation("3G");			
		} else if(route[3] && route[0] && route[1]) {
			element.setOritentation("3H");			
		} else if(route[0] && route[1]) {
			element.setOritentation("2HD");			
		} else if(route[1] && route[2]) {
			element.setOritentation("2BD");
		} else if(route[2] && route[3]) {
			element.setOritentation("2BG");			
		} else if(route[3] && route[0]) {
			element.setOritentation("2HG");			
		} else if(route[3] && route[1]) {
			element.setOritentation("2GD");			
		} else if(route[0] && route[2]) {
			element.setOritentation("2HB");			
		} else if(route[0]) {
			element.setOritentation("H");			
		} else if(route[1]) {
			element.setOritentation("D");			
		} else if(route[2]) {
			element.setOritentation("B");			
		} else if(route[3]) {
			element.setOritentation("G");			
		} else {
			element.setOritentation("O");
		}
		if(Parametres.debug) System.out.println("Demande de l'actualisation de l'image");
		element.actualiserImage();	
		elementCarrefour.getFeux()[0].creerEtAfficherFeux();
		elementCarrefour.getFeux()[1].creerEtAfficherFeux();
	}
	
	
	/**
	 * Permet d'acc?der ? un ElementRoute de la liste afin de la parcourir
	 * @param index "num?ro" de la route dans la liste
	 * @return si l'ElementRoute existe celui-ci, sinon null.
	 */
	ElementRoute getRoute(int index) {
		if(index<listeRoutes.size()) {
			return (ElementRoute) listeRoutes.get(index);
		} else {
			return null;
		}
	}
	
	/**
	 * Permet d'acc?der ? un ElementCarrefour de la liste afin de la parcourir
	 * @param index "num?ro" du carrefour dans la liste
	 * @return si l'ElementCarrefour existe celui-ci, sinon null.
	 */
	ElementCarrefour getCarrefour(int index) {
		if(index<listeCarrefours.size()) {
			return (ElementCarrefour) listeCarrefours.get(index);
		} else {
			return null;
		}
	}
	
	
	
	/**
	 * @return Returns the listeCarrefours.
	 */
	public Vector getListeCarrefours() {
		return listeCarrefours;
	}
	/**
	 * @return Returns the listeRoutes.
	 */
	public Vector getListeRoutes() {
		return listeRoutes;
	}
	/**
	 * @return Returns the listeVehicules.
	 */
	public Vector getListeVehicules() {
		return listeVehicules;
	}
	
	public void genererStructureSimulation() {
		this.leVillage = new Village(this);		
	}
	
	public void demarrerSimulateur() {
		this.leVillage.start();
	}
	
	public void ajouterVehicule(ElementCarrefour leCarrefour) {
		leVillage.ajouterVehicule(leCarrefour.getElementVillageCarrefour());
	}
}
