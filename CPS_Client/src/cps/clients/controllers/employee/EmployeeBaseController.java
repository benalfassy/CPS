package cps.clients.controllers.employee;

import java.util.concurrent.CompletableFuture;

import cps.clientServer.RequestResult;
import cps.clientServer.RequestsSender;
import cps.clients.controllers.BaseController;
import cps.entities.Employee;
import cps.utilities.DialogBuilder;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;

// TODO: Auto-generated Javadoc
/**
 * The Class EmployeeBaseController. The base of all employee controllers.
 */
public class EmployeeBaseController extends BaseController
{
    
    protected static Employee MyEmployee;
    
    /**
     * Gets the my employee.
     *
     * @return the my employee
     */
    public static Employee getMyEmployee()
    {
	return MyEmployee;
    }
    
    
    protected void LogOut()
    {
	try
	{
	    CompletableFuture.runAsync(() ->
	    
	    {
		if (!RequestsSender.LogoutUser(MyEmployee.getUsername()).GetRequestResult()
			.equals(RequestResult.Succeed))
		{
		    Platform.runLater(() ->
		    {
			DialogBuilder.AlertDialog(AlertType.ERROR, null,
				"Failed to log out your user, please contact IT", null, false);
		    });
		}
	    });
	}
	catch (Exception e)
	{
	    DialogBuilder.AlertDialog(AlertType.ERROR, null, "Failed to log out your user, please contact IT", null,
		    false);
	}
    }
}
