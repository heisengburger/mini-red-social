package org.example;

import org.example.config.Neo4jConfig;
import org.example.database.Neo4jConnection;
import org.example.view.MainFrame;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        Neo4jConfig config = Neo4jConfig.load();

        Neo4jConnection connection = new Neo4jConnection(config);
        try {
            connection.verify();
            System.out.println("Conexion correcta con Neo4j en " + config.uri());

            SwingUtilities.invokeLater(() -> {
                MainFrame frame = new MainFrame(connection.driver(), connection::close);
                frame.setVisible(true);
            });
        } catch (Exception e) {
            connection.close();
            JOptionPane.showMessageDialog(
                    null,
                    "No se pudo conectar con Neo4j: " + e.getMessage(),
                    "Error de conexion",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
