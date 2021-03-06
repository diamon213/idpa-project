package memoXD;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import model.Studyset;

import java.io.IOException;
import java.util.Vector;

/**
 * Der Controller für die Ansicht im Karteikarten Modus
 * <p>
 * Dient als Controller für die View home.fxml
 *
 * @author Aladin Boudouda
 *
 */
public class HomeController {

    public Vector<Studyset> studysets;

    @FXML
    private Button recent;

    @FXML
    private HBox hbox;

    /**
     * Methode bei Knopfdruck "Lernsets"
     * <p>
     * Die Methode zur Lernsets-Übersicht führen
     *
     */
    @FXML
    void pressLernsets() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("overview.fxml"));
        Parent root = fxmlLoader.load();

        OverviewController controller = fxmlLoader.getController();
        controller.initData(studysets);

        App.setRoot(root);
    }

    /**
     * Methode um Startseite zu initialisieren
     * <p>
     * Die Methode soll dafür sorgen, dass
     * die Startseite richtig aufgebaut wird
     *
     */
    private void initStartpage() {
        int counter = 0;

        if (studysets.size() == 0) {
            recent.setText("Neues Lernset...");
            recent.setPrefHeight(250);
            recent.setOnAction(ActionEvent -> {
                try {
                    pressLernsets();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            //TODO help import or create set
        } else {
            for (Studyset studyset: studysets) {
                if (studyset.getMostRecent()) {
                    counter = setCounter(counter, studyset);
                } else {
                    if (counter == 0) {
                        int random = (int) (Math.random() * studysets.size());
                        Studyset tempStudyset = studysets.get(random);
                        counter = setCounter(counter, tempStudyset);
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
                                e.printStackTrace();
                            }
                        });
                        hbox.getChildren().add(button);
                        counter++;
                    }
                }
            }

        }
    }

    /**
     * Aktualisiert den Counter
     *<p>
     * Die Methode aktualisiert den Zähler im GUI
     *
     * @param counter Zahl des Counters
     * @param studyset das momentane Lernset
     */
    private int setCounter(int counter, Studyset studyset) {
        studyset.setMastery(studyset.calcMastery());
        String percentage = String.format("%.0f", studyset.getMastery());
        recent.setText(studyset.getStudysetName() + " " + percentage + "%" );
        recent.setOnAction(ActionEvent -> {
            try {
                pressStudyset(studyset);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        counter++;
        return counter;
    }

    /**
     * Methode bei Knopfdruck eines Lernsets
     * <p>
     * Die Methode zum angeklickten Lernset führen
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
     * Methode um die Lernsets zu initialisieren
     * <p>
     * Die Methode übergibt dieser Klasse als Parameter
     * die Lernsets mit
     *
     * @param studysets sammlung von allen Lernsets
     */
    public void initData(Vector<Studyset> studysets) {
        this.studysets = studysets;
        initStartpage();
    }

}