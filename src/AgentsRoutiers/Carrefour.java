package AgentsRoutiers;
//terminé
import Village.*;
import Gestionnaire.*;

/**
 *
 * @author BENHOUMINE Abdelkhalek & BANE Mamadou La classe Contient les
 *         information sur une carrefour Les route gauche , droite , haut et bas
 *         qui sont liées à chaque carrefour
 * 
 */
public abstract class Carrefour {
	protected int identite;

	// Route Liée à une route Droite Gauche , Bas , Haut
	protected RouteD RoadG = null, RoadH = null, RoadD = null, RoadB = null;
	protected int VGauche = -1, VHaut = -1, VDroit = -1, VBas = -1;

	// existence des voies connectes au carrefour
	protected boolean gauche = false;
	protected boolean droit = false;
	protected boolean haut = false;
	protected boolean bas = false;

	// lien concret avec un élèment graphique
	protected ElementCarrefour elementGraphique = null;

	/**
	 * 
	 * Methode pour lier la partie gauche du carrefour à une route
	 * 
	 * @param laquelle
	 */
	public void setRoadG(RouteD laquelle) {
		this.RoadG = laquelle;
		this.VGauche = laquelle.getID();
	}

	public void setRoadH(RouteD laquelle) {
		this.RoadH = laquelle;
		this.VHaut = laquelle.getID();
	}

	public void setRoadD(RouteD laquelle) {
		this.RoadD = laquelle;
		this.VDroit = laquelle.getID();
	}

	public void setRoadB(RouteD laquelle) {
		this.RoadB = laquelle;
		this.VBas = laquelle.getID();
	}

	public RouteD getRoadG() {
		return this.RoadG;
	}

	public RouteD getRoadH() {
		return this.RoadH;
	}

	public RouteD getRoadD() {
		return this.RoadD;
	}

	public RouteD getRoadB() {
		return this.RoadB;
	}

	public int getIdentite() {
		return this.identite;
	}

	protected void setExisteG(boolean libre) {
		this.gauche = libre;
	}

	protected void setExisteH(boolean libre) {
		this.haut = libre;
	}

	protected void setExisteD(boolean libre) {
		this.droit = libre;
	}

	protected void setExisteB(boolean libre) {
		this.bas = libre;
	}

	protected boolean getExisteG() {
		return this.gauche;
	}

	protected boolean getExisteH() {
		return this.haut;
	}

	protected boolean getExisteD() {
		return this.droit;
	}

	protected boolean getExisteB() {
		return this.bas;
	}

	protected int getVoieG() {
		return this.VGauche;
	}

	protected int getVoieH() {
		return this.VHaut;
	}

	protected int getVoieD() {
		return this.VDroit;
	}

	protected int getVoieB() {
		return this.VBas;
	}

	public ElementCarrefour getElementGraph() {
		return this.elementGraphique;
	}

	/*
	 * pour assurer la communication entre les carrefour
	 */
	protected GestMsgRoute gestMsgRoute = null;
	protected GestMsgVoiture gestMsgVoiture = null;
	protected GestMsgCarrefour gestMsgCarrefour = null;

	// pour la connaissance de l'etat des routes connexes
	protected EtatRoute routeG = null;
	protected EtatRoute routeH = null;
	protected EtatRoute routeD = null;
	protected EtatRoute routeB = null;

	// pour la configuration de son propre etat
	protected EtatCarrefour etatCarrefourActuel = null;

}