package com.yd.web.controller.admin.rotary;

import com.yd.api.LoginUser;
import com.yd.api.result.rotary.YdRotaryDrawActivityResult;
import com.yd.api.service.rotary.YdRotaryDrawActivityService;
import com.yd.core.res.BaseResponse;
import com.yd.web.controller.BaseController;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 转盘抽奖活动
 * @author wuyc
 * created 2019/10/17 15:34
 **/
@RestController
@RequestMapping("/admin/rotary/activity")
public class AdminRotaryDrawActivityController extends BaseController {

    @Reference
    private YdRotaryDrawActivityService ydRotaryDrawActivityService;

    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseResponse<String> saveOrUpdate(HttpServletRequest request, YdRotaryDrawActivityResult params) throws Exception{
        BaseResponse<String> result = BaseResponse.success(null, "00", "保存成功");
        // params.setMerchantId(getCurrMerchantId());
        ydRotaryDrawActivityService.saveOrUpdate(params);
        return result;
    }

}
