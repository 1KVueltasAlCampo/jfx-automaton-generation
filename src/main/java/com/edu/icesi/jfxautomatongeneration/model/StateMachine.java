package com.edu.icesi.jfxautomatongeneration.model;

import java.util.ArrayList;
import java.util.Collections;

public class StateMachine {
    public ArrayList<ArrayList<Integer>> states; // Q0 -> Q1 (0)  Q0(1)
    public ArrayList<Integer> checkList;
    public ArrayList<ArrayList<Integer>> outPuts;
    private int tablesEnd;
    private ArrayList<String> combinations = new ArrayList<>();
    private int[] blockOfTheState;

    public ArrayList<ArrayList<Integer>> partitioning = new ArrayList<>(); //{Q0,Q3} { Q1,Q4,Q6}
    public ArrayList<ArrayList<Integer>> fPartitioning = new ArrayList<>();


    public StateMachine(ArrayList<ArrayList<Integer>> states, ArrayList<ArrayList<Integer>> outPuts) {
        this.states = states;
        checkList = new ArrayList<>();
        this.outPuts = outPuts;
        checkList.add(0);
        tablesEnd = states.get(0).size();
        machineProcess();

    }

    public void machineProcess() {
        isConnectedToRoot();
        deleteStatesRootUnconnected();
        createInitialPartition();
        partitioningProcess();
        showPartitioning();
    }

    public String[][] getMooreFinalMachine(){
        String[][] mooreFinalMachine = new String[partitioning.size()][states.size()+1];
        for(int i=0;i<partitioning.size();i++){
            mooreFinalMachine[i] = getStateRow(partitioning.get(i).get(0),i);
        }
        return mooreFinalMachine;
    }
    private String[] getStateRow(int index,int position){
        int size = states.get(0).size()+1;
        String[] stateRow = new String[size];
        stateRow[0] = "Q"+position;
        System.out.println("inicio");
        System.out.println("Q"+index);
        for(int i=1;i<size-1;i++){
            ArrayList<Integer> qStates = findStateSet(index);
            System.out.println("Q"+blockOfTheState[qStates.get(i)]);
            stateRow[i] = "Q"+blockOfTheState[qStates.get(i)];
        }
        ArrayList<Integer> qOutputs = findOutputSet(index);
        stateRow[size-1] = qOutputs.get(1)+"";
        //Hay que buscar el indice del primero
        return stateRow;
    }



    private void partitioningProcess(){
        int pastSize;
        int currentSize;
        do{
            pastSize=partitioning.size();
            createPartitioning();
            currentSize=partitioning.size();
        }
        while(currentSize!=pastSize);
    }

    public void createInitialPartition() {
        blockOfTheState = new int[states.size()];
        for (int i = 0; i < outPuts.size(); i++) {
            String combination = "";
            for (int j = 1; j < outPuts.get(i).size(); j++) {
                combination += outPuts.get(i).get(j) + "";
            }
            int index = isInCombinations(combinations, combination);
            if (index != -1) {
                //System.out.println("Q"+outPuts.get(i).get(0)+" is in "+index);
                blockOfTheState[outPuts.get(i).get(0)] = index;
                partitioning.get(index).add(outPuts.get(i).get(0));
            } else {
                combinations.add(combination); //Se agrega la nueva combinacion. Ej: 1, 10, 01, etc
                ArrayList<Integer> newCombination = new ArrayList<>();
                newCombination.add(outPuts.get(i).get(0));
                partitioning.add(newCombination); //Se añade un estado a la particion. EJ: Q1, Q0
                //System.out.println("Q"+outPuts.get(i).get(0)+" is in "+(partitioning.size()-1));
                blockOfTheState[outPuts.get(i).get(0)] = partitioning.size()-1;
            }
        }
    }

    public void createPartitioning(){
        //Validation to stop
        for(int i=0;i<partitioning.size();i++){
            ArrayList<Integer> newBlock = new ArrayList<>();
            System.out.println("Size = "+partitioning.get(i).size());
                for(int j=1;j<partitioning.get(i).size();j++){
                    int differentState = areInSameBlock(partitioning.get(i).get(0),partitioning.get(i).get(j));
                    if( differentState!= -1){
                        newBlock.add(partitioning.get(i).get(j));
                        partitioning.get(i).remove(j);
                        blockOfTheState[differentState] = partitioning.size();
                        j--;
                    }
                }
                System.out.println("Fin del ciclo "+"Size = "+partitioning.get(i).size());
                if(newBlock.size()!=0){
                    partitioning.add(newBlock);
                }
        }
    }

    private int areInSameBlock(int firstState,int comparableState){
        ArrayList<Integer> firstStateSet = findStateSet(firstState);
        ArrayList<Integer> comparableStateSet = findStateSet(comparableState);
        if(firstStateSet!=null && comparableStateSet != null){
            for(int i=1;i<firstStateSet.size();i++){
                System.out.println(firstStateSet.get(i)+":"+blockOfTheState[firstStateSet.get(i)]+" = "+comparableStateSet.get(i)+": "+blockOfTheState[comparableStateSet.get(i)]);
                if(blockOfTheState[firstStateSet.get(i)]!=blockOfTheState[comparableStateSet.get(i)]){
                    return comparableState;
                }
            }
        }
        //System.out.println("Null "+firstState+" or "+comparableState);
        return -1;
    }

    private ArrayList<Integer> findStateSet(int qState){
        for(int i=0;i<states.size();i++){
            if(qState==states.get(i).get(0)){
                ArrayList<Integer> stateWithoutIndex = states.get(i);
                return stateWithoutIndex;
            }
        }
        System.out.println("Null for "+qState);
        return null;
    }

    private ArrayList<Integer> findOutputSet(int qState){
        for(int i=0;i<states.size();i++){
            if(qState==states.get(i).get(0)){
                ArrayList<Integer> stateWithoutIndex = outPuts.get(i);
                return stateWithoutIndex;
            }
        }
        System.out.println("Null for "+qState);
        return null;
    }

    private void showPartitioning() {
        for (int i = 0; i < partitioning.size(); i++) {
            System.out.println("Elements " + partitioning.get(i).size());
            for (int j = 0; j < partitioning.get(i).size(); j++) {
                System.out.println(partitioning.get(i).get(j));
            }
        }
    }



    private int isInCombinations(ArrayList<String> combinations, String combination) {
        for (int i = 0; i < combinations.size(); i++) {
            if (combinations.get(i).equals(combination)) {
                return i;
            }
        }
        return -1;
    }


    public void deleteStatesRootUnconnected() {
        ArrayList<ArrayList<Integer>> newStates = new ArrayList<>();
        ArrayList<ArrayList<Integer>> newOutputs = new ArrayList<>();
        Collections.sort(checkList);
        for (int i = 0; i < checkList.size(); i++) {
            newStates.add(states.get(checkList.get(i)));
            newOutputs.add(outPuts.get(checkList.get(i)));
        }
        states=newStates;
        outPuts=newOutputs;
    }


    //Recorrer matriz, seleccionar nodos conectados a la raiz
    public void isConnectedToRoot() {
        isConnectedToRoot(0, 0);
        for (int j = 0; j < checkList.size(); j++) {
            // System.out.println(checkList.get(j));
        }
    }

    private void isConnectedToRoot(int c, int r) {
        if ((c + 1) < tablesEnd) { //sus
            int index = states.get(r).get(c + 1); //sus
            //System.out.println(index+"a probar");
            //System.out.println("Columna :"+c+" Fila :"+r);
            if (!searchState(index)) {
                //System.out.println("C :"+c+" F :"+r);
                //System.out.println("No está: "+index);
                checkList.add(index);
                isConnectedToRoot(0, index);
                isConnectedToRoot(c + 1, r);
            } else {
                //System.out.println("Ya está: " + index);
                isConnectedToRoot(c + 1, r);
                //System.out.println("MUERTE");
                //System.out.println("C :"+c+" F :"+r);
            }
        }
        /*else{
            System.out.println("Se murio "+c+""+r);
        }
         */
    }

    private boolean searchState(int state) {
        for (int i = 0; i < checkList.size(); i++) {
            if (checkList.get(i) == state) {
                return true;
            }
        }
        return false;
    }

}
