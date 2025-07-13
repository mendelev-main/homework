package org.example.dao;

import org.example.model.User;
import org.example.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDAOTest {

    @Container
    private final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test_db")
            .withUsername("postgres")
            .withPassword("postgres");

    private UserDAO userDAO;
    private SessionFactory sessionFactory;

    @BeforeAll
    void startContainerAndSetupHibernate() {
        postgres.start();

        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());

        sessionFactory = HibernateUtil.getSessionFactory();
        userDAO = new UserDAOImpl();
    }



    @Test
    void testSaveAndFindById() {
        User user = new User();
        user.setName("Ivan");
        user.setEmail("ivan@gmail.com");
        user.setAge(28);

        userDAO.save(user);

        User found = userDAO.findById(user.getId());

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Ivan");
        assertThat(found.getEmail()).isEqualTo("ivan@gmail.com");
    }

    @Test
    void testFindAll() {
        User user = new User();
        user.setName("Maria");
        user.setEmail("maria@gmail.com");
        user.setAge(30);

        userDAO.save(user);

        List<User> users = userDAO.findAll();
        assertThat(users).isNotEmpty();
        assertThat(users).anyMatch(u -> u.getEmail().equals("maria@gmail.com"));
    }

    @Test
    void testUpdate(){
        User user = new User();
        user.setName("Valera");
        user.setEmail("valera@gmail.com");
        user.setAge(25);
        userDAO.save(user);

        User newUser = userDAO.findById(user.getId());
        newUser.setName("Valera-Updated");
        newUser.setEmail("Valera-Updated@gmail.com");

        userDAO.update(newUser);

        assertEquals(user.getId(), newUser.getId());
        assertEquals("Valera-Updated", userDAO.findById(user.getId()).getName());
        assertEquals("Valera-Updated@gmail.com", userDAO.findById(user.getId()).getEmail());
    }

    @Test
    void testDelete(){
        User user = new User();
        user.setName("Dmitry");
        user.setEmail("dmitry@gmail.com");
        user.setAge(27);
        userDAO.save(user);

        userDAO.delete(user.getId());

        User deleted = userDAO.findById(user.getId());
        assertNull(deleted);

    }

    @AfterAll
    void stopContainer() {
        var session = sessionFactory.openSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM User").executeUpdate();
        session.getTransaction().commit();
        session.close();

        postgres.stop();
    }


}