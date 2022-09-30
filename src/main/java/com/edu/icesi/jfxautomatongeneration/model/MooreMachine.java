package com.edu.icesi.jfxautomatongeneration.model;

import java.util.ArrayList;
import java.util.Collections;

public class MooreMachine {
    public ArrayList<ArrayList<Integer>> states;
    public ArrayList<Integer> checkList;
    public ArrayList<ArrayList<Integer>> outPuts;
    private int tablesEnd;
    private ArrayList<String> combinations = new ArrayList<>();

    public ArrayList<ArrayList<Integer>> partitioning = new ArrayList<>();


    public MooreMachine(ArrayList<ArrayList<Integer>> states, ArrayList<ArrayList<Integer>> outPuts){
        this.states=states;
        checkList=new ArrayList<>();
        this.outPuts=outPuts;
        checkList.add(0);
        tablesEnd=states.get(0).size();
        machineProcess();

    }

    public void machineProcess(){
        isConnectedToRoot();
        deleteStatesRootUnconnected();
        createInitialPartition();
    }

    public void createInitialPartition(){
        for(int i=0;i<outPuts.size();i++){
            String combination = "";
            for(int j=1;j<outPuts.get(i).size();j++){
                combination += outPuts.get(i).get(j)+"";
            }
            int index = isInCombinations(combinations,combination);
            if(index!=-1){
                partitioning.get(index).add(outPuts.get(i).get(0));
            }
            else{
                combinations.add(combination);
                ArrayList<Integer> newCombination = new ArrayList<>();
                newCombination.add(outPuts.get(i).get(0));
                partitioning.add(newCombination);
            }
        }
        showPartitioning();
    }

    private void showPartitioning(){
        for(int i=0;i<partitioning.size();i++){
            System.out.println("Combination: "+combinations.get(i));
            System.out.println("Elements");
            for (int j=0;j<partitioning.get(i).size();j++){
                System.out.println(partitioning.get(i).get(j));
            }
        }
    }

    private int isInCombinations(ArrayList<String> combinations,String combination){
        for(int i=0;i<combinations.size();i++){
            if(combinations.get(i).equals(combination)){
                return i;
            }
        }
        return -1;
    }


    public void deleteStatesRootUnconnected(){
        Collections.sort(checkList);
        for(int i=0;i<states.size();i++){
            if(!searchState(i)){
                System.out.println("Removed "+i);
                states.remove(i);
                outPuts.remove(i);
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
            //System.out.println(index+"a probar");
            //System.out.println("Columna :"+c+" Fila :"+r);
            if (!searchState(index)){
                //System.out.println("C :"+c+" F :"+r);
                //System.out.println("No está: "+index);
                checkList.add(index);
                isConnectedToRoot(0,index);
                isConnectedToRoot(c+1,r);
            }
            else {
                //System.out.println("Ya está: " + index);
                isConnectedToRoot(c+1, r);
                //System.out.println("MUERTE");
                //System.out.println("C :"+c+" F :"+r);
            }
        }
        /*else{
            System.out.println("Se murio "+c+""+r);
        }
         */
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
