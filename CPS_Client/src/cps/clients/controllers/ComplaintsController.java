package cps.clients.controllers;

import java.util.concurrent.CompletableFuture;

import cps.clientServer.RequestResult;
import cps.clientServer.RequestsSender;
import cps.clientServer.ServerResponse;
import cps.entities.Complaint;
import cps.utilities.Consts;
import cps.utilities.ConstsWeb;
import cps.utilities.DialogBuilder;
import cps.utilities.InputValidator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

// TODO: Auto-generated Javadoc
/**
 * The Class ComplaintsController.
 * Used for submitting complaints about the company.
 */
public class ComplaintsController extends BaseController
{
    
    @FXML
    private TextArea complaintDeatils;
    
    @FXML
    private Label Headline;
    
    @FXML
    private TextField memberID;
    
    @FXML
    private ProgressBar prgBar;
    
    Complaint complaint;
    
    /**
     * Submits the complaint with the details filled by the client.
     *
     * @param event
     * 			 the event
     */
    @FXML
    void OnSubmit(ActionEvent event)
    {
	
	String result = DialogBuilder.AlertDialog(AlertType.CONFIRMATION, Consts.Approved, ConstsWeb.SumbitComplaint,
		null, false);
	
	if (result.equals("OK"))
	{
	    complaint = new Complaint(memberID.getText(), complaintDeatils.getText());
	    
	    if (!InputValidator.Id(memberID.getText()))
	    {
		DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.InputsAreIncorrect, null, false);
		
		return;
	    }
	    if (!InputValidator.TextIsEmpty(complaintDeatils.getText()))
	    {
		DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.InputsAreIncorrect, null, false);
		
		return;
		
	    }
	    
	    prgBar.setVisible(true);
	    
	    CompletableFuture.runAsync(() ->
	    {
		ServerResponse<Complaint> ComplaintResponse = RequestsSender.AddComplaint(complaint);
		
		Platform.runLater(() ->
		{
		    prgBar.setVisible(false);
		    
		    if (ComplaintResponse.GetRequestResult().equals(RequestResult.Failed))
		    {
			DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
			return;
		    }
		    if (ComplaintResponse.GetRequestResult().equals(RequestResult.NotFound))
		    {
			DialogBuilder.AlertDialog(AlertType.ERROR, null, "Sorry, your ID was not found\n", null, false);
			return;
		    }
		    DialogBuilder.AlertDialog(AlertType.INFORMATION, Consts.Approved, Consts.ComplaintRegistered, null,
			    false);
		    
		    myControllersManager.SetScene(ConstsWeb.Web, null);
		});
		
	    });
	    
	}
    }
    
    /**
     * Sets the previous scene.
     *
     * @param event
     * 			 the event
     */
    @FXML
    void OnBack(ActionEvent event)
    {
	myControllersManager.Back(PreviousScene, ConstsWeb.Complaints);
    }
    
}
