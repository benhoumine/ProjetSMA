package ProtocoleDeCommunication;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import jade.proto.ContractNetResponder;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author Naji kawtar & Soukayna etalbi
 */
public class ContratNetRoute extends ContractNetInitiator{
    
    public ContratNetRoute(Agent a, ACLMessage cfp) {
        super(a, cfp);
    }
    //Réception d’un message PROPOSE

    protected void handlePropose(ACLMessage propose, Vector v) {
        System.out.println("Info (Voiture): "
                + propose.getSender().getLocalName() + " propose " + propose.getContent());
    }
//Réception d’un message REFUSE

    protected void handleRefuse(ACLMessage refuse) {
        System.out.println("Info (Carrefour): " + refuse.getSender().getName()
                + " ne trouve de message pas");
    }
//Réception d’un message FAILURE

    protected void handleFailure(ACLMessage failure) {
        if (failure.getSender().equals(myAgent.getAMS())) {
            System.out.println("Le destinataire n'existe pas");
        } else {
            System.out.println("Erreur : Action non réalisée ("
                    + failure.getSender().getName() + ")");
        }
    }
//Réception d’un message INFORM

    protected void handleInform(ACLMessage inform) {
        System.out.println("Avancer : VOITURE "
                + inform.getSender().getName());
    }
//Préparation des réponses
    protected void handleAllResponses(Vector reponses, Vector acceptes) {
        double bestPrice = Double.MAX_VALUE;
        ACLMessage accept = null;
        System.out.println("Préparation des réponses");
        for (Iterator iterator = reponses.iterator(); iterator.hasNext();) {
            ACLMessage msg = (ACLMessage) iterator.next();
            if (msg.getPerformative() == ACLMessage.PROPOSE) {
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                acceptes.addElement(reply); // Rejeter tout … sauf
                double price = Double.parseDouble(msg.getContent());
                if (price < bestPrice) { // Evaluer les propositions
                    bestPrice = price;
                    accept = reply;
                }
            }
        }
        if (accept != null) {
            accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
        }
    }//handleAllResponses
}
