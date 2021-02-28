package control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Mastery;
import model.Student;
import model.Studyset;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Vector;

public class EditStudysetController {

    public String[] arr = {"Frau", "Herr", "Andere"};
    public Studyset studyset;
    public Vector<Studyset> studysets;
    public String scene;
    public Image addImage;

    @FXML
    private TextField studysetNameTextfield;

    @FXML
    private VBox vbox;

    @FXML
    public void pressAdd(ActionEvent event) throws FileNotFoundException {
        addNewPerson(null);
    }

    public void addNewPerson(Student student) throws FileNotFoundException {
        HBox hbox = new HBox();

        ImageView picture = new ImageView(addImage);

        VBox vbox1 = new VBox();
        VBox vbox2 = new VBox();

        Label label1 = new Label("Vorname:");
        Label label2 = new Label("Nachname");
        Label label3 = new Label("Anrede:");

        TextField firstnameTextfield = new TextField();
        TextField lastnameTextfield = new TextField();
        ChoiceBox salutationChoicebox = new ChoiceBox(FXCollections.observableArrayList(arr));

        ImageView trash = new ImageView(new Image(new FileInputStream("src/assets/delete.png")));

        setTextFieldStyle(firstnameTextfield);
        setTextFieldStyle(lastnameTextfield);
        salutationChoicebox.setStyle("-fx-background-color:  #91AA9D");

        setLabelStyle(label1);
        setLabelStyle(label2);
        setLabelStyle(label3);

        if (student != null)  {
            firstnameTextfield.setText(student.getFirstName());
            firstnameTextfield.setAccessibleText(student.getMastery().toString());
            lastnameTextfield.setText(student.getLastName());

            salutationChoicebox.getSelectionModel().select(student.getSalutation());

            if (student.getImage() != null) {
                picture.setImage(student.getImage());
            }
        } else {
            firstnameTextfield.setAccessibleText("UNKNOWN");
        }


        hbox.setSpacing(10);
        hbox.setPrefSize(591, 115);

        picture.setOnMouseClicked(mouseEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Bilddateien", "*.png", "*.jpg", "*.gif", "*.bmp"));
            File f = fileChooser.showOpenDialog(null);

            if (f != null) {
                try {
                    picture.setImage(new Image(new FileInputStream(f.getAbsolutePath())));
                } catch (FileNotFoundException e) {
                    System.out.println("File not found");
                }
            }
        });

        picture.setFitWidth(50);
        picture.setPreserveRatio(true);
        HBox.setMargin(picture, new Insets(29, 40, 0, 30));

        hbox.getChildren().add(picture);

        vbox1.setSpacing(30);
        vbox1.setPrefSize(66, 117);
        vbox1.getChildren().add(label1);
        vbox1.getChildren().add(label2);
        vbox1.getChildren().add(label3);

        hbox.getChildren().add(vbox1);

        vbox2.setSpacing(20);
        vbox2.setPrefSize(154, 126);
        vbox2.getChildren().add(firstnameTextfield);
        vbox2.getChildren().add(lastnameTextfield);
        salutationChoicebox.setPrefWidth(155);
        vbox2.getChildren().add(salutationChoicebox);

        hbox.getChildren().add(vbox2);

        trash.setFitWidth(50);
        trash.setPreserveRatio(true);
        HBox.setMargin(trash, new Insets(29, 0, 0, 35));

        trash.setOnMouseClicked(MouseEvent -> {
            VBox parent =  (VBox) hbox.getParent();
            parent.getChildren().remove(hbox);
        });

        hbox.getChildren().add(trash);

        vbox.getChildren().add(hbox);
    }

    @FXML
    void saveStudyset(ActionEvent event) {

        if (studysetNameTextfield.getText().isBlank()) {
            showAlert(vbox.getScene().getWindow(),"Bitte geben Sie einen validen Namen für das Lernset ein.");
            return;
        }

        String tempStudysetName = studysetNameTextfield.getText();
        Studyset tempStudyset = new Studyset(tempStudysetName);

        for  (int i= 0; i < vbox.getChildren().size(); i++) {

            HBox hbox = (HBox) vbox.getChildren().get(i);
            VBox formVbox =  (VBox) hbox.getChildren().get(2);

            TextField firstnameField = (TextField) formVbox.getChildren().get(0);
            TextField lastnameField = (TextField) formVbox.getChildren().get(1);
            ChoiceBox salutationBox = (ChoiceBox) formVbox.getChildren().get(2);

            if (firstnameField.getText().isEmpty() || lastnameField.getText().isEmpty() ||  salutationBox.getSelectionModel().getSelectedItem() == null) {
                showAlert(vbox.getScene().getWindow(),"Bitte füllen Sie alle Felder.");
                return;
            }

            ImageView imageView = (ImageView) hbox.getChildren().get(0);
            String firstname = firstnameField.getText();
            String lastname = lastnameField.getText();
            String salutation = salutationBox.getSelectionModel().getSelectedItem().toString();
            Mastery mastery = switch (firstnameField.getAccessibleText()) {
                case "MASTERED" -> Mastery.MASTERED;
                case "KNOWN" -> Mastery.KNOWN;
                default -> Mastery.UNKNOWN;
            };

            Student tempStudent = new Student(firstname, lastname, tempStudysetName, salutation, mastery);

            if (imageView.getImage() != addImage) {
                tempStudent.setImage(imageView.getImage());
            }

            tempStudyset.addStudent(tempStudent);
        }

        if (studyset != null) {
            studysets.remove(studyset);
        }
        studyset = tempStudyset;
        studysets.add(studyset);

        updateDB(studyset);

        lockEdit(scene);
    }

    private void showAlert(Window owner, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Eingabefehler");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public void lockEdit(String scene) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(scene));
            Parent root = loader.load();
            Stage localStage = (Stage) vbox.getScene().getWindow();
            Stage stage = (Stage) localStage.getOwner();

            if (scene.equals("../view/overview.fxml")) {
                OverviewController controller = loader.getController();
                controller.initData(studysets);

                stage.setScene(new Scene(root));

            } else if (scene.equals("../view/studyset.fxml")) {
                StudysetController controller = loader.getController();
                controller.initData(studysets, studyset);

                stage.setScene(new Scene(root));

            } else {
                System.out.println("bad scene");
            }

            localStage.close();
        } catch (Exception e) {
            System.out.println("OOF");
        }

    }

    void initTable() throws FileNotFoundException {
        for (Student student : studyset.getStudents()) {
            addNewPerson(student);
        }
    }

    void setTextFieldStyle(TextField textField) {
        textField.setStyle("" +
                "-fx-text-fill: white;" +
                "-fx-background-color:  #3c3c44");
    }

    void setLabelStyle(Label label) {
        label.setStyle("-fx-text-fill: white");
    }

    void initData(Vector<Studyset> studysets, Studyset studyset, String scene) throws FileNotFoundException {
        this.studysets = studysets;
        this.studyset = studyset;
        this.scene = scene;
        addImage = new Image(new FileInputStream("src/assets/file.png"));

        setTextFieldStyle(studysetNameTextfield);

        if (studyset != null) {
            initTable();
            studysetNameTextfield.setText(studyset.getStudysetName());
        }
    }

    void updateDB(Studyset studyset) {
        //TODO add / update studyset in DB
        //check if studyset already is in DB
        //if yes -> update studyset in DB
        //if no -> add studyset to db
    }
}
