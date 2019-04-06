package ConteneurMain;
import jade.wrapper.AgentContainer;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.ControllerException;
//terminé

/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
* Cette Classe est pour lancer le plateforme JADE même l'interface graphique
* 
*/

public class MainConteneur {
	/**
	 * 
	 * La 1er fonction main qui se charge pour lancer le plateforme JADE de l'application 
	 * 
	 * @param args
	 */
    
    public static void main(String[] args) {
        try {
        	
            Runtime rt=Runtime.instance();
            Properties p=new ExtendedProperties();
            //Lancer l'interface graphique
            p.setProperty("gui","true");
            //définir les profiles avec un ensemble de proprietés
            ProfileImpl impl=new ProfileImpl(p);
            AgentContainer container=rt.createMainContainer(impl);
            //Démarer le plateforme JADE
            container.start();
        } catch (ControllerException ex) {
        	// au cas ou il y'a une exception
        		ex.printStackTrace();
        }
        
    }
}


/**
*
*@author BENHOUMINE Abdelkhalek & Bane Mamadou
*
*/
