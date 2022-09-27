package com.edu.icesi.jfxautomatongeneration;

import com.edu.icesi.jfxautomatongeneration.model.MooreMachine;
import com.edu.icesi.jfxautomatongeneration.model.TableViewTest;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.Arrays;
import java.util.ResourceBundle;

public class AutomatonController implements Initializable {

    @FXML
    private TableView<TableViewTest> automatonTableview = new TableView<>();
    @FXML
    private TextField numberOfStatesTxtField;

    @FXML
    private TextField transitionElementsTxtField;

    @FXML
    private TextField outputsTxtField;

    @FXML
    private TableView<?> MealyAutomatonTableview;


    private int numberOfRows;
    private int numberOfColumns;
    private String[] transitions;
    private String[] outputs;
    private int counter = 0;

    private ObservableList<String> stateOptions;
    private ObservableList<String> outputsOptions;
    private ArrayList<String> states = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> initialTableMoore;
    private MooreMachine mooreMachine;


    @FXML
    protected void startApplication() {
        launchFXML("definition-of-table-view.fxml","Definition");
    }

    private void initializeOptions(){
        states.clear();
        for(int i=0;i<numberOfRows;i++){
            states.add("Q"+i);
        }
        stateOptions = FXCollections.observableArrayList(states);
        outputsOptions = FXCollections.observableArrayList(Arrays.asList(outputs));
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
    void generateInitialAutomaton(ActionEvent event) {
        initialTableMoore = new ArrayList<>();
        ObservableList<TableViewTest> list = automatonTableview.getItems();
        for(int i=0;i<numberOfRows;i++){
            ArrayList<Integer> values = new ArrayList<>();
            for(int j=0;j<numberOfColumns;j++){
                values.add(Integer.parseInt(list.get(i).getOption(j).substring(1)));
            }
            values.add(Integer.parseInt(list.get(i).getOption(numberOfColumns)));
            initialTableMoore.add(values);
        }
        checkArray();
        mooreMachine = new MooreMachine(initialTableMoore);
        
    }

    @FXML
    void generateInitialAutomatonMealy(ActionEvent event) {

    }

    private void checkArray(){
        for(int i=0;i<numberOfRows;i++){
            for(int j=0;j<numberOfColumns+1;j++){
                System.out.println(initialTableMoore.get(i).get(j));
            }
        }
    }

    @FXML
    protected void buildMooreBtn(){
        numberOfRows = Integer.parseInt(numberOfStatesTxtField.getText());
        transitions = transitionElementsTxtField.getText().split(",");
        numberOfColumns = transitions.length;
        outputs = outputsTxtField.getText().split(",");
        initializeOptions();
        insertMooreColumns();
        insertMooreValues();
        launchFXML("automaton-table-view.fxml","Automaton table");
    }

    @FXML
    protected void buildMealyBtn() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        insertMooreColumns();
        insertMooreValues();
    }

    private void insertMooreColumns(){

        for (int i = 0; i < numberOfColumns; i++) {
            TableColumn<TableViewTest, StringProperty> column = new TableColumn<>(transitions[i]);
            int finalI = i;
            column.setCellValueFactory(z ->{
                final int a = finalI;
                StringProperty value = z.getValue().optionProperty(a);
                return Bindings.createObjectBinding(() -> value);
            } );
            fillWithCombobox(column,stateOptions);
            automatonTableview.getColumns().add(column);
        }
        TableColumn<TableViewTest, StringProperty> outputColumn = new TableColumn<>("Output");
        outputColumn.setCellValueFactory(z ->{
            final int a = numberOfColumns;
            StringProperty value = z.getValue().optionProperty(a);
            return Bindings.createObjectBinding(() -> value);
        } );
        fillWithCombobox(outputColumn,outputsOptions);

        automatonTableview.getColumns().add(outputColumn);
    }

    private void fillWithCombobox(TableColumn<TableViewTest, StringProperty> column, ObservableList cbBox){

        column.setCellFactory(col -> {
            TableCell<TableViewTest, StringProperty> c = new TableCell<>();
            final ComboBox<String> comboBox = new ComboBox<>(cbBox);
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
    }

    private void insertMooreValues(){
        for (int i = 0; i < numberOfRows; i++) {
            automatonTableview.getItems().add(
                    new TableViewTest(numberOfColumns+1)
            );
        }
    }

}