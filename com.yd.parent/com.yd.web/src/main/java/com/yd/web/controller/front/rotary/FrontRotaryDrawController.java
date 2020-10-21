package com.yd.web.controller.front.rotary;

import com.yd.api.result.rotary.YdRotaryDrawActivityResult;
import com.yd.api.result.rotary.YdRotaryDrawPrizeResult;
import com.yd.api.result.rotary.YdRotaryDrawRecordResult;
import com.yd.api.service.rotary.YdRotaryDrawActivityService;
import com.yd.api.service.rotary.YdRotaryDrawPrizeService;
import com.yd.api.service.rotary.YdRotaryDrawRecordService;
import com.yd.api.service.rotary.YdRotaryDrawUserService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.web.controller.FrontBaseController;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author wuyc
 * created 2019/10/17 19:11
 **/
@RestController
@RequestMapping("/front/rotary")
public class FrontRotaryDrawController extends FrontBaseController {

    @Reference
    private YdRotaryDrawActivityService ydRotaryDrawActivityService;

    @Reference
    private YdRotaryDrawRecordService ydRotaryDrawRecordService;

    @Reference
    private YdRotaryDrawPrizeService ydRotaryDrawPrizeService;

    @Reference
    private YdRotaryDrawUserService ydRotaryDrawUserService;

    @RequestMapping(value = "/userDraw", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> userDraw(HttpServletRequest request,
                                         @NotEmpty(message = "活动唯一标识不能为空") String uuid) throws Exception{
        Integer userId = getCurrUserId(request);
        if (userId == 0) {
            userId = 1;
        }
        System.out.println(request.getParameter("aaa"));
        BaseResponse<String> result = BaseResponse.success(null, "00", "抽奖成功");
        ydRotaryDrawActivityService.userDraw(uuid, userId);
        return result;
    }

    @RequestMapping(value = "/findDrawRecord", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<List<YdRotaryDrawRecordResult>> findDrawRecord(HttpServletRequest request,
                                                                       @NotEmpty(message = "活动唯一标识不能为空") String uuid)throws Exception{
        BaseResponse<List<YdRotaryDrawRecordResult>> result = BaseResponse.success(null, "00", "查询成功");
        int userId = getCurrUserId(request);
        if (userId == 0) {
            userId = 1;
        }
        YdRotaryDrawActivityResult rotaryDrawActivityResult = ydRotaryDrawActivityService.getYdRotaryDrawActivityByUuid(uuid);
        result.setResult(ydRotaryDrawRecordService.findUserDrawRecordList(userId, rotaryDrawActivityResult.getId(), null, null));
        return result;
    }

    @RequestMapping(value = "/findUserDrawCount", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<Integer> findUserDrawCount(HttpServletRequest request, @NotEmpty(message = "活动唯一标识不能为空") String uuid) {
        BaseResponse<Integer> result = BaseResponse.success(null, "00", "查询成功");
        try {
            int userId = getCurrUserId(request);
            if (userId == 0) {
                userId = 1;
            }
            result.setResult(ydRotaryDrawUserService.getUserCanUseDrawCount(userId, uuid));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/findPrizeList", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<List<YdRotaryDrawPrizeResult>> findPrizeList(HttpServletRequest request, @NotEmpty(message = "活动唯一标识不能为空") String uuid) {
        BaseResponse<List<YdRotaryDrawPrizeResult>> result = BaseResponse.success(null, "00", "查询成功");
        YdRotaryDrawActivityResult rotaryDrawActivityResult = ydRotaryDrawActivityService.getYdRotaryDrawActivityByUuid(uuid);
        YdRotaryDrawPrizeResult ydRotaryDrawPrizeResult = new YdRotaryDrawPrizeResult();
        ydRotaryDrawPrizeResult.setActivityId(rotaryDrawActivityResult.getId());
        result.setResult(ydRotaryDrawPrizeService.getAll(ydRotaryDrawPrizeResult));
        return result;
    }


}
