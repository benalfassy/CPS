package cps.clients.controllers.employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import cps.clientServer.RequestResult;
import cps.clientServer.RequestsSender;
import cps.clientServer.ServerResponse;
import cps.entities.ChangeParkingSpotStatusRequest;
import cps.entities.ChangeParkinglotStatusRequest;
import cps.entities.ParkingSpot;
import cps.entities.Parkinglot;
import cps.entities.enums.ParkingSpotStatus;
import cps.entities.enums.ParkinglotStatus;
import cps.utilities.Consts;
import cps.utilities.ConstsEmployees;
import cps.utilities.DialogBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;

// TODO: Auto-generated Javadoc
/**
 * The Class CeoLoginController.
 * Used as the CEO's main page.
 */
public class CeoLoginController extends EmployeeBaseController {

	private ArrayList<String> DisableParkingSpotInputs = new ArrayList<>();

	private ArrayList<String> InitializeParkingSpotInputs = new ArrayList<>();

	private ArrayList<String> DisableParkingLotInputs = new ArrayList<>();

	/**
	 * Instantiates a new CEO login controller.
	 */
	public CeoLoginController() {
		super();
		DisableParkingSpotInputs.add("Parking spot column:");
		DisableParkingSpotInputs.add("Parking spot floor:");
		DisableParkingSpotInputs.add("Parking spot row:");
		DisableParkingSpotInputs.add("Parking lot name:");

		InitializeParkingSpotInputs.add("Parking lot name:");
		InitializeParkingSpotInputs.add("Width:");

		DisableParkingLotInputs.add("Parking lot name:");
	}

	@FXML
	private Label Headline;

	/**
	 * Creates a new parking lot and sets its sizes
	 * according to the given inputs.
	 * 
	 * @param event
	 *            the event
	 */
	@FXML
	void OnInitializeParkingLot(ActionEvent event) {
		Dialog<List<String>> dialog = DialogBuilder.InputsDialog(Consts.FillRequest, InitializeParkingSpotInputs,
				Consts.Submit);
		Optional<List<String>> result = dialog.showAndWait();
		result.ifPresent(inputs -> {
			String parkinglotName = inputs.get(0);
			String s_width = inputs.get(1);
			int width = Integer.parseInt(s_width);
			boolean IsValid = Pattern.matches("[0-9]+", inputs.get(1)) && 4 <= Integer.parseInt(inputs.get(1))
					&& Integer.parseInt(inputs.get(1)) <= 8;
			if (IsValid) {
				Parkinglot parkinglot = new Parkinglot(parkinglotName, width, ParkinglotStatus.Open, 5, 4);
				ServerResponse<Parkinglot> ParkinglotRes = RequestsSender.AddParkinglot(parkinglot);
				if (ParkinglotRes.GetRequestResult().equals(RequestResult.Succeed))
					DialogBuilder.AlertDialog(AlertType.INFORMATION, "", ConstsEmployees.ParkingLotWasinitialized, null,
							false);
				else {
					DialogBuilder.AlertDialog(AlertType.ERROR, "", ConstsEmployees.ParkingLotWasAlreadyinitialized,
							null, false);
				}
			} else
				DialogBuilder.AlertDialog(AlertType.ERROR, "", ConstsEmployees.ParkingLotSizeError, null, false);
		});

	}

	/**
	 * Disables a selected parking lot.
	 * 
	 * @param event
	 *            the event
	 */
	@FXML
	void OnRigisterDisabledParkingLot(ActionEvent event) {
		Dialog<List<String>> dialog = DialogBuilder.InputsDialog(Consts.FillRequest, DisableParkingLotInputs,
				Consts.Submit);
		Optional<List<String>> result = dialog.showAndWait();
		result.ifPresent(inputs -> {
			String parkinglotName = inputs.get(0);
			// parkinglotName= parkinglotName.toLowerCase();
			ServerResponse<Parkinglot> ParkinglotRes = RequestsSender.GetParkinglot(parkinglotName);
			if (!ParkinglotRes.GetRequestResult().equals(RequestResult.NotFound)) {
				Parkinglot parkinglot = ParkinglotRes.GetResponseObject();
				ParkinglotStatus status = parkinglot.getStatus();
				if (status.equals(ParkinglotStatus.OutOfOrder))
					DialogBuilder.AlertDialog(AlertType.ERROR, "", ConstsEmployees.AlreadyDisabled, null, false);

				else {
					ChangeParkinglotStatusRequest changeParkinglotStatusRequest = new ChangeParkinglotStatusRequest(
							parkinglotName, ParkinglotStatus.OutOfOrder);
					ServerResponse<ChangeParkinglotStatusRequest> ParkinglotDisableRes = RequestsSender
							.ChangeParkinglotStatus(changeParkinglotStatusRequest);

					if (ParkinglotDisableRes.GetRequestResult().equals(RequestResult.Succeed)) {
						DialogBuilder.AlertDialog(AlertType.INFORMATION, "", ConstsEmployees.ParkingLotDisabled, null,
								false);
					} else {
						DialogBuilder.AlertDialog(AlertType.ERROR, "", Consts.ServerProblemMessage, null, false);
					}
				}
			} else
				DialogBuilder.AlertDialog(AlertType.ERROR, "", ConstsEmployees.ParkingLotNotFound, null, false);

		});
	}

	/**
	 * Sets the reserve parking spot scene.
	 * 
	 * @param event
	 *            the event
	 */
	@FXML
	void OnReserveParkingSpot(ActionEvent event) {
		myControllersManager.SetScene(ConstsEmployees.ReserveParkingSpot, ConstsEmployees.CeoLogin);
	}

	/**
	 * Disables a selected parking spot.
	 * 
	 * @param event
	 *            the event
	 */
	@FXML
	void OnRegisterDisabeledParkingSpot(ActionEvent event) {
		Dialog<List<String>> dialog = DialogBuilder.InputsDialog(Consts.FillRequest, DisableParkingSpotInputs,
				Consts.Submit);
		Optional<List<String>> result = dialog.showAndWait();
		result.ifPresent(inputs -> {
			String parkinglotName = inputs.get(3);
			// parkinglotName= parkinglotName.toLowerCase();
			ServerResponse<Parkinglot> ParkinglotRes = RequestsSender.GetParkinglot(parkinglotName);
			if (!ParkinglotRes.GetRequestResult().equals(RequestResult.NotFound)) {
				Parkinglot parkinglot = ParkinglotRes.GetResponseObject();
				ParkinglotStatus status = parkinglot.getStatus();
				if (status.equals(ParkinglotStatus.OutOfOrder))
					DialogBuilder.AlertDialog(AlertType.ERROR, "", ConstsEmployees.ParkingLotIsDisabled, null, false);
				else {
					boolean temp1 = Pattern.matches("[0-9]+", inputs.get(0))
							&& parkinglot.getWidth() >= Integer.parseInt(inputs.get(0))
							&& 0 < Integer.parseInt(inputs.get(0));
					boolean temp2 = Pattern.matches("[0-9]+", inputs.get(1))
							&& parkinglot.getHeight() >= Integer.parseInt(inputs.get(1))
							&& 0 < Integer.parseInt(inputs.get(1));
					boolean temp3 = Pattern.matches("[0-9]+", inputs.get(2))
							&& parkinglot.getDepth() >= Integer.parseInt(inputs.get(2))
							&& 0 < Integer.parseInt(inputs.get(2));
					if (temp1 && temp2 && temp3) {
						ParkingSpot parkingspot = new ParkingSpot(Integer.parseInt(inputs.get(0)),
								Integer.parseInt(inputs.get(1)), Integer.parseInt(inputs.get(2)));

						ChangeParkingSpotStatusRequest changeParkingSpotStatusRequest = new ChangeParkingSpotStatusRequest(
								parkingspot, parkinglotName, ParkingSpotStatus.Disabled);
						ServerResponse<ChangeParkingSpotStatusRequest> ChangeParkingSpotStatusRes = RequestsSender
								.ChangeParkingSpotStatus(changeParkingSpotStatusRequest);
						if (ChangeParkingSpotStatusRes.GetRequestResult().equals(RequestResult.Succeed))
							DialogBuilder.AlertDialog(AlertType.INFORMATION, "", ConstsEmployees.ParkingSpotDisabled,
									null, false);
						else if (ChangeParkingSpotStatusRes.GetRequestResult().equals(RequestResult.NotFound))
							DialogBuilder.AlertDialog(AlertType.ERROR, "", ConstsEmployees.ParkingSpotAlreadyDisabled,
									null, false);
						else
							DialogBuilder.AlertDialog(AlertType.ERROR, "", Consts.ServerProblemMessage, null, false);

					} else
						DialogBuilder.AlertDialog(AlertType.ERROR, "", ConstsEmployees.FieldWrong, null, false);
				}
			} else
				DialogBuilder.AlertDialog(AlertType.ERROR, "", ConstsEmployees.ParkingLotNotFound, null, false);
		});
	}

	/**
	 * Sets the manage request rate change scene.
	 * 
	 * @param event
	 *            the event
	 */
	@FXML
	void OnUpdatePrices(ActionEvent event) {
		myControllersManager.SetScene(ConstsEmployees.ManageRequestRateChange, ConstsEmployees.CeoLogin);
	}

	/**
	 * Sets the produce report scene.
	 * 
	 * @param event
	 *            the event
	 */
	@FXML
	void OnProduceReport(ActionEvent event) {
		myControllersManager.SetScene(ConstsEmployees.ProduceReport, ConstsEmployees.CeoLogin);
	}

	/**
	 * Enables a selected parking lot.
	 * 
	 * @param event
	 *            the event
	 */
	@FXML
	void OnUndisableParkingLot(ActionEvent event) {
		Dialog<List<String>> dialog = DialogBuilder.InputsDialog(Consts.FillRequest, DisableParkingLotInputs,
				Consts.Submit);
		Optional<List<String>> result = dialog.showAndWait();
		result.ifPresent(inputs -> {
			String parkinglotName = inputs.get(0);
			// parkinglotName= parkinglotName.toLowerCase();
			ServerResponse<Parkinglot> ParkinglotRes = RequestsSender.GetParkinglot(parkinglotName);
			if (!ParkinglotRes.GetRequestResult().equals(RequestResult.NotFound)) {
				Parkinglot parkinglot = ParkinglotRes.GetResponseObject();
				ParkinglotStatus status = parkinglot.getStatus();
				if (status.equals(ParkinglotStatus.Open))
					DialogBuilder.AlertDialog(AlertType.ERROR, "", ConstsEmployees.Alreadyinabled, null, false);

				else {
					ChangeParkinglotStatusRequest changeParkinglotStatusRequest = new ChangeParkinglotStatusRequest(
							parkinglotName, ParkinglotStatus.Open);
					ServerResponse<ChangeParkinglotStatusRequest> ParkinglotDisableRes = RequestsSender
							.ChangeParkinglotStatus(changeParkinglotStatusRequest);

					if (ParkinglotDisableRes.GetRequestResult().equals(RequestResult.Succeed)) {
						DialogBuilder.AlertDialog(AlertType.INFORMATION, "", ConstsEmployees.ParkingLotIsInabled, null,
								false);
					} else {
						DialogBuilder.AlertDialog(AlertType.ERROR, "", Consts.ServerProblemMessage, null, false);
					}
				}
			} else
				DialogBuilder.AlertDialog(AlertType.ERROR, "", ConstsEmployees.ParkingLotNotFound, null, false);
		});
	}

	/**
	 * Enables a selected parking spot.
	 * 
	 * @param event
	 *            the event
	 */
	@FXML
	void OnUndisableParkingSpot(ActionEvent event) {
		Dialog<List<String>> dialog = DialogBuilder.InputsDialog(Consts.FillRequest, DisableParkingSpotInputs,
				Consts.Submit);
		Optional<List<String>> result = dialog.showAndWait();
		result.ifPresent(inputs -> {
			String parkinglotName = inputs.get(3);
			// parkinglotName= parkinglotName.toLowerCase();
			ServerResponse<Parkinglot> ParkinglotRes = RequestsSender.GetParkinglot(parkinglotName);
			if (!ParkinglotRes.GetRequestResult().equals(RequestResult.NotFound)) {
				Parkinglot parkinglot = ParkinglotRes.GetResponseObject();
				ParkinglotStatus status = parkinglot.getStatus();
				if (status.equals(ParkinglotStatus.OutOfOrder))
					DialogBuilder.AlertDialog(AlertType.ERROR, "", ConstsEmployees.ParkingLotIsDisabled, null, false);
				else {

					boolean temp1 = Pattern.matches("[0-9]+", inputs.get(0))
							&& parkinglot.getWidth() >= Integer.parseInt(inputs.get(0))
							&& 0 < Integer.parseInt(inputs.get(0));
					boolean temp2 = Pattern.matches("[0-9]+", inputs.get(1))
							&& parkinglot.getHeight() >= Integer.parseInt(inputs.get(1))
							&& 0 < Integer.parseInt(inputs.get(1));
					boolean temp3 = Pattern.matches("[0-9]+", inputs.get(2))
							&& parkinglot.getDepth() >= Integer.parseInt(inputs.get(2))
							&& 0 < Integer.parseInt(inputs.get(2));
					if (temp1 && temp2 && temp3) {
						ParkingSpot parkingspot = new ParkingSpot(Integer.parseInt(inputs.get(0)),
								Integer.parseInt(inputs.get(1)), Integer.parseInt(inputs.get(2)));
						ChangeParkingSpotStatusRequest changeParkingSpotStatusRequest = new ChangeParkingSpotStatusRequest(
								parkingspot, parkinglotName, ParkingSpotStatus.Active);
						ServerResponse<ChangeParkingSpotStatusRequest> ChangeParkingSpotStatusRes = RequestsSender
								.ChangeParkingSpotStatus(changeParkingSpotStatusRequest);
						if (ChangeParkingSpotStatusRes.GetRequestResult().equals(RequestResult.Succeed))
							DialogBuilder.AlertDialog(AlertType.INFORMATION, "", ConstsEmployees.parkingSpotInabled,
									null, false);
						else if (ChangeParkingSpotStatusRes.GetRequestResult().equals(RequestResult.NotFound))
							DialogBuilder.AlertDialog(AlertType.ERROR, "", ConstsEmployees.ParkingSpotAlreadyEnabled,
									null, false);
						else
							DialogBuilder.AlertDialog(AlertType.ERROR, "", Consts.ServerProblemMessage, null, false);
					} else
						DialogBuilder.AlertDialog(AlertType.ERROR, "", ConstsEmployees.FieldWrong, null, false);

				}
			} else
				DialogBuilder.AlertDialog(AlertType.ERROR, "", ConstsEmployees.ParkingLotNotFound, null, false);
		});
	}

	/**
	 * Sets the previous scene.
	 * 
	 * @param event
	 *            the event
	 */
	@FXML
	void OnBack(ActionEvent event) {
		LogOut();

		myControllersManager.Back(PreviousScene, ConstsEmployees.CeoLogin);
	}

}
