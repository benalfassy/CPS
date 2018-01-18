package cps.entities;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class ChangeRatesResponse.
 */
public class ChangeRatesResponse implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    private String requestId;
    
    private boolean isApproved;
    
    /**
     * Instantiates a new change rates response.
     *
     * @param requestId
     *            the request id
     * @param isApproved
     *            the is approved
     */
    public ChangeRatesResponse(String requestId, boolean isApproved)
    {
	this.requestId = requestId;
	this.isApproved = isApproved;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
	return "Change rates request id: " + requestId + "\nApproved: " + isApproved;
    }
    
    /**
     * Gets the request id.
     *
     * @return the request id
     */
    public String getRequestId()
    {
	return requestId;
    }
    
    /**
     * Gets the checks if it is approved.
     *
     * @return the checks if is approved
     */
    public boolean getIsApproved()
    {
	return isApproved;
    }
}
