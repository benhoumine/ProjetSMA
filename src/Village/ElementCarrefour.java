package Village;
import AgentsRoutiers.CarrefourAFeux;
import javax.swing.JLayeredPane;

import GuiSimuTrafic.SimuTraficBloc;
/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
* 
* 
*/
public class ElementCarrefour extends ElementGenerique {

	private SimuTraficBloc elementSimu;
	private ElementRoute[] routes = new ElementRoute[4];
	private ElementFeu[] feux = new ElementFeu[2];
	private JLayeredPane paneauFeux;
	private CarrefourAFeux elementVillageCarrefour = null;
	
	public ElementCarrefour(SimuTraficBloc leBloc, JLayeredPane paneauFeux) {
		elementSimu = leBloc;
		leBloc.setElementVillage(this);
		this.paneauFeux = paneauFeux;
		feux[0] = new ElementFeu(0, leBloc, paneauFeux);
		feux[1] = new ElementFeu(1, leBloc, paneauFeux);
		
	}
	
	/**
	 * Permet de relier un carrefour ? une route
	 * @param laquelle 0=haut, 1=droite, 2=bas, 3=gauche
	 * @param route La route en question
	 */
	public void setRoute(int laquelle, ElementRoute route) {
		routes[laquelle] = route;		
	}
	/**
	 * Permet de récupérer une route
	 * @param laquelle 0=haut, 1=droite, 2=bas, 3=gauche
	 * @return la route en question
	 */
	public ElementRoute getRoute(int laquelle) {
		return routes[laquelle];
	}
	
	
	
	/**
	 * @return Returns the elementVillageCarrefour.
	 */
	public CarrefourAFeux getElementVillageCarrefour() {
		return elementVillageCarrefour;
	}
	/**
	 * @param elementVillageCarrefour The elementVillageCarrefour to set.
	 */
	public void setElementVillageCarrefour(CarrefourAFeux elementVillageCarrefour) {
		this.elementVillageCarrefour = elementVillageCarrefour;		
	}
	
	/**
	 * Permet l'acc�s aux �l�ments graphiques de dessin des feux.
	 *
	 */
	public ElementFeu[] getFeux() {
		return feux;
	}
	
	public void actualiserImage() {
		feux[0].actualiserImage();
		feux[1].actualiserImage();
	}
}
