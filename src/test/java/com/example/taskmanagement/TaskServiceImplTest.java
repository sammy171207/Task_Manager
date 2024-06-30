package com.example.taskmanagement;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.service.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTasks() {
        Task task1 = new Task();
        Task task2 = new Task();
        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetTaskById() {
        Task task = new Task();
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTaskById_NotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Task result = taskService.getTaskById(1L);

        assertNull(result);
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateTask() {
        Task task = new Task();
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.createTask(task);

        assertNotNull(result);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testUpdateTask() {
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");

        Task updatedTask = new Task();
        updatedTask.setTitle("New Title");
        updatedTask.setDescription("New Description");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        Task result = taskService.updateTask(1L, updatedTask);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        assertEquals("New Description", result.getDescription());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void testUpdateTask_NotFound() {
        Task updatedTask = new Task();
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Task result = taskService.updateTask(1L, updatedTask);

        assertNull(result);
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteTask() {
        Long id = 1L;

        taskService.deleteTask(id);

        verify(taskRepository, times(1)).deleteById(id);
    }
}
