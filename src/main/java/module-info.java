module com.example.csc325tipcalc {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.csc325tipcalc to javafx.fxml;
    exports com.example.csc325tipcalc;
}