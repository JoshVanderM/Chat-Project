package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import java.util.Scanner;

import java.util.logging.Level;
import java.util.logging.Logger;

import message.Message;
import message.MessageTypes;

/*
 * Sender is processing user input, translates user input into messages
 * and sends them to the chat server
 *
 *
 *@author wolfdieterotte
 */
public class Sender extends Thread implements MessageTypes
{
    Socket serverConnection = null;
    Scanner userInput = new Scanner(System.in);
    String inputLine = null;
    boolean hasJoined; // flag indicating is we have joined chat
    
    /*
     * Constructor
     */
     public Sender()
     {
         userInput = new Scanner(System.in);
         hasJoined = false;
     }
     
     /*
      * Implementation interface Runnable not needed for threading
      */
      @Override
      public void run()
      {
          ObjectOutputStream writeToNet;
          ObjectInputStream readFromNet;
          
          // until forever, unless the user enters SHUTDOWN or SHUTDOWN_ALL
          while(true)
          {
              // get user input
              inputLine = userInput.nextLine();
              
              //--------------------------------------------------------------
              if(inputLine.startsWith("JOIN"))
              //--------------------------------------------------------------
              {
                  //ignore, if we already joined a chat
                  if(hasJoined == true)
                  {
                      System.err.println("You have already joined a chat...");
                      continue;
                  }
                  
                  // read server information user provided with JOIN command
                  String[] connectivityInfo = inputLine.split("[ ]+");
                  
                  // if there is information, that may override the connectivity information
                  // that was provided through the properties
                  try
                  {
                      ChatClient.serverNodeInfo = new NodeInfo(connectivityInfo[1], Integer.parseInt(connectivityInfo[2]));
                  }
                  catch (ArrayIndexOutOfBoundsException ex)
                  {
                      // don't do anything, we may have defaults
                  }
                  
                  // check if we have valid server connectivity information
                  if (ChatClient.serverNodeInfo == null)
                  {
                     System.err.println("[Sender].run No server connectivity information provided!");
                     continue;                     
                  }
                  
                  // server information was provided, so send join request
                  try
                  {
                      // then connection to server
                      serverConnection = new Socket(ChatClient.serverNodeInfo.getAddress(), ChatClient.serverNodeInfo.getPort());
                      
                      // open object streams
                      readFromNet = new ObjectInputStream(serverConnection.getInputStream());
                      writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());
                      
                      // send join request
                      writeToNet.writeObject(new Message(JOIN, ChatClient.myNodeInfo));
                      
                      //close connection
                      serverConnection.close();
                  }
                  catch (IOException ex)
                  {
                      //Dr.Otte told us to remove this
                      //Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, "Error connecting to server or opening or writing/reading object streams
                      // or closing connection", ex);
                      // continue;
                  }
                  
                  // we are in!
                  hasJoined = true;
                  
                  System.out.println("Joined chat...");
              }
              
              //--------------------------------------------------------------
              else if( inputLine.startsWith("LEAVE"))
              //--------------------------------------------------------------
              {
                  if (hasJoined == false)
                  {
                      System.err.println("You have not joined a chat yet...");
                      continue;
                  }
                  
                  // send leave request
                  try
                  {
                      // open connection to server 
                      serverConnection = new Socket(ChatClient.serverNodeInfo.getAddress(), ChatClient.serverNodeInfo.getPort());
                      
                      // open Connection to server
                      readFromNet = new ObjectInputStream(serverConnection.getInputStream());
                      writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());
                      
                      // send join request
                      writeToNet.writeObject(new Message(LEAVE, ChatClient.myNodeInfo));
                      
                      // close connection
                      serverConnection.close();
                  }
                  catch (IOException ex)
                  {
                      //Dr.Otte told us to remove this
                      //Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, "Error connecting to server or opening or writing/reading object streams
                      // or closing connection", ex);
                      // continue;
                  }
                  
                  // we are out!
                  hasJoined = false;
                  
                  System.out.println("Left chat...");
              }
              
              //--------------------------------------------------------------
              else if (inputLine.startsWith("SHUTDOWN ALL"))
              //--------------------------------------------------------------
              {
                // check if we are in the chat
                if(hasJoined == false)
                {
                    System.err.println("To shut down the whole chat, you need to first join it...");
                    continue;
                } 

                // we are a participant, send out a SHUTDOWN_ALL message
                try
                {
                    // open connection to server
                    serverConnection = new Socket(ChatClient.serverNodeInfo.getAddress(), ChatClient.serverNodeInfo.getPort());
                    
                    // open object streams
                    readFromNet = new ObjectInputStream(serverConnection.getInputStream());
                    writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());
                      
                    // send shutdown all request
                    writeToNet.writeObject(new Message(SHUTDOWN_ALL, ChatClient.myNodeInfo));
                      
                    // close connection
                    serverConnection.close();
                } 
                catch (IOException ex)
                {
                  //Dr.Otte told us to remove this
                  //Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, "Error connecting to server or opening or writing/reading object streams
                  // or closing connection", ex);
                   
                }

                System.out.println("Sent shutdown all request...\n");                 
              }
              
             //---------------------------------------------------------------
             else if (inputLine.startsWith("SHUTDOWN"))
             //---------------------------------------------------------------
             {
                // if we are a participant, leave chat first   
                if (hasJoined == true)
                {
                    // send leave request
                    try
                    {
                       // open connection to server
                       serverConnection = new Socket(ChatClient.serverNodeInfo.getAddress(), ChatClient.serverNodeInfo.getPort());
                    
                       // open object streams
                       readFromNet = new ObjectInputStream(serverConnection.getInputStream());
                       writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());
                      
                       // send shutdown all request
                       writeToNet.writeObject(new Message(SHUTDOWN, ChatClient.myNodeInfo));
                      
                       // close connection
                       serverConnection.close();
                       
                       System.out.println("Leave chat...");
                    }
                    catch (IOException ex)
                    {
                      //Dr.Otte told us to remove this
                      //Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, "Error connecting to server or opening or writing/reading object streams
                      // or closing connection", ex);  
                    }
                }
                
                System.out.println("Exiting...\n");
                System.exit(0);
             }
             //---------------------------------------------------------------
             else // sending a note
             //---------------------------------------------------------------
             {
                if(hasJoined == false)
                {
                    System.err.println("You need to join a chat first!");
                    continue;
                }
                
                // send note
                try
                {
                   // open connection to server
                   serverConnection = new Socket(ChatClient.serverNodeInfo.getAddress(), ChatClient.serverNodeInfo.getPort());                   
                   
                   // open object streams
                   readFromNet = new ObjectInputStream(serverConnection.getInputStream());
                   writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());
                      
                   // send shutdown all request
                   writeToNet.writeObject(new Message(NOTE, ChatClient.myNodeInfo.getName()+ ": " + inputLine));
                      
                   // close connection
                   serverConnection.close();
                       
                   System.out.println("Message sent...");
                } 
                catch (IOException ex)
                {
                  //Dr.Otte told us to remove this
                  //Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, "Error connecting to server or opening or writing/reading object streams
                  // or closing connection", ex);
                  //continue;     
                }                
             }
          }  
      }   
}
