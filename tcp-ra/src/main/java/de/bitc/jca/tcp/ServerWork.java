/**
 *
 */
package de.bitc.jca.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.WorkManager;

import de.bitc.jca.TcpResourceAdapter;

/**
 * @author jobuchholz
 *
 */
public class ServerWork implements Work {

    /** The logger */
    private static Logger log = Logger.getLogger(ServerWork.class.getName());


    private WorkManager workManager;
    private TcpResourceAdapter resourceAdapter;
    private ServerSocket service;


    private boolean released;

    public ServerWork(TcpResourceAdapter tcpResourceAdapter) {
        this.workManager = tcpResourceAdapter.getBootstrapContext().getWorkManager();
        this.resourceAdapter = tcpResourceAdapter;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            this.service = new ServerSocket(resourceAdapter.getPort());
            released = false;
        } catch (IOException e) {
            log.severe("Cant start service: " + e.getMessage());
        }
        while (!released && !service.isClosed()) {
            try {
                final Socket socket = service.accept();
                if(!released && !service.isClosed() && socket!=null && socket.isConnected()) {
                    // don't process the message here, schedule it.
                    Work messageProcessor = new MessageWork(socket,resourceAdapter);
                    workManager.scheduleWork(messageProcessor);
                }
            } catch (IOException e) {
                log.severe("Cant create socket: " + e.getMessage());
            } catch (WorkException e) {
                log.severe("Can't schedule work: " + e.getMessage());
            }
        }
    }

    /* (non-Javadoc)
     * @see javax.resource.spi.work.Work#release()
     */
    @Override
    public void release() {
        if(this.service != null) {
            try {
                service.close();
            } catch (IOException e) {
                log.severe("Cant close service: " + e.getMessage());
            }
        }
        this.released = true;
    }

}
