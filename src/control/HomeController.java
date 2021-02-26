package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.Studyset;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;


public class HomeController {

    public Vector<Studyset> studysets;

    @FXML
    private Button recent;

    @FXML
    private Button recent1;

    @FXML
    private Button recent2;

    @FXML
    private Button recent3;

    public void pressButton(ActionEvent event) {
        System.out.println(studysets.get(0).getStudysetName());
        recent.setText("Hallo");
        System.out.println(recent.getText());
    }

    public void pressLernsets(ActionEvent event) throws IOException {
        System.out.println("Lernsets");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/overview.fxml"));
        Parent root = loader.load();
        Stage primaryStage = (Stage) recent.getScene().getWindow();

        OverviewController controller = loader.getController();
        controller.initData(studysets);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void initData(Vector<Studyset> studysets) {
        this.studysets = studysets;

    }
}
