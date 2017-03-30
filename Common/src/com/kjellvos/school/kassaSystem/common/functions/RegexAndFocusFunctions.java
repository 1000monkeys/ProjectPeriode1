package com.kjellvos.school.kassaSystem.common.functions;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 * Created by kjevo on 3/25/17.
 */
public class RegexAndFocusFunctions {
    public void doPriceRegex(TextField textField, String oldValue, String newValue){
        if (newValue.matches("[€]+[0-9]{1,10}\\.[0-9]{2}")) {
            textField.setText(newValue);
        }else{
            if (oldValue != null) {
                if (textField.isFocused() && (newValue.matches("[€]+[0-9]{1,10}\\.[0-9]{1}") || newValue.matches("[€]+[0-9]{0}\\.[0-9]{2}"))) {
                    textField.setText(newValue);
                }else{
                    textField.setText(oldValue);
                }
            }else{
                textField.setText("€0.01");
            }
        }
    }

    public void catchWrongInputOnFocusLeavePrice(TextField textField, boolean newValue){
        if(!newValue){
            if (!textField.getText().matches("[€]+[0-9]{1,10}\\.[0-9]{2}")) {
                String[] values = textField.getText().split("\\.");
                if(values[0].equals("€")){
                    values[0] = "0";
                }else{
                    values[0] = values[0].substring(1, values[0].length());
                }
                if(values[1].matches("[0-9]{1}")){
                    values[1] = values[1] + "0";
                }
                textField.setText("€" + values[0] + "." + values[1]);
            }
        }
    }

    public void doTimeRegex(TextField textField, String oldValue, String newValue){
        if (newValue.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
            textField.setText(newValue);
        }else{
            if (oldValue != null) {
                if (textField.isFocused() &&
                (
                    newValue.matches("[0-2]{1}+[0-3]{1}+:+[0-5]{1}+[0-9]{1}+:+[0-5]{1}+[0-9]{1}") ||
                    newValue.matches("[0-2]{1}+:+[0-5]{1}+[0-9]{1}+:+[0-5]{1}+[0-9]{1}") ||
                    newValue.matches("[0-3]{1}+:+[0-5]{1}+[0-9]{1}+:+[0-5]{1}+[0-9]{1}") ||
                    newValue.matches("[0-2]{1}+[0-3]{1}+:+[0-5]{1}+:+[0-5]{1}+[0-9]{1}") ||
                    newValue.matches("[0-2]{1}+[0-3]{1}+:+[0-9]{1}+:+[0-5]{1}+[0-9]{1}") ||
                    newValue.matches("[0-2]{1}+[0-3]{1}+:+[0-5]{1}+[0-9]{1}+:+[0-9]{1}") ||
                    newValue.matches("[0-2]{1}+[0-3]{1}+:+[0-5]{1}+[0-9]{1}+:+[0-5]{1}")
                )){
                    textField.setText(newValue);
                }else{
                    textField.setText(oldValue);
                }
            }else{
                textField.setText("");
            }
        }
    }

    public void catchWrongInputOnFocusLeaveTime(TextField textField, boolean newValue){
        if (!textField.getText().matches(           "[0-2]{1}+[0-3]{1}+:+[0-5]{1}+[0-9]{1}+:+[0-5]{1}+[0-9]{1}")) {
            String[] values = textField.getText().split(":");
            if (values[0].matches("[0-2]{1}") || values[0].matches("[0-3]{1}")) {
                values[0] = "0" + values[0];
            }
            if (values[1].matches("[0-5]{1}") || values[1].matches("[0-9]{1}")) {
                values[1] = "0" + values[1];
            }
            if (values[2].matches("[0-5]{1}") || values[2].matches("[0-9]{1}")) {
                values[2] = "0" + values[2];
            }
            textField.setText(values[0] + ":" + values[1] + ":" + values[2]);
        }
    }

    public void showNothingSelectedAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Niks geselecteerd!");
        alert.setHeaderText("U heeft helemaal geen prijs geselecteerd in het overzicht!");
        alert.setContentText("Selecteer het item en druk dan opnieuw op verwijder!");
        alert.showAndWait();
    }
}
