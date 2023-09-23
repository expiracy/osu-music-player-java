package com.expiracy.osumusicplayer.components;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

import java.io.File;

public class ControlButton extends Button {
    private boolean pressed = false;

    public ControlButton(String label) {
        super(label);
        super.getStyleClass().add("control-button");
    }

    public void press() {
        this.pressed = !this.pressed;

        if (this.pressed) {
            this.getStyleClass().add("control-button-pressed");
        } else {
            this.getStyleClass().remove("control-button-pressed");
        }
    }

}
