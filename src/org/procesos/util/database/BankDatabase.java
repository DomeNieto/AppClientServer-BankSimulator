package org.procesos.util.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BankDatabase {

    // Nombre del archivo donde se almacenan los datos de las cuentas.
    private static final String FILE_NAME = "bank.txt";

    // Map estático que almacena los números de cuenta y sus respectivos saldos.
    private static final Map<String, Double> accounts = new HashMap<>();

    // Carga las cuentas desde el archivo bank.txt.
    public static synchronized void readDatabase() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("Archivo de cuentas creado.");
            } catch (IOException e) {
                System.out.println("Error al crear el archivo de cuentas: " + e.getMessage());
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                accounts.put(parts[0], Double.parseDouble(parts[1]));
            }
        } catch (IOException e) {
            System.out.println("No se pudo cargar la base de datos: " + e.getMessage());
        }

    }

    // Guarda los datos del Map accounts en el archivo bank.txt
    public static synchronized void saveAccounts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String key : accounts.keySet()) {
                writer.write(key + "," + accounts.get(key) + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error al guardar cuentas: " + e.getMessage());
        }
    }

    // Obtiene el saldo de una cuenta específica.
    public static synchronized double getBalance(String account) {
        return accounts.getOrDefault(account, 0.0);
    }

    // Agrega una cantidad de dinero a la cuenta especificada.
    public static synchronized void deposit(String account, double amount) {
        accounts.put(account, getBalance(account) + amount);
        saveAccounts();
    }

    // Retira dinero de una cuenta si hay fondos suficientes.
    public static synchronized boolean withdraw(String account, double amount) {
        if (getBalance(account) >= amount) {
            accounts.put(account, getBalance(account) - amount);
            saveAccounts();
            return true;
        }
        return false;
    }

}
