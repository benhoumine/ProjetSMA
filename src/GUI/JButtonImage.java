package GUI;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author BENHOUMINE Abdelkhalek & BANE Mamadou
 * 	<p> Le but de cette classe est de
 *         facilité la création d'une button et même l'ajout d'une image.</p>
 * 
 */
public class JButtonImage extends JButton {

	public JButtonImage(String image) {
		ImageIcon imagebtn = new ImageIcon(this.getClass().getResource(image));
		this.setIcon(imagebtn);
		this.setOpaque(true);
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);
		this.setSize(this.getIcon().getIconWidth(), this.getIcon().getIconHeight());

	}

}
