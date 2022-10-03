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

    //Runs all the process required
    public void machineProcess() {
        isConnectedToRoot();
        deleteStatesRootUnconnected();
        createInitialPartition();
        partitioningProcess();
    }

    //Returns an reduced Moore automaton machine
    public String[][] getMooreFinalMachine(){
        String[][] mooreFinalMachine = new String[partitioning.size()][states.size()+1];
        for(int i=0;i<partitioning.size();i++){
            mooreFinalMachine[i] = getMooreStateRow(partitioning.get(i).get(0),i);
        }
        return mooreFinalMachine;
    }

    //Returns a reduced row, for a Moore machine
    private String[] getMooreStateRow(int index,int position){
        int size = states.get(0).size()+1;
        String[] stateRow = new String[size];
        stateRow[0] = "Q"+position;
        for(int i=1;i<size-1;i++){
            ArrayList<Integer> qStates = findStateSet(index);
            stateRow[i] = "Q"+blockOfTheState[qStates.get(i)];
        }
        ArrayList<Integer> qOutputs = findOutputSet(index);
        stateRow[size-1] = qOutputs.get(1)+"";
        //Hay que buscar el indice del primero
        return stateRow;
    }

    //Returns an reduced Mealy automaton machine
    public String[][] getMealyFinalMachine(){
        int sizeOfRows = 1+((states.get(0).size()-1)*2);
        String[][] mealyFinalMachine = new String[partitioning.size()][sizeOfRows];
        for(int i=0;i<partitioning.size();i++){
            mealyFinalMachine[i] = getMealyStateRow(partitioning.get(i).get(0),i);
        }
        return mealyFinalMachine;
    }

    //Returns a reduced row, for a Mealy machine
    private String[] getMealyStateRow(int index,int position){
        int size = 1+((states.get(0).size()-1)*2);
        String[] stateRow = new String[size];
        stateRow[0] = "Q"+position;
        for(int i=1;i<size;i++){
            ArrayList<Integer> qStates = findStateSet(index);
            ArrayList<Integer> qOutputs = findOutputSet(index);
            if((i+1)%2==0){
                stateRow[i] = "Q"+blockOfTheState[qStates.get(i/2)];
            }
            else{
                stateRow[i] = qOutputs.get((i-1)/2)+"";
            }
        }
        //Hay que buscar el indice del primero
        return stateRow;
    }


    //Made partitioning on the automaton
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

    //Create the first set of partitioning aiming to find the reduced machine
    public void createInitialPartition() {
        blockOfTheState = new int[states.size()];
        for (int i = 0; i < outPuts.size(); i++) {
            String combination = "";
            for (int j = 1; j < outPuts.get(i).size(); j++) {
                combination += outPuts.get(i).get(j) + "";
            }
            int index = isInCombinations(combinations, combination);
            if (index != -1) {
                blockOfTheState[outPuts.get(i).get(0)] = index;
                partitioning.get(index).add(outPuts.get(i).get(0));
            } else {
                combinations.add(combination); //Se agrega la nueva combinacion. Ej: 1, 10, 01, etc
                ArrayList<Integer> newCombination = new ArrayList<>();
                newCombination.add(outPuts.get(i).get(0));
                partitioning.add(newCombination); //Se añade un estado a la particion. EJ: Q1, Q0
                blockOfTheState[outPuts.get(i).get(0)] = partitioning.size()-1;
            }
        }
    }

    //It starts making partitions inside the initial partition
    public void createPartitioning(){
        //Validation to stop
        for(int i=0;i<partitioning.size();i++){
            ArrayList<Integer> newBlock = new ArrayList<>();
                for(int j=1;j<partitioning.get(i).size();j++){
                    int differentState = areInSameBlock(partitioning.get(i).get(0),partitioning.get(i).get(j));
                    if( differentState!= -1){
                        newBlock.add(partitioning.get(i).get(j));
                        partitioning.get(i).remove(j);
                        blockOfTheState[differentState] = partitioning.size();
                        j--;
                    }
                }
                if(newBlock.size()!=0){
                    partitioning.add(newBlock);
                }
        }
    }

    //Validate if two states are in the same set verifying it at the previous obtained partition
    private int areInSameBlock(int firstState,int comparableState){
        ArrayList<Integer> firstStateSet = findStateSet(firstState);
        ArrayList<Integer> comparableStateSet = findStateSet(comparableState);
        if(firstStateSet!=null && comparableStateSet != null){
            for(int i=1;i<firstStateSet.size();i++){
                if(blockOfTheState[firstStateSet.get(i)]!=blockOfTheState[comparableStateSet.get(i)]){
                    return comparableState;
                }
            }
        }
        return -1;
    }

    //Returns the set which contains an Q state
    private ArrayList<Integer> findStateSet(int qState){
        return getIntegers(qState, states);
    }


    private ArrayList<Integer> getIntegers(int qState, ArrayList<ArrayList<Integer>> states) {
        for(int i=0;i<states.size();i++){
            if(qState==states.get(i).get(0)){
                ArrayList<Integer> stateWithoutIndex = states.get(i);
                return stateWithoutIndex;
            }
        }
        return null;
    }

    //Find the set which contains an output.
    private ArrayList<Integer> findOutputSet(int qState){
        return getIntegers(qState, outPuts);
    }




    private int isInCombinations(ArrayList<String> combinations, String combination) {
        for (int i = 0; i < combinations.size(); i++) {
            if (combinations.get(i).equals(combination)) {
                return i;
            }
        }
        return -1;
    }


    //Find all the states connected to the root and delete the ones disconnected
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


    //Verify all the states connected to root
    public void isConnectedToRoot() {
        isConnectedToRoot(0, 0);
        for (int j = 0; j < checkList.size(); j++) {
        }
    }

    private void isConnectedToRoot(int c, int r) {
        if ((c + 1) < tablesEnd) { //sus
            int index = states.get(r).get(c + 1);
            if (!searchState(index)) {
                checkList.add(index);
                isConnectedToRoot(0, index);
                isConnectedToRoot(c + 1, r);
            } else {
                isConnectedToRoot(c + 1, r);
            }
        }
    }

    //Find a specific state
    private boolean searchState(int state) {
        for (int i = 0; i < checkList.size(); i++) {
            if (checkList.get(i) == state) {
                return true;
            }
        }
        return false;
    }

}
