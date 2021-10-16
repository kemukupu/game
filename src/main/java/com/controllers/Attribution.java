package com.controllers;
import java.net.URL;
import java.util.ResourceBundle;

import com.ApplicationController;
import com.MainApp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * This class is the controller for the attributions modal.
 */
public class Attribution extends ApplicationController implements Initializable {
    @FXML ImageView exitButton;

    /**
     * Close this modal
     */
    private void __close() {
        MainApp.closeModal();
    }
    
    //// Public Methods ////

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Inital setup & loading of data
        super.initialize();
        
        //Set event handlers
        this.exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, _event -> this.__close());  
    }
}