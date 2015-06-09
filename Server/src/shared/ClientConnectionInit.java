package shared;

import java.io.Serializable;

// Needs to implement Serializable so that it can be read with an ObjectInputStream
public class ClientConnectionInit implements Serializable {

    private static final long serialVersionUID = 1L;    
    
    public int clientIncomingPort;
    public int clientNum;
    
    public ClientConnectionInit(int clientIncomingPort, int clientNum) {
        this.clientIncomingPort = clientIncomingPort;
        this.clientNum = clientNum;
    }
}
