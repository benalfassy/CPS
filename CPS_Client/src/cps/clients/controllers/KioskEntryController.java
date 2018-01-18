package cps.clients.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import cps.clientServer.RequestResult;
import cps.clientServer.RequestsSender;
import cps.clientServer.ServerResponse;
import cps.entities.AddRealTimeParkingRequest;
import cps.entities.FullMembership;
import cps.entities.PartialMembership;
import cps.entities.Reservation;
import cps.entities.enums.ReservationStatus;
import cps.entities.enums.ReservationType;
import cps.utilities.Consts;
import cps.utilities.DialogBuilder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressBar;

// TODO: Auto-generated Javadoc
/**
 * The Class KioskEntryController. 
 * Used for entering the parking lot.
 */
public class KioskEntryController extends BaseController
{
    
    @FXML
    private ProgressBar prgBarPreOrder;
    
    @FXML
    private ProgressBar prgBarMember;
    
    private ArrayList<String> PreOrderInputs = new ArrayList<>();
    
    private ArrayList<String> MemberInputs = new ArrayList<>();
    
    private ArrayList<String> subscriptionTypes = new ArrayList<>();
    
    private String parkinglot;
    
    /**
     * Instantiates a new kiosk entry controller.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public KioskEntryController() throws IOException
    {
	super();
	
	PreOrderInputs.add("Order id:");
	PreOrderInputs.add("Car number:");
	
	MemberInputs.add("Subscription id:");
	MemberInputs.add("Car number:");
	
	subscriptionTypes.add(Consts.FullMembership);
	subscriptionTypes.add(Consts.PartialMembership);
	
	parkinglot = new BufferedReader(
		new InputStreamReader(getClass().getResourceAsStream(Consts.ParkinglotNamePathFromController)))
			.readLine();
    }
    
    /**
     * Sets the guest entry scene.
     *
     * @param event
     *            the event
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws URISyntaxException
     *             the URI syntax exception
     */
    @FXML
    void OnGuestEntry(ActionEvent event) throws IOException, URISyntaxException
    {
	myControllersManager.SetScene(Consts.GuestEntry, Consts.KioskEntry);
    }
    
    /**
     * Opens a dialog window and tells the robot to park the client's car, according to the details filled by the client.
     *
     * @param event
     *            the event
     */
    @FXML
    void OnPreOrderEntry(ActionEvent event)
    {
	Dialog<List<String>> dialog = DialogBuilder.InputsDialog(Consts.FillRequest, PreOrderInputs, Consts.Submit);
	
	Optional<List<String>> result = dialog.showAndWait();
	
	prgBarPreOrder.setVisible(true);
	
	result.ifPresent(inputs ->
	{
	    CompletableFuture.runAsync(() ->
	    {
		ServerResponse<Reservation> reservationResponse = RequestsSender.GetReservation(inputs.get(0));
		
		Reservation reservation = reservationResponse.GetResponseObject();
		
		Platform.runLater(() ->
		{
		    prgBarPreOrder.setVisible(false);
		    
		    if (reservationResponse.GetRequestResult().equals(RequestResult.Failed))
		    {
			DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
			return;
		    }
		    
		    if (reservationResponse.GetRequestResult().equals(RequestResult.NotFound)
			    || !inputs.get(1).equals(reservationResponse.GetResponseObject().getCarNumber())
			    || !reservationResponse.GetResponseObject().getReservationStatus()
				    .equals(ReservationStatus.NotStarted))
		    {
			DialogBuilder.AlertDialog(AlertType.ERROR, null, "No reservation with these details was found.",
				null, false);
			return;
		    }
		    
		    if (reservationResponse.GetRequestResult().equals(RequestResult.Succeed))
		    {
			if (!reservation.getArrivalDate().equals(LocalDate.now())
				|| !reservation.getArrivalHour().isBefore(LocalTime.now()))
			{
			    
			    DialogBuilder.AlertDialog(AlertType.ERROR, null,
				    "You have a reservation, but not for now. \nYou can check your reservation details on the main page.",
				    null, false);
			    return;
			}
		    }
		    if (reservationResponse.GetRequestResult().equals(RequestResult.Succeed)
			    && inputs.get(1).equals(reservationResponse.GetResponseObject().getCarNumber())
			    && !reservation.getParkingLot().equals(parkinglot))
		    {
			DialogBuilder.AlertDialog(AlertType.ERROR, null,
				"You have a reservation, but not for this parking lot. \nYou can check your reservation details on the main page.",
				null, false);
			return;
		    }
		    if (reservationResponse.GetRequestResult().equals(RequestResult.Succeed)
			    && inputs.get(1).equals(reservationResponse.GetResponseObject().getCarNumber()))
		    {
			
			if (reservation.getReservationType().equals(ReservationType.Web)
				&& reservation.getArrivalHour().plusMinutes(30).isBefore(LocalTime.now()))
			{
			    float paymentAmount = (float) (reservation.getPrice() * 0.2);
			    
			    Consumer<Void> afterPayment = Void ->
			    {
				AddRealTimeParkingRequest request = new AddRealTimeParkingRequest(parkinglot,
					LocalDateTime.now(),
					LocalDateTime.of(reservation.getLeavingDate(), reservation.getLeavingHour()),
					reservation.getCarNumber(), false);
				
				ServerResponse<AddRealTimeParkingRequest> insertResponse = RequestsSender
					.TryInsertCar(request);
				Platform.runLater(() ->
				{
				    if (insertResponse.GetRequestResult().equals(RequestResult.Succeed))
				    {
					
					DialogBuilder.AlertDialog(AlertType.INFORMATION, Consts.Approved,
						Consts.LeaveTheCarMessage, null, false);
					
					myControllersManager.GoToHomePage(Consts.Payment);
					return;
				    }
				    else
				    {
					DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage,
						null, false);
					return;
				    }
				});
			    };
			    myControllersManager.Payment(reservation, paymentAmount, afterPayment, Consts.KioskEntry);
			}
			else
			{
			    
			    AddRealTimeParkingRequest request = new AddRealTimeParkingRequest(parkinglot,
				    LocalDateTime.now(),
				    LocalDateTime.of(reservation.getLeavingDate(), reservation.getLeavingHour()),
				    reservation.getCarNumber(), false);
			    
			    ServerResponse<AddRealTimeParkingRequest> insertResponse = RequestsSender
				    .TryInsertCar(request);
			    if (insertResponse.GetRequestResult().equals(RequestResult.Succeed))
			    {
				
				DialogBuilder.AlertDialog(AlertType.INFORMATION, Consts.Approved,
					Consts.LeaveTheCarMessage, null, false);
				
				myControllersManager.GoToHomePage(Consts.Payment);
				return;
			    }
			    else
			    {
				DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null,
					false);
				return;
			    }
			}
		    }
		});
	    });
	});
    }
    
    /**
     * Opens a dialog window with the options for a full or a partial membership, and calls the handle function.
     *
     * @param event
     *            the event
     */
    @FXML
    void OnMemberEntry(ActionEvent event)
    {
	String buttonResult = DialogBuilder.AlertDialog(AlertType.NONE, "Register", "Please choose a subscription type",
		subscriptionTypes, true);
	
	switch (buttonResult)
	{
	case Consts.FullMembership:
	    HandleFullMember();
	    break;
	
	case Consts.PartialMembership:
	    HandlePartialMember();
	    break;
	
	default:
	    break;
	}
    }
    
    /**
     * Sets the previous scene.
     *
     * @param event
     *            the event
     */
    @FXML
    void OnBack(ActionEvent event)
    {
	myControllersManager.Back(PreviousScene, Consts.KioskEntry);
    }
    
    private void HandleFullMember()
    {
	Dialog<List<String>> dialog = DialogBuilder.InputsDialog(Consts.FillRequest, MemberInputs, Consts.Submit);
	
	Optional<List<String>> result = dialog.showAndWait();
	
	result.ifPresent(inputs ->
	{
	    
	    prgBarMember.setVisible(true);
	    
	    CompletableFuture.runAsync(() ->
	    {
		ServerResponse<FullMembership> fullMmembershipResponse = RequestsSender
			.GetFullMembership(inputs.get(0));
		
		FullMembership fullMembership = fullMmembershipResponse.GetResponseObject();
		
		Platform.runLater(() ->
		{
		    prgBarMember.setVisible(false);
		    
		    if (fullMmembershipResponse.GetRequestResult().equals(RequestResult.Failed))
		    
		    {
			DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
			return;
		    }
		    
		    if (fullMmembershipResponse.GetRequestResult().equals(RequestResult.NotFound)
			    || !fullMmembershipResponse.GetResponseObject().GetCarNumber().equals(inputs.get(1)))
		    
		    {
			DialogBuilder.AlertDialog(AlertType.ERROR, null, "No membership with these details was found.",
				null, false);
			return;
		    }
		    
		    if (fullMmembershipResponse.GetRequestResult().equals(RequestResult.Succeed)
			    && LocalDate.now().isAfter(fullMembership.getExpiryDate()))
		    {
			DialogBuilder.AlertDialog(AlertType.ERROR, null,
				"Your membership has expired. \nYou can renew on the main page -> Monitor and Control.",
				null, false);
			return;
		    }
		    
		    if (fullMmembershipResponse.GetRequestResult().equals(RequestResult.Succeed))
		    {
			LocalDateTime expiry = LocalDateTime.of(fullMembership.getExpiryDate(),
				LocalTime.parse("00:00"));
			
			LocalDateTime parkingExitDateTime = LocalDateTime.now().plusDays(14).isAfter(expiry) ? expiry
				: LocalDateTime.now().plusDays(14);
			
			AddRealTimeParkingRequest request = new AddRealTimeParkingRequest(parkinglot,
				LocalDateTime.now(), parkingExitDateTime, fullMembership.GetCarNumber(), false);
			
			ServerResponse<AddRealTimeParkingRequest> insertResponse = RequestsSender.TryInsertCar(request);
			
			if (insertResponse.GetRequestResult().equals(RequestResult.Succeed))
			{
			    
			    DialogBuilder.AlertDialog(AlertType.INFORMATION, Consts.Approved, Consts.LeaveTheCarMessage,
				    null, false);
			    
			    myControllersManager.GoToHomePage(Consts.KioskEntry);
			}
			else if (insertResponse.GetRequestResult().equals(RequestResult.AlredyExist))
			{
			    DialogBuilder.AlertDialog(AlertType.ERROR, null, "Your car is already in the parking lot.",
				    null, false);
			    return;
			}
			else
			{
			    DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
			    return;
			}
		    }
		});
	    });
	});
    }
    
    private void HandlePartialMember()
    {
	Dialog<List<String>> dialog = DialogBuilder.InputsDialog(Consts.FillRequest, MemberInputs, Consts.Submit);
	
	Optional<List<String>> result = dialog.showAndWait();
	
	result.ifPresent(inputs ->
	{
	    prgBarMember.setVisible(true);
	    
	    CompletableFuture.runAsync(() ->
	    {
		ServerResponse<PartialMembership> partialMembershipResponse = RequestsSender
			.GetPartialMembership(inputs.get(0));
		
		String carNumber = inputs.get(1);
		
		PartialMembership partialMembership = partialMembershipResponse.GetResponseObject();
		
		Platform.runLater(() ->
		{
		    prgBarMember.setVisible(false);
		    
		    if (partialMembershipResponse.GetRequestResult().equals(RequestResult.Failed))
		    
		    {
			DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
			return;
		    }
		    
		    if (partialMembershipResponse.GetRequestResult().equals(RequestResult.NotFound)
			    || (partialMembershipResponse.GetRequestResult().equals(RequestResult.Succeed)
				    && !partialMembership.GetCarList().contains(carNumber)))
		    
		    {
			DialogBuilder.AlertDialog(AlertType.ERROR, null, "No membership with these details was found.",
				null, false);
			return;
		    }
		    
		    if (partialMembershipResponse.GetRequestResult().equals(RequestResult.Succeed)
			    && LocalDate.now().isAfter(partialMembership.getExpiryDate()))
		    {
			DialogBuilder.AlertDialog(AlertType.ERROR, null,
				"Your membership has expired. \nYou can renew on the main page -> Monitor and Control.",
				null, false);
			return;
		    }
		    
		    if (partialMembershipResponse.GetRequestResult().equals(RequestResult.Succeed)
			    && (LocalDate.now().getDayOfWeek().name().equals("FRIDAY")
				    || (LocalDate.now().getDayOfWeek().name().equals("SATURDAY"))))
		    {
			
			DialogBuilder.AlertDialog(AlertType.ERROR, null,
				"You can not enter the parking lot during the weekend.", null, false);
			return;
		    }
		    
		    if (partialMembershipResponse.GetRequestResult().equals(RequestResult.Succeed))
		    {
			AddRealTimeParkingRequest request = new AddRealTimeParkingRequest(parkinglot,
				LocalDateTime.now(), LocalDateTime.of(LocalDate.now(), partialMembership.GetExitTime()),
				inputs.get(1), false);
			
			ServerResponse<AddRealTimeParkingRequest> insertResponse = RequestsSender.TryInsertCar(request);
			
			if (insertResponse.GetRequestResult().equals(RequestResult.Succeed))
			{
			    
			    DialogBuilder.AlertDialog(AlertType.INFORMATION, Consts.Approved, Consts.LeaveTheCarMessage,
				    null, false);
			    
			    myControllersManager.GoToHomePage(Consts.KioskEntry);
			}
			else if (insertResponse.GetRequestResult().equals(RequestResult.AlredyExist))
			{
			    DialogBuilder.AlertDialog(AlertType.ERROR, null, "Your car is already in the parking lot.",
				    null, false);
			    return;
			}
			else
			{
			    DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
			    return;
			}
		    }
		});
		
	    });
	    
	});
    }
}
