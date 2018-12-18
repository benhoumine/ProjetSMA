package ProtocoleDeCommunication;

import Gestionnaire.GestMsgRoute;
import Gestionnaire.GestMsgVoiture;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;

/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
* 
* 
*/

public class ContratNetVehicule extends ContractNetResponder{
    GestMsgVoiture gestMsgVoiture=null;
    GestMsgRoute gestMsgRoute=null;
    public ContratNetVehicule(Agent a, MessageTemplate mt) {
        super(a, mt);
    }
     // Réception d’un message CFP
                protected ACLMessage handleCfp(ACLMessage cfp)throws RefuseException, FailureException, NotUnderstoodException {
                    String msg = cfp.getContent();
                    if (msg!=null) {
                        gestMsgVoiture.recupMsg(Integer.parseInt(msg));
                        ACLMessage propose = cfp.createReply();
                        propose.setPerformative(ACLMessage.PROPOSE);
                        propose.setContent(msg);
                        return propose;
                    } else {
                        throw new RefuseException("Message non trouver");
                    }
                }
     //Réception d’un message Accept-Proposal

                protected ACLMessage handleAcceptProposal(ACLMessage cfp,ACLMessage propose, ACLMessage accept) throws FailureException {
                    String msg = cfp.getContent();
                    if (msg!=null) {
                        ACLMessage inform = accept.createReply();
                        inform.setPerformative(ACLMessage.INFORM);
                        return inform;
                    } else {
                        throw new FailureException("voiture recherche message");
                    }
                }
    
}
