
import cifrado.CifradoAES;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import protocolo.ProtocoloDiffieHellman;

public class ManejadorCliente extends Thread {
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private ProtocoloDiffieHellman protocoloDH;
    private SecretKey claveAES;

    public ManejadorCliente(Socket socket) {
        this.socket = socket;
        try {
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);
            protocoloDH = new ProtocoloDiffieHellman();
            claveAES = null;
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // Intercambio de claves Diffie-Hellman
            String clavePublicaCodificada = ProtocoloDiffieHellman.codificarClavePublica(protocoloDH.obtenerClavePublica());
            salida.println(clavePublicaCodificada);

            String clavePublicaRemotaCodificada = entrada.readLine();
            PublicKey clavePublicaRemota = ProtocoloDiffieHellman.decodificarClavePublica(clavePublicaRemotaCodificada);

            claveAES = protocoloDH.generarClaveCompartida(clavePublicaRemota.getEncoded());

            // Lógica de chat cifrado
            while (true) {
                String mensajeCifrado = entrada.readLine();
                String mensajeDescifrado = CifradoAES.descifrar(mensajeCifrado, claveAES);
                System.out.println("Mensaje recibido: " + mensajeDescifrado);

                // Puedes enviar mensajes cifrados al cliente utilizando CifradoAES.cifrar(...)
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}