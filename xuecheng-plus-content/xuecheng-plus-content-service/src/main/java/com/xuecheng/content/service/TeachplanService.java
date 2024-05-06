package com.xuecheng.content.service;

import com.xuecheng.base.exception.RestErrorResponse;
import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.TeachplanMedia;

import java.util.List;

/**
 * @author Mr.M
 * @version 1.0
 * @description 课程计划管理相关接口
 * @date 2023/2/14 12:10
 */
public interface TeachplanService {
    /**
     * 根据课程id查询课程计划
     *
     * @param courseId 课程计划
     * @return
     */
    List<TeachplanDto> findTeachplanTree(Long courseId);

    /**
     * 新增/修改/保存课程计划
     *
     * @param saveTeachplanDto
     */
    void saveTeachplan(SaveTeachplanDto saveTeachplanDto);

    RestErrorResponse delTeachplan(Long teachId);

    void delTeachplan1(Long teachId);

    void maveupTeachplan(Long teachId);

    void mavedownTeachplan(Long teachId);



}
