package cps.clients.controllers.employee;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import cps.clientServer.RequestResult;
import cps.clientServer.RequestsSender;
import cps.clientServer.ServerResponse;
import cps.entities.Employee;
import cps.entities.LoginIdentification;
import cps.entities.enums.EmployeeType;
import cps.entities.enums.LogedStatus;
import cps.utilities.Consts;
import cps.utilities.ConstsEmployees;
import cps.utilities.DialogBuilder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

// TODO: Auto-generated Javadoc
/**
 * The Class EmployeesLoginController.
 */
public class EmployeesLoginController extends EmployeeBaseController
{
    
    @FXML
    private PasswordField password;
    
    @FXML
    private TextField userName;
    
    @FXML
    private Button login;
    
    @FXML
    private ProgressIndicator prgBar;
    
    /**
     * Logins to the employee's account if he isn't already logged in.
     * @param event the event
     * @throws InterruptedException the interrupted exception
     * @throws ExecutionException the execution exception
     */
    @FXML
    void OnLogin(ActionEvent event) throws InterruptedException, ExecutionException
    {
	if (password.getText().equals("") || userName.getText().equals(""))
	{
	    DialogBuilder.AlertDialog(AlertType.ERROR, null, ConstsEmployees.OneFieldOrMoreIsEmpty, null, false);
	    return;
	}
	
	Platform.runLater(() -> prgBar.setVisible(true));
	
	CompletableFuture.runAsync(() ->
	{
	    LoginIdentification loginIdentification = new LoginIdentification(userName.getText(), password.getText());
	    
	    ServerResponse<Employee> employeeRes = RequestsSender.GetEmployee(loginIdentification);
	    
	    Platform.runLater(() ->
	    {
		if (employeeRes.GetRequestResult().equals(RequestResult.WrongCredentials))
		{
		    DialogBuilder.AlertDialog(AlertType.ERROR, null, ConstsEmployees.IncorrectUsernameOrPassword, null,
			    false);
		    Platform.runLater(() -> prgBar.setVisible(false));
		    return;
		}
		
		if (employeeRes.GetRequestResult().equals(RequestResult.Failed))
		{
		    DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
		    Platform.runLater(() -> prgBar.setVisible(false));
		    return;
		}
		
		if (employeeRes.GetRequestResult().equals(RequestResult.Succeed))
		{
		    if (employeeRes.GetResponseObject().getLogedStatus().equals(LogedStatus.LogedIn))
		    {
			DialogBuilder.AlertDialog(AlertType.ERROR, null,
				"You are already logged in.\nPlease log out first.", null, false);
			Platform.runLater(() -> prgBar.setVisible(false));
			return;
		    }
		    
		    MyEmployee = employeeRes.GetResponseObject();
		    
		    if (!RequestsSender.LoginUser(MyEmployee.getUsername()).GetRequestResult()
			    .equals(RequestResult.Succeed))
		    {
			DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
			Platform.runLater(() -> prgBar.setVisible(false));
			return;
		    }
		    
		    if (MyEmployee.getEmployeeType().equals(EmployeeType.CustomerService))
			myControllersManager.SetScene(ConstsEmployees.CustomerServiceEntery,
				ConstsEmployees.EmployeesLogin);
		    else if (MyEmployee.getEmployeeType().equals(EmployeeType.CEO))
			myControllersManager.SetScene(ConstsEmployees.CeoLogin, ConstsEmployees.EmployeesLogin);
		    else if (MyEmployee.getEmployeeType().equals(EmployeeType.Parkinglot))
			myControllersManager.SetScene(ConstsEmployees.ParkingLotWorkerEntery,
				ConstsEmployees.EmployeesLogin);
		    else if (MyEmployee.getEmployeeType().equals(EmployeeType.ParkinglotManager))
			myControllersManager.SetScene(ConstsEmployees.ManagerLogin, ConstsEmployees.EmployeesLogin);
		    
		}
		
		Platform.runLater(() -> prgBar.setVisible(false));
	    });
	});
	
    }
    
}
