package com.gogirl.gogirl_service.service.service.label;

import com.gogirl.gogirl_service.entity.Label;

import java.util.List;

/**
 * Created by yinyong on 2018/9/3.
 */
public interface LabelService {

    List<Label> listLabel();

    int insertLabel(Label label, String createUserName);

    int deleteLabelById(Integer id);

    int updateLabel(Label label);
}
