package cps.clients.controllers;

import java.util.concurrent.CompletableFuture;

import cps.clientServer.RequestResult;
import cps.clientServer.RequestsSender;
import cps.clientServer.ServerResponse;
import cps.entities.Customer;
import cps.utilities.Consts;
import cps.utilities.DialogBuilder;
import cps.utilities.InputValidator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

// TODO: Auto-generated Javadoc
/**
 * The Class MonitorAndControlCustomerController.
 * Used for getting a customer's information.
 */
public class MonitorAndControlCustomerController extends BaseController
{
    
    @FXML
    private Label Headline;
    
    @FXML
    private TextArea textBox;
    
    @FXML
    private TextField customerID;
    
    @FXML
    private ProgressBar prgBar;
    
    /**
     * Prints the selected client's information.
     *
     * @param event
     *            the event
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
		if (serverResponse.GetRequestResult().equals(RequestResult.Failed))
		{
		    DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
		    
		}
		
		else if (serverResponse.GetRequestResult().equals(RequestResult.NotFound))
		{
		    DialogBuilder.AlertDialog(AlertType.ERROR, null, "Customer ID was not found.", null, false);
		    
		}
		
		else if (serverResponse.GetRequestResult().equals(RequestResult.Succeed))
		{
		    textBox.setText(serverResponse.GetResponseObject().toString());
		}
		
		prgBar.setVisible(false);
		
	    });
	    
	});
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
	myControllersManager.Back(PreviousScene, Consts.MonitorAndControlCustomer);
    }
}
