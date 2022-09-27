module com.edu.icesi.jfxautomatongeneration {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.edu.icesi.jfxautomatongeneration to javafx.fxml;
    exports com.edu.icesi.jfxautomatongeneration;
    exports com.edu.icesi.jfxautomatongeneration.model;
    opens com.edu.icesi.jfxautomatongeneration.model to javafx.fxml;

}