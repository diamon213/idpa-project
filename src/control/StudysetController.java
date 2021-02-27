package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Student;
import model.Studyset;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

public class StudysetController {

    public Vector<Studyset> studysets;
    public Studyset currentStudyset;

    @FXML
    private TextField searchbar;

    @FXML
    private Label titleLabel;

    @FXML
    private VBox vbox;

    @FXML
    void editStudyset() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/editStudyset.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();

            EditStudysetController controller = fxmlLoader.getController();
            controller.initData(studysets, currentStudyset,  "../view/studyset.fxml");

            stage.setTitle("Lernset bearbeiten...");
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(vbox.getScene().getWindow());
            stage.show();
        } catch (Exception e) {
            System.out.println("Cant load window");
        }

        System.out.println("studyset added");
    }

    @FXML
    void btnHome(ActionEvent event) throws IOException {
        navigate("../view/home.fxml");
    }

    public void navigate(String viewPath) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewPath));
        Parent root = loader.load();

        Stage primaryStage = (Stage) vbox.getScene().getWindow();
        if (viewPath.equals("../view/home.fxml")) {
            HomeController controller = loader.getController();
            controller.initData(studysets);
        } else if (viewPath.equals("../view/overview.fxml")) {
            OverviewController controller = loader.getController();
            controller.initData(studysets);
        } else {
            System.out.println("wrong usage");
            return;
        }

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    @FXML
    void btnLernsets(ActionEvent event) throws IOException {
        navigate("../view/overview.fxml");
    }

    @FXML
    public void keyTyped(KeyEvent event) {
        String text = searchbar.getText();

        for (int i = 0; i < vbox.getChildren().size(); i++) {
            HBox hbox = (HBox) vbox.getChildren().get(i);
            Label firstname = (Label) hbox.getChildren().get(1);
            Label lastname = (Label) hbox.getChildren().get(2);
            if (firstname.getText().toLowerCase().contains(text.toLowerCase()) ||lastname.getText().toLowerCase().contains(text.toLowerCase())) {
                vbox.getChildren().get(i).setVisible(true);
                vbox.getChildren().get(i).setManaged(true);
            } else {
                vbox.getChildren().get(i).setVisible(false);
                vbox.getChildren().get(i).setManaged(false);
            }
        }
    }

    @FXML
    void pressFlashcards(ActionEvent event) {
        initGame(0);
    }
    @FXML
    void pressStudy(ActionEvent event) {
        initGame(1);
    }
    @FXML
    void pressNames(ActionEvent event) {
        initGame(2);
    }
    @FXML
    void pressTest(ActionEvent event) {
        initGame(3);
    }

    public void initGame(int i) {
        switch (i) {
            case 0: startFlashcards();
            case 1: //TODO start studymode
            case 2: //TODO start namemode
            case 3: //TODO start test
        }
    }

    void startFlashcards() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/flashcards.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();

            FlashcardsController controller = fxmlLoader.getController();
            controller.initData(studysets, currentStudyset);

            stage.setTitle("Karteikarten");
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(vbox.getScene().getWindow());
            stage.show();
        } catch (Exception e) {
            System.out.println("Cant load window");
        }
    }

    @FXML
    void deleteStudyset(ActionEvent event) throws IOException {

        studysets.remove(currentStudyset);
        //TODO remove from DB

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/overview.fxml"));
        Parent root = loader.load();

        Stage primaryStage = (Stage) vbox.getScene().getWindow();
        OverviewController controller = loader.getController();
        controller.initData(studysets);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
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

            HBox.setMargin(masteryImg, new Insets(10, 20, 0, 0));
            HBox.setMargin(salutation, new Insets(0, 0, 0, 30));

            vbox.getChildren().add(hbox);
        }
    }
}