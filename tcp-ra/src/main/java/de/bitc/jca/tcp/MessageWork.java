/**
 *
 */
package de.bitc.jca.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.resource.spi.work.Work;

import de.bitc.jca.TcpResourceAdapter;
import de.bitc.jca.inflow.TcpActivation;
import de.bitc.jca.inflow.TcpActivationSpec;

/**
 * @author jobuchholz
 *
 */
public class MessageWork implements Work {

    /** The logger */
    private static Logger log = Logger.getLogger(MessageWork.class.getName());

    private TcpResourceAdapter resourceAdapter;
    private Socket socket;

    public MessageWork(Socket socket, TcpResourceAdapter resourceAdapter) {
        this.resourceAdapter = resourceAdapter;
        this.socket = socket;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = this.socket.getInputStream();
            outputStream = this.socket.getOutputStream();

            StringBuilder stringBuilder = new StringBuilder();
            int inputChar = 13;
            boolean exit = false;
            while (!exit && (inputChar = inputStream.read()) != -1) {
                stringBuilder.append((char) inputChar);
                if (inputChar == 13) {
                    String message = stringBuilder.toString().trim();

                    // quit means bye bye
                    if ("quit".equals(message)) {
                        exit = true;
                    } else {
                        for (Entry<TcpActivationSpec, TcpActivation> entrySet : resourceAdapter.getActivations().entrySet()) {
                            entrySet.getValue().sendMessage(message);
                        }

                        outputStream.write(("broadcasting to mdb's " + message + "\n").getBytes());
                        outputStream.flush();

                        // erease stringbuilder for the next message
                        stringBuilder.setLength(0);
                    }
                }
            }
        } catch (IOException e) {
            log.severe("IO error: " + e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.severe("Can't close outputstream " + e.getMessage());
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.severe("Can't close inputstream " + e.getMessage());
                }
            }
            release();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.resource.spi.work.Work#release()
     */
    @Override
    public void release() {
        if (this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException e) {
                log.severe("Can't close socket " + e.getMessage());
            }
        }
    }

}
