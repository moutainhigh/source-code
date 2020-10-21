package com.yd.web.controller.admin.gift;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.order.YdGiftOrderDetailResult;
import com.yd.api.service.gift.YdGiftCategoryService;
import com.yd.api.service.gift.YdGiftService;
import com.yd.api.service.order.YdGiftOrderDetailService;
import com.yd.api.service.order.YdGiftOrderService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.DTOUtils;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.controller.BaseController;
import com.yd.web.export.YdSupplierGiftOrderExcel;
import com.yd.web.util.ExcelUtiles;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 供应商礼品订单
 * @author wuyc
 * created 2019/11/22 17:06
 **/
@Controller
@RequestMapping("/admin/supplier/gift/order")
@MerchantCheck(groupCode = EnumSiteGroup.SUPPLIER, alias="mod_suppler_gift", name = "礼品管理")
public class AdminSupplierGiftOrderController extends BaseController {

    @Reference
    private YdGiftService ydGiftService;

    @Reference
    private YdGiftOrderService ydGiftOrderService;

    @Reference
    private YdGiftCategoryService ydGiftCategoryService;

    @Reference
    private YdGiftOrderDetailService ydGiftOrderDetailService;

    @ApiOperation(value = "供应商订单列表数据", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "giftOrderId", value = "礼品主订单id"),
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "礼品订单详情id"),
            @ApiImplicitParam(paramType = "query", name = "orderStatus", value = "WAIT_DELIVER | WAIT_GOODS | SUCCESS"),
            @ApiImplicitParam(paramType = "query", name = "startTime", value = "订单起始时间"),
            @ApiImplicitParam(paramType = "query", name = "endTime", value = "订单结束时间")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SUPPLIER, alias="supplier_gift_order_data", name="供应商订单列表数据")
    @ResponseBody
    @RequestMapping(value = "/findList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdGiftOrderDetailResult>> deleteYdGift(HttpServletRequest request, String orderStatus,
                                                              String startTime, String endTime, Integer giftOrderId, Integer orderId) {
        BaseResponse<Page<YdGiftOrderDetailResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            YdGiftOrderDetailResult ydGiftOrderDetailResult = new YdGiftOrderDetailResult();
            ydGiftOrderDetailResult.setStartTime(startTime);
            ydGiftOrderDetailResult.setEndTime(endTime);
            ydGiftOrderDetailResult.setOrderStatus(orderStatus);
            ydGiftOrderDetailResult.setSupplierId(getCurrMerchantId(request));
            ydGiftOrderDetailResult.setId(orderId);
            ydGiftOrderDetailResult.setGiftOrderId(giftOrderId);
            ydGiftOrderDetailResult.setTransType("online");
            ydGiftOrderDetailResult.setPayStatus("SUCCESS");
            result.setResult(ydGiftOrderDetailService.findSupplierGiftOrderDetailListByPage(ydGiftOrderDetailResult, getPageInfo(request)));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "供应商礼品订单详情", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SUPPLIER, alias="supplier_gift_order_detail", name="供应商礼品订单详情")
    @ResponseBody
    @RequestMapping(value = "/getGiftOrderDetail", method = {RequestMethod.POST})
    public BaseResponse<YdGiftOrderDetailResult> getGiftOrderDetail(HttpServletRequest request, Integer id) {
        BaseResponse<YdGiftOrderDetailResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydGiftOrderDetailService.getSupplierGiftOrderDetail(id));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "供应商礼品订单发货", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "detailOrderId", value = "礼品订单详情id"),
            @ApiImplicitParam(paramType = "query", name = "expressOrderId", value = "快递单号")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SUPPLIER, alias="supplier_gift_order_update_express", name="供应商礼品订单发货")
    @ResponseBody
    @RequestMapping(value = "/updateExpress", method = {RequestMethod.POST})
    public BaseResponse<String> saveOrUpdateGift(HttpServletRequest request, Integer detailOrderId, String expressOrderId) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            Integer supplerId = getCurrMerchantId(request);
            ydGiftOrderDetailService.updateGiftOrderExpress(supplerId, detailOrderId, expressOrderId);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "供应商订单导出", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "giftOrderId", value = "礼品主订单id"),
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "礼品订单详情id"),
            @ApiImplicitParam(paramType = "query", name = "orderStatus", value = "WAIT_DELIVER | WAIT_GOODS | SUCCESS"),
            @ApiImplicitParam(paramType = "query", name = "startTime", value = "订单起始时间"),
            @ApiImplicitParam(paramType = "query", name = "endTime", value = "订单结束时间")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SUPPLIER, alias="supplier_gift_order_data", name="供应商订单列表数据")
    @RequestMapping(value = "/export", method = {RequestMethod.GET})
    public void export(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, String orderStatus, String startTime, String endTime,
                         Integer giftOrderId, Integer orderId) {

        List<YdSupplierGiftOrderExcel> excelList = new ArrayList<>();

        YdGiftOrderDetailResult ydGiftOrderDetailResult = new YdGiftOrderDetailResult();
        ydGiftOrderDetailResult.setStartTime(startTime);
        ydGiftOrderDetailResult.setEndTime(endTime);
        ydGiftOrderDetailResult.setOrderStatus(orderStatus);
        ydGiftOrderDetailResult.setSupplierId(getCurrMerchantId(request));
        ydGiftOrderDetailResult.setId(orderId);
        ydGiftOrderDetailResult.setGiftOrderId(giftOrderId);
        ydGiftOrderDetailResult.setTransType("online");
        ydGiftOrderDetailResult.setPayStatus("SUCCESS");

        PagerInfo pagerInfo = new PagerInfo(Integer.MAX_VALUE, 1);
        Page<YdGiftOrderDetailResult> list = ydGiftOrderDetailService.findSupplierGiftOrderDetailListByPage(ydGiftOrderDetailResult, pagerInfo);
        excelList.addAll(DTOUtils.convertList(list.getData(), YdSupplierGiftOrderExcel.class));

        if (CollectionUtils.isNotEmpty(excelList)) {
            excelList.forEach(data -> {
                //  WAIT_DELIVER | WAIT_GOODS | SUCCESS
                if (data.getOrderStatus().equalsIgnoreCase("WAIT_DELIVER") ){
                    data.setOrderStatus("待发货");
                } else if (data.getOrderStatus().equalsIgnoreCase("WAIT_GOODS")) {
                    data.setOrderStatus("待收货");
                } else if(data.getOrderStatus().equalsIgnoreCase("SUCCESS")) {
                    data.setOrderStatus("已完成");
                }
            });
        }

//        ExcelUtiles.exportExcel(excelList, "礼品订单导出", "礼品订单导出",
//                YdSupplierGiftOrderExcel.class, "礼品订单导出.xls", response);

        try {
            String fileName = "礼品订单导出.xls";
            ExportParams params = new ExportParams("礼品订单导出","礼品订单导出");

            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

            Workbook workbook =  ExcelExportUtil.exportExcel(params, YdSupplierGiftOrderExcel.class, excelList);
            workbook.write(response.getOutputStream());
        } catch (Exception e) {

        }
    }

}
