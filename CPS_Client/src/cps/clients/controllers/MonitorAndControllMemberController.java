package cps.clients.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import cps.clientServer.RequestResult;
import cps.clientServer.RequestsSender;
import cps.clientServer.ServerResponse;
import cps.entities.FullMembership;
import cps.entities.Parkinglot;
import cps.entities.PartialMembership;
import cps.utilities.Consts;
import cps.utilities.DialogBuilder;
import cps.utilities.InputValidator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

// TODO: Auto-generated Javadoc
/**
 * The Class MonitorAndControllMemberController.
 * Used for getting a membership's information.
 */
public class MonitorAndControllMemberController extends BaseController
{
    
    private ArrayList<String> subscriptionTypes = new ArrayList<>();
    
    String fullOrPartialMembership;
    
    float rate = 5;
    
    /**
     * Instantiates a new monitor and controll member controller.
     */
    public MonitorAndControllMemberController()
    {
	subscriptionTypes.add(Consts.Payment);
    }
    
    /** The Headline. */
    @FXML
    private Label Headline;
    
    /** The prg bar. */
    @FXML
    private ProgressBar prgBar;
    
    /** The Subscription renewal. */
    @FXML
    private Button SubscriptionRenewal;
    
    /** The Text member deatil. */
    @FXML
    private TextArea TextMemberDeatil;
    
    /** The Subscription ID. */
    @FXML
    private TextField Subscription_ID;
    
    /** The full mebership changed. */
    FullMembership fullMebershipChanged = null;
    
    /** The parial mebership changed. */
    PartialMembership parialMebershipChanged = null;
    
    /**
     * Prints the selected membership's information.
     *
     * @param event
     * 			 the event
     */
    @FXML
    void OnSubmit(ActionEvent event)
    {
	SubscriptionRenewal.setDisable(true);
	
	TextMemberDeatil.clear();
	
	if (!InputValidator.OrderId(Subscription_ID.getText()))
	{
	    DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.InputsAreIncorrect, null, false);
	    
	    return;
	}
	
	SetfullOrPartialMembership();
	
	prgBar.setVisible(true);
	
	CompletableFuture.runAsync(() ->
	{
	    ServerResponse<FullMembership> monitorAndControllfullmembership = RequestsSender
		    .GetFullMembership(Subscription_ID.getText());
	    
	    ServerResponse<PartialMembership> monitorAndControllPartialMember = RequestsSender
		    .GetPartialMembership(Subscription_ID.getText());
	    
	    Platform.runLater(() ->
	    {
		if (monitorAndControllfullmembership.GetRequestResult().equals(RequestResult.Failed))
		{
		    DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
		    
		}
		else if (monitorAndControllfullmembership.GetRequestResult().equals(RequestResult.NotFound))
		{
		    if (monitorAndControllPartialMember.GetRequestResult().equals(RequestResult.Failed))
		    {
			DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
		    }
		    else if (monitorAndControllPartialMember.GetRequestResult().equals(RequestResult.NotFound))
		    {
			DialogBuilder.AlertDialog(AlertType.ERROR, null, "Sorry, your membership was not found", null,
				false);
		    }
		}
		
		if (monitorAndControllfullmembership.GetRequestResult().equals(RequestResult.Succeed))
		{
		    TextMemberDeatil.setText(monitorAndControllfullmembership.GetResponseObject().toString());
		    SubscriptionRenewal.setDisable(false);
		    
		}
		else if (monitorAndControllPartialMember.GetRequestResult().equals(RequestResult.Succeed))
		{
		    TextMemberDeatil.setText(monitorAndControllPartialMember.GetResponseObject().toString());
		    SubscriptionRenewal.setDisable(false);
		}
		
		prgBar.setVisible(false);
	    });
	    
	});
	
    }
    
    /**
     * Renews a membership for an additional month.
     * Calculates the price of the renewal and sets the payment scene.
     *
     * @param event 
     * 			the event
     */
    @FXML
    void OnSubscriptionRenewal(ActionEvent event)
    {
	float paymentAmount = 0;
	
	String newDate = LocalDate.now().plusDays(28).toString();
	newDate = "The expiration of the new subscription is: " + newDate;
	String buttonResult = DialogBuilder.AlertDialog(AlertType.NONE, "Renewal Subscription", newDate,
		subscriptionTypes, true);
	
	if (buttonResult == Consts.Payment)
	{
	    SubscriptionRenewal.setDisable(true);
	    
	    if (fullOrPartialMembership.equals("fullMembership"))
	    {
		ServerResponse<FullMembership> getFullMembership = RequestsSender
			.GetFullMembership(Subscription_ID.getText());
		if (getFullMembership.GetRequestResult().equals(RequestResult.Failed))
		{
		    DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
		    return;
		}
		fullMebershipChanged = getFullMembership.GetResponseObject();
		paymentAmount = 72 * rate;
	    }
	    else if (fullOrPartialMembership.equals("partialMembership"))
	    {
		ServerResponse<PartialMembership> getpartialMembership = RequestsSender
			.GetPartialMembership(Subscription_ID.getText());
		if (getpartialMembership.GetRequestResult().equals(RequestResult.Failed))
		{
		    DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
		    return;
		}
		parialMebershipChanged = getpartialMembership.GetResponseObject();
		paymentAmount = AmountToPay(parialMebershipChanged);
	    }
	    Consumer<Void> afterPayment = Void ->
	    {
		if (fullOrPartialMembership.equals("fullMembership"))
		{
		    ServerResponse<FullMembership> ChangeExpiryDateFull = RequestsSender
			    .ChangeExpireFullMembership(fullMebershipChanged);
		    if (ChangeExpiryDateFull.GetRequestResult().equals(RequestResult.Failed))
		    {
			Platform.runLater(() ->
			{
			    DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
			    return;
			});
			
		    }
		}
		else if (fullOrPartialMembership.equals("partialMembership"))
		{
		    ServerResponse<PartialMembership> ChangeExpiryDatePartial = RequestsSender
			    .ChangeExpirePartialMembership(parialMebershipChanged);
		    if (ChangeExpiryDatePartial.GetRequestResult().equals(RequestResult.Failed))
		    {
			Platform.runLater(() ->
			{
			    DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
			    return;
			});
		    }
		}
		
		Platform.runLater(() ->
		{
		    DialogBuilder.AlertDialog(AlertType.INFORMATION, Consts.Approved, Consts.SubscriptionRenewal, null,
			    false);
		    
		    myControllersManager.GoToHomePage(Consts.Payment);
		});
	    };
	    
	    if (fullOrPartialMembership.equals("fullMembership"))
	    {
		
		myControllersManager.Payment(fullMebershipChanged, paymentAmount, afterPayment,
			Consts.MonitorAndControllMember);
	    }
	    else if (fullOrPartialMembership.equals("partialMembership"))
	    {
		
		myControllersManager.Payment(parialMebershipChanged, paymentAmount, afterPayment,
			Consts.MonitorAndControllMember);
	    }
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
	SubscriptionRenewal.setDisable(true);
	myControllersManager.Back(PreviousScene, Consts.MonitorAndControllMember);
    }
    
    /**
     * Checks if the membership is full or partial. 
     */
    void SetfullOrPartialMembership()
    {
	int savedValue = Integer.parseInt(Subscription_ID.getText());
	if (savedValue >= 1000000 && savedValue < 2000000)
	{
	    this.fullOrPartialMembership = "fullMembership";
	    return;
	}
	else if (savedValue > 2000000 && savedValue < 3000000)
	{
	    this.fullOrPartialMembership = "partialMembership";
	    return;
	    
	}
    }
    
    /**
     * Calculates the price of a partial membership renewal.
     *
     * @param partialMember
     *            the partial member
     * @return the float
     */
    private float AmountToPay(PartialMembership partialMember)
    {
	float cars = partialMember.GetCarList().size();
	float hours = 0;
	hours = (cars > 1 ? 54 : 60);
	String parkinglot = partialMember.GetParkinglot();
	ServerResponse<Parkinglot> getParkingLot = RequestsSender.GetParkinglot(parkinglot);
	rate = getParkingLot.GetResponseObject().getInAdvanceRate();
	return (cars * hours * rate);
	
    }
    
}
