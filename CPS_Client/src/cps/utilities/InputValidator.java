package cps.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Pattern;

import cps.entities.Customer;
import cps.entities.FullMembership;

// TODO: Auto-generated Javadoc
/**
 * The Class InputValidator.
 * This class is used to validate the client's inputs.
 */
public class InputValidator
{
    
    /**
     * Validate Car number.
     *
     * @param carNumber the car number
     * @return true, if successful
     */
    public static boolean CarNumber(String carNumber)
    {
	if (!Pattern.matches("[0-9]+", carNumber) || carNumber.length() < 5 || carNumber.length() > 8)
	{
	    return false;
	}
	return true;
    }
    
    /**
     * Validate Credit card number.
     *
     * @param creditcard the creditcard
     * @return true, if successful
     */
    public static boolean CreditCardNumber(String creditcard)
    {
	if (!Pattern.matches("[0-9]+", creditcard))
	{
	    return false;
	}
	return true;
    }
    
    /**
     * Validate ccv.
     *
     * @param ccv the ccv
     * @return true, if successful
     */
    public static boolean Ccv(String ccv)
    {
	if ((!Pattern.matches("[0-9]+", ccv)) || (ccv.length() != 3)) return false;
	return true;
    }
    
    /**
     * Validate Expiration date.
     *
     * @param date the date
     * @return true, if successful
     */
    public static boolean ExpirationDate(YearMonth date)
    {
	if (date.isBefore(YearMonth.now()))
	{
	    return false;
	}
	return true;
    }
    
    /**
     * Validate Starting date.
     *
     * @param startingDate the starting date
     * @return true, if successful
     */
    public static boolean StartingDate(LocalDate startingDate)
    {
	if (startingDate.isBefore(LocalDate.now()))
	{
	    return false;
	}
	return true;
    }
    
    /**
     * Check leaving date.
     *
     * @param startingDate the starting date
     * @param leavingDate the leaving date
     * @return true, if successful
     */
    public static boolean CheckLeavingDate(LocalDate startingDate, LocalDate leavingDate)
    {
	
	if (startingDate.isAfter(leavingDate)) return false;
	if ((Period.between(startingDate, leavingDate).getYears() > 0)
		|| (Period.between(startingDate, leavingDate).getMonths() > 0)
		|| (Period.between(startingDate, leavingDate).getDays() > 14))
	{
	    return false;
	}
	return true;
    }
    
    /**
     * Validate Email.
     *
     * @param email the email
     * @return true, if successful
     */
    public static boolean Email(String email)
    {
	if (!Pattern.matches("^.+@.+\\..+$", email))
	{
	    return false;
	}
	return true;
    }
    
    /**
     * Validate Id.
     *
     * @param id the id
     * @return true, if successful
     */
    public static boolean Id(String id)
    {
	if (!Pattern.matches("[0-9]+", id) || id.length() != 9)
	{
	    return false;
	}
	return true;
	
    }
    
    /**
     * Check visa date.
     *
     * @param date the date
     * @return true, if successful
     */
    public static boolean CheckVisaDate(String date)
    {
	try
	{
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
	    return ExpirationDate(YearMonth.parse(date, formatter));
	}
	catch (Exception e)
	{
	    return false;
	}
    }
    
    /**
     * Validate hour format.
     *
     * @param hour the hour
     * @return true, if successful
     */
    public static boolean CheckHourFormat(String hour)
    {
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm"); // HH = 24h
	// format
	dateFormat.setLenient(false); // this will not enable 25:67 for example
	try
	{
	    dateFormat.parse(hour);
	}
	catch (ParseException e)
	{
	    return false;
	}
	return true;
    }
    
    /**
     * Check leaving hour.
     *
     * @param startingHour the starting hour
     * @param leavingHour the leaving hour
     * @param startingDate the starting date
     * @param leavingDate the leaving date
     * @return true, if successful
     */
    public static boolean CheckLeavingHour(String startingHour, String leavingHour, LocalDate startingDate,
	    LocalDate leavingDate)
    {
	if (startingDate.equals(leavingDate) && (LocalTime.parse(startingHour).isAfter(LocalTime.parse(leavingHour))))
	    return false;
	return true;
    }
    
    /**
     * Validate Full membership class.
     *
     * @param fullMembership the full membership
     * @return true, if successful
     */
    public static boolean FullMembership(FullMembership fullMembership)
    {
	if (CarNumber(fullMembership.GetCarNumber()) && StartingDate(fullMembership.GetStartDate())
		&& Id(fullMembership.GetCustomerId()))
	{
	    return true;
	}
	return false;
    }
    
    /**
     * Validate Customer class.
     *
     * @param customer the customer
     * @return true, if successful
     */
    public static boolean Customer(Customer customer)
    {
	if (Email(customer.GetEmail()) && Id(customer.GetId()))
	{
	    return true;
	}
	return false;
    }
    
    /**
     * Validate Order in advance class.
     *
     * @param carNumber the car number
     * @param arrivalDate the arrival date
     * @param leavingDate the leaving date
     * @param arrivalHour the arrival hour
     * @param leavingHour the leaving hour
     * @return true, if successful
     */
    public static boolean OrderInAdvance(String carNumber, LocalDate arrivalDate, LocalDate leavingDate,
	    String arrivalHour, String leavingHour)
    {
	
	if (CarNumber(carNumber) && StartingDate(arrivalDate) && CheckHourFormat(arrivalHour)
		&& CheckHourFormat(leavingHour) && CheckLeavingHour(arrivalHour, leavingHour, arrivalDate, leavingDate)
		&& CheckLeavingDate(arrivalDate, leavingDate))
	{
	    return true;
	}
	return false;
    }
    
    /**
     * Validate Partial membership class.
     *
     * @param carlist the car list
     * @param email the email
     * @param arrivalDate the arrival date
     * @return true, if successful
     */
    public static boolean PartialMembership(ArrayList<String> carlist, String email, LocalDate arrivalDate)
    {
	
	if (CheckCarList(carlist) && StartingDate(arrivalDate) && Email(email))
	{
	    return true;
	}
	return false;
    }
    
    /**
     * Validate car list.
     *
     * @param carlist the car list
     * @return true, if successful
     */
    public static boolean CheckCarList(ArrayList<String> carlist)
    {
	if (carlist.isEmpty()) return false;
	for (String string : carlist)
	{
	    if (!CarNumber(string)) return false;
	}
	return true;
	
    }
    
    /**
     * Validate that Text isn't empty.
     *
     * @param text the text
     * @return true, if successful
     */
    public static boolean TextIsEmpty(String text)
    {
	if (text.length() > 0) return true;
	return false;
	
    }
    
    /**
     * Validate Order id.
     *
     * @param orderid the order id
     * @return true, if successful
     */
    public static boolean OrderId(String orderid)
    {
	if (!Pattern.matches("[0-9]+", orderid)) return false;
	return true;
	
    }
    
}
