package de.bitc.ejb;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;

import org.jboss.ejb3.annotation.ResourceAdapter;

import de.bitc.jca.inflow.TcpMessageListener;

/**
 * Message-Driven Bean implementation class for: MessageListenerBean
 */
@MessageDriven(name="InboundEventHandler",
        activationConfig = {
                @ActivationConfigProperty(propertyName = "topic", propertyValue = "test")
        }, messageListenerInterface = TcpMessageListener.class
        )
@ResourceAdapter(value="ra-ear.ear#tcp-eis.rar")
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
        log.fine("Message received in MDB - The Hell ends: "+msg);
    }

}
