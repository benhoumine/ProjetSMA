package Model.Agents;
//terminé

import AgentsRoutiers.Voiture;
import Gestionnaire.Message;
import ProtocoleDeCommunication.ContratNetRoute;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author abdelkhalek benhoumine & bane mamadou
 * 
 */
public class Route extends Agent{
	private Logger logger = LoggerFactory.getLogger(Carrefour.class);
    private Voiture[] voitures;
    @Override
    protected void setup() {
        super.setup();
         Object args[] = this.getArguments();
        if (args == null || args.length < 2) {
            doDelete();
        } else {
            logger.debug("route (" + this.getAID()+ ") créé");
            addBehaviour(new CyclicBehaviour() {
                public void action() {
                    if (voitures!=null) {
                        ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                        cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
                        cfp.setReplyByDate(new Date(System.currentTimeMillis() + 2000));
                        for (int i = 0; i < args.length; i++) {
                            cfp.addReceiver(new AID("Vehicule" + i, AID.ISLOCALNAME));
                        }
                        Message mgs=new Message(0, myAgent);
                        cfp.setContent("Voiture : Je suis prete >>> Route");
                        addBehaviour(new ContratNetRoute(myAgent, null));
                    } else {
                    	logger.error("Erooor Voiture null");
                        doDelete();
                    }}});}}
    @Override
    protected void takeDown() {
       logger.info("c est la fin.");
    }
}
