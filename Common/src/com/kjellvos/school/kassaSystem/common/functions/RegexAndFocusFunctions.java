package com.kjellvos.school.kassaSystem.common.functions;

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

    //TODO Change to HH:MM:SS
    public void doTimeRegex(TextField textField, String oldValue, String newValue){
        if (newValue.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
            textField.setText(newValue);
        }else{
            if (oldValue != null) {
                if (textField.isFocused() && (newValue.matches("[0-2]{1}+:+[0-5]{1}+[0-9]{1}") || newValue.matches("[0-4]{1}+:+[0-5]{1}+[0-9]{1}") || newValue.matches("[0-2]{1}+:+[0-5]{1}+[0-9]{1}") || newValue.matches("[0-2]{1}+[0-3]{1}+:+[0-9]{1}") || newValue.matches("[0-2]{1}+[0-3]{1}+:+[0-5]{1}"))){
                    textField.setText(newValue);
                }else{
                    textField.setText(oldValue);
                }
            }else{
                textField.setText("");
            }
        }
    }

    //TODO Change to HH:MM:SS
    public void catchWrongInputOnFocusLeaveTime(TextField textField, boolean newValue){
        if(!newValue){
            if (!textField.getText().matches("[0-2]+[0-4]+:+[0-9]{2}")) {
                String[] values = textField.getText().split(":");
                if(values[0].matches("[0-9]{1}")){
                    values[0] = "0" + values[0];
                }else if (values[0].matches("[0-2]{0}")) {
                    values[0] = "00";
                }
                if(values[1].matches("[0-5]{1}")){
                    values[1] = "0" + values[1];
                }else if (values[1].matches("[0-5]{0}")) {
                    values[1] = "00";
                }
                textField.setText(values[0] + ":" + values[1]);
            }
        }
    }
}
