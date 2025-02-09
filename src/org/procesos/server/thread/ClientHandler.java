package org.procesos.server.thread;

import java.net.Socket;

public class ClientHandler extends Thread {
    // Socket que representa la conexión del cliente.
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

    }
}
