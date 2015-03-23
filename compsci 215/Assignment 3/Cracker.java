/**
 * Created by Barr on 23-Mar-15.
 */
import java.security.*;
import java.io.*;

public class Cracker {

    public static void main(String[] args) {
        String result = "";
        String fileWithWords = "words.txt";
        BufferedReader fileReader = null;
        boolean brk = false;

        try {
            String line = "";
            fileReader = new BufferedReader(new FileReader(fileWithWords));

            while((line = fileReader.readLine()) != null){
                result = MD5Check(line);

                if(result.equals("RESPONSE TO CRACK GOES HERE")){
                    System.out.println("Found Pass : " + line);
                    System.out.println("Output : " + result);
                    brk = true;
                    break;
                }
            }

            if (brk){
                fileReader.close();
            }

        } catch (IOException e) {
            System.out.println("problem\n");
        }
    }

    private static String MD5Check(String passPhrase){
        String ha1 = "USER:REALM" + passPhrase;
        String ha2 = "METHOD:URI";
        String nonce = "NONCE";

        try {
            MessageDigest message = MessageDigest.getInstance("MD5"); //ha1 - need to hash this
            MessageDigest digest = MessageDigest.getInstance("MD5");

            byte[] arrayOne = message.digest(ha1.getBytes()); // we add all our shit here

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < arrayOne.length; ++i) {
                sb.append(Integer.toHexString((arrayOne[i] & 0xFF) | 0x100).substring(1,3));
            }

            String fin = sb.toString() + ":" + nonce + ":" + ha2;
            byte[] arrayFin = digest.digest(fin.getBytes());

            StringBuffer ender = new StringBuffer();
            for (int i = 0; i < arrayFin.length; ++i) {
                ender.append(Integer.toHexString((arrayFin[i] & 0xFF) | 0x100).substring(1, 3));
            }

            return ender.toString();

        } catch (NoSuchAlgorithmException e){e.printStackTrace();}
        return null;
    }
}
