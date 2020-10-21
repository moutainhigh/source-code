package com.yd.web.controller.admin.menu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yd.api.result.menu.YdMenuResult;
import com.yd.api.service.menu.MenuService;
import com.yd.core.res.BaseResponse;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.controller.BaseController;

/**
 * 菜单管理类
 **/
@RestController
@RequestMapping("/admin/menu")
public class AdminMenuController extends BaseController {

    @Reference
    private MenuService menuService;
	
    @MerchantCheck
    @RequestMapping("/list")
    @ApiOperation(value = "菜单列表", httpMethod = "GET")
    public BaseResponse<List<YdMenuResult>> menuList(HttpServletRequest request) {
    	return BaseResponse.success(menuService.getMenuList(getCurrMerchantId(request)));
    }
    
}
