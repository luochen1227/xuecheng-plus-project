package com.xuecheng.content.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.RestErrorResponse;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "课程教师编辑接口", tags = "课程教师编辑接口")
@RestController
public class CourseTeacherController {
    @Resource
    private CourseTeacherService courseTeacherServicel;

    @GetMapping("/courseTeacher/list/{courseId}")
    public List<CourseTeacher> getCourseTeacher(@PathVariable Long courseId) {
        List<CourseTeacher> courseTeacher = courseTeacherServicel.getCourseTeacher(courseId);
        return courseTeacher;
    }

    @PostMapping("/courseTeacher")
    public List<CourseTeacher> saveCourseTeacher(@RequestBody CourseTeacher courseTeacher) {
        courseTeacherServicel.saveOrUpdate(courseTeacher);
        List<CourseTeacher> result = courseTeacherServicel.saveCourseTeacher(courseTeacher);
        return result;
    }

        @DeleteMapping("/courseTeacher/course/{courseId}/{teachId}")
    public RestErrorResponse delCourseTeacher(@PathVariable Long courseId, @PathVariable Long teachId){
        LambdaQueryWrapper<CourseTeacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseTeacher::getCourseId,courseId);
        wrapper.eq(CourseTeacher::getId,teachId);
        courseTeacherServicel.remove(wrapper);
        RestErrorResponse restErrorResponse = new RestErrorResponse();
        restErrorResponse.setErrCode("200");
        return restErrorResponse;
    }
//    @DeleteMapping("/courseTeacher/course/{courseId}/{teachId}")
//    public void delCourseTeacher(@PathVariable Long courseId, @PathVariable Long teachId) {
//        LambdaQueryWrapper<CourseTeacher> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(CourseTeacher::getCourseId, courseId);
//        wrapper.eq(CourseTeacher::getId, teachId);
//        courseTeacherServicel.remove(wrapper);
//        XueChengPlusException.cast("","200");
//    }

}
