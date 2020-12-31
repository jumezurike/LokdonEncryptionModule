package com.lokdonencryption.lokdonencrypt;

import android.util.Log;

import com.amazonaws.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;


/**
 *
 * @author LokDon
 */
class CipherControl {

    private int MPIN_LENGTH = 4;
    private int MAX_MPIN_TOUR = 2;
    private int MAX_MOVES = 256;
    private int MAX_KNIGHT_TOUR = 5;
    private int ST_SIZE = 256;
    private int KNIGHT_MATRIX_WIDTH = 16;
    private int KNIGHT_MATRIX_HEIGHT = 16;
    private int MAX_MPIN_TRANS_TOUR = 3;

    private ArrayList<char[]> cipherTemplateList = null;
    private char[] singleCipherTemplate;
    public static CipherControl instance = null;
    private int[] knightMoveNumbers;

    private ArrayList<int[]> knightWalkNumberList = null;

    CipherControl() {
    }
    boolean verifyKey(String key,String userKey){
        if(decryptPassword(userKey).equals(key)){
            return true;
        }
        return false;
    }

    /*
    create knight move numbers and store in integer array.
    This helps to run knight from a specified random location on chess board
     */
    private int[] generateKnightMovePositions(int moves) {
        this.MAX_KNIGHT_TOUR = moves;
        knightMoveNumbers = new int[this.MAX_KNIGHT_TOUR];
        for (int i = 0; i < knightMoveNumbers.length; i++) {
            knightMoveNumbers[i] = new Random().nextInt((this.KNIGHT_MATRIX_WIDTH * this.KNIGHT_MATRIX_HEIGHT) - 1);
            System.out.println("Night Moves:" + knightMoveNumbers[i]);
        }
        return knightMoveNumbers;
    }

    public File encryptFile(InputStream fis, String key, String filePath) throws Exception {
        //FileInputStream fis=new FileInputStream(file);
        byte[] fbyte = IOUtils.toByteArray(fis);
        byte[] encryptedByte = xorWithKey(fbyte, key.getBytes());

        File outFile = new File(filePath);
        try {
            if (!outFile.exists()) {
                outFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(outFile);
            fos.write(encryptedByte);
            fos.close();
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }
        return outFile;
    }

    public File decryptFile(InputStream fis, String key, String path) throws Exception {
        //FileInputStream fis=new FileInputStream(file);
        byte[] fbyte = IOUtils.toByteArray(fis);
        byte[] decryptedByte = xorWithKey(fbyte, key.getBytes());
        //String fileName=file.getAbsolutePath();

        File outFile = new File(path);
        try {
            if (!outFile.exists()) {
                outFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(outFile);
            fos.write(decryptedByte);
            fos.close();
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }
        return outFile;
    }

    /* singleton class objects are permissible only */
    public static CipherControl getInstance() {
        if (instance == null) {
            instance = new CipherControl();
        }
        return instance;
    }

    /*
    print a single cipher template that is an array of wide chars, may be standard template
    or may be a custom template after a knight's move
     */
    private void printCipherTemplate(char[] cipherChars) {
        int starter = 0;
        System.out.println("My Cipher Template is As Follows:");
        for (int i = 0; i < this.MAX_MOVES; i++) {
            System.out.print(cipherChars[i]);
            starter++;
            if (starter == this.KNIGHT_MATRIX_WIDTH) {
                System.out.println("");
                starter = 0;
            } else {
                System.out.print("\t");
            }
        }
    }

    /*
    print knight moves that is an array of integers from knight walk , starting from a location stated
    in array of knightMoveNumbers;
     */
    private void printKnightMoves(int[] knightMoves) {
        int starter = 0;
        System.out.println("Knight Moves are as follows :");
        for (int i = 0; i < this.MAX_MOVES; i++) {
            System.out.print(knightMoves[i]);
            starter++;
            if (starter == this.KNIGHT_MATRIX_WIDTH) {
                System.out.println("");
                starter = 0;
            } else {
                System.out.print("\t");
            }
        }
    }

    /*
    create a custom cipher template with given walk points of a knight, fetch chars from standard
    template.
     */
    private char[] createCipherTemplate(int[] knightTourMatrix) {
        char tempArr[] = new char[(this.KNIGHT_MATRIX_WIDTH * this.KNIGHT_MATRIX_HEIGHT)];
        for (int i = 0; i < knightTourMatrix.length; i++) {
            // find the value of knight moves starting from first index
            int indexValue = knightTourMatrix[i];
            // fetch the data present in ST on this indexValue and store in cipherTemplate
            tempArr[i] = AllCharacter.getInstance().getStandardCharacterSet()[indexValue];
        }
        return tempArr;
    }

    /*
    we must first call private fucntion generateKnightMovePositions() with expected number of moves, then
    we can use this function to create an array list holding array of numbers that represent a knight's
    walk on board.
    knightWalkNumberList[0] = {0...255}
    knightWalkNumberList[1] = {0...255}
    ...
    knightWalkNumberList[n] = {0...255}
     */
    private ArrayList<int[]> createKnightWalkNumberStore() {
        this.knightWalkNumberList = new ArrayList<int[]>();
        // Generating knight move using random number
        for (int i = 0; i < this.knightMoveNumbers.length; i++) {
            KnightSolver solver = new KnightSolver(this.KNIGHT_MATRIX_WIDTH, this.KNIGHT_MATRIX_HEIGHT);
            solver.setPosition(this.knightMoveNumbers[i] % this.KNIGHT_MATRIX_WIDTH, this.knightMoveNumbers[i] / KNIGHT_MATRIX_HEIGHT);
            int temp[] = solver.loopSolver();
            // we have now solver object with int array of 256 possible moves of knight, print and store it.
            this.printKnightMoves(temp);
            this.knightWalkNumberList.add(temp);
            solver.reset();
            solver = null;
        }
        return this.knightWalkNumberList;
    }

    /*
    create an array list of wide char arrays representing cipher chars. Each time we run a knight, we store
    walk path number and then create a cipher array of chars.
    cipherTemplateList[0] = char array {a,b,c...d}
    cipherTemplateList[1] = char array {w,z,,c,..z}
    ...
    cipherTemplateList[n] = char array { #,%,6,7,*}
     */
    private ArrayList<char[]> createCipherTemplateStore(char[] singleCipherTemplateArray) {
        if (this.cipherTemplateList == null) {
            this.cipherTemplateList = new ArrayList<char[]>();
        }
        this.cipherTemplateList.add(singleCipherTemplateArray);
        return this.cipherTemplateList;
    }

    /*
    Encrypts entered keys using lokdon Algo, total number of moves (default 5 moves)
     */
    public String generatePassword(String strVal, int totalMoves) {
        System.out.println("Password Key=" + strVal);
        char[] key = strVal.toCharArray();
        // create a random number list of knight moves and initialize knightMoveNumbers array.
        this.generateKnightMovePositions(totalMoves);

        // now initialize array list of walks and store in knightWalkNumberList
        this.createKnightWalkNumberStore();
        // Creating new character array using knight moves and store in ciperTemplateStore
        System.out.println("Total Knight Walk = " + this.knightWalkNumberList.size());
        if (this.cipherTemplateList != null) {
            this.cipherTemplateList.clear();
        }
        for (int index = 0; index < this.knightWalkNumberList.size(); index++) {
            char[] currentCipherChars = this.createCipherTemplate(this.knightWalkNumberList.get(index));
            //this.printCipherTemplate(currentCipherChars);
            this.createCipherTemplateStore(currentCipherChars);
        }
        /*
        Till now we have
        1. Array of knight moves
        2. Array list of ciper arrays
        3. Array list of knight's walk
         */
        // original password store
        char originalPassword[] = new char[key.length];
        originalPassword = key;
        // place holders
        char silentPassword[] = new char[key.length];
        int originalPassIndex[] = new int[key.length];
        int silentPassIndex[] = new int[key.length];
        System.out.println("Cipher Template List Size=" + this.cipherTemplateList.size());
        // find the indices of our input password chars in M1 to M5 and encrypt
        for (int index = 0; index < this.cipherTemplateList.size(); index++) {
            System.out.print(" orignal index -");
            for (int i = 0; i < originalPassword.length; i++) {
                for (int j = 0; j < this.cipherTemplateList.get(index).length; j++) {
                    if (originalPassword[i] == this.cipherTemplateList.get(index)[j]) {
                        originalPassIndex[i] = j;
                        System.out.print(originalPassIndex[i] + " , ");
                    }
                }
            }
            System.out.println();
            /*
            Now we have to add silent password chars from M1 starting from backward postion of M1 i.e. 255.254 and so on up to end of length of password
            collect as many as chars from M1 from end of cipher matrix , but not more than length of password
             */

            // For M1 , initialize silentSupplier vector
            int pos = 0;
            if (index == 0) {
                int travelling = this.ST_SIZE - key.length;
                for (int i = this.cipherTemplateList.get(index).length - 1; i >= travelling; i--) {
                    silentPassword[pos] = this.cipherTemplateList.get(index)[i];
                    silentPassIndex[pos] = i;
                    pos++;
                }
            } else {
                // For
                //M2 and so on , find the index of silentSuplier chars and add to cipher Index in M2 to M5
                for (int i = 0; i < silentPassword.length; i++) {
                    for (int j = 0; j < this.cipherTemplateList.get(index).length; j++) {
                        if (silentPassword[i] == this.cipherTemplateList.get(index)[j]) {
                            silentPassIndex[pos] = j;
                            pos++;
                        }
                    }
                }
            }

            System.out.println();
            // generate cipher password after adding the index of silentSupplier chars from M1
            int newAddedValue[] = new int[key.length];

            for (int i = 0; i < silentPassIndex.length; i++) {
                int value = silentPassIndex[i] + originalPassIndex[i];
                System.out.println("Adding " + silentPassIndex[i] + " And " + originalPassIndex[i] + " = " + value);
                if (value > this.ST_SIZE - 1) {
                    newAddedValue[i] = value - this.ST_SIZE;
                } else {
                    newAddedValue[i] = value;
                }
            }

            System.out.println();

            char newAddedValueTemp[] = new char[newAddedValue.length];
            for (int i = 0; i < newAddedValue.length; i++) {
                newAddedValueTemp[i] = this.cipherTemplateList.get(index)[newAddedValue[i]];
            }
            System.out.println();
            // override oldPassword
            originalPassword = newAddedValueTemp;
        }

        System.out.println();

        System.out.print("Silent Pass - ");

        for (int i = 0; i < silentPassword.length; i++) {
            System.out.print(silentPassword[i]);
        }

        System.out.println();

        StringBuffer buffer = new StringBuffer();
        buffer.append(originalPassword);
        buffer.append(silentPassword);
        System.out.println();
        System.out.print("Knight move - ");
        for (int i = 0; i < knightMoveNumbers.length; i++) {
            buffer.append(AllCharacter.getInstance().getCharacterReverseArray()[knightMoveNumbers[i]]);
            System.out.print(knightMoveNumbers[i] + ",");
        }
        buffer.append(AllCharacter.getInstance().getCharacterReverseArray()[knightMoveNumbers.length]);
        System.out.println();
        System.out.println("Final Password:" + buffer.toString());
        return buffer.toString();
    }

    /* getPasswordComponents Function breaks the encrypted password and return linear strings of encrypted
    password, encrypted silent passord and encrypted knight moves
    */
    private String[] getPasswordComponents(String encryptedPassword) {
        String[] passwordArray = new String[3];
        // Since there are three elements in above array, one will hold password chars, 2nd for silent pass
        // and 3rd one for knight moves.
        for (int i = 0; i < passwordArray.length; i++) {
            passwordArray[i] = new String();
        }
        System.out.println("Knight Move Character " + encryptedPassword.charAt(encryptedPassword.length() - 1));
        int move = 0;
        for (int i = 0; i < AllCharacter.getInstance().getCharacterReverseArray().length; i++) {
            if (AllCharacter.getInstance().getCharacterReverseArray()[i] == encryptedPassword.charAt(encryptedPassword.length() - 1)) {
                System.out.println("Total Moves =" + i);
                move = i;
            }
        }
        int start = (encryptedPassword.length()) - (move + 1);
        int end = encryptedPassword.length() - 1;
        passwordArray[0] = encryptedPassword.substring(start, end); // knight moves array
        passwordArray[1] = encryptedPassword.substring(0, (start + 1) / 2); // password array
        passwordArray[2] = encryptedPassword.substring((start + 1) / 2, start); // silent pass
        return passwordArray;
    }


    /*
    Function used to break down encrypted mpin and return linear strings of encrypted password and
    encrypted knight moves
    */
    private String[] getSecurityPinComponents(String encryptedPin) {
        System.out.println("Received encrypted MPIN =" + encryptedPin);
        String[] mpinArray = new String[2];

        for (int i = 0; i < mpinArray.length; i++) {
            mpinArray[i] = new String();
        }
        // last 2 chars are knight moves
        mpinArray[0] = encryptedPin.substring(encryptedPin.length() - 2); // knight moves array
        mpinArray[1] = encryptedPin.substring(0, encryptedPin.length() - 2); // password array
        return mpinArray;
    }

    /*
    Decrypt original encrypted password and reutrn string readble password
    */

    public String decryptPassword(String pass) {
        String passwordBuffer[] = getPasswordComponents(pass);
        ArrayList<int[]> knightMovesNumberList = new ArrayList<int[]>();
        ArrayList<char[]> knightMovesStringList = new ArrayList<char[]>();
        ArrayList<int[]> ecryptKeyValue = new ArrayList<int[]>();
        System.out.println("password Component A(Knight Moves)=" + passwordBuffer[0]);
        System.out.println("password Component B(Password Array)=" + passwordBuffer[1]);
        System.out.println("password Component C(Silent Password)=" + passwordBuffer[2]);
        System.out.println();
        int decKnighMove[] = new int[passwordBuffer[0].length()];
        char moveChar[] = new char[passwordBuffer[0].length()];
        //append knight moves char
        moveChar = passwordBuffer[0].toCharArray();
        System.out.println();
        System.out.print("Dec Knight Moves = ");
        for (int i = 0; i < decKnighMove.length; i++) {
            for (int j = 0; j < AllCharacter.getInstance().getCharacterReverseArray().length; j++) {
                if (moveChar[i] == AllCharacter.getInstance().getCharacterReverseArray()[j]) {
                    decKnighMove[i] = j;
                    System.out.print(j + ",");
                }
            }
        }
        System.out.println();
        // Generating knight move using random number
        for (int i = 0; i < decKnighMove.length; i++) {
            KnightSolver solver = new KnightSolver(KNIGHT_MATRIX_WIDTH, KNIGHT_MATRIX_HEIGHT);
            solver.setPosition(decKnighMove[i] % KNIGHT_MATRIX_WIDTH, decKnighMove[i] / KNIGHT_MATRIX_HEIGHT);
            int temp[] = solver.loopSolver();
            knightMovesNumberList.add(temp);
            solver.reset();
            solver = null;
        }

        // Creating new character array using knight moves
        for (int index = 0; index < knightMovesNumberList.size(); index++) {
            char tempArr[] = new char[(KNIGHT_MATRIX_WIDTH * KNIGHT_MATRIX_HEIGHT)];
            System.out.print("M" + (index + 1) + "  --  ");
            for (int i = 0; i < knightMovesNumberList.get(index).length; i++) {
                tempArr[i] = AllCharacter.getInstance().getStandardCharacterSet()[knightMovesNumberList.get(index)[i]];
                //System.out.print(AllCharacter.getInstance().getStandardCharacterSet()[knightMovesNumberList.get(index)[i]]+",");
            }

            System.out.println();
            knightMovesStringList.add(tempArr);
            tempArr = null;
        }
        char orignalPassword[] = new char[passwordBuffer[1].length()];
        orignalPassword = passwordBuffer[1].toCharArray();
        char silentPassword[] = new char[passwordBuffer[1].length()];
        silentPassword = passwordBuffer[2].toCharArray();
        int orignalPassIndex[] = new int[passwordBuffer[1].length()];
        int silentPassIndex[] = new int[passwordBuffer[1].length()];

        for (int index = knightMovesStringList.size() - 1; index >= 0; index--) {
            for (int i = 0; i < orignalPassword.length; i++) {
                //System.out.print(index+" pass index ");
                for (int j = 0; j < knightMovesStringList.get(index).length; j++) {
                    if (orignalPassword[i] == knightMovesStringList.get(index)[j]) {
                        orignalPassIndex[i] = j;
                        //System.out.print(j+",");
                    }
                }
            }
            System.out.println();

            for (int i = 0; i < silentPassword.length; i++) {
                ///System.out.print(index+" silent pass index ");
                for (int j = 0; j < knightMovesStringList.get(index).length; j++) {
                    if (silentPassword[i] == knightMovesStringList.get(index)[j]) {
                        silentPassIndex[i] = j;
                        // System.out.print(j+",");
                    }
                }
            }
            System.out.println();
            System.out.println("value = ");
            int newAddedValue[] = new int[orignalPassIndex.length];
            for (int i = 0; i < orignalPassIndex.length; i++) {
                int value = orignalPassIndex[i] + ST_SIZE;
                if (value > ST_SIZE - 1) {
                    int temp = value - silentPassIndex[i];
                    if (temp > ST_SIZE - 1) {
                        temp = temp - ST_SIZE;
                    }
                    newAddedValue[i] = temp;
                } else {
                    newAddedValue[i] = value;
                }
                System.out.print(newAddedValue[i] + ",");
            }
            System.out.println();
            char newAddedValueTemp[] = new char[newAddedValue.length];
            for (int i = 0; i < newAddedValue.length; i++) {
                newAddedValueTemp[i] = knightMovesStringList.get(index)[newAddedValue[i]];
            }
            System.out.println();
            orignalPassword = newAddedValueTemp;
        }

        StringBuffer buff = new StringBuffer();
        buff.append(orignalPassword);
        System.out.println("Original Password=" + buff.toString());
        return buff.toString();
    }

    /*
    find if a value exists in an array
    */
    public int getArrayIndexValue(int[] arr, int value) {
        int k = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == value) {
                k = value;
                break;
            }

        }
        return k;
    }

    /*
    Encrypt MPIN for rasbita app
    */
    public String encryptSecurityPin(String mpin) {
        int[] leftBorder = new int[]{0, 16, 32, 48, 64, 80, 96, 112, 128, 144, 160, 176, 192, 208, 224, 240};
        int[] rightBorder = new int[]{15, 31, 47, 63, 79, 95, 111, 127, 143, 159, 175, 191, 207, 223, 239, 255};
        char[] mpinArray = mpin.toCharArray();

        //create random number list of number of moves of night
        this.generateKnightMovePositions(MAX_MPIN_TOUR);
        // now initialize array list of walks and store in knightWalkNumberList
        this.createKnightWalkNumberStore();
        System.out.println("Total Knight Walks for PIN = " + this.knightWalkNumberList.size());
        if (this.cipherTemplateList != null) {
            this.cipherTemplateList.clear();
        }

        // Creating new character array using knight moves and store in ciperTemplateStore
        for (int index = 0; index < this.knightWalkNumberList.size(); index++) {
            char[] currentCipherChars = this.createCipherTemplate(this.knightWalkNumberList.get(index));
            this.printCipherTemplate(currentCipherChars);
            this.createCipherTemplateStore(currentCipherChars);
        }
        /*
        we have now
        1. Knight move positions Array
        2. Array list of cipher chars based on knight moves
        3. Knight moves array list, based on knight move positions.
        */
        // find the indices of our input PIN Digits in M1 and M2 and store in an array

        int originalPinIndex[] = new int[mpinArray.length];
        // place holders
        StringBuffer cipherBuffer = new StringBuffer();

        for (int index = 0; index < this.cipherTemplateList.size(); index++) {
            System.out.print(" in M1 orignal index of PIN is as : ");
            for (int i = 0; i < mpinArray.length; i++) {
                for (int j = 0; j < this.cipherTemplateList.get(index).length; j++) {
                    //System.out.println("Comparing " +mpinArray[i]+", "+ this.cipherTemplateList.get(index)[j]) );
                    if (mpinArray[i] == this.cipherTemplateList.get(index)[j]) {
                        originalPinIndex[i] = j;
                        System.out.print(originalPinIndex[i] + " , ");
                    }
                }
            }
            System.out.println();

            // we need to find out positions of mpin key in M2 only
            if (index == 1) {
                // check if we are in situation of boundaries in left or right array
                int pinPosition = 0;
                for (int mpos = 0; mpos < originalPinIndex.length; mpos++) {
                    System.out.println("searching for " + originalPinIndex[mpos] + " in left Array");
                    pinPosition = this.getArrayIndexValue(leftBorder, originalPinIndex[mpos]);
                    if (pinPosition > -1) { // if index found
                        System.out.println("Match Found in Left Array index number:" + pinPosition);
                        // we got a mpin position in left array boundry
                        // 2 chars from left of the pinPosition from M2 should be taken as cipher char
                        cipherBuffer.append(this.cipherTemplateList.get(index)[pinPosition + 1]);
                        cipherBuffer.append(this.cipherTemplateList.get(index)[pinPosition + 2]);
                    } else {
                        //
                        System.out.println("searching for " + originalPinIndex[mpos] + "in RIGHT Array");
                        pinPosition = this.getArrayIndexValue(rightBorder, originalPinIndex[mpos]);
                        if (pinPosition > -1) { // if index  found
                            System.out.println("Match Found in Right Array index number:" + pinPosition);
                            // 2 chars from right of the pinPosition from M2 should be taken as cipher char
                            cipherBuffer.append(this.cipherTemplateList.get(index)[pinPosition - 1]);
                            cipherBuffer.append(this.cipherTemplateList.get(index)[pinPosition - 2]);
                        } else {
                            // we dit not find the index as a value in either left or right array
                            // just take one chr from left and one char from right
                            cipherBuffer.append(this.cipherTemplateList.get(index)[originalPinIndex[mpos] - 1]);
                            cipherBuffer.append(this.cipherTemplateList.get(index)[originalPinIndex[mpos] + 1]);
                        }
                    }
                }
            }
        }
        System.out.println("Encrypted PIN=" + cipherBuffer);
        // add knights tour as chars from reverse ST
        for (int i = 0; i < this.knightMoveNumbers.length; i++) {
            cipherBuffer.append(AllCharacter.getInstance().getCharacterReverseArray()[this.knightMoveNumbers[i]]);
            System.out.println(this.knightMoveNumbers[i] + ",");
        }
        System.out.println("Final Encrypted PIN=" + cipherBuffer);
        return cipherBuffer.toString();
    }

    /*
    Function to decrypt MPIN for rasbita
    */
    public String decryptSecurityPin(String mpinEncrypt) {
        //we have mpin encrypt with last 2 characters respresenting knight's move from reverse standard template
        // last two chars denotes the number of knight's move from reverse ST

        // find mpin components
        ArrayList<int[]> mpinKnightMovesNumberList = new ArrayList<int[]>();
        ArrayList<char[]> mpinKnightMovesStringList = new ArrayList<char[]>();
        StringBuffer returnBuffer = new StringBuffer();

        String mpinBuffer[] = getSecurityPinComponents(mpinEncrypt);
        System.out.println("Knight Moves For MPIN=" + mpinBuffer[0]);
        System.out.println("MPIN Password Array=" + mpinBuffer[1]);

        // we have knight moves and mpin encrypt in mpin buffer
        // find the knight moves from reverse standard template
        int mpinKnightMoves[] = new int[mpinBuffer[0].length()];
        char mpinKnightChars[] = new char[mpinBuffer[0].length()];
        //append knight moves char
        mpinKnightChars = mpinBuffer[0].toCharArray();
        System.out.println();
        System.out.print("Dec Knight Moves = ");
        for (int i = 0; i < mpinKnightMoves.length; i++) {
            for (int j = 0; j < AllCharacter.getInstance().getCharacterReverseArray().length; j++) {
                if (mpinKnightChars[i] == AllCharacter.getInstance().getCharacterReverseArray()[j]) {
                    mpinKnightMoves[i] = j;
                    System.out.print(j + ",");
                }
            }
        }
        System.out.println("mpin knight moves" + mpinKnightMoves[0] + "," + mpinKnightMoves[1]);

        // we had encrypted mpin on M2 template , iterate over m1 and m2
        // Generating knight move using given knight move numbers
        for (int i = 0; i < mpinKnightMoves.length; i++) {
            KnightSolver solver = new KnightSolver(KNIGHT_MATRIX_WIDTH, KNIGHT_MATRIX_HEIGHT);
            solver.setPosition(mpinKnightMoves[i] % KNIGHT_MATRIX_WIDTH, mpinKnightMoves[i] / KNIGHT_MATRIX_HEIGHT);
            int temp[] = solver.loopSolver();
            mpinKnightMovesNumberList.add(temp);
            solver.reset();
            solver = null;
        }

        // Creating m1 and m2 array using knight moves
        for (int index = 0; index < mpinKnightMovesNumberList.size(); index++) {
            char tempArr[] = new char[(KNIGHT_MATRIX_WIDTH * KNIGHT_MATRIX_HEIGHT)];
            System.out.print("M" + (index + 1) + "  --  ");
            for (int i = 0; i < mpinKnightMovesNumberList.get(index).length; i++) {
                tempArr[i] = AllCharacter.getInstance().getStandardCharacterSet()[mpinKnightMovesNumberList.get(index)[i]];
                //System.out.print(AllCharacter.getInstance().getStandardCharacterSet()[knightMovesNumberList.get(index)[i]]+",");
            }

            System.out.println();
            mpinKnightMovesStringList.add(tempArr);
            tempArr = null;
        }

        //now we have full matrix of M1 and M2 above
        char originalSecurityPin[] = new char[mpinBuffer[1].length()];
        originalSecurityPin = mpinBuffer[1].toCharArray();
        int originalPinCharIndex[] = new int[mpinBuffer[1].length()];
        System.out.println("Length of mpinKnightMovesStringList=" + mpinKnightMovesStringList.size());

        /* Make sure we are checking MPIN encrypted chars only in M2 */
        /* Logic to decrypt MPIN encrypt, below
        1. take two chars from last each time until password is there,
        2. find their positions in M2, Find the differnce of postions of the chars
        3. if position 1 < postion 2, its a left boundary case, take char at Position + 1
        4. if position 1 > postion 2, its a right boundary case or left-right condition
        4.a if position 1 - positions == 2 , left right condition achieved, char = (position1 + position2) / 2
        4.b else its right position , char = postion 1-1
        */
        System.out.println("mpin 1 = " + mpinBuffer[1]);


        String lastTwoChars;

        int[] positions = new int[2];
        for (int index = mpinKnightMovesStringList.size() - 1; index >= 0; index--) {
            // for M2
            if (index == 1) {
                boolean shouldContinue = true;
                while (shouldContinue) {
                    // iterate over string and take 2 chars from alst each time, as we have 8 chars mpin
                    lastTwoChars = mpinBuffer[index].substring(mpinBuffer[index].length() - 2);
                    System.out.println("Last Two Chars = " + lastTwoChars);
                    char[] twoCharsFromMpin = new char[lastTwoChars.length()];
                    twoCharsFromMpin = lastTwoChars.toCharArray();
                    //start from last character
                    System.out.println("Last Two Chars length= " + lastTwoChars.length());
                    for (int i = lastTwoChars.length() - 1; i >= 0; i--) {
                        for (int j = 0; j < mpinKnightMovesStringList.get(index).length; j++) {
                            //System.out.println("I = "+i+",j="+j);
                            if (twoCharsFromMpin[i] == mpinKnightMovesStringList.get(index)[j]) {
                                positions[i] = j;
                                //System.out.print("Positions = "+j+",");
                                break;
                            }
                        }
                    }
                    System.out.println("Last Char Position=" + positions[1]);
                    System.out.println("Second Last Char Position=" + positions[0]);

                    if (positions[1] < positions[0]) {
                        System.out.println("Left Pos Char would be" + mpinKnightMovesStringList.get(index)[positions[0] + 1]);
                        returnBuffer.append(mpinKnightMovesStringList.get(index)[positions[0] + 1]);
                    } else if (positions[1] > positions[0]) {
                        if (positions[1] - positions[0] == 2) {
                            System.out.println("Mid Char would be" + mpinKnightMovesStringList.get(index)[(positions[0] + positions[1]) / 2]);
                            returnBuffer.append(mpinKnightMovesStringList.get(index)[(positions[0] + positions[1]) / 2]);
                        } else {
                            System.out.println("Right Char would be" + mpinKnightMovesStringList.get(index)[positions[0] + 1]);
                            returnBuffer.append(mpinKnightMovesStringList.get(index)[positions[0] - 1]);
                        }
                    }
                    mpinBuffer[1] = mpinBuffer[1].substring(0, mpinBuffer[1].length() - 2);
                    shouldContinue = true;
                    System.out.println("Buffer = " + mpinBuffer[1]);
                    if (mpinBuffer[1].length() <= 0) {
                        shouldContinue = false;
                    }

                }
            }
            System.out.println();
        }
        return returnBuffer.reverse().toString();
    }

    /*
    Function to encrypt generic data up to M2.
    The M2 encrypt would be posted to LokDOn User registration and edit profile
    However, Password would be encrypted upto M5.
    The function would recieve a string data and will return encoded M2 string
    */
    public String encryptGenericData(String genericString) {
        char[] genericStringToArray = genericString.toCharArray();
        //create random number list of number of moves of night
        this.generateKnightMovePositions(MAX_MPIN_TOUR); // M1 and M2 only
        // now initialize array list of walks and store in knightWalkNumberList
        this.createKnightWalkNumberStore();
        System.out.println("Total Knight Walks for Encryting Generic String = " + this.knightWalkNumberList.size());
        if (this.cipherTemplateList != null) {
            this.cipherTemplateList.clear();
        }

        // Creating new character array using knight moves and store in ciperTemplateStore
        for (int index = 0; index < this.knightWalkNumberList.size(); index++) {
            char[] genericStringCipherChars = this.createCipherTemplate(this.knightWalkNumberList.get(index));
            this.printCipherTemplate(genericStringCipherChars);
            this.createCipherTemplateStore(genericStringCipherChars);
        }
        /*
        we have now
        1. Knight move positions Array
        2. Array list of cipher chars based on knight moves
        3. Knight moves array list, based on knight move positions.
        */
        // find the indices of our input data string in M1 and M2 and store in an array
        // we will add indices of our data chars and will finally find new chars from M2 as ciper of our data

        int fixedDataCharsFromM1Index[] = new int[genericStringToArray.length];
        int originalDataCharsM2Index[] = new int[genericStringToArray.length];
        // place holders
        StringBuffer cipherBuffer = new StringBuffer();
        for (int index = 0; index < this.cipherTemplateList.size(); index++) {
            System.out.println(" in M" + index + " orignal index of generic Data chars is as : ");
            if (index == 0) {
                // take index from Knight tour matrix of M1 in decresing order 255..254..253..>
                int travelling = this.MAX_MOVES - genericStringToArray.length;
                System.out.println("Travelling backwards for " + travelling);
                int i = 0;
                for (int x = this.MAX_MOVES - 1; x >= travelling; x--) {
                    fixedDataCharsFromM1Index[i] = this.knightWalkNumberList.get(index)[x];
                    System.out.println("From M1, positions" + fixedDataCharsFromM1Index[i]);
                    i++;
                }
            }
            for (int i = 0; i < genericStringToArray.length; i++) {

                // from m2, collect data chars position
                for (int j = 0; j < this.cipherTemplateList.get(index).length; j++) {
                    if (genericStringToArray[i] == this.cipherTemplateList.get(index)[j]) {
                        if (index == 1) {
                            originalDataCharsM2Index[i] = j;
                            System.out.println("From M2, positions" + j);
                        }
                    }
                }
            }
        }
        /* Add both Indices from M1 and M2 cipher templates
           if result is more tahn 255 , sutract 255 from it that will give new index
        */
        int dataCharsFinalIndex[] = new int[genericStringToArray.length];
        for (int pos = 0; pos < genericStringToArray.length; pos++) {
            System.out.println("Adding index values" + fixedDataCharsFromM1Index[pos] + " and " + originalDataCharsM2Index[pos]);
            dataCharsFinalIndex[pos] = fixedDataCharsFromM1Index[pos] + originalDataCharsM2Index[pos];
            if (dataCharsFinalIndex[pos] >= this.ST_SIZE) {
                dataCharsFinalIndex[pos] = dataCharsFinalIndex[pos] - this.ST_SIZE;
            }
            System.out.println("Final Index = " + dataCharsFinalIndex[pos]);
            cipherBuffer.append(this.cipherTemplateList.get(1)[dataCharsFinalIndex[pos]]);
        }
        System.out.println("Encrypted PIN=" + cipherBuffer);
        // add knights tour as chars from reverse ST
        for (int i = 0; i < this.knightMoveNumbers.length; i++) {
            cipherBuffer.append(AllCharacter.getInstance().getCharacterReverseArray()[this.knightMoveNumbers[i]]);
            System.out.println(this.knightMoveNumbers[i] + ",");
        }
        System.out.println("Final Encrypted Data=" + cipherBuffer);
        return cipherBuffer.toString();
    }

    /*  function to de-compile generic Data , encode above
        we will result a String and accept a encoded value
    */
    public String decryptGenericData(String encodedData) {
        //we have mpin encrypt with last 2 characters respresenting knight's move from reverse standard template
        // last two chars denotes the number of knight's move from reverse ST

        // find mpin components
        ArrayList<int[]> dataKnightMovesNumberList = new ArrayList<int[]>();
        ArrayList<char[]> dataKnightMovesStringList = new ArrayList<char[]>();
        StringBuffer returnBuffer = new StringBuffer();

        String dataBuffer[] = getSecurityPinComponents(encodedData);
        System.out.println("Knight Moves For Generic Data=" + dataBuffer[0]);
        System.out.println("Generic Data Array=" + dataBuffer[1]);

        // we have knight moves and mpin encrypt in mpin buffer
        // find the knight moves from reverse standard template
        int dataKnightMoves[] = new int[dataBuffer[0].length()];
        char dataKnightChars[] = new char[dataBuffer[0].length()];
        //append knight moves char
        dataKnightChars = dataBuffer[0].toCharArray();
        System.out.println();
        System.out.print("Dec Knight Moves = ");
        for (int i = 0; i < dataKnightMoves.length; i++) {
            for (int j = 0; j < AllCharacter.getInstance().getCharacterReverseArray().length; j++) {
                if (dataKnightChars[i] == AllCharacter.getInstance().getCharacterReverseArray()[j]) {
                    dataKnightMoves[i] = j;
                    System.out.print(j + ",");
                }
            }
        }
        System.out.println("mpin knight moves" + dataKnightMoves[0] + "," + dataKnightMoves[1]);

        // we had encrypted mpin on M2 template , iterate over m1 and m2
        // Generating knight move using given knight move numbers

        for (int i = 0; i < dataKnightMoves.length; i++) {
            KnightSolver solver = new KnightSolver(KNIGHT_MATRIX_WIDTH, KNIGHT_MATRIX_HEIGHT);
            solver.setPosition(dataKnightMoves[i] % KNIGHT_MATRIX_WIDTH, dataKnightMoves[i] / KNIGHT_MATRIX_HEIGHT);
            int temp[] = solver.loopSolver();
            dataKnightMovesNumberList.add(temp);
            solver.reset();
            solver = null;
        }

        // Creating m1 and m2 array using knight moves
        for (int index = 0; index < dataKnightMovesNumberList.size(); index++) {
            char tempArr[] = new char[(KNIGHT_MATRIX_WIDTH * KNIGHT_MATRIX_HEIGHT)];
            System.out.print("M" + (index + 1) + "  --  ");
            for (int i = 0; i < dataKnightMovesNumberList.get(index).length; i++) {
                tempArr[i] = AllCharacter.getInstance().getStandardCharacterSet()[dataKnightMovesNumberList.get(index)[i]];
                //System.out.print(AllCharacter.getInstance().getStandardCharacterSet()[knightMovesNumberList.get(index)[i]]+",");
            }

            System.out.println();
            dataKnightMovesStringList.add(tempArr);
            tempArr = null;
        }

        // Logic to decrypt Data Ciphering
        // read index of current incoming encrypt from m2. Read index in decresing order from knightMovesSringList
        // match character in m2 cipher list
        char encryptedDataArray[] = new char[dataBuffer[1].length()];
        encryptedDataArray = dataBuffer[1].toCharArray();
        int encryptedDataIndexFromM2[] = new int[dataBuffer[1].length()];
        int indexValuesFromM1[] = new int[dataBuffer[1].length()];
        for (int index = dataKnightMovesStringList.size() - 1; index >= 0; index--) {
            if (index == 1) {//M2
                for (int i = 0; i < dataBuffer[index].length(); i++) {
                    for (int j = 0; j < dataKnightMovesStringList.get(index).length; j++) {
                        if (encryptedDataArray[i] == dataKnightMovesStringList.get(index)[j]) {
                            encryptedDataIndexFromM2[i] = j;
                            System.out.println("Index Found From M2 = " + j);
                        }

                    }
                }
            }
            if (index == 0) {
                // for m1 , get index values from last
                // take index from Knight tour matrix of M1 in decresing order 255..254..253..>
                int travelling = this.MAX_MOVES - encryptedDataArray.length;
                System.out.println("Travelling backwards for " + travelling);
                int i = 0;
                for (int x = this.MAX_MOVES - 1; x >= travelling; x--) {
                    indexValuesFromM1[i] = dataKnightMovesNumberList.get(index)[x];
                    System.out.println("From M1, positions" + indexValuesFromM1[i]);
                    i++;
                }
            }
        }
        // we have to add 256 in M2 Index and subtract m1 index to get actual index on M2
        char actualData[] = new char[encryptedDataArray.length];
        for (int i = 0; i < encryptedDataArray.length; i++) {
            encryptedDataIndexFromM2[i] = encryptedDataIndexFromM2[i] + this.ST_SIZE;
            encryptedDataIndexFromM2[i] = encryptedDataIndexFromM2[i] - indexValuesFromM1[i];
            if (encryptedDataIndexFromM2[i] >= this.ST_SIZE) {
                encryptedDataIndexFromM2[i] = encryptedDataIndexFromM2[i] - this.ST_SIZE;
            }
            System.out.println("Final positions = " + encryptedDataIndexFromM2[i]);
            // Fetch Chars at new M2 Index , that will be decoded data
            actualData[i] = dataKnightMovesStringList.get(1)[encryptedDataIndexFromM2[i]];
            System.out.println("Final Data = " + actualData[i]);
        }
        System.out.println();
        StringBuffer buff = new StringBuffer();
        buff.append(actualData);
        System.out.println("Original Data=" + buff.toString());
        return buff.toString();

    }




    /* *//*
    Function to encrypt generic data up to M2.
    The M2 encrypt would be posted to LokDOn User registration and edit profile
    However, Password would be encrypted upto M5.
    The function would recieve a string data and will return encoded M2 string
    *//*
    public String encryptGenericData(String genericString) {
        char [] genericStringToArray = genericString.toCharArray();
        //create random number list of number of moves of night
        this.generateKnightMovePositions(MAX_MPIN_TOUR); // M1 and M2 only
        // now initialize array list of walks and store in knightWalkNumberList
        this.createKnightWalkNumberStore();
        System.out.println("Total Knight Walks for Encryting Generic String = "+ this.knightWalkNumberList.size());
        if (this.cipherTemplateList != null) { this.cipherTemplateList.clear(); }

        // Creating new character array using knight moves and store in ciperTemplateStore
        for (int index = 0; index < this.knightWalkNumberList.size(); index++) {
            char[] genericStringCipherChars = this.createCipherTemplate(this.knightWalkNumberList.get(index));
            this.printCipherTemplate(genericStringCipherChars);
            this.createCipherTemplateStore(genericStringCipherChars);
        }
        *//*
        we have now
        1. Knight move positions Array
        2. Array list of cipher chars based on knight moves
        3. Knight moves array list, based on knight move positions.
        *//*
        // find the indices of our input data string in M1 and M2 and store in an array
        // we will add indices of our data chars and will finally find new chars from M2 as ciper of our data

        int originalDataCharsM1Index[] = new int[genericStringToArray.length];
        int originalDataCharsM2Index[] = new int[genericStringToArray.length];
        // place holders
        StringBuffer cipherBuffer = new StringBuffer();
        for (int index = 0; index < this.cipherTemplateList.size(); index++) {
            System.out.print(" in M"+index+" orignal index of generic Data chars is as : ");
            for (int i = 0; i < genericStringToArray.length; i++) {
                for (int j = 0; j < this.cipherTemplateList.get(index).length; j++) {
                    if (genericStringToArray[i] == this.cipherTemplateList.get(index)[j]) {
                        if (index == 1) {
                            originalDataCharsM2Index[i] = j;
                            System.out.println("From M2, positions" + j);
                        }
                        if (index == 0) {
                            originalDataCharsM1Index[i] = j;
                            System.out.println("From M1, positions" + j);
                        }
                    }
                }
            }
        }
        *//* Add both Indices from M1 and M2 cipher templates
           if result is more tahn 255 , sutract 255 from it that will give new index
        *//*
        int dataCharsFinalIndex[] = new int[genericStringToArray.length];
        for (int pos = 0; pos < genericStringToArray.length; pos++) {
            System.out.println("Adding index values" + originalDataCharsM1Index[pos]+ " and " + originalDataCharsM2Index[pos]);
            dataCharsFinalIndex[pos] =  originalDataCharsM1Index[pos] + originalDataCharsM2Index[pos];
            if (dataCharsFinalIndex[pos] > this.ST_SIZE) {
                dataCharsFinalIndex[pos] = dataCharsFinalIndex[pos] - this.ST_SIZE;
            }
            System.out.println("Final Index = "+ dataCharsFinalIndex[pos]);
            cipherBuffer.append(this.cipherTemplateList.get(1)[dataCharsFinalIndex[pos]]);
        }
        System.out.println("Encrypted PIN=" + cipherBuffer);
        // add knights tour as chars from reverse ST
        for (int i = 0; i < this.knightMoveNumbers.length; i++) {
            cipherBuffer.append(AllCharacter.getInstance().getCharacterReverseArray()[this.knightMoveNumbers[i]]);
            System.out.println(this.knightMoveNumbers[i] + ",");
        }
        System.out.println("Final Encrypted Data=" + cipherBuffer);
        return cipherBuffer.toString();
    }*/

    /*
    Function to decrypt a given encrypted PIN with a given single position
    */
    public String decryptMpinFromSinglePosition(String mpin) {
        // last char is always KT. Combine last char of M2 KT3 with M3
        // input will be mpin = M3+KT3 from M2KT1KT2KT3
        char lastChar = mpin.charAt(mpin.length() - 1);
        ;
        // rest 8 chars are encrypt of PIN
        if (mpin != null && mpin.length() > 0) {
            mpin = mpin.substring(0, mpin.length() - 1);
        }
        ArrayList<char[]> mpinKnightMovesStringList = new ArrayList<char[]>();
        System.out.println("The Encrypt = " + mpin);
        int mpinKnightMove = 0;
        ArrayList<int[]> mpinKnightMovesNumberList = new ArrayList<int[]>();
        StringBuffer returnBuffer = new StringBuffer();
        // its knights move character from reversed ST
        for (int j = 0; j < AllCharacter.getInstance().getCharacterReverseArray().length; j++) {
            if (lastChar == AllCharacter.getInstance().getCharacterReverseArray()[j]) {
                mpinKnightMove = j;
            }
        }
        System.out.println("The Knight Moves is = " + mpinKnightMove);

        KnightSolver solver = new KnightSolver(KNIGHT_MATRIX_WIDTH, KNIGHT_MATRIX_HEIGHT);
        solver.setPosition(mpinKnightMove % KNIGHT_MATRIX_WIDTH, mpinKnightMove / KNIGHT_MATRIX_HEIGHT);
        int temp[] = solver.loopSolver();
        mpinKnightMovesNumberList.add(temp);
        solver.reset();
        solver = null;
        // Creating cipher array using knight moves
        for (int index = 0; index < mpinKnightMovesNumberList.size(); index++) {
            char tempArr[] = new char[(KNIGHT_MATRIX_WIDTH * KNIGHT_MATRIX_HEIGHT)];
            System.out.print("M" + (index + 1) + "  --  ");
            for (int i = 0; i < mpinKnightMovesNumberList.get(index).length; i++) {
                tempArr[i] = AllCharacter.getInstance().getStandardCharacterSet()[mpinKnightMovesNumberList.get(index)[i]];
                //System.out.print(AllCharacter.getInstance().getStandardCharacterSet()[knightMovesNumberList.get(index)[i]]+",");
            }
            mpinKnightMovesStringList.add(tempArr);
            tempArr = null;
        }
        //now we have full matrix of Mn
        char originalSecurityPin[] = new char[mpin.length()];
        originalSecurityPin = mpin.toCharArray();
        int originalPinCharIndex[] = new int[mpin.length()];
        System.out.println("Length of mpinKnightMovesStringList=" + mpinKnightMovesStringList.size());
        System.out.println("mpin 1 = " + mpin);

        String lastTwoChars;
        int[] positions = new int[2];
        boolean shouldContinue = true;
        for (int index = mpinKnightMovesStringList.size() - 1; index >= 0; index--) {
            while (shouldContinue) {
                // iterate over string and take 2 chars from alst each time, as we have 8 chars mpin
                lastTwoChars = mpin.substring(mpin.length() - 2);
                System.out.println("Last Two Chars = " + lastTwoChars);
                char[] twoCharsFromMpin = new char[lastTwoChars.length()];
                twoCharsFromMpin = lastTwoChars.toCharArray();
                //start from last character
                System.out.println("Last Two Chars length= " + lastTwoChars.length());
                for (int i = lastTwoChars.length() - 1; i >= 0; i--) {
                    for (int j = 0; j < mpinKnightMovesStringList.get(index).length; j++) {
                        //System.out.println("I = "+i+",j="+j);
                        if (twoCharsFromMpin[i] == mpinKnightMovesStringList.get(index)[j]) {
                            positions[i] = j;
                            //System.out.print("Positions = "+j+",");
                            break;
                        }
                    }
                }
                System.out.println("Last Char Position=" + positions[1]);
                System.out.println("Second Last Char Position=" + positions[0]);

                if (positions[1] < positions[0]) {
                    System.out.println("Left Pos Char would be" + mpinKnightMovesStringList.get(index)[positions[0] + 1]);
                    returnBuffer.append(mpinKnightMovesStringList.get(index)[positions[0] + 1]);
                } else if (positions[1] > positions[0]) {
                    if (positions[1] - positions[0] == 2) {
                        System.out.println("Mid Char would be" + mpinKnightMovesStringList.get(index)[(positions[0] + positions[1]) / 2]);
                        returnBuffer.append(mpinKnightMovesStringList.get(index)[(positions[0] + positions[1]) / 2]);
                    } else {
                        System.out.println("Right Char would be" + mpinKnightMovesStringList.get(index)[positions[0] + 1]);
                        returnBuffer.append(mpinKnightMovesStringList.get(index)[positions[0] - 1]);
                    }
                }
                mpin = mpin.substring(0, mpin.length() - 2);
                System.out.println("Remaining MPIN Buffer = " + mpin);
                if (mpin.length() <= 0) {
                    shouldContinue = false;
                }
            }
        }
        System.out.println();
        return returnBuffer.reverse().toString();
    }

    /*
    Encrypt MPIN for Lokdon app, wrapper over encryptSecurityPin() ,
    returns: M2PIN+KT2+KT3+M3PIN
    */
    public String encryptTransactionSecurityPin(String mpin) {
        int[] leftBorder = new int[]{0, 16, 32, 48, 64, 80, 96, 112, 128, 144, 160, 176, 192, 208, 224, 240};
        int[] rightBorder = new int[]{15, 31, 47, 63, 79, 95, 111, 127, 143, 159, 175, 191, 207, 223, 239, 255};
        char[] mpinArray = mpin.toCharArray();

        //create random number list of number of moves of night
        this.generateKnightMovePositions(MAX_MPIN_TRANS_TOUR);
        // now initialize array list of walks and store in knightWalkNumberList
        this.createKnightWalkNumberStore();
        System.out.println("Total Knight Walks for PIN = " + this.knightWalkNumberList.size());
        if (this.cipherTemplateList != null) {
            this.cipherTemplateList.clear();
        }

        // Creating new character array using knight moves and store in ciperTemplateStore
        for (int index = 0; index < this.knightWalkNumberList.size(); index++) {
            char[] currentCipherChars = this.createCipherTemplate(this.knightWalkNumberList.get(index));
            this.printCipherTemplate(currentCipherChars);
            this.createCipherTemplateStore(currentCipherChars);
        }
        /*
        we have now
        1. Knight move positions Array
        2. Array list of cipher chars based on knight moves
        3. Knight moves array list, based on knight move positions.
        */
        // find the indices of our input PIN Digits in M1 and M2 and store in an array

        int originalPinIndex[] = new int[mpinArray.length];
        // place holders
        StringBuffer cipherBufferForM2 = new StringBuffer();
        StringBuffer cipherBufferForM3 = new StringBuffer();

        for (int index = 0; index < this.cipherTemplateList.size(); index++) {
            System.out.print(" in M1 orignal index of PIN is as : ");
            for (int i = 0; i < mpinArray.length; i++) {
                for (int j = 0; j < this.cipherTemplateList.get(index).length; j++) {
                    //System.out.println("Comparing " +mpinArray[i]+", "+ this.cipherTemplateList.get(index)[j]) );
                    if (mpinArray[i] == this.cipherTemplateList.get(index)[j]) {
                        originalPinIndex[i] = j;
                        System.out.print(originalPinIndex[i] + " , ");
                    }
                }
            }
            System.out.println();

            // we need to find out positions of mpin key in M2 and M3
            if (index == 1) {
                // check if we are in situation of boundaries in left or right array
                int pinPosition = 0;
                for (int mpos = 0; mpos < originalPinIndex.length; mpos++) {
                    System.out.println("searching for " + originalPinIndex[mpos] + " in left Array");
                    pinPosition = this.getArrayIndexValue(leftBorder, originalPinIndex[mpos]);
                    if (pinPosition > -1) { // if index found
                        System.out.println("Match Found in Left Array index number:" + pinPosition);
                        // we got a mpin position in left array boundry
                        // 2 chars from left of the pinPosition from M2 should be taken as cipher char
                        cipherBufferForM2.append(this.cipherTemplateList.get(index)[pinPosition + 1]);
                        cipherBufferForM2.append(this.cipherTemplateList.get(index)[pinPosition + 2]);
                    } else {
                        //
                        System.out.println("searching for " + originalPinIndex[mpos] + "in RIGHT Array");
                        pinPosition = this.getArrayIndexValue(rightBorder, originalPinIndex[mpos]);
                        if (pinPosition > -1) { // if index  found
                            System.out.println("Match Found in Right Array index number:" + pinPosition);
                            // 2 chars from right of the pinPosition from M2 should be taken as cipher char
                            cipherBufferForM2.append(this.cipherTemplateList.get(index)[pinPosition - 1]);
                            cipherBufferForM2.append(this.cipherTemplateList.get(index)[pinPosition - 2]);
                        } else {
                            // we dit not find the index as a value in either left or right array
                            // just take one chr from left and one char from right
                            cipherBufferForM2.append(this.cipherTemplateList.get(index)[originalPinIndex[mpos] - 1]);
                            cipherBufferForM2.append(this.cipherTemplateList.get(index)[originalPinIndex[mpos] + 1]);
                        }
                    }
                }
            }
            if (index == 2) { // In M3
                // check if we are in situation of boundaries in left or right array
                int pinPosition = 0;
                for (int mpos = 0; mpos < originalPinIndex.length; mpos++) {
                    System.out.println("searching for " + originalPinIndex[mpos] + " in left Array");
                    pinPosition = this.getArrayIndexValue(leftBorder, originalPinIndex[mpos]);
                    if (pinPosition > -1) { // if index found
                        System.out.println("Match Found in Left Array index number:" + pinPosition);
                        // we got a mpin position in left array boundry
                        // 2 chars from left of the pinPosition from M2 should be taken as cipher char
                        cipherBufferForM3.append(this.cipherTemplateList.get(index)[pinPosition + 1]);
                        cipherBufferForM3.append(this.cipherTemplateList.get(index)[pinPosition + 2]);
                    } else {
                        //
                        System.out.println("searching for " + originalPinIndex[mpos] + "in RIGHT Array");
                        pinPosition = this.getArrayIndexValue(rightBorder, originalPinIndex[mpos]);
                        if (pinPosition > -1) { // if index  found
                            System.out.println("Match Found in Right Array index number:" + pinPosition);
                            // 2 chars from right of the pinPosition from M2 should be taken as cipher char
                            cipherBufferForM3.append(this.cipherTemplateList.get(index)[pinPosition - 1]);
                            cipherBufferForM3.append(this.cipherTemplateList.get(index)[pinPosition - 2]);
                        } else {
                            // we dit not find the index as a value in either left or right array
                            // just take one chr from left and one char from right
                            cipherBufferForM3.append(this.cipherTemplateList.get(index)[originalPinIndex[mpos] - 1]);
                            cipherBufferForM3.append(this.cipherTemplateList.get(index)[originalPinIndex[mpos] + 1]);
                        }
                    }
                }
            }
        }
        System.out.println("Encrypted PIN in M2=" + cipherBufferForM2);
        System.out.println("Encrypted PIN in M3=" + cipherBufferForM3);
        // add knights tour as chars from reverse ST
        for (int i = 0; i < this.knightMoveNumbers.length; i++) {
            cipherBufferForM2.append(AllCharacter.getInstance().getCharacterReverseArray()[this.knightMoveNumbers[i]]);
            System.out.println(this.knightMoveNumbers[i] + ",");
        }
        System.out.println("Final Encrypted M2 PIN=" + cipherBufferForM2);
        System.out.println("Final Encrypted M3 PIN=" + cipherBufferForM3);
        return cipherBufferForM2.append(cipherBufferForM3).toString();
    }

    /* Function to decrypt trasaction mpin , a simple wrapper over decryptSecurityPin() function
     */
    public String decryptTransactionSecurityPin(String transPin) {
        // last char is KT3, skip this char and call decryptSecurityPin() function

        if (transPin != null && transPin.length() > 0) {
            transPin = transPin.substring(0, transPin.length() - 1);
        }
        String decryptedPin = this.decryptSecurityPin(transPin);
        System.out.println("The decrypted Pin = " + decryptedPin);
        return decryptedPin;
    }

    private static String encode(String s, String key) {
        return base64Encode(xorWithKey(s.getBytes(), key.getBytes()));
    }

    private static String decode(String s, String key) {
        return new String(xorWithKey(base64Decode(s), key.getBytes()));
    }

    private static byte[] xorWithKey(byte[] input, byte[] secret) {
        final byte[] output = new byte[input.length];
        if (secret.length == 0) {
            throw new IllegalArgumentException("empty security key");
        }
        int spos = 0;
        for (int pos = 0; pos < input.length; ++pos) {
            output[pos] = (byte) (input[pos] ^ secret[spos]);
            ++spos;
            if (spos >= secret.length) {
                spos = 0;
            }
        }
        return output;
    }

    private static byte[] base64Decode(String s) {


        Base64.Decoder d = Base64.getDecoder();
        return d.decode(s);
    }

    private static String base64Encode(byte[] bytes) {
        Base64.Encoder enc = Base64.getEncoder();
        return enc.encodeToString(bytes).replaceAll("\\s", "");
    }

    public String encryptNew(String original, String m3pin, String sess_id) {
        String enc = encryptGenericData(original);
        enc = enc.concat(m3pin);
        return encode(enc, sess_id);
    }

    public String decryptNew(String encrypted, String m3pin, String sess_id) {
        String dec = decode(encrypted, sess_id);
        System.out.println("decoded: " + dec);
        String original = dec.replace(m3pin, "");
        return decryptGenericData(original);
    }

}