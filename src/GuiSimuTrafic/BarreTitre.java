package GuiSimuTrafic;
import Village.*;
import javax.swing.*;
//import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 *
 * <p>Title: BarreTitre</p>
 * <p>Description: cr?ation d'une barre de titre</p>
 * @author Naji kawtar & Soukayna etalbi
 * @version 1.0
 * ETAT: FONCTIONNEL MAIS ? AMELIORER
 */

public class BarreTitre extends JPanel{

  private JLabel labelTitre ;
  private JLabel etatVillage;
  private JButton simulerCreer ;
  private ReseauRoutier reseau;

  private ElementVillage leVillage = null;
  
  /**
   * Action ex?cut? lorsque l'on clique sur le bouton "simuler/cr?er"
   */

  public ActionListener simulerListener = new ActionListener(){
    public void actionPerformed(ActionEvent e) {
      etatVillage.setText("Cr?erVillage");
      repaint();      
      leVillage = new ElementVillage(reseau);
	  leVillage.genererStructureSimulation();
	  leVillage.demarrerSimulateur();
    }
  };


  public BarreTitre(ReseauRoutier leReseau) {

  	this.reseau = leReseau;
    this.setFont(Parametres.police);
    this.setBackground(Parametres.couleurFond);

    etatVillage = new JLabel("Simuler");
    labelTitre = new JLabel("Simulateur d'un r?seau routier");
    simulerCreer = new JButton();
    simulerCreer.add(etatVillage);
    simulerCreer.addActionListener(simulerListener);
    simulerCreer.setEnabled(true);

    this.add(labelTitre);
    this.add(simulerCreer);

  }

}
