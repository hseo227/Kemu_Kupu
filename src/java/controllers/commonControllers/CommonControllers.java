package controllers.commonControllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * This class contains all the common functionalities across all the controllers
 */
abstract public class CommonControllers {
    protected final double opacityLower = 0.5;
    protected final double opacityHigher = 1;

    /**
     * When the mouse cursor hovers on the button, decrease its opacity
     * @param event It is used to change the opacity
     */
    @FXML
    protected void buttonHovered(MouseEvent event) {
        ((Button) event.getSource()).setOpacity(opacityLower);
    }

    /**
     * When the mouse cursor leaves on the button, increase its opacity
     * But if the button is disable, then don't do anything (don't increase its opacity)
     * @param event It is used to change the opacity
     */
    @FXML
    protected void buttonExited(MouseEvent event) {
        if (!((Button) event.getSource()).isDisable()){
            ((Button) event.getSource()).setOpacity(opacityHigher);
        }
    }

    /**
     * Disable/un-disable all the items in the array depends on the boolean disable,
     * at the same time, change the item opacity
     * @param disable Disable/un-disable the items depends on this boolean
     * @param items Items that they are are going to disable/un-disable
     */
    protected void disableItems(boolean disable, Node... items) {
        double opacity = opacityHigher;
        if (disable) {
            opacity = opacityLower;
        }

        for (Node item : items) {
            item.setDisable(disable);
            item.setOpacity(opacity);
        }
    }
}
