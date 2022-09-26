package com.edu.icesi.jfxautomatongeneration;

import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AutomatonController implements Initializable {

    @FXML
    private TableView automatonTableview = new TableView<>();
    @FXML
    private TextField numberOfStatesTxtField;

    @FXML
    private TextField transitionElementsTxtField;

    private int numberOfRows;
    private int numberOfColumns;
    private String[] transitions;

    @FXML
    protected void startApplication() {
        launchFXML("definition-of-table-view.fxml","Definition");
    }



    private void launchFXML(String fxml, String title) {
        Parent root = loadFxml(fxml);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setOnHidden(event -> {
            //do all your processing here
        });
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.show();
    }

    private Parent loadFxml(String fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AutomatonApplication.class.getResource(fxml));
            fxmlLoader.setController(this);
            return fxmlLoader.load();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Can't load requested document right now.\nRequested document: \"" + fxml + "\"");
            throw new NullPointerException("Document is null");
        }
    }
    @FXML
    protected void showInstructions(){

    }

    @FXML
    protected void continueBtn(){
        numberOfRows = Integer.parseInt(numberOfStatesTxtField.getText());
        transitions = transitionElementsTxtField.getText().split(",");
        numberOfColumns = transitions.length;
        System.out.println("number of rows "+numberOfRows);
        System.out.println("number of columns "+numberOfColumns);
        insertColumns();
        insertValues();
        launchFXML("automaton-table-view.fxml","Automaton table");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        insertColumns();
        insertValues();
    }

    private void insertColumns(){
        ObservableList<String> options = FXCollections.observableArrayList(
                "1",
                "2",
                "3"
        );
        automatonTableview.getColumns().add(new TableColumn<>("Estado"));
        for (int i = 0; i < numberOfColumns; i++) {
            TableColumn<TableViewTest, StringProperty> column = new TableColumn<>("option");
           

            column.setCellFactory(col -> {
                TableCell<TableViewTest, StringProperty> c = new TableCell<>();
                final ComboBox<String> comboBox = new ComboBox<>(options);
                c.itemProperty().addListener((observable, oldValue, newValue) -> {
                    if (oldValue != null) {
                        comboBox.valueProperty().unbindBidirectional(oldValue);
                    }
                    if (newValue != null) {
                        comboBox.valueProperty().bindBidirectional(newValue);
                    }
                });
                c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node) null).otherwise(comboBox));
                return c;
            });
            automatonTableview .getColumns().add(column);
        }
        automatonTableview.getColumns().add(new TableColumn<>("Salida"));
    }

    private void insertValues(){
        List<ComboBox> comboBoxes = new ArrayList<>();
        comboBoxes.add(new ComboBox(FXCollections.observableArrayList("a","b","c")));
        comboBoxes.get(0).setItems(FXCollections.observableArrayList("a","b","c"));
        for (int i = 0; i < numberOfRows; i++) {
            automatonTableview.getItems().add(
                    FXCollections.observableArrayList(
                        comboBoxes
                    )
            );
        }
    }

}