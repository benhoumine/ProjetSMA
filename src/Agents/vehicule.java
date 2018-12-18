package Agents;

import AgentsRoutiers.Voiture;
import Gestionnaire.Message;
import ProtocoleDeCommunication.ContratNetRoute;
import ProtocoleDeCommunication.ContratNetVehicule;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import java.util.Date;

/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
*/
public class vehicule extends Agent{
    private Carrefour carrefour;
    @Override
    protected void setup() {
        super.setup();
         Object args[] = this.getArguments();
        if (args == null || args.length < 2) {
            System.out.println("Usage : <titre> <seuil>");
            doDelete(); //appelle takeDown()
        } else {
          
            System.out.println("Voiture (" + this.getAID()+ ") créé");
            
            addBehaviour(new CyclicBehaviour() {
                public void action() {
                    if (carrefour!=null) {
                        ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                        cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
            // On veut reçevoir la réponse dans 2 secs
                        cfp.setReplyByDate(new Date(System.currentTimeMillis() + 2000));
                            cfp.addReceiver(new AID("carrefour"+ AID.ISLOCALNAME));
                        Message mgs=new Message(0, myAgent);
                        cfp.setContent("Je rentre sur la route");
                        addBehaviour(new ContratNetVehicule(myAgent, null));
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
