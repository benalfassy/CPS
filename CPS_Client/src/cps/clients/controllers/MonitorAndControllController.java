package cps.clients.controllers;

import cps.utilities.Consts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

// TODO: Auto-generated Javadoc
/**
 * The Class MonitorAndControllController.
 */
public class MonitorAndControllController extends BaseController
{
    
    @FXML
    private Label Headline;
    
    /**
     * Sets the monitor and control member scene.
     *
     * @param event
     *            the event
     */
    @FXML
    void OnMember(ActionEvent event)
    {
	myControllersManager.SetScene(Consts.MonitorAndControllMember, Consts.MonitorAndControll);
    }
    
    /**
     * Sets the monitor and control not member scene.
     *
     * @param event
     *            the event
     */
    @FXML
    void OnNotAMember(ActionEvent event)
    {
	myControllersManager.SetScene(Consts.MonitorAndControllNotMember, Consts.MonitorAndControll);
    }
    
    /**
     * Sets the monitor and control customer scene.
     *
     * @param event
     *            the event
     */
    @FXML
    void OnCustomerInfo(ActionEvent event)
    {
	myControllersManager.SetScene(Consts.MonitorAndControlCustomer, Consts.MonitorAndControll);
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
	myControllersManager.Back(PreviousScene, Consts.MonitorAndControll);
    }
}
