package model;

import java.time.LocalDate;

/**
 * Класс задачи, наследующий {@link model.BaseTask}.
 * Добавляет описание, приоритет и дедлайн.
 *
 * @author Timur
 *
 */
public class Task extends BaseTask {

    private String description;
    private int priority;
    private LocalDate deadline;

    /**
     * Создает задачу с полным набором параметров.
     *
     * @param title       название
     * @param description описание
     * @param priority    приоритет (1 — высокий, 3 — низкий)
     * @param deadline    дата дедлайна
     */
    public Task(String title, String description, int priority, LocalDate deadline) {
        super(title);
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s — %s (приоритет: %d, дедлайн: %s)",
                completed ? "✅" : "❌",
                title,
                description,
                priority,
                deadline);
    }
}
