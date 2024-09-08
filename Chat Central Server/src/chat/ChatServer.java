package chat;

import java.io.Serializable;
import java.io.IOException;
import java.net.ServerSocket;
import utils.NetworkUtilities;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

import utils.PropertyHandler;
import java.util.Properties;


/**
 * Chat server class
 * 
 * Reads configuration information and starts up the ChatServer
 * 
 * @author wolfdieterotte
 */
public class ChatServer implements Runnable
{
    // array list to contain all the participants' information
    public static List<NodeInfo> participants = new ArrayList<>();

    // connection related variables
    int port = 0;
    ServerSocket serverSocket = null;


    // constructor
    public ChatServer(String propertiesFile)
    {
        Properties properties = null;
        try
        {
            properties = new PropertyHandler(propertiesFile);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "Cannot open properties file", ex);
            System.exit(1);
        }

        // get port number
        try
        {
            port = Integer.parseInt(properties.getProperty("PORT"));
        }
        catch (NumberFormatException ex)
        {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "Cannot read port", ex);
            System.exit(1);
        }
    }


    // code entry point, not used for threading
    @Override
    public void run()
    {
        // setting up server socket
        try
        {
            serverSocket = new ServerSocket(port);
            System.out.println("ChatServer listening on " + NetworkUtilities .getMyIP() + ":" + port);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "Cannot open server socket", ex);
            System.exit(1);
        }

        // server loop
        while (true)
        {
            try
            {
                (new ChatServerWorker(serverSocket.accept())).start();
            }
            catch (IOException e)
            {
                System.out.println("[ChatServer.run] Warning: Error accepting client");
            }
        }
    }


    // main()
    public static void main(String[] args)
    {

        String propertiesFile = null;

        try
        {
            propertiesFile = args[0];
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            propertiesFile = "config/ChatServer.properties";
        }

        // start server
        (new ChatServer(propertiesFile)).run();
    }
}
