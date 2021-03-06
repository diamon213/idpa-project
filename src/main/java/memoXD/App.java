package memoXD;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Studyset;

import java.io.IOException;
import java.util.Vector;

/**
 * Die Main-Klasse der Applikation
 * <p>
 * Die Applikation ruft beim Start diese Klasse auf
 *
 * @author Aladin Boudouda
 *
 */
public class App extends Application {

    private static Scene scene;

    private Vector<Studyset> studysets;

    /**
     * Startet das GUI
     *<p>
     * Die Methode initialisiert zu erst die Daten von der Datenbank
     * und startet dann das JavaFX GUI
     *
     * @param stage das Hauptfenster
     */
    @Override
    public void start(Stage stage) throws IOException {

        initDataFromDB();

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("home" + ".fxml"));
        Parent root = fxmlLoader.load();

        HomeController controller = fxmlLoader.getController();
        controller.initData(studysets);

        scene = new Scene(root);
        stage.setTitle("MemoXD");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Lädt den Inhalt des Fensters
     *<p>
     * Die Methode setzt die Wurzelszene des Fensters
     *
     * @param root die Szene
     */
    static void setRoot(Parent root) {
        scene.setRoot(root);
    }

    /**
     * Initialisiert die Daten von der Datenbank
     *<p>
     * Es wird eine Verbindung zu Datenbank hergestellt und die Daten werden
     * von der Datenbank abgerufen
     *
     */
    private void initDataFromDB() {
        //TODO initialize data from DB or create one
        studysets = new Vector<>();
    }

    /**
     * Main Methode der Applikation
     *<p>
     * startet die Applikation über JavaFX
     *
     */
    public static void main(String[] args) {
        launch();
    }

}