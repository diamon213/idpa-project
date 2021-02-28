package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Mastery;
import model.Student;
import model.Studyset;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

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

    @FXML
    void cancelGame(ActionEvent event) {
        saveProgress();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/studyset.fxml"));
            Parent root = loader.load();
            Stage localStage = (Stage) flashcard.getScene().getWindow();
            Stage stage = (Stage) localStage.getOwner();

            StudysetController controller = loader.getController();
            controller.initData(studysets, studyset);

            stage.setScene(new Scene(root));

            localStage.close();

            } catch (IOException ioException) {
            System.out.println("Failed");
        }
    }

    @FXML
    void lastFlashcard(ActionEvent event) {
        try {
            currentStudent = students.get(progress - 2);
            progress--;
            updateProgressLabels();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("oof");
        }
    }

    @FXML
    void nextFlashcard(ActionEvent event) {
        try {
            currentStudent = students.get(progress);
            progress++;
            updateProgressLabels();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("oof");
        }
    }

    @FXML
    void turnImage(MouseEvent event) {
        flipCard(false);
    }

    @FXML
    void turnLabel(MouseEvent event) {
        flipCard(true);
    }

    void initData(Vector<Studyset> studysets, Studyset studyset) {
        this.studysets = studysets;
        this.studyset = studyset;
        this.students = studyset.getStudents();

        initFlashcards();
    }

    void initFlashcards() {
        progress = 1;
        titleLabel.setText(studyset.getStudysetName());
        Collections.shuffle(students);
        currentStudent = students.get(0);
        updateProgressLabels();
    }

    void updateProgressLabels() {
        studentLabel.setText(currentStudent.getName());

        if (currentStudent.getImage() == null) {
            try {
                studentImage.setImage(new Image(new FileInputStream("src/assets/file.png")));
                centerImage();
            } catch (FileNotFoundException f) {
                System.out.println("Bruh");
            }
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

    void updateProgressBar() {
        float division = students.size();
        float add = students.indexOf(currentStudent) + 1;

        progressBar.setProgress(add / division);
    }

    void flipCard(Boolean text) {
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

    public void centerImage() {
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

    void saveProgress() {
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

    void updateDB(Studyset studyset) {
        //TODO update mastery of all students in current studyset in DB
        //find current studyset in DB then iterate through every student's mastery attribute and update it
    }
}