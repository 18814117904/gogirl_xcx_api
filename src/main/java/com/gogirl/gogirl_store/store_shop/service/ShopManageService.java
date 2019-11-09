package com.gogirl.gogirl_store.store_shop.service;

import com.gogirl.gogirl_store.store_commons.dto.ShopManage;

import java.util.List;

/**
 * Created by yinyong on 2018/9/17.
 */
public interface ShopManageService {

    List<ShopManage> listShopManageForPage(ShopManage shopManage);

    List<ShopManage> listShopByServeIdForPage(Integer id);

    ShopManage getShopManageForDetail(Integer id);

    int updateShopManage(ShopManage shopManage);

    int deleteShopManageById(Integer id);

    int insertShopManage(ShopManage shopManage);
}
