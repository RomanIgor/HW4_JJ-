package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import java.util.List;

public class Task2 {

    public static void main(String[] args) {

        String user = System.getenv("USER");
        String password = System.getenv("PASSWORD");

        // Set the database credentials as system properties
        System.setProperty("hibernate.connection.username", user);
        System.setProperty("hibernate.connection.password", password);



        // 2.1 Описать сущность Book из пункта 1.1

        // 2.2 Создать EntityManager и сохранить в таблицу 10 книг
        try (EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("BookPU")) {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            EntityTransaction transaction = entityManager.getTransaction();

            try {
                transaction.begin();

                for (int i = 1; i <= 10; i++) {
                    Book book = new Book();
                    book.setName("Book " + i);
                    book.setAuthor("Author " + i);
                    entityManager.persist(book);
                }

                transaction.commit();
            } catch (PersistenceException e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                e.printStackTrace();
            } catch (RuntimeException e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                e.printStackTrace();
            } finally {
                entityManager.close();
            }
        }

        // 2.3 Выгрузить список книг какого-то автора
        try (EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("BookPU")) {
            EntityManager entityManager = entityManagerFactory.createEntityManager();

            try {
                String authorName = "Author 11";
                List<Book> books = entityManager.createQuery("SELECT b FROM Book b WHERE b.author = :author", Book.class)
                        .setParameter("author", authorName)
                        .getResultList();

                for (Book book : books) {
                    System.out.println("Book ID: " + book.getId() + ", Name: " + book.getName() + ", Author: " + book.getAuthor());
                }
            } finally {
                entityManager.close();
            }
        }
    }
}
