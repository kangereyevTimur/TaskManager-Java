package model;

/**
 * Класс User представляет учетную запись пользователя.
 * Содержит логин и пароль.
 *
 * @author Timur
 * @version 1.0
 */
public class User {
    private String username;
    private String password;

    /**
     * Конструктор для создания пользователя.
     *
     * @param username логин
     * @param password пароль
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return username;
    }
}
