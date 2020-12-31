# LokdonEncryptionModule
LokDon-ECSMID v1.0 for software development: Mobile secure communication SMS in MCU and IoT

LokDon-ECSMID is a mobile SDK included as part of the rich collection of LokDon's Security Module As a Software Service.

The first step in activating this project for use is to visit our activation website on https://activation.lokdon.com

Select your access portion and fill in the email address. Once this is done, you can proceed to payment and make a payment with your card. Once the payment
is processed successfully you will receive an email attached to it is your license key 

In your build.graddle file(:app module)

dependencies{

implementation 'com.lokdonencryption.lokdonencryptionmodule:lokdonencryptmodulemobile:0.0.1'

}

This will give us access to instantiate the Lokdon class

Lokdon your_object  = new Lokdon(Context c,  String licence_key);
The first parameter is the context while the second parameter is the license key issued to you on Lokdon website and forwaded to your email when
you pay for the product.

Once you pass this successfully. You can start using the encryption methods

1 ) encryptGenericData(String text);
2 ) decryptGenericData(String encryptText);
3 ) generatePassword(String values, int knightMoves);
4) decryptPassword(String encryptPass, int knightMoves)
4 )encryptFile(InputStream fis, String key, String filePath);
5 )decryptFile(InputStream fis, String key, String filePath);
6 ) encryptNew(String original, String m3pin, String sess_id);
7 ) decryptNew(String original, String m3pin, String sess_id);

These methods are used in eight contexts and all have what it can do for you

The encryptGenericData can encrypt any string such as name, address , phone number using Lokdon encryption. It requires only one parameter which is a
String text.

The decryptGenericData can decrypt any encrypted string such as name, address , phone number using Lokdon encryption. It requires only one parameter which is an
 String text.
 
 The generatePassword can be used to encrypt password and security keys up to five moves/levels. It requires two parameters which is the password and the
 number of moves which is always an odd number default of 5
 
 The decryptPassword can be used to decrypt password and security to the original values. It requires two parameters which is the encrypted password and the
 number of moves which is always an odd number default of 5
 
 The encryptFile can be used for File encryption. It requires three parameters namely the InputStream, The key which will be an intermediary representation
 of the file and the filepath
 
 The decryptFile can be used for File decryption. It requires three parameters namely the InputStream, The key which will be an intermediary representation
 of the file and the filepath
 
 The encryptNew will be used for encrypting sms and money transfers. This is optimized for secure and sensitive communications. It requires three parameters
 namely; original text, the m3pin which could intermediary representation set up by you and the session id
 
 The  decryptNew will be used for decrypting sms and money transfers. This is optimized for secure and sensitive communications. It requires three parameters
 namely; original text, the m3pin which could intermediary representation set up by you and the session id
 
 

