
package Gestionnaire;
import java.util.Vector;

import GuiSimuTrafic.Parametres;
/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
* 
* 
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
