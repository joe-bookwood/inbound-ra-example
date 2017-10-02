package de.bitc.ejb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;

import de.bitc.jca.inflow.TcpMessageListener;

/**
 * Message-Driven Bean implementation class for: MessageListenerBean
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "topic", propertyValue = "test") },
            messageListenerInterface = TcpMessageListener.class)
public class MessageListenerBean implements TcpMessageListener {

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
