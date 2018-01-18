package cps.clients.controllers.employee;

import java.util.ArrayList;

import cps.clientServer.RequestsSender;
import cps.clientServer.ServerResponse;
import cps.entities.Reservation;
import cps.entities.ReservationReport;
import cps.entities.enums.ReservationStatus;
import cps.utilities.ConstsEmployees;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class ReservationReportController.
 * Used for displaying a reservation report.
 */
public class ReservationReportController extends EmployeeBaseController {
	
	
	public class MyRow 
	{
		
		String ReservationId;
		
		String ParkingLotName;
		
		ReservationStatus Status;
		
		
		public MyRow(String a, String b, ReservationStatus c) 
		{
			ReservationId=a;
			ParkingLotName=b;
			Status=c;
		}
		
		
		public String getReservationId() {return ReservationId;}
		
		
		public ReservationStatus getStatus() {return Status;}
		
		
		public String getParkingLotName() {return ParkingLotName;}
	}
	
	private ObservableList<MyRow> InAdvancelist = FXCollections.observableArrayList();
	
	private ObservableList<MyRow> Guestlist = FXCollections.observableArrayList();

	
	

	
    @FXML
    private TextField excersizedReservations;

    @FXML
    private TextField reservationAmount;

    @FXML
    private TableView<MyRow> guestReservationTable;

    @FXML
    private TextField cancelledReservations;

    @FXML
    private TableView<MyRow> inAdvanceReservationTablr;
    
    /**
     * Initialize.
     */
    @FXML
    void initialize() 
    {
    	guestReservationTable.setEditable(true);
    	inAdvanceReservationTablr.setEditable(true);
    	
    	guestReservationTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("ReservationId"));
    	guestReservationTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("ParkingLotName"));
    	guestReservationTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("Status"));
    	
    	inAdvanceReservationTablr.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("ReservationId"));
    	inAdvanceReservationTablr.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("ParkingLotName"));
    	inAdvanceReservationTablr.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("Status"));
    }
		

    /**
     * Displays the reservation report. 
     * @param event the event
     */
    @FXML
    void OnShow2(ActionEvent event) 
    {	
    	String temp=MyEmployee.getOrgAffiliation();
    	if(temp.equals("CEO"))
    		temp="all";
      	ServerResponse<ReservationReport>ReservationReportRes= RequestsSender.GetReservationReport(temp);
    	ReservationReport reservationReport=ReservationReportRes.GetResponseObject();
    	
    	String amount=Integer.toString(reservationReport.getReservationAmount());
    	reservationAmount.setText(amount); 
    	String ex=Integer.toString(reservationReport.getReservationExcersied());
    	excersizedReservations.setText(ex);
    	String canceled=Integer.toString(reservationReport.getReservationCancelled());
    	cancelledReservations.setText(canceled);
    	
    	ArrayList<Reservation>reservationsList=reservationReport.getGuestList();
    	for (Reservation reservation : reservationsList) 
    	{
    		Guestlist.add(new MyRow(reservation.getOrderId(), reservation.getParkingLot(), reservation.getReservationStatus()));
		}
    	guestReservationTable.setItems(Guestlist);
    	

    	ArrayList<Reservation>reservationsList2=reservationReport.getInAdvanceList();
    	for (Reservation reservation : reservationsList2) 
    	{
    		InAdvancelist.add(new MyRow(reservation.getOrderId(), reservation.getParkingLot(), reservation.getReservationStatus()));
		}
    	inAdvanceReservationTablr.setItems(InAdvancelist);
    }

    /**
     * Sets the Previous scene.
     * @param event the event
     */
    @FXML
    void OnBack(ActionEvent event) 
    {
    	guestReservationTable.getItems().clear();
    	inAdvanceReservationTablr.getItems().clear();
    	myControllersManager.Back(PreviousScene, ConstsEmployees.ReservationReport);
    }

}
