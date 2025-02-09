package org.procesos.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import org.procesos.util.constant.Constants;

public class ClientApp {
    public static void main(String[] args) {

        // Se establece una conexión con el servidor a través de un Socket en localhost
        // y el puerto definido en Constants.SERVER_PORT (8082).
        try (Socket socket = new Socket("localhost", Constants.SERVER_PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                Scanner scanner = new Scanner(System.in)) {

            // Se reciben el saludo y petición de cuenta del servidor.
            System.out.println(in.readLine());
            System.out.println(in.readLine());

            // Se lee la entrada del usuario.
            String input = scanner.nextLine();

            // Se escribe inmediatamente la respuesta del usaurio en el servidor.
            output.write(input + "\n");
            output.flush();
            System.out.println(in.readLine());

            while (true) {
                String line;

                // Lee y muestra todas las líneas que no están vacías o en blanco recibidas
                // desde el servidor.
                while ((line = in.readLine()) != null && !line.isBlank()) {
                    System.out.println(line);
                }

                // Lee la elección del usuario y se escribe inmediatamente la respuesta del
                // usaurio en el servidor.
                String choice = scanner.nextLine();
                output.write(choice + "\n");
                output.flush();

                // Si el usuario elige 4, se termina la conexión y el programa finaliza.
                if (choice.equals("4")) {
                    System.out.println("Gracias por usar nuestro banco!");
                    break;
                }

                // Si el usuario elige 2 o 3, se pide un monto y lo envía al servidor.
                if (choice.equals("2") || choice.equals("3")) {
                    System.out.println(in.readLine());

                    // Lee el monto y lo envía al servidor.
                    String amountStr = scanner.nextLine();
                    output.write(amountStr + "\n");
                    output.flush();

                    // // Muestra la respuesta de confirmación recibida del servidor.
                    System.out.println(in.readLine());
                }
            }

        } catch (IOException e) {
            System.out.println("Error en el cliente: " + e.getMessage());
        }
    }
}
