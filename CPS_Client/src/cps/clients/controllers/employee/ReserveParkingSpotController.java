package cps.clients.controllers.employee;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import cps.clientServer.RequestResult;
import cps.clientServer.RequestsSender;
import cps.clientServer.ServerResponse;
import cps.entities.Customer;
import cps.entities.Reservation;
import cps.entities.enums.ReservationStatus;
import cps.entities.enums.ReservationType;
import cps.utilities.Consts;
import cps.utilities.ConstsEmployees;
import cps.utilities.DialogBuilder;
import cps.utilities.InputValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

// TODO: Auto-generated Javadoc
/**
 * The Class ReserveParkingSpotController.
 * Used for reserving a parking spot.
 */
public class ReserveParkingSpotController extends EmployeeBaseController
{
    
    @FXML 
    private TextField carNumber;
    
    @FXML 
    private TextField parkingLot; 
    
    @FXML 
    private TextField arrivalHour; 
    
    @FXML 
    private TextField leavingHour;
    
    @FXML
    private TextField customerId; 
    
    @FXML 
    private TextField email; 
    
    @FXML
    private DatePicker arrivalDate;
    
    @FXML
    private DatePicker leavingDate; 
    
    @FXML
    private Label Headline;
    
    Reservation reservation;
    
    Customer customer;
    
    /**
     * Initialize.
     */
    @FXML
    void initialize()
    {
	arrivalDate.setEditable(true);
	arrivalDate.setValue(LocalDate.now());
	leavingDate.setEditable(true);
	leavingDate.setValue(LocalDate.now());
    }
    
    /**
     * Submits the new reservation.
     * @param event the event
     */
    @FXML
    void OnSubmit(ActionEvent event)
    {
	
	// float paymentAmount = 0;
	
	if (!TryConstructOrderInAdvance())
	{
	    return;
	}
	
	ServerResponse<Reservation> OrderInAdvanceResponse = RequestsSender.Reservation(reservation);
	ServerResponse<Customer> AddCustomerIfNotExist = RequestsSender.AddCustomerIfNotExists(customer);
	
	if (OrderInAdvanceResponse.GetRequestResult().equals(RequestResult.Failed)
		|| AddCustomerIfNotExist.GetRequestResult().equals(RequestResult.Failed))
	{
	    DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
	    return;
	}
	
	DialogBuilder.AlertDialog(AlertType.INFORMATION, Consts.Approved, ConstsEmployees.ReservationSubmitted
		+ "\nYour reservation id: " + OrderInAdvanceResponse.GetResponseObject().getOrderId(), null, false);
	myControllersManager.Back(PreviousScene, ConstsEmployees.ReserveParkingSpot);
	
    }
    
    /**
     * Sets the Previous scene.
     * @param event the event
     */
    @FXML
    void OnBack(ActionEvent event)
    {
	myControllersManager.Back(PreviousScene, ConstsEmployees.ReserveParkingSpot);
    }
    
    /**
     * Creates the reservation according to the details filled by the employee.
     *
     * @return true, if successful
     */
    private boolean TryConstructOrderInAdvance()
    {
	if (!IsInputLegal())
	{
	    DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.InputsAreIncorrect, null, false);
	    
	    return false;
	}
	
	reservation = new Reservation(ReservationType.Employee, customerId.getText(), parkingLot.getText(),
		carNumber.getText(), arrivalDate.getValue(), leavingDate.getValue(),
		LocalTime.parse(arrivalHour.getText()), LocalTime.parse(leavingHour.getText()),
		ReservationStatus.NotStarted, 0);
	
	customer = new Customer(customerId.getText(), email.getText(), 0);
	
	return true;
    }
    
    /**
     * Checks if the input is legal.
     *
     * @return true, if successful
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
	
	if (!InputValidator.Id(customerId.getText()))
	{
	    result = false;
	    customerId.setStyle("-fx-background-color: tomato;");
	}
	else
	{
	    customerId.setStyle("-fx-background-color: white;");
	}
	
	if (!InputValidator.StartingDate(arrivalDate.getValue()))
	{
	    result = false;
	    arrivalDate.setStyle("-fx-background-color: tomato;");
	}
	else
	{
	    arrivalDate.setStyle("-fx-background-color: white;");
	}
	
	if (!InputValidator.CheckHourFormat(arrivalHour.getText()))
	{
	    result = false;
	    arrivalHour.setStyle("-fx-background-color: tomato;");
	}
	else
	{
	    arrivalHour.setStyle("-fx-background-color: white;");
	}
	
	if (!InputValidator.StartingDate(leavingDate.getValue()))
	{
	    result = false;
	    leavingDate.setStyle("-fx-background-color: tomato;");
	}
	else
	{
	    leavingDate.setStyle("-fx-background-color: white;");
	}
	
	if (!InputValidator.CheckHourFormat(leavingHour.getText()))
	{
	    result = false;
	    leavingHour.setStyle("-fx-background-color: tomato;");
	}
	else
	{
	    leavingHour.setStyle("-fx-background-color: white;");
	}
	
	if (InputValidator.CheckHourFormat(leavingHour.getText()) && InputValidator.StartingDate(leavingDate.getValue())
		&& InputValidator.CheckHourFormat(arrivalHour.getText())
		&& InputValidator.StartingDate(arrivalDate.getValue()))
	{
	    LocalDateTime arrival = LocalDateTime.of(arrivalDate.getValue(), LocalTime.parse(arrivalHour.getText()));
	    LocalDateTime leaving = LocalDateTime.of(leavingDate.getValue(), LocalTime.parse(leavingHour.getText()));
	    
	    if (arrival.isAfter(leaving))
	    {
		result = false;
		leavingHour.setStyle("-fx-background-color: tomato;");
		leavingDate.setStyle("-fx-background-color: tomato;");
	    }
	    else
	    {
		leavingHour.setStyle("-fx-background-color: white;");
		leavingDate.setStyle("-fx-background-color: white;");
	    }
	}
	
	if (parkingLot == null || parkingLot.getText().equals(""))
	{
	    result = false;
	    parkingLot.setStyle("-fx-background-color: tomato;");
	}
	else
	{
	    parkingLot.setStyle("-fx-background-color: white;");
	}
	
	return result;
    }
    
}
