package com.gogirl.gogirl_service.service.impl.type;

import com.gogirl.gogirl_service.dao.type.TypeMapper;
import com.gogirl.gogirl_service.entity.Type;
import com.gogirl.gogirl_service.service.service.type.TypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by yinyong on 2018/9/4.
 */
@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeMapper typeMapper;

    @Override
    public List<Type> listType() {
        return typeMapper.listType();
    }

    @Override
    public int deleteTypeById(Integer id) {
        return typeMapper.deleteTypeById(id);
    }

    @Override
    public int insertType(Type type, String createUserName) {
        type.setCreateTime(new Date());
        type.setCreateUser(createUserName);
        return typeMapper.insertType(type);
    }

    @Override
    public int updateType(Type type) {
        return typeMapper.updateType(type);
    }

    @Override
    public Type queryTypeForDetail(Integer id) {
        return typeMapper.queryTypeForDetail(id);
    }
}
