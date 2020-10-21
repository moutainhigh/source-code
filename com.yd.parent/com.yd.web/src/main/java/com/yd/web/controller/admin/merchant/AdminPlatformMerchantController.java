package com.yd.web.controller.admin.merchant;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.member.YdMerchantMemberOpenRecordResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.member.YdMerchantMemberOpenRecordService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.core.enums.YdRoleTypeEnum;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;
import com.yd.core.utils.PropertiesHelp;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.controller.BaseController;
import com.yd.web.export.YdMerchantExportExcel;
import com.yd.web.export.YdMerchantGiftExcel;
import com.yd.web.export.YdMerchantMemberExportExcel;
import com.yd.web.util.QrCodeUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统账号管理
 * @author wuyc
 * created 2019/11/25 15:37
 **/
@Controller
@MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="mod_platform_merchant", name = "商户管理")
@RequestMapping("/admin/platform/merchant")
public class AdminPlatformMerchantController extends BaseController {

    @Reference
    private YdMerchantService ydMerchantService;

    @Reference
    private YdMerchantMemberOpenRecordService ydMerchantMemberOpenRecordService;

    @ApiOperation(value = "平台商户列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "groupCode", value = "商户渠道(merchant, supplier)"),
            @ApiImplicitParam(paramType = "query", name = "merchantName", value = "商户名称"),
            @ApiImplicitParam(paramType = "query", name = "mobile", value = "手机号"),
            @ApiImplicitParam(paramType = "query", name = "memberLevel", value = "会员类型"),
            @ApiImplicitParam(paramType = "query", name = "inviteId", value = "邀请人"),
            @ApiImplicitParam(paramType = "query", name = "memberStatus", value = "会员状态 Y代表未过期，N代表已过期"),
            @ApiImplicitParam(paramType = "query", name = "isFlag", value = "Y代表禁用，N代表未禁用"),
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数")
    })
    @ResponseBody
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_merchant_data",name = "平台商户列表")
    @RequestMapping(value = "/findList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdMerchantResult>> findList(HttpServletRequest request, String groupCode, String merchantName,
                                                         String mobile, String memberStatus,String isFlag, Integer memberLevel, Integer inviteId) {
        BaseResponse<Page<YdMerchantResult>> result = BaseResponse.success(null, "00", "查询成功");
        PagerInfo pageInfo = getPageInfo(request, 1, 10);

        YdMerchantResult ydMerchantResult = new YdMerchantResult();
        ydMerchantResult.setMobile(mobile);
        ydMerchantResult.setGroupCode(groupCode);
        ydMerchantResult.setMemberStatus(memberStatus);
        ydMerchantResult.setMemberLevel(memberLevel);
        ydMerchantResult.setInviteId(inviteId);
        ydMerchantResult.setMerchantName(merchantName);
        ydMerchantResult.setIsFlag(isFlag);
        // 查询全部供应商和门店
        Page<YdMerchantResult> list = ydMerchantService.findPlatformMerchantList(ydMerchantResult, pageInfo);
        return BaseResponse.success(list);
    }

    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_merchant_data",name = "平台商户列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "provinceId", value = "省id"),
            @ApiImplicitParam(paramType = "query", name = "cityId", value = "市id"),
            @ApiImplicitParam(paramType = "query", name = "districtId", value = "区id"),
    })

    @ResponseBody
    @RequestMapping(value = "/findStoreList", method = {RequestMethod.POST})
    public BaseResponse<List<YdMerchantResult>> findStoreList(Integer provinceId, Integer cityId, Integer districtId) {
        BaseResponse<List<YdMerchantResult>> result = BaseResponse.success(null, "00", "查询成功");
        YdMerchantResult ydMerchantResult = new YdMerchantResult();
        ydMerchantResult.setDistrictId(districtId);
        ydMerchantResult.setProvinceId(provinceId);
        ydMerchantResult.setCityId(cityId);
        List<YdMerchantResult> storeList = ydMerchantService.findStoreList(ydMerchantResult);
        result.setResult(storeList);
        return result;
    }

    @ApiOperation(value = "平台商户详情", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "id")
    })
    @ResponseBody
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_merchant_detail",name = "平台商户详情")
    @RequestMapping(value = "/getDetail", method = {RequestMethod.POST})
    public BaseResponse<YdMerchantResult> getDetail(HttpServletRequest request, Integer id) {
        BaseResponse<YdMerchantResult> result = BaseResponse.success(null, "00", "查询成功");
        YdMerchantResult detail = ydMerchantService.getYdMerchantById(id);
        result.setResult(detail);
        return result;
    }

    @ApiOperation(value = "平台查看商户会员开通记录", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "merchantId", value = "merchantId")
    })
    @ResponseBody
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_merchant_detail",name = "平台商户详情")
    @RequestMapping(value = "/getOpenMemberDetail", method = {RequestMethod.POST})
    public BaseResponse<List<YdMerchantMemberOpenRecordResult>> getOpenMemberDetail(HttpServletRequest request, Integer merchantId) {
        BaseResponse<List<YdMerchantMemberOpenRecordResult>> result = BaseResponse.success(null, "00", "查询成功");
        YdMerchantMemberOpenRecordResult req = new YdMerchantMemberOpenRecordResult();
        req.setMerchantId(merchantId);
        List<YdMerchantMemberOpenRecordResult> all = ydMerchantMemberOpenRecordService.getAll(req);
        result.setResult(all);
        return result;
    }

    @ApiOperation(value = "平台添加修改商户", httpMethod = "POST")
    @ResponseBody
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_merchant_update", name = "平台添加修改商户")
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    public BaseResponse<String> saveOrUpdate(HttpServletRequest request, YdMerchantResult ydMerchantResult) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            if (ydMerchantResult.getId() == null) {
                ydMerchantResult = ydMerchantService.platformInsertMerchant(ydMerchantResult);
            } else {
                ydMerchantResult = ydMerchantService.platformUpdateMerchant(ydMerchantResult);
            }
            if (EnumSiteGroup.MERCHANT.getCode().equals(ydMerchantResult.getGroupCode())) {
                if (StringUtils.isEmpty(ydMerchantResult.getShopQrCode())) {
                    String domain = PropertiesHelp.getProperty("webDomain");
                    String url = domain + "/store/front/" + ydMerchantResult.getId() + "/index";
                    String shopQrCode = QrCodeUtil.makeQrCode(url, 500, 500);
                    ydMerchantService.updateShopQrCode(ydMerchantResult.getId(), shopQrCode);
                }
            }
        } catch (BusinessException exception) {
            result = BaseResponse.fail(exception.getCode(), exception.getMessage());
        } catch (Exception e) {

        }
        return result;
    }

    @ApiOperation(value = "平台删除商户", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "merchantId", value = "商户id"),
    })
    @ResponseBody
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_merchant_delete",name = "系统账号删除")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public BaseResponse<String> deleteMerchant(Integer merchantId) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "删除成功");
        try {
            ydMerchantService.deleteMerchant(merchantId);
        } catch (BusinessException exception) {
            result = BaseResponse.fail(exception.getCode(), exception.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台升级商户会员", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "merchantId", value = "商户id"),
            @ApiImplicitParam(paramType = "query", name = "memberLevel", value = "会员等级"),
    })
    @ResponseBody
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_merchant_update", name = "平台添加修改商户")
    @RequestMapping(value = "/memberUpgrade", method = {RequestMethod.POST})
    public BaseResponse<String> memberUpgrade(Integer merchantId, Integer memberLevel) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydMerchantService.merchantUpgrade(merchantId, memberLevel);
        } catch (BusinessException exception) {
            result = BaseResponse.fail(exception.getCode(), exception.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台续费商户会员", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "merchantId", value = "商户id"),
            @ApiImplicitParam(paramType = "query", name = "memberLevel", value = "会员等级"),
    })
    @ResponseBody
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_merchant_update", name = "平台添加修改商户")
    @RequestMapping(value = "/memberRenewal", method = {RequestMethod.POST})
    public BaseResponse<String> memberRenewal(Integer merchantId, Integer memberLevel) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydMerchantService.memberRenewal(merchantId, memberLevel);
        } catch (BusinessException exception) {
            result = BaseResponse.fail(exception.getCode(), exception.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台重新开通商户会员", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "merchantId", value = "商户id"),
            @ApiImplicitParam(paramType = "query", name = "memberLevel", value = "会员等级"),
    })
    @ResponseBody
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_merchant_update", name = "平台重新开通商户会员")
    @RequestMapping(value = "/openMemberAgain", method = {RequestMethod.POST})
    public BaseResponse<String> openMemberAgain(Integer merchantId, Integer memberLevel) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydMerchantService.openMemberAgain(merchantId, memberLevel);
        } catch (BusinessException exception) {
            result = BaseResponse.fail(exception.getCode(), exception.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台启用禁用商户", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "merchantId", value = "商户id"),
            @ApiImplicitParam(paramType = "query", name = "isFlag", value = "Y代表禁用，N代表未禁用"),
    })
    @ResponseBody
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_merchant_update", name = "平台添加修改商户")
    @RequestMapping(value = "/updateMerchantStatus", method = {RequestMethod.POST})
    public BaseResponse<String> updateMerchantStatus(Integer merchantId, String isFlag) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydMerchantService.updateMerchantStatus(merchantId, isFlag);
        } catch (BusinessException exception) {
            result = BaseResponse.fail(exception.getCode(), exception.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台商户导出", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "groupCode", value = "商户渠道(merchant, supplier)"),
            @ApiImplicitParam(paramType = "query", name = "merchantName", value = "商户名称"),
            @ApiImplicitParam(paramType = "query", name = "mobile", value = "手机号"),
            @ApiImplicitParam(paramType = "query", name = "memberLevel", value = "会员类型"),
            @ApiImplicitParam(paramType = "query", name = "inviteId", value = "邀请人"),
            @ApiImplicitParam(paramType = "query", name = "memberStatus", value = "会员状态 Y代表未过期，N代表已过期"),
            @ApiImplicitParam(paramType = "query", name = "isFlag", value = "Y代表禁用，N代表未禁用"),
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_merchant_data",name = "平台商户列表")
    @RequestMapping(value = "/export", method = {RequestMethod.GET})
    public void export(HttpServletRequest request, HttpServletResponse response, String groupCode, String merchantName,
                       String mobile, String memberStatus, String isFlag, Integer memberLevel, Integer inviteId) {
        PagerInfo pageInfo = getPageInfo(request, 1, Integer.MAX_VALUE);

        YdMerchantResult ydMerchantResult = new YdMerchantResult();
        ydMerchantResult.setMobile(mobile);
        ydMerchantResult.setGroupCode(groupCode);
        ydMerchantResult.setMemberStatus(memberStatus);
        ydMerchantResult.setMemberLevel(memberLevel);
        ydMerchantResult.setInviteId(inviteId);
        ydMerchantResult.setMerchantName(merchantName);
        ydMerchantResult.setIsFlag(isFlag);
        // 查询全部供应商和门店
        Page<YdMerchantResult> list = ydMerchantService.findPlatformMerchantList(ydMerchantResult, pageInfo);

        // 只导门店的数据
        try {

            List<YdMerchantExportExcel> excelList = setExportData(list);
            String fileName = "商户导出.xls";
            ExportParams params = new ExportParams("商户导出", "商户导出");

            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

            Workbook workbook =  ExcelExportUtil.exportExcel(params, YdMerchantExportExcel.class, excelList);
            workbook.write(response.getOutputStream());
        } catch (Exception e) {

        }
    }

    private List<YdMerchantExportExcel> setExportData(Page<YdMerchantResult> list) {
        if (list.getTotalRecord() <= 0) return new ArrayList<>();
        List<YdMerchantResult> merchantList = list.getData();

        List<YdMerchantExportExcel> resultList = new ArrayList<>();
        for (YdMerchantResult ydMerchantResult : merchantList) {
            if (EnumSiteGroup.MERCHANT.getCode().equalsIgnoreCase(ydMerchantResult.getGroupCode())
                    && ydMerchantResult.getId().equals(ydMerchantResult.getPid())) {
                YdMerchantExportExcel ydMerchantExportExcel = new YdMerchantExportExcel();
                ydMerchantExportExcel.setChannel(ydMerchantResult.getInviteId());
                ydMerchantExportExcel.setMerchantName(ydMerchantResult.getMerchantName());
                ydMerchantExportExcel.setMobile(ydMerchantResult.getMobile());

                YdMerchantMemberOpenRecordResult req = new YdMerchantMemberOpenRecordResult();
                req.setMerchantId(ydMerchantResult.getId());
                List<YdMerchantMemberOpenRecordResult> memberList = ydMerchantMemberOpenRecordService.getAll(req);
                if (CollectionUtils.isEmpty(memberList)) {
                    ydMerchantExportExcel.setRecordList(new ArrayList<>());
                } else {
                    List<YdMerchantMemberExportExcel> recordList = new ArrayList<>();
                    memberList.forEach(memberData -> {
                        YdMerchantMemberExportExcel ydMerchantMemberExportExcel = new YdMerchantMemberExportExcel();
                        ydMerchantMemberExportExcel.setMemberType(memberData.getMemberType());
                        ydMerchantMemberExportExcel.setOpenType(memberData.getOpenType());
                        ydMerchantMemberExportExcel.setPayPrice(memberData.getPayPrice());
                        ydMerchantMemberExportExcel.setStartTime(memberData.getStartTime());
                        recordList.add(ydMerchantMemberExportExcel);
                    });
                    ydMerchantExportExcel.setRecordList(recordList);
                }
                resultList.add(ydMerchantExportExcel);
            }
        }
        return resultList;
    }

}
