package com.yd.web.controller.admin.market;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.req.MerchantDrawReq;
import com.yd.api.result.decoration.YdVrManagerResult;
import com.yd.api.result.draw.YdMerchantDrawActivityResult;
import com.yd.api.result.draw.YdUserDrawRecordResult;
import com.yd.api.result.market.YdWelfareManagerResult;
import com.yd.api.service.decoration.YdVrManagerService;
import com.yd.api.service.draw.YdMerchantDrawActivityService;
import com.yd.api.service.draw.YdUserDrawRecordService;
import com.yd.api.service.market.YdWelfareManagerService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.controller.BaseController;
import com.yd.web.request.MerchantHotItemRequest;
import com.yd.web.request.WelfareManagerRequest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author wuyc
 * created 2019/12/4 11:52
 **/
@RestController
@MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="mod_welfare_manager",name = "福利管理")
@RequestMapping("/admin/platform/welfare/manager")
public class AdminWelfareManagerController extends BaseController {

    @Reference
    private YdWelfareManagerService ydWelfareManagerService;

    @ApiOperation(value = "平台福利列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "merchantId", value = "merchantId")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="platform_welfare_data",name = "平台福利列表")
    @RequestMapping(value = "/findList", method = {RequestMethod.POST})
    public BaseResponse<List<YdWelfareManagerResult>> findList(HttpServletRequest request, Integer merchantId) {
        BaseResponse<List<YdWelfareManagerResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydWelfareManagerService.getAll(new YdWelfareManagerResult()));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台福利详情", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="platform_welfare_detail",name = "平台福利详情")
    @RequestMapping(value = "/getDetail", method = {RequestMethod.POST})
    public BaseResponse<YdWelfareManagerResult> getDetail(HttpServletRequest request, Integer id) {
        BaseResponse<YdWelfareManagerResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydWelfareManagerService.getYdWelfareManagerById(id));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台福利编辑", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "id"),
            @ApiImplicitParam(paramType = "query", name = "title", value = "title"),
            @ApiImplicitParam(paramType = "query", name = "imageUrl", value = "图片地址"),
            @ApiImplicitParam(paramType = "query", name = "merchantIds", value = "商户ids"),
            @ApiImplicitParam(paramType = "query", name = "jumpUrl", value = "跳转链接地址")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="platform_welfare_update",name = "平台福利编辑")
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    public BaseResponse<String> saveOrUpdate(Integer id, String title, String imageUrl, String merchantIds, String jumpUrl) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            YdWelfareManagerResult ydVrManagerResult = new YdWelfareManagerResult();
            ydVrManagerResult.setTitle(title);
            ydVrManagerResult.setJumpUrl(jumpUrl);
            ydVrManagerResult.setImageUrl(imageUrl);
            ydVrManagerResult.setMerchantIds(merchantIds);
            if (id != null && id > 0) {
                ydVrManagerResult.setId(id);
                ydWelfareManagerService.updateYdWelfareManager(ydVrManagerResult);
            } else {
                ydWelfareManagerService.insertYdWelfareManager(ydVrManagerResult);
            }
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台福利删除", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="platform_welfare_delete",name = "平台福利删除")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public BaseResponse<String> delete(HttpServletRequest request, Integer id) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "删除成功");
        try {
            ydWelfareManagerService.deleteYdWelfareManager(id);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台福利上下架", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "id"),
            @ApiImplicitParam(paramType = "query", name = "isEnable", value = "Y 上架，N下架"),
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="platform_welfare_update_status",name = "平台福利上下架")
    @RequestMapping(value = "/updateStatus", method = {RequestMethod.POST})
    public BaseResponse<String> updateStatus(HttpServletRequest request, Integer id, String isEnable) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "删除成功");
        try {
            ydWelfareManagerService.updateYdWelfareManagerStatus(id, isEnable);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台福利列表排序", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT,  alias="platform_welfare_list_sort", name="平台福利列表排序")
    @RequestMapping(value = "/listSort", method = {RequestMethod.POST})
    public BaseResponse<String> listSort(HttpServletRequest request, @RequestBody WelfareManagerRequest dataList) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "添加成功");
        try {
            ydWelfareManagerService.listSort(dataList.getDataList());
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

}
