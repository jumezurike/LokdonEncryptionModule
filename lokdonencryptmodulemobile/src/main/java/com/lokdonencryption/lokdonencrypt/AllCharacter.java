package com.lokdonencryption.lokdonencrypt;

public class AllCharacter {

    private final int CHARSET_SIZE = 256;
    public static AllCharacter instance = null;
    private final char[] reverseArray = new char[CHARSET_SIZE];
    private final char[] charArray = new char[CHARSET_SIZE];
    private String API_KEY = "5d01c9620cb2e11d2f027504110fb40a3f510f8e";

    public static AllCharacter getInstance() {
        if (instance == null) {
            instance = new AllCharacter();
        }
        return instance;
    }

    private AllCharacter() {
        LatinCharsetProvider provider = new LatinCharsetProvider();
        for (int i = 0; i < getStandardCharacterSet().length; i++) {
            charArray[i] = provider.getLatinCharset().get(i);
        }
    }

    //Basic Latin Unicode in UTF-8
    // 16 chars for ASCII punctuation and symbols
    char SPACE = '\u0020';
    char EXCLAIMATION = '\u0021';
    char QUOTATION = '\"';
    char NUMBER = '\u0023';
    char DOLLAR = '\u0024';
    char PERCENT = '\u0025';
    char AMPERSAND = '\u0026';
    char APOSTRPOHE = '\'';
    char LEFT_PAREN = '\u0028';
    char RIGHT_PAREN = '\u0029';
    char ASTERISK = '\u002A';
    char PLUS = '\u002B';
    char COMMA = '\u002C';
    char MINUS = '\u002D';
    char FULLSTOP = '\u002E';
    char SOLIDUS = '\u002F';
    // 10 chars for ASCII Digits

    char ZERO = '\u0030';
    char ONE = '\u0031';
    char TWO = '\u0032';
    char THREE = '\u0033';
    char FOUR = '\u0034';
    char FIVE = '\u0035';
    char SIX = '\u0036';
    char SEVEN = '\u0037';
    char EIGHT = '\u0038';
    char NINE = '\u0039';
    // 7 chars for ASCII punctuation and symbols
    char COLON = '\u003A';
    char SEMICOLON = '\u003B';
    char LESSTHAN = '\u003C';
    char EQUAL = '\u003D';
    char GREATERTHAN = '\u003E';
    char QUESTION = '\u003F';
    char AT = '\u0040';

    // 26 chars for Uppercase Latin alphabet
    char CAPITAL_A = '\u0041';
    char CAPITAL_B = '\u0042';
    char CAPITAL_C = '\u0043';
    char CAPITAL_D = '\u0044';
    char CAPITAL_E = '\u0045';
    char CAPITAL_F = '\u0046';
    char CAPITAL_G = '\u0047';
    char CAPITAL_H = '\u0048';
    char CAPITAL_I = '\u0049';
    char CAPITAL_J = '\u004A';
    char CAPITAL_K = '\u004B';
    char CAPITAL_L = '\u004C';
    char CAPITAL_M = '\u004D';
    char CAPITAL_N = '\u004E';
    char CAPITAL_O = '\u004F';
    char CAPITAL_P = '\u0050';
    char CAPITAL_Q = '\u0051';
    char CAPITAL_R = '\u0052';
    char CAPITAL_S = '\u0053';
    char CAPITAL_T = '\u0054';
    char CAPITAL_U = '\u0055';
    char CAPITAL_V = '\u0056';
    char CAPITAL_W = '\u0057';
    char CAPITAL_X = '\u0058';
    char CAPITAL_Y = '\u0059';
    char CAPITAL_Z = '\u005A';

    // 6 chars for ASCII punctuation and symbols
    char LEFTSQUARE = '\u005B';
    char REVERSE_SOLIDIUS = '\\';
    //    char REVERSE_SOLIDIUS = "'\u005C'';
    char RIGHTSQUARE = '\u005D';
    char CIRCUMFLEX = '\u005E';
    char UNDERSCORE = '\u005F';
    char GRAVEACCENT = '\u0060';

    // 26 chars for Lowercase Latin alphabet
    char SMALL_A = '\u0061';
    char SMALL_B = '\u0062';
    char SMALL_C = '\u0063';
    char SMALL_D = '\u0064';
    char SMALL_E = '\u0065';
    char SMALL_F = '\u0066';
    char SMALL_G = '\u0067';
    char SMALL_H = '\u0068';
    char SMALL_I = '\u0069';
    char SMALL_J = '\u006A';
    char SMALL_K = '\u006B';
    char SMALL_L = '\u006C';
    char SMALL_M = '\u006D';
    char SMALL_N = '\u006E';
    char SMALL_O = '\u006F';
    char SMALL_P = '\u0070';
    char SMALL_Q = '\u0071';
    char SMALL_R = '\u0072';
    char SMALL_S = '\u0073';
    char SMALL_T = '\u0074';
    char SMALL_U = '\u0075';
    char SMALL_V = '\u0076';
    char SMALL_W = '\u0077';
    char SMALL_X = '\u0078';
    char SMALL_Y = '\u0079';
    char SMALL_Z = '\u007A';
    // 4 chars for ASCII punctuation and symbols

    char LEFTCURLY = '\u007B';
    char VERTICALLINE = '\u007C';
    char RIGHTCURLY = '\u007D';
    char TILDE = '\u007E';
    // 7F to 9F are control characters
    // Total 95 chars from Basic Latin except control Chars

    // 96 chars for Latin-1 Supplement
    // char NOBREAK = '\u00A0';// as discuss with ravi sir
    char INV_EXCLAIM = '\u00A1';
    char CENTSIGN = '\u00A2';
    char POUND = '\u00A3';
    char CURRENCY = '\u00A4';
    char YEN = '\u00A5';
    char PIPE = '\u00A6';
    char SECTION = '\u00A7';
    char DIAERESIS = '\u00A8';
    char COPYRIGHT = '\u00A9';
    char FEMININE = '\u00AA';
    char LEFT_ANGLE = '\u00AB';
    char NOT_SIGN = '\u00AC';
    char SOFT_HYPHEN = '\u00AD';
    char REGISTERED = '\u00AE';
    char MACRON = '\u00AF';
    char DEGREE = '\u00B0';
    char PLUS_MINUS = '\u00B1';
    char SQUARE = '\u00B2';
    char CUBICAL = '\u00B3';
    char ACUTE_ACCENT = '\u00B4';
    char MICRO_SIGN = '\u00B5';
    char PILCROW = '\u00B6';
    char MIDDLE_DOT = '\u00B7';
    char CEDILA = '\u00B8';
    char SUPERSCRIPT_ONE = '\u00B9';
    char MASCULINE_ORDINAL_ONE = '\u00BA';
    char RIGHT_ANGLE = '\u00BB';
    char ONE_QUARTER = '\u00BC';
    char ONE_HALF = '\u00BD';
    char THREE_QUARTER = '\u00BE';
    char INVERTED_QUESTION = '\u00BF';
    char LAT_CAPITAL_A_GRAVE = '\u00C0';
    char LAT_CAPITAL_A_ACUTE = '\u00C1';
    char LAT_CAPITAL_A_CIRCUM = '\u00C2';
    char LAT_CAPITAL_A_TILDE = '\u00C3';
    char LAT_CAPITAL_A_DIAERSIS = '\u00C4';
    char LAT_CAPITAL_A_RING = '\u00C5';
    char LAT_CAPITAL_A_E = '\u00C6';
    char LAT_CAPITAL_C_CADILA = '\u00C7';
    char LAT_CAPITAL_E_GRAVE = '\u00C8';
    char LAT_CAPITAL_E_ACUTE = '\u00C9';
    char LAT_CAPITAL_E_CIRCUM = '\u00CA';
    char LAT_CAPITAL_E_DIAERSIS = '\u00CB';
    char LAT_CAPITAL_I_GRAVE = '\u00CC';
    char LAT_CAPITAL_I_ACUTE = '\u00CD';
    char LAT_CAPITAL_I_CIRCUM = '\u00CE';
    char LAT_CAPITAL_I_DIAERSIS = '\u00CF';
    char LAT_CAPITAL_ETH = '\u00D0';
    char LAT_CAPITAL_N_TILDE = '\u00D1';
    char LAT_CAPITAL_O_GRAVE = '\u00D2';
    char LAT_CAPITAL_O_ACUTE = '\u00D3';
    char LAT_CAPITAL_O_CIRCUM = '\u00D4';
    char LAT_CAPITAL_O_TILDE = '\u00D5';
    char LAT_CAPITAL_O_DIAERSIS = '\u00D6';
    char MULTIPLCATION = '\u00D7';
    char LAT_CAPITAL_O_STROKE = '\u00D8';
    char LAT_CAPITAL_U_GRAVE = '\u00D9';
    char LAT_CAPITAL_U_ACUTE = '\u00DA';
    char LAT_CAPITAL_U_CIRCUM = '\u00DB';
    char LAT_CAPITAL_U_DIAERSIS = '\u00DC';
    char LAT_CAPITAL_Y_ACUTE = '\u00DD';
    char CAP_THORN = '\u00DE';
    char SHARP_S = '\u00DF';
    char LAT_SMALL_A_GRAVE = '\u00E0';
    char LAT_SMALL_A_ACUTE = '\u00E1';
    char LAT_SMALL_A_CIRCUM = '\u00E2';
    char LAT_SMALL_A_TILDE = '\u00E3';
    char LAT_SMALL_A_DIAERSIS = '\u00E4';
    char LAT_SMALL_A_RING = '\u00E5';
    char LAT_SMALL_A_AE = '\u00E6';
    char LAT_SMALL_C_CEDILA = '\u00E7';
    char LAT_SMALL_E_GRAVE = '\u00E8';
    char LAT_SMALL_E_ACUTE = '\u00E9';
    char LAT_SMALL_E_CIRCUM = '\u00EA';
    char LAT_SMALL_E_DIAERSIS = '\u00EB';
    char LAT_SMALL_I_GRAVE = '\u00EC';
    char LAT_SMALL_I_ACUTE = '\u00ED';
    char LAT_SMALL_I_CIRCUM = '\u00EE';
    char LAT_SMALL_I_DIAERSIS = '\u00EF';
    char LAT_SMALL_ETH = '\u00F0';
    char LAT_SMALL_N_TILDE = '\u00F1';
    char LAT_SMALL_O_GRAVE = '\u00F2';
    char LAT_SMALL_O_ACUTE = '\u00F3';
    char LAT_SMALL_O_CIRCUM = '\u00F4';
    char LAT_SMALL_O_TILDE = '\u00F5';
    char LAT_SMALL_O_DIAERSIS = '\u00F6';
    char DIVISION = '\u00F7';
    char LAT_SMALL_O_STROKE = '\u00F8';
    char LAT_SMALL_U_GRAVE = '\u00F9';
    char LAT_SMALL_U_ACUTE = '\u00FA';
    char LAT_SMALL_U_CIRCUM = '\u00FB';
    char LAT_SMALL_U_DIAERSIS = '\u00FC';
    char LAT_SMALL_Y_ACUTE = '\u00FD';
    char SMALL_THORN = '\u00FE';
    // 63 chars from Latin Extended A
    char LAT_SMALL_Y_DIAERSIS = '\u00FF';
    char LAT_CAP_A_MACRON = '\u0100';
    char LAT_SMALL_A_MACRON = '\u0101';
    char LAT_CAP_A_BREVE = '\u0102';
    char LAT_SMALL_A_BREVE = '\u0103';
    char LAT_CAP_A_OGONEK = '\u0104';
    char LAT_SMALL_A_OGONEK = '\u0105';
    char LAT_CAP_C_ACUTE = '\u0106';
    char LAT_SMALL_C_ACUTE = '\u0107';
    char LAT_CAP_C_CIRCUM = '\u0108';
    char LAT_SMALL_C_CIRCUM = '\u0109';
    char LAT_CAP_C_DOT = '\u010A';
    char LAT_SMALL_C_DOT = '\u010B';
    char LAT_CAP_C_CARON = '\u010C';
    char LAT_SMALL_C_CARON = '\u010D';
    char LAT_CAP_D_CARON = '\u010E';
    char LAT_SMALL_D_CARON = '\u010F';
    char LAT_CAP_D_STROKE = '\u0110';
    char LAT_SMALL_D_STROKE = '\u0111';
    char LAT_CAP_E_MACRON = '\u0112';
    char LAT_SMALL_E_MACRON = '\u0113';
    char LAT_CAP_E_BREVE = '\u0114';
    char LAT_SMALL_E_BREVE = '\u0115';
    char LAT_CAP_E_DOT = '\u0116';
    char LAT_SMALL_E_DOT = '\u0117';
    char LAT_CAP_E_OGONEK = '\u0118';
    char LAT_SMALL_E_OGONEK = '\u0119';
    char LAT_CAP_E_CARON = '\u011A';
    char LAT_SMALL_E_CARON = '\u011B';
    char LAT_CAP_G_CIRCUM = '\u011C';
    char LAT_SMALL_G_CIRCUM = '\u011D';
    char LAT_CAP_G_BREVE = '\u011E';
    char LAT_SMALL_G_BREVE = '\u011F';
    char LAT_CAP_G_DOT = '\u0120';
    char LAT_SMALL_G_DOT = '\u0121';
    char LAT_CAP_G_CEDILA = '\u0122';
    char LAT_SMALL_G_CEDILA = '\u0123';
    char LAT_CAP_H_CIRCUM = '\u0124';
    char LAT_SMALL_H_CIRCUM = '\u0125';
    char LAT_CAP_H_STROKE = '\u0126';
    char LAT_SMALL_H_STROKE = '\u0127';
    char LAT_CAP_I_TILDE = '\u0128';
    char LAT_SMALL_I_TILDE = '\u0129';
    char LAT_CAP_I_MACRON = '\u012A';
    char LAT_SMALL_I_MACRON = '\u012B';
    char LAT_CAP_I_BREVE = '\u012C';
    char LAT_SMALL_I_BREVE = '\u012D';
    char LAT_CAP_I_OGONEK = '\u012E';
    char LAT_SMALL_I_OGONEK = '\u012F';
    char LAT_CAP_I_DOT = '\u0130';
    char LAT_SMALL_I_DOTLESS = '\u0131';
    char LAT_CAP_LIGATURE = '\u0132';
    char LAT_SMALL_LIGATURE = '\u0133';
    char LAT_CAP_J_CIRCUM = '\u0134';
    char LAT_SMALL_J_CIRCUM = '\u0135';
    char LAT_CAP_K_CEDILA = '\u0136';
    char LAT_SMALL_K_CEDILA = '\u0137';
    char LAT_SMALL_KRA = '\u0138';
    char LAT_CAP_L_ACUTE = '\u0139';
    char LAT_SMALL_L_ACUTE = '\u013A';
    char LAT_CAP_L_CEDILA = '\u013B';
    char LAT_SMALL_L_CEDILA = '\u013C';
    char LAT_CAP_L_CARON = '\u013D';
    char LAT_SMALL_L_CARON = '\u013E';
    char LAT_CAP_L_MIDDOT = '\u013F';
    char LAT_SMALL_L_MIDDOT = '\u0140';
    char LAT_BIG_L_STRIKE = '\u0141';

    private char standardCharacterSet[] = {
            SPACE, EXCLAIMATION, QUOTATION, NUMBER, DOLLAR, PERCENT, AMPERSAND, APOSTRPOHE,
            LEFT_PAREN, RIGHT_PAREN, ASTERISK, PLUS, COMMA, MINUS, FULLSTOP, SOLIDUS,
            ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, COLON, SEMICOLON,
            LESSTHAN, EQUAL, GREATERTHAN, QUESTION,
            AT, CAPITAL_A, CAPITAL_B, CAPITAL_C, CAPITAL_D, CAPITAL_E, CAPITAL_F, CAPITAL_G, CAPITAL_H,
            CAPITAL_I, CAPITAL_J, CAPITAL_K, CAPITAL_L, CAPITAL_M, CAPITAL_N, CAPITAL_O,
            CAPITAL_P, CAPITAL_Q, CAPITAL_R, CAPITAL_S, CAPITAL_T, CAPITAL_U, CAPITAL_V, CAPITAL_W, CAPITAL_X, CAPITAL_Y, CAPITAL_Z, LEFTSQUARE, REVERSE_SOLIDIUS, RIGHTSQUARE, CIRCUMFLEX, UNDERSCORE,
            GRAVEACCENT, SMALL_A, SMALL_B, SMALL_C, SMALL_D, SMALL_E, SMALL_F, SMALL_G, SMALL_H, SMALL_I, SMALL_J, SMALL_K, SMALL_L, SMALL_M, SMALL_N, SMALL_O,
            SMALL_P, SMALL_Q, SMALL_R, SMALL_S, SMALL_T, SMALL_U, SMALL_V, SMALL_W, SMALL_X, SMALL_Y, SMALL_Z, LEFTCURLY, VERTICALLINE, RIGHTCURLY, TILDE,
            INV_EXCLAIM, CENTSIGN, POUND, CURRENCY, YEN, PIPE, SECTION, DIAERESIS, COPYRIGHT, FEMININE, LEFT_ANGLE, NOT_SIGN, SOFT_HYPHEN, REGISTERED, MACRON, DEGREE,
            PLUS_MINUS, SQUARE, CUBICAL, ACUTE_ACCENT, MICRO_SIGN, PILCROW, MIDDLE_DOT, CEDILA, SUPERSCRIPT_ONE, MASCULINE_ORDINAL_ONE, RIGHT_ANGLE, ONE_QUARTER, ONE_HALF, THREE_QUARTER, INVERTED_QUESTION, LAT_CAPITAL_A_GRAVE,
            LAT_CAPITAL_A_ACUTE, LAT_CAPITAL_A_CIRCUM, LAT_CAPITAL_A_TILDE, LAT_CAPITAL_A_DIAERSIS, LAT_CAPITAL_A_RING, LAT_CAPITAL_A_E, LAT_CAPITAL_C_CADILA, LAT_CAPITAL_E_GRAVE, LAT_CAPITAL_E_ACUTE, LAT_CAPITAL_E_CIRCUM, LAT_CAPITAL_E_DIAERSIS, LAT_CAPITAL_I_GRAVE, LAT_CAPITAL_I_ACUTE, LAT_CAPITAL_I_CIRCUM, LAT_CAPITAL_I_DIAERSIS, LAT_CAPITAL_ETH,
            LAT_CAPITAL_N_TILDE, LAT_CAPITAL_O_GRAVE, LAT_CAPITAL_O_ACUTE, LAT_CAPITAL_O_CIRCUM, LAT_CAPITAL_O_TILDE, LAT_CAPITAL_O_DIAERSIS, MULTIPLCATION, LAT_CAPITAL_O_STROKE, LAT_CAPITAL_U_GRAVE, LAT_CAPITAL_U_ACUTE, LAT_CAPITAL_U_CIRCUM, LAT_CAPITAL_U_DIAERSIS, LAT_CAPITAL_Y_ACUTE, CAP_THORN, SHARP_S, LAT_SMALL_A_GRAVE,
            LAT_SMALL_A_ACUTE, LAT_SMALL_A_CIRCUM, LAT_SMALL_A_TILDE, LAT_SMALL_A_DIAERSIS, LAT_SMALL_A_RING, LAT_SMALL_A_AE, LAT_SMALL_C_CEDILA, LAT_SMALL_E_GRAVE, LAT_SMALL_E_ACUTE, LAT_SMALL_E_CIRCUM, LAT_SMALL_E_DIAERSIS, LAT_SMALL_I_GRAVE, LAT_SMALL_I_ACUTE, LAT_SMALL_I_CIRCUM, LAT_SMALL_I_DIAERSIS, LAT_SMALL_ETH,
            LAT_SMALL_N_TILDE, LAT_SMALL_O_GRAVE, LAT_SMALL_O_ACUTE, LAT_SMALL_O_CIRCUM, LAT_SMALL_O_TILDE, LAT_SMALL_O_DIAERSIS, DIVISION, LAT_SMALL_O_STROKE, LAT_SMALL_U_GRAVE, LAT_SMALL_U_ACUTE, LAT_SMALL_U_CIRCUM, LAT_SMALL_U_DIAERSIS, LAT_SMALL_Y_ACUTE, SMALL_THORN, LAT_SMALL_Y_DIAERSIS, LAT_CAP_A_MACRON,
            LAT_SMALL_A_MACRON, LAT_CAP_A_BREVE, LAT_SMALL_A_BREVE, LAT_CAP_A_OGONEK, LAT_SMALL_A_OGONEK, LAT_CAP_C_ACUTE, LAT_SMALL_C_ACUTE, LAT_CAP_C_CIRCUM, LAT_SMALL_C_CIRCUM, LAT_CAP_C_DOT, LAT_SMALL_C_DOT, LAT_CAP_C_CARON, LAT_SMALL_C_CARON, LAT_CAP_D_CARON, LAT_SMALL_D_CARON, LAT_CAP_D_STROKE,
            LAT_SMALL_D_STROKE, LAT_CAP_E_MACRON, LAT_SMALL_E_MACRON, LAT_CAP_E_BREVE, LAT_SMALL_E_BREVE, LAT_CAP_E_DOT, LAT_SMALL_E_DOT, LAT_CAP_E_OGONEK, LAT_SMALL_E_OGONEK, LAT_CAP_E_CARON, LAT_SMALL_E_CARON, LAT_CAP_G_CIRCUM, LAT_SMALL_G_CIRCUM, LAT_CAP_G_BREVE, LAT_SMALL_G_BREVE, LAT_CAP_G_DOT,
            LAT_SMALL_G_DOT, LAT_CAP_G_CEDILA, LAT_SMALL_G_CEDILA, LAT_CAP_H_CIRCUM, LAT_SMALL_H_CIRCUM, LAT_CAP_H_STROKE, LAT_SMALL_H_STROKE, LAT_CAP_I_TILDE, LAT_SMALL_I_TILDE, LAT_CAP_I_MACRON, LAT_SMALL_I_MACRON, LAT_CAP_I_BREVE, LAT_SMALL_I_BREVE, LAT_CAP_I_OGONEK, LAT_SMALL_I_OGONEK, LAT_CAP_I_DOT,
            LAT_SMALL_I_DOTLESS, LAT_CAP_LIGATURE, LAT_SMALL_LIGATURE, LAT_CAP_J_CIRCUM, LAT_SMALL_J_CIRCUM, LAT_CAP_K_CEDILA, LAT_SMALL_K_CEDILA, LAT_SMALL_KRA, LAT_CAP_L_ACUTE, LAT_SMALL_L_ACUTE, LAT_CAP_L_CEDILA, LAT_SMALL_L_CEDILA, LAT_CAP_L_CARON, LAT_SMALL_L_CARON, LAT_CAP_L_MIDDOT, LAT_SMALL_L_MIDDOT, LAT_BIG_L_STRIKE
    };

    public char[] getCharacterReverseArray() {
        int index = 0;
        for (int i = standardCharacterSet.length - 1; i >= 0; i--) {
            reverseArray[index] = standardCharacterSet[i];
            index++;
        }
        return reverseArray;
    }

    public char[] getStandardCharacterSet() {
        return standardCharacterSet;
    }

    public void setStandardCharacterSet(char[] standardCharacterSet) {
        this.standardCharacterSet = standardCharacterSet;
    }
}
