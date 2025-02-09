package org.procesos.server.thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.procesos.util.database.BankDatabase;

public class ClientHandler extends Thread {
    // Socket que representa la conexión del cliente.
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            // Envía un mensaje de bienvenida y solicita el número de cuenta.
            output.write("Bienvenido al Banco Pichincha!\n");
            output.flush();
            output.write("Ingrese su número de cuenta:\n");
            output.flush();

            String account = input.readLine();
            if (account == null) {
                return;
            }

            output.write("¡Cuenta recibida! Ahora puedes hacer una operación.\n");
            output.flush();

            while (true) {

                // Muestra un menú con las opciones: consultar saldo, depositar, retirar y
                // salir.
                output.write("""
                                Menú del Banco
                        1. Consultar saldo
                        2. Depositar
                        3. Retirar
                        4. Salir
                        Selecione una opcion:
                        """ + "\n");
                output.flush();

                // Procesa la opción seleccionada por el usuario.
                // Utiliza BankDatabase para gestionar los movimientos de la cuenta.
                String choice = input.readLine();
                if (choice == null)
                    break;

                switch (choice) {
                    case "1":
                        output.write("Saldo actual: " + BankDatabase.getBalance(account) + "\n");
                        output.flush();
                        break;
                    case "2":
                        output.write("Ingrese monto a depositar:\n");
                        output.flush();

                        String depositInput = input.readLine();
                        if (depositInput == null) {
                            break;
                        }

                        try {
                            double depositAmount = Double.parseDouble(depositInput);
                            if (depositAmount > 0) {
                                BankDatabase.deposit(account, depositAmount);
                                output.write(
                                        "Depósito exitoso. Nuevo saldo: " + BankDatabase.getBalance(account) + "\n");
                            } else {
                                output.write("El monto debe ser mayor a 0.\n");
                            }
                        } catch (NumberFormatException e) {
                            output.write("Entrada inválida. Introduzca un número válido.\n");
                        }
                        output.flush();
                        break;
                    case "3":
                        output.write("Ingrese monto a retirar:\n");
                        output.flush();
                        String withdrawInput = input.readLine();
                        
                        if (withdrawInput == null) {
                            break;
                        }

                        try {
                            double withdrawAmount = Double.parseDouble(withdrawInput);
                            if (withdrawAmount > 0) {
                                if (BankDatabase.withdraw(account, withdrawAmount)) {
                                    output.write(
                                            "Retiro exitoso. Nuevo saldo: " + BankDatabase.getBalance(account) + "\n");
                                } else {
                                    output.write("Fondos insuficientes.\n");
                                }
                            } else {
                                output.write("El monto debe ser mayor a 0.\n");
                            }
                        } catch (NumberFormatException e) {
                            output.write("Entrada inválida. Introduzca un número válido.\n");
                        }
                        output.flush();
                        break;
                    case "4":
                        // Si el cliente elige salir, cierra la conexión y finaliza el hilo.
                        output.write("Gracias por usar el banco!\n");
                        output.flush();
                        return;
                    default:
                        output.write("Opción inválida.\n");
                        output.flush();
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error en la comunicación: " + e.getMessage());
        }
    }
}
