package org.procesos.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.procesos.util.constant.Constants;
import org.procesos.util.database.BankDatabase;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT);
        BankDatabase.readDatabase();

        System.out.println("Banco en línea. Esperando clientes...");
        while (true) {
            Socket clientSocket = serverSocket.accept();
        }
    }
}
