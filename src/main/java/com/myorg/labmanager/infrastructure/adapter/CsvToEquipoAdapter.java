package com.myorg.labmanager.infrastructure.adapter;

import com.myorg.labmanager.domain.model.Equipo;
import com.myorg.labmanager.domain.model.EquipoFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvToEquipoAdapter implements DataImporter {
    @Override
    public List<Equipo> importarEquipos(String csvPath) throws IOException {
        List<Equipo> equipos = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line = br.readLine(); // Leer encabezado
            
            if (line == null || !validarEncabezado(line)) {
                throw new IOException("Formato de CSV inválido. Se espera: tipo,nombre,cantidad");
            }
            
            int lineNumber = 1;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                
                // Ignorar líneas vacías
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    Equipo equipo = parsearLinea(line);
                    equipos.add(equipo);
                } catch (Exception e) {
                    System.err.println("Error en línea " + lineNumber + ": " + e.getMessage());
                    // Continuar con las demás líneas
                }
            }
        }
        
        return equipos;
    }
    
    private boolean validarEncabezado(String encabezado) {
        String[] columnas = encabezado.toLowerCase().split(",");
        return columnas.length >= 3 
            && columnas[0].trim().contains("tipo")
            && columnas[1].trim().contains("nombre")
            && columnas[2].trim().contains("cantidad");
    }
    
    private Equipo parsearLinea(String linea) {
        String[] columnas = linea.split(",");
        
        if (columnas.length < 3) {
            throw new IllegalArgumentException("La línea debe tener al menos 3 columnas");
        }
        
        String tipo = columnas[0].trim();
        String nombre = columnas[1].trim();
        int cantidad;
        
        try {
            cantidad = Integer.parseInt(columnas[2].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Cantidad inválida: " + columnas[2]);
        }
        
        if (tipo.isEmpty() || nombre.isEmpty()) {
            throw new IllegalArgumentException("Tipo y nombre no pueden estar vacíos");
        }
        
        if (cantidad <= 0) {
            throw new IllegalArgumentException("Cantidad debe ser mayor a 0");
        }
        
        return EquipoFactory.create(tipo, nombre, cantidad);
    }
    
    /**
     * Genera un archivo CSV de ejemplo para guiar al usuario
     */
    public String generarEjemploCSV() {
        return "tipo,nombre,cantidad\n" +
               "Laptop,Dell Inspiron 15,5\n" +
               "Microscopio,Olympus CX23,3\n" +
               "Proyector,Epson PowerLite,2\n" +
               "Arduino,Arduino Uno R3,10\n";
    }
}
