package com.edu.icesi.jfxautomatongeneration.model;

import java.util.ArrayList;
import java.util.Collections;

public class MooreMachine {
    public ArrayList<ArrayList<Integer>> states;
    public ArrayList<Integer> checkList;
    public ArrayList<Integer> outPuts;
    private int tablesEnd;


    public MooreMachine(ArrayList<ArrayList<Integer>> states, ArrayList<Integer> outPuts){
        this.states=states;
        checkList=new ArrayList<>();
        this.outPuts=outPuts;
        checkList.add(0);
        tablesEnd=states.get(0).size();
        isConnectedToRoot();

    }

    public void deleteStatesRootUnconnected(){
        Collections.sort(checkList);
        for(int i=0;i<states.size();i++){
            if(!searchState(i)){
                System.out.println("Removed "+i);
                states.remove(i);
            }
        }
    }


    //Recorrer matriz, seleccionar nodos conectados a la raiz
    public void isConnectedToRoot(){
         isConnectedToRoot(0, 0);
        for(int j=0;j<checkList.size();j++){
            System.out.println(checkList.get(j));
        }
    }

    private void isConnectedToRoot(int c, int r){
        if((c+1)<tablesEnd){ //sus
            int index=states.get(r).get(c+1); //sus
            System.out.println(index+"a probar");
            System.out.println("Columna :"+c+" Fila :"+r);
            if (!searchState(index)){
                System.out.println("C :"+c+" F :"+r);
                System.out.println("No está: "+index);
                checkList.add(index);
                isConnectedToRoot(0,index);
                isConnectedToRoot(c+1,r);
            }
            else {
                System.out.println("Ya está: " + index);
                isConnectedToRoot(c+1, r);
                System.out.println("MUERTE");
                System.out.println("C :"+c+" F :"+r);
            }
        }
        else{
            System.out.println("Se murio "+c+""+r);
        }
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
