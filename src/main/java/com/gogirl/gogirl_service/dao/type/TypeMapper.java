package com.gogirl.gogirl_service.dao.type;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.gogirl.gogirl_service.entity.Type;

import java.util.List;

/**
 * Created by yinyong on 2018/9/4.
 */
@Mapper
@Repository
public interface TypeMapper {

    List<Type> listType();

    int deleteTypeById(Integer id);

    int insertType(Type type);

    int updateType(Type type);

    Type queryTypeForDetail(Integer id);
}
