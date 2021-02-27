package control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Mastery;
import model.Student;
import model.Studyset;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

public class StudyController {

    Vector<Studyset> studysets;
    Studyset studyset;
    Vector<Student> students;
    Vector<Student> masteredStudents;
    Vector<Student> knownStudents;
    Vector<Student> unknownStudents;
    Student currentStudent;
    Boolean mode;


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
    void pressAnswer(ActionEvent event) {
        initResult();
    }

    @FXML
    void pressResultNext(ActionEvent event) {
        decideNextStudent();
    }

    @FXML
    void pressSkip(ActionEvent event) {

    }
    @FXML
    void cancelGame() {
        //TODO SAVE
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
            System.out.println("Failed");
        }
    }

    void initResult() {
        String answer = answerBar.getText();
        Boolean resultCondition;

        result.setVisible(true);
        result.setManaged(true);
        prompt.setVisible(false);
        prompt.setManaged(false);

        if (mode) {
            resultCondition = currentStudent.getFirstName().equalsIgnoreCase(answer);
        } else {
            resultCondition = currentStudent.getLastName().equalsIgnoreCase(answer);
        }

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

        updateProgress();
        resultName.setText(currentStudent.getName());
        resultImageView.setImage(currentStudent.getImage());
    }

    void initPrompt() {
        result.setVisible(false);
        result.setManaged(false);
        prompt.setVisible(true);
        prompt.setManaged(true);

        if (currentStudent.getImage() != null) {
            studentImage.setImage(currentStudent.getImage());
        } else {
            try {
                studentImage.setImage(new Image(new FileInputStream("")));
            } catch (FileNotFoundException f) {
                System.out.println("File not found");
            }
        }
        answerBar.setText("");
        updateProgress();
    }

    void decideNextStudent() {

        if (knownStudents.size() == 0 && unknownStudents.size() == 0) {
            //TODO end study

        } else {
            if (knownStudents.size() > 5) {
                currentStudent = knownStudents.get((int)(Math.random() * knownStudents.size()));
            } else if (unknownStudents.size() == 0) {
                currentStudent = knownStudents.get((int)(Math.random() * knownStudents.size()));
            } else {
                currentStudent = unknownStudents.get((int)(Math.random() * unknownStudents.size()));

            }
            initPrompt();
        }
    }

    void removeStudentFromVector(Student student) {
        switch (student.getMastery()) {
            case UNKNOWN -> unknownStudents.remove(unknownStudents.get(unknownStudents.indexOf(student)));
            case KNOWN -> knownStudents.remove(knownStudents.get(knownStudents.indexOf(student)));
        }
    }

    void updateProgress() {
        progressLabel1.setText(masteredStudents.size() + "/" + students.size());
        progressLabel2.setText(knownStudents.size() + "/" + students.size());
        progressLabel3.setText(unknownStudents.size() + "/" + students.size());

        studyset.setMastery(studyset.calcMastery());
        progressBar.setProgress(studyset.getMastery() / 100);
    }

    void initData(Vector<Studyset> studysets, Studyset studyset, Boolean mode) {

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

            stage.setOnCloseRequest(WindowEvent -> {
                cancelGame();
            });
        } catch (Exception e) {
            System.out.println("Failed to set onCloseRequest");
        }

        decideNextStudent();
    }

}