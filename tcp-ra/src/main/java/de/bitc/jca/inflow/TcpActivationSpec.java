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

import java.util.logging.Logger;

import javax.resource.spi.Activation;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.ConfigProperty;
import javax.resource.spi.InvalidPropertyException;
import javax.resource.spi.ResourceAdapter;

/**
 * TcpActivationSpec
 *
 * @version $Revision: $
 */
@Activation(messageListeners = { de.bitc.jca.inflow.TcpMessageListener.class })
public class TcpActivationSpec implements ActivationSpec
{

   /** The logger */
   private static Logger log = Logger.getLogger(TcpActivationSpec.class.getName());

   /** The resource adapter */
   private ResourceAdapter ra;

   /** port */
   @ConfigProperty(defaultValue = "12000")
   private String port;

   /**
    * Default constructor
    */
   public TcpActivationSpec()
   {

   }

   /** 
    * Set port
    * @param port The value
    */
   public void setPort(String port)
   {
      this.port = port;
   }

   /** 
    * Get port
    * @return The value
    */
   public String getPort()
   {
      return port;
   }

   /**
    * This method may be called by a deployment tool to validate the overall
    * activation configuration information provided by the endpoint deployer.
    *
    * @throws InvalidPropertyException indicates invalid configuration property settings.
    */
   public void validate() throws InvalidPropertyException
   {
      log.finest("validate()");

   }

   /**
    * Get the resource adapter
    *
    * @return The handle
    */
   public ResourceAdapter getResourceAdapter()
   {
      log.finest("getResourceAdapter()");
      return ra;
   }

   /**
    * Set the resource adapter
    *
    * @param ra The handle
    */
   public void setResourceAdapter(ResourceAdapter ra)
   {
      log.finest("setResourceAdapter()");
      this.ra = ra;
   }


}
