package control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Studyset;

import java.util.Vector;

/**
 * Die Main-Klasse der Applikation
 * <p>
 * Die Applikation ruft beim Start diese Klasse auf
 *
 * @author Aladin Boudouda
 *
 */
public class Main extends Application {

    private Vector<Studyset> studysets;

    /**
     * Startet das GUI
     *<p>
     * Die Methode initialisiert zu erst die Daten von der Datenbank
     * und startet dann das JavaFX GUI
     *
     * @param primaryStage das Hauptfenster
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        initDataFromDB();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/home.fxml"));
        Parent root = loader.load();

        HomeController controller = loader.getController();
        controller.initData(studysets);

        primaryStage.setTitle("MemoXD");
        primaryStage.setScene(new Scene(root, 650, 425));
        primaryStage.show();


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
        launch(args);
    }
}
