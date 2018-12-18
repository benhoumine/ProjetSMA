
package AgentsRoutiers;

/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
*/


public class EtatRoute {

    private int identite ;
    private int vitesseAutorisee ;  
    private String etat ; 
    
    private EtatVehicule EtatVehiculeCourant = null; 
    private EtatVehicule EtatVehiculeSuiv = null;
    private EtatVehicule EtatVehiculePrec = null;
    private EtatVehicule EtatVehiculeSortant = null; 
    
    private EtatCarrefour carrefourDebut = new EtatCarrefour(0,false,false,false,false);
    private EtatCarrefour carrefourFin = new EtatCarrefour(0,false,false, false, false) ;

    //CONSTRUCTEURS
    public EtatRoute(int id, int speedAgree, 
    				 EtatCarrefour carrefourD, EtatCarrefour carrefourF) {
        this.identite = id ;
        this.vitesseAutorisee = speedAgree; 
        this.carrefourDebut = carrefourD;
        this.carrefourFin = carrefourF;
    }

    public EtatRoute(int id, int speedAgree, String go0, String go1, 
    		EtatCarrefour carrefourD, EtatCarrefour carrefourF, EtatVehicule vehicSuiv){
        this.identite = id ;
        this.vitesseAutorisee = speedAgree;
        this.carrefourDebut = carrefourD;
        this.carrefourFin = carrefourF;
        this.EtatVehiculeSuiv = vehicSuiv;
    }

    public EtatRoute(int id, int speedAgree, 
    		EtatCarrefour carrefourD, EtatCarrefour carrefourF, EtatVehicule vehicPrec, EtatVehicule vehicSuiv){
        this.identite = id ;
        this.vitesseAutorisee = speedAgree;
        this.carrefourDebut = carrefourD;
        this.carrefourFin = carrefourF;
        this.EtatVehiculeSuiv = vehicSuiv;
        this.EtatVehiculePrec = vehicPrec;
    }

    public EtatRoute(int id, String etatRoute, EtatVehicule vehicOut, EtatVehicule vehicActu){
        this.identite = id ;
        this.etat = etatRoute ; 
        this.EtatVehiculeSortant = vehicOut;
        this.EtatVehiculeCourant = vehicActu; 
    }


    //ACCESSEURS
    public int getID() { return identite; }
    public int getVitesseAutorisee(){return vitesseAutorisee; }
    public EtatVehicule getVehiculeSortant(){ return EtatVehiculeSortant; }
    public EtatVehicule getEtatVehiculeCourant(){return EtatVehiculeCourant;}
    
    public EtatVehicule getEtatVehicSuiv(){
        return this.EtatVehiculeSuiv;
    }

    public EtatVehicule getEtatVehicPrec(){
        return this.EtatVehiculePrec;
    }


    public EtatCarrefour getEtatCarrefourDebut(){
        return (this.carrefourDebut);
    }

    public EtatCarrefour getEtatCarrefourFin(){
        return (this.carrefourFin);
    }

    
    public void setEtatVehiculeSuiv(EtatVehicule suiv){
    	this.EtatVehiculeSuiv = suiv ; 
    }
    
    public void setEtatVehiculePrec(EtatVehicule prec){
    	this.EtatVehiculePrec = prec ; 
    }

    public void setEtatVehiculeCourant(EtatVehicule courant){
    	this.EtatVehiculeCourant = courant ;
    }
    
    public void setEtatVehiculeSortant(EtatVehicule sortant){
    	this.EtatVehiculeSortant = sortant ; 
    }
    
	public void setCarrefourDebut(EtatCarrefour carrefourDebut) {
		this.carrefourDebut = carrefourDebut;
	}

	public void setCarrefourFin(EtatCarrefour carrefourFin) {
		this.carrefourFin = carrefourFin;
	}
}
