package org.example;

import org.postgresql.util.PSQLException;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class App {
    Scanner scanner = new Scanner(System.in);
    final String URL = "jdbc:postgresql://localhost:5432/it.academy";
    final String USER_NAME = "postgres";
    final String PASSWORD = "hr";
    Connection connection;

    public Connection connect() throws SQLException {
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            return connection;
    }

    public void register() throws Exception {
        System.out.print("Register menu" + "\nPrint login: ");
        String login = scanner.next();
        System.out.print("Print email: ");
        String email = scanner.next();
        if(!email.matches("\\w+\\d*@\\w+\\.\\w+")) throw new Exception("Некорректный email!");
        System.out.print("Print password: ");
        String password = scanner.next();

        Connection connect = connect();
        PreparedStatement statement = connect.prepareStatement(
                "INSERT INTO Users(login, email, password, date_of_registration) VALUES (?, ?, ?, ?)");
        statement.setString(1, login);
        statement.setString(2, email);
        statement.setString(3, password);
        statement.setTimestamp(4,
                Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        try {
            statement.executeUpdate();
        } catch (PSQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Registration completed successfully!");
    }

    public void authorize() throws SQLException {
        System.out.print("Authorize menu" + "\nPrint your login: ");
        String login = scanner.next();
        System.out.print("Print your password: ");
        String password = scanner.next();

        Connection connect = connect();
        PreparedStatement statement = connect.prepareStatement(
                "SELECT * FROM Users");

        ResultSet resultSet = statement.executeQuery();
        boolean res = false;

        while (resultSet.next()) {
            if(resultSet.getString("login").equals(login) &&
                    resultSet.getString("password").equals(password))
                res = true;
        }
        statement = connect.prepareStatement(
                "INSERT INTO User_logs VALUES (?, ?, ?)"
        );
        statement.setString(1, login);
        statement.setTimestamp(2,
                Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        Logs logs;
        if(res) {
            logs = Logs.OK;
            System.out.println("Вы вошли в приложение!");
        }
        else {
            logs = Logs.FAIL;
            System.out.println("Неверный логин или пароль!");
        }
        statement.setString(3, String.valueOf(logs));
        statement.executeUpdate();
    }
}
