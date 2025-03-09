package com.example.csc325tipcalc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
public class HelloController {
    // formatters for currency and percentages
    private static final NumberFormat currency =
            NumberFormat.getCurrencyInstance();
    private static final NumberFormat percent =
            NumberFormat.getPercentInstance();

    private BigDecimal tipPercentage = new BigDecimal(0.15); // 15% default

    // GUI controls defined in FXML and used by the controller's code
    @FXML
    private TextField amountTextField;

    @FXML
    private Button calcButton;


    @FXML
    private Label tipPercentageLabel;

    @FXML
    private Slider tipPercentageSlider;

    @FXML
    private TextField tipTextField;

    @FXML
    private TextField totalTextField;

    // calculates and displays the tip and total amounts
    @FXML
    private void calculateButtonPressed(ActionEvent event) {

        try {
            BigDecimal amount = new BigDecimal(amountTextField.getText());
            BigDecimal tip = amount.multiply(tipPercentage);
            BigDecimal total = amount.add(tip);

            tipTextField.setText(currency.format(tip));
            totalTextField.setText(currency.format(total));
        }
        catch (NumberFormatException ex) {
            amountTextField.setText("Enter amount");
            amountTextField.selectAll();
            amountTextField.requestFocus();
        }
    }

    // called by FXMLLoader to initialize the controller
    public void initialize() {
//Gave the button a fx ID and disabled it to make sure it wasnt using the button
calcButton.setDisable(true);
        // 0-4 rounds down, 5-9 rounds up
        currency.setRoundingMode(RoundingMode.HALF_UP);

        //I tried using Double but had some errors in some places
        tipPercentageLabel.textProperty().bind( Bindings.createStringBinding(() -> percent.format(BigDecimal.valueOf(tipPercentageSlider.getValue() / 100.0)), tipPercentageSlider.valueProperty()));

        ObjectBinding<BigDecimal> tipPercentageBinding = Bindings.createObjectBinding(() -> BigDecimal.valueOf(tipPercentageSlider.getValue() / 100.0), tipPercentageSlider.valueProperty());

        ObjectBinding<BigDecimal> amountBinding = Bindings.createObjectBinding(() -> {
            try {
                return new BigDecimal(amountTextField.getText());
            } catch (NumberFormatException e) {
                return BigDecimal.ZERO;}
        }, amountTextField.textProperty());
        //Create binding for tip
        ObjectBinding<BigDecimal> tipBinding = Bindings.createObjectBinding(() -> amountBinding.get().multiply(tipPercentageBinding.get()), amountBinding, tipPercentageBinding);

        // Created a binding for total
        ObjectBinding<BigDecimal> totalBinding = Bindings.createObjectBinding(() -> amountBinding.get().add(tipBinding.get()), amountBinding, tipBinding);

        // Using tip binding
        tipTextField.textProperty().bind( Bindings.createStringBinding(() -> currency.format(tipBinding.get()), tipBinding));
        //Using the total binding
        totalTextField.textProperty().bind(Bindings.createStringBinding(() -> currency.format(totalBinding.get()), totalBinding));

        // Value for the tip bar
        tipPercentage = BigDecimal.valueOf(tipPercentageSlider.getValue() / 100.0);


        //Commenting out the listener
        /**    // listener for changes to tipPercentageSlider's value
            tipPercentageSlider.valueProperty().addListener(
                    new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                            tipPercentage = BigDecimal.valueOf(newValue.intValue() / 100.0);
                            tipPercentageLabel.setText(percent.format(tipPercentage));
        */

                    }
                }

