package memoXD;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Mastery;
import model.Student;
import model.Studyset;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Der Controller für das CSV-Import-Fenster
 * <p>
 * Dient als Controller für die View csvImport.fxml
 *
 * @author Aladin Boudouda
 *
 */
public class CsvImportController {

    public Vector<Studyset> studysets;
    public Button fileButton;
    public TextField fileTextField;
    public TextField nameTextField;

    /**
     * Methode bei Knopfdruck des Filechoosers
     * <p>
     * Die Methode öffnet den System Filemanager
     * und füllt den Dateipfad der gewählten Datei in das Textfeld
     *
     */
    public void pressFilechooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Dateien", "*.csv"));
        File f = fileChooser.showOpenDialog(null);

        if (f != null) {
            fileTextField.setText(f.getAbsolutePath());
        }
    }

    /**
     * Methode um das Fenster zu schliessen
     * <p>
     * Die Methode schliesst das momentane Fenster
     */
    public void closeWindow() {
        Stage stage = (Stage) fileTextField.getScene().getWindow();
        stage.close();
    }

    /**
     * Methode bei "OK" Knopfdruck
     * <p>
     * Die Methode extrahiert die Eingaben in die Formularfelder
     * und speichert diese in die Datenbank ab
     */
    public void pressOk() throws FileNotFoundException {
        if (fileTextField.getText().isEmpty()) {
            showAlert(fileTextField.getScene().getWindow(),"Bitte geben Sie eine CSV Datei ein.");
            return;
        }
        if (nameTextField.getText().isEmpty()) {
            showAlert(fileTextField.getScene().getWindow(),"Bitte geben Sie einen Namen für das Lernset ein.");
            return;
        }

        Scanner scanner = new Scanner(new File(fileTextField.getText()));

        StringBuilder csv = new StringBuilder();

        while (scanner.hasNext()) {
            String text = scanner.next() + " ";
            csv.append(text);
        }

        Pattern pattern = Pattern.compile(
                "[a-zA-Z_äÄöÖüÜß\"]{1,40};[a-zA-Z0_äÄöÖüÜß \"]{1,40};[a-zA-Z_äÄöÖüÜß \"]{1,40};;;"
        );
        Matcher matcher = pattern.matcher(csv);

        Studyset studyset = new Studyset(nameTextField.getText());
        ArrayList<String> list = new ArrayList<>();

        while ( matcher.find() ) {
            String[] strings = (csv.substring(matcher.start(), matcher.end()).split("[;]{1,3}"));
            for (int i = 0; i < 3; i++) {
                list.add(strings[i].replaceAll("\"", ""));
            }
            Student student = new Student(
                    list.get(2),
                    list.get(1),
                    nameTextField.getText(),
                    list.get(0), Mastery.UNKNOWN
            );

            studyset.addStudent(student);
            list.clear();
        }
        studysets.add(studyset);

        saveToDB(studyset);

        lockImport();
    }

    /**
     * Methode für Pop-ups
     * <p>
     * Die Methode öffnet ein Pop-up mit dem gegebenen Text
     *
     * @param owner das momentane Fenster
     * @param message Pop-up Nachricht
     */
    private void showAlert(Window owner, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Eingabefehler");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
    /**
     * Methode um die Lernsets zu initialisieren
     * <p>
     * Die Methode übergibt dieser Klasse als Parameter die Lernsets mit
     *
     * @param studysets die mitgegebenen Lernsets
     */
    public void initData(Vector<Studyset> studysets) {
        this.studysets = studysets;

        fileTextField.setStyle("" +
                "-fx-text-fill: white;" +
                "-fx-background-color:  #3c3c44");
        nameTextField.setStyle("" +
                "-fx-text-fill: white;" +
                "-fx-background-color:  #3c3c44");
    }

    /**
     * Methode um den Import abzuschliessen
     * <p>
     * Die Methode führt zum vorherigen Fenster
     *
     */
    private void lockImport() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("overview.fxml"));
            Parent root = loader.load();

            OverviewController controller = loader.getController();
            controller.initData(studysets);

            App.setRoot(root);
            closeWindow();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Methode um Lernset in der Datenbank zu speichern
     * <p>
     * Die Methode soll das aktuelle Lernset in der Datenbank speichern
     *
     * @param studyset Lernset, dass man speichern will
     */
    private void saveToDB(Studyset studyset) {
        //TODO add studyset to DB
    }
}
