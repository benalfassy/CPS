package cps.clients.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.function.Consumer;

import cps.clientServer.RequestResult;
import cps.clientServer.RequestsSender;
import cps.clientServer.ServerResponse;
import cps.entities.Customer;
import cps.entities.Parkinglot;
import cps.entities.Reservation;
import cps.entities.enums.ReservationStatus;
import cps.entities.enums.ReservationType;
import cps.utilities.Consts;
import cps.utilities.ConstsWeb;
import cps.utilities.DialogBuilder;
import cps.utilities.InputValidator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.MenuButton;

// TODO: Auto-generated Javadoc
/**
 * The Class OrderInAdvanceController.
 */
public class OrderInAdvanceController extends BaseController
{
    
    @FXML 
    private TextField carNumber;
    
    @FXML
    private MenuButton parkingLot; 
    
    @FXML
    private Label Headline;
    
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
    
    ArrayList<Parkinglot> parkinglist = new ArrayList<Parkinglot>();
    
    String parking_Lot;
    
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
	ServerResponse<ArrayList<Parkinglot>> initListParkinglot = RequestsSender.GetAllParkinglots(false);
	int length = initListParkinglot.GetResponseObject().size();
	for (int i = 0; i < length; i++)
	{
	    MenuItem item = new MenuItem(initListParkinglot.GetResponseObject().get(i).getParkinglotName());
	    item.setOnAction(a ->
	    {
		parking_Lot = (item.getText());
		parkingLot.setText(parking_Lot);
	    });
	    parkingLot.getItems().add(item);
	    parkinglist.add(initListParkinglot.GetResponseObject().get(i));
	}
    }
    
    /**
     * Client clicks on payment button.
     *
     * @param event the event
     */
    @FXML
    void OnPayment(ActionEvent event)
    {
	
	if (!TryConstructOrderInAdvance())
	{
	    return;
	}
		
	Consumer<Void> afterPayment = Void ->
	{
	    ServerResponse<Reservation> OrderInAdvanceResponse = RequestsSender.Reservation(reservation);
	    
	    ServerResponse<Customer> AddCustomerIfNotExist = RequestsSender.AddCustomerIfNotExists(customer);
	    
	    Platform.runLater(() ->
	    {
		if (OrderInAdvanceResponse.GetRequestResult().equals(RequestResult.Failed)
			|| AddCustomerIfNotExist.GetRequestResult().equals(RequestResult.Failed))
		{
		    DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
		    
		    return;
		}
		
		if (OrderInAdvanceResponse.GetRequestResult().equals(RequestResult.ResourceNotAvaillable))
		{
		    DialogBuilder.AlertDialog(AlertType.ERROR, null, "We are sorry, the parking lot is full.", null, false);
		    
		    return;
		}
		
		DialogBuilder.AlertDialog(AlertType.INFORMATION, Consts.Approved, Consts.ThankYouForOrderInAdvance
			+ "\n Your order ID : " + OrderInAdvanceResponse.GetResponseObject().getOrderId(), null, false);
		
		myControllersManager.GoToHomePage(Consts.Payment);
	    });
	};
	
	myControllersManager.Payment(reservation, reservation.getPrice(), afterPayment, ConstsWeb.OrderInAdvance);
	arrivalDate.setValue(LocalDate.now());
	leavingDate.setValue(LocalDate.now());
	
    }
    
    /**
     * Client clicks on back button.
     *
     * @param event the event
     */
    @FXML
    void OnBack(ActionEvent event)
    {
	arrivalDate.setValue(LocalDate.now());
	leavingDate.setValue(LocalDate.now());
	myControllersManager.Back(PreviousScene, ConstsWeb.OrderInAdvance);
    }
    
    /**
     * Try construct order in advance.
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
	
	reservation = new Reservation(ReservationType.Web, customerId.getText(), parking_Lot, carNumber.getText(),
		arrivalDate.getValue(), leavingDate.getValue(), LocalTime.parse(arrivalHour.getText()),
		LocalTime.parse(leavingHour.getText()), ReservationStatus.NotStarted, AmountToPay());
	
	customer = new Customer(customerId.getText(), email.getText(), 0);
	
	return true;
    }
    
    /**
     *Sets the payment amount.
     *
     * @return the float
     */
    private float AmountToPay()
    {
	float rate = 0;
	float days = (float) (Period.between(arrivalDate.getValue(), leavingDate.getValue()).getDays());
	float hours = (LocalTime.parse(leavingHour.getText()).getHour()
		- LocalTime.parse(arrivalHour.getText()).getHour());
	for (Parkinglot parkinglot : parkinglist)
	{
	    if (parkinglot.getParkinglotName().equals(parking_Lot)) rate = parkinglot.getInAdvanceRate();
	}
	return ((hours + days * 24) * rate);
    }
    
    /**
     * Checks if is input legal.
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
	    
	    if (arrival.isAfter(leaving) && arrival.isBefore(LocalDateTime.now()))
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
	
	if (parking_Lot == null)
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
