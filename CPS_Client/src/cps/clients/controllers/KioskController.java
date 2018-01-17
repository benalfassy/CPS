package cps.clients.controllers;

import java.util.ArrayList;

import cps.utilities.Consts;
import cps.utilities.DialogBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;

// TODO: Auto-generated Javadoc
/**
 * The Class KioskController.
 */
public class KioskController extends BaseController
{
    
    private ArrayList<String> subscriptionTypes = new ArrayList<>();
    
    /**
     * Instantiates a new kiosk controller.
     */
    public KioskController()
    {
	subscriptionTypes.add(Consts.FullMembership);
	subscriptionTypes.add(Consts.PartialMembership);
    }
    
    @FXML
    private Label Headline;
    
    /**
     * Sets the kiosk entry scene.
     *
     * @param event
     *            the event
     */
    @FXML
    void OnEnter(ActionEvent event)
    {
	myControllersManager.SetScene(Consts.KioskEntry, Consts.Kiosk);
    }
    
    /**
     * Sets the kiosk exit scene.
     *
     * @param event
     *            the event
     */
    @FXML
    void OnExit(ActionEvent event)
    {
	myControllersManager.SetScene(Consts.KioskExit, Consts.Kiosk);
    }
    
    /**
     * Sets a dialog window with the options to register a full or a partial membership.
     *
     * @param event
     *            the event
     */
    @FXML
    void OnRegister(ActionEvent event)
    {
	String buttonResult = DialogBuilder.AlertDialog(AlertType.NONE, "Register", "Please choose a subscription type",
		subscriptionTypes, true);
	
	switch (buttonResult)
	{
	case Consts.FullMembership:
	    myControllersManager.SetScene(Consts.FullMembershipRegister, Consts.Kiosk);
	    break;
	
	case Consts.PartialMembership:
	    myControllersManager.SetScene(Consts.PartialMembershipRegister, Consts.Kiosk);
	    break;
	
	default:
	    break;
	}
    }
    
    /**
     * Sets the monitor and control scene.
     *
     * @param event
     *            the event
     * @throws InterruptedException
     *             the interrupted exception
     */
    @FXML
    void OnMonitorAndControll(ActionEvent event) throws InterruptedException
    {
	
	myControllersManager.SetScene(Consts.MonitorAndControll, Consts.Kiosk);
    }
}
