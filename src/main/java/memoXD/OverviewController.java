package memoXD;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Studyset;

import java.io.IOException;
import java.util.Vector;

public class OverviewController {

    private Vector<Studyset> studysets;

    @FXML
    private VBox vbox;

    @FXML
    private TextField searchbar;

    /**
     * Methode bei Eingabe in die Suchleiste
     * <p>
     * Die Methode filtert die Liste bei jeder Eingabe in die Suchleiste
     *
     */
    public void keyTyped() {
        String text = searchbar.getText();

        for (int i = 0; i < vbox.getChildren().size(); i++) {
            Button button = (Button) vbox.getChildren().get(i);
            if (button.getText().toLowerCase().contains(text.toLowerCase())) {
                vbox.getChildren().get(i).setVisible(true);
                vbox.getChildren().get(i).setManaged(true);
            } else {
                vbox.getChildren().get(i).setVisible(false);
                vbox.getChildren().get(i).setManaged(false);
            }
        }
    }

    /**
     * Methode um die Lernset-Liste zu initialisieren
     * <p>
     * Die Methode erstellt für jedes Lernset einen Knopf,
     * der auch zu diesem Lernset führt
     *
     */
    private void initVbox() {
        Font font = new Font("System", 25);

        searchbar.setStyle("" +
                "-fx-text-fill: white;" +
                "-fx-background-color:  #3c3c44");

        for (Studyset studyset : studysets) {
            Button button = new Button();
            button.setText(studyset.getStudysetName());
            button.setFont(font);
            button.setStyle("-fx-background-color: #91AA9D");
            button.setPrefSize(400, 65);
            button.setAlignment(Pos.BASELINE_LEFT);

            button.addEventHandler(ActionEvent.ACTION, (e)-> {
                try {
                    pressStudyset(studyset);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });

            vbox.getChildren().add(button);
        }
    }

    /**
     * Methode bei Knopfdruck "Home"
     * <p>
     * Die Methode führt zu Startseite zurück
     *
     */
    public void pressHome() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("home.fxml"));
        Parent root = loader.load();

        HomeController controller = loader.getController();
        controller.initData(studysets);

        App.setRoot(root);
    }

    /**
     * Methode bei Knopfdruck eines Lernsets
     * <p>
     * Die Methode soll zum angeklickten Lernset führen
     *
     * @param studyset angeklicktes Lernset
     */
    public void pressStudyset(Studyset studyset) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("studyset.fxml"));
        Parent root = fxmlLoader.load();

        StudysetController controller = fxmlLoader.getController();
        controller.initData(studysets, studyset);

        App.setRoot(root);
    }

    /**
     * Methode bei Knopfdruck "Selber zusammenstellen"
     * <p>
     * Die Methode führt zur Lernset-Zusammenstellung
     *
     */
    public void pressAdd() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("editStudyset.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();

            EditStudysetController controller = fxmlLoader.getController();
            controller.initData(studysets, null, "overview.fxml");

            stage.setTitle("Lernset zusammenstellen...");
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(vbox.getScene().getWindow());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Methode bei Knopfdruck "Aus CSV importieren"
     * <p>
     * Die Methode führt zur CSV-Import-Seite
     *
     */
    public void pressImport() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("csvImport.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();

            CsvImportController controller = fxmlLoader.getController();
            controller.initData(studysets);

            stage.setTitle("Klasse importieren...");
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(vbox.getScene().getWindow());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Methode um die Lernsets zu initialisieren
     * <p>
     * Die Methode übergibt dieser Klasse als Parameter
     * die Lernsets mit
     *
     * @param studysets sammlung von allen Lernsets
     */
    public void initData(Vector<Studyset> studysets) {
        this.studysets = studysets;
        initVbox();
    }
}
