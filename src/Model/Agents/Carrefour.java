package Model.Agents;
//terminé
import AgentsRoutiers.Voiture;
import Gestionnaire.Message;
import ProtocoleDeCommunication.ContratNetCarrefour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author BENHOUMINE Abdelkhalek & BANE Mamadou
 * Description  : Agent Carrefour , il nous permet de suivre le content et les message envoye par l'interface et donne des (ordres)
 */
public class Carrefour extends Agent{
	
	private Logger logger = LoggerFactory.getLogger(Carrefour.class);
	
    private Voiture[] vehicule_voiture;
    @Override
   /** 
    * Cette method existe par default ,elle s'exécute au moment de la création de l'agent 
    */
    protected void setup() {
        super.setup();
         Object args[] = this.getArguments();
        if (args == null || args.length < 2) {
            doDelete();
            logger.error("Agent carrefour error");
        } else {
            logger.debug("Agent Carrefour  ---> " +this.getLocalName() + " a été créé ");
            
            addBehaviour(new CyclicBehaviour() {
                public void action() {
                    if (vehicule_voiture!=null) {
                        ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                        cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
                        cfp.setReplyByDate(new Date(System.currentTimeMillis() + 2000));
                        for (int i = 0; i < args.length; i++) {
                            cfp.addReceiver(new AID("route" + i, AID.ISLOCALNAME));
                        }
                        Message mgs=new Message(0, myAgent);
                        cfp.setContent("_Voiture : Je suis prete >>> Carrefour");
                        addBehaviour(new ContratNetCarrefour(myAgent, null));
                    } else {
                        doDelete();
                    }}
            });}}

    @Override
    /**
     * 
     * takeDown : pour indiquer la terminaison d'éxecution de l'agent Carrefour
     */
    protected void takeDown() {
       logger.info("Agent carrefour terminé");
    }

}

