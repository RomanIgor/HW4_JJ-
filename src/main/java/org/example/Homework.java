package org.example;
/**
 * Задания необходимо выполнять на ЛЮБОЙ СУБД (postgres, mysql, sqllite, h2, ...)
 *
 * 1. С помощью JDBC выполнить:
 * 1.1 Создать таблицу book с колонками id bigint, name varchar, author varchar, ...
 * 1.2 Добавить в таблицу 10 книг
 * 1.3 Сделать запрос select from book where author = 'какое-то имя' и прочитать его с помощью ResultSet
 */




import jakarta.persistence.EntityManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Homework {

    public static void main(String[] args) {
        // 1.1 Создать таблицу book
        createBookTable();

        // 1.2 Добавить в таблицу 10 книг
        insertBooks();

        // 1.3 Сделать запрос select from book where author = 'какое-то имя' и прочитать его с помощью ResultSet
        selectBooksByAuthor("Author 20");
    }
    static String url = "jdbc:postgresql://localhost:5432/persons";
    static String username = System.getenv("user");
    static String password = System.getenv("password");
    private static void createBookTable() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            String createTableQuery = "CREATE TABLE IF NOT EXISTS book (id BIGINT, name VARCHAR, author VARCHAR)";
            statement.execute(createTableQuery);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertBooks() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO book VALUES (?, ?, ?)")) {

            for (int i = 1; i <= 20; i++) {
                preparedStatement.setLong(1, i);
                preparedStatement.setString(2, "Book " + i);
                preparedStatement.setString(3, "Author " + i);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void selectBooksByAuthor(String author) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM book WHERE author = ?")) {

            preparedStatement.setString(1, author);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) {
                    System.out.println("No books found for author: " + author);
                } else {
                    while (resultSet.next()) {
                        long id = resultSet.getLong("id");
                        String name = resultSet.getString("name");
                        String bookAuthor = resultSet.getString("author");
                        System.out.println("Book ID: " + id + ", Name: " + name + ", Author: " + bookAuthor);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }}





