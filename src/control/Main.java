package control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Mastery;
import model.Student;
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

        primaryStage.setTitle("MemoXD");
        primaryStage.setScene(new Scene(root, 650, 425));
        primaryStage.show();


    }

    public void initDataFromDB() {
        //TODO initialize data from DB or create one
        studysets = new Vector<>();

        Studyset recentStudyset = new Studyset("G4b");
        recentStudyset.setMostRecent(true);

        studysets.add(recentStudyset);
        studysets.add(new Studyset("I2a"));
        studysets.add(new Studyset("H2a"));
        studysets.add(new Studyset("I1b"));

        studysets.get(0).setMostRecent(true);

        studysets.get(0).addStudent(new Student("Aladin", "Boudouda", "i3a", "Herr", Mastery.KNOWN));
        studysets.get(0).addStudent(new Student("Tenzin", "Leduk", "i3a", "Herr", Mastery.MASTERED));
        studysets.get(0).addStudent(new Student("Andrej", "Klimov", "i3a", "Herr", Mastery.UNKNOWN));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
