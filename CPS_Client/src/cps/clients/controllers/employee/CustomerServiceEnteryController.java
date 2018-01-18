package cps.clients.controllers.employee;

import cps.utilities.ConstsEmployees;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

// TODO: Auto-generated Javadoc
/**
 * The Class CustomerServiceEnteryController.
 * Used as the customer service employee's main page.
 */
public class CustomerServiceEnteryController  extends EmployeeBaseController{

	@FXML
    private Label Headline;
	
    /**
     * Sets the manage complaints scene.
     * @param event the event
     */
    @FXML
    void OnManageComplaints(ActionEvent event) 
    {
    	myControllersManager.SetScene(ConstsEmployees.ManageComplaints, ConstsEmployees.CustomerServiceEntery);
    }

    /**
     * Sets the reserve parking spot scene.
     * @param event the event
     */
    @FXML
    void OnSaveParkingSpot(ActionEvent event) 
    {
    	myControllersManager.SetScene(ConstsEmployees.ReserveParkingSpot, ConstsEmployees.CustomerServiceEntery);

    }
    
    /**
     * Sets the previous scene
     * @param event the event
     */
    @FXML
    void OnBack(ActionEvent event) 
    {
	LogOut();
	
    	myControllersManager.Back(PreviousScene,ConstsEmployees.ParkingLotWorkerEntery );
    }
    
    /**
     * Sets the manage customer scene.
     * @param event the event
     */
    @FXML
    void OnManageCustomer(ActionEvent event) 
    {
    	myControllersManager.SetScene(ConstsEmployees.ManageCustomer, ConstsEmployees.CustomerServiceEntery);

    }
    
}
