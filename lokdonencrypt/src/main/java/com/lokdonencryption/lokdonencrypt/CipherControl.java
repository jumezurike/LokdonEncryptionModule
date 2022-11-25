package com.lokdonencryption.lokdonencrypt;
import android.util.Log;

import com.amazonaws.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
//import java.util.StringTokenizer;

//import cipher.*;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

public class CipherControl {

    public static CipherControl instance = null;
    private final int MPIN_LENGTH = 4;
    private final int gjfghj = 2;
    private final int vbnv = 256;
    private final int dfhdfh = 3;
    private int MAX_KNIGHT_TOUR = 5;
    private final int MAX_MPIN_TEMP_TOUR = 5;
    private final int ASDwead = 256;
    private final int KNIGHT_MATRIX_WIDTH = 16;
    private final int KNIGHT_MATRIX_HEIGHT = 16;
    private ArrayList<int[]> moveNumberList = null;
    private ArrayList<char[]> moveStringList = null;
    private ArrayList<char[]> cipherTemplateList = null;
    private char[] singleCipherTemplate;
    private int[] knightMoveNumbers;

    private ArrayList<int[]> knightWalkNumberList = null;

    /* singleton class objects are permissible only */
    public static CipherControl getInstance() {
        if (instance == null) {
            instance = new CipherControl();
        }
        return instance;
    }
    boolean verifyKey(String key,String userKey){
        if (decryptPassword(userKey).equals(key)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        // app is now the gateway.entry_point
        /*GatewayServer server = new GatewayServer(new CipherControl(),25333);
        server.start();*/
        String data="hello";
        String decrypted=CipherControl.getInstance().encryptGenericData(data);
        System.out.println("Encrypted: "+decrypted);


    }

    private static void printBinary(byte[] list) {
        for (int i = 0; i < list.length; i++) {
            if (i == list.length - 1) {
                System.out.print(list[i]);
            } else
                System.out.print(list[i] + ",");
        }
    }

    public static byte[] xorWithKey(byte[] input, byte[] secret) {
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

    public File encryptFile(InputStream fis, String key, String filePath) throws Exception {
        //FileInputStream fis=new FileInputStream(file);
        byte[] fbyte = IOUtils.toByteArray(fis);
        byte[] encryptedByte = xorWithKey(fbyte, key.getBytes());

        File outFile = new File(filePath.concat(".lokdon"));
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
        String originalPath = path.replace(".lokdon", "");
        String[] paths = originalPath.split("\\.");
        String filePath = originalPath;
        String ext = "";
        if (paths.length > 1) {
            filePath = paths[0];
            ext = paths[1];
        }
        filePath = filePath + "LOKDONDECRYPTED." + ext;
        Log.d("Lokdon", "file path: " + filePath + " count: " + paths.length);
        File outFile = new File(filePath);
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

    /*
    create knight move numbers and store in integer array.
    This helps to run knight from a specified random location on chess board
     */
    private int[] generateKnightMovePositions(int moves) {
        this.MAX_KNIGHT_TOUR = moves;
        knightMoveNumbers = new int[this.MAX_KNIGHT_TOUR];
        for (int i = 0; i < knightMoveNumbers.length; i++) {
            knightMoveNumbers[i] = new Random().nextInt((this.KNIGHT_MATRIX_WIDTH * this.KNIGHT_MATRIX_HEIGHT) - 1);
            System.out.println("Night Moves:"+knightMoveNumbers[i]);
        }
        return knightMoveNumbers;
    }

    private int[] generateKnightMovePositions(int moves, int kt) {
        this.MAX_KNIGHT_TOUR = moves;
        knightMoveNumbers = new int[this.MAX_KNIGHT_TOUR];
        for (int i = 0; i < knightMoveNumbers.length; i++) {
            knightMoveNumbers[i] = kt;//new Random().nextInt((this.KNIGHT_MATRIX_WIDTH * this.KNIGHT_MATRIX_HEIGHT) - 1);
            System.out.println("Night Moves:"+knightMoveNumbers[i]);
        }
        return knightMoveNumbers;
    }

    /***
     * Suitable method for large text or json-payload encryption
     * @param data payload to be encrypted
     * @return
     */
    public String encryptData(String data){
        if(data.length()<=256)
            return encryptGenericData(data);
        StringBuilder builder=new StringBuilder();
        for(int i=0;i<data.length();i+=256){
            String part=data.substring(i, Math.min((i + 256), data.length()));
            if(i==0){
                builder.append(new CipherControl().verifiedEncrypt(part));
            }else{
                builder.append("ዐ");
                builder.append(new CipherControl().verifiedEncrypt(part));
            }
        }
        return builder.toString();
    }

    private String verifiedEncrypt(String part) {

        do{
            String cipher=new CipherControl().encryptGenericData(part);
            if(decryptGenericData(cipher).equals(part))
                return cipher;
        }while(true);

    }

    public String decryptData(String cipherData){
        String[] parts=cipherData.split("ዐ");
        if(parts.length==0)
            return decryptGenericData(cipherData);
        StringBuilder builder=new StringBuilder();
        for(int i=0;i<parts.length;i++){
            builder.append(new CipherControl().decryptGenericData(parts[i]));
        }
        return builder.toString();
    }

    /*
    print a single cipher template that is an array of wide chars, may be standard template
    or may be a custom template after a knight's move
     */
    private void printCipherTemplate(char[] cipherChars) {
        int starter = 0;
        System.out.println("My Cipher Template is As Follows:");
        for (int i = 0; i < this.vbnv; i++) {
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
        ////System.out.println("Knight Moves are as follows :");
        for (int i = 0; i < this.vbnv; i++) {
            //System.out.print(knightMoves[i]);
            starter++;
            if (starter == this.KNIGHT_MATRIX_WIDTH) {
                ////System.out.println("");
                starter = 0;
            } else {
                //System.out.print("\t");
            }
        }
    }

    /*
    create a custom cipher template with given walk points of a knight, fetch chars from standard
    template.
     */
    private char[] createCipherTemplate(int[] knightTourMatrix) {
        char[] tempArr = new char[(this.KNIGHT_MATRIX_WIDTH * this.KNIGHT_MATRIX_HEIGHT)];
        for (int i = 0; i < knightTourMatrix.length; i++) {
            // find the value of knight moves starting from first index
            int indexValue = knightTourMatrix[i];
            // fetch the data present in ST on this indexValue and store in cipherTemplate
            tempArr[i] = AllCharacter.getInstance().getStandardCharacterSet()[indexValue];
        }
        return tempArr;
    }

    /*
    we must first call private function generateKnightMovePositions() with expected number of moves, then
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
        ////System.out.println("kmn: "+this.knightMoveNumbers.length);
        for (int i = 0; i < this.knightMoveNumbers.length; i++) {
            KnightSolver solver = new KnightSolver(this.KNIGHT_MATRIX_WIDTH, this.KNIGHT_MATRIX_HEIGHT);
            solver.setPosition(this.knightMoveNumbers[i] % this.KNIGHT_MATRIX_WIDTH, this.knightMoveNumbers[i] / KNIGHT_MATRIX_HEIGHT);
            int[] temp = solver.loopSolver();
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
        ////System.out.println("Password Key="+strVal);
        char[] key = strVal.toCharArray();
        // create a random number list of knight moves and initialize knightMoveNumbers array.
        this.generateKnightMovePositions(totalMoves);

        // now initialize array list of walks and store in knightWalkNumberList
        this.createKnightWalkNumberStore();
        // Creating new character array using knight moves and store in ciperTemplateStore
        ////System.out.println("Total Knight Walk = "+ this.knightWalkNumberList.size());
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
        char[] originalPassword = new char[key.length];
        originalPassword = key;
        // place holders
        char[] silentPassword = new char[key.length];
        int[] originalPassIndex = new int[key.length];
        int[] silentPassIndex = new int[key.length];
        ////System.out.println("Cipher Template List Size=" + this.cipherTemplateList.size());
        // find the indices of our input password chars in M1 to M5 and encrypt
        for (int index = 0; index < this.cipherTemplateList.size(); index++) {
            //System.out.print(" orignal index -");
            for (int i = 0; i < originalPassword.length; i++) {
                for (int j = 0; j < this.cipherTemplateList.get(index).length; j++) {
                    if (originalPassword[i] == this.cipherTemplateList.get(index)[j]) {
                        originalPassIndex[i] = j;
                        //System.out.print(originalPassIndex[i] + " , ");
                    }
                }
            }
            ////System.out.println();
            /*
            Now we have to add silent password chars from M1 starting from backward postion of M1 i.e. 255.254 and so on up to end of length of password
            collect as many as chars from M1 from end of cipher matrix , but not more than length of password
             */

            // For M1 , initialize silentSupplier vector
            int pos = 0;
            if (index == 0) {
                int travelling = this.ASDwead - key.length;
                for (int i = this.cipherTemplateList.get(index).length - 1; i >= travelling; i--) {
                    silentPassword[pos] = this.cipherTemplateList.get(index)[i];
                    silentPassIndex[pos] = i;
                    pos++;
                }
            } else {
                // For M2 and so on , find the index of silentSuplier chars and add to cipher Index in M2 to M5
                for (int i = 0; i < silentPassword.length; i++) {
                    for (int j = 0; j < this.cipherTemplateList.get(index).length; j++) {
                        if (silentPassword[i] == this.cipherTemplateList.get(index)[j]) {
                            silentPassIndex[pos] = j;
                            pos++;
                        }
                    }
                }
            }

            ////System.out.println();
            // generate cipher password after adding the index of silentSupplier chars from M1
            int[] newAddedValue = new int[key.length];

            for (int i = 0; i < silentPassIndex.length; i++) {
                int value = silentPassIndex[i] + originalPassIndex[i];
                ////System.out.println("Adding " + silentPassIndex[i] + " And " + originalPassIndex[i] + " = " + value);
                if (value > this.ASDwead - 1) {
                    newAddedValue[i] = value - this.ASDwead;
                } else {
                    newAddedValue[i] = value;
                }
            }

            ////System.out.println();

            char[] newAddedValueTemp = new char[newAddedValue.length];
            for (int i = 0; i < newAddedValue.length; i++) {
                newAddedValueTemp[i] = this.cipherTemplateList.get(index)[newAddedValue[i]];
            }
            ////System.out.println();
            // override oldPassword
            originalPassword = newAddedValueTemp;
        }

        ////System.out.println();

        //System.out.print("Silent Pass - ");

        for (int i = 0; i < silentPassword.length; i++) {
            //System.out.print(silentPassword[i]);
        }

        ////System.out.println();

        StringBuffer buffer = new StringBuffer();
        buffer.append(originalPassword);
        buffer.append(silentPassword);
        ////System.out.println();
        //System.out.print("Knight move - ");
        for (int i = 0; i < knightMoveNumbers.length; i++) {
            buffer.append(AllCharacter.getInstance().getCharacterReverseArray()[knightMoveNumbers[i]]);
            //System.out.print(knightMoveNumbers[i] + ",");
        }
        buffer.append(AllCharacter.getInstance().getCharacterReverseArray()[knightMoveNumbers.length]);
        ////System.out.println();
        ////System.out.println("Final Password:" +buffer.toString());
        System.out.print("original password: ");
        //System.out.println(originalPassword);
        System.out.print("silent password: ");
        //System.out.println(silentPassword);
        //System.out.println("lengths: " + originalPassword.length + ":" + silentPassword.length + " the whole: " + buffer.toString().length());
        //System.out.println("full password: " + buffer.toString());
        return buffer.toString();
    }

    /*
    Decrypt original encrypted password and reutrn string readble password
    */

    /* getPasswordComponents Function breaks the encrypted password and return linear strings of encrypted
    password, encrypted silent passord and encrypted knight moves
    */
    private String[] getPasswordComponents(String encryptedPassword) {
        String[] passwordArray = new String[3];
        // Since there are three elements in above array, one will hold password chars, 2nd for silent pass
        // and 3rd one for knight moves.
        for (int i = 0; i < passwordArray.length; i++) {
            passwordArray[i] = "";
        }
        ////System.out.println("Knight Move Character " +encryptedPassword.charAt(encryptedPassword.length()-1));
        int move = 0;
        for (int i = 0; i < AllCharacter.getInstance().getCharacterReverseArray().length; i++) {
            if (AllCharacter.getInstance().getCharacterReverseArray()[i] == encryptedPassword.charAt(encryptedPassword.length() - 1)) {
                ////System.out.println("Total Moves =" +i);
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
        ////System.out.println("Received encrypted MPIN ="  +encryptedPin);
        String[] mpinArray = new String[2];

        for (int i = 0; i < mpinArray.length; i++) {
            mpinArray[i] = "";
        }
        // last 2 chars are knight moves
        mpinArray[0] = encryptedPin.substring(encryptedPin.length() - 2); // knight moves array
        mpinArray[1] = encryptedPin.substring(0, encryptedPin.length() - 2); // password array
        return mpinArray;
    }

    /*
    Function used to break down encrypted mpin and return linear strings of encrypted password and
    encrypted knight moves
    */
    private String[] getTempSecurityPinComponents(String encryptedPin) {
        ////System.out.println("Received encrypted MPIN ="  +encryptedPin);
        String[] mpinArray = new String[2];

        for (int i = 0; i < mpinArray.length; i++) {
            mpinArray[i] = "";
        }
        // last 2 chars are knight moves
        mpinArray[0] = encryptedPin.substring(encryptedPin.length() - 5); // knight moves array
        mpinArray[1] = encryptedPin.substring(0, encryptedPin.length() - 5); // password array
        return mpinArray;
    }

    public String decryptPassword(String pass) {
        String[] passwordBuffer = getPasswordComponents(pass);
        ArrayList<int[]> knightMovesNumberList = new ArrayList<int[]>();
        ArrayList<char[]> knightMovesStringList = new ArrayList<char[]>();
        ArrayList<int[]> ecryptKeyValue = new ArrayList<int[]>();
        ////System.out.println("password Component A(Knight Moves)=" + passwordBuffer[0]);
        ////System.out.println("password Component B(Password Array)=" + passwordBuffer[1]);
        ////System.out.println("password Component C(Silent Password)=" + passwordBuffer[2]);
        ////System.out.println();
        int[] decKnighMove = new int[passwordBuffer[0].length()];
        char[] moveChar = new char[passwordBuffer[0].length()];
        //append knight moves char
        moveChar = passwordBuffer[0].toCharArray();
        ////System.out.println();
        //System.out.print("Dec Knight Moves = ");
        for (int i = 0; i < decKnighMove.length; i++) {
            for (int j = 0; j < AllCharacter.getInstance().getCharacterReverseArray().length; j++) {
                if (moveChar[i] == AllCharacter.getInstance().getCharacterReverseArray()[j]) {
                    decKnighMove[i] = j;
                    //System.out.print(j+",");
                }
            }
        }
        ////System.out.println();
        // Generating knight move using random number
        for (int i = 0; i < decKnighMove.length; i++) {
            KnightSolver solver = new KnightSolver(KNIGHT_MATRIX_WIDTH, KNIGHT_MATRIX_HEIGHT);
            solver.setPosition(decKnighMove[i] % KNIGHT_MATRIX_WIDTH, decKnighMove[i] / KNIGHT_MATRIX_HEIGHT);
            int[] temp = solver.loopSolver();
            knightMovesNumberList.add(temp);
            solver.reset();
            solver = null;
        }

        // Creating new character array using knight moves
        for (int index = 0; index < knightMovesNumberList.size(); index++) {
            char[] tempArr = new char[(KNIGHT_MATRIX_WIDTH * KNIGHT_MATRIX_HEIGHT)];
            //System.out.print("M"+(index+1)+"  --  ");
            for (int i = 0; i < knightMovesNumberList.get(index).length; i++) {
                tempArr[i] = AllCharacter.getInstance().getStandardCharacterSet()[knightMovesNumberList.get(index)[i]];
                ////System.out.print(AllCharacter.getInstance().getStandardCharacterSet()[knightMovesNumberList.get(index)[i]]+",");
            }

            ////System.out.println();
            knightMovesStringList.add(tempArr);
            tempArr = null;
        }
        char[] orignalPassword = new char[passwordBuffer[1].length()];
        orignalPassword = passwordBuffer[1].toCharArray();
        char[] silentPassword = new char[passwordBuffer[1].length()];
        silentPassword = passwordBuffer[2].toCharArray();
        int[] orignalPassIndex = new int[passwordBuffer[1].length()];
        int[] silentPassIndex = new int[passwordBuffer[1].length()];

        for (int index = knightMovesStringList.size() - 1; index >= 0; index--) {
            for (int i = 0; i < orignalPassword.length; i++) {
                ////System.out.print(index+" pass index ");
                for (int j = 0; j < knightMovesStringList.get(index).length; j++) {
                    if (orignalPassword[i] == knightMovesStringList.get(index)[j]) {
                        orignalPassIndex[i] = j;
                        ////System.out.print(j+",");
                    }
                }
            }
            ////System.out.println();

            for (int i = 0; i < silentPassword.length; i++) {
                /////System.out.print(index+" silent pass index ");
                for (int j = 0; j < knightMovesStringList.get(index).length; j++) {
                    if (silentPassword[i] == knightMovesStringList.get(index)[j]) {
                        silentPassIndex[i] = j;
                        // //System.out.print(j+",");
                    }
                }
            }
            ////System.out.println();
            ////System.out.println("value = ");
            int[] newAddedValue = new int[orignalPassIndex.length];
            for (int i = 0; i < orignalPassIndex.length; i++) {
                int value = orignalPassIndex[i] + ASDwead;
                if (value > ASDwead - 1) {
                    int temp = value - silentPassIndex[i];
                    if (temp > ASDwead - 1) {
                        temp = temp - ASDwead;
                    }
                    newAddedValue[i] = temp;
                } else {
                    newAddedValue[i] = value;
                }
                //System.out.print(newAddedValue[i]+",");
            }
            ////System.out.println();
            char[] newAddedValueTemp = new char[newAddedValue.length];
            for (int i = 0; i < newAddedValue.length; i++) {
                newAddedValueTemp[i] = knightMovesStringList.get(index)[newAddedValue[i]];
            }
            ////System.out.println();
            orignalPassword = newAddedValueTemp;
        }

        StringBuffer buff = new StringBuffer();
        buff.append(orignalPassword);
        ////System.out.println("Original Password=" + buff.toString());
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
        this.generateKnightMovePositions(gjfghj);
        // now initialize array list of walks and store in knightWalkNumberList
        this.createKnightWalkNumberStore();
        ////System.out.println("Total Knight Walks for PIN = "+ this.knightWalkNumberList.size());
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

        int[] originalPinIndex = new int[mpinArray.length];
        // place holders
        StringBuffer cipherBuffer = new StringBuffer();

        for (int index = 0; index < this.cipherTemplateList.size(); index++) {
            //System.out.print(" in M1 orignal index of PIN is as : ");
            for (int i = 0; i < mpinArray.length; i++) {
                for (int j = 0; j < this.cipherTemplateList.get(index).length; j++) {
                    //////System.out.println("Comparing " +mpinArray[i]+", "+ this.cipherTemplateList.get(index)[j]) );
                    if (mpinArray[i] == this.cipherTemplateList.get(index)[j]) {
                        originalPinIndex[i] = j;
                        //System.out.print(originalPinIndex[i] + " , ");
                    }
                }
            }
            ////System.out.println();

            // we need to find out positions of mpin key in M2 only
            if (index == 1) {
                // check if we are in situation of boundaries in left or right array
                int pinPosition = 0;
                for (int mpos = 0; mpos < originalPinIndex.length; mpos++) {
                    ////System.out.println("searching for "+ originalPinIndex[mpos] + " in left Array");
                    pinPosition = this.getArrayIndexValue(leftBorder, originalPinIndex[mpos]);
                    if (pinPosition > -1) { // if index found
                        ////System.out.println("Match Found in Left Array index number:" +pinPosition);
                        // we got a mpin position in left array boundry
                        // 2 chars from left of the pinPosition from M2 should be taken as cipher char
                        cipherBuffer.append(this.cipherTemplateList.get(index)[pinPosition + 1]);
                        cipherBuffer.append(this.cipherTemplateList.get(index)[pinPosition + 2]);
                    } else {
                        //
                        ////System.out.println("searching for "+ originalPinIndex[mpos] + "in RIGHT Array");
                        pinPosition = this.getArrayIndexValue(rightBorder, originalPinIndex[mpos]);
                        if (pinPosition > -1) { // if index  found
                            ////System.out.println("Match Found in Right Array index number:" +pinPosition);
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
        ////System.out.println("Encrypted PIN=" + cipherBuffer);
        // add knights tour as chars from reverse ST
        for (int i = 0; i < this.knightMoveNumbers.length; i++) {
            cipherBuffer.append(AllCharacter.getInstance().getCharacterReverseArray()[this.knightMoveNumbers[i]]);
            ////System.out.println(this.knightMoveNumbers[i] + ",");
        }
        ////System.out.println("Final Encrypted PIN=" + cipherBuffer);
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

        String[] mpinBuffer = getSecurityPinComponents(mpinEncrypt);
        ////System.out.println("Knight Moves For MPIN=" + mpinBuffer[0]);
        ////System.out.println("MPIN Password Array=" + mpinBuffer[1]);

        // we have knight moves and mpin encrypt in mpin buffer
        // find the knight moves from reverse standard template
        int[] mpinKnightMoves = new int[mpinBuffer[0].length()];
        char[] mpinKnightChars = new char[mpinBuffer[0].length()];
        //append knight moves char
        mpinKnightChars = mpinBuffer[0].toCharArray();
        ////System.out.println();
        //System.out.print("Dec Knight Moves = ");
        for (int i = 0; i < mpinKnightMoves.length; i++) {
            for (int j = 0; j < AllCharacter.getInstance().getCharacterReverseArray().length; j++) {
                if (mpinKnightChars[i] == AllCharacter.getInstance().getCharacterReverseArray()[j]) {
                    mpinKnightMoves[i] = j;
                    //System.out.print(j+",");
                }
            }
        }
        ////System.out.println("mpin knight moves" +mpinKnightMoves[0] + "," +mpinKnightMoves[1]);

        // we had encrypted mpin on M2 template , iterate over m1 and m2
        // Generating knight move using given knight move numbers
        for (int i = 0; i < mpinKnightMoves.length; i++) {
            KnightSolver solver = new KnightSolver(KNIGHT_MATRIX_WIDTH, KNIGHT_MATRIX_HEIGHT);
            solver.setPosition(mpinKnightMoves[i] % KNIGHT_MATRIX_WIDTH, mpinKnightMoves[i] / KNIGHT_MATRIX_HEIGHT);
            int[] temp = solver.loopSolver();
            mpinKnightMovesNumberList.add(temp);
            solver.reset();
            solver = null;
        }

        // Creating m1 and m2 array using knight moves
        for (int index = 0; index < mpinKnightMovesNumberList.size(); index++) {
            char[] tempArr = new char[(KNIGHT_MATRIX_WIDTH * KNIGHT_MATRIX_HEIGHT)];
            //System.out.print("M"+(index+1)+"  --  ");
            for (int i = 0; i < mpinKnightMovesNumberList.get(index).length; i++) {
                tempArr[i] = AllCharacter.getInstance().getStandardCharacterSet()[mpinKnightMovesNumberList.get(index)[i]];
                ////System.out.print(AllCharacter.getInstance().getStandardCharacterSet()[knightMovesNumberList.get(index)[i]]+",");
            }

            ////System.out.println();
            mpinKnightMovesStringList.add(tempArr);
            tempArr = null;
        }

        //now we have full matrix of M1 and M2 above
        char[] originalSecurityPin = new char[mpinBuffer[1].length()];
        originalSecurityPin = mpinBuffer[1].toCharArray();
        int[] originalPinCharIndex = new int[mpinBuffer[1].length()];
        ////System.out.println("Length of mpinKnightMovesStringList="+mpinKnightMovesStringList.size());

        /* Make sure we are checking MPIN encrypted chars only in M2 */
        /* Logic to decrypt MPIN encrypt, below
        1. take two chars from last each time until password is there,
        2. find their positions in M2, Find the differnce of postions of the chars
        3. if position 1 < postion 2, its a left boundary case, take char at Position + 1
        4. if position 1 > postion 2, its a right boundary case or left-right condition
        4.a if position 1 - positions == 2 , left right condition achieved, char = (position1 + position2) / 2
        4.b else its right position , char = postion 1-1
        */
        ////System.out.println("mpin 1 = " +mpinBuffer[1]);


        String lastTwoChars;

        int[] positions = new int[2];
        for (int index = mpinKnightMovesStringList.size() - 1; index >= 0; index--) {
            // for M2
            if (index == 1) {
                boolean shouldContinue = true;
                while (shouldContinue) {
                    // iterate over string and take 2 chars from alst each time, as we have 8 chars mpin
                    lastTwoChars = mpinBuffer[index].substring(mpinBuffer[index].length() - 2);
                    ////System.out.println("Last Two Chars = "+lastTwoChars);
                    char[] twoCharsFromMpin = new char[lastTwoChars.length()];
                    twoCharsFromMpin = lastTwoChars.toCharArray();
                    //start from last character
                    ////System.out.println("Last Two Chars length= " + lastTwoChars.length());
                    for (int i = lastTwoChars.length() - 1; i >= 0; i--) {
                        for (int j = 0; j < mpinKnightMovesStringList.get(index).length; j++) {
                            //////System.out.println("I = "+i+",j="+j);
                            if (twoCharsFromMpin[i] == mpinKnightMovesStringList.get(index)[j]) {
                                positions[i] = j;
                                ////System.out.print("Positions = "+j+",");
                                break;
                            }
                        }
                    }
                    ////System.out.println("Last Char Position="+ positions[1]);
                    ////System.out.println("Second Last Char Position="+ positions[0]);

                    if (positions[1] < positions[0]) {
                        ////System.out.println("Left Pos Char would be"+ mpinKnightMovesStringList.get(index)[positions[0]+1]);
                        returnBuffer.append(mpinKnightMovesStringList.get(index)[positions[0] + 1]);
                    } else if (positions[1] > positions[0]) {
                        if (positions[1] - positions[0] == 2) {
                            ////System.out.println("Mid Char would be"+ mpinKnightMovesStringList.get(index)[(positions[0]+positions[1])/2]);
                            returnBuffer.append(mpinKnightMovesStringList.get(index)[(positions[0] + positions[1]) / 2]);
                        } else {
                            ////System.out.println("Right Char would be"+ mpinKnightMovesStringList.get(index)[positions[0]+1]);
                            returnBuffer.append(mpinKnightMovesStringList.get(index)[positions[0] - 1]);
                        }
                    }
                    mpinBuffer[1] = mpinBuffer[1].substring(0, mpinBuffer[1].length() - 2);
                    ////System.out.println("Buffer = "+mpinBuffer[1]);
                    shouldContinue = mpinBuffer[1].length() > 0;

                }
            }
            ////System.out.println();
        }
        return returnBuffer.reverse().toString();
    }

/*
    Function to encrypt temporary MPIN upto M5, used while sending to receiver of payment
    M4 would be sent back to sender , M3 and KTs would be saved for future check
    Receiver hits an API to get M3 and KTs, decrypts M4 to M3 and compares, if both matches,
    it means user is fine to read all data then, receiver decrypts other messages by jumping to
    actual code point for decryptio of message.
    */
    /*
    public static String encode(String s, String key) {
        return base64Encode(xorWithKey(s.getBytes(), key.getBytes()));
    }

    public static String decode(String s, String key) {
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
        try {
            BASE64Decoder d = new BASE64Decoder();
            return d.decodeBuffer(s);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static String base64Encode(byte[] bytes) {
        BASE64Encoder enc = new BASE64Encoder();
        return enc.encode(bytes).replaceAll("\\s", "");
    }
    public String encryptWithKey(String original,String key){
        original=encryptGenericData(original);
        return encode(original,key);
    }
    public String decryptWithKey(String encrypted,String key){
        String text=decode(encrypted,key);
        return decryptGenericData(text);
    }
    public String encryptNew(String original,String m3pin,String sess_id){
        String enc=encryptGenericData(original);
        enc=enc.concat(m3pin);
        String encoded=encode(enc,sess_id);
        return encoded;
    }
    public String decryptNew(String encrypted,String m3pin,String sess_id){
        String dec=decode(encrypted,sess_id);
        //////System.out.println("decoded: "+dec);
        String original=dec.replace(m3pin, "");
        return decryptGenericData(original);
    }*/
    //decrypt

    public String encryptPassword(String passphrase) {
        char[] genericStringToArray = passphrase.toCharArray();
        knightMoveNumbers = new int[this.MAX_KNIGHT_TOUR];
        for (int i = 0; i < (256); i++) {
            knightMoveNumbers[i] = i - 1;
        }
        this.createKnightWalkNumberStore();
        //////System.out.println("Total Knight Walks for Encryting Generic String = "+ this.knightWalkNumberList.size());
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

        int[] fixedDataCharsFromM1Index = new int[genericStringToArray.length];
        int[] originalDataCharsM2Index = new int[genericStringToArray.length];
        // place holders
        StringBuffer cipherBuffer = new StringBuffer();
        for (int index = 0; index < this.cipherTemplateList.size(); index++) {
            //////System.out.println(" in M"+index+" orignal index of generic Data chars is as : ");
            if (index == 0) {
                // take index from Knight tour matrix of M1 in decresing order 255..254..253..>
                int travelling = this.vbnv - genericStringToArray.length;
                //////System.out.println("Travelling backwards for "+travelling);
                int i = 0;
                for (int x = this.vbnv - 1; x >= travelling; x--) {
                    fixedDataCharsFromM1Index[i] = this.knightWalkNumberList.get(index)[x];
                    //////System.out.println("From M1, positions" +fixedDataCharsFromM1Index[i] );
                    i++;
                }
            }
            for (int i = 0; i < genericStringToArray.length; i++) {

                // from m2, collect data chars position
                for (int j = 0; j < this.cipherTemplateList.get(index).length; j++) {
                    if (genericStringToArray[i] == this.cipherTemplateList.get(index)[j]) {
                        if (index == 1) {
                            originalDataCharsM2Index[i] = j;
                            //////System.out.println("From M2, positions" + j);
                        }
                    }
                }
            }
        }
        /* Add both Indices from M1 and M2 cipher templates
           if result is more tahn 255 , sutract 255 from it that will give new index
        */
        int[] dataCharsFinalIndex = new int[genericStringToArray.length];
        for (int pos = 0; pos < genericStringToArray.length; pos++) {
            //////System.out.println("Adding index values" + fixedDataCharsFromM1Index[pos]+ " and " + originalDataCharsM2Index[pos]);
            dataCharsFinalIndex[pos] = fixedDataCharsFromM1Index[pos] + originalDataCharsM2Index[pos];
            if (dataCharsFinalIndex[pos] >= this.ASDwead) {
                dataCharsFinalIndex[pos] = dataCharsFinalIndex[pos] - this.ASDwead;
            }
            //////System.out.println("Final Index = "+ dataCharsFinalIndex[pos]);
            cipherBuffer.append(this.cipherTemplateList.get(1)[dataCharsFinalIndex[pos]]);
        }
        ////System.out.println("Encrypted PIN=" + cipherBuffer);
        // add knights tour as chars from reverse ST
        for (int i = 0; i < this.knightMoveNumbers.length; i++) {
            cipherBuffer.append(AllCharacter.getInstance().getCharacterReverseArray()[this.knightMoveNumbers[i]]);
            ////System.out.println(this.knightMoveNumbers[i] + ",");
        }
        ////System.out.println("Final Encrypted Data=" + cipherBuffer);
        return cipherBuffer.toString();
    }

    /*
    Function to encrypt generic data up to M2.
    The M2 encrypt would be posted to LokDOn User registration and edit profile
    However, Password would be encrypted upto M5.
    The function would receive a string data and will return encoded M2 string
    */
    public String encryptGenericData(String genericString) {
        char[] genericStringToArray = genericString.toCharArray();
        //create random number list of number of moves of night
        this.generateKnightMovePositions(gjfghj); // M1 and M2 only
        // now initialize array list of walks and store in knightWalkNumberList
        this.createKnightWalkNumberStore();
        ////System.out.println("Total Knight Walks for Encrypting Generic String = "+ this.knightWalkNumberList.size());
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

        int[] fixedDataCharsFromM1Index = new int[genericStringToArray.length];
        int[] originalDataCharsM2Index = new int[genericStringToArray.length];
        // place holders
        StringBuffer cipherBuffer = new StringBuffer();
        for (int index = 0; index < this.cipherTemplateList.size(); index++) {
            ////System.out.println(" in M"+index+" orignal index of generic Data chars is as : ");
            if (index == 0) {
                // take index from Knight tour matrix of M1 in decresing order 255..254..253..>
                int travelling = this.vbnv - genericStringToArray.length;
                ////System.out.println("Travelling backwards for "+travelling);
                int i = 0;
                for (int x = this.vbnv - 1; x >= travelling; x--) {
                    fixedDataCharsFromM1Index[i] = this.knightWalkNumberList.get(index)[x];
                    ////System.out.println("From M1, positions" +fixedDataCharsFromM1Index[i] );
                    i++;
                }
            }
            for (int i = 0; i < genericStringToArray.length; i++) {

                // from m2, collect data chars position
                for (int j = 0; j < this.cipherTemplateList.get(index).length; j++) {
                    if (genericStringToArray[i] == this.cipherTemplateList.get(index)[j]) {
                        if (index == 1) {
                            originalDataCharsM2Index[i] = j;
                            ////System.out.println("From M2, positions" + j);
                        }
                    }
                }
            }
        }
        /* Add both Indices from M1 and M2 cipher templates
           if result is more tahn 255 , sutract 255 from it that will give new index
        */
        int[] dataCharsFinalIndex = new int[genericStringToArray.length];
        for (int pos = 0; pos < genericStringToArray.length; pos++) {
            ////System.out.println("Adding index values" + fixedDataCharsFromM1Index[pos]+ " and " + originalDataCharsM2Index[pos]);
            dataCharsFinalIndex[pos] = fixedDataCharsFromM1Index[pos] + originalDataCharsM2Index[pos];
            if (dataCharsFinalIndex[pos] >= this.ASDwead) {
                dataCharsFinalIndex[pos] = dataCharsFinalIndex[pos] - this.ASDwead;
            }
            ////System.out.println("Final Index = "+ dataCharsFinalIndex[pos]);
            cipherBuffer.append(this.cipherTemplateList.get(1)[dataCharsFinalIndex[pos]]);
        }
        ////System.out.println("Encrypted PIN=" + cipherBuffer);
        // add knights tour as chars from reverse ST
        for (int i = 0; i < this.knightMoveNumbers.length; i++) {
            cipherBuffer.append(AllCharacter.getInstance().getCharacterReverseArray()[this.knightMoveNumbers[i]]);
            ////System.out.println(this.knightMoveNumbers[i] + ",");
        }
        ////System.out.println("Final Encrypted Data=" + cipherBuffer);
        return cipherBuffer.toString();
    }
    public String encryptGenericData(String genericString, String salt) {
        char[] genericStringToArray = genericString.toCharArray();
        //create random number list of number of moves of night
        this.generateKnightMovePositions(gjfghj); // M1 and M2 only
        // now initialize array list of walks and store in knightWalkNumberList
        this.createKnightWalkNumberStore();
        ////System.out.println("Total Knight Walks for Encrypting Generic String = "+ this.knightWalkNumberList.size());
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

        int[] fixedDataCharsFromM1Index = new int[genericStringToArray.length];
        int[] originalDataCharsM2Index = new int[genericStringToArray.length];
        // place holders
        StringBuffer cipherBuffer = new StringBuffer();
        for (int index = 0; index < this.cipherTemplateList.size(); index++) {
            ////System.out.println(" in M"+index+" orignal index of generic Data chars is as : ");
            if (index == 0) {
                // take index from Knight tour matrix of M1 in decresing order 255..254..253..>
                int travelling = this.vbnv - genericStringToArray.length;
                ////System.out.println("Travelling backwards for "+travelling);
                int i = 0;
                for (int x = this.vbnv - 1; x >= travelling; x--) {
                    fixedDataCharsFromM1Index[i] = this.knightWalkNumberList.get(index)[x];
                    ////System.out.println("From M1, positions" +fixedDataCharsFromM1Index[i] );
                    i++;
                }
            }
            for (int i = 0; i < genericStringToArray.length; i++) {

                // from m2, collect data chars position
                for (int j = 0; j < this.cipherTemplateList.get(index).length; j++) {
                    if (genericStringToArray[i] == this.cipherTemplateList.get(index)[j]) {
                        if (index == 1) {
                            originalDataCharsM2Index[i] = j;
                            ////System.out.println("From M2, positions" + j);
                        }
                    }
                }
            }
        }
        /* Add both Indices from M1 and M2 cipher templates
           if result is more tahn 255 , sutract 255 from it that will give new index
        */
        int[] dataCharsFinalIndex = new int[genericStringToArray.length];
        for (int pos = 0; pos < genericStringToArray.length; pos++) {
            ////System.out.println("Adding index values" + fixedDataCharsFromM1Index[pos]+ " and " + originalDataCharsM2Index[pos]);
            dataCharsFinalIndex[pos] = fixedDataCharsFromM1Index[pos] + originalDataCharsM2Index[pos];
            if (dataCharsFinalIndex[pos] >= this.ASDwead) {
                dataCharsFinalIndex[pos] = dataCharsFinalIndex[pos] - this.ASDwead;
            }
            ////System.out.println("Final Index = "+ dataCharsFinalIndex[pos]);
            cipherBuffer.append(this.cipherTemplateList.get(1)[dataCharsFinalIndex[pos]]);
        }
        ////System.out.println("Encrypted PIN=" + cipherBuffer);
        // add knights tour as chars from reverse ST
        for (int i = 0; i < this.knightMoveNumbers.length; i++) {
            cipherBuffer.append(AllCharacter.getInstance().getCharacterReverseArray()[this.knightMoveNumbers[i]]);
            ////System.out.println(this.knightMoveNumbers[i] + ",");
        }
        ////System.out.println("Final Encrypted Data=" + cipherBuffer);
        String buff=cipherBuffer.toString();
        return new String(xorWithKey(buff.getBytes(StandardCharsets.UTF_8),salt.getBytes(StandardCharsets.UTF_8)),StandardCharsets.UTF_8);
    }

    public String decryptGenericData(String encodedData, String salt) {
        //we have mpin encrypt with last 2 characters respresenting knight's move from reverse standard template
        // last two chars denotes the number of knight's move from reverse ST

        // find mpin components
        ArrayList<int[]> dataKnightMovesNumberList = new ArrayList<int[]>();
        ArrayList<char[]> dataKnightMovesStringList = new ArrayList<char[]>();
        StringBuffer returnBuffer = new StringBuffer();

        String[] dataBuffer = getSecurityPinComponents(encodedData);
        ////System.out.println("Knight Moves For Generic Data=" + dataBuffer[0]);
        ////System.out.println("Generic Data Array=" + dataBuffer[1]);

        // we have knight moves and mpin encrypt in mpin buffer
        // find the knight moves from reverse standard template
        int[] dataKnightMoves = new int[dataBuffer[0].length()];
        char[] dataKnightChars = new char[dataBuffer[0].length()];
        //append knight moves char
        dataKnightChars = dataBuffer[0].toCharArray();
        ////System.out.println();
        //System.out.print("Dec Knight Moves = ");
        for (int i = 0; i < dataKnightMoves.length; i++) {
            for (int j = 0; j < AllCharacter.getInstance().getCharacterReverseArray().length; j++) {
                if (dataKnightChars[i] == AllCharacter.getInstance().getCharacterReverseArray()[j]) {
                    dataKnightMoves[i] = j;
                    //System.out.print(j+",");
                }
            }
        }
        ////System.out.println("mpin knight moves" +dataKnightMoves[0] + "," +dataKnightMoves[1]);

        // we had encrypted mpin on M2 template , iterate over m1 and m2
        // Generating knight move using given knight move numbers

        for (int i = 0; i < dataKnightMoves.length; i++) {
            KnightSolver solver = new KnightSolver(KNIGHT_MATRIX_WIDTH, KNIGHT_MATRIX_HEIGHT);
            solver.setPosition(dataKnightMoves[i] % KNIGHT_MATRIX_WIDTH, dataKnightMoves[i] / KNIGHT_MATRIX_HEIGHT);
            int[] temp = solver.loopSolver();
            dataKnightMovesNumberList.add(temp);
            solver.reset();
            solver = null;
        }

        // Creating m1 and m2 array using knight moves
        for (int index = 0; index < dataKnightMovesNumberList.size(); index++) {
            char[] tempArr = new char[(KNIGHT_MATRIX_WIDTH * KNIGHT_MATRIX_HEIGHT)];
            //System.out.print("M"+(index+1)+"  --  ");
            for (int i = 0; i < dataKnightMovesNumberList.get(index).length; i++) {
                tempArr[i] = AllCharacter.getInstance().getStandardCharacterSet()[dataKnightMovesNumberList.get(index)[i]];
                ////System.out.print(AllCharacter.getInstance().getStandardCharacterSet()[knightMovesNumberList.get(index)[i]]+",");
            }

            ////System.out.println();
            dataKnightMovesStringList.add(tempArr);
            tempArr = null;
        }

        // Logic to decrypt Data Ciphering
        // read index of current incoming encrypt from m2. Read index in decresing order from knightMovesSringList
        // match character in m2 cipher list
        char[] encryptedDataArray = new char[dataBuffer[1].length()];
        encryptedDataArray = dataBuffer[1].toCharArray();
        int[] encryptedDataIndexFromM2 = new int[dataBuffer[1].length()];
        int[] indexValuesFromM1 = new int[dataBuffer[1].length()];
        for (int index = dataKnightMovesStringList.size() - 1; index >= 0; index--) {
            if (index == 1) {//M2
                for (int i = 0; i < dataBuffer[index].length(); i++) {
                    for (int j = 0; j < dataKnightMovesStringList.get(index).length; j++) {
                        if (encryptedDataArray[i] == dataKnightMovesStringList.get(index)[j]) {
                            encryptedDataIndexFromM2[i] = j;
                            ////System.out.println("Index Found From M2 = "+j);
                        }

                    }
                }
            }
            if (index == 0) {
                // for m1 , get index values from last
                // take index from Knight tour matrix of M1 in decresing order 255..254..253..>
                int travelling = this.vbnv - encryptedDataArray.length;
                ////System.out.println("Travelling backwards for "+travelling);
                int i = 0;
                for (int x = this.vbnv - 1; x >= travelling; x--) {
                    indexValuesFromM1[i] = dataKnightMovesNumberList.get(index)[x];
                    ////System.out.println("From M1, positions" +indexValuesFromM1[i] );
                    i++;
                }
            }
        }
        // we have to add 256 in M2 Index and subtract m1 index to get actual index on M2
        char[] actualData = new char[encryptedDataArray.length];
        for (int i = 0; i < encryptedDataArray.length; i++) {
            encryptedDataIndexFromM2[i] = encryptedDataIndexFromM2[i] + this.ASDwead;
            encryptedDataIndexFromM2[i] = encryptedDataIndexFromM2[i] - indexValuesFromM1[i];
            if (encryptedDataIndexFromM2[i] >= this.ASDwead) {
                encryptedDataIndexFromM2[i] = encryptedDataIndexFromM2[i] - this.ASDwead;
            }
            ////System.out.println("Final positions = " +  encryptedDataIndexFromM2[i]);
            // Fetch Chars at new M2 Index , that will be decoded data
            actualData[i] = dataKnightMovesStringList.get(1)[encryptedDataIndexFromM2[i]];
            ////System.out.println("Final Data = "+actualData[i]);
        }
        ////System.out.println();
        StringBuffer buff = new StringBuffer();
        buff.append(actualData);
        ////System.out.println("Original Data=" + buff.toString());
        return buff.toString();

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

        String[] dataBuffer = getSecurityPinComponents(encodedData);
        ////System.out.println("Knight Moves For Generic Data=" + dataBuffer[0]);
        ////System.out.println("Generic Data Array=" + dataBuffer[1]);

        // we have knight moves and mpin encrypt in mpin buffer
        // find the knight moves from reverse standard template
        int[] dataKnightMoves = new int[dataBuffer[0].length()];
        char[] dataKnightChars = new char[dataBuffer[0].length()];
        //append knight moves char
        dataKnightChars = dataBuffer[0].toCharArray();
        ////System.out.println();
        //System.out.print("Dec Knight Moves = ");
        for (int i = 0; i < dataKnightMoves.length; i++) {
            for (int j = 0; j < AllCharacter.getInstance().getCharacterReverseArray().length; j++) {
                if (dataKnightChars[i] == AllCharacter.getInstance().getCharacterReverseArray()[j]) {
                    dataKnightMoves[i] = j;
                    //System.out.print(j+",");
                }
            }
        }
        ////System.out.println("mpin knight moves" +dataKnightMoves[0] + "," +dataKnightMoves[1]);

        // we had encrypted mpin on M2 template , iterate over m1 and m2
        // Generating knight move using given knight move numbers

        for (int i = 0; i < dataKnightMoves.length; i++) {
            KnightSolver solver = new KnightSolver(KNIGHT_MATRIX_WIDTH, KNIGHT_MATRIX_HEIGHT);
            solver.setPosition(dataKnightMoves[i] % KNIGHT_MATRIX_WIDTH, dataKnightMoves[i] / KNIGHT_MATRIX_HEIGHT);
            int[] temp = solver.loopSolver();
            dataKnightMovesNumberList.add(temp);
            solver.reset();
            solver = null;
        }

        // Creating m1 and m2 array using knight moves
        for (int index = 0; index < dataKnightMovesNumberList.size(); index++) {
            char[] tempArr = new char[(KNIGHT_MATRIX_WIDTH * KNIGHT_MATRIX_HEIGHT)];
            //System.out.print("M"+(index+1)+"  --  ");
            for (int i = 0; i < dataKnightMovesNumberList.get(index).length; i++) {
                tempArr[i] = AllCharacter.getInstance().getStandardCharacterSet()[dataKnightMovesNumberList.get(index)[i]];
                ////System.out.print(AllCharacter.getInstance().getStandardCharacterSet()[knightMovesNumberList.get(index)[i]]+",");
            }

            ////System.out.println();
            dataKnightMovesStringList.add(tempArr);
            tempArr = null;
        }

        // Logic to decrypt Data Ciphering
        // read index of current incoming encrypt from m2. Read index in decresing order from knightMovesSringList
        // match character in m2 cipher list
        char[] encryptedDataArray = new char[dataBuffer[1].length()];
        encryptedDataArray = dataBuffer[1].toCharArray();
        int[] encryptedDataIndexFromM2 = new int[dataBuffer[1].length()];
        int[] indexValuesFromM1 = new int[dataBuffer[1].length()];
        for (int index = dataKnightMovesStringList.size() - 1; index >= 0; index--) {
            if (index == 1) {//M2
                for (int i = 0; i < dataBuffer[index].length(); i++) {
                    for (int j = 0; j < dataKnightMovesStringList.get(index).length; j++) {
                        if (encryptedDataArray[i] == dataKnightMovesStringList.get(index)[j]) {
                            encryptedDataIndexFromM2[i] = j;
                            ////System.out.println("Index Found From M2 = "+j);
                        }

                    }
                }
            }
            if (index == 0) {
                // for m1 , get index values from last
                // take index from Knight tour matrix of M1 in decresing order 255..254..253..>
                int travelling = this.vbnv - encryptedDataArray.length;
                ////System.out.println("Travelling backwards for "+travelling);
                int i = 0;
                for (int x = this.vbnv - 1; x >= travelling; x--) {
                    indexValuesFromM1[i] = dataKnightMovesNumberList.get(index)[x];
                    ////System.out.println("From M1, positions" +indexValuesFromM1[i] );
                    i++;
                }
            }
        }
        // we have to add 256 in M2 Index and subtract m1 index to get actual index on M2
        char[] actualData = new char[encryptedDataArray.length];
        for (int i = 0; i < encryptedDataArray.length; i++) {
            encryptedDataIndexFromM2[i] = encryptedDataIndexFromM2[i] + this.ASDwead;
            encryptedDataIndexFromM2[i] = encryptedDataIndexFromM2[i] - indexValuesFromM1[i];
            if (encryptedDataIndexFromM2[i] >= this.ASDwead) {
                encryptedDataIndexFromM2[i] = encryptedDataIndexFromM2[i] - this.ASDwead;
            }
            ////System.out.println("Final positions = " +  encryptedDataIndexFromM2[i]);
            // Fetch Chars at new M2 Index , that will be decoded data
            actualData[i] = dataKnightMovesStringList.get(1)[encryptedDataIndexFromM2[i]];
            ////System.out.println("Final Data = "+actualData[i]);
        }
        ////System.out.println();
        StringBuffer buff = new StringBuffer();
        buff.append(actualData);
        ////System.out.println("Original Data=" + buff.toString());
        return buff.toString();

    }

    public String encryptTempSecurityPin() {
        Random rand = new Random();
        String mpin = String.format("%04d", rand.nextInt(10000));
        ////System.out.println("Random MPIN=" + mpin);
        int[] leftBorder = new int[]{0, 16, 32, 48, 64, 80, 96, 112, 128, 144, 160, 176, 192, 208, 224, 240};
        int[] rightBorder = new int[]{15, 31, 47, 63, 79, 95, 111, 127, 143, 159, 175, 191, 207, 223, 239, 255};
        char[] mpinArray = mpin.toCharArray();

        //create random number list of number of moves of night
        this.generateKnightMovePositions(MAX_MPIN_TEMP_TOUR);
        // now initialize array list of walks and store in knightWalkNumberList
        this.createKnightWalkNumberStore();
        ////System.out.println("Total Knight Walks for PIN = "+ this.knightWalkNumberList.size());
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
        // find the indices of our input PIN Digits in M3 and M4 and store in an array

        int[] originalPinIndex = new int[mpinArray.length];
        // place holders
        StringBuffer cipherBuffer = new StringBuffer();
        ////System.out.println("Cipher Templates List size="+this.cipherTemplateList.size());
        for (int index = 0; index < this.cipherTemplateList.size(); index++) {
            //System.out.print(" in M"+(index+1)+" orignal index of PIN is as : ");
            for (int i = 0; i < mpinArray.length; i++) {
                for (int j = 0; j < this.cipherTemplateList.get(index).length; j++) {
                    //////System.out.println("Comparing " +mpinArray[i]+", "+ this.cipherTemplateList.get(index)[j]) );
                    if (mpinArray[i] == this.cipherTemplateList.get(index)[j]) {
                        originalPinIndex[i] = j;
                        //System.out.print(originalPinIndex[i] + " , ");
                    }
                }
            }
            ////System.out.println();

            // we need to find out positions of mpin key in M3 and M4 only
            if (index == 2 || index == 3) {
                // check if we are in situation of boundaries in left or right array
                int pinPosition = 0;
                for (int mpos = 0; mpos < originalPinIndex.length; mpos++) {
                    ////System.out.println("searching for "+ originalPinIndex[mpos] + " in left Array");
                    pinPosition = this.getArrayIndexValue(leftBorder, originalPinIndex[mpos]);
                    if (pinPosition > -1) { // if index found
                        ////System.out.println("Match Found in Left Array index number:" +pinPosition);
                        // we got a mpin position in left array boundry
                        // 2 chars from left of the pinPosition from M2 should be taken as cipher char
                        cipherBuffer.append(this.cipherTemplateList.get(index)[pinPosition + 1]);
                        cipherBuffer.append(this.cipherTemplateList.get(index)[pinPosition + 2]);
                    } else {
                        //
                        ////System.out.println("searching for "+ originalPinIndex[mpos] + "in RIGHT Array");
                        pinPosition = this.getArrayIndexValue(rightBorder, originalPinIndex[mpos]);
                        if (pinPosition > -1) { // if index  found
                            ////System.out.println("Match Found in Right Array index number:" +pinPosition);
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
        ////System.out.println("Encrypted PIN=" + cipherBuffer);
        // add knights tour as chars from reverse ST
        for (int i = 0; i < this.knightMoveNumbers.length; i++) {
            cipherBuffer.append(AllCharacter.getInstance().getCharacterReverseArray()[this.knightMoveNumbers[i]]);
            ////System.out.println(this.knightMoveNumbers[i] + ",");
        }
        ////System.out.println("Final Encrypted PIN=" + cipherBuffer);
        // format returned is M3M4KTn
        return cipherBuffer.toString();
    }

    /*
    Function to decrypt temp MPIN for Lokdon
    */
    public String decryptTempSecurityPin(String mpinEncrypt) {
        //we have mpin encrypt with last 5 characters respresenting knight's move from reverse standard template
        // last two chars denotes the number of knight's move from reverse ST
        // before last 5 chars all M4 (10 chars) and M3(10 chars)
        // find mpin components
        ArrayList<int[]> mpinKnightMovesNumberList = new ArrayList<int[]>();
        ArrayList<char[]> mpinKnightMovesStringList = new ArrayList<char[]>();
        StringBuffer returnBuffer = new StringBuffer();

        String[] mpinBuffer = this.getTempSecurityPinComponents(mpinEncrypt);
        ////System.out.println("Knight Moves For MPIN=" + mpinBuffer[0]);
        ////System.out.println("MPIN Password Array=" + mpinBuffer[1]);

        // we have knight moves and mpin encrypt in mpin buffer
        // find the knight moves from reverse standard template
        int[] mpinKnightMoves = new int[mpinBuffer[0].length()];
        char[] mpinKnightChars = new char[mpinBuffer[0].length()];
        //append knight moves char
        mpinKnightChars = mpinBuffer[0].toCharArray();
        ////System.out.println();
        //System.out.print("Dec Knight Moves = ");
        for (int i = 0; i < mpinKnightMoves.length; i++) {
            for (int j = 0; j < AllCharacter.getInstance().getCharacterReverseArray().length; j++) {
                if (mpinKnightChars[i] == AllCharacter.getInstance().getCharacterReverseArray()[j]) {
                    mpinKnightMoves[i] = j;
                    //System.out.print(j+",");
                }
            }
        }
        //////System.out.println("mpin knight moves" +mpinKnightMoves[0] + "," +mpinKnightMoves[1]);

        // we had encrypted mpin on M3-M4 template , iterate over m3 and m4
        // Generating knight move using given knight move numbers
        for (int i = 0; i < mpinKnightMoves.length; i++) {
            KnightSolver solver = new KnightSolver(KNIGHT_MATRIX_WIDTH, KNIGHT_MATRIX_HEIGHT);
            solver.setPosition(mpinKnightMoves[i] % KNIGHT_MATRIX_WIDTH, mpinKnightMoves[i] / KNIGHT_MATRIX_HEIGHT);
            int[] temp = solver.loopSolver();
            mpinKnightMovesNumberList.add(temp);
            solver.reset();
            solver = null;
        }

        // Creating m3 and m4 array using knight moves
        for (int index = 0; index < mpinKnightMovesNumberList.size(); index++) {
            char[] tempArr = new char[(KNIGHT_MATRIX_WIDTH * KNIGHT_MATRIX_HEIGHT)];
            //System.out.print("M"+(index+1)+"  --  ");
            for (int i = 0; i < mpinKnightMovesNumberList.get(index).length; i++) {
                tempArr[i] = AllCharacter.getInstance().getStandardCharacterSet()[mpinKnightMovesNumberList.get(index)[i]];
                ////System.out.print(AllCharacter.getInstance().getStandardCharacterSet()[knightMovesNumberList.get(index)[i]]+",");
            }

            ////System.out.println();
            mpinKnightMovesStringList.add(tempArr);
            tempArr = null;
        }

        //now we have full matrix of M1 to  M5 above
        char[] originalSecurityPin = new char[mpinBuffer[1].length()];
        originalSecurityPin = mpinBuffer[1].toCharArray();
        int[] originalPinCharIndex = new int[mpinBuffer[1].length()];
        ////System.out.println("Length of mpinKnightMovesStringList="+mpinKnightMovesStringList.size());

        /* Make sure we are checking MPIN encrypted chars only in M2 */
        /* Logic to decrypt MPIN encrypt, below
        1. take two chars from last each time until password is there,
        2. find their positions in M2, Find the differnce of postions of the chars
        3. if position 1 < postion 2, its a left boundary case, take char at Position + 1
        4. if position 1 > postion 2, its a right boundary case or left-right condition
        4.a if position 1 - positions == 2 , left right condition achieved, char = (position1 + position2) / 2
        4.b else its right position , char = postion 1-1
        */
        ////System.out.println("mpin clubbed with M3-M4 = " +mpinBuffer[1]);
        String lastTwoChars;

        int[] positions = new int[2];
        for (int index = mpinKnightMovesStringList.size() - 1; index >= 0; index--) {
            // for M4
            if (index == 3) {
                boolean shouldContinue = true;
                while (shouldContinue) {
                    // iterate over string and take 2 chars from atleast each time, as we have 8 chars mpin
                    lastTwoChars = mpinBuffer[1].substring(mpinBuffer[1].length() - 2);
                    ////System.out.println("Last Two Chars = "+lastTwoChars);
                    char[] twoCharsFromMpin = new char[lastTwoChars.length()];
                    twoCharsFromMpin = lastTwoChars.toCharArray();
                    //start from last character
                    ////System.out.println("Last Two Chars length= " + lastTwoChars.length());
                    for (int i = lastTwoChars.length() - 1; i >= 0; i--) {
                        for (int j = 0; j < mpinKnightMovesStringList.get(index).length; j++) {
                            //////System.out.println("I = "+i+",j="+j);
                            if (twoCharsFromMpin[i] == mpinKnightMovesStringList.get(index)[j]) {
                                positions[i] = j;
                                ////System.out.print("Positions = "+j+",");
                                break;
                            }
                        }
                    }
                    ////System.out.println("Last Char Position="+ positions[1]);
                    ////System.out.println("Second Last Char Position="+ positions[0]);

                    if (positions[1] < positions[0]) {
                        ////System.out.println("Left Pos Char would be"+ mpinKnightMovesStringList.get(index)[positions[0]+1]);
                        returnBuffer.append(mpinKnightMovesStringList.get(index)[positions[0] + 1]);
                    } else if (positions[1] > positions[0]) {
                        if (positions[1] - positions[0] == 2) {
                            ////System.out.println("Mid Char would be"+ mpinKnightMovesStringList.get(index)[(positions[0]+positions[1])/2]);
                            returnBuffer.append(mpinKnightMovesStringList.get(index)[(positions[0] + positions[1]) / 2]);
                        } else {
                            ////System.out.println("Right Char would be"+ mpinKnightMovesStringList.get(index)[positions[0]+1]);
                            returnBuffer.append(mpinKnightMovesStringList.get(index)[positions[0] - 1]);
                        }
                    }
                    mpinBuffer[1] = mpinBuffer[1].substring(0, mpinBuffer[1].length() - 2);
                    ////System.out.println("Buffer = "+mpinBuffer[1]);
                    shouldContinue = mpinBuffer[1].length() > 8;

                }
            }
            ////System.out.println();
        }
        ////System.out.println("Data="+ returnBuffer.reverse().toString());
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
        this.generateKnightMovePositions(dfhdfh);
        // now initialize array list of walks and store in knightWalkNumberList
        this.createKnightWalkNumberStore();
        ////System.out.println("Total Knight Walks for PIN = "+ this.knightWalkNumberList.size());
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

        int[] originalPinIndex = new int[mpinArray.length];
        // place holders
        StringBuffer cipherBufferForM2 = new StringBuffer();
        StringBuffer cipherBufferForM3 = new StringBuffer();

        for (int index = 0; index < this.cipherTemplateList.size(); index++) {
            //System.out.print(" in M1 orignal index of PIN is as : ");
            for (int i = 0; i < mpinArray.length; i++) {
                for (int j = 0; j < this.cipherTemplateList.get(index).length; j++) {
                    //////System.out.println("Comparing " +mpinArray[i]+", "+ this.cipherTemplateList.get(index)[j]) );
                    if (mpinArray[i] == this.cipherTemplateList.get(index)[j]) {
                        originalPinIndex[i] = j;
                        //System.out.print(originalPinIndex[i] + " , ");
                    }
                }
            }
            ////System.out.println();

            // we need to find out positions of mpin key in M2 and M3
            if (index == 1) {
                // check if we are in situation of boundaries in left or right array
                int pinPosition = 0;
                for (int mpos = 0; mpos < originalPinIndex.length; mpos++) {
                    ////System.out.println("searching for "+ originalPinIndex[mpos] + " in left Array");
                    pinPosition = this.getArrayIndexValue(leftBorder, originalPinIndex[mpos]);
                    if (pinPosition > -1) { // if index found
                        ////System.out.println("Match Found in Left Array index number:" +pinPosition);
                        // we got a mpin position in left array boundry
                        // 2 chars from left of the pinPosition from M2 should be taken as cipher char
                        cipherBufferForM2.append(this.cipherTemplateList.get(index)[pinPosition + 1]);
                        cipherBufferForM2.append(this.cipherTemplateList.get(index)[pinPosition + 2]);
                    } else {
                        //
                        ////System.out.println("searching for "+ originalPinIndex[mpos] + "in RIGHT Array");
                        pinPosition = this.getArrayIndexValue(rightBorder, originalPinIndex[mpos]);
                        if (pinPosition > -1) { // if index  found
                            ////System.out.println("Match Found in Right Array index number:" +pinPosition);
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
                    ////System.out.println("searching for "+ originalPinIndex[mpos] + " in left Array");
                    pinPosition = this.getArrayIndexValue(leftBorder, originalPinIndex[mpos]);
                    if (pinPosition > -1) { // if index found
                        ////System.out.println("Match Found in Left Array index number:" +pinPosition);
                        // we got a mpin position in left array boundry
                        // 2 chars from left of the pinPosition from M2 should be taken as cipher char
                        cipherBufferForM3.append(this.cipherTemplateList.get(index)[pinPosition + 1]);
                        cipherBufferForM3.append(this.cipherTemplateList.get(index)[pinPosition + 2]);
                    } else {
                        //
                        ////System.out.println("searching for "+ originalPinIndex[mpos] + "in RIGHT Array");
                        pinPosition = this.getArrayIndexValue(rightBorder, originalPinIndex[mpos]);
                        if (pinPosition > -1) { // if index  found
                            ////System.out.println("Match Found in Right Array index number:" +pinPosition);
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
        ////System.out.println("Encrypted PIN in M2=" + cipherBufferForM2);
        ////System.out.println("Encrypted PIN in M3=" + cipherBufferForM3);
        // add knights tour as chars from reverse ST
        for (int i = 0; i < this.knightMoveNumbers.length; i++) {
            cipherBufferForM2.append(AllCharacter.getInstance().getCharacterReverseArray()[this.knightMoveNumbers[i]]);
            ////System.out.println(this.knightMoveNumbers[i] + ",");
        }
        ////System.out.println("Final Encrypted M2 PIN=" + cipherBufferForM2);
        ////System.out.println("Final Encrypted M3 PIN=" + cipherBufferForM3);
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
        ////System.out.println("The decrypted Pin = "+decryptedPin);
        return decryptedPin;
    }

    /*
    Function to decrypt a given encrypted PIN with a given single position
    */
    public String decryptMpinFromSinglePosition(String mpin) {
        // last char is always KT. Combine last char of M2 KT3 with M3
        // input will be mpin = M3+KT3 from M2KT1KT2KT3
        char lastChar = mpin.charAt(mpin.length() - 1);
        // rest 8 chars are encrypt of PIN
        if (mpin != null && mpin.length() > 0) {
            mpin = mpin.substring(0, mpin.length() - 1);
        }
        ArrayList<char[]> mpinKnightMovesStringList = new ArrayList<char[]>();
        ////System.out.println("The Encrypt = " +mpin);
        int mpinKnightMove = 0;
        ArrayList<int[]> mpinKnightMovesNumberList = new ArrayList<int[]>();
        StringBuffer returnBuffer = new StringBuffer();
        // its knights move character from reversed ST
        for (int j = 0; j < AllCharacter.getInstance().getCharacterReverseArray().length; j++) {
            if (lastChar == AllCharacter.getInstance().getCharacterReverseArray()[j]) {
                mpinKnightMove = j;
            }
        }
        ////System.out.println("The Knight Moves is = "+mpinKnightMove);

        KnightSolver solver = new KnightSolver(KNIGHT_MATRIX_WIDTH, KNIGHT_MATRIX_HEIGHT);
        solver.setPosition(mpinKnightMove % KNIGHT_MATRIX_WIDTH, mpinKnightMove / KNIGHT_MATRIX_HEIGHT);
        int[] temp = solver.loopSolver();
        mpinKnightMovesNumberList.add(temp);
        solver.reset();
        solver = null;
        // Creating cipher array using knight moves
        for (int index = 0; index < mpinKnightMovesNumberList.size(); index++) {
            char[] tempArr = new char[(KNIGHT_MATRIX_WIDTH * KNIGHT_MATRIX_HEIGHT)];
            //System.out.print("M"+(index+1)+"  --  ");
            for (int i = 0; i < mpinKnightMovesNumberList.get(index).length; i++) {
                tempArr[i] = AllCharacter.getInstance().getStandardCharacterSet()[mpinKnightMovesNumberList.get(index)[i]];
                ////System.out.print(AllCharacter.getInstance().getStandardCharacterSet()[knightMovesNumberList.get(index)[i]]+",");
            }
            mpinKnightMovesStringList.add(tempArr);
            tempArr = null;
        }
        //now we have full matrix of Mn
        char[] originalSecurityPin = new char[mpin.length()];
        originalSecurityPin = mpin.toCharArray();
        int[] originalPinCharIndex = new int[mpin.length()];
        ////System.out.println("Length of mpinKnightMovesStringList="+mpinKnightMovesStringList.size());
        ////System.out.println("mpin 1 = " +mpin);

        String lastTwoChars;
        int[] positions = new int[2];
        boolean shouldContinue = true;
        for (int index = mpinKnightMovesStringList.size() - 1; index >= 0; index--) {
            while (shouldContinue) {
                // iterate over string and take 2 chars from alst each time, as we have 8 chars mpin
                lastTwoChars = mpin.substring(mpin.length() - 2);
                ////System.out.println("Last Two Chars = "+lastTwoChars);
                char[] twoCharsFromMpin = new char[lastTwoChars.length()];
                twoCharsFromMpin = lastTwoChars.toCharArray();
                //start from last character
                ////System.out.println("Last Two Chars length= " + lastTwoChars.length());
                for (int i = lastTwoChars.length() - 1; i >= 0; i--) {
                    for (int j = 0; j < mpinKnightMovesStringList.get(index).length; j++) {
                        //////System.out.println("I = "+i+",j="+j);
                        if (twoCharsFromMpin[i] == mpinKnightMovesStringList.get(index)[j]) {
                            positions[i] = j;
                            ////System.out.print("Positions = "+j+",");
                            break;
                        }
                    }
                }
                ////System.out.println("Last Char Position="+ positions[1]);
                ////System.out.println("Second Last Char Position="+ positions[0]);

                if (positions[1] < positions[0]) {
                    ////System.out.println("Left Pos Char would be"+ mpinKnightMovesStringList.get(index)[positions[0]+1]);
                    returnBuffer.append(mpinKnightMovesStringList.get(index)[positions[0] + 1]);
                } else if (positions[1] > positions[0]) {
                    if (positions[1] - positions[0] == 2) {
                        ////System.out.println("Mid Char would be"+ mpinKnightMovesStringList.get(index)[(positions[0]+positions[1])/2]);
                        returnBuffer.append(mpinKnightMovesStringList.get(index)[(positions[0] + positions[1]) / 2]);
                    } else {
                        ////System.out.println("Right Char would be"+ mpinKnightMovesStringList.get(index)[positions[0]+1]);
                        returnBuffer.append(mpinKnightMovesStringList.get(index)[positions[0] - 1]);
                    }
                }
                mpin = mpin.substring(0, mpin.length() - 2);
                ////System.out.println("Remaining MPIN Buffer = "+mpin);
                if (mpin.length() <= 0) {
                    shouldContinue = false;
                }
            }
        }
        ////System.out.println();
        return returnBuffer.reverse().toString();
    }

    public String decrypt(String message, String key) {
        //message=message.substring(0,key.length());
        StringBuffer buffer = new StringBuffer();
        try {
            ////System.out.println(" message  " + message);
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

                    //System.out.printf("got the new index " + moveIndex);

                }
            }


            String moveChar = message.substring(message.length() - (moveIndex + 1), message.length() - 1);
            ////System.out.println(" Encrypt Data :  " + message.substring(0, message.length() - (moveIndex + 1)));
            char[] chiperCharArray = message.substring(0, message.length() - (moveIndex + 1)).toCharArray();

            ////System.out.println(" Move Character :::  " + moveChar);
            char[] moveArray = moveChar.toCharArray();
            int[] moveIndexArray = new int[moveArray.length];
            for (int j = 0; j < moveArray.length; j++) {
                for (int i = 0; i < AllCharacter.getInstance().getCharacterReverseArray().length; i++) {

                    if (moveArray[j] == AllCharacter.getInstance().getCharacterReverseArray()[i]) {
                        //System.out.print(i + ",");
                        moveIndexArray[j] = i;
                        break;
                    }
                }
            }
            ////System.out.println();

            // Generating knight move using random number
            for (int i = 0; i < moveIndexArray.length; i++) {
                KnightSolver solver = new KnightSolver(KNIGHT_MATRIX_WIDTH, KNIGHT_MATRIX_HEIGHT);
                solver.setPosition(moveIndexArray[i] % KNIGHT_MATRIX_WIDTH, moveIndexArray[i] / KNIGHT_MATRIX_HEIGHT);
                int[] temp = solver.loopSolver();
                moveNumberList.add(temp);
                solver.reset();
                solver = null;
            }


            // Creating new character array using knight moves
            for (int index = 0; index < moveNumberList.size(); index++) {
                char[] tempArr = new char[(KNIGHT_MATRIX_WIDTH * KNIGHT_MATRIX_HEIGHT)];
                //System.out.print("M" + (index + 1) + "  --  ");
                for (int i = 0; i < moveNumberList.get(index).length; i++) {
                    tempArr[i] = AllCharacter.getInstance().getStandardCharacterSet()[moveNumberList.get(index)[i]];
                    //System.out.print("&&&!" + AllCharacter.getInstance().getStandardCharacterSet()[moveNumberList.get(index)[i]] + ",");
                }

                ////System.out.println();
                moveStringList.add(tempArr);
                tempArr = null;
            }


            ////System.out.println();
            int[] chiperValueArray = new int[chiperCharArray.length];
            for (int i = 0; i < chiperCharArray.length; i++) {
                for (int j = 0; j < moveStringList.get(0).length; j++) {
                    if (chiperCharArray[i] == moveStringList.get(0)[j]) {
                        chiperValueArray[i] = j;
                        //System.out.print(j + ",");
                    }
                }
            }


            ////System.out.println(" Decrypt value ");
            for (int i = 0; i < chiperValueArray.length; i++) {
                //System.out.print(AllCharacter.getInstance().getStandardCharacterSet()[chiperValueArray[i]]);
                buffer.append(AllCharacter.getInstance().getStandardCharacterSet()[chiperValueArray[i]]);
            }
            ////System.out.println("");
        } catch (Exception e) {
            buffer.append(message);
        }


//        int moveIndex = message.charAt(message.length()-1);

        return buffer.toString();
    }

    /*
     * Signs the payload with shadowUWA
     * @return base64 signed string (token)
     * @param payload: payload data (encoded json)
     * @param shadowUWA: shadow UWA (without position)
     */
    public String[] signPayloadWithShadowUWA(String header, String payload, String shadowUWA) {
        try {
            payload = encryptGenericData(payload);
            shadowUWA = getRandomUWAShadow(shadowUWA);
            String headerData = Base64.getEncoder().encodeToString(header.getBytes());
            byte[] payloadData = payload.getBytes();
            byte[] shadowUWAData = shadowUWA.getBytes();
            byte[] resultData = signWith(payloadData, shadowUWAData);

            String[] tokenParts = new String[3];
            tokenParts[0] = headerData;
            tokenParts[1] = Base64.getEncoder().encodeToString(resultData);
            tokenParts[2] = Base64.getEncoder().encodeToString(shadowUWAData);
            return tokenParts;
        } catch (Exception ex) {
            //
        }
        return null;
    }

    public String getRandomUWAShadow(String uwa) {
        int length = uwa.length();
        int pos = new Random().nextInt(length - 1);
        uwa += uwa;
        return uwa.substring(pos, pos + 11);
    }

    public void testSign() {
        try {
            String a = "BHE1HSsLDxQsLi0dKwQiWgRxNR0rCw8ULCA/KBU1WwA=";
            String b = "3kslslzislg";
            byte[] aBytes = Base64.getDecoder().decode(a);
            byte[] bBytes = Base64.getEncoder().encode(b.getBytes(StandardCharsets.UTF_8));

            byte[] signed = signWith(aBytes, bBytes);
            byte[] decoded = Base64.getDecoder().decode(signed);
            String decrypted = new CipherControl().decryptGenericData(new String(decoded, StandardCharsets.UTF_8));
            //System.out.println("decrypted output: " + decrypted);


            //byte[] unsigned=signWith(signed,Base64.getEncoder().encode(b.getBytes("UTF-8")));

            ////System.out.println("output: "+new CipherControl().decryptGenericData(new String(Base64.getDecoder().decode(unsigned),"UTF-8")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String generateToken(String UWA, String payload, long expirySeconds) throws Exception {
        try {
            String header = "expires: " + expirySeconds;
            int x = new Random().nextInt(1000);
            int pos = x % 42;
            int rightOrLeft = new Random().nextInt(10);
            boolean isRight = rightOrLeft % 2 == 0;
            //System.out.println("shadow position: " + pos + ", direction: " + (isRight ? "right" : "left"));
            String shadow = getUWAShadow(UWA, pos, isRight);//step #1
            payload = new CipherControl().encryptGenericData(payload);
            header = new CipherControl().encryptGenericData(header);
            //System.out.println("header before sign: " + header);
            //System.out.println("shadow before sign: " + shadow);
            byte[] payloadEncrypt = signWith(Base64.getEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8), Base64.getEncoder().encode(shadow.getBytes(StandardCharsets.UTF_8)));//step #4
            byte[] headerEncrypt = signWith(Base64.getEncoder().encodeToString(header.getBytes(StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8), Base64.getEncoder().encode(shadow.getBytes(StandardCharsets.UTF_8)));//step #5
            String uwaShadow = x + (isRight ? "R" : "L") + ":" + shadow;//step#3
            printBinary(headerEncrypt);
            //System.out.println("uwaShadow: " + uwaShadow);
            uwaShadow = encryptGenericData(uwaShadow);//step#6
            ////System.out.println("header encoded: "+new String(headerEncrypt));
            String tokenHeader = Base64.getEncoder().encodeToString(headerEncrypt);
            String tokenPayload = Base64.getEncoder().encodeToString(payloadEncrypt);
            String tokenCertificate = Base64.getEncoder().encodeToString(uwaShadow.getBytes(StandardCharsets.UTF_8));

            String token = tokenHeader + "." + tokenPayload + "." + tokenCertificate;
            //System.out.println("token: " + token);
            return token;
        } catch (Exception ex) {
            //
        }
        return null;
    }

    public String validateToken(String UWA, String token) throws Exception {
        try {
            String[] tokenComp = token.split("\\.");
            if (tokenComp.length != 3)
                return "Error: token in wrong format!";
            String shadowEnc = new String(Base64.getDecoder().decode(tokenComp[2]));
            String shadowPos = decryptGenericData(shadowEnc);
            //System.out.println("decrypted uwa shadow: " + shadowPos);
            String[] shadowComp = shadowPos.split(":");
            if (shadowComp.length != 2)
                return "Error: uwa shadow in wrong format!";
            String xPos = shadowComp[0];
            String shadow = shadowComp[1];
            if (xPos.contains("R")) {
                String pos = xPos.replace('R', ' ');
                pos = pos.trim();
                int position = Integer.parseInt(pos);
                position = position % 42;
                String originalShadow = getUWAShadow(UWA, position, true);
                if (!originalShadow.equals(shadow))
                    return "Error: invalid certificate/uwaShadow provided!";
            }
            if (xPos.contains("L")) {
                String pos = xPos.replace('L', ' ');
                pos = pos.trim();
                int position = Integer.parseInt(pos);
                position = position % 42;
                String originalShadow = getUWAShadow(UWA, position, false);
                if (!originalShadow.equals(shadow))
                    return "Error: invalid certificate/uwaShadow provided!";
            }
            //System.out.println("shadow: " + shadow);
            //shadow=decryptGenericData(shadow);
            //System.out.println("shadow: " + shadow + "\r\ntoken: " + tokenComp[0]);
            //String headerEnc=new String(Base64.getDecoder().decode(tokenComp[0]),"UTF-8");
            ////System.out.println("header decoded: "+headerEnc);
            byte[] headerEnc = Base64.getDecoder().decode(tokenComp[0]);
            printBinary(headerEnc);
            String header = new String(Base64.getDecoder().decode(signWith(headerEnc, Base64.getEncoder().encode(shadow.getBytes(StandardCharsets.UTF_8)))), StandardCharsets.UTF_8);
            //System.out.println("header unsigned: " + header);
            header = decryptGenericData(header);
            //System.out.println("header: " + header);
            String[] headerComp = header.split(":");
            if (headerComp.length != 2)
                return "Error: header is invalid!";
            Long time = Long.parseLong(headerComp[1].trim());
            Long current = Instant.now().getEpochSecond();
            Long diff = current - time;
            if (current>time) {
                return "Error: token expired";
            }
            byte[] payloadEnc = Base64.getDecoder().decode(tokenComp[1]);
            String payload = new String(Base64.getDecoder().decode(signWith(payloadEnc, Base64.getEncoder().encode(shadow.getBytes()))), StandardCharsets.UTF_8);
            return decryptGenericData(payload);
        } catch (Exception ex) {
            //
        }
        return "Error: invalid request";
    }

    private String getUWAShadow(String uwa, int pos, boolean isRight) {
        try {
            if (isRight) {
                uwa += uwa;
                return uwa.substring(pos, pos + 11);
            } else {
                uwa += uwa;
                uwa = new StringBuilder(uwa).reverse().toString();
                return uwa.substring(pos, pos + 11);
            }
        } catch (Exception ex) {
            //
        }
        return null;
    }

    private byte[] signWith(byte[] payloadData, byte[] shadowUWAData) {
        try {
            final byte[] output = new byte[payloadData.length];
            if (shadowUWAData.length == 0) {
                throw new IllegalArgumentException("empty security key");
            }
            int spos = 0;
            for (int pos = 0; pos < payloadData.length; ++pos) {
                ////System.out.println("total1: "+payloadData.length+", pos: "+pos+", total2: "+shadowUWAData.length+" pos2: "+spos);
                output[pos] = (byte) (payloadData[pos] ^ shadowUWAData[spos]);
                ++spos;
                if (spos >= shadowUWAData.length) {
                    spos = 0;
                }
            }
            return output;
        } catch (Exception ex) {
            //
        }
        return null;
    }

    public String getPayloadFromSignedToken(String payload, String signature) {
        try {
            byte[] payloadData = payload.getBytes();
            byte[] signData = signature.getBytes();
            byte[] result = signWith(payloadData, signData);
            return new String(result);
        } catch (Exception ex) {
            //
        }
        return null;
    }

    /*
        @param shadowUWA: is m3 uwa without position
        @param m2uwa: is m2 uwa with position
        @param serverUWA: is full plain-text server UWA.
     */
    public boolean isCertificateValid(String shadowUWA, String payload, String serverUWA, int originalHash) {
        try {
            String shadowRemap = decryptMpinFromSinglePosition(shadowUWA);
            if (shadowRemap == null || !shadowRemap.equals(serverUWA)) {
                //something is changed from client side request
                return false;
            }
            String payloadData = getPayloadFromSignedToken(payload, shadowUWA);
            int hash = payloadData.hashCode();
            // payload is changed on it's way
            return hash == originalHash;
            //everything is okay
        } catch (Exception ex) {
            //invalid request
        }
        return false;
    }

    public String[] encryptUWA(String uwa) {
        int[] leftBorder = new int[]{0, 16, 32, 48, 64, 80, 96, 112, 128, 144, 160, 176, 192, 208, 224, 240};
        int[] rightBorder = new int[]{15, 31, 47, 63, 79, 95, 111, 127, 143, 159, 175, 191, 207, 223, 239, 255};
        char[] mpinArray = uwa.toCharArray();

        //create random number list of number of moves of night
        this.generateKnightMovePositions(dfhdfh);
        // now initialize array list of walks and store in knightWalkNumberList
        this.createKnightWalkNumberStore();
        ////System.out.println("Total Knight Walks for PIN = "+ this.knightWalkNumberList.size());
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

        int[] originalPinIndex = new int[mpinArray.length];
        // place holders
        StringBuffer cipherBufferForM2 = new StringBuffer();
        StringBuffer cipherBufferForM3 = new StringBuffer();

        for (int index = 0; index < this.cipherTemplateList.size(); index++) {
            //System.out.print(" in M1 orignal index of PIN is as : ");
            for (int i = 0; i < mpinArray.length; i++) {
                for (int j = 0; j < this.cipherTemplateList.get(index).length; j++) {
                    //////System.out.println("Comparing " +mpinArray[i]+", "+ this.cipherTemplateList.get(index)[j]) );
                    if (mpinArray[i] == this.cipherTemplateList.get(index)[j]) {
                        originalPinIndex[i] = j;
                        //System.out.print(originalPinIndex[i] + " , ");
                    }
                }
            }

            if (index == 1) {
                int pinPosition = 0;
                for (int mpos = 0; mpos < originalPinIndex.length; mpos++) {
                    pinPosition = this.getArrayIndexValue(leftBorder, originalPinIndex[mpos]);
                    if (pinPosition > -1) {
                        cipherBufferForM2.append(this.cipherTemplateList.get(index)[pinPosition + 1]);
                        cipherBufferForM2.append(this.cipherTemplateList.get(index)[pinPosition + 2]);
                    } else {
                        pinPosition = this.getArrayIndexValue(rightBorder, originalPinIndex[mpos]);
                        if (pinPosition > -1) {
                            cipherBufferForM2.append(this.cipherTemplateList.get(index)[pinPosition - 1]);
                            cipherBufferForM2.append(this.cipherTemplateList.get(index)[pinPosition - 2]);
                        } else {
                            cipherBufferForM2.append(this.cipherTemplateList.get(index)[originalPinIndex[mpos] - 1]);
                            cipherBufferForM2.append(this.cipherTemplateList.get(index)[originalPinIndex[mpos] + 1]);
                        }
                    }
                }
            }
            if (index == 2) {
                int pinPosition = 0;
                for (int mpos = 0; mpos < originalPinIndex.length; mpos++) {
                    pinPosition = this.getArrayIndexValue(leftBorder, originalPinIndex[mpos]);
                    if (pinPosition > -1) {
                        cipherBufferForM3.append(this.cipherTemplateList.get(index)[pinPosition + 1]);
                        cipherBufferForM3.append(this.cipherTemplateList.get(index)[pinPosition + 2]);
                    } else {
                        pinPosition = this.getArrayIndexValue(rightBorder, originalPinIndex[mpos]);
                        if (pinPosition > -1) {
                            cipherBufferForM3.append(this.cipherTemplateList.get(index)[pinPosition - 1]);
                            cipherBufferForM3.append(this.cipherTemplateList.get(index)[pinPosition - 2]);
                        } else {
                            cipherBufferForM3.append(this.cipherTemplateList.get(index)[originalPinIndex[mpos] - 1]);
                            cipherBufferForM3.append(this.cipherTemplateList.get(index)[originalPinIndex[mpos] + 1]);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < this.knightMoveNumbers.length; i++) {
            cipherBufferForM2.append(AllCharacter.getInstance().getCharacterReverseArray()[this.knightMoveNumbers[i]]);

        }
        return new String[]{cipherBufferForM2.toString(), cipherBufferForM3.toString()};
    }

    public String encryptWithKt(String strVal, int totalMoves, int Kt) {
        ////System.out.println("Password Key="+strVal);
        char[] key = strVal.toCharArray();
        // create a random number list of knight moves and initialize knightMoveNumbers array.
        this.generateKnightMovePositions(totalMoves, Kt);

        // now initialize array list of walks and store in knightWalkNumberList
        this.createKnightWalkNumberStore();
        // Creating new character array using knight moves and store in ciperTemplateStore
        ////System.out.println("Total Knight Walk = "+ this.knightWalkNumberList.size());
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
        char[] originalPassword = new char[key.length];
        originalPassword = key;
        // place holders
        char[] silentPassword = new char[key.length];
        int[] originalPassIndex = new int[key.length];
        int[] silentPassIndex = new int[key.length];
        ////System.out.println("Cipher Template List Size=" + this.cipherTemplateList.size());
        // find the indices of our input password chars in M1 to M5 and encrypt
        for (int index = 0; index < this.cipherTemplateList.size(); index++) {
            //System.out.print(" orignal index -");
            for (int i = 0; i < originalPassword.length; i++) {
                for (int j = 0; j < this.cipherTemplateList.get(index).length; j++) {
                    if (originalPassword[i] == this.cipherTemplateList.get(index)[j]) {
                        originalPassIndex[i] = j;
                        //System.out.print(originalPassIndex[i] + " , ");
                    }
                }
            }
            ////System.out.println();
            /*
            Now we have to add silent password chars from M1 starting from backward postion of M1 i.e. 255.254 and so on up to end of length of password
            collect as many as chars from M1 from end of cipher matrix , but not more than length of password
             */

            // For M1 , initialize silentSupplier vector
            int pos = 0;
            if (index == 0) {
                int travelling = this.ASDwead - key.length;
                for (int i = this.cipherTemplateList.get(index).length - 1; i >= travelling; i--) {
                    silentPassword[pos] = this.cipherTemplateList.get(index)[i];
                    silentPassIndex[pos] = i;
                    pos++;
                }
            } else {
                // For M2 and so on , find the index of silentSuplier chars and add to cipher Index in M2 to M5
                for (int i = 0; i < silentPassword.length; i++) {
                    for (int j = 0; j < this.cipherTemplateList.get(index).length; j++) {
                        if (silentPassword[i] == this.cipherTemplateList.get(index)[j]) {
                            silentPassIndex[pos] = j;
                            pos++;
                        }
                    }
                }
            }

            ////System.out.println();
            // generate cipher password after adding the index of silentSupplier chars from M1
            int[] newAddedValue = new int[key.length];

            for (int i = 0; i < silentPassIndex.length; i++) {
                int value = silentPassIndex[i] + originalPassIndex[i];
                ////System.out.println("Adding " + silentPassIndex[i] + " And " + originalPassIndex[i] + " = " + value);
                if (value > this.ASDwead - 1) {
                    newAddedValue[i] = value - this.ASDwead;
                } else {
                    newAddedValue[i] = value;
                }
            }

            ////System.out.println();

            char[] newAddedValueTemp = new char[newAddedValue.length];
            for (int i = 0; i < newAddedValue.length; i++) {
                newAddedValueTemp[i] = this.cipherTemplateList.get(index)[newAddedValue[i]];
            }
            ////System.out.println();
            // override oldPassword
            originalPassword = newAddedValueTemp;
        }

        ////System.out.println();

        //System.out.print("Silent Pass - ");

        for (int i = 0; i < silentPassword.length; i++) {
            //System.out.print(silentPassword[i]);
        }

        ////System.out.println();

        StringBuffer buffer = new StringBuffer();
        buffer.append(originalPassword);
        buffer.append(silentPassword);
        ////System.out.println();
        //System.out.print("Knight move - ");
        for (int i = 0; i < knightMoveNumbers.length; i++) {
            buffer.append(AllCharacter.getInstance().getCharacterReverseArray()[knightMoveNumbers[i]]);
            //System.out.print(knightMoveNumbers[i] + ",");
        }
        buffer.append(AllCharacter.getInstance().getCharacterReverseArray()[knightMoveNumbers.length]);
        ////System.out.println();
        ////System.out.println("Final Password:" +buffer.toString());
        //System.out.print("original password: ");
        //System.out.println(originalPassword);
        //System.out.print("silent password: ");
        //System.out.println(silentPassword);
        //System.out.println("lengths: " + originalPassword.length + ":" + silentPassword.length + " the whole: " + buffer.toString().length());
        //System.out.println("full password: " + buffer.toString());
        return buffer.toString();
    }

}
