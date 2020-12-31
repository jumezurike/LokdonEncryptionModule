package com.lokdonencryption.lokdonencrypt;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SmsEncryption {

    private static SmsEncryption instance = null;
    private int KNIGHT_MATRIX_WIDTH = 16;
    private int KNIGHT_MATRIX_HEIGHT = 16;
    private int ST_SIZE = 256;//start from 0
    private int KNIGHT_MOVE_TOTAL = 5;
    private int[] knightMoveNumber = new int[KNIGHT_MOVE_TOTAL];
    //    private int[] knightMoveNumber ={88,26,16,63,50};// = new int[KNIGHT_MOVE_TOTAL];
    private ArrayList<int[]> moveNumberList = null;
    private ArrayList<char[]> moveStringList = null;

    public static SmsEncryption getInstance() {
        if (instance == null)
            instance = new SmsEncryption();
        return instance;
    }

    private SmsEncryption() {

    }


    public String getEncryptSms(String msg, String key1) {
        String key2 = key1.replaceAll("-", "");
        String key3 = key2.replaceAll("\\+", "");
        String key = key3.replaceAll("\\s", "");
        //DebugUtil.printLog("Use phone no " + key);


        ArrayList<String> list = permutation(key);
        Random rand = new Random();
        int randomNum = rand.nextInt(list.size());
        key = list.get(randomNum);
        if (key.length() == 10 || key.length() == 11) {
            String newKey = key;


            knightMoveNumber[0] = Integer.parseInt(newKey.substring(0, 2));
            knightMoveNumber[1] = Integer.parseInt(newKey.substring(2, 4));
            knightMoveNumber[2] = Integer.parseInt(newKey.substring(4, 6));
            knightMoveNumber[3] = Integer.parseInt(newKey.substring(6, 8));
            if (key.length() == 11) {
                knightMoveNumber[4] = Integer.parseInt(checkThreeDigitValue(newKey.substring(8, 11)));
            } else {
                knightMoveNumber[4] = Integer.parseInt(newKey.substring(8, 10));
            }
        } else if (key.length() == 12) {
            String newKey = key;


            knightMoveNumber[0] = Integer.parseInt(newKey.substring(0, 2));
            knightMoveNumber[1] = Integer.parseInt(newKey.substring(2, 4));
            knightMoveNumber[2] = Integer.parseInt(newKey.substring(4, 6));
            knightMoveNumber[3] = Integer.parseInt(checkThreeDigitValue(newKey.substring(6, 9)));


            knightMoveNumber[4] = Integer.parseInt(checkThreeDigitValue(newKey.substring(9, 12)));


        } else if (key.length() == 13) {

            String newKey = key;

            knightMoveNumber[0] = Integer.parseInt(newKey.substring(0, 2));
            knightMoveNumber[1] = Integer.parseInt(newKey.substring(2, 4));
            knightMoveNumber[2] = Integer.parseInt(checkThreeDigitValue(newKey.substring(4, 7)));
            knightMoveNumber[3] = Integer.parseInt(checkThreeDigitValue(newKey.substring(7, 10)));
            knightMoveNumber[4] = Integer.parseInt(checkThreeDigitValue(newKey.substring(10, 13)));
        } else if (key.length() == 14) {

            String newKey = key;

            knightMoveNumber[0] = Integer.parseInt(newKey.substring(0, 2));
            knightMoveNumber[1] = Integer.parseInt(checkThreeDigitValue(newKey.substring(2, 5)));
            knightMoveNumber[2] = Integer.parseInt(checkThreeDigitValue(newKey.substring(5, 8)));
            knightMoveNumber[3] = Integer.parseInt(checkThreeDigitValue(newKey.substring(8, 11)));
            knightMoveNumber[4] = Integer.parseInt(checkThreeDigitValue(newKey.substring(11, 14)));
        } else if (key.length() == 15) {

            String newKey = key;

            knightMoveNumber[0] = Integer.parseInt(checkThreeDigitValue(newKey.substring(0, 3)));
            knightMoveNumber[1] = Integer.parseInt(checkThreeDigitValue(newKey.substring(3, 6)));
            knightMoveNumber[2] = Integer.parseInt(checkThreeDigitValue(newKey.substring(6, 9)));
            knightMoveNumber[3] = Integer.parseInt(checkThreeDigitValue(newKey.substring(9, 12)));
            knightMoveNumber[4] = Integer.parseInt(checkThreeDigitValue(newKey.substring(12, 15)));
        }
        HashMap<Integer, Integer> map = new HashMap<>();

        removeDuplicateEntry(map);
//        Set<Map.Entry<Integer, Integer>> entires = map.entrySet();
//        for (Map.Entry<Integer, Integer> ent : entires) {
//
//            Log.d("4444444&&&" ,""+ ent.getKey() + " ==> " + ent.getValue());
//        }
        moveNumberList = new ArrayList<int[]>();
        moveStringList = new ArrayList<char[]>();

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
        for (int index = 0; index < moveNumberList.size(); index++) {
            char tempArr[] = new char[(KNIGHT_MATRIX_WIDTH * KNIGHT_MATRIX_HEIGHT)];
            System.out.print(knightMoveNumber[index] + "   M" + (index + 1) + "  --  ");
            for (int i = 0; i < moveNumberList.get(index).length; i++) {
                tempArr[i] = AllCharacter.getInstance().getStandardCharacterSet()[moveNumberList.get(index)[i]];
                System.out.print(AllCharacter.getInstance().getStandardCharacterSet()[moveNumberList.get(index)[i]] + ",");
            }

            System.out.println();
            moveStringList.add(tempArr);
            tempArr = null;
        }


        char[] messageArray = msg.toCharArray();
        int[] messageIndex = new int[messageArray.length];

        System.out.print("Password   --  ");
        for (int i = 0; i < messageArray.length; i++) {
            for (int j = 0; j < AllCharacter.getInstance().getStandardCharacterSet().length; j++) {
                if (messageArray[i] == AllCharacter.getInstance().getStandardCharacterSet()[j]) {
                    messageIndex[i] = j;
                    System.out.print(j + ",");
                }
            }
        }

        System.out.println();
        System.out.println("printing new message");
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < messageIndex.length; i++) {

            System.out.print(moveStringList.get(0)[messageIndex[i]] + ",");
            buffer.append(moveStringList.get(0)[messageIndex[i]]);
        }

        for (int i = 0; i < knightMoveNumber.length; i++)
            buffer.append(AllCharacter.getInstance().getCharacterReverseArray()[knightMoveNumber[i]]);

        buffer.append(AllCharacter.getInstance().getCharacterReverseArray()[knightMoveNumber.length]);
        System.out.println();
        System.out.println(" new Generated Message ::::: " + buffer.toString());

        // decryptSms(buffer.toString());
        return buffer.toString();
    }

    public String getEncryptFile1(String msg, String key1) {
        String key2 = key1.replaceAll("-", "");
        String key3 = key2.replaceAll("\\+", "");
        String key = key3.replaceAll("\\s", "");
        //DebugUtil.printLog("Use phone no " + key);


        ArrayList<String> list = permutation(key);
        Random rand = new Random();
        int randomNum = rand.nextInt(list.size());
        key = list.get(randomNum);
        if (key.length() == 10 || key.length() == 11) {
            String newKey = key;


            knightMoveNumber[0] = Integer.parseInt(newKey.substring(0, 2));
            knightMoveNumber[1] = Integer.parseInt(newKey.substring(2, 4));
            knightMoveNumber[2] = Integer.parseInt(newKey.substring(4, 6));
            knightMoveNumber[3] = Integer.parseInt(newKey.substring(6, 8));
            if (key.length() == 11) {
                knightMoveNumber[4] = Integer.parseInt(checkThreeDigitValue(newKey.substring(8, 11)));
            } else {
                knightMoveNumber[4] = Integer.parseInt(newKey.substring(8, 10));
            }
        } else if (key.length() == 12) {
            String newKey = key;


            knightMoveNumber[0] = Integer.parseInt(newKey.substring(0, 2));
            knightMoveNumber[1] = Integer.parseInt(newKey.substring(2, 4));
            knightMoveNumber[2] = Integer.parseInt(newKey.substring(4, 6));
            knightMoveNumber[3] = Integer.parseInt(checkThreeDigitValue(newKey.substring(6, 9)));


            knightMoveNumber[4] = Integer.parseInt(checkThreeDigitValue(newKey.substring(9, 12)));


        } else if (key.length() == 13) {

            String newKey = key;

            knightMoveNumber[0] = Integer.parseInt(newKey.substring(0, 2));
            knightMoveNumber[1] = Integer.parseInt(newKey.substring(2, 4));
            knightMoveNumber[2] = Integer.parseInt(checkThreeDigitValue(newKey.substring(4, 7)));
            knightMoveNumber[3] = Integer.parseInt(checkThreeDigitValue(newKey.substring(7, 10)));
            knightMoveNumber[4] = Integer.parseInt(checkThreeDigitValue(newKey.substring(10, 13)));
        } else if (key.length() == 14) {

            String newKey = key;

            knightMoveNumber[0] = Integer.parseInt(newKey.substring(0, 2));
            knightMoveNumber[1] = Integer.parseInt(checkThreeDigitValue(newKey.substring(2, 5)));
            knightMoveNumber[2] = Integer.parseInt(checkThreeDigitValue(newKey.substring(5, 8)));
            knightMoveNumber[3] = Integer.parseInt(checkThreeDigitValue(newKey.substring(8, 11)));
            knightMoveNumber[4] = Integer.parseInt(checkThreeDigitValue(newKey.substring(11, 14)));
        } else if (key.length() == 15) {

            String newKey = key;

            knightMoveNumber[0] = Integer.parseInt(checkThreeDigitValue(newKey.substring(0, 3)));
            knightMoveNumber[1] = Integer.parseInt(checkThreeDigitValue(newKey.substring(3, 6)));
            knightMoveNumber[2] = Integer.parseInt(checkThreeDigitValue(newKey.substring(6, 9)));
            knightMoveNumber[3] = Integer.parseInt(checkThreeDigitValue(newKey.substring(9, 12)));
            knightMoveNumber[4] = Integer.parseInt(checkThreeDigitValue(newKey.substring(12, 15)));
        }
        HashMap<Integer, Integer> map = new HashMap<>();

        removeDuplicateEntry(map);
//        Set<Map.Entry<Integer, Integer>> entires = map.entrySet();
//        for (Map.Entry<Integer, Integer> ent : entires) {
//
//            Log.d("4444444&&&" ,""+ ent.getKey() + " ==> " + ent.getValue());
//        }
        moveNumberList = new ArrayList<int[]>();
        moveStringList = new ArrayList<char[]>();

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
        for (int index = 0; index < moveNumberList.size(); index++) {
            char tempArr[] = new char[(KNIGHT_MATRIX_WIDTH * KNIGHT_MATRIX_HEIGHT)];
            System.out.print(knightMoveNumber[index] + "   M" + (index + 1) + "  --  ");
            for (int i = 0; i < moveNumberList.get(index).length; i++) {
                tempArr[i] = AllCharacter.getInstance().getStandardCharacterSet()[moveNumberList.get(index)[i]];
                System.out.print(AllCharacter.getInstance().getStandardCharacterSet()[moveNumberList.get(index)[i]] + ",");
            }

            System.out.println();
            moveStringList.add(tempArr);
            tempArr = null;
        }


        char[] messageArray = msg.toCharArray();
        int[] messageIndex = new int[messageArray.length];

        System.out.print("Password   --  ");
        for (int i = 0; i < messageArray.length; i++) {
            for (int j = 0; j < AllCharacter.getInstance().getStandardCharacterSet().length; j++) {
                if (messageArray[i] == AllCharacter.getInstance().getStandardCharacterSet()[j]) {
                    messageIndex[i] = j;
                    System.out.print(j + ",");
                }
            }
        }

        System.out.println();
        System.out.println("printing new message");
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < messageIndex.length; i++) {

            System.out.print(moveStringList.get(0)[messageIndex[i]] + ",");
            buffer.append(moveStringList.get(0)[messageIndex[i]]);
        }

        for (int i = 0; i < knightMoveNumber.length; i++)
            buffer.append(AllCharacter.getInstance().getCharacterReverseArray()[knightMoveNumber[i]]);

        buffer.append(AllCharacter.getInstance().getCharacterReverseArray()[knightMoveNumber.length]);
        System.out.println();
        System.out.println(" new Generated Message ::::: " + buffer.toString());

        // decryptSms(buffer.toString());
        return buffer.toString();
    }

    private void removeDuplicateEntry(HashMap<Integer, Integer> map) {
        for (int j = 0; j < knightMoveNumber.length; j++) {
            if (map.containsKey(knightMoveNumber[j])) {
                if (knightMoveNumber[j] > 255) {
                    knightMoveNumber[j] = knightMoveNumber[j] - 1;
                } else {
                    knightMoveNumber[j] = knightMoveNumber[j] + 1;
                }

                j--;
            } else {
                map.put(knightMoveNumber[j], knightMoveNumber[j]);
            }
        }
    }

    private String checkThreeDigitValue(String val) {

        String finalString;
        if (Integer.parseInt(val) >= 255) {
            finalString = val.substring(1, val.length());
            finalString = "1" + finalString;
        } else {
            finalString = val;
        }
//        if (Integer.parseInt("" + val.charAt(0)) > 1) {
//            finalString = val.substring(1, val.length());
//            finalString = "1" + finalString;
//        } else {
//            finalString = val;
//        }
        return finalString;
    }


    public String decryptSms(String message) {
        StringBuffer buffer = new StringBuffer();
        try {
            //DebugUtil.printLog(" message  " + message);
            if (moveNumberList == null) {
                moveNumberList = new ArrayList<int[]>();
                moveStringList = new ArrayList<char[]>();
            }
            moveNumberList.clear();
            moveStringList.clear();
            int moveIndex = -1;
            for (int i = 0; i < AllCharacter.getInstance().getCharacterReverseArray().length; i++) {
                if (message.charAt(message.length() - 1) == AllCharacter.getInstance().getCharacterReverseArray()[i]) {

                    moveIndex = i;

                    System.out.printf("got the new index " + moveIndex);

                }
            }


            String moveChar = message.substring(message.length() - (moveIndex + 1), message.length() - 1);
            System.out.println(" Encrypt Data :  " + message.substring(0, message.length() - (moveIndex + 1)));
            char chiperCharArray[] = message.substring(0, message.length() - (moveIndex + 1)).toCharArray();

            System.out.println(" Move Character :::  " + moveChar);
            char moveArray[] = moveChar.toCharArray();
            int moveIndexArray[] = new int[moveArray.length];
            for (int j = 0; j < moveArray.length; j++) {
                for (int i = 0; i < AllCharacter.getInstance().getCharacterReverseArray().length; i++) {

                    if (moveArray[j] == AllCharacter.getInstance().getCharacterReverseArray()[i]) {
                        System.out.print(i + ",");
                        moveIndexArray[j] = i;
                        break;
                    }
                }
            }
            System.out.println();

            // Generating knight move using random number
            for (int i = 0; i < moveIndexArray.length; i++) {
                KnightSolver solver = new KnightSolver(KNIGHT_MATRIX_WIDTH, KNIGHT_MATRIX_HEIGHT);
                solver.setPosition(moveIndexArray[i] % KNIGHT_MATRIX_WIDTH, moveIndexArray[i] / KNIGHT_MATRIX_HEIGHT);
                int temp[] = solver.loopSolver();
                moveNumberList.add(temp);
                solver.reset();
                solver = null;
            }


            // Creating new character array using knight moves
            for (int index = 0; index < moveNumberList.size(); index++) {
                char tempArr[] = new char[(KNIGHT_MATRIX_WIDTH * KNIGHT_MATRIX_HEIGHT)];
                System.out.print("M" + (index + 1) + "  --  ");
                for (int i = 0; i < moveNumberList.get(index).length; i++) {
                    tempArr[i] = AllCharacter.getInstance().getStandardCharacterSet()[moveNumberList.get(index)[i]];
                    System.out.print("&&&!" + AllCharacter.getInstance().getStandardCharacterSet()[moveNumberList.get(index)[i]] + ",");
                }

                System.out.println();
                moveStringList.add(tempArr);
                tempArr = null;
            }


            System.out.println();
            int chiperValueArray[] = new int[chiperCharArray.length];
            for (int i = 0; i < chiperCharArray.length; i++) {
                for (int j = 0; j < moveStringList.get(0).length; j++) {
                    if (chiperCharArray[i] == moveStringList.get(0)[j]) {
                        chiperValueArray[i] = j;
                        System.out.print(j + ",");
                    }
                }
            }


            System.out.println(" Decrypt value ");
            for (int i = 0; i < chiperValueArray.length; i++) {
                System.out.print(AllCharacter.getInstance().getStandardCharacterSet()[chiperValueArray[i]]);
                buffer.append(AllCharacter.getInstance().getStandardCharacterSet()[chiperValueArray[i]]);
            }
            System.out.println("");
        } catch (Exception e) {
            buffer.append(message);
        }


//        int moveIndex = message.charAt(message.length()-1);


        System.out.println(" new Generated Message ::::: 111 " + buffer.toString());
        return buffer.toString();
    }


    public static ArrayList<String> permutation(String s) {
        // The result
        ArrayList<String> res = new ArrayList<String>();
        // If input string's length is 1, return {s}
        if (s.length() == 1) {
            Log.d("#####", "" + res.size());
            res.add(s);
        } else if (s.length() > 1) {
            int lastIndex = s.length() - 1;
            // Find out the last character
            String last = s.substring(lastIndex);
            // Rest of the string
            String rest = s.substring(0, lastIndex);
            // Perform permutation on the rest string and
            // merge with the last character
            if (res.size() != 1000) {
                // Log.d("&*&*",""+res.size());
                res = merge(permutation(rest), last);
            }
        }
        return res;
    }


    public static ArrayList<String> merge(ArrayList<String> list, String c) {
        ArrayList<String> res = new ArrayList<>();
        // Loop through all the string in the list
        for (String s : list) {
            // For each string, insert the last character to all possible positions
            // and add them to the new list
            for (int i = 0; i <= s.length(); ++i) {
                String ps = new StringBuffer(s).insert(i, c).toString();
                Log.d("#####55", "" + res.size());
                res.add(ps);

                if (res.size() > 1000) {
                    Log.d("#####66", "" + res.size());
                    return res;
                }
            }
        }
        //  Log.d("#####44",""+res.size());
        if (res.size() > 1000) {
            return res;
        }
        return res;
    }
// generating the random number for Knight Moves

    public String getEncryptedFile(String fileString) {

// generating the random number for Knight Moves
        String number = "";

        for (int j = 0; j < 5; j++) {
            Random random = new Random();
            int randomNum = random.nextInt(255);

            Log.d("##@@@init", "" + randomNum);
            if (randomNum < 10) {
                randomNum = (randomNum + 10);
            }
            number = number + "" + randomNum;

            Log.d("##@@@FinalNumber", number);


        }

        String text = getEncryptSms(fileString, number);
        return text;

    }

}
