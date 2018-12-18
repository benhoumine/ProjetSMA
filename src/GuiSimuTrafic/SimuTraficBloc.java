package GuiSimuTrafic;

import javax.swing.*;
import java.awt.*;
import Village.*;

/**
 * @author BENHOUMINE Abdelkhalek
 *
 */
public class SimuTraficBloc extends JButton {
	
	private String etat = "blank"; /* D?finit l'?tat du Bloc */
	private String orientation = ""; /* Orientation s'il y a lieu */
		
	private int ligne;
	private int colone;
	
	private ElementGenerique elementVillage = null;
	
	private static int echelle = 50; /* Echelle du Zoom en % */
	public static int scaleMethod = Image.SCALE_FAST;
	private static int size = 80;
	
	private static ImageIcon IconeBlank = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "blank.png"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
	private static ImageIcon IconeCarrefour = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefour4.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
	
	private static ImageIcon IconeCarrefour3H = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefour3H.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
	private static ImageIcon IconeCarrefour3D = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefour3D.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
	private static ImageIcon IconeCarrefour3G = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefour3G.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
	private static ImageIcon IconeCarrefour3B = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefour3B.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
	private static ImageIcon IconeCarrefour2HD = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefour2HD.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
	private static ImageIcon IconeCarrefour2BG = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefour2BG.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
	private static ImageIcon IconeCarrefour2HG = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefour2HG.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
	private static ImageIcon IconeCarrefour2BD = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefour2BD.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
	private static ImageIcon IconeCarrefourH = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefourH.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
	private static ImageIcon IconeCarrefourD = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefourD.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
	private static ImageIcon IconeCarrefourB = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefourB.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
	private static ImageIcon IconeCarrefourG = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefourG.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
	private static ImageIcon IconeCarrefourO = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefourO.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
	
	private static ImageIcon IconeRouteH = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "routeH.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
	private static ImageIcon IconeRouteV = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "routeV.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
	
		
	public SimuTraficBloc(int ligne, int colone) {		
	super(IconeBlank);
        this.ligne=ligne;
        this.colone=colone;
        this.setMargin(new Insets(0,0,0,0));
        this.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        this.setBackground(new Color(255,102,102));
	}
	
	
	/**
	 * Sets the bloc as being blank
	 */
	public void setEtatBlank() {
		this.setIcon(IconeBlank);
		etat = "blank";
	}
	
	/**
	 * Sets the bloc as being a Carrefour
	 */
	public void setEtatCarrefour() {
		this.setIcon(IconeCarrefour);
		etat = "carrefour";
		orientation = "";
	}
	
	/**
	 * Sets the bloc as being a Route
	 */
	public void setEtatRoute(String orientation) {
		if(orientation == "H") {
			this.setIcon(IconeRouteH);			
			etat = "route";
			this.orientation = "H";			
		} else {
			this.setIcon(IconeRouteV);
			etat = "route";
			this.orientation = "V";
		}		
	}

	public void actualiserImage() {
		/*if(Parametres.debug)
			System.out.println("Actualisation Images " + this.ligne +" " + this.colone);
	*/
		if (etat=="route") {			
			setEtatRoute(orientation);
			((ElementRoute) elementVillage).actualiserImage();
		}
		if(etat=="carrefour") {
			if(this.getElementVillage()==null) {
				setEtatCarrefour();
			} else {
				((ElementCarrefour) elementVillage).actualiserImage();
				if(Parametres.debug)
					System.out.println("Passage dans la maj carrefour complexe");
				if(this.orientation == "") 
					this.setIcon(IconeCarrefour);
				if(this.orientation == "3H") 
					this.setIcon(IconeCarrefour3H);
				if(this.orientation == "3D") 
					this.setIcon(IconeCarrefour3D);
				if(this.orientation == "3G") 
					this.setIcon(IconeCarrefour3G);
				if(this.orientation == "3B") 
					this.setIcon(IconeCarrefour3B);
				if(this.orientation == "2HD") 
					this.setIcon(IconeCarrefour2HD);
				if(this.orientation == "2BG") 					
					this.setIcon(IconeCarrefour2BG);
				if(this.orientation == "2HG") 
					this.setIcon(IconeCarrefour2HG);
				if(this.orientation == "2BD") { 					
					this.setIcon(IconeCarrefour2BD);
				}
				if(this.orientation == "2HB") 
					this.setIcon(IconeRouteV);
				if(this.orientation == "2GD") 
					this.setIcon(IconeRouteH);
				if(this.orientation == "H") 
					this.setIcon(IconeCarrefourH);
				if(this.orientation == "D") 
					this.setIcon(IconeCarrefourD);
				if(this.orientation == "B") 
					this.setIcon(IconeCarrefourB);
				if(this.orientation == "G") 
					this.setIcon(IconeCarrefourG);
				if(this.orientation == "O") 
					this.setIcon(IconeCarrefourO);
			}
		}
		if(etat=="blank") {
			setEtatBlank();			
		}	
	}
	
	/**
	 * @return Returns the Etat.
	 */
	public String getEtat() {
		return etat;
	}
	
	/**
	 * @return Returns the Orientation.
	 */
	public String getOrientation() {
		return orientation;
	}
	
	/**
	 * Permet de changer l'orientation d'un bloc
	 * @param orientation nouvelle orientation
	 */
	public void setOritentation(String orientation) {
		this.orientation=orientation;
	}
	
	/**
	 * @return Returns the colone.
	 */
	public int getColone() {
		return colone;
	}
	/**
	 * @return Returns the ligne.
	 */
	public int getLigne() {
		return ligne;
	}
	/**
	 * @return Returns the echelle.
	 */
	public static int getEchelle() {
		return echelle;
	}
	
	/**
	 * @param echelle The echelle to set.
	 */
	public static void setEchelle(int echelle) {
		
		SimuTraficBloc.echelle = echelle;
		
		if(Parametres.debug)
			System.out.println("Mise ? l'?chelle " + SimuTraficBloc.echelle +" des icones");
		
		
		IconeBlank = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "blank.png"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
		
		IconeCarrefour = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefour4.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
		IconeCarrefour3H = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefour3H.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
		IconeCarrefour3D = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefour3D.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
		IconeCarrefour3G = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefour3G.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
		IconeCarrefour3B = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefour3B.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
		IconeCarrefour2HD = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefour2HD.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
		IconeCarrefour2BG = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefour2BG.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
		IconeCarrefour2HG = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefour2HG.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
		IconeCarrefour2BD = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefour2BD.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
		IconeCarrefourH = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefourH.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
		IconeCarrefourD = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefourD.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
		IconeCarrefourB = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefourB.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
		IconeCarrefourG = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefourG.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
		IconeCarrefourO = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "carrefourO.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
		
		IconeRouteH = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "routeH.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
		IconeRouteV = new ImageIcon((new ImageIcon( SimuTraficBloc.class.getResource(Parametres.imagepath + "routeV.jpg"))).getImage().getScaledInstance(size*echelle/100,size*echelle/100,scaleMethod));
	}
	/**
	 * @return Returns the size.
	 */
	public static int getTaille() {
		return size;
	}
	/**
	 * @return Returns the elementVillage.
	 */
	public ElementGenerique getElementVillage() {
		return elementVillage;
	}
	/**
	 * @param elementVillage The elementVillage to set.
	 */
	public void setElementVillage(ElementGenerique elementVillage) {
		this.elementVillage = elementVillage;
	}
}
