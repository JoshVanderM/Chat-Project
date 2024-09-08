package chat;

import java.io.Serializable;


/*
* Simple class to represent IP/port/name information of a host
*/
public class NodeInfo implements Serializable
{

    String address;
    int port;
    String name = null;

    /**
     * Detail constructor
     * 
     * @param address
     * @param port
     * @param name
     */
    public NodeInfo(String address, int port, String name)
    {
        this.address = address;
        this.port = port;
        this.name = name;
    }


    /**
     * Constructor when name is null
     * 
     * @param address
     * @param port
     */
    public NodeInfo(String address, int port)
    {
        this(address, port, null);
    }


    // Getter methods
    String getAddress()
    {
        return this.address;
    }

    int getPort()
    {
        return this.port;
    }

    String getName()
    {
        return this.name;
    }

    /**
     * Implementation and override of method equals from Object
     * Implicitly used in ArrayList's remove()
     * Two NodeInfo onjects are the same, if the IPs and the ports are the same
     * 
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other)
    {
        String otherIP = ((NodeInfo) other).getAddress();
        int otherPort  = ((NodeInfo) other).getPort();
        
        return otherIP.equals(this.address) && (otherPort == this.port);
    }
}   
