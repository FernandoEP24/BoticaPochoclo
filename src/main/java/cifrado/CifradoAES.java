package cifrado;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CifradoAES {
    public static String cifrar(String mensaje, SecretKey clave) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, clave);
        byte[] encrypted = cipher.doFinal(mensaje.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String descifrar(String mensajeCifrado, SecretKey clave) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, clave);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(mensajeCifrado));
        return new String(decrypted);
    }

    public static SecretKey generarClaveAES() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        return keyGen.generateKey();
    }
}
