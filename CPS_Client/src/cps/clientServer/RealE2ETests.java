package cps.clientServer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;

import cps.entities.ActivityReport;
import cps.entities.AddRealTimeParkingRequest;
import cps.entities.ChangeParkingSpotStatusRequest;
import cps.entities.ChangeParkinglotStatusRequest;
import cps.entities.ChangeRatesRequest;
import cps.entities.ChangeRatesResponse;
import cps.entities.CloseComplaintRequest;
import cps.entities.Complaint;
import cps.entities.ComplaintsReport;
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
import cps.entities.enums.ParkingSpotStatus;
import cps.entities.enums.ParkinglotStatus;
import cps.entities.enums.ReservationStatus;
import cps.entities.enums.ReservationType;
import cps.utilities.CPS_Tracer;
import cps.utilities.Pdf_Builder;

// TODO: Auto-generated Javadoc
/**
 * The Class RealE2ETests. This class is used for End to End tests of the application.
 */
@SuppressWarnings("unused")
public class RealE2ETests
{
    
    /**
     * The main method - tests entry.
     *
     * @param args the arguments
     */
    public static void main(String[] args)
    {
	try
	{
	    new RequestsSender("127.0.0.1");
	    
	    // FullMembershipTest() PartialMembershipTest() ComplaintTest()
	    // CustomerTest() ReservationTest() ChangeRatesTest()
	    // EmployeeTest() ParkinglotTest() DisabledParkingSpotsTest()
	    // GuestEntryTest()
	    //ComplaintsReportTest();
	    //DisabledReportTest();
	    //PerformanceReportTest()
	    //ReservationReportTest()
	    //ActivityReportTest()
	    //StatusReportTest()
	    for (int i = 0; i < 1; i++)
	    {
		if (ActivityReportTest())
		{
		    System.out.println("Test Succeed");
		}
		else
		{
		    System.out.println("Test Failed");
		}
	    }
	    
	}
	catch (Exception e)
	{
	    System.out.println("Failed with exception: " + e);
	    e.printStackTrace();
	}
    }
    
    private static boolean EntryAndRemoveDynamicTest()
    {
	ServerResponse<AddRealTimeParkingRequest> serverResponse = null;
	
	for (int i = 0; i < 1; i++)
	{
	    AddRealTimeParkingRequest request = new AddRealTimeParkingRequest("Test lot", LocalDateTime.now(),
		    LocalDateTime.now().plusHours(5), "333333", true);
	    
	    serverResponse = RequestsSender.TryInsertCar(request);
	    
	    CPS_Tracer.TraceInformation("server respnse after trying to add car: \n" + serverResponse);
	}
	
	if (!serverResponse.GetRequestResult().equals(RequestResult.Succeed))
	{
	    return false;
	}
	return true;
    }
    
    private static boolean DisabledParkingSpotsTest()
    {
	ChangeParkingSpotStatusRequest request = new ChangeParkingSpotStatusRequest(new ParkingSpot(1, 1, 2),
		"Test lot", ParkingSpotStatus.Disabled);
	
	ServerResponse<ChangeParkingSpotStatusRequest> serverResponse = RequestsSender.ChangeParkingSpotStatus(request);
	
	if (!serverResponse.GetRequestResult().equals(RequestResult.Succeed))
	{
	    return false;
	}
	
	ServerResponse<ArrayList<ParkingSpot>> serverResponse2 = RequestsSender.GetAllDisabledParkingSpots();
	
	CPS_Tracer.TraceInformation(serverResponse2.toString());
	
	if (!serverResponse2.GetRequestResult().equals(RequestResult.Succeed))
	{
	    return false;
	}
	
	request = new ChangeParkingSpotStatusRequest(new ParkingSpot(1, 1, 2), "Test lot", ParkingSpotStatus.Active);
	
	ServerResponse<ChangeParkingSpotStatusRequest> serverResponse3 = RequestsSender
		.ChangeParkingSpotStatus(request);
	
	if (!serverResponse3.GetRequestResult().equals(RequestResult.Succeed))
	{
	    return false;
	}
	
	return true;
    }
    
    private static boolean ChangeRatesTest()
    {
	ChangeRatesRequest changeRatesRequest = new ChangeRatesRequest("Test lot", 55, 55);
	
	ServerResponse<ChangeRatesRequest> serverResponse = RequestsSender.AddChangeRatesRequest(changeRatesRequest);
	
	ServerResponse<ArrayList<ChangeRatesRequest>> serverListResponse = RequestsSender.GetAllChangeRatesRequests();
	
	System.out.println(serverListResponse.GetResponseObject());
	
	ChangeRatesResponse changeRatesResponse = new ChangeRatesResponse(
		serverResponse.GetResponseObject().getRequestId(), true);
	
	ServerResponse<ChangeRatesResponse> serverChangeResponse = RequestsSender
		.CloseChangeRatesRequest(changeRatesResponse);
	
	if (!serverChangeResponse.GetRequestResult().equals(RequestResult.Succeed))
	{
	    return false;
	}
	
	return true;
    }
    
    private static boolean ComplaintTest()
    {
	Complaint complaint = new Complaint("301731469",
		"My Ferari got dirty with dust in Nosh parkinglot.. \nI will not bring my Lamburgini unless u compensate me in 1,000$ !!!");
	
	ServerResponse<Complaint> serverResponse = RequestsSender.AddComplaint(complaint);
	
	ServerResponse<ArrayList<Complaint>> serverGetResponse = RequestsSender.GetAllActiveComplaints();
	
	CPS_Tracer.TraceInformation(serverGetResponse.toString());
	;
	
	boolean isMyComplaintThere = false;
	
	for (Complaint c : serverGetResponse.GetResponseObject())
	{
	    if (c.getComplaintId().equals(serverResponse.GetResponseObject().getComplaintId()))
	    {
		isMyComplaintThere = true;
	    }
	}
	
	if (!isMyComplaintThere)
	{
	    return false;
	}
	
	CloseComplaintRequest closeComplaintRequest = new CloseComplaintRequest(
		serverResponse.GetResponseObject().getComplaintId(), 2000);
	
	ServerResponse<CloseComplaintRequest> serverResponse2 = RequestsSender.CloseComplaint(closeComplaintRequest);
	
	if (!serverResponse2.GetRequestResult().equals(RequestResult.Succeed))
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
	
	ServerResponse<Parkinglot> serverGetResponse = RequestsSender.GetParkinglot("Test lot" + seed);
	
	ServerResponse<ChangeParkinglotStatusRequest> serverResponse2 = RequestsSender.ChangeParkinglotStatus(
		new ChangeParkinglotStatusRequest(serverGetResponse.GetResponseObject().getParkinglotName(),
			ParkinglotStatus.Closed));
	
	ServerResponse<ArrayList<Parkinglot>> serverGetAllResponse = RequestsSender.GetAllParkinglots(false);
	
	CPS_Tracer.TraceInformation(serverGetAllResponse.toString());
	
	serverGetResponse = RequestsSender.GetParkinglot("Testlot" + seed);
	
	if (!serverResponse2.GetRequestResult().equals(RequestResult.Succeed)
		|| !serverGetResponse.GetResponseObject().getStatus().equals(ParkinglotStatus.Closed))
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
	
	Reservation reservation = new Reservation(ReservationType.Web, id, "Test lot", "333333", LocalDate.now(),
		LocalDate.now().plusDays(1), LocalTime.parse("11:11"), LocalTime.parse("11:11"), ReservationStatus.NotStarted, 55);
	
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
	
	PartialMembership partialMembership = new PartialMembership(id, LocalDate.now(), LocalDate.now().plusDays(20),
		"Test lot", carList, LocalTime.now());
	
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
    
    private static boolean ComplaintsReportTest()
    {
	String id = Integer.toString(new Random().nextInt(1000000) + 3000000);
	
	ServerResponse<ComplaintsReport> serverResponse = RequestsSender.GetComplaintsReport();
	
	CPS_Tracer.TraceInformation(serverResponse.toString());
	
	System.out.println(serverResponse.GetResponseObject().getComplaintAmount() + " " + serverResponse.GetResponseObject().getHandledComplaints() + "\n");
	
	if (!serverResponse.GetRequestResult().equals(RequestResult.Succeed))
	{
	    return false;
	}
	
	return true;
    }
    
    private static boolean DisabledReportTest()
    {
	String id = Integer.toString(new Random().nextInt(1000000) + 3000000);
	
	ServerResponse<DisabledReport> serverResponse = RequestsSender.GetDisabledReport("all");
	
	CPS_Tracer.TraceInformation(serverResponse.toString());
	
	System.out.println(serverResponse.GetResponseObject().getDisabledAmount() + " \n" + serverResponse.GetResponseObject().getActiveList() + " \n" + serverResponse.GetResponseObject().getDisabledList()+"\n");
	
	if (!serverResponse.GetRequestResult().equals(RequestResult.Succeed))
	{
	    return false;
	}
	
	return true;
    }
    
    private static boolean PerformanceReportTest()
    {
	ServerResponse<PerformanceReport> serverResponse = RequestsSender.GetPerformanceReport();
	
	CPS_Tracer.TraceInformation(serverResponse.toString());
	
	System.out.println(serverResponse.GetResponseObject().getMembershipAmount() + " \n" + serverResponse.GetResponseObject().getMembersMultipleCars() + "\n");
	
	if (!serverResponse.GetRequestResult().equals(RequestResult.Succeed))
	{
	    return false;
	}
	
	return true;
    }
    
    private static boolean ReservationReportTest()
    {
	ServerResponse<ReservationReport> serverResponse = RequestsSender.GetReservationReport("all");
	
	CPS_Tracer.TraceInformation(serverResponse.toString());
	
	System.out.println(serverResponse.GetResponseObject().getReservationAmount() + " \n" + serverResponse.GetResponseObject().getReservationExcersied() + "\n" + serverResponse.GetResponseObject().getReservationCancelled() + "\n" + serverResponse.GetResponseObject().getGuestList() + "\n" + serverResponse.GetResponseObject().getInAdvanceList() + "\n");
	
	if (!serverResponse.GetRequestResult().equals(RequestResult.Succeed))
	{
	    return false;
	}
	
	return true;
    }
    
    private static boolean ActivityReportTest()
    {
    LocalDate date = LocalDate.of(2017, 4, 1);
	ServerResponse<ActivityReport> serverResponse = RequestsSender.GetActivityReport(date);
	
	CPS_Tracer.TraceInformation(serverResponse.toString());
	
	System.out.println(serverResponse.GetResponseObject().getArrExercised() + " \n" + serverResponse.GetResponseObject().getArrCancelled() + "\n" + serverResponse.GetResponseObject().getArrDisabled() + "\n" + serverResponse.GetResponseObject().getMedianExercised() + "\n" + serverResponse.GetResponseObject().getMedianCancelled() + "\n" + 
	serverResponse.GetResponseObject().getMedianDisabled() + " \n" + serverResponse.GetResponseObject().getDeviationExercised() + "\n" + serverResponse.GetResponseObject().getDeviationCancelled() + "\n" + serverResponse.GetResponseObject().getDeviationDisabled() + "\n");
	
	if (!serverResponse.GetRequestResult().equals(RequestResult.Succeed))
	{
	    return false;
	}
	
	return true;
    }
    
    private static boolean StatusReportTest()
    {
	ServerResponse<StatusReport> serverResponse = RequestsSender.GetStatusReport();
	
	CPS_Tracer.TraceInformation(serverResponse.toString());
	
	System.out.println(serverResponse.GetResponseObject().getTable() + "\n");
	
	Pdf_Builder pdf = new Pdf_Builder("enter directory path here");
	
	pdf.build(serverResponse.GetResponseObject());
	
	if (!serverResponse.GetRequestResult().equals(RequestResult.Succeed))
	{
	    return false;
	}
	
	return true;
    }
}
