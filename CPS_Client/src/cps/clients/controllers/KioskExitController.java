package cps.clients.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import cps.clientServer.RequestResult;
import cps.clientServer.RequestsSender;
import cps.clientServer.ServerResponse;
import cps.entities.FullMembership;
import cps.entities.Parkinglot;
import cps.entities.PartialMembership;
import cps.entities.RemoveCarRequest;
import cps.entities.Reservation;
import cps.entities.enums.ReservationType;
import cps.utilities.Consts;
import cps.utilities.DialogBuilder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;

// TODO: Auto-generated Javadoc
/**
 * The Class KioskExitController.
 * Used for exiting the parking lot.
 */
public class KioskExitController extends BaseController
{
    
    @FXML
    private Label Headline;
    
    private ArrayList<String> PreOrderInputs = new ArrayList<>();
    
    private ArrayList<String> MemberInputs = new ArrayList<>();
    
    private String parkinglotName;
    
    private Parkinglot parkinglot;
    
    /**
     * Instantiates a new kiosk exit controller.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public KioskExitController() throws IOException
    {
	super();
	
	PreOrderInputs.add("Order id:");
	PreOrderInputs.add("Car number:");
	
	MemberInputs.add("Subscription id:");
	MemberInputs.add("Car number:");
	
	parkinglotName = new BufferedReader(
		new InputStreamReader(getClass().getResourceAsStream(Consts.ParkinglotNamePathFromController)))
			.readLine();
	
	parkinglot = RequestsSender.GetParkinglot(parkinglotName).GetResponseObject();
    }
    
    /**
     * Tells the robot to get the Member's car.
     *
     * @param event the event
     */
    @FXML
    void OnMemberExit(ActionEvent event)
    {
	Dialog<List<String>> dialog = DialogBuilder.InputsDialog(Consts.FillRequest, MemberInputs, Consts.Submit);
	
	Optional<List<String>> result = dialog.showAndWait();
	
	result.ifPresent(inputs ->
	{
	    ServerResponse<FullMembership> fullMemberResponse = RequestsSender.GetFullMembership(inputs.get(0));
	    ServerResponse<PartialMembership> partialMemberResponse = RequestsSender
		    .GetPartialMembership(inputs.get(0));
	    
	    if (fullMemberResponse.GetRequestResult().equals(RequestResult.Failed)
		    || partialMemberResponse.GetRequestResult().equals(RequestResult.Failed))
	    {
		DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
		return;
	    }
	    
	    if (fullMemberResponse.GetRequestResult().equals(RequestResult.NotFound)
		    && partialMemberResponse.GetRequestResult().equals(RequestResult.NotFound))
	    {
		DialogBuilder.AlertDialog(AlertType.ERROR, null, "Membership not found.", null, false);
		return;
	    }
	    
	    ServerResponse<RemoveCarRequest> removeResponse = RequestsSender
		    .RemoveCar(new RemoveCarRequest(parkinglotName, inputs.get(1)));
	    
	    if (removeResponse.GetRequestResult().equals(RequestResult.NotFound))
	    {
		DialogBuilder.AlertDialog(AlertType.ERROR, null, "Car not found.", null, false);
		return;
	    }
	    else if (removeResponse.GetRequestResult().equals(RequestResult.Succeed))
	    {
		DialogBuilder.AlertDialog(AlertType.INFORMATION, Consts.Approved, Consts.LeaveTheParkinglotMessage,
			null, false);
		
		myControllersManager.GoToHomePage(Consts.KioskExit);
	    }
	    else
	    {
		DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
		return;
	    }
	});
    }
    
    /**
     * Tells the robot to get the Guest's car.
     * Calculating the price and sets the payment scene.
     *
     * @param event the event
     */
    @FXML
    void OnGuestExit(ActionEvent event)
    {
	Dialog<List<String>> dialog = DialogBuilder.InputsDialog(Consts.FillRequest, PreOrderInputs, Consts.Submit);
	
	Optional<List<String>> result = dialog.showAndWait();
	
	result.ifPresent(inputs ->
	{
	    ServerResponse<Reservation> reservationResponse = RequestsSender.GetReservation(inputs.get(0));
	    
	    if (reservationResponse.GetRequestResult().equals(RequestResult.Failed))
	    {
		DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
		return;
	    }
	    
	    if (reservationResponse.GetRequestResult().equals(RequestResult.NotFound))
	    {
		DialogBuilder.AlertDialog(AlertType.ERROR, null, "Order not found.", null, false);
		return;
	    }
	    
	    if (reservationResponse.GetRequestResult().equals(RequestResult.Succeed)
		    && !reservationResponse.GetResponseObject().getCarNumber().equals(inputs.get(1)))
	    {
		DialogBuilder.AlertDialog(AlertType.ERROR, null, "Car not found.", null, false);
		return;
	    }
	    
	    ServerResponse<RemoveCarRequest> removeRequest = RequestsSender
		    .RemoveCar(new RemoveCarRequest(parkinglotName, inputs.get(1)));
	    if (removeRequest.GetRequestResult().equals(RequestResult.NotFound))
	    {
		DialogBuilder.AlertDialog(AlertType.ERROR, null, "Order not found.", null, false);
		return;
	    }
	    if (removeRequest.GetRequestResult().equals(RequestResult.Succeed))
	    {
		Reservation reservation = reservationResponse.GetResponseObject();
		
		if (reservation.getReservationType().equals(ReservationType.Local))
		{
		    Consumer<Void> afterPayment = Void ->
		    {
			Platform.runLater(() ->
			{
			    DialogBuilder.AlertDialog(AlertType.INFORMATION, Consts.Approved,
				    Consts.LeaveTheParkinglotMessage, null, false);
			    
			    myControllersManager.GoToHomePage(Consts.Payment);
			});
		    };
		    
		    float paymentAmount = LocalDateTime.of(reservation.getArrivalDate(), reservation.getArrivalHour())
			    .until(LocalDateTime.now(), ChronoUnit.HOURS) * parkinglot.getGuestRate();
		    
		    myControllersManager.Payment(reservation, paymentAmount, afterPayment, Consts.KioskExit);
		}
		else
		{
		    DialogBuilder.AlertDialog(AlertType.INFORMATION, Consts.Approved, Consts.LeaveTheParkinglotMessage,
			    null, false);
		    
		    myControllersManager.GoToHomePage(Consts.Payment);
		}
	    }
	    else
	    {
		DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
		return;
	    }
	    
	});
    }
    
    /**
     * Sets the previous scene.
     *
     * @param event the event
     */
    @FXML
    void OnBack(ActionEvent event)
    {
	myControllersManager.Back(PreviousScene, Consts.KioskExit);
    }
}
