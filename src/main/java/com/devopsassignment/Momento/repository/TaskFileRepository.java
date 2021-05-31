package com.devopsassignment.Momento.repository;

import com.devopsassignment.Momento.model.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskFileRepository {

    String fileName = "src/main/resources/tasks.json";

    public JSONArray readFile(){
        System.out.println("Reading file");
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonFile = new JSONArray();

        try (FileReader reader = new FileReader(fileName))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            jsonFile = (JSONArray) obj;
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonFile;
    }

    public void writeToFile(JSONArray jsonFile){
        try(FileWriter writer = new FileWriter(fileName)){
            System.out.println("Writing to file");
            writer.write(jsonFile.toJSONString());
            writer.flush();
            writer.close();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public ArrayList<Task> getTasks(){

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Task> taskList = new ArrayList<>();
        JSONArray taskJsonList = readFile();
        taskList = gson.fromJson(taskJsonList.toJSONString(), new TypeToken<ArrayList<Task>>() {}.getType());
        HashSet<Task> hset = new HashSet<>(taskList);
        ArrayList<Task> uniqueTaskList = new ArrayList<>(hset);

        return uniqueTaskList;
    }

    public Task saveTask(Task newTask){
        System.out.println("Saving Task to file");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JSONParser jsonParser = new JSONParser();
        try {
            if(findById(newTask.getId())==null){
                System.out.println("Task not already present in db");
                ArrayList<Task> uniqueTaskList = getTasks();
                JSONObject newTaskjson = (JSONObject) jsonParser.parse(gson.toJson(newTask));
                System.out.println(newTaskjson);
                JSONArray taskJsonList = readFile();
                taskJsonList.add(newTaskjson);
                writeToFile(taskJsonList);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newTask;
    }

    public Task findById(Long id){
        List<Task> allTasks = getTasks();
        for (Task task: allTasks){
            if(task.getId()==id){
                return task;
            }
        }
        return null;
    }

    public void deleteById(Long id){
        System.out.println("Deleting task from file");
        if(findById(id)!=null){
            System.out.println("Found the task in the file");
            List<Task> allTasks = getTasks();
            for(Task task: allTasks){
                if(task.getId()==id){
                    allTasks.remove(task);
                    System.out.println(allTasks);
                    break;
                }
            }
            try{
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JSONParser jsonParser = new JSONParser();
                JSONArray modifiedTaskList = (JSONArray) jsonParser.parse(gson.toJson(allTasks));
                writeToFile(modifiedTaskList);
            }catch (ParseException e){
                e.printStackTrace();
            }
        }
    }
}
