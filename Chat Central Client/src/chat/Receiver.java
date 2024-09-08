package chat;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;

import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Represents the reciever side of a chat client that receives messages
 * from the chat server
 * 
 * @author wolfdieterotte
 */

public class Receiver extends Thread
{
    static ServerSocket receiverSocket = null;
    static String userName = null;

    /**
     * Constructor
     */
    public Receiver()
    {
        try
        {
            receiverSocket = new ServerSocket(ChatClient.myNodeInfo.getPort());
            System.out.println("[Receiver.Receiver] receiver socket created, listening on port " + ChatClient.myNodeInfo.getPort());
        }
        catch(IOException ex)
        {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, "Creating receiver socket failed", ex);
        }

        System.out.println(ChatClient.myNodeInfo.getName() + " Listening on " + ChatClient.myNodeInfo.getAddress() + ":" +
            ChatClient.myNodeInfo.getPort());
    }


    // thread entry point
    @Override
    public void run()
    {
        // run server loop
        while(true)
        {
            try
            {
                (new ReceiverWorker(receiverSocket.accept())).start();
            }
            catch(IOException e)
            {
                System.out.println("[Receiver.run] Warning: Error accepting client");
            }
        }
    }
}
