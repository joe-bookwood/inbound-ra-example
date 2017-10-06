package de.bitc.ejb;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;

import de.bitc.jca.inflow.TcpMessageListener;

/**
 * Message-Driven Bean implementation class for: MessageListenerBean
 */
@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty(propertyName = "topic", propertyValue = "test")
        } //, messageListenerInterface = TcpMessageListener.class
        )
public class InboundEventHandler implements TcpMessageListener {

    /** The logger */
    private static Logger log = Logger.getLogger(InboundEventHandler.class.getName());

    /**
     * Default constructor.
     */
    public InboundEventHandler() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see TcpMessageListener#onMessage(String)
     */
    @Override
    public void onMessage(String msg) {
        // TODO Auto-generated method stub
    }

}
