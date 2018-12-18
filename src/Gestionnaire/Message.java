/*****************************************************************************
 Cette classe permet de creer un message identifie par "msgi" comportant un
 objet choisi par l'utilisateur. Cela permet de donner des informations sur
 l'etat d'un vehicule, d'une route ou bien d'un carrefour.  chaque message
 a une signification respectant le cahier des charges mis en place.

 ------------------------------------------------------MSG ENVOYE PAR ROUTE----
 msg0  : avancer
 msg1  : tu approches d'une voiture
 msg2  : une voiture s'approche de toi
 msg3  : tu approches d'une voiture et une voiture s'approche de toi
 msg4  : tu peux entrer sur la route
 msg5  : boom
 msg6  : arret obligatoire
 msg7  : tu approches d'un carrefour
 msg8  : ...Fin de route...
 msg9  : donner voiture?
 msg10 : faire entrer une voiture
 msg11 : refuser voiture
 msg12 : envoyer mesures
 ----------------------------------------------------MSG ENVOYE PAR CARREFOUR--
 msg13 : refus de voiture
 msg14 : accepter voiture
 msg15 : demander de faire rentrer une voiture sur la route
 msg16 : voie possible
 msg17 : quelle voie
 msg18 : arret obligatoire
 msg19 : envoyer mesure
 ----------------------------------------------------MSG ENVOYE PAR VOITURE----
 msg20 : entrer sur un carrefour
 msg21 :
 msg22 : changer de files
 msg23 : je m'arrete
 msg24 : j'avance
 msg25 : je suis mort
 msg26 : indiquer ma position
 msg27 : avance-toi
 msg28 : arrete-toi
 msg29 : random choix voies
--------------------------------------------------MSG ENVOYE PAR L'UTILISATEUR--
 msg30 : generer une nouvelle voiture en debut d'une route
 
 *********************************************************************************/

package Gestionnaire;
import java.util.Vector;

import GuiSimuTrafic.Parametres;

/**
 * <p>Titre: Message</p>
 * <p>Description: sert pour le dialogue entre les acteurs du reseau routier</p>
 * @author Naji kawtar & Soukayna etalbi
 * @version 1.0
 */

public class Message {

    private Vector msgCree = new Vector(2) ;
    private int nbMaxMsg = 31;

    private String msg[] = {"msg0", "msg1", "msg2", "msg3", "msg4", "msg5",
                            "msg6", "msg7", "msg8", "msg9", "msg10",
                            "msg11", "msg12", "msg13", "msg14", "msg15",
                            "msg16", "msg17", "msg18", "msg19", "msg20",
                            "msg21", "msg22", "msg23", "msg24", "msg25",
                            "msg26", "msg27", "msg28", "msg29", "msg30"} ;

    public Message(int numMessage, Object monObjet) {

        if (numMessage >= 0 && numMessage < nbMaxMsg){
            msgCree.add(0, msg[numMessage]) ;
        } else {
        	if(Parametres.debug)
        		System.out.println("message indefini: on avance par defaut.");
            msgCree.add(0,msg[0]) ;
        }
        msgCree.add(1, monObjet);

    }

    /**
     * recuperer les informations d'un message
     * @return Vector
     */
    public synchronized Vector getMessage(){
        return msgCree ;
    }

}
