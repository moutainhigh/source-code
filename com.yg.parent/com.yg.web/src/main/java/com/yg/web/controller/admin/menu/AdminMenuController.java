package com.yg.web.controller.admin.menu;

import com.yg.api.result.menu.YgMenuResult;
import com.yg.api.service.menu.YgMenuService;
import com.yg.core.res.BaseResponse;
import com.yg.web.anotation.MerchantCheck;
import com.yg.web.controller.BaseController;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 菜单管理类
 **/
@RestController
@RequestMapping("/admin/menu")
public class AdminMenuController extends BaseController {

    @Reference
    private YgMenuService menuService;
	
    // @MerchantCheck
    @RequestMapping("/list")
    @ApiOperation(value = "菜单列表", httpMethod = "GET")
    public BaseResponse<List<YgMenuResult>> menuList(HttpServletRequest request) {
    	return BaseResponse.success(menuService.getMenuList(getCurrMerchantId(request)));
    }
    
}
