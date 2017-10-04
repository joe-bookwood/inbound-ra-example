package de.bitc.ejb;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;

import org.jboss.ejb3.annotation.ResourceAdapter;

import de.bitc.jca.inflow.TcpMessageListener;

/**
 * Message-Driven Bean implementation class for: MessageListenerBean
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "topic", propertyValue = "test") },
            messageListenerInterface = TcpMessageListener.class)
@ResourceAdapter("tcp-eis-1.0.0-SNAPSHOT.rar")
public class MessageListenerBean implements TcpMessageListener {

    /** The logger */
    private static Logger log = Logger.getLogger(MessageListenerBean.class.getName());

    /**
     * Default constructor.
     */
    public MessageListenerBean() {
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
