package control;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Studyset;

import java.io.IOException;
import java.util.Vector;

public class OverviewController {

    public Vector<Studyset> studysets;


    @FXML
    public VBox vbox = null;

    @FXML
    public TextField searchbar = null;

    @FXML
    public Button asd = null;

    public void btnHome(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/home.fxml"));
        Parent root = loader.load();
        Stage primaryStage = (Stage) vbox.getScene().getWindow();

        HomeController controller = loader.getController();
        controller.initData(studysets);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void btnLernsets(ActionEvent event) throws IOException {
        System.out.println("Lernsets");
        Scene lernsetScene = new Scene(FXMLLoader.load(getClass().getResource("../view/overview.fxml")));
        Stage primaryStage = (Stage) vbox.getScene().getWindow();
        primaryStage.setScene(lernsetScene);
        primaryStage.show();
    }

    public void pressAdd(ActionEvent event) {
        /*Button button = new Button();
        vbox.getChildren().add(button);*/
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/editStudyset.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();

            EditStudysetController controller = fxmlLoader.getController();
            controller.initData(studysets, null, "../view/overview.fxml");

            stage.setTitle("Lernset zusammenstellen...");
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(vbox.getScene().getWindow());
            stage.show();
        } catch (Exception e) {
            System.out.println("Cant load window");
        }
    }

    public void pressImport(ActionEvent event) {
        /*Button button = new Button();
        vbox.getChildren().add(button);*/
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/csvImport.fxml"));
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
            System.out.println("Cant load window");
        }

        System.out.println("class added");
    }

    public void keyTyped(KeyEvent event) {
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

    public void initVbox() {
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

    public void initData(Vector<Studyset> studysets) {
        this.studysets = studysets;
        initVbox();
    }

    public void pressStudyset(Studyset studyset) throws IOException {
        System.out.println(studyset.getStudysetName());

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/studyset.fxml"));
        Parent root = loader.load();
        Stage primaryStage = (Stage) vbox.getScene().getWindow();

        StudysetController controller = loader.getController();
        controller.initData(studysets, studyset);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
