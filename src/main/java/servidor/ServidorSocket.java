package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorSocket {
    private ServerSocket serverSocket;
    private int puerto = 12345;

    public ServidorSocket() {
        try {
            serverSocket = new ServerSocket(puerto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void iniciar() {
        while (true) {
            try {
                System.out.println("Esperando conexiones...");
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado: " + socket.getInetAddress());
                // Aquí deberías manejar la conexión con el cliente
                // Puedes crear una clase ManejadorCliente para ello.
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ServidorSocket servidor = new ServidorSocket();
        servidor.iniciar();
    }
}