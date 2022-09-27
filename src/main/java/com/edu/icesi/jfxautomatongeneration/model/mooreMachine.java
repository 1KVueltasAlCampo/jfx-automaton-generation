package com.edu.icesi.jfxautomatongeneration.model;

import java.util.ArrayList;

public class mooreMachine {
    public ArrayList<ArrayList<Integer>> states;
    public ArrayList<Integer> checkList;

    public mooreMachine(ArrayList<ArrayList<Integer>> states){
        this.states=states;

    }

    public ArrayList<Integer> deleteStatesRootUnconnected(ArrayList<ArrayList<Integer>> states){
        int n=states.size();
        int m=states.get(0).size();
        return states.get(0);

    }

    //Recorrer matriz, seleccionar nodos conectados a la raiz
    public void isConnectedToRoot(){
         isConnectedToRoot(0, 0, 0);
    }

    private void isConnectedToRoot(int c, int r, int i){
            //C < row length
        if(r<states.get(0).size()-1){
            int index=states.get(r).get(c);
            if (!searchState(index)){
                checkList.add(index);
                isConnectedToRoot(c,index,i);
            }
            else{
                isConnectedToRoot(c,i+1,i+1);
            }
        }
        return;
    }
    private boolean searchState(int state){
        for(int i=0;i<checkList.size();i++){
            if (checkList.get(i)==state){
                return true;
            }
        }
        return false;
    }

    public void matrixCreation(ArrayList<String> states, String checkList, int i){

    }

    public ArrayList<String> subGrouping(ArrayList<ArrayList<String>> states){
        return states.get(0);
    }
}
