package com.edu.icesi.jfxautomatongeneration.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class TableViewTest {
    private List<StringProperty> options = new ArrayList<>();
    public TableViewTest(int size){
        createOptions(size);
    }
    private void createOptions(int size){
        for (int i=0;i<size;i++){
            options.add(new SimpleStringProperty());
        }
    }

    private StringProperty option = new SimpleStringProperty();

    public String getOption(int b) {
        return options.get(b).get();
    }

    public void setOption(String value) {
        option.set(value);
    }

    public StringProperty optionProperty(int index) {
        return options.get(index);
    }

}