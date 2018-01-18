package cps.clients.controllers;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import cps.utilities.Consts;
import cps.utilities.DialogBuilder;
import cps.utilities.InputValidator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

// TODO: Auto-generated Javadoc
/**
 * The Class PaymentController. 
 * Used for all payments.
 */
public class PaymentController extends BaseController
{
    
    @FXML
    private TextField threeSecretNumbers;
    
    @FXML
    private TextArea orderDetails;
    
    @FXML
    private TextField creditCard;
    
    @FXML
    private Label paymentAmount;
    
    @FXML
    private TextField expirationDate;
    
    @FXML
    private ProgressBar prgBar;
    
    private Consumer<Void> afterPaymentDetailsCheck;
    
    /**
     * Initialize.
     */
    @FXML
    void initialize()
    {
	prgBar.setVisible(false);
	orderDetails.setEditable(false);
    }
    
    /**
     * Prints the payment amount.
     *
     * @param paymentAmount the new payment amount
     */
    void setPaymentAmount(float paymentAmount)
    {
	this.paymentAmount.setText(Float.toString(paymentAmount) + "$");
    }
    
    /**
     * Sets the outcome of the payment check.
     *
     * @param afterPaymentDetailsCheck the after payment details check
     */
    void SetOnSubmit(Consumer<Void> afterPaymentDetailsCheck)
    {
	this.afterPaymentDetailsCheck = afterPaymentDetailsCheck;
    }
    
    /**
     * Prints the order's details.
     *
     * @param order the order
     */
    void SetOrderDetails(String order)
    {
	orderDetails.setText(order);
    }
    
    /**
     * Sets the previous scene.
     *
     * @param event the event
     */
    @FXML
    void OnBack(ActionEvent event)
    {
	myControllersManager.Back(PreviousScene, Consts.Payment);
    }
    
    /**
     * Checks if the payment is approved and returns the outcome to the function that called for the payment.
     *
     * @param event the event
     */
    @FXML
    void OnSubmit(ActionEvent event)
    {
	if (IsPaymentDetailsAccepted())
	{
	    
	    prgBar.setVisible(true);
	    
	    CompletableFuture.runAsync(() ->
	    {
		afterPaymentDetailsCheck.accept(null);
		
		Platform.runLater(() ->
		{
		    prgBar.setVisible(false);
		});
	    });
	    
	}
	else
	{
	    DialogBuilder.AlertDialog(AlertType.ERROR, null, Consts.InputsAreIncorrect, null, false);
	}
    }
    
    /**
     * Checks if the payment details are correct.
     *
     * @return true, if successful
     */
    private boolean IsPaymentDetailsAccepted()
    {
	boolean result = true;
	
	if (!InputValidator.CheckVisaDate(expirationDate.getText()))
	{
	    result = false;
	    expirationDate.setStyle("-fx-background-color: tomato;");
	}
	else
	{
	    expirationDate.setStyle("-fx-background-color: white;");
	}
	
	if (!InputValidator.CreditCardNumber(creditCard.getText()))
	{
	    result = false;
	    creditCard.setStyle("-fx-background-color: tomato;");
	}
	else
	{
	    creditCard.setStyle("-fx-background-color: white;");
	}
	
	if (!InputValidator.Ccv(threeSecretNumbers.getText()))
	{
	    result = false;
	    threeSecretNumbers.setStyle("-fx-background-color: tomato;");
	}
	else
	{
	    threeSecretNumbers.setStyle("-fx-background-color: white;");
	}
	
	return result;
    }
}
