package com.yd.web.controller.admin.material;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.material.YdMaterialPictureResult;
import com.yd.api.service.material.YdMaterialPictureService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.controller.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

/**
 * 素材库controller
 * @author wuyc
 * created 2019/10/28 15:54
 **/
@RestController
@RequestMapping("/admin/material")
@MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="mod_material",name = "素材管理")
public class AdminMaterialController  extends BaseController {

    @Reference
    private YdMaterialPictureService ydMaterialPictureService;

    @ApiOperation(value = "素材库列表", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_material_list",name = "平台素材库列表")
    @RequestMapping(value = "/findMaterialList", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<Page<YdMaterialPictureResult>> findMaterialList(HttpServletRequest request) {
        BaseResponse<Page<YdMaterialPictureResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            PagerInfo pagerInfo = new PagerInfo(request, 1, Integer.MAX_VALUE);
            result.setResult(ydMaterialPictureService.findMaterialListByPage(pagerInfo));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "素材库新增", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pictureUrl", value = "图片地址", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_material_add",name = "平台素材库新增")
    @RequestMapping(value = "/addMaterial", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<Page<YdMaterialPictureResult>> addMaterial(HttpServletRequest request, String pictureUrls) {
        BaseResponse<Page<YdMaterialPictureResult>> result = BaseResponse.success(null, "00", "添加成功");
        try {
            String[] imageList = pictureUrls.split(",");
            for (String pictureUrl : imageList) {
                YdMaterialPictureResult req = new YdMaterialPictureResult();
                req.setPictureUrl(pictureUrl);
                req.setIsEnable("Y");
                req.setSort(1000);
                ydMaterialPictureService.insertYdMaterialPicture(req);
            }
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "素材库删除", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "页码", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_material_delete",name = "平台素材库删除")
    @RequestMapping(value = "/deleteMaterial", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<Page<YdMaterialPictureResult>> deleteMaterial(HttpServletRequest request, Integer id) {
        BaseResponse<Page<YdMaterialPictureResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            ydMaterialPictureService.deleteYdMaterialPicture(id);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }


}
