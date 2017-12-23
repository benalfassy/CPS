package clientServerCPS;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;

import CPS_Utilities.CPS_Tracer;
import CPS_Utilities.CloseComplaintRequest;
import CPS_Utilities.LoginIdentification;
import entities.Complaint;
import entities.Customer;
import entities.Employee;
import entities.FullMembership;
import entities.Parkinglot;
import entities.PartialMembership;
import entities.Reservation;
import entities.enums.ParkinglotStatus;
import entities.enums.ReservationStatus;
import entities.enums.ReservationType;

@SuppressWarnings("unused")
public class Tests
{
    public static void main(String[] args)
    {
	try
	{
	    new RequestsSender();
	    
	    // FullMembershipTest() PartialMembershipTest() ComplaintTest()
	    // CustomerTest() ReservationTest()
	    // EmployeeTest() ParkinglotTest()
	    
	    if (ComplaintTest())
	    {
		System.out.println("Test Succeed");
	    }
	    else
	    {
		System.out.println("Test Failed");
	    }
	}
	catch (Exception e)
	{
	    System.out.println("Failed with exception: " + e);
	}
    }
    
    private static boolean ComplaintTest()
    {
	Complaint complaint = new Complaint("301731469",
		"My Ferari got dirty with dust in Nosh parkinglot.. \nI will not bring my Lamburgini unless u compensate me in 1,000$ !!!");
	
	ServerResponse<Complaint> serverResponse = RequestsSender.AddComplaint(complaint);
	
	ServerResponse<ArrayList<Complaint>> serverGetResponse = RequestsSender.GetAllActiveComplaints();
	
	boolean isMyComplaintThere = false;
	
	for(Complaint c : serverGetResponse.GetResponseObject())
	{
	    if(c.getComplaintId().equals(serverResponse.GetResponseObject().getComplaintId()))
	    {
		isMyComplaintThere = true;
	    }
	}
	
	if(!isMyComplaintThere)
	{
	    return false;
	}
	
	CloseComplaintRequest closeComplaintRequest = new CloseComplaintRequest(serverResponse.GetResponseObject().getComplaintId(), 1000);
	
	ServerResponse<CloseComplaintRequest> serverResponse2 = RequestsSender.CloseComplaint(closeComplaintRequest);
	
	if(!serverResponse2.GetRequestResult().equals(RequestResult.Succeed))
	{
	    return false;
	}
	
	return true;
    }
    
    private static boolean ParkinglotTest()
    {
	String seed = Integer.toString(new Random().nextInt(1000000) + 3000000);
	
	Parkinglot parkinglot = new Parkinglot("Test lot" + seed, 5, ParkinglotStatus.Closed, 10, 20);
	
	ServerResponse<Parkinglot> serverResponse = RequestsSender.AddParkinglot(parkinglot);
	
	CPS_Tracer.TraceInformation(serverResponse.toString());
	
	ServerResponse<ArrayList<Parkinglot>> serverGetResponse = RequestsSender.GetAllParkinglots();
	
	CPS_Tracer.TraceInformation(serverGetResponse.toString());
	
	if (!serverGetResponse.GetRequestResult().equals(RequestResult.Succeed)
		|| !serverResponse.GetRequestResult().equals(RequestResult.Succeed))
	{
	    return false;
	}
	
	return true;
    }
    
    private static boolean EmployeeTest()
    {
	LoginIdentification creds = new LoginIdentification("benalfasi", "notrealpw");
	
	ServerResponse<Employee> serverResponse = RequestsSender.GetEmployee(creds);
	
	CPS_Tracer.TraceInformation(serverResponse.toString());
	
	if (!serverResponse.GetRequestResult().equals(RequestResult.Succeed))
	{
	    return false;
	}
	
	LoginIdentification falseCreds = new LoginIdentification("blabla", "1234");
	
	ServerResponse<Employee> serverResponse2 = RequestsSender.GetEmployee(falseCreds);
	
	CPS_Tracer.TraceInformation(serverResponse2.toString());
	
	if (!serverResponse2.GetRequestResult().equals(RequestResult.WrongCredentials))
	{
	    return false;
	}
	
	return true;
    }
    
    private static boolean ReservationTest()
    {
	String id = Integer.toString(new Random().nextInt(1000000) + 3000000);
	
	Reservation reservation = new Reservation(ReservationType.InAdvance, id, "Testlot", "333333", LocalDate.now(),
		LocalDate.now(), LocalTime.parse("11:11"), LocalTime.parse("11:11"), ReservationStatus.NotStarted);
	
	ServerResponse<Reservation> serverResponse = RequestsSender.Reservation(reservation);
	
	CPS_Tracer.TraceInformation(serverResponse.toString());
	
	ServerResponse<Reservation> serverGetResponse = RequestsSender
		.GetReservation(serverResponse.GetResponseObject().getOrderId());
	
	CPS_Tracer.TraceInformation(serverResponse.toString());
	
	if (!serverGetResponse.GetRequestResult().equals(RequestResult.Succeed))
	{
	    return false;
	}
	
	return true;
    }
    
    private static boolean FullMembershipTest()
    {
	String id = Integer.toString(new Random().nextInt(1000000) + 3000000);
	
	FullMembership fullMembership = new FullMembership(id, LocalDate.now(), LocalDate.now(), "333333333");
	
	ServerResponse<FullMembership> serverResponse = RequestsSender.RegisterFullMembership(fullMembership);
	
	CPS_Tracer.TraceInformation(serverResponse.toString());
	
	ServerResponse<FullMembership> serverGetRespone = RequestsSender
		.GetFullMembership(serverResponse.GetResponseObject().GetSubscriptionId());
	
	CPS_Tracer.TraceInformation(serverGetRespone.toString());
	
	if (!serverGetRespone.GetRequestResult().equals(RequestResult.Succeed))
	{
	    return false;
	}
	
	return true;
    }
    
    private static boolean PartialMembershipTest()
    {
	String id = Integer.toString(new Random().nextInt(1000000) + 3000000);
	
	ArrayList<String> carList = new ArrayList<>();
	carList.add("444444444");
	carList.add("555555555");
	
	PartialMembership partialMembership = new PartialMembership(id, LocalDate.now(), LocalDate.now(), "testLot",
		carList, LocalTime.now());
	
	ServerResponse<PartialMembership> serverResponse = RequestsSender.RegisterPartialMembership(partialMembership);
	
	CPS_Tracer.TraceInformation(serverResponse.toString());
	
	ServerResponse<PartialMembership> serverGetRespone = RequestsSender
		.GetPartialMembership(serverResponse.GetResponseObject().GetSubscriptionId());
	
	CPS_Tracer.TraceInformation(serverGetRespone.toString());
	
	if (!serverGetRespone.GetRequestResult().equals(RequestResult.Succeed))
	{
	    return false;
	}
	
	return true;
    }
    
    private static boolean CustomerTest()
    {
	String id = Integer.toString(new Random().nextInt(1000000) + 3000000);
	
	Customer customer = new Customer(id, "test@gmail.com", 100);
	
	ServerResponse<Customer> serverResponse = RequestsSender.AddCustomerIfNotExists(customer);
	
	CPS_Tracer.TraceInformation(serverResponse.toString());
	
	ServerResponse<Customer> serverGetRespone = RequestsSender
		.GetCustomer(serverResponse.GetResponseObject().GetId());
	
	CPS_Tracer.TraceInformation(serverGetRespone.toString());
	
	if (!serverGetRespone.GetRequestResult().equals(RequestResult.Succeed))
	{
	    return false;
	}
	
	return true;
    }
}
