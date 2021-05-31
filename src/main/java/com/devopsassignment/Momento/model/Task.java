package com.devopsassignment.Momento.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Task {

    @Id
    private long id;
    private String name;
    private String description;

    @Override
    public String toString() {
        return "Task{" +
                "id"+":" + id +
                ", \n name'" + name + '\'' +
                ", \n description='" + description + '\'' +
                '}';
    }
}
