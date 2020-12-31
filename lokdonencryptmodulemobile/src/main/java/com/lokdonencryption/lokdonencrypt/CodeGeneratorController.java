package com.lokdonencryption.lokdonencrypt;

import java.util.ArrayList;
import java.util.Random;

public class CodeGeneratorController {

    private int KNIGHT_MATRIX_WIDTH = 16;
    private int KNIGHT_MATRIX_HEIGHT = 16;
    private int ST_SIZE = 256;//start from 0
    private int KNIGHT_MOVE_TOTAL = 5;

    public static CodeGeneratorController instance = null;
    private int[] knightMoveNumber;// = new int[KNIGHT_MOVE_TOTAL];
    private ArrayList<int[]> moveNumberList = null;
    private ArrayList<char[]> moveStringList = null;
    private ArrayList<int[]> ecryptKeyValue = null;

    private CodeGeneratorController() {


    }

    /**
     * Single Ton class
     *
     * @return
     */
    public static CodeGeneratorController getInstance() {
        if (instance == null)
            instance = new CodeGeneratorController();
        return instance;
    }


    /**
     * this will generate random number for knight move's
     *
     * @return
     */
    private int[] generateKnightMoveNumber(int move) {
        KNIGHT_MOVE_TOTAL = move;
        knightMoveNumber = new int[KNIGHT_MOVE_TOTAL];
        for (int i = 0; i < knightMoveNumber.length; i++) {
            knightMoveNumber[i] = new Random().nextInt((KNIGHT_MATRIX_WIDTH * KNIGHT_MATRIX_HEIGHT) - 1);
        }
//        int random[] = {130,54,87,93,218};
//        knightMoveNumber =random;
        return knightMoveNumber;


    }

    /**
     * Provide first even number to generate silent password
     *
     * @return
     */
    private int getFirstEvenNumberFromKnightMoveIndex() {
        // as discussed with JOE there is always one even number
        int tempNumber = -1;
        while (tempNumber % 2 != 0) {
            tempNumber = new Random().nextInt((KNIGHT_MATRIX_WIDTH * KNIGHT_MATRIX_HEIGHT) - 1);
        }
        //DebugUtil.printLog("tempNumber  " + tempNumber);

        return tempNumber;
    }


    /**
     *  Generating new encrypt key
     * @param key
     * @return
     */
    public String generatePassKey(char[] key ,int move) {

        generateKnightMoveNumber(move);
        moveNumberList = new ArrayList<int[]>();
        moveStringList = new ArrayList<char[]>();
        ecryptKeyValue = new ArrayList<int[]>();

        // Generating knight move using random number
        for (int i = 0; i < knightMoveNumber.length; i++) {
            KnightSolver solver = new KnightSolver(KNIGHT_MATRIX_WIDTH, KNIGHT_MATRIX_HEIGHT);
            solver.setPosition(knightMoveNumber[i] % KNIGHT_MATRIX_WIDTH, knightMoveNumber[i] / KNIGHT_MATRIX_HEIGHT);
            int temp[] = solver.loopSolver();
            moveNumberList.add(temp);
            solver.reset();
            solver = null;
        }


        // Creating new character array using knight moves
        for (int index = 0; index < moveNumberList.size(); index++)
        {
            char tempArr[] = new char[(KNIGHT_MATRIX_WIDTH * KNIGHT_MATRIX_HEIGHT)];
            System.out.print("M"+(index+1)+"  --  ");
            for (int i = 0; i < moveNumberList.get(index).length; i++) {
                tempArr[i] = AllCharacter.getInstance().getStandardCharacterSet()[moveNumberList.get(index)[i]];
                System.out.print(AllCharacter.getInstance().getStandardCharacterSet()[moveNumberList.get(index)[i]]+",");
            }

            System.out.println();
            moveStringList.add(tempArr);
            tempArr = null;
        }

        char orignalPassword[] = new char[key.length];
        orignalPassword = key;
        char silentPassword[] = new char[key.length];
        int orignalPassIndex[] = new int[key.length];
        int silentPassIndex[] = new int[key.length];
        for (int index = 0; index < moveStringList.size(); index++) {

            System.out.print(" orignal index -");
            for (int i = 0;i<orignalPassword.length;i++)
            {
                for (int j = 0;j<moveStringList.get(index).length;j++)
                {
                    if(orignalPassword[i] == moveStringList.get(index)[j])
                    {
                        orignalPassIndex[i]= j;
                        System.out.print(orignalPassIndex[i]+",");
                    }
                }

            }
            System.out.println();


            if(index == 0)
            {
                System.out.print(" reverse pass-");
                int val = 0;
                int travelling = ST_SIZE - key.length;
                for (int j = moveStringList.get(index).length-1;j>=(travelling);j--)
                {
                    silentPassword[val] = moveStringList.get(index)[j];
                    silentPassIndex[val] = j;
                    val++;
                }

            }
            else
            {
                for (int i = 0;i<silentPassword.length;i++)
                {
                    for (int j = 0;j<moveStringList.get(index).length;j++)
                    {
                        if(silentPassword[i] == moveStringList.get(index)[j])
                            silentPassIndex[i] = j;
                    }
                }
            }

            System.out.println();
            //  System.out.print("value -   ");
            int newAddedValue[] = new int[key.length];
            for (int i = 0;i<silentPassIndex.length;i++)
            {
                int value = silentPassIndex[i]+orignalPassIndex[i];
                //System.out.print(silentPassIndex[i]+"+"+orignalPassIndex[i]+" ="+value);
                if(value>ST_SIZE-1)
                {
                    newAddedValue[i] = value-ST_SIZE;
                }
                else
                {
                    newAddedValue[i] = value;
                }
                // System.out.println();
                // System.out.print(newAddedValue[i]+",");

            }
            System.out.println();
            //   System.out.println(" newAddedValue.length "+newAddedValue.length);
            // System.out.print(index+" Move Password -");
            char newAddedValueTemp[] = new char[newAddedValue.length];
            for (int i = 0;i<newAddedValue.length;i++)
            {
                newAddedValueTemp[i] = moveStringList.get(index)[newAddedValue[i]];
                // System.out.print(moveStringList.get(index)[newAddedValue[i]]);
            }
            System.out.println();

            orignalPassword = newAddedValueTemp;

        }


        System.out.println();
        System.out.print("Silent Pass - ");
        for (int i = 0;i<silentPassword.length;i++)
            System.out.print(silentPassword[i]);

        System.out.println();

        StringBuffer buffer = new StringBuffer();
        buffer.append(orignalPassword);
        buffer.append(silentPassword);
        System.out.println();
        System.out.print("Knight move - ");
        for (int i = 0;i<knightMoveNumber.length;i++)
        {
            buffer.append(AllCharacter.getInstance().getCharacterReverseArray()[knightMoveNumber[i]]);
            System.out.print(knightMoveNumber[i]+",");
        }
        buffer.append(AllCharacter.getInstance().getCharacterReverseArray()[knightMoveNumber.length]);

        System.out.println();
        return buffer.toString();


        //qĉĭĺô^T­7''Ð^ô
    }

    /**
     * break the password in array
     * array 0 contain encrypt pass
     * array 1 contain silent pass
     * array 2 contain Knight tour moves
     * array 3 contain silent index
     *
     * @param encryptPass
     * @return
     */
    private String[] getPasswordArray(String encryptPass) {
        String[] passArray = new String[3];
        for (int i = 0; i < passArray.length; i++) {
            passArray[i] = new String();
        }

        System.out.println("  Move Character "+encryptPass.charAt(encryptPass.length()-1));
        int move = 0;
        for (int i = 0;i<AllCharacter.getInstance().getCharacterReverseArray().length;i++)
        {
            if(AllCharacter.getInstance().getCharacterReverseArray()[i] == encryptPass.charAt(encryptPass.length()-1))
            {
                System.out.println(" Total Move "+i);
                move = i;
            }
        }

        int start = (encryptPass.length())-(move+1);
        int end = encryptPass.length()-1;
//        System.out.println(start +"   9999     "+end);
//        System.out.println(" Move "+encryptPass.substring(start,end));
//        System.out.println(" Pass "+encryptPass.substring(0,(start+1)/2));
//        System.out.println(" Silent Pass "+encryptPass.substring((start+1)/2,start));

        passArray[0] = encryptPass.substring(start,end);// knight move
        passArray[1] = encryptPass.substring(0,(start+1)/2); // password
        passArray[2] = encryptPass.substring((start+1)/2,start);// silent password
        return passArray;
    }

    /**
     * Decrypt The orignal value
     *
     * @param pass
     * @return
     */
    public String decryptSilentPassKey(String pass) {
        String keyArr[] = getPasswordArray(pass);

        moveNumberList = new ArrayList<int[]>();
        moveStringList = new ArrayList<char[]>();
        ecryptKeyValue = new ArrayList<int[]>();

        int decKnighMove [] = new int[keyArr[0].length()];
        char moveChar[] = new char[keyArr[0].length()];
        moveChar = keyArr[0].toCharArray();
        System.out.println();
        System.out.print("Dec Knight Move - ");
        for ( int i = 0;i<decKnighMove.length;i++)
        {

            for (int j = 0;j<AllCharacter.getInstance().getCharacterReverseArray().length;j++)
            {
                if(moveChar[i] == AllCharacter.getInstance().getCharacterReverseArray()[j])
                {
                    decKnighMove[i] = j;
                    System.out.print(j+",");
                }
            }
        }

        System.out.println();
        // Generating knight move using random number
        for (int i = 0; i < decKnighMove.length; i++) {
            KnightSolver solver = new KnightSolver(KNIGHT_MATRIX_WIDTH, KNIGHT_MATRIX_HEIGHT);

            solver.setPosition(decKnighMove[i] % KNIGHT_MATRIX_WIDTH, decKnighMove[i] / KNIGHT_MATRIX_HEIGHT);
            int temp[] = solver.loopSolver();
            moveNumberList.add(temp);
            solver.reset();

            solver = null;
        }


        // Creating new character array using knight moves
        for (int index = 0; index < moveNumberList.size(); index++) {
            char tempArr[] = new char[(KNIGHT_MATRIX_WIDTH * KNIGHT_MATRIX_HEIGHT)];
            //  System.out.print("M"+(index+1)+"  --  ");
            for (int i = 0; i < moveNumberList.get(index).length; i++) {
                tempArr[i] = AllCharacter.getInstance().getStandardCharacterSet()[moveNumberList.get(index)[i]];
                // System.out.print(AllCharacter.getInstance().getStandardCharacterSet()[moveNumberList.get(index)[i]]+",");
            }

            System.out.println();
            moveStringList.add(tempArr);


            tempArr = null;
        }

        char orignalPassword[] = new char[keyArr[1].length()];
        orignalPassword = keyArr[1].toCharArray();
        char silentPassword[] = new char[keyArr[1].length()];
        silentPassword = keyArr[2].toCharArray();
        int orignalPassIndex[] = new int[keyArr[1].length()];
        int silentPassIndex[] = new int[keyArr[1].length()];
        for (int index = moveStringList.size()-1;index>=0;index--)
        {
            for (int i = 0;i<orignalPassword.length;i++)
            {
                //System.out.print(index+" pass index ");
                for (int j = 0;j<moveStringList.get(index).length;j++)
                {
                    if(orignalPassword[i] == moveStringList.get(index)[j])
                    {
                        orignalPassIndex[i] = j;
                        //System.out.print(j+",");
                    }
                }
            }
            System.out.println();

            for (int i = 0;i<silentPassword.length;i++)
            {
                ///System.out.print(index+" silent pass index ");
                for (int j = 0;j<moveStringList.get(index).length;j++)
                {
                    if(silentPassword[i] == moveStringList.get(index)[j])
                    {
                        silentPassIndex[i] = j;
                        // System.out.print(j+",");
                    }

                }
            }
            System.out.println();
            System.out.print("value -   ");
            int newAddedValue[] = new int[orignalPassIndex.length];
            for (int i = 0;i<orignalPassIndex.length;i++)
            {
                int value = orignalPassIndex[i]+ST_SIZE;
                //System.out.print(silentPassIndex[i]+"+"+orignalPassIndex[i]+" ="+value);
                if(value>ST_SIZE-1)
                {
                    int temp  = value-silentPassIndex[i];
                    if(temp >ST_SIZE -1)
                        temp = temp-ST_SIZE;
                    newAddedValue[i] = temp;
                }
                else
                {
                    newAddedValue[i] = value;
                }
                // System.out.println();
                System.out.print(newAddedValue[i]+",");

            }
            System.out.println();
            char newAddedValueTemp[] = new char[newAddedValue.length];
            for (int i = 0;i<newAddedValue.length;i++)
            {
                newAddedValueTemp[i] = moveStringList.get(index)[newAddedValue[i]];
                // System.out.print(moveStringList.get(index)[newAddedValue[i]]);
            }
            System.out.println();

            orignalPassword = newAddedValueTemp;


        }
        StringBuffer buff = new StringBuffer();
        buff.append(orignalPassword);
        return buff.toString();

    }





}
