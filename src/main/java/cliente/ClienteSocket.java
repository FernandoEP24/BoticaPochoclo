package cliente;

import cifrado.CifradoAES;
import protocolo.ProtocoloDiffieHellman;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

public class ClienteSocket {
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private ProtocoloDiffieHellman protocoloDH;
    private SecretKey claveAES;

    public ClienteSocket() {
        try {
            socket = new Socket("localhost", 12345);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);
            protocoloDH = new ProtocoloDiffieHellman();
            claveAES = null;
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void ejecutar() {
        try {
            // Intercambio de claves Diffie-Hellman
            String clavePublicaCodificada = ProtocoloDiffieHellman.codificarClavePublica(protocoloDH.obtenerClavePublica());
            salida.println(clavePublicaCodificada);

            String clavePublicaRemotaCodificada = entrada.readLine();
            PublicKey clavePublicaRemota = ProtocoloDiffieHellman.decodificarClavePublica(clavePublicaRemotaCodificada);

            claveAES = protocoloDH.generarClaveCompartida(clavePublicaRemota.getEncoded());

            // Lógica de chat cifrado
            BufferedReader lectorTeclado = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.print("Mensaje: ");
                String mensaje = lectorTeclado.readLine();
                String mensajeCifrado = CifradoAES.cifrar(mensaje, claveAES);
                salida.println(mensajeCifrado);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ClienteSocket cliente = new ClienteSocket();
        cliente.ejecutar();
    }
}
