package com.gogirl.gogirl_store.store_shop.dao;

import com.gogirl.gogirl_store.store_commons.dto.ShopManage;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yinyong on 2018/9/17.
 */
@Mapper
@Repository
public interface ShopManageMapper {

    List<ShopManage> listShopManageForPage(ShopManage shopManage);

    List<ShopManage> listShopByServeIdForPage(Integer id);

    ShopManage getShopManageForDetail(Integer id);

    ShopManage checkShopManageNo(String shopNo);

    int updateShopManage(ShopManage shopManage);

    int deleteShopServeByShopId(Integer id);

    int deleteShopTechnicianById(Integer id);

    int deleteShopManageById(Integer id);

    int insertShopManage(ShopManage shopManage);

    int insertShopServe(Integer shopId);
}
