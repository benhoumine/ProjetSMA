package AgentsRoutiers;
import Village.*;
import Gestionnaire.*;

/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
*/
public abstract class Carrefour {
	
	/*---------------------------------------------------------
	 *     CARACTERISTIQUES D'UN CARREFOUR 
	 *---------------------------------------------------------*/
	
	// identite du carrefour
	protected int identite ;
    
	// routes connectes au carrefour
	protected RouteD RoadG = null; 
	protected RouteD RoadH = null;
	protected RouteD RoadD = null;
	protected RouteD RoadB = null;
    
	// identite des voies connectes au carrefour
    protected int voieGauche = -1 ;
    protected int voieHaut = -1 ;
    protected int voieDroit = -1 ;
    protected int voieBas = -1 ; 
    
    // existence des voies connectes au carrefour 
	protected boolean gauche = false ; 
	protected boolean droit = false ;
	protected boolean haut = false ; 
	protected boolean bas = false ;
	
	// lien concret avec un element graphique
	protected ElementCarrefour elementGraphique = null;
	
	
	/*---------------------------------------------------------
	 * 		ACCESSEURS DU CARREFOUR
	 *---------------------------------------------------------*/
	
	public void setRoadG(RouteD laquelle) { this.RoadG = laquelle;
											this.voieGauche = laquelle.getID();}
	public void setRoadH(RouteD laquelle) { this.RoadH = laquelle;
											this.voieHaut = laquelle.getID();}
	public void setRoadD(RouteD laquelle) { this.RoadD = laquelle;
											this.voieDroit = laquelle.getID();}
	public void setRoadB(RouteD laquelle) { this.RoadB = laquelle;
											this.voieBas = laquelle.getID();}
	
	public RouteD getRoadG(){return this.RoadG;}
	public RouteD getRoadH(){return this.RoadH;}
	public RouteD getRoadD(){return this.RoadD;}
	public RouteD getRoadB(){return this.RoadB;}
	
	public int getIdentite() { return this.identite;}
	
	protected void setExisteG(boolean libre) { this.gauche = libre;}
	protected void setExisteH(boolean libre) { this.haut = libre;}
	protected void setExisteD(boolean libre) { this.droit = libre;}
	protected void setExisteB(boolean libre) { this.bas = libre;}
	
	protected boolean getExisteG(){return this.gauche;}
	protected boolean getExisteH(){return this.haut;}
	protected boolean getExisteD(){return this.droit;}
	protected boolean getExisteB(){return this.bas;}
	
	protected int getVoieG(){ return this.voieGauche;}
	protected int getVoieH(){ return this.voieHaut;}
	protected int getVoieD(){ return this.voieDroit;}
	protected int getVoieB(){ return this.voieBas;}
	
	public ElementCarrefour getElementGraph() {
		return this.elementGraphique;
	}
	
	
	
	/*---------------------------------------------------------
	 * 		MEMOIRE DU CARREFOUR
	 *---------------------------------------------------------*/

	// pour la gestion de la communication 
	protected GestMsgRoute gestMsgRoute = null ; 
	protected GestMsgVoiture gestMsgVoiture = null ; 
	protected GestMsgCarrefour gestMsgCarrefour = null ;
	
	//pour la connaissance de l'etat des routes connexes 
 	protected EtatRoute routeG = null ; 
 	protected EtatRoute routeH = null ; 
 	protected EtatRoute routeD = null ; 
 	protected EtatRoute routeB = null ; 
 	
 	// pour la configuration de son propre etat
 	protected EtatCarrefour etatCarrefourActuel = null;  
   	
}