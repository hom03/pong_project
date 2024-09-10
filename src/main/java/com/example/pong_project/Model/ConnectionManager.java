package com.example.pong_project.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionManager {
    private static ConnectionManager instance;
    private Connection connection;

    private ConnectionManager() {

    }

    public static synchronized ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public synchronized Connection getConnection(){
        if (connection == null || !isConnectionValid(connection)) {
            try{
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pong_game_db", "root", "javaDBconnect1#");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return connection;
    }

    private boolean isConnectionValid(Connection connection) {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public synchronized void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
