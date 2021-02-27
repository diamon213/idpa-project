package control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.WindowEvent;
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
    private Label promptLabel1;

    @FXML
    private AnchorPane assignPrompt;

    @FXML
    private ImageView assignImage;

    @FXML
    private HBox hbox;

    @FXML
    void pressAnswer(ActionEvent event) {
        initResult(answerBar.getText());
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

        //TODO save data to DB

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

    void initResult(String name) {
        boolean resultCondition = true;

        result.setVisible(true);
        result.setManaged(true);
        prompt.setVisible(false);
        prompt.setManaged(false);
        assignPrompt.setVisible(false);
        assignPrompt.setManaged(false);

        switch (mode) {
            case FIRSTNAME -> resultCondition = currentStudent.getFirstName().equalsIgnoreCase(name);
            case LASTNAME -> resultCondition = currentStudent.getLastName().equalsIgnoreCase(name);
            case ASSIGN -> resultCondition = currentStudent.getName().equals(name);
        }

        checkResultCondition(resultCondition);

        updateProgress();
        resultName.setText(currentStudent.getName());
        resultImageView.setImage(currentStudent.getImage());
    }

    void checkResultCondition(boolean resultCondition) {
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

    void initPrompt() {
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

    void initPromptImage(ImageView imageView) {
        if (currentStudent.getImage() != null) {
            imageView.setImage(currentStudent.getImage());
        } else {
            try {
                imageView.setImage(new Image(new FileInputStream("")));
            } catch (FileNotFoundException f) {
                System.out.println("File not found");
            }
        }
    }

    void initAssignPrompt() {
        System.out.println("bruh");
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
            button.setOnAction(ActionEvent -> {
                initResult(button.getText());
            });
        }

    }

    void decideNextStudent() {

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

    void initData(Vector<Studyset> studysets, Studyset studyset, StudyMode mode) {

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