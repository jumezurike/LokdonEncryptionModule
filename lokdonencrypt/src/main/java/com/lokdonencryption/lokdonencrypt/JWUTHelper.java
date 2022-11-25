package com.lokdonencryption.lokdonencrypt;

public class JWUTHelper {

    private String uwa;
    private String payload;

    /***
     * JWUTHelper is a helper class that will accelerate JWUT development
     * @param uwa the Universal Wallet Address of the entity, either a person or device.
     * @param payload entity specific secure information that may help identify the user.
     *                Payload max size is 255 bytes.
     */
    public JWUTHelper(String uwa, String payload){
        this.uwa=uwa;
        this.payload=payload;
    }
    public JWUTHelper withUniversalWalletAddress(String uwa){
        this.uwa=uwa;
        return this;
    }
    public JWUTHelper withPayload(String payload){
        this.payload=payload;
        return this;
    }
    public static class JWUT{
        private String token;
        protected JWUT(String token){
            this.token=token;
        }
        
    }
}
