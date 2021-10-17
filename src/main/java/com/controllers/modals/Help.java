package com.controllers.modals;

import com.controllers.ModalController;
import com.enums.Modals;
import com.util.Modal;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * This class is the controller for the settings modal.
 */
public class Help extends ModalController {

    @FXML ImageView attributionsButton;

    //// Private Methods ////
    private void openAttributions() {
        Modal.showModal(Modals.ATTRIBUTION);
    }

    //// Public Methods ////

    @Override
    public void initializeModal() {
        super.initializeModal();
        this.attributionsButton.addEventHandler(MouseEvent.MOUSE_CLICKED, _event -> openAttributions());
    }
}