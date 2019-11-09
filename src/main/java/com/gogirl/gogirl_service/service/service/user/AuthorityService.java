package com.gogirl.gogirl_service.service.service.user;

import java.util.List;
import java.util.Map;

/**
 * Created by yinyong on 2019/5/7.
 */
public interface AuthorityService {

    public List<Map> getMenuByPermission(Map map);
}
