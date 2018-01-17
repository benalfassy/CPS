package cps.clients.controllers.employee;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import cps.clientServer.RequestResult;
import cps.clientServer.RequestsSender;
import cps.clientServer.ServerResponse;
import cps.entities.StatusReport;
import cps.utilities.Consts;
import cps.utilities.ConstsEmployees;
import cps.utilities.DialogBuilder;
import cps.utilities.Pdf_Builder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

// TODO: Auto-generated Javadoc
/**
 * The Class StatusReportController.
 * Used for displaying a reservation report.
 */
public class StatusReportController extends EmployeeBaseController
{
    @FXML
    private TextField directory;
    
    @FXML
    private ProgressBar prgBar;
    
    /**
     * Opens directory chooser 
     * @param event the event
     */
    @FXML
    void OnBrowse(ActionEvent event)
    {
	DirectoryChooser chooser = new DirectoryChooser();
	
	File libDirectory = chooser.showDialog(null);
	
	if (libDirectory == null) return;
	
	directory.setText(libDirectory.getAbsolutePath());
    }
    
    /**
     * Sets the Previous scene.
     * @param event the event
     */
    @FXML
    void OnBack(ActionEvent event)
    {
	myControllersManager.Back(PreviousScene, ConstsEmployees.StatusReport);
    }
    
    /**
     * Sends request to build a PDF with the status report and save it in the chosen directory.
     * @param event the event
     */
    @FXML
    void OnSave(ActionEvent event)
    {
	if (directory.getText().equals(""))
	{
	    DialogBuilder.AlertDialog(AlertType.ERROR, "", ConstsEmployees.ChoosePathAgain, null, false);
	    return;
	}
	
	prgBar.setVisible(true);
	
	CompletableFuture.runAsync(() ->
	{
	    ServerResponse<StatusReport> serverResponse = RequestsSender.GetStatusReport();
	    
	    Platform.runLater(() ->
	    {
		prgBar.setVisible(false);
		
		if (serverResponse.GetRequestResult().equals(RequestResult.Succeed))
		{
		    Pdf_Builder pdf = new Pdf_Builder(directory.getText());
		    
		    pdf.build(serverResponse.GetResponseObject());
		    
		    DialogBuilder.AlertDialog(AlertType.INFORMATION, "", ConstsEmployees.StatusReportSaved, null,
			    false);
		}
		else
		{
		    DialogBuilder.AlertDialog(AlertType.ERROR, "", Consts.ServerProblemMessage, null, false);
		}
		
	    });
	    
	});
	
    }
    
}
