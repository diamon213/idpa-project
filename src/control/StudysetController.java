package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Mastery;
import model.Student;
import model.StudyMode;
import model.Studyset;

import java.io.FileInputStream;
import java.io.IOException;
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
    private Label masteryLabel;

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
    void resetMastery() throws IOException {
        for (Student student: currentStudyset.getStudents()) {
            student.setMastery(Mastery.UNKNOWN);
        }
        currentStudyset.setMastery(currentStudyset.calcMastery());

        //TODO update mastery of all students in current studyset in DB

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/studyset.fxml"));
        Parent root = loader.load();

        Stage primaryStage = (Stage) vbox.getScene().getWindow();
        StudysetController controller = loader.getController();
        controller.initData(studysets, currentStudyset);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
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
        if (currentStudyset.getStudents().size() == 0) {
            showAlert("Das Lernset hat gar keine Personen.");
        } else {
            startFlashcards();
        }
    }

    @FXML
    void pressStudyFirstnames(ActionEvent event) {

        currentStudyset.setMastery(currentStudyset.calcMastery());

        if (currentStudyset.getStudents().size() == 0) {
            showAlert("Das Lernset hat gar keine Personen.");
        } else if (currentStudyset.getMastery() != 100) {
            startStudyMode(StudyMode.FIRSTNAME);
        } else {
            showAlert("Du hast schon alle Namen gemeistert! :)");
        }
    }

    @FXML
    void pressStudyLastnames(ActionEvent event) {

        currentStudyset.setMastery(currentStudyset.calcMastery());

        if (currentStudyset.getStudents().size() == 0) {
            showAlert("Das Lernset hat gar keine Schüler.");
        } else if (currentStudyset.getMastery() != 100) {
            startStudyMode(StudyMode.LASTNAME);
        } else {
            showAlert("Du hast schon alle Namen gemeistert! :)");
        }
    }

    @FXML
    void pressAssign(ActionEvent event) {

        currentStudyset.setMastery(currentStudyset.calcMastery());

        if (currentStudyset.getStudentCount() > 3) {
            startStudyMode(StudyMode.ASSIGN);
        } else {
            showAlert("Das Lernset hat zu wenig Schüler für diesen Modus");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Warnung...");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(vbox.getScene().getWindow());
        alert.show();
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

    void startStudyMode(StudyMode mode) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/study.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();

            StudyController controller = fxmlLoader.getController();
            controller.initData(studysets, currentStudyset, mode);

            stage.setTitle("Lernmodus");
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(vbox.getScene().getWindow());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleteStudyset(ActionEvent event) throws IOException {

        studysets.remove(currentStudyset);

        //TODO remove current studyset from DB

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

            studyset.setMastery(studyset.calcMastery());
            String masteryLabelText = String.format("%.0f", studyset.getMastery());
            masteryLabel.setText("Lernstand: " + masteryLabelText + "%");

            hbox.setSpacing(10);
            hbox.setMinSize(446, 40);
            hbox.setStyle("-fx-background-color:  #91AA9D");

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

            searchbar.setStyle("" +
                    "-fx-text-fill: white;" +
                    "-fx-background-color:  #3c3c44");

            vbox.setSpacing(5);
            vbox.getChildren().add(hbox);
        }
    }
}