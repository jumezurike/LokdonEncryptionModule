package com.lokdonencryption.lokdonencrypt;

public class JWUTHelper {

    private String uwa;
    private String payload;
    private long expiry;

    /***
     * JWUTHelper is a helper class that will accelerate JWUT development
     * @param uwa the Universal Wallet Address of the entity, either a person or device.
     * @param payload entity specific secure information that may help identify the user.
     *                Payload max size is 255 bytes.
     * @param expires The expiry time in seconds, counted from now (the moment of method call)
     */
    public JWUTHelper(String uwa, String payload, long expires){
        this.uwa=uwa;
        this.payload=payload;
        this.expiry=expires;
    }
    public JWUTHelper withUniversalWalletAddress(String uwa){
        this.uwa=uwa;
        return this;
    }
    public JWUTHelper withPayload(String payload){
        this.payload=payload;
        return this;
    }
    public JWUT build() throws JWUTException{
        try{
            return new JWUT(CipherControl.getInstance().generateToken(this.uwa,this.payload,this.expiry));
        }catch (Exception e){
            throw new JWUTException("Error generating your JWUT token! "+e.getLocalizedMessage());
        }
    }
    public static class JWUT{
        private String token;
        protected JWUT(String token){
            this.token=token;
        }

    }
    public static class JWUTException extends Exception{
        public JWUTException(String msg){
            super(msg);
        }
    }
}
