package com.devopsassignment.Momento.controller;

import com.devopsassignment.Momento.security.JwtTokenUtil;
import com.devopsassignment.Momento.model.AuthenticationRequest;
import com.devopsassignment.Momento.model.JWTTokenResponse;
import com.devopsassignment.Momento.model.Task;
import com.devopsassignment.Momento.service.TaskService;
import com.devopsassignment.Momento.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping
public class TaskController {
    @Autowired
    TaskService taskService;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UsersService usersService;


    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest){
        try{
            authenticate(authenticationRequest.getUserName(), authenticationRequest.getPassword());
        }
        catch (Exception e) {
            HashMap<String, String> response = new HashMap<String, String>();
            response.put("Error:", e.getMessage());
//            logger.error("Error in getting all managers inumbers" + e.getMessage());
            return new ResponseEntity<HashMap<String, String>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        final UserDetails userDetails = usersService
                .loadUserByUsername(authenticationRequest.getUserName());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JWTTokenResponse(token));
    }

    @GetMapping("/api/tasks")
    public List<Task> getTasks(){
        return taskService.getTasks();
    }

    @PostMapping("/api/tasks")
    public Task postTask(@RequestBody Task newTask){
        System.out.println("In controller");
        return taskService.saveTask(newTask);
    }

    @GetMapping("/api/tasks/{id}")
    public Task getTask(@PathVariable Long id) {
        return taskService.findById(id);
//                .orElseThrow(() -> new TaskNotFoundExcptn(id));
    }

    @PutMapping("/api/tasks/{id}")
    public Task updateTask(@RequestBody Task newTask, @PathVariable Long id) {
//        return taskService.findById(id);
        if(taskService.findById(id)!=null){
            taskService.deleteById(id);
            return taskService.saveTask(newTask);
        }
        else{
            return taskService.saveTask(newTask);
        }
//                .map(task -> {
//                    return taskService.saveTask(task);
//                })
//                .orElseGet(() -> {
//                    newTask.setId(id);
//                    return taskService.saveTask(newTask);
//                });
    }

    @DeleteMapping("/api/tasks/{id}")
    void deleteTask(@PathVariable Long id) {
        taskService.deleteById(id);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}

