
package AgentsRoutiers;

/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
* 
* 
*/

public class EtatCarrefour {

    private int identite ;
    private boolean gauche ;
    private boolean haut ;
    private boolean droit ;
    private boolean bas ;
    private EtatVehicule vehiculeSortant = new EtatVehicule(0,0,0,0,"","",0); 
    private EtatVehicule vehiculeEntrant = new EtatVehicule(0,0,0,0,"","",0); 
    
    
    //CONSTRUCTEURS
    public EtatCarrefour(int ID, boolean G, boolean H, boolean D, boolean B){
        this.identite = ID;
        this.gauche = G ;
        this.haut = H ;
        this.droit = D ;
        this.bas = B ;
    }

    public EtatCarrefour(int ID, boolean G, boolean H, boolean D, boolean B, EtatVehicule entrant, EtatVehicule sortant){
        this.identite = ID;
        this.gauche = G ;
        this.haut = H ;
        this.droit = D ;
        this.bas = B ;
        this.vehiculeEntrant = entrant; 
        this.vehiculeSortant = sortant;
    }
    
    
    //accesseurs
    public int getID() { return this.identite ;}
    public boolean getG() { return this.gauche ;}
    public boolean getH() { return this.haut ;}
    public boolean getD() { return this.droit ;}
    public boolean getB() { return this.bas ;}
    public EtatVehicule getVehicSortant(){ return this.vehiculeSortant; }
    public EtatVehicule getVehicEntrant(){ return this.vehiculeEntrant; }
    
    public void setID(int ID) {this.identite = ID ;}
    public void setG(boolean G) { this.gauche = G ;}
    public void setH(boolean H) { this.haut = H ;}
    public void setD(boolean D) { this.droit = D ;}
    public void setB(boolean B) { this.bas = B ;}
    public void setVehicEntrant(EtatVehicule entrant) { this.vehiculeEntrant = entrant; }
    public void setVehicSortant(EtatVehicule sortant) { this.vehiculeSortant = sortant; }
    
}

