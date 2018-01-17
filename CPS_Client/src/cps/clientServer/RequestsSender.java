package cps.clientServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cps.entities.ActivityReport;
import cps.entities.AddRealTimeParkingRequest;
import cps.entities.ChangeParkingSpotStatusRequest;
import cps.entities.ChangeParkinglotStatusRequest;
import cps.entities.ChangeRatesRequest;
import cps.entities.ChangeRatesResponse;
import cps.entities.CloseComplaintRequest;
import cps.entities.Complaint;
import cps.entities.ComplaintsReport;
import cps.entities.CreditCustomerRequest;
import cps.entities.Customer;
import cps.entities.DisabledReport;
import cps.entities.Employee;
import cps.entities.FullMembership;
import cps.entities.LoginIdentification;
import cps.entities.ParkingSpot;
import cps.entities.Parkinglot;
import cps.entities.PartialMembership;
import cps.entities.PerformanceReport;
import cps.entities.RemoveCarRequest;
import cps.entities.Reservation;
import cps.entities.ReservationReport;
import cps.entities.StatusReport;
import cps.utilities.Consts;
import cps.utilities.DialogBuilder;
import javafx.scene.control.Dialog;

// TODO: Auto-generated Javadoc
/**
 * The Class RequestsSender. This is the API that the clients can use to speak
 * with the server.
 */
public class RequestsSender
{
    
    private static String serverIP;
    
    /**
     * Instantiates a new requests sender with ip from the GUI.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws URISyntaxException
     *             the URI syntax exception
     */
    public RequestsSender() throws IOException, URISyntaxException
    {
	ArrayList<String> ip = new ArrayList<>();
	
	ip.add("IP:");
	
	Dialog<List<String>> dialog = DialogBuilder.InputsDialog("Set server's IP", ip, Consts.Submit);
	
	dialog.setHeaderText("Add server ip or Cancel to use local host");
	
	Optional<List<String>> result = dialog.showAndWait();
	
	result.ifPresent(inputs ->
	{
	    serverIP = inputs.get(0);
	});
	
	if (!result.isPresent())
	{
	    serverIP = "127.0.0.1";
	}
    }
    
    /**
     * Instantiates a new requests sender for E2E tests.
     *
     * @param ip
     *            the ip
     */
    public RequestsSender(String ip)
    {
	serverIP = ip;
    }
    
    @SuppressWarnings("unchecked")
    private static <T> ServerResponse<T> SendRequest(Object sentObject, String serverDestination)
    {
	try (Socket socket = new Socket(serverIP, ClientServerConsts.PORT);
		ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream()))
	{
	    
	    ClientRequest clientRequest = new ClientRequest(sentObject, serverDestination);
	    
	    outputStream.writeObject(clientRequest);
	    
	    ServerResponse<T> serverResponse = (ServerResponse<T>) inputStream.readObject();
	    
	    return serverResponse;
	}
	catch (Exception e)
	{
	    System.out.println(e);
	    e.printStackTrace();
	    return new ServerResponse<T>(RequestResult.Failed, null, "Internal server error");
	}
    }
    
    /**
     * Register full membership.
     *
     * @param fullMembership
     *            the full membership
     * @return the server response
     */
    public static ServerResponse<FullMembership> RegisterFullMembership(FullMembership fullMembership)
    {
	return SendRequest(fullMembership, ClientServerConsts.RegisterFullMembership);
    }
    
    /**
     * Register partial membership.
     *
     * @param partialMembership
     *            the partial membership
     * @return the server response
     */
    public static ServerResponse<PartialMembership> RegisterPartialMembership(PartialMembership partialMembership)
    {
	return SendRequest(partialMembership, ClientServerConsts.RegisterPartialMembership);
    }
    
    /**
     * Adds the customer if not exists.
     *
     * @param customer
     *            the customer
     * @return the server response
     */
    public static ServerResponse<Customer> AddCustomerIfNotExists(Customer customer)
    {
	return SendRequest(customer, ClientServerConsts.AddCustomerIfNotExists);
    }
    
    /**
     * Gets the full membership.
     *
     * @param subscriptionId
     *            the subscription id
     * @return the server response
     */
    public static ServerResponse<FullMembership> GetFullMembership(String subscriptionId)
    {
	return SendRequest(subscriptionId, ClientServerConsts.GetFullMembership);
    }
    
    /**
     * Gets the partial membership.
     *
     * @param subscriptionId
     *            the subscription id
     * @return the server response
     */
    public static ServerResponse<PartialMembership> GetPartialMembership(String subscriptionId)
    {
	return SendRequest(subscriptionId, ClientServerConsts.GetPartialMembership);
    }
    
    /**
     * Gets the customer.
     *
     * @param customerId
     *            the customer id
     * @return the server response
     */
    public static ServerResponse<Customer> GetCustomer(String customerId)
    {
	return SendRequest(customerId, ClientServerConsts.GetCustomer);
    }
    
    /**
     * Adds Reservation.
     *
     * @param reservation
     *            the reservation
     * @return the server response
     */
    public static ServerResponse<Reservation> Reservation(Reservation reservation)
    {
	return SendRequest(reservation, ClientServerConsts.Reservation);
    }
    
    /**
     * Gets the reservation.
     *
     * @param orderId
     *            the order id
     * @return the server response
     */
    public static ServerResponse<Reservation> GetReservation(String orderId)
    {
	return SendRequest(orderId, ClientServerConsts.GetReservation);
    }
    
    /**
     * Gets the employee.
     *
     * @param loginIdentification
     *            the login identification
     * @return the server response
     */
    public static ServerResponse<Employee> GetEmployee(LoginIdentification loginIdentification)
    {
	return SendRequest(loginIdentification, ClientServerConsts.GetEmployee);
    }
    
    /**
     * Adds the parking lot.
     *
     * @param parkinglot
     *            the parking lot
     * @return the server response
     */
    public static ServerResponse<Parkinglot> AddParkinglot(Parkinglot parkinglot)
    {
	return SendRequest(parkinglot, ClientServerConsts.AddParkinglot);
    }
    
    /**
     * Gets all the parking lots.
     *
     * @return the server response
     */
    public static ServerResponse<ArrayList<Parkinglot>> GetAllParkinglots(boolean onlyNotFull)
    {
	return SendRequest(onlyNotFull, ClientServerConsts.GetAllParkinglots);
    }
    
    /**
     * Gets the parking lot.
     *
     * @param parkinglotName
     *            the parking lot name
     * @return the server response
     */
    public static ServerResponse<Parkinglot> GetParkinglot(String parkinglotName)
    {
	return SendRequest(parkinglotName, ClientServerConsts.GetParkingLot);
    }
    
    /**
     * Change parking lot status.
     *
     * @param changeParkinglotStatusRequest
     *            the change parking lot status request
     * @return the server response
     */
    public static ServerResponse<ChangeParkinglotStatusRequest> ChangeParkinglotStatus(
	    ChangeParkinglotStatusRequest changeParkinglotStatusRequest)
    {
	return SendRequest(changeParkinglotStatusRequest, ClientServerConsts.ChangeParkinglotStatus);
    }
    
    /**
     * Adds a complaint.
     *
     * @param complaint
     *            the complaint
     * @return the server response
     */
    public static ServerResponse<Complaint> AddComplaint(Complaint complaint)
    {
	return SendRequest(complaint, ClientServerConsts.AddComplaint);
    }
    
    /**
     * Gets all active complaints.
     *
     * @return the server response
     */
    public static ServerResponse<ArrayList<Complaint>> GetAllActiveComplaints()
    {
	return SendRequest(null, ClientServerConsts.GetAllActiveComplaints);
    }
    
    /**
     * Close complaint.
     *
     * @param closeComplaintRequest
     *            the close complaint request
     * @return the server response
     */
    public static ServerResponse<CloseComplaintRequest> CloseComplaint(CloseComplaintRequest closeComplaintRequest)
    {
	return SendRequest(closeComplaintRequest, ClientServerConsts.CloseComplaint);
    }
    
    /**
     * Adds the rate change request.
     *
     * @param changeRatesRequest
     *            the rate change request
     * @return the server response
     */
    public static ServerResponse<ChangeRatesRequest> AddChangeRatesRequest(ChangeRatesRequest changeRatesRequest)
    {
	return SendRequest(changeRatesRequest, ClientServerConsts.AddChangeRatesRequest);
    }
    
    /**
     * Close rate change request.
     *
     * @param changeRatesResponse
     *            the rate change response
     * @return the server response
     */
    public static ServerResponse<ChangeRatesResponse> CloseChangeRatesRequest(ChangeRatesResponse changeRatesResponse)
    {
	return SendRequest(changeRatesResponse, ClientServerConsts.CloseChangeRatesRequest);
    }
    
    /**
     * Gets the all rate change requests.
     *
     * @return the server response
     */
    public static ServerResponse<ArrayList<ChangeRatesRequest>> GetAllChangeRatesRequests()
    {
	return SendRequest(null, ClientServerConsts.GetAllChangeRatesRequests);
    }
    
    /**
     * Change parking spot status.
     *
     * @param changeParkingSpotStatusRequest
     *            the change parking spot status request
     * @return the server response
     */
    public static ServerResponse<ChangeParkingSpotStatusRequest> ChangeParkingSpotStatus(
	    ChangeParkingSpotStatusRequest changeParkingSpotStatusRequest)
    {
	return SendRequest(changeParkingSpotStatusRequest, ClientServerConsts.ChangeParkingSpotStatus);
    }
    
    /**
     * Gets all the disabled parking spots.
     *
     * @return the server response
     */
    public static ServerResponse<ArrayList<ParkingSpot>> GetAllDisabledParkingSpots()
    {
	return SendRequest(null, ClientServerConsts.GetAllDisabledParkingSpots);
    }
    
    /**
     * Change expiration date of full membership.
     *
     * @param fullMembership
     *            the full membership
     * @return the server response
     */
    public static ServerResponse<FullMembership> ChangeExpireFullMembership(FullMembership fullMembership)
    {
	return SendRequest(fullMembership, ClientServerConsts.ChangeExpireFullMembership);
    }
    
    /**
     * Change expiration date of partial membership.
     *
     * @param partialMembership
     *            the partial membership
     * @return the server response
     */
    public static ServerResponse<PartialMembership> ChangeExpirePartialMembership(PartialMembership partialMembership)
    {
	return SendRequest(partialMembership, ClientServerConsts.ChangeExpirePartialMembership);
    }
    
    /**
     * Try to insert a car.
     *
     * @param addRealTimeParkingRequest
     *            the add real time parking request
     * @return the server response
     */
    public static ServerResponse<AddRealTimeParkingRequest> TryInsertCar(
	    AddRealTimeParkingRequest addRealTimeParkingRequest)
    {
	return SendRequest(addRealTimeParkingRequest, ClientServerConsts.TryInsertGuestCar);
    }
    
    /**
     * Removes the car.
     *
     * @param removeCarRequest
     *            the remove car request
     * @return the server response
     */
    public static ServerResponse<RemoveCarRequest> RemoveCar(RemoveCarRequest removeCarRequest)
    {
	return SendRequest(removeCarRequest, ClientServerConsts.RemoveCar);
    }
    
    /**
     * Gets the complaints report.
     *
     * @return the server response
     */
    public static ServerResponse<ComplaintsReport> GetComplaintsReport()
    {
	return SendRequest(null, ClientServerConsts.GetComplaintsReport);
    }
    
    /**
     * Gets the performance report.
     *
     * @return the server response
     */
    public static ServerResponse<PerformanceReport> GetPerformanceReport()
    {
	return SendRequest(null, ClientServerConsts.GetPerformanceReport);
    }
    
    /**
     * Gets the reservation report.
     *
     * @param parkinglotName
     *            the parkinglot name
     * @return the server response
     */
    public static ServerResponse<ReservationReport> GetReservationReport(String parkinglotName)
    {
	return SendRequest(parkinglotName, ClientServerConsts.GetReservationReport);
    }
    
    /**
     * Gets the disabled report.
     *
     * @param parkinglotName
     *            the parkinglot name
     * @return the server response
     */
    public static ServerResponse<DisabledReport> GetDisabledReport(String parkinglotName)
    {
	return SendRequest(parkinglotName, ClientServerConsts.GetDisabledReport);
    }
    
    /**
     * Gets the activity report.
     *
     * @param localDate
     *            the local date
     * @return the server response
     */
    public static ServerResponse<ActivityReport> GetActivityReport(LocalDate localDate)
    {
	return SendRequest(localDate, ClientServerConsts.GetActivityReport);
    }
    
    /**
     * Gets the status report.
     *
     * @return the server response
     */
    public static ServerResponse<StatusReport> GetStatusReport()
    {
	return SendRequest(null, ClientServerConsts.GetStatusReport);
    }
    
    /**
     * Login user.
     *
     * @param username
     *            the username
     * @return the server response
     */
    public static ServerResponse<String> LoginUser(String username)
    {
	return SendRequest(username, ClientServerConsts.LoginUser);
    }
    
    /**
     * Logout user.
     *
     * @param username
     *            the username
     * @return the server response
     */
    public static ServerResponse<String> LogoutUser(String username)
    {
	return SendRequest(username, ClientServerConsts.LogoutUser);
    }
    
    /**
     * Close reservation.
     *
     * @param reservationId
     *            the reservation id
     * @return the server response
     */
    public static ServerResponse<String> CloseReservation(String reservationId)
    {
	return SendRequest(reservationId, ClientServerConsts.CloseReservation);
    }
    
    /**
     * Credit customer.
     *
     * @param request
     *            the request
     * @return the server response
     */
    public static ServerResponse<CreditCustomerRequest> CreditCustomer(CreditCustomerRequest request)
    {
	return SendRequest(request, ClientServerConsts.AddCreditToCustomer);
    }
}
