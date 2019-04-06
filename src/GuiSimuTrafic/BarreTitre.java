package GuiSimuTrafic;
import Village.*;
import javax.swing.*;
//import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
* 
* 
*/

public class BarreTitre extends JPanel{

  private JLabel labelTitre ;
  private JLabel etatVillage;
  private JButton simulerCreer ;
  private ReseauRoutier reseau;

  private ElementVillage leVillage = null;
  
  /**
   * Action exécuté lorsque l'on clique sur le bouton "simuler/créer"
   */
  public ActionListener simulerListener = new ActionListener(){
    public void actionPerformed(ActionEvent e) {
      etatVillage.setText("CréerVillage");
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
    labelTitre = new JLabel("Simulateur d'un réseau routier");
    simulerCreer = new JButton();
    simulerCreer.add(etatVillage);
    simulerCreer.addActionListener(simulerListener);
    simulerCreer.setEnabled(true);

    this.add(labelTitre);
    this.add(simulerCreer);

  }

}
