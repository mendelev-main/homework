package org.example;

import org.example.dao.UserDAO;
import org.example.dao.UserDAOImpl;
import org.example.model.User;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAOImpl();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1 - Создать пользователя");
            System.out.println("2 - Показать всех");
            System.out.println("3 - Найти по id");
            System.out.println("4 - Обновить пользователя");
            System.out.println("5 - Удалить пользователя");
            System.out.println("0 - Выход");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Age: ");
                    int age = Integer.parseInt(scanner.nextLine());

                    userDAO.save(new User(name, email, age));
                    break;
                case "2":
                    List<User> users = userDAO.findAll();
                    users.forEach(System.out::println);
                    break;
                case "3":
                    System.out.print("id: ");
                    Long id = Long.parseLong(scanner.nextLine());
                    System.out.println(userDAO.findById(id));
                    break;
                case "4":
                    System.out.println("id :");
                    Long updateId = Long.parseLong(scanner.nextLine());
                    User u = userDAO.findById(updateId);
                    if (u == null) {
                        System.out.println("Такого пользователя нет!");
                        break;
                    }
                    System.out.print("New name: ");
                    u.setName(scanner.nextLine());
                    System.out.print("New email: ");
                    u.setEmail(scanner.nextLine());
                    System.out.print("New age: ");
                    u.setAge(Integer.parseInt(scanner.nextLine()));
                    userDAO.update(u);
                    break;
                case "5":
                    System.out.print("id: ");
                    Long deleteId = Long.parseLong(scanner.nextLine());
                    userDAO.delete(deleteId);
                    break;
                case "0":
                    System.out.println("Exit...");
                    System.exit(0);
            }
        }
    }
}