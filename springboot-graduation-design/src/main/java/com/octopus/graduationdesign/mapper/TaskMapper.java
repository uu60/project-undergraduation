package com.octopus.graduationdesign.mapper;

import com.octopus.graduationdesign.pojo.dao.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskMapper {

    void addTask(@Param("task") Task task);

    void updateProcessTask(@Param("task") Task task);

    void updateFailTask(@Param("task") Task task);

    void updateSucceedTask(@Param("task") Task task);

    List<Task> getTasksByState(String username, List<Integer> states);

    String getResultPathById(Integer id);

    void deleteTask(Task task);
}
