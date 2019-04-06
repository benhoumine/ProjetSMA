package GUI;

import javax.swing.*;

/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
* 
* 
*/
public class JLabelOutil extends JLabel{

  private ImageIcon imageRoute;
  private int largeurImage = 195 ;
  private int hauteurImage = 55 ;

  public JLabelOutil(String repFichier) {
    this.imageRoute = new ImageIcon(this.getClass().getResource(repFichier));
    this.setSize(imageRoute.getIconWidth(), imageRoute.getIconHeight());
    this.setIcon(imageRoute);
  }

  public JLabelOutil(String repFichier, int largeur, int hauteur) {
    this.imageRoute = new ImageIcon(this.getClass().getResource(repFichier));
    this.setSize(largeur, hauteur);
    this.setIcon(imageRoute);
  }

  public int[] getDimension(){
    int[] lesDimensions = {largeurImage, hauteurImage} ;
    return lesDimensions;
  }

}
