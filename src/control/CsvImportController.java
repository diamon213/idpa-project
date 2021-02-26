package control;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvImportController {
    public Button fileButton = null;
    public Button okButton = null;
    public TextField fileTextField = null;

    public void pressFilechooser(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Dateien", "*.csv"));
        File f = fileChooser.showOpenDialog(null);

        if (f != null) {
            fileTextField.setText(f.getAbsolutePath());
        }
    }

    public void pressCancel(ActionEvent event) {
        Stage stage = (Stage) fileTextField.getScene().getWindow();
        stage.close();
    }

    public void pressOk(ActionEvent event) throws FileNotFoundException {
        System.out.println("lesgo");
        Scanner scanner = new Scanner(new File(fileTextField.getText()));

        StringBuilder csv = new StringBuilder();

        while (scanner.hasNext()) {
            String text = scanner.next() + " ";
            csv.append(text);
        }

        Pattern pattern = Pattern.compile("[a-zA-Z_äÄöÖüÜß\"]{1,4};[a-zA-Z0_äÄöÖüÜß \"]{1,40};[a-zA-Z_äÄöÖüÜß \"]{1,40};;;");
        Matcher matcher = pattern.matcher(csv);

        ArrayList<String> list = new ArrayList<>();

        while ( matcher.find() ) {
            String[] strings = (csv.substring(matcher.start(), matcher.end()).split("[;]{1,3}"));
            for (int i = 0; i < 3; i++) {
                System.out.println(strings[i].replaceAll("\"", ""));
            }
        }


    }
}
