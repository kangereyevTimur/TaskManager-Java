package storage;

import model.Task;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс отвечает за сохранение и загрузку задач из текстового файла.
 * Используется для долговременного хранения данных между запусками программы.
 *
 * @author Timur
 * @version 1.2
 */
public class FileStorage {

    private final String fileName;

    /**
     * Создает объект для работы с файлом.
     *
     * @param fileName имя файла, в котором будут храниться задачи
     */
    public FileStorage(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Сохраняет список задач в файл.
     *
     * @param tasks список задач для сохранения
     */
    public void saveTasks(List<Task> tasks) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Task t : tasks) {
                writer.printf("%s;%s;%d;%s;%b%n",
                        t.getTitle(),
                        t.getDescription(),
                        t.getPriority(),
                        t.getDeadline(),
                        t.isCompleted());
            }
        } catch (IOException e) {
            System.err.println("Ошибка сохранения задач: " + e.getMessage());
        }
    }

    /**
     * Загружает задачи из файла.
     *
     * @return список загруженных задач
     */
    public List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) return tasks;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 5) {
                    Task task = new Task(
                            parts[0],
                            parts[1],
                            Integer.parseInt(parts[2]),
                            LocalDate.parse(parts[3])
                    );
                    task.setCompleted(Boolean.parseBoolean(parts[4]));
                    tasks.add(task);
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка загрузки задач: " + e.getMessage());
        }
        return tasks;
    }
}
