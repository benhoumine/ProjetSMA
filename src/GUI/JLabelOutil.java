package GUI;

import javax.swing.*;

/**
 *
 * <p>Title: JLabelOutil </p>
 * <p>Description: etiquette "?tendue" avec une image de 195*55 </p>
 * @author Naji kawtar & Soukayna etalbi
 * @version 1.0
 */

public class JLabelOutil extends JLabel{

  private ImageIcon imageRoute;
  private int largeurImage = 195 ;
  private int hauteurImage = 55 ;

  /**
   * JLabelOutil
   * @param repFichier String
   */

  public JLabelOutil(String repFichier) {
    this.imageRoute = new ImageIcon(this.getClass().getResource(repFichier));
    this.setSize(imageRoute.getIconWidth(), imageRoute.getIconHeight());
    this.setIcon(imageRoute);
  }

  /**
   *
   * @param repFichier String
   * @param largeur int
   * @param hauteur int
   */
  public JLabelOutil(String repFichier, int largeur, int hauteur) {
    this.imageRoute = new ImageIcon(this.getClass().getResource(repFichier));
    this.setSize(largeur, hauteur);
    this.setIcon(imageRoute);
  }


  // ACCESSEURS
  /**
   * getDimension : retourne lun tableau d'Integer, respectivement la largeur et la hauteur du label
   * @return int[]
   */
  public int[] getDimension(){
    int[] lesDimensions = {largeurImage, hauteurImage} ;
    return lesDimensions;
  }

}
