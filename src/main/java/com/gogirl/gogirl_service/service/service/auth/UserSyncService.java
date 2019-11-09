package com.gogirl.gogirl_service.service.service.auth;

import com.gogirl.gogirl_service.entity.GetByBLUserAcct;

import javax.servlet.http.HttpServletRequest;

public interface UserSyncService {

    GetByBLUserAcct getUserByCookie(HttpServletRequest request);
}
