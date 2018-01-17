package cps.clients.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.function.Consumer;

import cps.clientServer.RequestResult;
import cps.clientServer.RequestsSender;
import cps.clientServer.ServerResponse;
import cps.entities.Customer;
import cps.entities.Parkinglot;
import cps.entities.PartialMembership;
import cps.utilities.Consts;
import cps.utilities.DialogBuilder;
import cps.utilities.InputValidator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

// TODO: Auto-generated Javadoc 
/**
 * The Class PartialMembershipRegisterController.
 * Used for registering for a partial membership.
 */
public class PartialMembershipRegisterController extends BaseController
{
    
    @FXML
    private TextField carNumber;
    
    @FXML
    private MenuButton parkingLot;
    
    @FXML
    private Label Headline;
    
    @FXML
    private TextField exitHour;
    
    @FXML
    private ListView<String> carListView;
    
    @FXML
    private TextField id;
    
    @FXML
    private DatePicker startingDatePicker;
    
    @FXML
    private TextField email;
    
    private PartialMembership partialMembership;
    
    private Customer customer;
    
    private String parking_Lot;
    
    private ObservableList<String> cars = FXCollections.observableArrayList();
    
    private ArrayList<Parkinglot> parkinglist = new ArrayList<Parkinglot>();
    
    private ArrayList<String> carList = new ArrayList<String>();
    
    /**
     * Initialize.
     */
    @FXML
    void initialize()
    {
	carListView.setEditable(true);
	startingDatePicker.setEditable(true);
	startingDatePicker.setValue(LocalDate.now());
	ServerResponse<ArrayList<Parkinglot>> initListParkinglot = RequestsSender.GetAllParkinglots(false);
	if (initListParkinglot.GetRequestResult().equals(RequestResult.Failed))
	{
	    DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
	    return;
	}
	
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
     * Sets the previous scene.
     *
     * @param event
     *            the event
     */
    @FXML
    void OnBack(ActionEvent event)
    {
	myControllersManager.Back(PreviousScene, Consts.PartialMembershipRegister);
	
	carListView.setItems(null);
	cars.clear();
	exitHour.clear();
    }
    
    /**
     * Calculates the price of the membership and sets the payment controller.
     * After approving the payment, creates the new partial membership.
     *
     * @param event
     *            the event
     */
    @FXML
    void OnSubmitAndPay(ActionEvent event)
    {
	
	if (!TryConstructPartialMembership())
	{
	    return;
	}
	
	float paymentAmount = AmountToPay();
	
	Consumer<Void> afterPayment = Void ->
	{
	    ServerResponse<PartialMembership> registerPartialMembershipResponse = RequestsSender
		    .RegisterPartialMembership(partialMembership);
	    ServerResponse<Customer> AddCustomerIfNotExist = RequestsSender.AddCustomerIfNotExists(customer);
	    
	    Platform.runLater(() ->
	    {
		if (registerPartialMembershipResponse.GetRequestResult().equals(RequestResult.Failed)
			|| AddCustomerIfNotExist.GetRequestResult().equals(RequestResult.Failed))
		{
		    DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.ServerProblemMessage, null, false);
		    
		    return;
		}
		
		if (registerPartialMembershipResponse.GetRequestResult().equals(RequestResult.AlredyExist))
		{
		    DialogBuilder.AlertDialog(AlertType.ERROR, null, "One of your cars is already registered to this parking lot.", null, false);
		    
		    return;
		}
		
		DialogBuilder.AlertDialog(AlertType.INFORMATION, Consts.Approved,
			Consts.ThankYouForRegistering + "\n Your subscription ID : "
				+ registerPartialMembershipResponse.GetResponseObject().GetSubscriptionId(),
			null, false);
		
		myControllersManager.GoToHomePage(Consts.Payment);
	    });
	    
	};
	
	myControllersManager.Payment(partialMembership, paymentAmount, afterPayment, Consts.PartialMembershipRegister);
	
	carListView.setItems(null);
	cars.clear();
	carList = new ArrayList<>();
    }
    
    /**
     * Creates the membership according to the details filled by the customer.
     *
     * @return true, if successful
     */
    private boolean TryConstructPartialMembership()
    {
	
	if (exitHour.getText().equals(""))
	{
	    exitHour.setText("00:00");
	}
	
	if (!IsInputLegal())
	{
	    DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.InputsAreIncorrect, null, false);
	    return false;
	}
	
	customer = new Customer(id.getText(), email.getText(), 0);
	
	partialMembership = new PartialMembership(id.getText(), startingDatePicker.getValue(),
		startingDatePicker.getValue().plusDays(28), parking_Lot, carList, LocalTime.parse(exitHour.getText()));
	
	return true;
    }
    
    /**
     * Adds a car to the membership.
     *
     * @param event
     *            the event
     */
    @FXML
    void OnAddCar(ActionEvent event)
    {
	
	cars.add(carNumber.getText());// insert car to list view
	carList.add(carNumber.getText());
	
	carNumber.clear();
	
	carListView.setItems(cars);
    }
    
    /**
     * Removes the selected car from the membership.
     *
     * @param event
     *            the event
     */
    @FXML
    void OnRemoveSelected(ActionEvent event)
    {
	final int selectedIdx = carListView.getSelectionModel().getSelectedIndex();
	if (selectedIdx != -1)
	{
	    String itemToRemove = carListView.getSelectionModel().getSelectedItem();
	    
	    final int newSelectedIdx = (selectedIdx == carListView.getItems().size() - 1) ? selectedIdx - 1
		    : selectedIdx;
	    
	    carListView.getItems().remove(selectedIdx);
	    for (int i = 0; i < carList.size(); i++)
	    {
		if (carList.get(i).equals(itemToRemove)) carList.remove(i);
	    }
	    
	    carListView.getSelectionModel().select(newSelectedIdx);
	}
	
    }
    
    /**
     * Calculates the membership's price.
     *
     * @return the float
     */
    private float AmountToPay()
    {
	float rate = 0;
	float cars = carList.size();
	float hours = 0;
	hours = (cars > 1 ? 54 : 60);
	for (Parkinglot parkinglot : parkinglist)
	{
	    if (parkinglot.getParkinglotName().equals(parking_Lot))
	    {
		rate = parkinglot.getInAdvanceRate();
	    }
	}
	return (cars * hours * rate);
    }
    
    /**
     * Checks if the input is legal.
     *
     * @return true, if successful
     */
    private boolean IsInputLegal()
    {
	boolean result = true;
	
	if (!InputValidator.CheckCarList(carList))
	{
	    result = false;
	    carListView.setStyle("-fx-background-color: tomato;");
	}
	else
	{
	    carListView.setStyle("-fx-background-color: white;");
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
	
	if (!InputValidator.Id(id.getText()))
	{
	    result = false;
	    id.setStyle("-fx-background-color: tomato;");
	}
	else
	{
	    id.setStyle("-fx-background-color: white;");
	}
	
	if (!InputValidator.StartingDate(startingDatePicker.getValue()))
	{
	    result = false;
	    startingDatePicker.setStyle("-fx-background-color: tomato;");
	}
	else
	{
	    startingDatePicker.setStyle("-fx-background-color: white;");
	}
	
	if (!InputValidator.CheckHourFormat(exitHour.getText()))
	{
	    result = false;
	    exitHour.setStyle("-fx-background-color: tomato;");
	}
	else
	{
	    exitHour.setStyle("-fx-background-color: white;");
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
