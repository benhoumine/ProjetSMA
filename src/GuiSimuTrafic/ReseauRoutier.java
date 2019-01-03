package GuiSimuTrafic;



import javax.swing.*;

import GUI.ReglageRoute;
import GUI.ReglageCarrefour;
import Village.ElementCarrefour;
import Village.ElementRoute;
import Village.ElementVillage;

import java.awt.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
* 
* 
*/
public class ReseauRoutier extends JScrollPane {

	Vector listeCases = new Vector();
	int taille;
	JLayeredPane metaBoite = new JLayeredPane();
	JPanel boite = new JPanel();
	JLabel LabelTemp ;
	ReglageRoute reglage;
	ReglageCarrefour reglageC;
	ElementVillage leVillage;
	
  public ReseauRoutier(int taille) {
  	
  	super();
  	this.taille = taille;
  	GridLayout gestionBoite = new GridLayout(taille,taille);  	
  	boite.setLayout(gestionBoite);

  	boite.setBounds(0,0,taille*SimuTraficBloc.getEchelle()*SimuTraficBloc.getTaille()/100, taille*SimuTraficBloc.getEchelle()*SimuTraficBloc.getTaille()/100);  	
  
  	/*LabelTemp = new JLabel(new ImageIcon(new ImageIcon("voiture2.png").getImage().getScaledInstance(10,10,Image.SCALE_FAST)));
  	LabelTemp.setBounds(0,0,LabelTemp.getIcon().getIconWidth(), LabelTemp.getIcon().getIconHeight());
  	LabelTemp.setLocation(10,10);
  	//(new LabelRunner(LabelTemp, true)).start();
  	metaBoite.add(LabelTemp, 0);*/
  	
  	
  	metaBoite.add(boite, 1);
  	  	
  	metaBoite.setPreferredSize(boite.getSize());
  	metaBoite.setOpaque(true);

  	
  	this.setViewportView(metaBoite);

  	
  	//this.getViewport().setViewSize()

    ReseauRoutierListener gestionClik = new ReseauRoutierListener();
  	
  	for (int i=0; i<taille; i++) {  		
  		listeCases.add(i,new Vector());
  		for (int j=0; j<taille; j++) {
  			Vector v = (Vector) listeCases.get(i);
			v.add(j, new SimuTraficBloc(i,j));
			boite.add((SimuTraficBloc) v.get(j));
			((SimuTraficBloc) v.get(j)).addMouseListener(gestionClik);
  		}
  	}
  
  }
  
  public void setVillage(ElementVillage leVillage) {
  	this.leVillage = leVillage;
  }
  
  public void recadrer(SimuTraficBloc centre) {
  	
  	int ligne = centre.getLigne();
  	int colone = centre.getColone();
  	int tailleCase = SimuTraficBloc.getEchelle()*SimuTraficBloc.getTaille()/100;

  	boite.setBounds(0,0,taille*SimuTraficBloc.getEchelle()*SimuTraficBloc.getTaille()/100, taille*SimuTraficBloc.getEchelle()*SimuTraficBloc.getTaille()/100);
  	Dimension taille = this.getViewport().getSize();
  	Point vue = new Point((colone*tailleCase-(taille.width/2)), (ligne*tailleCase-(taille.height/2)));
 
  	// Recadrage dans l'affichable.
  	vue.x = (vue.x < 0) ? 0 : vue.x;
  	vue.y = (vue.y < 0) ? 0 : vue.y;

  	// Affichage du cadre recard? si besoin
  	this.getViewport().setViewPosition(vue);
  	
  	if(Parametres.debug)
  		System.out.println("Nouvelle vue : " + this.getViewport().getViewPosition());
  	
  }
  
	/**
	 * @return Returns the listeCases.
	 */
	public Vector getListeCases() {
		return listeCases;
	}
	
	public JLayeredPane getMetaBoite() {
		return metaBoite; 
	}
	
  private class ReseauRoutierListener implements MouseListener {
  	
  	// gestion des ?venements de cases de la route
  	public void mousePressed (MouseEvent evenement){
  		
  		SimuTraficBloc source = (SimuTraficBloc) evenement.getSource();
  		
  		if (Parametres.isUsed == "select") {
  			
  			if (source.getElementVillage() instanceof ElementRoute ) {
  				if (reglage != null) {
  					reglage.dispose();
  				}
  				reglage = new ReglageRoute((ElementRoute) source.getElementVillage());
  				
  			} else if (source.getElementVillage() instanceof ElementCarrefour)  {
  				if(reglageC != null) {
  					reglageC.dispose();
  				} 
  				reglageC = new ReglageCarrefour((ElementCarrefour) source.getElementVillage());  			  			
  			}
  			
  			
  			if(Parametres.debug)
  				System.out.println("selection d'un bloc de traffic");
  			return;
  		} else if (Parametres.isUsed == "suppr"){
  			// On vide le carrefour et les routes attenantes
  			// si elle ne sont pas reli?es ? un carrefour
  			if(Parametres.debug)
  				System.out.println("Suppression d'un bloc demand?e");
  			
  			if (source.getEtat() == "route") {
  				if(Parametres.debug)
  					System.out.println("Suppression d'une route");
  				supprimerRoute(source);
  			} else if(source.getEtat() == "carrefour") {
  				if(Parametres.debug)
  	  				System.out.println("Suppression d'un carrefour");
  				supprimerCarrefour(source);
  			}  			

  			return;
  			
  		} else if (Parametres.isUsed == "carrefour") {  			  			
  			if(noCarrefoursAutours(source)) {
  				source.setEtatCarrefour();  			  			
  				
  			if(Parametres.debug)
  				System.out.println("Placement d'un carrefour");
  			}
  			
  			return;
  			
  		} else if (Parametres.isUsed == "route") {
  			SimuTraficBloc[] carrefoursProches = trouverCarrefourProche(source);
  			
  			if(carrefoursProches[0] != null && carrefoursProches[1] != null) {
  				if (Parametres.debug)
  					System.out.println("Carrefour le plus proche: " + carrefoursProches[0].getLigne()+ " " + carrefoursProches[1].getLigne());
  				if(carrefoursProches.length == 2) {
  					if (Parametres.debug)
  	  					System.out.println("Carrefour le plus proche: " + carrefoursProches[0].getLigne()+ " " + carrefoursProches[1].getLigne());
  					tracerRoute(carrefoursProches);
  				} else {
  					if (Parametres.debug)
  	  					System.out.println("Carrefour les plus proche: " + carrefoursProches[0].getLigne()+ " " + carrefoursProches[1].getLigne());
  					if (Parametres.debug)
  	  					System.out.println("Carrefour les plus proche: " + carrefoursProches[2].getLigne()+ " " + carrefoursProches[3].getLigne());
  					
  					SimuTraficBloc[] carrefours1 = {carrefoursProches[0], carrefoursProches[1]};
  					tracerRoute(carrefours1);
  					SimuTraficBloc[] carrefours2 = {carrefoursProches[2], carrefoursProches[3]};
  					tracerRoute(carrefours2);
  				}
  			} 
  			
  			if(Parametres.debug)
  				System.out.println("Placement d'une route");
  			return;
  		} else if (Parametres.isUsed == "zoom") {
  			if (Parametres.debug)
  				System.out.println("ZOOM demand?");
  			// Bouton Gauche, Zoom-IN
  			if(evenement.getButton() == MouseEvent.BUTTON1) {
  				if (Parametres.debug)
  					System.out.println("Zoom-IN demand?");
  				zoomer(source, true);
  			}  				
  			// Bouton Droit, Zoom-OUT
  			if(evenement.getButton() == MouseEvent.BUTTON2 || evenement.getButton() == MouseEvent.BUTTON3) {
  				if (Parametres.debug)
  					System.out.println("Zoom-OUT demand?");
  				zoomer(source, false);
  			}
  		} else if (Parametres.isUsed == "voiture") {
  			 String[] sexe = {"Course", "Simple"};
  		    JOptionPane jop = new JOptionPane();
  		    int rang = jop.showOptionDialog(null, 
  		      "Veuillez indiquer le Type de voiture désirée !",
  		      "Choisissez votre type!",
  		      JOptionPane.YES_NO_CANCEL_OPTION,
  		      JOptionPane.QUESTION_MESSAGE,
  		      null,
  		      sexe,
  		      sexe[1]);
  			Parametres.TypeVoitureParDefaut = rang; 
  			
  			if (source.getElementVillage() instanceof ElementCarrefour) {
  				leVillage.ajouterVehicule((ElementCarrefour) source.getElementVillage());
  			}
  		}
  		
  	}
  	
  	private boolean noCarrefoursAutours(SimuTraficBloc s) {
  		boolean b[] = new boolean[4];
  		
  		if(s.getColone()>0) {
  			b[0] = ((SimuTraficBloc) ((Vector) listeCases.get(s.getLigne())).get(s.getColone()-1)).getEtat() != "carrefour"; 
  		} else {
  			b[0] = true;
  		}
  		
  		if(s.getColone()+ 1 < listeCases.size()) {
  			b[1] = ((SimuTraficBloc) ((Vector) listeCases.get(s.getLigne())).get(s.getColone()+1)).getEtat() != "carrefour";
  		} else {
  			b[1] = true;
  		}
  		
  		if(s.getLigne()>0) {
  			b[2] = ((SimuTraficBloc) ((Vector) listeCases.get(s.getLigne()-1)).get(s.getColone())).getEtat() != "carrefour";
  		} else {
  			b[2] = true;
  		}
  		
  		if(s.getLigne() + 1<listeCases.size()) {
  			b[3] = ((SimuTraficBloc) ((Vector) listeCases.get(s.getLigne()+1)).get(s.getColone())).getEtat() != "carrefour";
  		} else {
  			b[3] = true;
  		}
  	return b[0] && b[1] && b[2] && b[3];

  	}
   	
  	private void zoomer(SimuTraficBloc centre, boolean zoomin) {
  		
  		if(zoomin) {  			  	
  			SimuTraficBloc.setEchelle(SimuTraficBloc.getEchelle()+10);
  		} else {
  			if(SimuTraficBloc.getEchelle()-10 > 10) {
  				SimuTraficBloc.setEchelle(SimuTraficBloc.getEchelle()-10);
  			}
  		}
  		
  		// On passe en revue les cases et on les r?actualise
  		
  		for(int i=0; i<listeCases.size(); i++) {
  			for(int j=0; j<((Vector) listeCases.get(i)).size(); j++) {
  				
  				SimuTraficBloc actuel = ((SimuTraficBloc) ((Vector) listeCases.get(i)).get(j));
  				/*if(Parametres.debug)
  				 System.out.println("Traitement case " + actuel.getLigne() +" " + actuel.getColone() );
  				 */
  				actuel.actualiserImage();
  			}
  		}
  		
  		// On recentre la vue sur la case du click
  		recadrer(centre);
  		
  	}
  
  	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	
	public void mouseMoved(MouseEvent e) {
        LabelTemp.setLocation(e.getX()-LabelTemp.getWidth()/2,
                              e.getY()-LabelTemp.getHeight()/2);
    }
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

  	private void tracerRoute(SimuTraficBloc[] carrefours) {
  		if(carrefours[0].getLigne() == carrefours[1].getLigne()) {
  			//Il faut tracer une route horizontale
  			Vector casesLigne = (Vector) listeCases.get(carrefours[0].getLigne());
  			for (int i=carrefours[0].getColone()+1; i<carrefours[1].getColone();i++) {
  				SimuTraficBloc actuel = (SimuTraficBloc) casesLigne.get(i); 
  				if(actuel.getEtat() != "route" && actuel.getEtat() != "carrefour") {
  					actuel.setEtatRoute("H");
  				} else if(actuel.getOrientation() != "H") {
  					actuel.setEtatCarrefour();
  				}
  			}  			
  		} else {
  			// Il faut tracer une route verticale
  			for (int i=carrefours[0].getLigne()+1; i<carrefours[1].getLigne();i++) {
  				SimuTraficBloc actuel = (SimuTraficBloc) ((Vector) listeCases.get(i)).get(carrefours[0].getColone());
  				if(actuel.getEtat() != "route" && actuel.getEtat() != "carrefour") {
  					actuel.setEtatRoute("V");
  				} else if(actuel.getOrientation() != "V"){
  					actuel.setEtatCarrefour();
  				}
  			}
  		}
  	}
  	
  	private void supprimerCarrefour(SimuTraficBloc carrefour) {
  		// On va supprimer un carrefour et toutes les routes attenantes au carrefour..
  		  		
  		int ligne = carrefour.getLigne();
  		int colone = carrefour.getColone();
  		
  		// Cases au dessus et en dessous
  		if(ligne-1>0) {
  			if(((SimuTraficBloc) ((Vector) listeCases.get(ligne-1)).get(colone)).getEtat() == "route" && ((SimuTraficBloc) ((Vector) listeCases.get(ligne-1)).get(colone)).getOrientation() == "V") {
  				supprimerRoute((SimuTraficBloc) ((Vector) listeCases.get(ligne-1)).get(colone));
  			}
  		}
  		if(ligne+1<listeCases.size()) {
  			if(((SimuTraficBloc) ((Vector) listeCases.get(ligne+1)).get(colone)).getEtat() == "route" && ((SimuTraficBloc) ((Vector) listeCases.get(ligne+1)).get(colone)).getOrientation() == "V") {
  				supprimerRoute((SimuTraficBloc) ((Vector) listeCases.get(ligne+1)).get(colone));
  			}
  		}
  		
  		// Cases ? gauche et ? droite
  		if(colone-1>0) {
  			if(((SimuTraficBloc) ((Vector) listeCases.get(ligne)).get(colone-1)).getEtat() == "route" && ((SimuTraficBloc) ((Vector) listeCases.get(ligne)).get(colone-1)).getOrientation() == "H") {
  				supprimerRoute((SimuTraficBloc) ((Vector) listeCases.get(ligne)).get(colone-1));
  			}
  		}  		
  		if(colone+1<((Vector) listeCases.get(ligne)).size()) {
  			if(((SimuTraficBloc) ((Vector) listeCases.get(ligne)).get(colone+1)).getEtat() == "route" && ((SimuTraficBloc) ((Vector) listeCases.get(ligne)).get(colone+1)).getOrientation() == "H") {
  				supprimerRoute((SimuTraficBloc) ((Vector) listeCases.get(ligne)).get(colone+1));
  			}
  		}
  		
  		// On supprime maintenant le carrefour
  		
		((SimuTraficBloc) ((Vector) listeCases.get(ligne)).get(colone)).setEtatBlank();
  	}
  	
  	private void supprimerRoute(SimuTraficBloc route) {
  		
  		SimuTraficBloc[] carrefours = trouverCarrefourProche(route);
  		
  		// Si c'est une route verticale
  		if(route.getOrientation() == "V") {
  			int colone = route.getColone();
  			  			 			
  			for (int i=carrefours[0].getLigne()+1; i<carrefours[1].getLigne(); i++) {
  				((SimuTraficBloc) ((Vector) listeCases.get(i)).get(colone)).setEtatBlank();
  			}
 	
 		}
  		// Si c'est une route horizontale
  		if(route.getOrientation() == "H") {
  			int ligne = route.getLigne();
  					 		
  			Vector listLigne = (Vector) listeCases.get(ligne);
  			
  			for (int i=carrefours[0].getColone()+1; i<carrefours[1].getColone(); i++) {
  				((SimuTraficBloc) listLigne.get(i)).setEtatBlank();
  			}
 	
 		}
 	}

  	
  	
  	private SimuTraficBloc[] trouverCarrefourProche(SimuTraficBloc source) {
  		int ligne = source.getLigne();
  		int colone = source.getColone();
  		
  		SimuTraficBloc prochesLigne[] = new SimuTraficBloc[2];
  		SimuTraficBloc prochesColone[] = new SimuTraficBloc[2];
  		
  		// on explore la ligne
  		Vector ligneVecteur = (Vector) listeCases.get(ligne);
  		
  		for(int i=1;(colone+i<ligneVecteur.size() || colone-i >= 0);i++) {
  			
  			SimuTraficBloc temp = null;
  			
  			if(colone+i<ligneVecteur.size()) {
  				temp = ((SimuTraficBloc) ligneVecteur.get(colone+i));
  				if (Parametres.debug) 
  					System.out.println("Examen case: " + temp.getLigne() + " " + temp.getColone());
  				if (temp.getEtat() == "carrefour" && prochesLigne[1] == null) {
  					prochesLigne[1] = temp;
  					if (Parametres.debug) 
  	  					System.out.println("Carrefour Trouv?: " + temp.getLigne() + " " + temp.getColone());
  				}
  			}
  			
  			if(colone-i>=0) {
  				temp = ((SimuTraficBloc) ligneVecteur.get(colone-i));
  				if (Parametres.debug) 
  					System.out.println("Examen case: " + temp.getLigne() + " " + temp.getColone());
  				if (temp.getEtat() =="carrefour" && prochesLigne[0] == null) {
  					prochesLigne[0] = temp;
  					if (Parametres.debug) 
  	  					System.out.println("Carrefour Trouv?: " + temp.getLigne() + " " + temp.getColone());
  				}
  			}
  			
  		}
  		// On explore la colone
  		for(int i=1;(ligne+i<listeCases.size() || ligne-i >= 0);i++) {
  			SimuTraficBloc temp = null;
  			
  			if(ligne+i<listeCases.size()) {
  				temp = ((SimuTraficBloc) ((Vector) listeCases.get(ligne+i)).get(colone));
  				if (Parametres.debug) 
  					System.out.println("Examen case: " + temp.getLigne() + " " + temp.getColone());
  				if (temp.getEtat() == "carrefour" && prochesColone[1] == null) {
  					prochesColone[1] = temp;
  					if (Parametres.debug) 
  	  					System.out.println("Carrefour Trouv?: " + temp.getLigne() + " " + temp.getColone());
  				}
  			}
  			
  			if(ligne-i>=0) {
  				temp = ((SimuTraficBloc) ((Vector) listeCases.get(ligne-i)).get(colone));
  				if (Parametres.debug) 
  					System.out.println("Examen case: " + temp.getLigne() + " " + temp.getColone());
  				if (temp.getEtat() =="carrefour" && prochesColone[0] == null) {
  					prochesColone[0] = temp;
  					if (Parametres.debug) 
  	  					System.out.println("Carrefour Trouv?: " + temp.getLigne() + " " + temp.getColone());
  				}
  			}
  		}
  		
  		// On a pas trouv? de carrefours proches
  		if((prochesLigne[0] == null || prochesLigne[1] == null) && (prochesColone[0] == null || prochesColone[1] == null))  {  			
  			if(Parametres.debug)
  				System.out.println("On a pas trouv? de carrefours pour la route");
  			return  new SimuTraficBloc[2];
  		}
  		
  		// On a trouv? des carrefours que sur la Colone
  		if(prochesLigne[0] == null || prochesLigne[1] == null) {
  			return prochesColone;
  		} 
  		// On a trouv? des carrefours que sur la Ligne
  		else if(prochesColone[0] == null || prochesColone[1] == null){
  			return prochesLigne;
  		}
  		
  		// On a trouv? des carrefours sur la ligne et sur la colone

  		SimuTraficBloc[] plusProches = {prochesLigne[0], prochesLigne[1], prochesColone[0], prochesColone[1]};
  		return plusProches;
			
  	}
  	

  }  
}
/*
class LabelRunner extends Thread {
	
	JLabel voiture = null;
	boolean H = false;
	
	public LabelRunner(JLabel voiture, boolean H) {
		this.voiture=voiture;
		this.H = H;
	}
	
	public void run() {
	
		while(true) {
			try {
				sleep(100);
				
			} catch (Exception e) {
				
			}
			if(H)
			voiture.setLocation((int) voiture.getLocation().getX(), (int) voiture.getLocation().getY() + 5);
			else 
				voiture.setLocation((int) voiture.getLocation().getX()+5, (int) voiture.getLocation().getY());
			System.out.println("Position Voiture: " + voiture.getLocation());
		
		}
	}
	

}*/