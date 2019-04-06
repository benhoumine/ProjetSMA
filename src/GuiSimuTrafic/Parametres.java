package GuiSimuTrafic;

import java.awt.Color;
import java.awt.Font;



/**
 * 
 * 
 * 
 * @author abdelkhalek benhoumine
 *
 */

/**
 * 
 * Cette classe est utilisé pour la déclaration des parametre global pour l'application 
 * comme .env dans les autres langage 
 */
public class Parametres {

	/**
	 * pour activer le debug 
	 */
  public static final boolean debug = true;
  public static String outilParDefaut = "select" ;
  public static String isUsed = outilParDefaut ;
  
  /**
   * 
   * choisir le type de la voiture 
   * 0 : voiture normal 
   * 1 : voiture du course
   * 
   */
  public static int TypeVoitureParDefaut = 0 ; 
  public static Color couleurFond = new Color( 255, 255, 255) ;
  public static Font police = new Font("Serif", Font.PLAIN, 14);
  public static String imagepath = "../images/";

}
