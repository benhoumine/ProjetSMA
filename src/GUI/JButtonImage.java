package GUI;

import javax.swing.* ;

/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
* 
* 
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
