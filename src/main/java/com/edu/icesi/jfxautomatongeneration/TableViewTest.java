package com.edu.icesi.jfxautomatongeneration;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TableViewTest {


    private StringProperty option = new SimpleStringProperty();

    public String getOption() {
        return option.get();
    }

    public void setOption(String value) {
        option.set(value);
    }

    public StringProperty optionProperty() {
        return option;
    }

}