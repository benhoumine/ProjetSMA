package ConteneurMain;
import jade.wrapper.AgentContainer;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.ControllerException;
/**
 *
 * @author Naji kawtar & Soukayna etalbi
 */
public class MainConteneur {
    
    public static void main(String[] args) {
        try {
            Runtime rt=Runtime.instance();
            Properties p=new ExtendedProperties();
            /*Creer  interface graphique du Jade*/
            // p.setProperty("gui","true");
            /*Creer   profile qui contien les proprietes du conteur principal du Jade*/
            ProfileImpl impl=new ProfileImpl(p);
            /*Creer  conteneur principale du Jade*/
            AgentContainer container=rt.createMainContainer(impl);
            /*demarrage du conteneur principale du Jade*/
            container.start();
        } catch (ControllerException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
}
