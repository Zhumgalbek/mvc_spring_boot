package com.example.peaksoft.service.impl;

import com.example.peaksoft.model.Lesson;
import com.example.peaksoft.model.Task;
import com.example.peaksoft.repository.LessonRepository;
import com.example.peaksoft.repository.TaskRepository;
import com.example.peaksoft.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final LessonRepository lessonRepository;

    @Override
    public List<Task> getAllTasks(Long id) {
        return taskRepository.getAllTasks(id);
    }

    @Override
    public void addTask(Long id, Task task) {
        Lesson lesson = lessonRepository.findById(id).get();
        lesson.addTask(task);
        task.setLesson(lesson);
        taskRepository.save(task);
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.getById(id);
    }

    @Override
    public void updateTask(Task task, Long id) {
        Task task1 = taskRepository.findById(id).get();
        task1.setTaskName(task.getTaskName());
        task1.setTaskText(task.getTaskText());
        task1.setDeadLine(task.getDeadLine());
        taskRepository.save(task1);
    }

    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id).get();
        taskRepository.delete(task);
    }
}
