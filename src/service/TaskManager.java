package service;

import model.Task;
import storage.FileStorage;
import utils.LoggerUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Класс TaskManager реализует логику управления задачами:
 * добавление, удаление, сортировка и сохранение данных.
 * Использует {@link FileStorage} для хранения задач.
 *
 * Вся активность логируется с помощью {@link LoggerUtil}.
 *
 * @author Timur
 * @version 1.2
 */
public class TaskManager {

    private final List<Task> tasks;
    private final FileStorage storage;

    /**
     * Конструктор менеджера задач.
     *
     * @param storage объект для работы с файловым хранилищем
     */
    public TaskManager(FileStorage storage) {
        this.tasks = new ArrayList<>();
        this.storage = storage;
        LoggerUtil.init();
        LoggerUtil.info("TaskManager инициализирован");
    }

    /**
     * Добавляет задачу в список.
     *
     * @param task объект задачи для добавления
     */
    public void addTask(Task task) {
        tasks.add(task);
        LoggerUtil.info("Добавлена задача: " + task.getTitle());
    }

    /**
     * Удаляет задачу из списка.
     *
     * @param task задача, которую нужно удалить
     */
    public void removeTask(Task task) {
        if (tasks.remove(task)) {
            LoggerUtil.info("Удалена задача: " + task.getTitle());
        } else {
            LoggerUtil.warn("Попытка удалить несуществующую задачу: " + task.getTitle());
        }
    }

    /**
     * Возвращает все задачи.
     *
     * @return список всех задач
     */
    public List<Task> getAllTasks() {
        return tasks;
    }

    /**
     * Возвращает задачи, отсортированные по приоритету.
     *
     * @return новый список задач, отсортированных по приоритету
     */
    public List<Task> getSortedByPriority() {
        LoggerUtil.info("Сортировка задач по приоритету");
        return tasks.stream()
                .sorted(Comparator.comparingInt(Task::getPriority))
                .toList();
    }

    /**
     * Отмечает задачу как выполненную.
     *
     * @param task задача для отметки выполнения
     */
    public void markAsCompleted(Task task) {
        if (task != null) {
            task.setCompleted(true);
            LoggerUtil.info("Задача выполнена: " + task.getTitle());
        } else {
            LoggerUtil.warn("Попытка отметить как выполненную null-задачу");
        }
    }

    /**
     * Сохраняет текущее состояние списка задач в файл.
     */
    public void saveAll() {
        try {
            storage.saveTasks(tasks);
            LoggerUtil.info("Все задачи сохранены в файл");
        } catch (Exception e) {
            LoggerUtil.error("Ошибка при сохранении задач", e);
        }
    }
}
