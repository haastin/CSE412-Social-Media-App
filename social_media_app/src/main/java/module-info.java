module com.cse412 {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive java.sql;

    opens com.cse412 to javafx.fxml;
    exports com.cse412;
}
