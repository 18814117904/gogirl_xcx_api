package com.gogirl.gogirl_service.service.service.type;

import com.gogirl.gogirl_service.entity.Type;

import java.util.List;

/**
 * Created by yinyong on 2018/9/4.
 */
public interface TypeService {

    List<Type> listType();

    int deleteTypeById(Integer id);

    int insertType(Type type, String createUserName);

    int updateType(Type type);

    Type queryTypeForDetail(Integer id);
}
