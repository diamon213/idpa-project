package control;

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

/**
 * Der Controller für die Lernset-Ansicht
 * <p>
 * Dient als Controller für die View studyset.fxml
 *
 * @author Aladin Boudouda
 *
 */
public class StudysetController {

    private Vector<Studyset> studysets;
    private Studyset currentStudyset;

    @FXML
    private TextField searchbar;

    @FXML
    private Label titleLabel;

    @FXML
    private VBox vbox;

    @FXML
    private Label masteryLabel;

    /**
     * Methode bei Knopfdruck "Bearbeiten"
     * <p>
     * Die Methode soll zur Bearbeitungssansicht führen
     *
     */
    @FXML
    public void editStudyset() {
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

    /**
     * Methode bei Knopfdruck "Home"
     * <p>
     * Die Methode führt zu Startseite zurück
     *
     */
    @FXML
    public void pressHome() throws IOException {
        navigate("../view/home.fxml");
    }

    /**
     * Methode um durch Seiten zu navigieren
     * <p>
     * Die Methode führt zur gegebenen Seite
     *
     * @param viewPath gegebene Seite
     */
    private void navigate(String viewPath) throws IOException {

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

    /**
     * Methode bei Knopfdruck "Lernsets"
     * <p>
     * Die Methode soll zur Lernsets-Übersicht führen
     *
     */
    @FXML
    public void pressStudysets() throws IOException {
        navigate("../view/overview.fxml");
    }

    /**
     * Methode bei Knopfdruck "Lernstand zurücksetzen"
     * <p>
     * Die Methode setzt den Lernstand alles Schüler in diesem Lernset zurück
     *
     */
    @FXML
    public void resetMastery() throws IOException {
        for (Student student: currentStudyset.getStudents()) {
            student.setMastery(Mastery.UNKNOWN);
        }
        currentStudyset.setMastery(currentStudyset.calcMastery());

        updateDB(currentStudyset);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/studyset.fxml"));
        Parent root = loader.load();

        Stage primaryStage = (Stage) vbox.getScene().getWindow();
        StudysetController controller = loader.getController();
        controller.initData(studysets, currentStudyset);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Methode bei Eingabe in die Suchleiste
     * <p>
     * Die Methode filtert die Liste bei jeder Eingabe in die Suchleiste
     *
     */
    @FXML
    public void keyTyped() {
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

    /**
     * Methode bei Knopfdruck "Quick Karteikarten"
     * <p>
     * Die Methode soll zum Karteikarten-Modus führen
     *
     */
    @FXML
    public void pressFlashcards() {
        if (currentStudyset.getStudents().size() == 0) {
            showAlert("Das Lernset hat gar keine Personen.");
        } else {
            startFlashcards();
        }
    }

    /**
     * Methode bei Knopfdruck "Vornamen lernen"
     * <p>
     * Die Methode soll zum Vornamen-Modus führen
     *
     */
    @FXML
    public void pressStudyFirstnames() {

        currentStudyset.setMastery(currentStudyset.calcMastery());

        if (currentStudyset.getStudents().size() == 0) {
            showAlert("Das Lernset hat gar keine Personen.");
        } else if (currentStudyset.getMastery() != 100) {
            startStudyMode(StudyMode.FIRSTNAME);
        } else {
            showAlert("Du hast schon alle Namen gemeistert! :)");
        }
    }

    /**
     * Methode bei Knopfdruck "Nachnamen lernen"
     * <p>
     * Die Methode soll zum Nachnamen-Modus führen
     *
     */
    @FXML
    public void pressStudyLastnames() {

        currentStudyset.setMastery(currentStudyset.calcMastery());

        if (currentStudyset.getStudents().size() == 0) {
            showAlert("Das Lernset hat gar keine Schüler.");
        } else if (currentStudyset.getMastery() != 100) {
            startStudyMode(StudyMode.LASTNAME);
        } else {
            showAlert("Du hast schon alle Namen gemeistert! :)");
        }
    }

    /**
     * Methode bei Knopfdruck "Auswahlmodus"
     * <p>
     * Die Methode soll zum Auswahl-Modus führen
     *
     */
    @FXML
    public void pressAssign() {

        currentStudyset.setMastery(currentStudyset.calcMastery());

        if (currentStudyset.getStudentCount() > 3) {
            startStudyMode(StudyMode.ASSIGN);
        } else {
            showAlert("Das Lernset hat zu wenig Schüler für diesen Modus");
        }
    }

    /**
     * Methode für Pop-ups
     * <p>
     * Die Methode öffnet ein Pop-up mit dem gegebenen Text
     *
     * @param message Pop-up Nachricht
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Warnung...");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(vbox.getScene().getWindow());
        alert.show();
    }

    /**
     * Methode um den Karteikarten-Modus zu starten
     * <p>
     * Die Methode startet das Karteikarten-Fenster
     *
     */
    private void startFlashcards() {
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

    /**
     * Methode um einen Lern-Modus zu starten
     * <p>
     * Die Methode startet das je nach angegebenen Modus den Vor-,
     * Nachnamen oder Multiple-Choice-Modus
     *
     * @param mode zu startender Modus
     */
    private void startStudyMode(StudyMode mode) {
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

    /**
     * Methode um das aktuelle Lernset zu löschen
     * <p>
     * Die Methode löscht das aktuelle Lernset aus dem Programm und aus der Datenbank
     *
     */
    @FXML
    private void deleteStudyset() throws IOException {

        studysets.remove(currentStudyset);

        deleteStudysetFromDB(currentStudyset);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/overview.fxml"));
        Parent root = loader.load();

        Stage primaryStage = (Stage) vbox.getScene().getWindow();
        OverviewController controller = loader.getController();
        controller.initData(studysets);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Methode um die Lernsets zu initialisieren
     * <p>
     * Die Methode übergibt dieser Klasse als Parameter
     * die Lernsets und das aktuelle Lernset mit
     *
     * @param studysets sammlung von allen Lernsets
     * @param studyset das aktuelle lernset
     */
    public void initData(Vector<Studyset> studysets, Studyset studyset) throws IOException {

        Image masteredImg = new Image(new FileInputStream("./src/assets/mastered.png"));
        Image knownImg = new Image(new FileInputStream("./src/assets/known.png"));
        Image unknownImg = new Image(new FileInputStream("./src/assets/unknown.png"));

        this.studysets = studysets;
        this.currentStudyset = studyset;
        titleLabel.setText(studyset.getStudysetName());
        searchbar.setStyle("" +
                "-fx-text-fill: white;" +
                "-fx-background-color:  #3c3c44");

        for (Student student : studyset.getStudents()) {

            HBox hbox = new HBox();
            Label salutation = new Label(student.getSalutation());
            Label firstName = new Label(student.getFirstName());
            Label lastName = new Label(student.getLastName());
            ImageView masteryImg = new ImageView();

            switch (student.getMastery()) {
                case UNKNOWN -> masteryImg.setImage(unknownImg);
                case KNOWN -> masteryImg.setImage(knownImg);
                case MASTERED -> masteryImg.setImage(masteredImg);
            }

            studyset.setMastery(studyset.calcMastery());
            String masteryLabelText = String.format("%.0f", studyset.getMastery());
            masteryLabel.setText(masteryLabelText + "%");

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

            vbox.setSpacing(5);
            vbox.getChildren().add(hbox);
        }
    }

    /**
     * Methode um Lernset in der Datenbank zu löschen
     * <p>
     * Die Methode soll das aktuelle Lernset in der Datenbank zu löschen
     *
     * @param studyset Lernset, dass man aktualisieren will
     */
    private void deleteStudysetFromDB(Studyset studyset) {
        //TODO remove current studyset from DB
    }

    /**
     * Methode um Lernset in der Datenbank zu aktualisieren
     * <p>
     * Die Methode soll das aktuelle Lernset in der Datenbank aktualisieren
     *
     * @param studyset Lernset, dass man aktualisieren will
     */
    private void updateDB(Studyset studyset) {
        //TODO update mastery of all students of current studyset in DB
        //iterate through students of studyset and update student's mastery in DB
    }
}