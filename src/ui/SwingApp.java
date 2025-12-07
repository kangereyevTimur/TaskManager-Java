package ui;

import com.formdev.flatlaf.FlatDarkLaf;
import model.Task;
import model.User;
import service.TaskManager;
import storage.FileStorage;
import storage.UserStorage;
import utils.LoggerUtil;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Главное окно приложения Task Manager с вкладками для задач.
 * Реализует авторизацию, управление задачами и сохранение данных пользователей.
 */
public class SwingApp extends JFrame {

    private final TaskManager manager;
    private final FileStorage storage;
    private final UserStorage userStorage = new UserStorage();
    private User currentUser;

    private DefaultListModel<Task> allModel, completedModel, overdueModel;
    private JList<Task> allList, completedList, overdueList;

    /**
     * Конструктор приложения. Инициализирует FlatLaf, авторизацию и UI.
     */
    public SwingApp() {
        FlatDarkLaf.setup();
        LoggerUtil.init();

        if (!showLoginDialog()) System.exit(0);

        setTitle("Task Manager — " + currentUser.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);

        storage = new FileStorage("tasks_" + currentUser.getUsername() + ".txt");
        manager = new TaskManager(storage);

        List<Task> loaded = storage.loadTasks();
        manager.getAllTasks().addAll(loaded);

        initUI();
        setVisible(true);
    }

    /**
     * Диалог авторизации и регистрации пользователя.
     */
    private boolean showLoginDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        panel.add(new JLabel("Логин:"));
        panel.add(usernameField);
        panel.add(new JLabel("Пароль:"));
        panel.add(passwordField);

        int option = JOptionPane.showConfirmDialog(null, panel, "Авторизация", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            List<User> users = userStorage.loadUsers();
            for (User u : users) {
                if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                    currentUser = u;
                    return true;
                }
            }

            int register = JOptionPane.showConfirmDialog(
                    null,
                    "Пользователь не найден. Зарегистрировать?",
                    "Регистрация",
                    JOptionPane.YES_NO_OPTION
            );

            if (register == JOptionPane.YES_OPTION) {
                User newUser = new User(username, password);
                userStorage.saveUser(newUser);
                currentUser = newUser;
                return true;
            }
        }

        return false;
    }

    /**
     * Инициализация интерфейса приложения с вкладками.
     */
    private void initUI() {
        allModel = new DefaultListModel<>();
        completedModel = new DefaultListModel<>();
        overdueModel = new DefaultListModel<>();

        allList = new JList<>(allModel);
        completedList = new JList<>(completedModel);
        overdueList = new JList<>(overdueModel);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Все задачи", new JScrollPane(allList));
        tabs.addTab("Выполненные ✅", new JScrollPane(completedList));
        tabs.addTab("Просроченные ⏰", new JScrollPane(overdueList));

        refreshLists();

        add(tabs, BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Добавить");
        JButton completeBtn = new JButton("Отметить выполненной");
        JButton deleteBtn = new JButton("Удалить");
        JButton sortBtn = new JButton("Сортировать по приоритету");

        controls.add(addBtn);
        controls.add(completeBtn);
        controls.add(deleteBtn);
        controls.add(sortBtn);

        add(controls, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> showAddTaskDialog());
        completeBtn.addActionListener(e -> markCompleted());
        deleteBtn.addActionListener(e -> removeTask());
        sortBtn.addActionListener(e -> sortTasks());
    }

    /**
     * Обновляет содержимое всех вкладок.
     */
    private void refreshLists() {
        allModel.clear();
        completedModel.clear();
        overdueModel.clear();

        List<Task> all = manager.getAllTasks();
        LocalDate today = LocalDate.now();

        for (Task t : all) {
            allModel.addElement(t);

            if (t.isCompleted()) {
                completedModel.addElement(t);
            } else if (t.getDeadline().isBefore(today)) {
                overdueModel.addElement(t);
            }
        }
    }

    /**
     * Диалог добавления новой задачи.
     */
    private void showAddTaskDialog() {
        JTextField titleField = new JTextField();
        JTextField descField = new JTextField();
        JComboBox<Integer> priorityBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        JTextField deadlineField = new JTextField(LocalDate.now().plusDays(7).toString());

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Название:"));
        panel.add(titleField);
        panel.add(new JLabel("Описание:"));
        panel.add(descField);
        panel.add(new JLabel("Приоритет (1–5):"));
        panel.add(priorityBox);
        panel.add(new JLabel("Дедлайн (YYYY-MM-DD):"));
        panel.add(deadlineField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Добавить задачу", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String desc = descField.getText().trim();
            int priority = (int) priorityBox.getSelectedItem();
            LocalDate deadline = LocalDate.parse(deadlineField.getText().trim());

            Task newTask = new Task(title, desc, priority, deadline);
            manager.addTask(newTask);
            storage.saveTasks(manager.getAllTasks());
            refreshLists();
        }
    }

    /**
     * Отмечает задачу как выполненную.
     */
    private void markCompleted() {
        Task selected = allList.getSelectedValue();

        if (selected != null) {
            selected.setCompleted(true);
            storage.saveTasks(manager.getAllTasks());
            refreshLists();
        }
    }

    /**
     * Удаляет выбранную задачу.
     */
    private void removeTask() {
        Task selected = allList.getSelectedValue();

        if (selected != null) {
            manager.removeTask(selected);
            storage.saveTasks(manager.getAllTasks());
            refreshLists();
        }
    }

    /**
     * Сортирует задачи по приоритету.
     */
    private void sortTasks() {
        List<Task> sorted = manager.getSortedByPriority();
        allModel.clear();
        sorted.forEach(allModel::addElement);
    }

    /**
     * Точка входа в приложение.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingApp::new);
    }
}
