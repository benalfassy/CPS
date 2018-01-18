package cps.clients.controllers;

import java.time.LocalDate;
import java.util.function.Consumer;

import cps.clientServer.RequestResult;
import cps.clientServer.RequestsSender;
import cps.clientServer.ServerResponse;
import cps.entities.Customer;
import cps.entities.FullMembership;
import cps.utilities.Consts;
import cps.utilities.DialogBuilder;
import cps.utilities.InputValidator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

// TODO: Auto-generated Javadoc
/**
 * The Class FullMembershipRegisterController. 
 * Used for registering for a full membership.
 */
public class FullMembershipRegisterController extends BaseController
{
    
    @FXML
    private DatePicker startingDatePicker;
    
    @FXML
    private TextField carNumber;
    
    @FXML
    private Label Headline;
    
    @FXML
    private TextField id;
    
    @FXML
    private TextField email;
    
    FullMembership fullMembership;
    
    Customer customer;
    
    private final int rate = 5;
    
    /**
     * Initialize.
     */
    @FXML
    void initialize()
    {
	startingDatePicker.setEditable(true);
	startingDatePicker.setValue(LocalDate.now());
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
	myControllersManager.Back(PreviousScene, Consts.FullMembershipRegister);
	
    }
    
    /**
     * Calculates the price of the membership and sets the payment controller.
     * After approving the payment, creates the new full membership.
     *
     * @param event
     *            the event
     */
    @FXML
    void OnSubmitAndPay(ActionEvent event)
    {
	
	float paymentAmount = 72 * rate;
	
	if (!IsInputLegal())
	{
	    DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.InputsAreIncorrect, null, false);
	    return;
	}
	
	fullMembership = new FullMembership(id.getText(), startingDatePicker.getValue(),
		startingDatePicker.getValue().plusDays(28), carNumber.getText());
	
	customer = new Customer(id.getText(), email.getText(), 0);
	
	Consumer<Void> afterPayment = Void ->
	{
	    ServerResponse<FullMembership> registerFullMembershipResponse = RequestsSender
		    .RegisterFullMembership(fullMembership);
	    
	    ServerResponse<Customer> AddCustomerIfNotExist = RequestsSender.AddCustomerIfNotExists(customer);
	    
	    Platform.runLater(() ->
	    {
		if (registerFullMembershipResponse.GetRequestResult().equals(RequestResult.Failed)
			|| AddCustomerIfNotExist.GetRequestResult().equals(RequestResult.Failed))
		{
		    DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
		    
		    return;
		}
		
		if (registerFullMembershipResponse.GetRequestResult().equals(RequestResult.AlredyExist))
		{
		    DialogBuilder.AlertDialog(AlertType.ERROR, null, "We alreay have a full membership client signed with this car.", null, false);
		    
		    return;
		}
		
		DialogBuilder
			.AlertDialog(AlertType.INFORMATION, Consts.Approved,
				Consts.ThankYouForRegistering + "\n Your subscription ID : "
					+ registerFullMembershipResponse.GetResponseObject().GetSubscriptionId(),
				null, false);
		
		myControllersManager.GoToHomePage(Consts.Payment);
	    });
	    
	};
	
	myControllersManager.Payment(fullMembership, paymentAmount, afterPayment, Consts.FullMembershipRegister);
    }
    
    /**
     * Checks if the input is legal.
     *
     * @return True, if successful
     */
    private boolean IsInputLegal()
    {
	boolean result = true;
	
	if (!InputValidator.CarNumber(carNumber.getText()))
	{
	    result = false;
	    carNumber.setStyle("-fx-background-color: tomato;");
	}
	else
	{
	    carNumber.setStyle("-fx-background-color: white;");
	}
	
	if (!InputValidator.Email(email.getText()))
	{
	    result = false;
	    email.setStyle("-fx-background-color: tomato;");
	}
	else
	{
	    email.setStyle("-fx-background-color: white;");
	}
	
	if (!InputValidator.Id(id.getText()))
	{
	    result = false;
	    id.setStyle("-fx-background-color: tomato;");
	}
	else
	{
	    id.setStyle("-fx-background-color: white;");
	}
	
	if (!InputValidator.StartingDate(startingDatePicker.getValue()))
	{
	    result = false;
	    startingDatePicker.setStyle("-fx-background-color: tomato;");
	}
	else
	{
	    startingDatePicker.setStyle("-fx-background-color: white;");
	}
	
	return result;
    }
}
