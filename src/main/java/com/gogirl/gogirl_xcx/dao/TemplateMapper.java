package com.gogirl.gogirl_xcx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_xcx.entity.Template;

@Mapper
public interface TemplateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Template record);

    int insertSelective(Template record);

    List<Template> selectByTemplate(Template template);
    Template selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Template record);

    int updateByPrimaryKey(Template record);
}