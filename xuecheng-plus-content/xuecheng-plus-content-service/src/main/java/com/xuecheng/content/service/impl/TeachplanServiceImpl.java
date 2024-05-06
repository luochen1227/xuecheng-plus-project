package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.RestErrorResponse;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Mr.M
 * @version 1.0
 * @description TODO
 * @date 2023/2/14 12:11
 */
@Service
public class TeachplanServiceImpl implements TeachplanService {

    @Autowired
    TeachplanMapper teachplanMapper;
    @Resource
    TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public List<TeachplanDto> findTeachplanTree(Long courseId) {
        List<TeachplanDto> teachplanDtos = teachplanMapper.selectTreeNodes(courseId);
        return teachplanDtos;
    }

    private int getTeachplanCount(Long courseId, Long parentId) {
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper = queryWrapper.eq(Teachplan::getCourseId, courseId).eq(Teachplan::getParentid, parentId);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        return count + 1;
    }

    @Override
    public void saveTeachplan(SaveTeachplanDto saveTeachplanDto) {
        //通过课程计划id判断是新增和修改
        Long teachplanId = saveTeachplanDto.getId();
        if (teachplanId == null) {
            //新增
            Teachplan teachplan = new Teachplan();
            BeanUtils.copyProperties(saveTeachplanDto, teachplan);
            //确定排序字段，找到它的同级节点个数，排序字段就是个数加1  select count(1) from teachplan where course_id=117 and parentid=268
            Long parentid = saveTeachplanDto.getParentid();
            Long courseId = saveTeachplanDto.getCourseId();
            int teachplanCount = getTeachplanCount(courseId, parentid);
            teachplan.setOrderby(teachplanCount);
            teachplanMapper.insert(teachplan);

        } else {
            //修改
            Teachplan teachplan = teachplanMapper.selectById(teachplanId);
            //将参数复制到teachplan
            BeanUtils.copyProperties(saveTeachplanDto, teachplan);
            teachplanMapper.updateById(teachplan);
        }

    }

        @Override
    public RestErrorResponse delTeachplan(Long teachId) {
        RestErrorResponse restErrorResponse = new RestErrorResponse();
        Teachplan teachplan = teachplanMapper.selectById(teachId);
        Long parentid = teachplan.getParentid();
        if (parentid != 0) {
            teachplanMapper.deleteById(teachId);
            restErrorResponse.setErrCode("200");
            LambdaQueryWrapper<TeachplanMedia> teachplanMediaLambdaQueryWrapper = new LambdaQueryWrapper<>();
            teachplanMediaLambdaQueryWrapper.eq(TeachplanMedia::getId,teachId);
            Integer i = teachplanMediaMapper.selectCount(teachplanMediaLambdaQueryWrapper);
            if (i == 0){
                return restErrorResponse;
            }
            teachplanMediaMapper.delete(teachplanMediaLambdaQueryWrapper);
            return restErrorResponse;
        }
        LambdaQueryWrapper<Teachplan> teachplanLambdaQueryWrapper = new LambdaQueryWrapper<>();
        teachplanLambdaQueryWrapper.eq(Teachplan::getParentid, teachId);
//        List<Teachplan> teachplans = teachplanMapper.selectList(teachplanLambdaQueryWrapper);
        Integer i = teachplanMapper.selectCount(teachplanLambdaQueryWrapper);
        if (i == 0){
            teachplanMapper.deleteById(teachId);
            restErrorResponse.setErrCode("200");
            return restErrorResponse;
        }else {
            restErrorResponse.setErrCode("120409");
            restErrorResponse.setErrMessage("课程计划信息还有子级信息，无法操作");
            return restErrorResponse;

        }
    }
    @Override
    public void delTeachplan1(Long teachId) {

        Teachplan teachplan = teachplanMapper.selectById(teachId);
        Long parentid = teachplan.getParentid();
        if (parentid != 0) {
            teachplanMapper.deleteById(teachId);
            LambdaQueryWrapper<TeachplanMedia> teachplanMediaLambdaQueryWrapper = new LambdaQueryWrapper<>();
            teachplanMediaLambdaQueryWrapper.eq(TeachplanMedia::getTeachplanId, teachId);
            Integer i = teachplanMediaMapper.selectCount(teachplanMediaLambdaQueryWrapper);
            System.out.println(i);
            if (i != 0) {
                teachplanMediaMapper.delete(teachplanMediaLambdaQueryWrapper);
            }
        }
        LambdaQueryWrapper<Teachplan> teachplanLambdaQueryWrapper = new LambdaQueryWrapper<>();
        teachplanLambdaQueryWrapper.eq(Teachplan::getParentid, teachId);
//        List<Teachplan> teachplans = teachplanMapper.selectList(teachplanLambdaQueryWrapper);
        Integer i = teachplanMapper.selectCount(teachplanLambdaQueryWrapper);
        if (i == 0) {
            teachplanMapper.deleteById(teachId);
        } else {
            XueChengPlusException.cast("课程计划信息还有子级信息，无法操作","120409");
        }

    }

    @Override
    public void maveupTeachplan(Long teachId) {
        Teachplan teachplan = teachplanMapper.selectById(teachId);
        Integer orderby = teachplan.getOrderby();
        Long parentid = teachplan.getParentid();
        if (parentid != 0){
            LambdaQueryWrapper<Teachplan> teachplanLambdaQueryWrapper = new LambdaQueryWrapper<>();
            teachplanLambdaQueryWrapper.eq(Teachplan::getParentid,parentid);
            teachplanLambdaQueryWrapper.eq(Teachplan::getOrderby,orderby-1);
            List<Teachplan> teachplans = teachplanMapper.selectList(teachplanLambdaQueryWrapper);
            Teachplan teachplan2 = new Teachplan();
            if (!teachplans.isEmpty()){
                Teachplan teachplan1 = teachplans.get(0);
                BeanUtils.copyProperties(teachplan1, teachplan2);
            }
            if (orderby > 1 ){
                teachplan2.setOrderby(orderby);
                teachplan.setOrderby(orderby - 1);
                teachplanMapper.updateById(teachplan);
                teachplanMapper.updateById(teachplan2);
            }
        }else{
            Long courseId = teachplan.getCourseId();
            LambdaQueryWrapper<Teachplan> teachplanQueryWrapper = new LambdaQueryWrapper<>();
            teachplanQueryWrapper.eq(Teachplan::getCourseId,courseId);
            teachplanQueryWrapper.eq(Teachplan::getParentid,0);
            teachplanQueryWrapper.eq(Teachplan::getOrderby,orderby-1);
            List<Teachplan> teachplans = teachplanMapper.selectList(teachplanQueryWrapper);
            Teachplan teachplan2 = new Teachplan();
            if (!teachplans.isEmpty()){
                Teachplan teachplan1 = teachplans.get(0);
                BeanUtils.copyProperties(teachplan1, teachplan2);
            }
            if (orderby > 1 ){
                teachplan2.setOrderby(orderby);
                teachplan.setOrderby(orderby - 1);
                teachplanMapper.updateById(teachplan);
                teachplanMapper.updateById(teachplan2);
            }
        }
    }

    @Override
    public void mavedownTeachplan(Long teachId) {
        Teachplan teachplan = teachplanMapper.selectById(teachId);
        Integer orderby = teachplan.getOrderby();
        Long parentid = teachplan.getParentid();
        if (parentid != 0){
            LambdaQueryWrapper<Teachplan> teachplanLambdaQueryWrapper = new LambdaQueryWrapper<>();
            teachplanLambdaQueryWrapper.eq(Teachplan::getParentid,parentid);
            teachplanLambdaQueryWrapper.eq(Teachplan::getOrderby,orderby+1);
            List<Teachplan> teachplans = teachplanMapper.selectList(teachplanLambdaQueryWrapper);
            Teachplan teachplan2 = new Teachplan();
            if (!teachplans.isEmpty()){
                Teachplan teachplan1 = teachplans.get(0);
                BeanUtils.copyProperties(teachplan1, teachplan2);
            }
            if (orderby >= 1 ){
                teachplan2.setOrderby(orderby);
                teachplan.setOrderby(orderby + 1);
                teachplanMapper.updateById(teachplan);
                teachplanMapper.updateById(teachplan2);
            }
        }else{
            Long courseId = teachplan.getCourseId();
            LambdaQueryWrapper<Teachplan> teachplanQueryWrapper = new LambdaQueryWrapper<>();
            teachplanQueryWrapper.eq(Teachplan::getCourseId,courseId);
            teachplanQueryWrapper.eq(Teachplan::getParentid,0);
            teachplanQueryWrapper.eq(Teachplan::getOrderby,orderby+1);
            List<Teachplan> teachplans = teachplanMapper.selectList(teachplanQueryWrapper);
            Teachplan teachplan2 = new Teachplan();
            if (!teachplans.isEmpty()){
                Teachplan teachplan1 = teachplans.get(0);
                BeanUtils.copyProperties(teachplan1, teachplan2);
            }
            if (orderby >= 1 ){
                teachplan2.setOrderby(orderby);
                teachplan.setOrderby(orderby + 1);
                teachplanMapper.updateById(teachplan);
                teachplanMapper.updateById(teachplan2);
            }
        }
    }
}
