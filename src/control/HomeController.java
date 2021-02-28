package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Studyset;

import java.io.IOException;
import java.util.Vector;


public class HomeController {

    public Vector<Studyset> studysets;

    @FXML
    private Button recent;

    @FXML
    private Button recent1;

    @FXML
    private Button recent2;

    @FXML
    private Button recent3;

    @FXML
    private HBox hbox;

    public void pressButton(ActionEvent event) {
        System.out.println(studysets.get(0).getStudysetName());
        recent.setText("Hallo");
        System.out.println(recent.getText());
    }

    public void pressLernsets() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/overview.fxml"));
        Parent root = loader.load();
        Stage primaryStage = (Stage) recent.getScene().getWindow();

        OverviewController controller = loader.getController();
        controller.initData(studysets);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void initStartpage() {
        int counter = 0;

        if (studysets.size() == 0) {
            System.out.println("elo");
            recent.setText("Neues Lernset...");
            recent.setPrefHeight(250);
            recent.setOnAction(ActionEvent -> {
                try {
                    pressLernsets();
                } catch (IOException e) {
                    System.out.println("Can't open Lernsets");
                }
            });
            //TODO help import or create set
        } else {
            for (Studyset studyset: studysets) {
                if (studyset.getMostRecent()) {
                    studyset.setMastery(studyset.calcMastery());
                    String percentage = String.format("%.0f", studyset.getMastery());
                    recent.setText(studyset.getStudysetName() + " " + percentage + "%" );
                    System.out.println(studyset.getMastery());
                    recent.setOnAction(ActionEvent -> {
                        try {
                            pressStudyset(studyset);
                        } catch (IOException e) {
                            System.out.println("bruh");
                        }
                    });
                    counter++;
                } else {
                    if (counter == 0) {
                        int random = (int) (Math.random() * studysets.size());
                        Studyset tempStudyset = studysets.get(random);
                        tempStudyset.setMastery(tempStudyset.calcMastery());
                        String percentage = String.format("%.0f", tempStudyset.getMastery());
                        recent.setText(tempStudyset.getStudysetName() + " " + percentage + "%" );
                        System.out.println(studyset.getMastery());
                        recent.setOnAction(ActionEvent -> {
                            try {
                                pressStudyset(tempStudyset);
                            } catch (IOException e) {
                                System.out.println("bruh");
                            }
                        });
                        counter++;
                    } else if (counter < 4) {
                        int random = (int) (Math.random() * studysets.size());
                        Studyset tempStudyset = studysets.get(random);

                        Button button = new Button(tempStudyset.getStudysetName());
                        Font font = new Font("System", 17);

                        button.setPrefSize(92, 100);
                        button.setFont(font);
                        button.setStyle("-fx-background-color: #91AA9D");
                        button.setAlignment(Pos.BOTTOM_LEFT);
                        button.setOnAction(ActionEvent -> {
                            try {
                                pressStudyset(tempStudyset);
                            } catch (IOException e) {
                                System.out.println("cant open window" + tempStudyset.getStudysetName());
                            }
                        });
                        hbox.getChildren().add(button);
                        counter++;
                    }
                }
            }

        }
    }

    public void pressStudyset(Studyset studyset) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/studyset.fxml"));
        Parent root = loader.load();
        Stage primaryStage = (Stage) hbox.getScene().getWindow();

        StudysetController controller = loader.getController();
        controller.initData(studysets, studyset);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void initData(Vector<Studyset> studysets) {
        this.studysets = studysets;
        initStartpage();
    }
}
