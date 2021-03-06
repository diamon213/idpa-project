package memoXD;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Mastery;
import model.Student;
import model.Studyset;

import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

/**
 * Der Controller für die Ansicht im Karteikarten Modus
 * <p>
 * Dient als Controller für die View flashcards.fxml
 *
 * @author Aladin Boudouda
 *
 */
public class FlashcardsController {

    public Vector<Studyset> studysets;
    public Studyset studyset;
    public Vector<Student> students;
    public Student currentStudent;
    public int progress;

    @FXML
    private Label progressLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label titleLabel;

    @FXML
    private AnchorPane flashcard;

    @FXML
    private Label smallProgressLabel;

    @FXML
    private Label studentLabel;

    @FXML
    private ImageView studentImage;

    /**
     * Methode um den Lernmodus zu verlassen
     * <p>
     * Die Methode speichert Lernstand der Schüler und führt zum vorherigen Fenster
     *
     */
    @FXML
    public void cancelGame() {
        saveProgress();
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("studyset.fxml"));
            Parent root = loader.load();
            Stage localStage = (Stage) flashcard.getScene().getWindow();

            StudysetController controller = loader.getController();
            controller.initData(studysets, studyset);

            App.setRoot(root);

            localStage.close();

            } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Methode eine Karteikarte zurückzugehen
     * <p>
     * Die Methode geht einen Schüler zurück
     *
     */
    @FXML
    public void lastFlashcard() {
        try {
            currentStudent = students.get(progress - 2);
            progress--;
            updateProgressLabels();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Keine vorherigen Schüler");
        }
    }

    /**
     * Methode eine Karteikarte nach vorne zu gehen
     * <p>
     * Die Methode geht einen Schüler nach vorne
     *
     */
    @FXML
    public void nextFlashcard() {
        try {
            currentStudent = students.get(progress);
            progress++;
            updateProgressLabels();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Keine weiteren Schüler");
        }
    }

    /**
     * Methode bei Klick auf die Karteikarte mit Bild
     * <p>
     * Die Methode dreht die Karteikarte um
     *
     */
    @FXML
    public void turnImage() {
        flipCard(false);
    }

    /**
     * Methode bei Klick auf die Karteikarte mit Text
     * <p>
     * Die Methode dreht die Karteikarte um
     *
     */
    @FXML
    public void turnLabel() {
        flipCard(true);
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
    public void initData(Vector<Studyset> studysets, Studyset studyset) {
        this.studysets = studysets;
        this.studyset = studyset;
        this.students = studyset.getStudents();

        initFlashcards();
    }

    /**
     * Methode um die Karteikarten aufzusetzen
     * <p>
     * Die Methode setzt die Karteikarte auf den ersten Schüler
     *
     */
    private void initFlashcards() {
        progress = 1;
        titleLabel.setText(studyset.getStudysetName());
        Collections.shuffle(students);
        currentStudent = students.get(0);
        updateProgressLabels();
    }

    /**
     * Methode um die Lernstandsanzeigen zu aktualisieren
     * <p>
     * Die Methode setzt die Lernstantsanzeigen auf die Werte,
     * die es vom Lernset erhält
     *
     */
    private void updateProgressLabels() {
        studentLabel.setText(currentStudent.getName());

        if (currentStudent.getImage() == null) {
            studentImage.setImage(new Image(App.class.getResourceAsStream("placeholder.png")));
            centerImage();
        } else {
            studentImage.setImage(currentStudent.getImage());
            centerImage();
        }

        flipCard(true);


        String progressText = progress + "/" + students.size();
        progressLabel.setText(progressText);
        smallProgressLabel.setText(progressText);

        updateProgressBar();
    }

    /**
     * Methode um die Fortschrittsbalken zu aktualisieren
     * <p>
     * Die Methode setzt den Fortschrittsbalken auf die Werte,
     * die es vom Lernset erhält
     *
     */
    private void updateProgressBar() {
        float division = students.size();
        float add = students.indexOf(currentStudent) + 1;

        progressBar.setProgress(add / division);
    }

    /**
     * Methode um die Karteikarte zu drehen
     * <p>
     * Die Methode sorgt dafür, dass die Karteikarte
     * zur richtigen Seite dreht
     *
     * @param text aktuelle seite der Karteikarte
     */
    private void flipCard(Boolean text) {
        if (text) {
            studentLabel.setVisible(false);
            studentLabel.setManaged(false);
            studentImage.setVisible(true);
            studentImage.setManaged(true);
        } else {
            studentImage.setVisible(false);
            studentImage.setManaged(false);
            studentLabel.setVisible(true);
            studentLabel.setManaged(true);
        }
    }

    /**
     * Methode um das Bild der Karteikarte in die Mitte zu setzen
     * <p>
     * Die Methode setzt das Bild des aktuellen
     * Schülers in die Mitte der Karteikarte
     *
     */
    private void centerImage() {
        Image img = studentImage.getImage();
        if (img != null) {
            double w;
            double h;

            double ratioX = studentImage.getFitWidth() / img.getWidth();
            double ratioY = studentImage.getFitHeight() / img.getHeight();

            double reducCoeff = Math.min(ratioX, ratioY);

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            studentImage.setX((studentImage.getFitWidth() - w) / 2);
            studentImage.setY((studentImage.getFitHeight() - h) / 2);

        }
    }

    /**
     * Methode um den Lernsstand der Schüler zu speichern
     * <p>
     * Die Methode speichert den Lernstand aller Schüler dieses Lernsets
     *
     */
    private void saveProgress() {
        for (Student student: students) {
            if (studyset.getStudents().get(studyset.getStudents().indexOf(student)).getMastery() == Mastery.UNKNOWN) {
                studyset.getStudents().get(studyset.getStudents().indexOf(student)).setMastery(Mastery.KNOWN);
            }
            if (student == currentStudent) {
                return;
            }
        }
        studysets.setElementAt(studyset, studysets.indexOf(studyset));

        updateDB(studyset);
    }

    /**
     * Methode um Lernset in der Datenbank zu aktualisieren
     * <p>
     * Die Methode soll das aktuelle Lernset in der Datenbank aktualisieren
     *
     * @param studyset Lernset, dass man aktualisieren will
     */
    private void updateDB(Studyset studyset) {
        //TODO update mastery of all students in current studyset in DB
        //find current studyset in DB then iterate through every student's mastery attribute and update it
    }
}