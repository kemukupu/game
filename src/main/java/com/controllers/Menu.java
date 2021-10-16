package com.controllers;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.ApplicationController;
import com.MainApp;
import com.components.animations.SlideComponentHorizontal;
import com.enums.Modals;
import com.enums.View;
import com.util.Sounds;

import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

/**
 * This class is the controller for the menu screen
 */
public class Menu extends ApplicationController implements Initializable {
    @FXML private ImageView exitGameButton;
    @FXML private ImageView enterTopicSelectButton;
    @FXML private ImageView settingsButton;
    @FXML private ImageView profileButton;
    @FXML private ImageView helpButton;
    @FXML private ImageView settingsLabel;
    @FXML private ImageView profileLabel;
    @FXML private ImageView helpLabel;
    private ImageView[] animated = new ImageView[4];

    private void onAnimateOut(Duration dur, double delta){
        animateImage(dur, delta, 0, 1);
    }

    private void animateImage(Duration dur, double delta, int index, int direction){
        Animation anim = new SlideComponentHorizontal(animated[index], dur, delta * direction).getAnimator();

        if (index + 1 >= animated.length){
            anim.setOnFinished(e -> transition());
        } else {
            if (direction == 1) {
                anim.setOnFinished(e -> animateImage(dur, delta, index + 1, direction * -1));
            } else {
                anim.setOnFinished(e -> animateImage(dur, delta, index + 1, direction));
            }
        }

        anim.play();
    }
    
    private void transition(){
        MainApp.setRoot(View.GAMEMODE);
        Sounds.playSoundEffect("pop");
    }

    @Override
    protected void start() {
        animated[0] = exitGameButton;
        animated[3] = helpButton;
        animated[1] = settingsButton;
        animated[2] = profileButton;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Inital setup & loading of data
        super.initialize();

        Sounds.playMusic("menu");

        //Set event handlers
        //exiting
        exitGameButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Sounds.playSoundEffect("pop");
            MainApp.setRoot(View.EXIT);
            event.consume();

            //pause and exit
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> Platform.exit());
            pause.play();
        });


        String[] iconButtons = {"settingsButton", "profileButton", "helpButton"};
        List<Node> icons = findNodesByID(anchorPane, iconButtons);
        icons.forEach(i -> {
            i.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                String labelId = i.getId().replace("Button", "Label");
                findNodesByID(anchorPane, labelId).setVisible(true);
            });
            i.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
                String labelId = i.getId().replace("Button", "Label");
                findNodesByID(anchorPane, labelId).setVisible(false);
            });
        });

        settingsButton.addEventHandler(MouseEvent.MOUSE_CLICKED, _event -> {
            Sounds.playSoundEffect("pop");
            super.settingsClick();
        });

        profileButton.addEventHandler(MouseEvent.MOUSE_CLICKED, _event -> {
            Sounds.playSoundEffect("pop");
            MainApp.showModal(Modals.PROFILE);
        });

        helpButton.addEventHandler(MouseEvent.MOUSE_CLICKED, _event -> {
            Sounds.playSoundEffect("pop");
            MainApp.showModal(Modals.HELP);
        });

        enterTopicSelectButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            onAnimateOut(Duration.millis(300), 120);
            event.consume();
        });
        //open attributions modal
        // infoButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
        //     Sounds.playSoundEffect("pop");
        //     MainApp.showModal(Modals.ATTRIBUTION);
        // });
    }    
}
