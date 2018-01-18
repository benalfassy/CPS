package cps.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import cps.entities.enums.ReservationStatus;
import cps.entities.enums.ReservationType;

// TODO: Auto-generated Javadoc
/**
 * The Class Reservation.
 */
public class Reservation implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    private String orderId;
    
    private String customerId;
    
    private String parkingLot;
    
    private String carNumber;
    
    private LocalDate arrivalDate;
    
    private LocalDate leavingDate;
    
    private LocalTime arrivalHour;
    
    private LocalTime leavingHour;
    
    private ReservationType reservationType;
    
    private ReservationStatus reservationStatus;
    
    private float price;
    
    /**
     * Instantiates a new reservation.
     *
     * @param reservationType
     *            the reservation type
     * @param customerId
     *            the customer id
     * @param parkinglot
     *            the parkinglot
     * @param carNumber
     *            the car number
     * @param arrivalDate
     *            the arrival date
     * @param leavingDate
     *            the leaving date
     * @param arrivalHour
     *            the arrival hour
     * @param leavingHour
     *            the leaving hour
     * @param reservationStatus
     *            the reservation status
     * @param price
     *            the price
     */
    public Reservation(ReservationType reservationType, String customerId, String parkinglot, String carNumber,
	    LocalDate arrivalDate, LocalDate leavingDate, LocalTime arrivalHour, LocalTime leavingHour,
	    ReservationStatus reservationStatus, float price)
    {
	
	this.customerId = customerId;
	this.parkingLot = parkinglot;
	this.carNumber = carNumber;
	this.arrivalDate = arrivalDate;
	this.leavingDate = leavingDate;
	this.arrivalHour = arrivalHour;
	this.leavingHour = leavingHour;
	this.reservationType = reservationType;
	this.reservationStatus = reservationStatus;
	this.orderId = "Not yet initialized";
	this.price = price;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
	return "order ID: " + orderId + "\nReservation Type: " + reservationType + "\nCustomer ID: " + customerId
		+ "\nParkinglot: " + parkingLot + "\nCar Number: " + carNumber + "\nArrival Date: " + arrivalDate
		+ "\nLeaving Date: " + leavingDate + "\nArrival Hour: " + arrivalHour + "\nLeaving Hour: " + leavingHour
		+ "\nReservation status: " + reservationStatus + "\nEstimated price: " + price;
    }
    
    /**
     * Gets the order id.
     *
     * @return the order id
     */
    public String getOrderId()
    {
	return orderId;
    }
    
    /**
     * Gets the price.
     *
     * @return the price
     */
    public float getPrice()
    {
	return price;
    }
    
    /**
     * Gets the parkinglot.
     *
     * @return the parkinglot
     */
    public String getParkinglot()
    {
	return parkingLot;
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
     * Gets the arrival date.
     *
     * @return the arrival date
     */
    public LocalDate getArrivalDate()
    {
	return arrivalDate;
    }
    
    /**
     * Gets the leaving date.
     *
     * @return the leaving date
     */
    public LocalDate getLeavingDate()
    {
	return leavingDate;
    }
    
    /**
     * Gets the arrival hour.
     *
     * @return the arrival hour
     */
    public LocalTime getArrivalHour()
    {
	return arrivalHour;
    }
    
    /**
     * Gets the leaving hour.
     *
     * @return the leaving hour
     */
    public LocalTime getLeavingHour()
    {
	return leavingHour;
    }
    
    /**
     * Gets the customer id.
     *
     * @return the customer id
     */
    public String getCustomerId()
    {
	return customerId;
    }
    
    /**
     * Gets the parking lot.
     *
     * @return the parking lot
     */
    public String getParkingLot()
    {
	return parkingLot;
    }
    
    /**
     * Gets the reservation status.
     *
     * @return the reservation status
     */
    public ReservationStatus getReservationStatus()
    {
	return reservationStatus;
    }
    
    /**
     * Gets the reservation type.
     *
     * @return the reservation type
     */
    public ReservationType getReservationType()
    {
	return reservationType;
    }
    
    /**
     * Sets the order id.
     *
     * @param orderId
     *            the new order id
     */
    public void setOrderId(String orderId)
    {
	this.orderId = orderId;
    }
}
