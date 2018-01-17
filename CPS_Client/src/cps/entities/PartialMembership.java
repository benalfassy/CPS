package cps.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class PartialMembership.
 */
public class PartialMembership extends MembershipBase
{
    
    private static final long serialVersionUID = 1L;
    
    private String parkinglot;
    
    private ArrayList<String> carList;
    
    private LocalTime exitTime;
    
    /**
     * Instantiates a new partial membership.
     *
     * @param id
     *            the id
     * @param startingDate
     *            the starting date
     * @param endingDate
     *            the ending date
     * @param parkinglot
     *            the parkinglot
     * @param carList
     *            the car list
     * @param exitTime
     *            the exit time
     */
    public PartialMembership(String id, LocalDate startingDate, LocalDate endingDate, String parkinglot,
	    ArrayList<String> carList, LocalTime exitTime)
    {
	super(id, startingDate, endingDate);
	
	this.parkinglot = parkinglot;
	this.carList = carList;
	this.exitTime = exitTime;
    }
    
    /**
     * Gets the parking lot.
     *
     * @return the string
     */
    public String GetParkinglot()
    {
	return parkinglot;
    }
    
    /**
     * Gets the car list.
     *
     * @return the array list
     */
    public ArrayList<String> GetCarList()
    {
	return carList;
    }
    
    /**
     * Gets the exit time.
     *
     * @return the local time
     */
    public LocalTime GetExitTime()
    {
	return exitTime;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see cps.entities.MembershipBase#toString()
     */
    @Override
    public String toString()
    {
	return "Partial Membership: \n\n" + super.toString() + "Parkinglot: " + parkinglot + "\nExit time: " + exitTime
		+ "\n" + "Cars: " + CarListString();
    }
    
    /**
     * Turns the car list to a string.
     *
     * @return the string
     */
    public String CarListString()
    {
	String carListString = "";
	for (String car : carList)
	{
	    carListString += (car + " ,");
	}
	
	// trick :
	
	if (carListString != "")
	{
	    carListString += '$';
	    carListString = carListString.replace(" ,$", "");
	}
	
	return carListString;
    }
}
