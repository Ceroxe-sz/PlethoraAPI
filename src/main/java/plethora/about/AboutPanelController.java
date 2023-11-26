package plethora.about;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AboutPanelController {
    public Label getAuthorLabel() {
        return authorLabel;
    }

    public Label getLicenceLabel() {
        return licenceLabel;
    }

    public Label getVersionLabel() {
        return versionLabel;
    }

    @FXML
    private Label authorLabel;

    @FXML
    private Label licenceLabel;

    @FXML
    private Label versionLabel;

}
