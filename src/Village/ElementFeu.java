
package Village;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import GuiSimuTrafic.Parametres;
import GuiSimuTrafic.SimuTraficBloc;

/**
 * @author Naji kawtar & Soukayna etalbi
 */
public class ElementFeu {

	
	private static int size = 21;
	JLayeredPane paneauFeux;
	SimuTraficBloc blocGraphique;
	int Orientation;
	String Etat;
	JLabel feu1;
	JLabel feu2;
	
	private ImageIcon IconeFeuRouge = new ImageIcon((new ImageIcon( this.getClass().getResource(Parametres.imagepath + "feuRouge.png"))).getImage().getScaledInstance(size*SimuTraficBloc.getEchelle()/100,size*SimuTraficBloc.getEchelle()/100,SimuTraficBloc.scaleMethod));
	private ImageIcon IconeFeuOrange = new ImageIcon((new ImageIcon( this.getClass().getResource(Parametres.imagepath + "feuOrange.png"))).getImage().getScaledInstance(size*SimuTraficBloc.getEchelle()/100,size*SimuTraficBloc.getEchelle()/100,SimuTraficBloc.scaleMethod));
	private ImageIcon IconeFeuVert = new ImageIcon((new ImageIcon( this.getClass().getResource(Parametres.imagepath + "feuVert.png"))).getImage().getScaledInstance(size*SimuTraficBloc.getEchelle()/100,size*SimuTraficBloc.getEchelle()/100,SimuTraficBloc.scaleMethod));
	
	public ElementFeu(int Orientation, SimuTraficBloc leBloc, JLayeredPane paneauFeux) {		
		this.Orientation = Orientation;
		this.blocGraphique = leBloc;
		this.paneauFeux = paneauFeux;
		
		//creerEtAfficherFeux();
		
	}
	
	public void creerEtAfficherFeux() {
		
		feu1 = new JLabel(IconeFeuRouge);
		feu1.setBounds(0,0, IconeFeuRouge.getIconWidth(), IconeFeuRouge.getIconHeight());
		feu2 = new JLabel(IconeFeuRouge);
		feu2.setBounds(0,0, IconeFeuRouge.getIconWidth(), IconeFeuRouge.getIconHeight());
		
		paneauFeux.add(feu1, JLayeredPane.DRAG_LAYER);
		paneauFeux.add(feu2, JLayeredPane.DRAG_LAYER);
		
		if(Orientation == 0) { // Route Horizontale
			if(Parametres.debug) System.out.println(blocGraphique.getOrientation());
			if(Parametres.debug) System.out.println((blocGraphique.getOrientation().indexOf('3') > 0 && blocGraphique.getOrientation() != "3D"));
			if(blocGraphique.getOrientation().indexOf('G') > 0 
			|| blocGraphique.getOrientation() == "" 
			|| (blocGraphique.getOrientation().indexOf('3') == 0 && blocGraphique.getOrientation() != "3D")) {
				
				feu1.setLocation(blocGraphique.getColone()*SimuTraficBloc.getTaille()*SimuTraficBloc.getEchelle()/100, ((blocGraphique.getLigne()+1)*SimuTraficBloc.getTaille()-size)*SimuTraficBloc.getEchelle()/100);
			} else {
				feu1.setVisible(false);
			}
			if(blocGraphique.getOrientation().indexOf('D') > 0 
			|| blocGraphique.getOrientation() == ""
			|| (blocGraphique.getOrientation().indexOf('3') == 0 && blocGraphique.getOrientation() != "3G")) {
				
				feu2.setLocation(((blocGraphique.getColone()+1)*SimuTraficBloc.getTaille()-size)*SimuTraficBloc.getEchelle()/100, blocGraphique.getLigne()*SimuTraficBloc.getTaille()*SimuTraficBloc.getEchelle()/100);
			} else {
				feu2.setVisible(false);
			}
		} else {			
			if(blocGraphique.getOrientation().indexOf('H') > 0 
			|| blocGraphique.getOrientation() == "" 
		    || (blocGraphique.getOrientation().indexOf('3') == 0 && blocGraphique.getOrientation() != "3B")) {
				
				if(Parametres.debug) System.out.println("BOOM " + blocGraphique.getOrientation());
				feu1.setLocation(blocGraphique.getColone()*SimuTraficBloc.getTaille()*SimuTraficBloc.getEchelle()/100, blocGraphique.getLigne()*SimuTraficBloc.getTaille()*SimuTraficBloc.getEchelle()/100);
			} else {
				feu1.setVisible(false);
			}
			if(blocGraphique.getOrientation().indexOf('B') > 0 
			|| blocGraphique.getOrientation() == ""
			|| (blocGraphique.getOrientation().indexOf('3') == 0 && blocGraphique.getOrientation() != "3H")) {
				
				feu2.setLocation(((blocGraphique.getColone()+1)*SimuTraficBloc.getTaille()-size)*SimuTraficBloc.getEchelle()/100, ((blocGraphique.getLigne()+1)*SimuTraficBloc.getTaille()-size)*SimuTraficBloc.getEchelle()/100);	
			} else {
				feu2.setVisible(false);
			}			
		}		
	}
	
	public void changerEtat(String nouvelEtat) {
		this.Etat = nouvelEtat;
		
		if(this.Etat == "rouge") {
			feu1.setIcon(IconeFeuRouge);
			feu2.setIcon(IconeFeuRouge);
		}
		if(this.Etat == "vert") {
			feu1.setIcon(IconeFeuVert);
			feu2.setIcon(IconeFeuVert);
		}
		if(this.Etat == "orange") {
			feu1.setIcon(IconeFeuOrange);
			feu2.setIcon(IconeFeuOrange);
		}
	}
	
	public void actualiserImage() {
		if (feu1 != null) {
			IconeFeuRouge = new ImageIcon((new ImageIcon( this.getClass().getResource(Parametres.imagepath + "feuRouge.png"))).getImage().getScaledInstance(size*SimuTraficBloc.getEchelle()/100,size*SimuTraficBloc.getEchelle()/100,SimuTraficBloc.scaleMethod));
			IconeFeuOrange = new ImageIcon((new ImageIcon( this.getClass().getResource(Parametres.imagepath + "feuOrange.png"))).getImage().getScaledInstance(size*SimuTraficBloc.getEchelle()/100,size*SimuTraficBloc.getEchelle()/100,SimuTraficBloc.scaleMethod));
			IconeFeuVert = new ImageIcon((new ImageIcon( this.getClass().getResource(Parametres.imagepath + "feuVert.png"))).getImage().getScaledInstance(size*SimuTraficBloc.getEchelle()/100,size*SimuTraficBloc.getEchelle()/100,SimuTraficBloc.scaleMethod));
			
			feu1.setBounds(0,0, IconeFeuRouge.getIconWidth(), IconeFeuRouge.getIconHeight());
			feu2.setBounds(0,0, IconeFeuRouge.getIconWidth(), IconeFeuRouge.getIconHeight());
			
			if(Orientation == 0) { // Route Horizontale
				if(Parametres.debug) System.out.println(blocGraphique.getOrientation());
				if(Parametres.debug) System.out.println((blocGraphique.getOrientation().indexOf('3') > 0 && blocGraphique.getOrientation() != "3D"));
				if(blocGraphique.getOrientation().indexOf('G') > 0 
						|| blocGraphique.getOrientation() == "" 
							|| (blocGraphique.getOrientation().indexOf('3') == 0 && blocGraphique.getOrientation() != "3D")) {
					
					feu1.setLocation(blocGraphique.getColone()*SimuTraficBloc.getTaille()*SimuTraficBloc.getEchelle()/100, ((blocGraphique.getLigne()+1)*SimuTraficBloc.getTaille()-size)*SimuTraficBloc.getEchelle()/100);
				} else {
					feu1.setVisible(false);
				}
				if(blocGraphique.getOrientation().indexOf('D') > 0 
						|| blocGraphique.getOrientation() == ""
							|| (blocGraphique.getOrientation().indexOf('3') == 0 && blocGraphique.getOrientation() != "3G")) {
					
					feu2.setLocation(((blocGraphique.getColone()+1)*SimuTraficBloc.getTaille()-size)*SimuTraficBloc.getEchelle()/100, blocGraphique.getLigne()*SimuTraficBloc.getTaille()*SimuTraficBloc.getEchelle()/100);
				} else {
					feu2.setVisible(false);
				}
			} else {			
				if(blocGraphique.getOrientation().indexOf('H') > 0 
						|| blocGraphique.getOrientation() == "" 
							|| (blocGraphique.getOrientation().indexOf('3') == 0 && blocGraphique.getOrientation() != "3B")) {
					
					if(Parametres.debug) System.out.println("BOOM " + blocGraphique.getOrientation());
					feu1.setLocation(blocGraphique.getColone()*SimuTraficBloc.getTaille()*SimuTraficBloc.getEchelle()/100, blocGraphique.getLigne()*SimuTraficBloc.getTaille()*SimuTraficBloc.getEchelle()/100);
				} else {
					feu1.setVisible(false);
				}
				if(blocGraphique.getOrientation().indexOf('B') > 0 
						|| blocGraphique.getOrientation() == ""
							|| (blocGraphique.getOrientation().indexOf('3') == 0 && blocGraphique.getOrientation() != "3H")) {
					
					feu2.setLocation(((blocGraphique.getColone()+1)*SimuTraficBloc.getTaille()-size)*SimuTraficBloc.getEchelle()/100, ((blocGraphique.getLigne()+1)*SimuTraficBloc.getTaille()-size)*SimuTraficBloc.getEchelle()/100);	
				} else {
					feu2.setVisible(false);
				}			
			}		
		}
	}
	
}
