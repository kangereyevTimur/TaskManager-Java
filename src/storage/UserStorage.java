package storage;

import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс UserStorage отвечает за хранение и загрузку пользователей из файла users.txt.
 * Используется для авторизации и регистрации.
 */
public class UserStorage {
    private final String filePath = "users.txt";

    /**
     * Загружает всех пользователей из файла.
     *
     * @return список пользователей
     */
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.add(new User(parts[0], parts[1]));
                }
            }
        } catch (IOException ignored) {}
        return users;
    }

    /**
     * Добавляет нового пользователя в файл.
     *
     * @param user новый пользователь
     */
    public void saveUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(user.getUsername() + "," + user.getPassword());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
