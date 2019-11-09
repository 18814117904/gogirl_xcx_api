package com.gogirl.gogirl_user.controller;

import com.alibaba.fastjson.JSONArray;
import com.gogirl.gogirl_order.order_commons.dto.OrderManage;
import com.gogirl.gogirl_order.order_commons.dto.OrderServe;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_order.order_manage.service.OrderManageService;
import com.gogirl.gogirl_technician.technician_commons.dto.TechnicianManage;
import com.gogirl.gogirl_user.dao.CustomerBalanceRecordMapper;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yinyong on 2019/1/20.
 */
@RestController
@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "balanceRecordExport")
public class BalanceRecordExportController {

    @Autowired
    private OrderManageService orderManageService;
    private CustomerBalanceRecordMapper customerBalanceRecordMapper;

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "export")
    public void listExportOrder(OrderManage orderManage, HttpServletRequest request, HttpServletResponse response) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if(orderManage.getExportTime() != null){
                Date exportTime = simpleDateFormat.parse(orderManage.getExportTime());
                orderManage.setExportTime(simpleDateFormat.format(exportTime));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean depJudge = orderManage.getDepartmentId() == null ? true : false;
        List<OrderManage> list = orderManageService.listOrderManageForExport(orderManage);
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet sheet = hssfWorkbook.createSheet("sheet1");
        sheet.setDefaultColumnWidth(25);
        HSSFCellStyle hssfCellStyle = hssfWorkbook.createCellStyle();
        HSSFFont hssfFont = hssfWorkbook.createFont();
        hssfFont.setFontName("宋体");
        hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        hssfCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        hssfCellStyle.setFont(hssfFont);
        createHeard(sheet, hssfWorkbook, depJudge);
        int listSize = list.size();
        int outIndex = 0; //订单列表循环唯一标识  i作为循环不正确
        for(int i = 0 ; i < listSize ; i ++) {
            int rowIndex = i + 1;
            HSSFRow hssfRow = sheet.createRow(rowIndex);
            // 获取订单下所有服务
            List<OrderServe> osList = list.get(outIndex).getListOrderServer();
            HSSFCell hssfCell0 = hssfRow.createCell(0);
            hssfCell0.setCellValue(list.get(outIndex).getOrderNo());
            hssfCell0.setCellStyle(hssfCellStyle);
            HSSFCell hssfCell1 = hssfRow.createCell(1);
            hssfCell1.setCellValue(list.get(outIndex).getCreateTime() == null ? "" : (simpleDateFormat1.format(list.get(outIndex).getCreateTime())));
            hssfCell1.setCellStyle(hssfCellStyle);
            HSSFCell hssfCell2 = hssfRow.createCell(2);
            hssfCell2.setCellValue(list.get(outIndex).getCustomer() == null ? "" : (list.get(outIndex).getCustomer().getStoreRecordRealName()));
            hssfCell2.setCellStyle(hssfCellStyle);
            HSSFCell hssfCell3 = hssfRow.createCell(3);
            hssfCell3.setCellValue(list.get(outIndex).getCustomer() == null ? "" : (list.get(outIndex).getCustomer().getPhone()));
            hssfCell3.setCellStyle(hssfCellStyle);

            for(int innerIndex = 0; innerIndex < list.get(outIndex).getListOrderServer().size(); innerIndex++){
                if(innerIndex == 0){
                    HSSFCell hssfCell4 = hssfRow.createCell(4);
                    hssfCell4.setCellValue(list.get(outIndex).getListOrderServer().get(innerIndex).getServe() == null ? "" : list.get(outIndex).getListOrderServer().get(innerIndex).getServe().getName());
                    hssfCell4.setCellStyle(hssfCellStyle);
                    HSSFCell hssfCell5 = hssfRow.createCell(5);
                    hssfCell5.setCellValue(list.get(outIndex).getListOrderServer().get(innerIndex).getProduceName());
                    hssfCell5.setCellStyle(hssfCellStyle);
                    HSSFCell hssfCell6 = hssfRow.createCell(6);
                    if((list.get(outIndex).getListOrderServer().get(innerIndex).getListTechnicianManage()) == null) {
                        hssfCell6.setCellValue("");
                    }else{
                        List<TechnicianManage> listTechnician = (List<TechnicianManage>) list.get(outIndex).getListOrderServer().get(innerIndex).getListTechnicianManage();
                        String str = "";
                        for(int indexo = 0; indexo < listTechnician.size(); indexo ++) {
                            if(indexo == (listTechnician.size() -1)) {
                                str = str + listTechnician.get(indexo).getName();
                            }else {
                                str = str + listTechnician.get(indexo).getName() + ",";
                            }
                        }
                        hssfCell6.setCellValue(str);
                    }

                    hssfCell6.setCellStyle(hssfCellStyle);
                    HSSFCell hssfCell7 = hssfRow.createCell(7);
                    if(list.get(outIndex).getListOrderServer().get(innerIndex).getServePrice() != null){
                        hssfCell7.setCellValue(list.get(outIndex).getListOrderServer().get(innerIndex).getServePrice()+"");
                    }else{
                        hssfCell7.setCellValue(list.get(outIndex).getListOrderServer().get(innerIndex).getServe().getPrice()+"");
                    }
                    hssfCell7.setCellStyle(hssfCellStyle);
                    HSSFCell hssfCell8 = hssfRow.createCell(8);
                    if(list.get(outIndex).getListOrderServer().get(innerIndex).getServeChangePrice() == null && list.get(outIndex).getChangePrice() != null) {
                        hssfCell8.setCellValue(list.get(outIndex).getChangePrice()+"");
                    }else{
                        hssfCell8.setCellValue(list.get(outIndex).getListOrderServer().get(innerIndex).getServeChangePrice() == null ? "0.0000" : list.get(outIndex).getListOrderServer().get(innerIndex).getServeChangePrice()+ "");
                    }
                    hssfCell8.setCellStyle(hssfCellStyle);
                }else{
                    HSSFRow hssfRow1 = sheet.createRow(rowIndex + innerIndex);
                    HSSFCell hssfCell4 = hssfRow1.createCell(4);
                    hssfCell4.setCellValue(list.get(outIndex).getListOrderServer().get(innerIndex).getServe().getName());
                    hssfCell4.setCellStyle(hssfCellStyle);
                    HSSFCell hssfCell5 = hssfRow1.createCell(5);
                    hssfCell5.setCellValue(list.get(outIndex).getListOrderServer().get(innerIndex).getProduceName());
                    hssfCell5.setCellStyle(hssfCellStyle);
                    HSSFCell hssfCell6 = hssfRow1.createCell(6);
                    if((list.get(outIndex).getListOrderServer().get(innerIndex).getListTechnicianManage()) == null) {
                        hssfCell6.setCellValue("");
                    }else{
                        List<TechnicianManage> listTechnician = (List<TechnicianManage>) list.get(outIndex).getListOrderServer().get(innerIndex).getListTechnicianManage();
                        String str = "";
                        for(int indexo = 0; indexo < listTechnician.size(); indexo ++) {
                            if(indexo == (listTechnician.size() -1)) {
                                str = str + listTechnician.get(indexo).getName();
                            }else {
                                str = str + listTechnician.get(indexo).getName() + ",";
                            }
                        }
                        hssfCell6.setCellValue(str);
                    }
                    hssfCell6.setCellStyle(hssfCellStyle);
                    HSSFCell hssfCell7 = hssfRow1.createCell(7);
                    hssfCell7.setCellValue(list.get(outIndex).getListOrderServer().get(innerIndex).getServe().getPrice()+"");
                    hssfCell7.setCellStyle(hssfCellStyle);
                    HSSFCell hssfCell8 = hssfRow1.createCell(8);
                    hssfCell8.setCellValue(list.get(outIndex).getListOrderServer().get(innerIndex).getServeChangePrice() == null ? "0.0000" : list.get(outIndex).getListOrderServer().get(innerIndex).getServeChangePrice()+ "");
                    hssfCell8.setCellStyle(hssfCellStyle);
                }
            }
            HSSFCell hssfCell9 = hssfRow.createCell(9);
            hssfCell9.setCellValue(list.get(outIndex).getTotalPrice()+"");
            hssfCell9.setCellStyle(hssfCellStyle);

            HSSFCell hssfCell10 = hssfRow.createCell(10);
            hssfCell10.setCellValue(list.get(outIndex).getDiscountPrice()+"");
            hssfCell10.setCellStyle(hssfCellStyle);

            HSSFCell hssfCell11 = hssfRow.createCell(11);
            BigDecimal bigDecimal = list.get(outIndex).getTotalPrice().subtract(list.get(outIndex).getDiscountPrice());
            if(bigDecimal.compareTo(new BigDecimal(0)) == -1) {
                hssfCell11.setCellValue("0.0000");
            }else{
                hssfCell11.setCellValue(bigDecimal +"");
            }

            hssfCell11.setCellStyle(hssfCellStyle);

             HSSFCell hssfCell12 = hssfRow.createCell(12);
            if(list.get(outIndex).getMultiplePaymentType() != null) {
                List<Map> listMap = JSONArray.parseArray(list.get(outIndex).getMultiplePaymentType(), Map.class);
                String str = "";
                for(int listMapIndex = 0; listMapIndex < listMap.size(); listMapIndex++) {
                    if(listMap.get(listMapIndex).get("type").toString().equals("1")) {
                        str = str + "微信支付"+listMap.get(listMapIndex).get("price").toString();
                    }else if(listMap.get(listMapIndex).get("type").toString().equals("2")) {
                        str = str + "会员"+listMap.get(listMapIndex).get("price").toString();
                    }else if(listMap.get(listMapIndex).get("type").toString().equals("3")) {
                        str = str + "其他"+listMap.get(listMapIndex).get("price").toString();
                    }else if(listMap.get(listMapIndex).get("type").toString().equals("4")) {
                        str = str + "充值"+listMap.get(listMapIndex).get("price").toString();
                    }else if(listMap.get(listMapIndex).get("type").toString().equals("5")) {
                        str = str + "pos机"+listMap.get(listMapIndex).get("price").toString();
                    }else if(listMap.get(listMapIndex).get("type").toString().equals("6")) {
                        str = str + "现金"+listMap.get(listMapIndex).get("price").toString();
                    }else if(listMap.get(listMapIndex).get("type").toString().equals("7")) {
                        str = str + "大众点评"+listMap.get(listMapIndex).get("price").toString();
                    }else if(listMap.get(listMapIndex).get("type").toString().equals("8")) {
                        str = str + "微信扫码"+listMap.get(listMapIndex).get("price").toString();
                    }else if(listMap.get(listMapIndex).get("type").toString().equals("9")) {
                        str = str + "团购"+listMap.get(listMapIndex).get("price").toString();
                    }else if(listMap.get(listMapIndex).get("type").toString().equals("10")) {
                        str = str + "免单"+listMap.get(listMapIndex).get("price").toString();
                    }else if(listMap.get(listMapIndex).get("type").toString().equals("11")) {
                        str = str + "会员帮付"+listMap.get(listMapIndex).get("price").toString();
                    }else if(listMap.get(listMapIndex).get("type").toString().equals("12")) {
                        str = str + "次卡"+listMap.get(listMapIndex).get("price").toString();
                    }
                    if(listMapIndex != (listMap.size() - 1)) {
                        str = str + ", ";
                    }else {
                        hssfCell12.setCellValue(str);
                    }
                }
            }else if(list.get(outIndex).getPaymentType() != null) {
                if(list.get(outIndex).getPaymentType() == 1) {
                    hssfCell12.setCellValue("微信支付");
                }else if(list.get(outIndex).getPaymentType() == 2) {
                    hssfCell12.setCellValue("会员");
                }else if(list.get(outIndex).getPaymentType() == 3) {
                    hssfCell12.setCellValue("其他");
                }else if(list.get(outIndex).getPaymentType() == 4) {
                    hssfCell12.setCellValue("充值");
                }else if(list.get(outIndex).getPaymentType() == 5) {
                    hssfCell12.setCellValue("pos机");
                }else if(list.get(outIndex).getPaymentType() == 6) {
                    hssfCell12.setCellValue("现金");
                }else if(list.get(outIndex).getPaymentType() == 7) {
                    hssfCell12.setCellValue("大众点评");
                }else if(list.get(outIndex).getPaymentType() == 8) {
                    hssfCell12.setCellValue("微信扫码");
                }else if(list.get(outIndex).getPaymentType() == 9) {
                    hssfCell12.setCellValue("团购");
                }else if(list.get(outIndex).getPaymentType() == 10) {
                    hssfCell12.setCellValue("免单");
                }else if(list.get(outIndex).getPaymentType() == 11) {
                    hssfCell12.setCellValue("会员帮付");
                }else if(list.get(outIndex).getPaymentType() == 12) {
                    hssfCell12.setCellValue("次卡");
                }
            }else {
                hssfCell12.setCellValue("");
            }
                hssfCell12.setCellStyle(hssfCellStyle);

            HSSFCell hssfCell13 = hssfRow.createCell(13);
            hssfCell13.setCellValue(list.get(outIndex).getMessage() == null ? "" : list.get(outIndex).getMessage());
            hssfCell13.setCellStyle(hssfCellStyle);

            HSSFCell hssfCell14 = hssfRow.createCell(14);
            if(list.get(outIndex).getStatus() == null){
                hssfCell14.setCellValue("");
            }else if(list.get(outIndex).getStatus() == 1) {
                hssfCell14.setCellValue("正在服务");
            }else if(list.get(outIndex).getStatus() == 2) {
                hssfCell14.setCellValue("待付款");
            }else if(list.get(outIndex).getStatus() == 3) {
                hssfCell14.setCellValue("未评价");
            }else if(list.get(outIndex).getStatus() == 4) {
                hssfCell14.setCellValue("已完成");
            }else if(list.get(outIndex).getStatus() == 5) {
                hssfCell14.setCellValue("已删除");
            }
            hssfCell14.setCellStyle(hssfCellStyle);

            HSSFCell hssfCell15 = hssfRow.createCell(15);
            hssfCell15.setCellValue(list.get(outIndex).getUserManage() == null ? "" : list.get(outIndex).getUserManage().getName());
            hssfCell15.setCellStyle(hssfCellStyle);

            HSSFCell hssfCell16 = hssfRow.createCell(16);
            hssfCell16.setCellValue(list.get(outIndex).getDataIntegrity() == null ? "0.00%" : list.get(outIndex).getDataIntegrity()+"%");
            hssfCell16.setCellStyle(hssfCellStyle);

//            if(depJudge) {
                HSSFCell hssfCell17 = hssfRow.createCell(17);
                hssfCell17.setCellValue(list.get(outIndex).getDepartmentName());
                hssfCell17.setCellStyle(hssfCellStyle);
//            }

            HSSFCell hssfCell18 = hssfRow.createCell(18);
            hssfCell18.setCellValue(list.get(outIndex).getRemark() == null ? "" : list.get(outIndex).getRemark());
            hssfCell18.setCellStyle(hssfCellStyle);

            if(osList.size() > 1){
                sheet.addMergedRegion(new CellRangeAddress(i+1, i+osList.size(), 0, 0));
                sheet.addMergedRegion(new CellRangeAddress(i+1, i+osList.size(), 1, 1));
                sheet.addMergedRegion(new CellRangeAddress(i+1, i+osList.size(), 2, 2));
                sheet.addMergedRegion(new CellRangeAddress(i+1, i+osList.size(), 3, 3));
                sheet.addMergedRegion(new CellRangeAddress(i+1, i+osList.size(), 9, 9));
                sheet.addMergedRegion(new CellRangeAddress(i+1, i+osList.size(), 10, 10));
                sheet.addMergedRegion(new CellRangeAddress(i+1, i+osList.size(), 11, 11));
                sheet.addMergedRegion(new CellRangeAddress(i+1, i+osList.size(), 12, 12));
                sheet.addMergedRegion(new CellRangeAddress(i+1, i+osList.size(), 13, 13));
                sheet.addMergedRegion(new CellRangeAddress(i+1, i+osList.size(), 14, 14));
                sheet.addMergedRegion(new CellRangeAddress(i+1, i+osList.size(), 15, 15));
                sheet.addMergedRegion(new CellRangeAddress(i+1, i+osList.size(), 16, 16));
                if(depJudge) {
                    sheet.addMergedRegion(new CellRangeAddress(i+1, i+osList.size(), 17, 17));
                }
                listSize = listSize + osList.size() - 1;  //合并行时总行数改变
                i = i + osList.size() - 1;
            }
            outIndex++;
        }
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename="+new String(("订单详情-"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())).getBytes(), "ISO8859-1") +".xls");
            ServletOutputStream outputStream =	response.getOutputStream();
            hssfWorkbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createHeard(HSSFSheet sheet, HSSFWorkbook hssfWorkbook, boolean depJudge) {
        HSSFFont hssfFont = hssfWorkbook.createFont();
        hssfFont.setFontName("宋体");
        HSSFCellStyle headHssfCellStyle = hssfWorkbook.createCellStyle();
        headHssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headHssfCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        headHssfCellStyle.setFont(hssfFont);
        headHssfCellStyle.setFillBackgroundColor((short) 80);
        HSSFRow row = sheet.createRow(0);
        row.setHeightInPoints(20);
        row.setRowStyle(headHssfCellStyle);
        HSSFCell cell = row.createCell(0);
        cell.setCellStyle(headHssfCellStyle);
        cell.setCellValue("订单编号");
        HSSFCell cell1 = row.createCell(1);
        cell1.setCellStyle(headHssfCellStyle);
        cell1.setCellValue("创建时间");
        HSSFCell cell2 = row.createCell(2);
        cell2.setCellStyle(headHssfCellStyle);
        cell2.setCellValue("客户名称");
        HSSFCell cell3 = row.createCell(3);
        cell3.setCellStyle(headHssfCellStyle);
        cell3.setCellValue("电话");
        HSSFCell cell4 = row.createCell(4);
        cell4.setCellStyle(headHssfCellStyle);
        cell4.setCellValue("服务");
        HSSFCell cell5 = row.createCell(5);
        cell5.setCellStyle(headHssfCellStyle);
        cell5.setCellValue("款式");
        HSSFCell cell6 = row.createCell(6);
        cell6.setCellStyle(headHssfCellStyle);
        cell6.setCellValue("技师");
        HSSFCell cell7 = row.createCell(7);
        cell7.setCellStyle(headHssfCellStyle);
        cell7.setCellValue("服务价格");
        HSSFCell cell8 = row.createCell(8);
        cell8.setCellStyle(headHssfCellStyle);
        cell8.setCellValue("改价");
        HSSFCell cell9 = row.createCell(9);
        cell9.setCellStyle(headHssfCellStyle);
        cell9.setCellValue("订单总价");
        HSSFCell cell11 = row.createCell(10);
        cell11.setCellStyle(headHssfCellStyle);
        cell11.setCellValue("优惠券");
        HSSFCell cell12 = row.createCell(11);
        cell12.setCellStyle(headHssfCellStyle);
        cell12.setCellValue("合计金额");
        HSSFCell cell13 = row.createCell(12);
        cell13.setCellStyle(headHssfCellStyle);
        cell13.setCellValue("收款来源");
        HSSFCell cell14 = row.createCell(13);
        cell14.setCellStyle(headHssfCellStyle);
        cell14.setCellValue("收款备注");
        HSSFCell cell15 = row.createCell(14);
        cell15.setCellStyle(headHssfCellStyle);
        cell15.setCellValue("订单状态");
        HSSFCell cell16 = row.createCell(15);
        cell16.setCellStyle(headHssfCellStyle);
        cell16.setCellValue("开单人");

        HSSFCell cell17 = row.createCell(16);
        cell17.setCellStyle(headHssfCellStyle);
        cell17.setCellValue("数据");

//        if(depJudge) {
            HSSFCell cell18 = row.createCell(17);
            cell18.setCellStyle(headHssfCellStyle);
            cell18.setCellValue("店铺");
//        }
        HSSFCell cell19 = row.createCell(18);
        cell19.setCellStyle(headHssfCellStyle);
        cell19.setCellValue("订单备注");
        
    /*    HSSFCell cell18 = row.createCell(17);
        cell18.setCellStyle(headHssfCellStyle);
        cell18.setCellValue("客照");*/
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "importExcel")
    public JsonResult importExcel(MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        return jsonResult;
    }
}
