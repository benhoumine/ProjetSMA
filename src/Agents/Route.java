package Agents;

import AgentsRoutiers.Voiture;
import Gestionnaire.Message;
import ProtocoleDeCommunication.ContratNetCarrefour;
import ProtocoleDeCommunication.ContratNetRoute;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import java.util.Date;

/**
 *
 * @author Naji kawtar & Soukayna etalbi
 */
public class Route extends Agent{
    private Voiture[] voiture;
    private Carrefour carrefour;
    @Override
    protected void setup() {
        super.setup();
         Object args[] = this.getArguments();
        if (args == null || args.length < 2) {
            System.out.println("Usage : <titre> <seuil>");
            doDelete(); //appelle takeDown()
        } else {
          
            System.out.println("route (" + this.getAID()+ ") créé");
            
            addBehaviour(new CyclicBehaviour() {
                public void action() {
                    if (voiture!=null) {
                        ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                        cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
                // On veut reçevoir la réponse dans 2 secs
                        cfp.setReplyByDate(new Date(System.currentTimeMillis() + 2000));
                        for (int i = 0; i < args.length; i++) {
                            cfp.addReceiver(new AID("Vehicule" + i, AID.ISLOCALNAME));
                        }
                        Message mgs=new Message(0, myAgent);
                        cfp.setContent("Je rentre sur la route");
                        addBehaviour(new ContratNetRoute(myAgent, null));
                    } else {
                        doDelete();
                    }//else
                }//action
            });//addBehaviour
        }//else
    }//setup

    @Override
    protected void takeDown() {
        System.out.println("c est la fin.");
    }
}
