package cps.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

// TODO: Auto-generated Javadoc
/**
 * The Class AddRealTimeParkingRequest.
 */
public class AddRealTimeParkingRequest implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    private String parkinglot;
    
    private LocalDateTime startDateTime;
    
    private LocalDateTime exiDateTime;
    
    private String carNumber;
    
    private boolean isGuestRequest;
    
    /**
     * Instantiates a new adds the real time parking request.
     *
     * @param parkinglot
     *            the parkinglot
     * @param startDateTime
     *            the start date time
     * @param exiDateTime
     *            the exi date time
     * @param carNumber
     *            the car number
     * @param isGuestRequest
     *            the is guest request
     */
    public AddRealTimeParkingRequest(String parkinglot, LocalDateTime startDateTime, LocalDateTime exiDateTime,
	    String carNumber, boolean isGuestRequest)
    {
	this.parkinglot = parkinglot;
	this.startDateTime = startDateTime;
	this.exiDateTime = exiDateTime;
	this.carNumber = carNumber;
	this.isGuestRequest = isGuestRequest;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
	return "parkinglot: " + parkinglot + "\ncar: " + carNumber + "\nfrom: " + startDateTime.toString() + "\nto: "
		+ exiDateTime.toString();
    }
    
    /**
     * Gets the car number.
     *
     * @return the car number
     */
    public String getCarNumber()
    {
	return carNumber;
    }
    
    /**
     * Gets if is guest request.
     *
     * @return the checks if is guest request
     */
    public boolean getIsGuestRequest()
    {
	return isGuestRequest;
    }
    
    /**
     * Gets the exi date time.
     *
     * @return the exi date time
     */
    public LocalDateTime getExiDateTime()
    {
	return exiDateTime;
    }
    
    /**
     * Gets the parkinglot.
     *
     * @return the parkinglot
     */
    public String getParkinglot()
    {
	return parkinglot;
    }
    
    /**
     * Gets the start date time.
     *
     * @return the start date time
     */
    public LocalDateTime getStartDateTime()
    {
	return startDateTime;
    }
}
