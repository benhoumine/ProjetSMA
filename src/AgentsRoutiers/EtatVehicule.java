

package AgentsRoutiers;
/**
 *
 * <p>Titre: Etat D'un Vehicule </p>
 * <p>Description: Permet de connaitre certaines informations sur un vehicule</p>
 * @author Naji kawtar & Soukayna etalbi
 * @version 1.0
 */

public class EtatVehicule {

    private int position;
    private int vitesse;
    private int identite;
    private int etatActuel;
    private String direction ;
    private int voie ; 
    private String voieChoisie = "";
    private String nom = ""; 
    private String lieu = "carrefour";
    private int VoieDeProvenance = 0 ; // id de la voie de provenance
    private String typeConduite = "sociable";  
    private int indexListe = 0 ; // position du vehicule dans la liste  
    							 // de la route surlaquelle il roule 
    
    // constructeur donnant l'etat d'un vehicule
    public EtatVehicule(int posRelative, int vitesse, int ID, int etatActuel, String direction, String name, int voieActu) {
        
    	this.position = posRelative;
        this.vitesse = vitesse;
        this.identite = ID;
        this.etatActuel = etatActuel;
        this.direction = direction ;
        this.voie = voieActu ;
        this.nom = name ;
    }

    // constructeur donnant l'etat du vehicule + le choix de la voie a prendre au prochain carrefour
    public EtatVehicule(int posRelative, int vitesse, int ID, int etatActuel, String direction, String name, int voieActu, String choixVoie) {
        
    	this.position = posRelative;
        this.vitesse = vitesse;
        this.identite = ID;
        this.etatActuel = etatActuel;
        this.direction = direction ;
        this.voie = voieActu ; 
        this.voieChoisie = choixVoie;
        this.nom = name ; 
    }


    // accesseurs
    public int getPos() {return position;}
    public int getVitesse() {return vitesse;}
    public int getID() {return identite;}
    public int getEtatActuel() { return etatActuel;}
    public int getVoie(){ return voie;}
    public String getDirection(){  return this.direction;}
    public String getVoieChoisie(){ return this.voieChoisie;}
    public int getIndex(){return this.indexListe; } 
    public String getNom() { return this.nom;}
    public String getTypeDeConduite(){return this.typeConduite;}
    public String getLieu(){return this.lieu;}
    public int getVoieProvenance() {return this.VoieDeProvenance; }
    
    public void setPos(int position) {this.position = position ;}
    public void setVitesse(int vitesse) {this.vitesse = vitesse ;}
    public void setID(int id) {this.identite = id ;}
    public void setEtatActuel(int etat) {this.etatActuel = etat ;}
    public void setDirection(String direction) {this.direction = direction ;}
    public void setVoie(int voieActu) {this.voie = voieActu ;}
    public void setVoieChoisie(String voie) {this.voieChoisie = voie ;}
    public void setIndex(int i) {this.indexListe = i; }
    public void setNom(String name) {this.nom = name; }
    public void setTypeDeConduite(String conduite) {this.typeConduite = conduite; }
    public void setLieu(String where) {this.lieu = where;} 
    public void setVoieProvenance(int numVoie){this.VoieDeProvenance = numVoie;} 
    
}
