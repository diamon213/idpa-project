package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import model.Studyset;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

public class StudysetController implements Initializable {

    public Vector<Studyset> studysets;
    public Studyset currentStudyset;

    @FXML
    private TextField searchbar;

    @FXML
    private Label titleLabel;

    @FXML
    void btnHome(ActionEvent event) {

    }

    @FXML
    void btnLernsets(ActionEvent event) {

    }

    @FXML
    void keyTyped(KeyEvent event) {

    }

    @FXML
    void pressButton(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initData(Vector<Studyset> studysets, Studyset studyset) {
        this.studysets = studysets;
        this.currentStudyset = studyset;
    }
}