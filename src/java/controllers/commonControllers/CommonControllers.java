package controllers.commonControllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * This class contains all the common functionalities across all the controllers
 */
abstract public class CommonControllers {
    protected final double OPACITY_LOWER = 0.4;
    protected final double OPACITY_HIGHER = 1;

    /**
     * When the mouse cursor hovers on the button, decrease its opacity
     *
     * @param event It is used to change the opacity
     */
    @FXML
    protected void buttonHovered(MouseEvent event) {
        ((Button) event.getSource()).setOpacity(OPACITY_LOWER);
    }

    /**
     * When the mouse cursor leaves on the button, increase its opacity
     * But if the button is disable, then don't do anything (don't increase its opacity)
     *
     * @param event It is used to change the opacity
     */
    @FXML
    protected void buttonExited(MouseEvent event) {
        if (!((Button) event.getSource()).isDisable()) {
            ((Button) event.getSource()).setOpacity(OPACITY_HIGHER);
        }
    }

    /**
     * Disable/un-disable all the items in the array depends on the boolean disable,
     * at the same time, change the item opacity if opacity needs to change
     *
     * @param disable       Disable/un-disable the items depends on this boolean
     * @param opacityChange Whether or not to change the items opacity depends on this boolean
     * @param items         Items that they are are going to disable/un-disable
     */
    protected void disableItems(boolean disable, boolean opacityChange, Node... items) {
        double opacity = OPACITY_HIGHER;
        if (disable) {
            opacity = OPACITY_LOWER;
        }

        for (Node item : items) {
            item.setDisable(disable);
            if (opacityChange) {
                item.setOpacity(opacity);
            }
        }
    }

    /**
     * Disable/un-disable all the items in the array depends on the boolean disable,
     * at the same time, change the item opacity, opacity will definitely change
     *
     * @param disable Disable/un-disable the items depends on this boolean
     * @param items   Items that they are are going to disable/un-disable
     */
    protected void disableItems(boolean disable, Node... items) {
        disableItems(disable, true, items);
    }
}
