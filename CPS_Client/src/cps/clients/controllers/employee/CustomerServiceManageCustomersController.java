package cps.clients.controllers.employee;

import java.util.concurrent.CompletableFuture;

import cps.clientServer.RequestResult;
import cps.clientServer.RequestsSender;
import cps.clientServer.ServerResponse;
import cps.entities.CreditCustomerRequest;
import cps.entities.Customer;
import cps.utilities.Consts;
import cps.utilities.ConstsEmployees;
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
 * The Class CustomerServiceManageCustomersController.
 * Used for acquiring information about a customer and refunding him.
 */
public class CustomerServiceManageCustomersController extends EmployeeBaseController
{
    
    @FXML
    private Label Headline;
    
    @FXML
    private TextArea textBox;
    
    @FXML
    private TextField customerID;
    
    @FXML
    private Button refund;
    
    @FXML
    private ProgressBar prgBar;
    
    private Customer customer;
    
    /**
     * Prints the selected customer's information.
     *
     * @param event the event
     */
    @FXML
    void OnSubmit(ActionEvent event)
    {
	if (!InputValidator.Id(customerID.getText()))
	{
	    DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.InputsAreIncorrect, null, false);
	    
	    return;
	}
	
	prgBar.setVisible(true);
	
	CompletableFuture.runAsync(() ->
	{
	    ServerResponse<Customer> serverResponse = RequestsSender.GetCustomer(customerID.getText());
	    
	    Platform.runLater(() ->
	    {
		prgBar.setVisible(false);
		
		if (serverResponse.GetRequestResult().equals(RequestResult.Failed))
		{
		    DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
		    return;
		}
		
		if (serverResponse.GetRequestResult().equals(RequestResult.NotFound))
		{
		    DialogBuilder.AlertDialog(AlertType.ERROR, null, "Customer ID was not found.", null, false);
		    return;
		}
		
		if (serverResponse.GetRequestResult().equals(RequestResult.Succeed))
		{
		    textBox.setText(serverResponse.GetResponseObject().toString());
		    customer = serverResponse.GetResponseObject();
		    if (customer.GetBalance() > 0) refund.setDisable(false);
		}
	    });
	    
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
	myControllersManager.Back(PreviousScene, ConstsEmployees.ManageCustomer);
    }
    
    /**
     * Refunds the selected customer.
     *
     * @param event the event
     */
    @FXML
    void OnRefund(ActionEvent event)
    {
	CreditCustomerRequest request = new CreditCustomerRequest(customer.GetId(), -customer.GetBalance());
	
	prgBar.setVisible(true);
	
	CompletableFuture.runAsync(() ->
	{
	    ServerResponse<CreditCustomerRequest> serverResponse = RequestsSender.CreditCustomer(request);
	    
	    Platform.runLater(() ->
	    {
		prgBar.setVisible(false);
		
		if (serverResponse.GetRequestResult().equals(RequestResult.Succeed))
		{
		    DialogBuilder.AlertDialog(AlertType.INFORMATION, null, "Refund was successful.", null, false);
		    refund.setDisable(true);
		}
		else
		{
		    DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
		    refund.setDisable(true);
		}
	    });
	    
	});
	
    }
}
