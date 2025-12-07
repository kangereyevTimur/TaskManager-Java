package model;

import java.io.Serializable;

/**
 * Базовый класс для всех типов задач.
 * Содержит общие свойства — идентификатор, название и статус выполнения.
 * Используется как родитель для {@link model.Task}.
 *
 * @author Timur
 * @version 1.0
 */
public abstract class BaseTask implements Serializable {

    private static int nextId = 1;
    protected int id;
    protected String title;
    protected boolean completed;

    /**
     * Создает базовую задачу с указанным названием.
     *
     * @param title название задачи
     */
    public BaseTask(String title) {
        this.id = nextId++;
        this.title = title;
        this.completed = false;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    /**
     * Устанавливает статус выполнения задачи.
     *
     * @param completed true — если выполнена
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (ID: %d)",
                completed ? "✅" : "❌", title, id);
    }
}
