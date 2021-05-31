package com.devopsassignment.Momento.exceptionhandling;

public class TaskNotFoundExcptn extends RuntimeException {

    public TaskNotFoundExcptn(Long id) {
        super("Could not find task " + id);
    }
}
