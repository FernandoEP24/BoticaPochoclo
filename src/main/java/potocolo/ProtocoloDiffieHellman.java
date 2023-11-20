package protocolo;

import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ProtocoloDiffieHellman {
    private KeyPair keyPair;

    public ProtocoloDiffieHellman() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DH");
        keyPairGen.initialize(2048);
        keyPair = keyPairGen.generateKeyPair();
    }

    public PublicKey obtenerClavePublica() {
        return keyPair.getPublic();
    }

    public SecretKey generarClaveCompartida(byte[] clavePublicaRemota) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("DH");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(clavePublicaRemota);
        PublicKey clavePublica = keyFactory.generatePublic(x509KeySpec);

        KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
        keyAgreement.init(keyPair.getPrivate());
        keyAgreement.doPhase(clavePublica, true);

        byte[] claveCompartida = keyAgreement.generateSecret();
        return new SecretKeySpec(claveCompartida, 0, claveCompartida.length, "AES");
    }

    public static String codificarClavePublica(PublicKey clavePublica) {
        return Base64.getEncoder().encodeToString(clavePublica.getEncoded());
    }

    public static PublicKey decodificarClavePublica(String clavePublicaCodificada) throws Exception {
        byte[] clavePublicaBytes = Base64.getDecoder().decode(clavePublicaCodificada);
        KeyFactory keyFactory = KeyFactory.getInstance("DH");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(clavePublicaBytes);
        return keyFactory.generatePublic(x509KeySpec);
    }
}
