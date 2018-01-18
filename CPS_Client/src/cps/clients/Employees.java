package cps.clients;

import java.net.URL;
import java.util.LinkedList;

import cps.clientServer.RequestResult;
import cps.clientServer.RequestsSender;
import cps.clients.controllers.ControllersManager;
import cps.clients.controllers.employee.EmployeeBaseController;
import cps.utilities.ConstsEmployees;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Pair;

// TODO: Auto-generated Javadoc
/**
 * The Employees Entry.
 */
public class Employees extends Application {
	
	/** The controllers manager. */
	ControllersManager controllersManager;

	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			LinkedList<Pair<String, URL>> list = new LinkedList<>();

			Class<?> myClass = getClass();

			list.add(new Pair<String, URL>(ConstsEmployees.EmployeesLogin,
					myClass.getResource(ConstsEmployees.EmployeesLoginFxmlPath)));
			list.add(new Pair<String, URL>(ConstsEmployees.ParkingLotWorkerEntery,
					myClass.getResource(ConstsEmployees.ParkingLotWorkerEnteryFxmlPath)));
			list.add(new Pair<String, URL>(ConstsEmployees.ManagerLogin,
					myClass.getResource(ConstsEmployees.ManagerLoginFxmlPath)));
			list.add(new Pair<String, URL>(ConstsEmployees.CustomerServiceEntery,
					myClass.getResource(ConstsEmployees.CustomerServiceEnteryFxmlPath)));
			list.add(new Pair<String, URL>(ConstsEmployees.CeoLogin,
					myClass.getResource(ConstsEmployees.CeoLoginFxmlPath)));
			list.add(new Pair<String, URL>(ConstsEmployees.ManageRequestRateChange,
					myClass.getResource(ConstsEmployees.ManageRequestRateChangeFxmlPath)));
			list.add(new Pair<String, URL>(ConstsEmployees.ReserveParkingSpot,
					myClass.getResource(ConstsEmployees.ReserveParkingSpotFxmlPath)));
			list.add(new Pair<String, URL>(ConstsEmployees.ReserveParkingSpotInLocalParkingLot,
					myClass.getResource(ConstsEmployees.ReserveParkingSpotInLocalParkingLotFxmlPath)));
			list.add(new Pair<String, URL>(ConstsEmployees.ManageComplaints,
					myClass.getResource(ConstsEmployees.ManageComplaintsFxmlPath)));
			list.add(new Pair<String, URL>(ConstsEmployees.ProduceReport,
					myClass.getResource(ConstsEmployees.ProduceReportFxmlPath)));
			list.add(new Pair<String, URL>(ConstsEmployees.ReservationReport,
					myClass.getResource(ConstsEmployees.ReservationReportFxmlPath)));
			list.add(new Pair<String, URL>(ConstsEmployees.ActivityReport,
					myClass.getResource(ConstsEmployees.ActivityReportFxmlPath)));
			list.add(new Pair<String, URL>(ConstsEmployees.ManagerProduceReport,
					myClass.getResource(ConstsEmployees.ManagerProduceReportFxmlPath)));
			list.add(new Pair<String, URL>(ConstsEmployees.StatusReport,
					myClass.getResource(ConstsEmployees.StatusReportControllerFxmlPath)));
			list.add(new Pair<String, URL>(ConstsEmployees.ManageCustomer,
					myClass.getResource(ConstsEmployees.ManageCustomerFxmlPath)));

			controllersManager = new ControllersManager(list, primaryStage, ConstsEmployees.EmployeesLogin);

			controllersManager.GoToHomePage(null);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/* (non-Javadoc)
	 * @see javafx.application.Application#stop()
	 */
	@Override
	public void stop() {
		if (EmployeeBaseController.getMyEmployee() != null) {
			if (!RequestsSender.LogoutUser(EmployeeBaseController.getMyEmployee().getUsername()).GetRequestResult()
					.equals(RequestResult.Succeed)) {
				System.out.println("Failed to log out user !!!");
			}
		}
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
