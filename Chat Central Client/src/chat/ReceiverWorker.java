package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import static message.MessageTypes.NOTE;
import static message.MessageTypes.SHUTDOWN;


/*
 *A thread processing a connection with the chat server.
 *
 * @author wolfdieterotte
 */
public class ReceiverWorker extends Thread
{
    Socket serverConnection = null;
    
    ObjectInputStream readFromNet = null;
    ObjectOutputStream writeToNet = null;
    
    Message message = null;
    
    /*
     *Constructor
     *
     * @param serverConnection
     */
    public ReceiverWorker(Socket serverConnection)
    {
        this.serverConnection = serverConnection;
        
        // open object streams
        try
        {
            readFromNet = new ObjectInputStream(serverConnection.getInputStream());
            writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());
        }
        catch(IOException ex)
        {
            //Dr.Otte told us to remove this
            //Logger.getLogger(ReceiverWorker.class.getName()).log(Level.SEVERE, "[ReceiverWorker.run] Could not open object streams", ex);
        }
        
    }
    
    // thread code entry point
    @Override
    public void run()
    {
        try
        {
            //read message
            message = (Message)readFromNet.readObject();

        }
        catch (IOException | ClassNotFoundException ex)
        {
            //Dr.Otte told us to remove this
            //Logger.getLogger(ReceiverWorker.class.getName()).log(Level.SEVERE, "[ReceiverWorker.run] Message could not be read", ex);
            
            // no use getting going
            //System.exit(1);
        }
        
        //decide what to do depending on the type of message received
        switch (message.getType())
        {
            //----------------------------------------------------------------
            case SHUTDOWN:
            //----------------------------------------------------------------
            
              System.out.println("Receiver shutdown message from server, exiting");
              
              try
              {
                  serverConnection.close();
              }
              catch (IOException ex)
              {
                  // we don't care, going to exit anyway
              }
              
              System.exit(0);
              
              break;
            
            //----------------------------------------------------------------
            case NOTE:
            //----------------------------------------------------------------
            
              // just display note received
              System.out.println((String) message.getContent());
              
              try
              {
                  serverConnection.close();
              }
              catch(IOException ex)
              {
                //Dr.Otte told us to remove this
                //Logger.getLogger(ReceiverWorker.class.getName()).log(Level.SEVERE, "[ReceiverWorker.run] could not work.");  
              }
              
              break;
              
            default:
              // cannot occur
        }
    } 
}
 
