package controllers.commonControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * This class contains all the common functionalities across all the controllers
 */
abstract public class CommonControllers {
    /**
     * When the mouse cursor hovers on the button, decrease its opacity
     * @param event It is used to change the opacity
     */
    @FXML
    protected void buttonHovered(MouseEvent event) {
        ((Button) event.getSource()).setOpacity(0.5);
    }

    /**
     * When the mouse cursor leaves on the button, increase its opacity
     * @param event It is used to change the opacity
     */
    @FXML
    protected void buttonExited(MouseEvent event) {
        ((Button) event.getSource()).setOpacity(1);
    }
}
