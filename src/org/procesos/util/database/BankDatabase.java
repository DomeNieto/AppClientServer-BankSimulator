package org.procesos.util.database;

import java.util.HashMap;
import java.util.Map;

public class BankDatabase {

    // Nombre del archivo donde se almacenan los datos de las cuentas.
    private static final String FILE_NAME = "bank.txt";

    // Map estático que almacena los números de cuenta y sus respectivos saldos.
    private static final Map<String, Double> accounts = new HashMap<>();

}
