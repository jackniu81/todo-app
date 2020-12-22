package com.sinjs.todoapi.controller;

import com.sinjs.todoapi.dao.TaskRepository;
import com.sinjs.todoapi.model.Task;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api(tags="Task Management")
@RequestMapping("/todo-api/v1/tasks")
@RestController
public class TaskController {
    @Autowired
    TaskRepository taskRepository;

    @ApiOperation("Get task list ")
    @GetMapping("/")
    public List<Task> index(@ApiParam("filter by completed. Empty means all") @RequestParam(required = false) Boolean completed) {

        return completed == null?
                taskRepository.findAll():
                taskRepository.findByCompleted(completed);
    }

    @ApiOperation("Create a new task")
    @PostMapping("/")
    public String postTask(@RequestBody Task task) {
        task.setEditTime(new Date());
        this.taskRepository.save(task);
        return "success";
    }

    @ApiOperation("Update a task")
    @PutMapping("/{id}")
    public String putTask(@PathVariable Long id, @RequestBody Task newTask) {
        newTask.setEditTime(new Date());
        this.taskRepository.save((newTask));
        return "success";
    }
}
