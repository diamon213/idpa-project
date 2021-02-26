package control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Studyset;

import java.util.Vector;

public class Main extends Application {

    public Vector<Studyset> studysets;

    @Override
    public void start(Stage primaryStage) throws Exception{

        initDataFromDB();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/home.fxml"));
        Parent root = loader.load();

        HomeController controller = loader.getController();
        controller.initData(studysets);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 650, 425));
        primaryStage.show();


    }

    public void initDataFromDB() {
        //TODO initialize data from DB
        studysets = new Vector<>();
        studysets.add(new Studyset("i2a"));
        studysets.add(new Studyset("i3a"));
        studysets.add(new Studyset("i5a"));
        studysets.add(new Studyset("i6a"));
        studysets.add(new Studyset("i2a"));
        studysets.add(new Studyset("i3a"));
        studysets.add(new Studyset("i5a"));
        studysets.add(new Studyset("i6a"));
        studysets.add(new Studyset("i2a"));
        studysets.add(new Studyset("i3a"));
        studysets.add(new Studyset("i5a"));
        studysets.add(new Studyset("i6a"));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
