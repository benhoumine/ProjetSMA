package GUI;

import javax.swing.* ;

/**
 *
 * <p>Title: JButtonImage</p>
 * <p>Description: bouton "?tendu", opaque, transparent ? bords transparent
 *  --> affiche une image </p>
 * @author Naji kawtar & Soukayna etalbi
 * @version 1.0
 */
public class JButtonImage extends JButton{


  /**
   * JButtonImage
   * @param image String
   */
  public JButtonImage(String image) {
    ImageIcon imagebtn=new ImageIcon(this.getClass().getResource(image));
    this.setIcon(imagebtn);
    this.setOpaque(true);
    this.setBorderPainted(false) ;
    this.setContentAreaFilled(false);
    this.setSize(this.getIcon().getIconWidth(),this.getIcon().getIconHeight());

  }

}
