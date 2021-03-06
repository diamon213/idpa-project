module memoxd {
    requires javafx.controls;
    requires javafx.fxml;

    opens memoXD to javafx.fxml;
    exports memoXD;
    exports model;
}