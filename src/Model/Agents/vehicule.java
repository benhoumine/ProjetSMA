package Model.Agents;
//terminé
import ProtocoleDeCommunication.ContratNetVehicule;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Gestionnaire.Message;

/**
 *
 * @author BENHOUMINE Abdelkhalek & BANE Mamadou
 * 
 */
public class vehicule extends Agent {
	private Carrefour carrefour;
	private Logger logger = LoggerFactory.getLogger(Carrefour.class);

	@Override
	/***
	 * 
	 * Avant démarrer l'agent Vehicule 
	 */
	protected void setup() {
		super.setup();
		Object args[] = this.getArguments();
		if (args == null || args.length < 2) {
			doDelete();
		} else {
			addBehaviour(new CyclicBehaviour() {
				public void action() {
					if (carrefour != null) {
						ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
						cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
						cfp.setReplyByDate(new Date(System.currentTimeMillis() + 2000));
						cfp.addReceiver(new AID("carrefour" + AID.ISLOCALNAME));
						Message mgs=new Message(0, myAgent);
						cfp.setContent("-Voiture : Je Suis prete ");
						addBehaviour(new ContratNetVehicule(myAgent, null));
					} else {
						doDelete();
					}}});}}

	@Override
	protected void takeDown() {
		System.out.println("c est la fin.");
	}
}
