package Village;

import javax.swing.*;

import GuiSimuTrafic.Parametres;
import GuiSimuTrafic.SimuTraficBloc;
/**
 * @author Naji kawtar & Soukayna etalbi
 */
public class ElementVehicule extends JLabel {

	public static final int VOITURE_NORMALE_0 = 0;
	public static final int VOITURE_NORMALE_1 = 1;
	public static final int VOITURE_POMPIERS  = 2;
	public static final int VOITURE_POLICE    = 3;
	
	public static final int DIRECTION_VERTICAL = 0;
	public static final int DIRECTION_HORIZONTALE = 1;
	
	private int typeVehicule;
	private int direction;
	private int sens;
	private int offset;
	
	
	public ElementVehicule(int typeVehicule, int direction, int sens) {
		super();		
		String suffixe = (direction == 0) ? "V" : "H";
		this.typeVehicule = typeVehicule;
		this.direction = direction;
		this.sens = sens;
		this.setIcon(new ImageIcon( this.getClass().getResource(Parametres.imagepath + "voiture_"+typeVehicule+"_"+suffixe+"_"+sens+".png")));
		this.setIcon(new ImageIcon(((ImageIcon)this.getIcon()).getImage().getScaledInstance(this.getIcon().getIconWidth()*SimuTraficBloc.getEchelle()/100, this.getIcon().getIconHeight()*SimuTraficBloc.getEchelle()/100, SimuTraficBloc.scaleMethod)));
		this.setBounds(0,0,this.getIcon().getIconWidth(), this.getIcon().getIconHeight());
	}
	
	public void actualiserImage() {
		String suffixe = (direction == 0) ? "V" : "H";
		this.setIcon(new ImageIcon( this.getClass().getResource(Parametres.imagepath + "voiture_"+typeVehicule+"_"+suffixe+"_"+sens+".png")));
		this.repaint();
		this.setIcon(new ImageIcon(((ImageIcon)this.getIcon()).getImage().getScaledInstance(this.getIcon().getIconWidth()*SimuTraficBloc.getEchelle()/100, this.getIcon().getIconHeight()*SimuTraficBloc.getEchelle()/100, SimuTraficBloc.scaleMethod)));		
	}
	
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getSens() {
		return sens;
	}
	public void setSens(int sens) {
		this.sens = sens;
	}

}
