package com.edu.icesi.jfxautomatongeneration;

import com.edu.icesi.jfxautomatongeneration.model.StateMachine;
import com.edu.icesi.jfxautomatongeneration.model.TableViewTest;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.security.auth.callback.Callback;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;

public class AutomatonController implements Initializable {

    @FXML
    private TableView<TableViewTest> mooreAutomatonTableview = new TableView<>();

    @FXML
    private TableView<String[]> newMooreMachineTableView= new TableView<>();
    @FXML
    private TableView<String[]> newMealyMachineTableView= new TableView<>();
    @FXML
    private Label welcomeTxt;

    @FXML
    private TextField numberOfStatesTxtField;

    @FXML
    private TextField transitionElementsTxtField;

    @FXML
    private TextField outputsTxtField;

    @FXML
    private TableView<TableViewTest> MealyAutomatonTableview = new TableView<>();


    private int numberOfStates;
    private int numberOfColumns;
    private String[] transitions;
    private String[] outputs;
    private int counter = 0;

    private ObservableList<String> stateOptions;
    private ObservableList<String> outputsOptions;
    private ArrayList<String> states = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> statesMatrix= new ArrayList<>();
    private ArrayList<ArrayList<Integer>> outputsMatrix=new ArrayList<>();
    private StateMachine stateMachine;
    private boolean mooreFinalMachineReady = false;
    private boolean mealyFinalMachineReady = false;
    private String[][] finalMachineTable;


    @FXML
    protected void startApplication() {
        Stage stage = (Stage) welcomeTxt.getScene().getWindow();
        stage.close();
        launchFXML("definition-of-table-view.fxml","Definition");
    }

    private void initializeOptions(){
        states.clear();
        for(int i=0;i<numberOfStates;i++){
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
    void generateInitialMooreAutomaton(ActionEvent event) {
        try{
            ObservableList<TableViewTest> list = mooreAutomatonTableview.getItems();
            statesMatrix.clear();
            outputsMatrix.clear();
            for(int i=0;i<numberOfStates;i++){
                ArrayList<Integer> statesValues = new ArrayList<>();
                ArrayList<Integer> outputValues = new ArrayList<>();
                statesValues.add(i);
                outputValues.add(i);
                for(int j=0;j<numberOfColumns;j++){
                    statesValues.add(Integer.parseInt(list.get(i).getOption(j).substring(1)));
                }
                outputValues.add(Integer.parseInt(list.get(i).getOption(numberOfColumns)));
                outputsMatrix.add(outputValues);
                statesMatrix.add(statesValues);
            }
            mooreFinalMachineReady=true;
            stateMachine = new StateMachine(statesMatrix,outputsMatrix);
            finalMachineTable = stateMachine.getMooreFinalMachine();
            insertNewMooreColumns();
            Stage stage = (Stage) mooreAutomatonTableview.getScene().getWindow();
            stage.close();
            launchFXML("NewMooreMachine.fxml","Moore machine");
        }
        catch (Exception e){
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Please fill all cells");
            a.show();
        }

    }

    @FXML
    void generateInitialAutomatonMealy(ActionEvent event) {
        try{
            ObservableList<TableViewTest> mealyList = MealyAutomatonTableview.getItems();
            statesMatrix.clear();
            outputsMatrix.clear();
            for(int i=0;i<numberOfStates;i++){
                ArrayList<Integer> statesValues = new ArrayList<>();
                ArrayList<Integer> outputValues = new ArrayList<>();
                statesValues.add(i);
                outputValues.add(i);
                for(int j=0;j<numberOfColumns*2;j++){
                    if(j%2==0){
                        statesValues.add(Integer.parseInt(mealyList.get(i).getOption(j).substring(1)));
                    }
                    else{
                        outputValues.add(Integer.parseInt(mealyList.get(i).getOption(j)));
                    }
                }
                statesMatrix.add(statesValues);
                outputsMatrix.add(outputValues);
            }
            mealyFinalMachineReady=true;
            stateMachine = new StateMachine(statesMatrix,outputsMatrix);
            finalMachineTable = stateMachine.getMealyFinalMachine();
            insertNewMealyColumns();
            Stage stage = (Stage) MealyAutomatonTableview.getScene().getWindow();
            stage.close();
            launchFXML("NewMealyMachine.fxml","Mealy machine");
        }
        catch (Exception e){
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Please fill all cells");
            a.show();
        }
    }

    private boolean correctAutomatonDefinition(){
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setAlertType(Alert.AlertType.ERROR);
        a.setContentText("* Number of states must be greater than 2 \n" +
                "* For transitions use only letters separated with comas \n"+
                "* For outputs use only numbers separated with comas");
        try{
            if(Integer.parseInt(numberOfStatesTxtField.getText()) >= 2 ){
                if(outputsTxtField.getText().matches("^(([0-9\s](,)?)*)+$")){
                    if((transitionElementsTxtField.getText()).matches("^(([a-zA-Z\s](,)?)*)+$")){
                        return true;
                    }
                }
            }
            throw new Exception();
        }
        catch (Exception e){
            a.show();
            return false;
        }
    }

    @FXML
    protected void buildMooreBtn(){
        if(correctAutomatonDefinition()){
            tableGenericInitialize();
            insertMooreColumns();
            insertMooreValues();
            launchFXML("Mooreautomaton-table-view.fxml","Automaton table");
        }
    }
    @FXML
    protected void buildMealyBtn() {
        if(correctAutomatonDefinition()){
            tableGenericInitialize();
            insertMealyColumns();
            insertMealyValues();
            launchFXML("Mealyautomaton-table-view.fxml","Automaton table");
        }
    }

    private void tableGenericInitialize(){
        numberOfStates = Integer.parseInt(numberOfStatesTxtField.getText());
        transitions = transitionElementsTxtField.getText().split(",");
        numberOfColumns = transitions.length;
        outputs = outputsTxtField.getText().split(",");
        initializeOptions();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            insertMooreColumns();
            insertMooreValues();
            insertMealyColumns();
            insertMealyValues();
            if(mooreFinalMachineReady){
                insertNewMooreColumns();
            }
            if(mealyFinalMachineReady){
                insertNewMealyColumns();
            }
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

    private void insertMooreColumns(){
        TableColumn statesColumn = insertStatesColumn();
        mooreAutomatonTableview.getColumns().add(statesColumn);
        for (int i = 0; i < numberOfColumns; i++) {
            TableColumn<TableViewTest, StringProperty> column = new TableColumn<>(transitions[i]);
            int finalI = i;
            cellValueFactory(column,finalI);
            fillWithCombobox(column,stateOptions);
            mooreAutomatonTableview.getColumns().add(column);
        }
        TableColumn<TableViewTest, StringProperty> outputColumn = new TableColumn<>("Output");
        cellValueFactory(outputColumn,numberOfColumns);
        fillWithCombobox(outputColumn,outputsOptions);

        mooreAutomatonTableview.getColumns().add(outputColumn);
    }

    private void insertNewMooreColumns(){
        for(int i=0;i<finalMachineTable[0].length;i++){
            TableColumn<String[],String> newColumn;
            if(i==0){
                newColumn = new TableColumn<>("State");
            }
            else if(i==finalMachineTable[0].length-1){
                newColumn = new TableColumn<>("Output");
            }
            else{
                newColumn = new TableColumn<>(transitions[i-1]);
            }
            int finalI = i;
            newColumn.setCellValueFactory((p)->{
                String[] x = p.getValue();
                return new SimpleStringProperty(x != null && x.length>0 ? x[finalI] : "<no name>");
            });
            newMooreMachineTableView.getColumns().add(newColumn);
        }
        newMooreMachineTableView.getItems().addAll(finalMachineTable);
    }

    private void insertNewMealyColumns(){
        for(int i=0;i<finalMachineTable[0].length;i++){
            TableColumn<String[],String> newColumn;
            if(i==0){
                newColumn = new TableColumn<>("State");
            }
            else if(i%2==0){
                newColumn = new TableColumn<>("Value");
            }
            else{
                newColumn=new TableColumn<>(transitions[(i-1)/2]);
            }
            int finalI = i;
            newColumn.setCellValueFactory((p)->{
                String[] x = p.getValue();
                return new SimpleStringProperty(x != null && x.length>0 ? x[finalI] : "<no name>");
            });
            newMealyMachineTableView.getColumns().add(newColumn);
        }
        newMealyMachineTableView.getItems().addAll(finalMachineTable);
    }

    private void insertMealyColumns(){
        TableColumn statesColumn = insertStatesColumn();
        MealyAutomatonTableview.getColumns().add(statesColumn);

        for (int i = 0; i < numberOfColumns*2; i++) {
            TableColumn<TableViewTest, StringProperty> column;
            if(i%2==0){
                column = new TableColumn<>(transitions[i/2]);
                int finalI = i;
                cellValueFactory(column,finalI);
                fillWithCombobox(column,stateOptions);
            }
            else {
                column = new TableColumn<>("Value");
                int finalI = i;
                cellValueFactory(column,finalI);
                fillWithCombobox(column,outputsOptions);
            }
            MealyAutomatonTableview.getColumns().add(column);
        }
    }

    private TableColumn insertStatesColumn() {
        counter = -2;
        TableColumn statesColumn = new TableColumn("State");
        statesColumn.setCellFactory(c ->{
            TableCell de = new TableCell<>();
            if(counter<numberOfStates){
                de.setText("Q"+counter);
            }
            counter++;
            return de;
        });
        return statesColumn;
    }

    private void cellValueFactory(TableColumn<TableViewTest, StringProperty> column,int a){
        column.setCellValueFactory(z ->{
            StringProperty value = z.getValue().optionProperty(a);
            return Bindings.createObjectBinding(() -> value);
        } );
    }



    private void insertMooreValues(){
        for (int i = 0; i < numberOfStates; i++) {
            mooreAutomatonTableview.getItems().add(
                    new TableViewTest(numberOfColumns+1)
            );
        }
    }

    private void insertMealyValues(){
        for (int i = 0; i < numberOfStates; i++) {
            MealyAutomatonTableview.getItems().add(
                    new TableViewTest(numberOfColumns*2)
            );
        }
    }

}