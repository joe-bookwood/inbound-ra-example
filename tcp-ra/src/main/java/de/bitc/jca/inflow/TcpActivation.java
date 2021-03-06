/*
 * IronJacamar, a Java EE Connector Architecture implementation
 * Copyright 2013, Red Hat Inc, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package de.bitc.jca.inflow;

import java.lang.reflect.Method;

import javax.resource.ResourceException;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.endpoint.MessageEndpointFactory;

import de.bitc.jca.TcpResourceAdapter;

/**
 * TcpActivation
 *
 * @version $Revision: $
 */
public class TcpActivation {

    /** The resource adapter */
    private TcpResourceAdapter ra;

    /** Activation spec */
    private TcpActivationSpec spec;

    /** The message endpoint factory */
    private MessageEndpointFactory endpointFactory;

    /** The MailListener.onMessage method */
    public static final Method ON_MESSAGE;

    static
    {
       try
       {
          Class[] sig = {String.class};
          ON_MESSAGE = TcpMessageListener.class.getMethod("onMessage", sig);
       }
       catch (Exception e)
       {
          throw new RuntimeException(e);
       }
 }

    /**
     * Default constructor
     *
     * @exception ResourceException
     *                Thrown if an error occurs
     */
    public TcpActivation() throws ResourceException {
        this(null, null, null);
    }

    /**
     * Constructor
     *
     * @param ra
     *            TcpResourceAdapter
     * @param endpointFactory
     *            MessageEndpointFactory
     * @param spec
     *            TcpActivationSpec
     * @exception ResourceException
     *                Thrown if an error occurs
     */
    public TcpActivation(TcpResourceAdapter ra, MessageEndpointFactory endpointFactory, TcpActivationSpec spec)
            throws ResourceException

    {
        this.ra = ra;
        this.endpointFactory = endpointFactory;
        this.spec = spec;
    }

    /**
     * Get activation spec class
     *
     * @return Activation spec
     */
    public TcpActivationSpec getActivationSpec() {
        return spec;
    }

    /**
     * Get message endpoint factory
     *
     * @return Message endpoint factory
     */
    public MessageEndpointFactory getMessageEndpointFactory() {
        return endpointFactory;
    }

    /**
     * Start the activation
     *
     * @throws ResourceException
     *             Thrown if an error occurs
     */
    public void start() throws ResourceException {

    }

    /**
     * Stop the activation
     */
    public void stop() {

    }


    public void sendMessage(String message) {

        MessageEndpoint endpoint =null;

        try {
            endpoint = endpointFactory.createEndpoint(null);
            if(endpoint != null && endpoint instanceof TcpMessageListener) {
                TcpMessageListener tcpMessageListener = (TcpMessageListener) endpoint;

                tcpMessageListener.onMessage(message);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if(endpoint != null) {
                endpoint.release();
            }
        }
    }

}
