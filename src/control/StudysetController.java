package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Student;
import model.Studyset;

import java.io.FileInputStream;
import java.io.IOException;
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
    private VBox vbox;

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

    public void initData(Vector<Studyset> studysets, Studyset studyset) throws IOException {

        Image masteredImg = new Image(new FileInputStream("./src/assets/mastered.png"));
        Image knownImg = new Image(new FileInputStream("./src/assets/known.png"));
        Image unknownImg = new Image(new FileInputStream("./src/assets/unknown.png"));

        this.studysets = studysets;
        this.currentStudyset = studyset;
        titleLabel.setText(studyset.getStudysetName());
        for (Student student : studyset.getStudents()) {

            HBox hbox = new HBox();
            Label salutation = new Label(student.getSalutation());
            Label firstName = new Label(student.getFirstName());
            Label lastName = new Label(student.getLastName());
            ImageView masteryImg = new ImageView();

            switch (student.getMastery()) {
                case UNKNOWN -> masteryImg.setImage(unknownImg);
                case KNOWN -> {
                    masteryImg.setImage(knownImg);
                }
                case MASTERED -> {
                    masteryImg.setImage(masteredImg);
                }
            }

            hbox.setSpacing(10);
            hbox.setPrefSize(446, 40);

            salutation.setPrefSize(66, 40);

            firstName.setPrefSize(141, 40);

            lastName.setPrefSize(176, 40);

            masteryImg.setFitWidth(20);
            masteryImg.setFitHeight(20);

            hbox.getChildren().add(salutation);
            hbox.getChildren().add(firstName);
            hbox.getChildren().add(lastName);
            hbox.getChildren().add(masteryImg);

            HBox.setMargin(masteryImg, new Insets(10,20,0,0));
            HBox.setMargin(salutation, new Insets(0,0,0,30));

            vbox.getChildren().add(hbox);
        }
    }
}