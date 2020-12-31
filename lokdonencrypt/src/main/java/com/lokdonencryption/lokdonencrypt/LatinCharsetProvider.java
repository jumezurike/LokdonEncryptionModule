package com.lokdonencryption.lokdonencrypt;

import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.ArrayList;
import java.util.Iterator;

public class LatinCharsetProvider extends CharsetProvider {
    private ArrayList<Character> latinCharset;

    public LatinCharsetProvider() {
        latinCharset = new ArrayList<Character>();
        int codePoint = 0;

        while (latinCharset.size() < CHARSET_SIZE) {
            if (!Character.isISOControl(codePoint)) {
                latinCharset.add((char) codePoint);
            }
            codePoint++;
        }
    }

    private static final int CHARSET_SIZE = 256;

    @Override
    public Iterator<Charset> charsets() {
        return null;
    }

    @Override
    public Charset charsetForName(String charsetName) {
        return null;
    }

    public ArrayList<Character> getLatinCharset() {
        return latinCharset;
    }

    public void setLatinCharset(ArrayList<Character> latinCharset) {
        this.latinCharset = latinCharset;
    }

}
