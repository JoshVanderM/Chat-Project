package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import java.util.Iterator;

import java.util.logging.Level;
import java.util.logging.Logger;

import message.Message;
import message.MessageTypes;

public class ChatServerWorker extends Thread implements MessageTypes
{
    Socket chatConnection = null;
    
    ObjectOutputStream writeToNet = null;
    ObjectInputStream readFromNet = null;
    
    Message message = null;
    
    public ChatServerWorker(Socket chatConnection)
    {
        this.chatConnection = chatConnection;
    }
    
    @Override
    public void run()
    {
        NodeInfo participantInfo = null;
        Iterator <NodeInfo> participantsIterator;
        
        try
        {
            // get object streams
            writeToNet = new ObjectOutputStream(chatConnection.getOutputStream());
            readFromnet = new ObjectInputStream(chatConnection.getInputStream());
            
            //read message
            message = (Message) readFromNet.readObject();
            
            chatConnection.close();  
        }
        catch(IOException | ClassNotFoundException e)
        {
          System.out.println("[ChatServerWorker.run] Failed to open object streams or message could not be read.");  
          
          System.exit(1);
        }
        
        // processing message
        switch (message.getType())
        {
            //----------------------------------------------------------------
            case JOIN:
            //----------------------------------------------------------------
       
              // read paticipant's NodeInfo
              NodeInfo joiningParticipantNodeInfo = (NodeInfo) message.getContent();

              // add this client to list of participants
              ChatServer.participants.add(joiningParticipantNodeInfo);

              // show who joined
              System.out.print(joiningParticipantNodeInfo.getName() + " joined. All current Participants: ");
              
              // print out all current participants
              participantsIterator = ChatServer.participants.iterator();
              while(participantsIterator.hasNext()) 
              {
                participantInfo = participantsIterator.next();
                System.out.print(participantInfo.name + ".");               
              } 
              System.out.println();

              break;

            //----------------------------------------------------------------
            case LEAVE:
            case SHUTDOWN:
            //----------------------------------------------------------------
            
              // remove this participant's Info
              NodeInfo leavingParticipantInfo = (NodeInfo) message.getContent();
              if (ChatServer.participants.remove(leavingParticipantInfo))
              {
                  System.err.println(leavingParticipantInfo.getName() + " remove");
              }
              else
              {
                  System.err.println(leavingParticipantInfo.getName() + " not found");
              }
              
              // show who left
              System.out.print(leavingParticipantInfo.getName() + " left. Remaining participants: ");
              
              //print out all remaining participants
              participantsIterator = ChatServer.participants.iterator();
              while(participantsIterator.hasNext())
              {
                 participantInfo = participantsIterator.next();
                 System.out.print(participantInfo.name + "");                 
                 
              }
              System.out.println();
              
              break;
              
            //----------------------------------------------------------------
            case SHUTDOWN_ALL:
            //----------------------------------------------------------------
            
              // run through all the participants and shut down every single one
              participantsIterator = ChatServer.participants.iterator();
              while(participantsIterator.hasNext())
              {
                  //get next participant
                  participantInfo = participantsIterator.next();
                  
                  try
                  {
                      // open connection to client
                      chatConnection = new Socket(participantInfo.address, participantInfo.port);
                      
                      // open object streams
                      writeToNet = new ObjectOutputStream(chatConnection.getOutputStream());
                      readFromNet = new ObjectInputStream(chatConnection.getInputStream());
                      
                      // send shutdown message
                      writeToNet.writeObject(new Message(SHUTDOWN, null));
                      
                      // close connection
                      chatConnection.close();
                  }
                  catch (IOException ex)
                  {
                    Logger.getLogger(ChatServerWorker.class.getName()).log(Level.SEVERE, "[ChatServerWorker].run could not open socket to client
                        or send a message", ex);  
                  }  
              }
            
              System.out.println("Shut down all clients, exiting...");
              
              // now exit myself
              System.exit(0);
              
            //----------------------------------------------------------------
            case NOTE:
            //----------------------------------------------------------------
            
              // just display note
              System.out.println((String) message.getContent());
              
              // run through all participants and send the note to every single one
              participantsIterator = ChatServer.participants.iterator();
              while(participantsIterator.hasNext())
              {
                // get next participant
                participantInfo = participantsIterator.next();

                try
                {
                   //open socket to one chat at a time
                   chatConnection = new Socket(participantInfo.address, participantInfo.port);
                   
                   // open object streams
                   writeToNet = new ObjectOutputStream(chatConnection.getOutputStream());
                   readFromNet = new ObjectInputStream(chatConnection.getInputStream());

                   // write message
                   writeToNet.writeObject(message);

                   // close connection to this client
                   chatConnection.close();                      
                }
                catch(IOException ex)
                {
                   Logger.getLogger(ChatServerWorker.class.getName()).log(Level.SEVERE, "[ChatServerWorker].run could not open socket to client
                       or send a message", ex); 
                }                 
              }
              
              break;
              
            default:
               // cannot occur
        }       
    } 
}
