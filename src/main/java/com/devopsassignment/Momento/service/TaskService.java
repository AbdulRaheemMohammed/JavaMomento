package com.devopsassignment.Momento.service;

import com.devopsassignment.Momento.model.Task;
import com.devopsassignment.Momento.repository.TaskFileRepository;
import com.devopsassignment.Momento.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskFileRepository taskFileRepository;

    public Task saveTask(Task newTask) {
//        System.out.println(taskFileRepository.saveTask(newTask));
        return taskFileRepository.saveTask(newTask);
//        return taskRepository.save(newTask);
    }

    public List<Task> getTasks() {
        System.out.println(taskFileRepository.getTasks());
        return taskFileRepository.getTasks();
//        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        System.out.println(taskFileRepository.findById(id));
        return taskFileRepository.findById(id);
//        return taskRepository.findById(id);
    }

    public void deleteById(Long id) {
        taskFileRepository.deleteById(id);
//        taskRepository.deleteById(id);
    }
}
