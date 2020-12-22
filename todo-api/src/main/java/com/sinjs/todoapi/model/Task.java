package com.sinjs.todoapi.model;

import io.swagger.annotations.*;
import lombok.*;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;

@NoArgsConstructor
@Data
@Entity
@ApiModel("Task")
public class Task {

    @Id
    @GeneratedValue
    @ApiModelProperty("Primary Key - ID")
    private Long id;

    @ApiModelProperty("Content of the task")
    @Size(max = 20)
    private String content;

    @ApiModelProperty("Completed or not")
    private boolean completed;

    @ApiModelProperty("Last update time")
    private Date editTime;

}
