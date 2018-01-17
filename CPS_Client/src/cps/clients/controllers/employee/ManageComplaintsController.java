package cps.clients.controllers.employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import cps.clientServer.RequestResult;
import cps.clientServer.RequestsSender;
import cps.clientServer.ServerResponse;
import cps.entities.CloseComplaintRequest;
import cps.entities.Complaint;
import cps.utilities.Consts;
import cps.utilities.ConstsEmployees;
import cps.utilities.DialogBuilder;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;

// TODO: Auto-generated Javadoc
/**
 * The Class ManageComplaintsController.
 * Used for viewing complaints and compensating customers.
 */
public class ManageComplaintsController extends EmployeeBaseController
{
    
    ArrayList<Complaint> myComplaints;
    
    ArrayList<String> myButtons = new ArrayList<String>();
    
    private ArrayList<String> Compensate = new ArrayList<>();
    
    private String currentComplaintId = null;
    
    @FXML
    private ListView<String> complaintsList;
    
    @FXML
    private ProgressBar prgBar;
    
    /**
     * Sets the Previous scene.
     * @param event the event
     */
    @FXML
    void OnBack(ActionEvent event)
    {
	complaintsList.setItems(null);
	
	myComplaints = null;
	
	currentComplaintId = null;
	
	myControllersManager.Back(PreviousScene, ConstsEmployees.ManageComplaints);
    }
    
    /**
     * Marks the complaint as handled and compensates the customer, if needed.
     * @param event the event
     */
    @FXML
    void OnHandleComplaint(ActionEvent event)
    {
	Complaint currentComplaint = null;
	
	if (currentComplaintId == null)
	{
	    DialogBuilder.AlertDialog(AlertType.WARNING, "", ConstsEmployees.ChooseComplaint, null, false);
	    return;
	}
	
	for (Complaint complaint : myComplaints)
	{
	    if (complaint.getComplaintId().equals(currentComplaintId))
	    {
		currentComplaint = complaint;
	    }
	    
	}
	if (currentComplaint != null)
	{
	    String Details = "Complaint ID: " + currentComplaint.getComplaintId() + "\nCustomer ID: "
		    + currentComplaint.getCustomerId() + "\nComplaint Details: "
		    + currentComplaint.getComplaintDetails() + "\n";
	    String result = DialogBuilder.AlertDialog(AlertType.NONE, ConstsEmployees.SelectChoice, Details, myButtons,
		    true);
	    
	    if (result.equals("Compensate Customer"))
	    {
		Dialog<List<String>> dialog = DialogBuilder.InputsDialog(Consts.FillRequest, Compensate, "Compensate");
		
		Optional<List<String>> myresult = dialog.showAndWait();
		
		myresult.ifPresent(inputs ->
		{
		    Float compensationAmount = Float.parseFloat(inputs.get(0));
		    
		    CloseComplaintRequest myClosedComplaint = new CloseComplaintRequest(currentComplaintId,
			    compensationAmount);
		    
		    CompletableFuture.runAsync(() ->
		    {
			RequestsSender.CloseComplaint(myClosedComplaint);
		    });
		    
		    DialogBuilder.AlertDialog(AlertType.INFORMATION, "", ConstsEmployees.ComplaintClosed, null, false);
		});
	    }
	    
	    else if (result.equals("Close without compensation"))
	    {
		
		CloseComplaintRequest myClosedComplaint = new CloseComplaintRequest(currentComplaintId, 0);
		
		CompletableFuture.runAsync(() ->
		{
		    RequestsSender.CloseComplaint(myClosedComplaint);
		});
		
		DialogBuilder.AlertDialog(AlertType.INFORMATION, "", ConstsEmployees.ComplaintClosed, null, false);
		
	    }
	}
	
	OnLoad(null);
    }
    
    /**
     * Prints all unhandled complaints.
     * @param event the event
     */
    @FXML
    void OnLoad(ActionEvent event)
    {
	currentComplaintId = null;
	
	prgBar.setVisible(true);
	
	CompletableFuture.runAsync(() ->
	{
	    ServerResponse<ArrayList<Complaint>> serverResponse = RequestsSender.GetAllActiveComplaints();
	    
	    Platform.runLater(() ->
	    {
		prgBar.setVisible(false);
		
		if (serverResponse.GetRequestResult().equals(RequestResult.Failed))
		{
		    DialogBuilder.AlertDialog(AlertType.ERROR, "", ConstsEmployees.FailToGetActive, null, false);
		    SetPreviousScene(PreviousScene);
		}
		
		ArrayList<Complaint> complaints = serverResponse.GetResponseObject();
		myComplaints = complaints;
		ObservableList<String> complaintsObservable = FXCollections.observableArrayList();
		
		for (Complaint complaint : complaints)
		{
		    complaintsObservable.add(complaint.getComplaintId());
		}
		
		complaintsList.setItems(complaintsObservable);
		
		if (complaints.size() == 0)
		{
		    DialogBuilder.AlertDialog(AlertType.INFORMATION, null, ConstsEmployees.NoComplaints, null, false);
		}
		
	    });
	    
	});
	
    }
    
    /**
     * Initialize.
     */
    @FXML
    void initialize()
    {
	myButtons.add("Compensate Customer");
	myButtons.add("Close without compensation");
	Compensate.add("Compensation Amount:");
	
	complaintsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
	{
	    
	    @Override
	    public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2)
	    {
		
		currentComplaintId = arg2;
		
	    }
	});
	
    }
}
