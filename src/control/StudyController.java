package control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Mastery;
import model.Student;
import model.StudyMode;
import model.Studyset;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

/**
 * Der Controller für die Lernmodi
 * <p>
 * Dient als Controller für die View study.fxml
 *
 * @author Aladin Boudouda
 *
 */
public class StudyController {

    Vector<Studyset> studysets;
    Studyset studyset;
    Vector<Student> students;
    Vector<Student> masteredStudents;
    Vector<Student> knownStudents;
    Vector<Student> unknownStudents;
    Student currentStudent;
    StudyMode mode;

    @FXML
    private Label progressLabel1;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label titleLabel;

    @FXML
    private Label progressLabel2;

    @FXML
    private Label progressLabel3;

    @FXML
    private AnchorPane result;

    @FXML
    private Label resultLabel;

    @FXML
    private ImageView resultImageView;

    @FXML
    private Label resultName;

    @FXML
    private AnchorPane prompt;

    @FXML
    private TextField answerBar;

    @FXML
    private Label promptLabel;

    @FXML
    private ImageView studentImage;

    @FXML
    private AnchorPane assignPrompt;

    @FXML
    private ImageView assignImage;

    @FXML
    private HBox hbox;

    /**
     * Methode bei Knopfdruck "Antwort"
     * <p>
     * Die Methode führt zu Ergebnis-Seite
     *
     */
    @FXML
    public void pressAnswer() {
        initResult(answerBar.getText());
    }


    /**
     * Methode um nach dem Ergebnis den nächsten Schüler zu testen
     * <p>
     * Die Methode lässt das Programm zum nächsten Sch¨ler gehen, der getestet wird
     *
     */
    @FXML
    public void pressResultNext() {
        decideNextStudent();
    }

    /**
     * Methode bei Knopfdruck "Abbrechen"
     * <p>
     * Die Methode speichert Lernstand der Schüler und führt zurück zum Lernset
     *
     */
    @FXML
    public void cancelGame() {

        updateDB(studyset);

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/studyset.fxml"));
            Parent root = loader.load();
            Stage localStage = (Stage) titleLabel.getScene().getWindow();
            Stage stage = (Stage) localStage.getOwner();

            StudysetController controller = loader.getController();
            controller.initData(studysets, studyset);

            stage.setScene(new Scene(root));

            localStage.close();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Methode um die Ergebnis-Seite zu zeigen
     * <p>
     * Die Methode führt zur Ergebnis-Seite und überprüft ob die Antwort stimmt
     *
     * @param answer antwort
     */
    private void initResult(String answer) {
        boolean resultCondition = true;

        result.setVisible(true);
        result.setManaged(true);
        prompt.setVisible(false);
        prompt.setManaged(false);
        assignPrompt.setVisible(false);
        assignPrompt.setManaged(false);

        switch (mode) {
            case FIRSTNAME -> resultCondition = currentStudent.getFirstName().equalsIgnoreCase(answer);
            case LASTNAME -> resultCondition = currentStudent.getLastName().equalsIgnoreCase(answer);
            case ASSIGN -> resultCondition = currentStudent.getName().equals(answer);
        }

        checkResultCondition(resultCondition);

        updateProgress();
        resultName.setText(currentStudent.getName());
        resultImageView.setImage(currentStudent.getImage());
    }

    /**
     * Methode um das Ergebnis zu setzen
     * <p>
     * Die Methode überprüft das Resultat und sorgt dementsprechend für die Ergebnis-Seite
     *
     * @param resultCondition ob das resultat stimmt oder nicht
     */
    private void checkResultCondition(boolean resultCondition) {
        if (resultCondition) {

            resultLabel.setText("Richtig! :)");
            removeStudentFromVector(currentStudent);

            if (currentStudent.getMastery() == Mastery.KNOWN) {
                masteredStudents.add(currentStudent);
                currentStudent.setMastery(Mastery.MASTERED);
            } else {
                knownStudents.add(currentStudent);
                currentStudent.setMastery(Mastery.KNOWN);
            }
        } else {
            resultLabel.setText("Falsch :(");
            removeStudentFromVector(currentStudent);
            unknownStudents.add(currentStudent);
            currentStudent.setMastery(Mastery.UNKNOWN);
        }

    }

    /**
     * Methode um die Eingabe-Seite zu öffnen
     * <p>
     * Die Methode führt wieder zur Test Eingabe für den Schüler
     *
     */
    private void initPrompt() {
        result.setVisible(false);
        result.setManaged(false);
        prompt.setVisible(true);
        prompt.setManaged(true);

        initPromptImage(studentImage);

        if (mode == StudyMode.LASTNAME) {
            promptLabel.setText("Nachname von:");
        } else {
            promptLabel.setText("Vorname von:");
        }

        answerBar.setText("");
        updateProgress();
    }

    /**
     * Methode um dass Bild bei der Eingabe-Seite zu setzen
     * <p>
     * Die Methode sorgt dafür, dass das Bild bei der Eingabe-Seite das Bild des Schülers ist
     *
     */
    private void initPromptImage(ImageView imageView) {
        if (currentStudent.getImage() != null) {
            imageView.setImage(currentStudent.getImage());
        } else {
            try {
                imageView.setImage(new Image(new FileInputStream("src/assets/placeholder.png")));
            } catch (FileNotFoundException f) {
                f.printStackTrace();
            }
        }
    }

    /**
     * Methode um die Auswahl-Seite zu öffnen für den Multiple-Choice-Modus
     * <p>
     * Die Methode führt wieder zur Multiple-Choice-Auswahl für den Schüler
     *
     */
    private void initAssignPrompt() {
        ArrayList<Student> studentButtons = new ArrayList<>();

        result.setVisible(false);
        result.setManaged(false);
        assignPrompt.setVisible(true);
        assignPrompt.setManaged(true);

        initPromptImage(assignImage);

        studentButtons.add(currentStudent);

        while (true) {
            assert false;
            if (!(studentButtons.size() < 4)) break;
            Student tempStudent = students.get((int)(Math.random() * students.size()));
            if (!studentButtons.contains(tempStudent)) {
                studentButtons.add(tempStudent);
            }
        }

        Collections.shuffle(studentButtons);

        for (int i = 0; i < 4; i++) {
            Button button = (Button) hbox.getChildren().get(i);
            button.setText(studentButtons.get(i).getName());
            button.setOnAction(ActionEvent -> initResult(button.getText()));
        }

        updateProgress();
    }

    /**
     * Methode um den nächsten Schüler zu bestimmen
     * <p>
     * Die Methode wählt nach einer Logik einen zufälligen Schüler
     * und macht diesen zum nächsten Schüler
     *
     */
    private void decideNextStudent() {

        if (knownStudents.size() == 0 && unknownStudents.size() == 0) {
            System.out.println("No students left");
        } else {
            if (knownStudents.size() > 5) {
                currentStudent = knownStudents.get((int)(Math.random() * knownStudents.size()));
            } else if (unknownStudents.size() == 0) {
                currentStudent = knownStudents.get((int)(Math.random() * knownStudents.size()));
            } else {
                currentStudent = unknownStudents.get((int)(Math.random() * unknownStudents.size()));

            }
            if (mode != StudyMode.ASSIGN) {
                initPrompt();
            } else {
                initAssignPrompt();
            }
        }
    }

    /**
     * Methode um den Schüler aus seinem Lernstand zu entfernen
     * <p>
     * Die Methode nimmt den Schüler aus der Liste zu der er gehört
     *
     * @param student Schüler der entfernt wird
     */
    private void removeStudentFromVector(Student student) {
        switch (student.getMastery()) {
            case UNKNOWN -> unknownStudents.remove(unknownStudents.get(unknownStudents.indexOf(student)));
            case KNOWN -> knownStudents.remove(knownStudents.get(knownStudents.indexOf(student)));
        }
    }

    /**
     * Methode um die Lernstandsanzeigen zu aktualisieren
     * <p>
     * Die Methode setzt die Lernstantsanzeigen auf die Werte,
     * die es vom Lernset erhält
     *
     */
    private void updateProgress() {
        progressLabel1.setText(masteredStudents.size() + "/" + students.size());
        progressLabel2.setText(knownStudents.size() + "/" + students.size());
        progressLabel3.setText(unknownStudents.size() + "/" + students.size());

        studyset.setMastery(studyset.calcMastery());
        progressBar.setProgress(studyset.getMastery() / 100);
    }

    /**
     * Methode um die Lernsets zu initialisieren
     * <p>
     * Die Methode übergibt dieser Klasse als Parameter die Lernsets,
     * das aktuelle Lernset und den gewählten Lernmodus
     *
     * @param studysets sammlung von allen Lernsets
     * @param studyset das aktuelle lernset
     * @param mode gewählter lernmodus
     */
    public void initData(Vector<Studyset> studysets, Studyset studyset, StudyMode mode) {

        this.studysets = studysets;
        this.studyset = studyset;
        this.mode = mode;

        students = studyset.getStudents();
        unknownStudents = new Vector<>();
        knownStudents = new Vector<>();
        masteredStudents = new Vector<>();

        for (Student student: students) {
            switch (student.getMastery()) {
                case MASTERED -> masteredStudents.add(student);
                case UNKNOWN -> unknownStudents.add(student);
                case KNOWN -> knownStudents.add(student);
            }
        }

        titleLabel.setText(studyset.getStudysetName());

        try {
            Stage stage = (Stage) titleLabel.getParent().getScene().getWindow();

            stage.setOnCloseRequest(WindowEvent -> cancelGame());
        } catch (Exception ignored) {

        }

        decideNextStudent();
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