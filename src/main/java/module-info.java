module com.edu.icesi.jfxautomatongeneration {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.edu.icesi.jfxautomatongeneration to javafx.fxml;
    exports com.edu.icesi.jfxautomatongeneration;
}