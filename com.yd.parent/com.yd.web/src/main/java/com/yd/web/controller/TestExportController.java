package com.yd.web.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSON;
import com.yd.core.res.BaseResponse;
import com.yd.web.export.YdMerchantExportExcel;
import com.yd.web.export.test.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wuyc
 * created 2020/6/17 20:17
 **/
@Controller
public class TestExportController {

    @RequestMapping(value="/yd/test/export/test1")
    public void testExport(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<ParentOrderExcel> excelList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                ParentOrderExcel parentOrderExcel = initOrderData();
                excelList.add(parentOrderExcel);
            }

            String fileName = "订单导出.xls";
            ExportParams params = new ExportParams("订单导出", "订单导出");

            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

            System.out.println("====导出的数据=" + JSON.toJSONString(excelList));
            Workbook workbook =  ExcelExportUtil.exportExcel(params, ParentOrderExcel.class, excelList);
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("======导出1失败");
        }
    }

    @RequestMapping(value="/yd/test/export/test2")
    public void testExport2(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<POrder1> excelList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                POrder1 parentOrderExcel = initOrderData1();
                excelList.add(parentOrderExcel);
            }

            String fileName = "订单导出.xls";
            ExportParams params = new ExportParams("订单导出", "订单导出");

            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

            System.out.println("====导出的数据=" + JSON.toJSONString(excelList));
            Workbook workbook =  ExcelExportUtil.exportExcel(params, POrder1.class, excelList);
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("======导出2失败");
        }
    }

    @RequestMapping(value="/yd/test/export/test3")
    public void testExport3(HttpServletRequest request, HttpServletResponse response) {
        try {
            String fileName = "订单导出.xls";
            ExportParams params = new ExportParams("订单导出", "订单导出");

            List<ChildOrderDetailExcel> excelList = initItemData();
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

            System.out.println("====导出的数据=" + JSON.toJSONString(excelList));
            Workbook workbook =  ExcelExportUtil.exportExcel(params, ChildOrderDetailExcel.class, excelList);
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("======导出3失败");
        }
    }

    private ParentOrderExcel initOrderData() {
        ParentOrderExcel pOrder = new ParentOrderExcel();

        List<ChildOrderExcel> childOrderList = new ArrayList<>();
        ChildOrderExcel childOrderExcel = new ChildOrderExcel();
        childOrderExcel.setOutOrderId("第一个父订单的子订单1");
        childOrderExcel.setOrderId(20001);
        childOrderExcel.setOrderStatus("待发货");
        childOrderExcel.setUserId(1000);
        childOrderExcel.setMobile("15166795821");
        childOrderExcel.setAddress("用户收货地址");
        childOrderExcel.setOrderTotalPrice(100.00);
        childOrderExcel.setItemTotalPrice(100.00);
        childOrderExcel.setItemCbTotalPrice(80.00);
        childOrderExcel.setFreightPrice(10.00);
        childOrderExcel.setTotalCb(90.00);
        childOrderExcel.setDetailList(initItemData());
        childOrderList.add(childOrderExcel);

        ChildOrderExcel childOrderExcel1 = new ChildOrderExcel();
        childOrderExcel1.setOutOrderId("第一个父订单的子订单1");
        childOrderExcel1.setOrderId(20001);
        childOrderExcel1.setOrderStatus("待发货");
        childOrderExcel1.setUserId(1000);
        childOrderExcel1.setMobile("15166795821");
        childOrderExcel1.setAddress("用户收货地址");
        childOrderExcel1.setOrderTotalPrice(100.00);
        childOrderExcel1.setItemTotalPrice(100.00);
        childOrderExcel1.setItemCbTotalPrice(80.00);
        childOrderExcel1.setFreightPrice(10.00);
        childOrderExcel1.setTotalCb(90.00);
        childOrderExcel1.setDetailList(initItemData());
        childOrderList.add(childOrderExcel1);

        pOrder.setChildList(childOrderList);
        pOrder.setpOrderId(102);
        return pOrder;
    }

    private List<ChildOrderDetailExcel> initItemData() {
        List<ChildOrderDetailExcel> detailList = new ArrayList<>();
        ChildOrderDetailExcel childOrderDetailExcel = new ChildOrderDetailExcel();
        childOrderDetailExcel.setItemId(30001);
        childOrderDetailExcel.setTitle("商品名称01");
        childOrderDetailExcel.setItemSpec("商品规格01");
        childOrderDetailExcel.setFirstCategoryName("一级分类01");
        childOrderDetailExcel.setSecondCategoryName("二级分类01");
        detailList.add(childOrderDetailExcel);

        ChildOrderDetailExcel childOrderDetailExcel1 = new ChildOrderDetailExcel();
        childOrderDetailExcel1.setItemId(30002);
        childOrderDetailExcel1.setTitle("商品名称02");
        childOrderDetailExcel1.setItemSpec("商品规格02");
        childOrderDetailExcel1.setFirstCategoryName("一级分类02");
        childOrderDetailExcel1.setSecondCategoryName("二级分类02");
        detailList.add(childOrderDetailExcel1);
        return detailList;
    }

    private POrder1 initOrderData1() {
        POrder1 pOrder = new POrder1();

        List<COrder1> childOrderList = new ArrayList<>();
        COrder1 childOrderExcel = new COrder1();
        childOrderExcel.setOutOrderId("第一个父订单的子订单1");
        childOrderExcel.setOrderId(20001);
        childOrderExcel.setOrderStatus("待发货");
        childOrderExcel.setUserId(1000);
        childOrderExcel.setMobile("15166795821");
        childOrderExcel.setAddress("用户收货地址");
        childOrderExcel.setOrderTotalPrice(100.00);
        childOrderExcel.setItemTotalPrice(100.00);
        childOrderExcel.setItemCbTotalPrice(80.00);
        childOrderExcel.setFreightPrice(10.00);
        childOrderExcel.setTotalCb(90.00);
        childOrderList.add(childOrderExcel);

        COrder1 childOrderExcel1 = new COrder1();
        childOrderExcel1.setOutOrderId("第一个父订单的子订单1");
        childOrderExcel1.setOrderId(20001);
        childOrderExcel1.setOrderStatus("待发货");
        childOrderExcel1.setUserId(1000);
        childOrderExcel1.setMobile("15166795821");
        childOrderExcel1.setAddress("用户收货地址");
        childOrderExcel1.setOrderTotalPrice(100.00);
        childOrderExcel1.setItemTotalPrice(100.00);
        childOrderExcel1.setItemCbTotalPrice(80.00);
        childOrderExcel1.setFreightPrice(10.00);
        childOrderExcel1.setTotalCb(90.00);
        childOrderList.add(childOrderExcel1);

        pOrder.setChildList(childOrderList);
        pOrder.setpOrderId(102);
        return pOrder;
    }

}
