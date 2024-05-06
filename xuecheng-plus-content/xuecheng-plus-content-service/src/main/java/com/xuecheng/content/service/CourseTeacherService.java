package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.po.CourseTeacher;

import java.util.List;

public interface CourseTeacherService extends IService<CourseTeacher> {
    List<CourseTeacher> getCourseTeacher(Long courseId);

    List<CourseTeacher> saveCourseTeacher(CourseTeacher courseTeacher);

}
