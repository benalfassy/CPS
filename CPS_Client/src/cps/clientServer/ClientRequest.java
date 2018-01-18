package cps.clientServer;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class ClientRequest. This is the object that the server accepts.
 */
public class ClientRequest implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private String serverDestination;
    
    private Object sentObject;
    
    /**
     * Instantiates a new client request.
     *
     * @param sentObject
     *            the sent object
     * @param serverDestination
     *            the server destination
     */
    public ClientRequest(Object sentObject, String serverDestination)
    {
	this.serverDestination = serverDestination;
	
	this.sentObject = sentObject;
    }
    
    /**
     * Gets the sent object.
     *
     * @return the object
     */
    public Object GetSentObject()
    {
	return sentObject;
    }
    
    /**
     * Gets the server destination.
     *
     * @return the server destination
     */
    public String getServerDestination()
    {
	return serverDestination;
    }
}
