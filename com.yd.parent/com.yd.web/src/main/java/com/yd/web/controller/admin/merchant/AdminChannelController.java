package com.yd.web.controller.admin.merchant;

import com.yd.api.result.merchant.YdMerchantChannelResult;
import com.yd.api.service.merchant.YdMerchantChannelService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.PropertiesHelp;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.controller.BaseController;
import com.yd.web.util.QrCodeUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 渠道工具，没有页面，只有接口，因为还没有分销系统
 * @author wuyc
 * created 2020/03/30 12:59
 **/
@RestController
@RequestMapping("/admin/merchant/channel")
public class AdminChannelController extends BaseController {

    @Reference
    private YdMerchantChannelService ydMerchantChannelService;

    @MerchantCheck()
    @RequestMapping(value = "/getByMerchantId", method = {RequestMethod.GET})
    public BaseResponse<YdMerchantChannelResult> getByMerchantId(HttpServletRequest request, Integer id) {
        BaseResponse<YdMerchantChannelResult> result = BaseResponse.success(null, "00", "操作成功");
        try {
            Integer merchantId = getCurrMerchantId(request);
            if (merchantId == null || merchantId != 10000) {
                throw new BusinessException("err_no_permission", "您没有权限");
            }
            result.setResult(ydMerchantChannelService.getYdMerchantChannelById(id));

        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @MerchantCheck()
    @RequestMapping(value = "/getByMerchantMerchantId", method = {RequestMethod.GET})
    public BaseResponse<YdMerchantChannelResult> getByMerchantMerchantId(HttpServletRequest request, Integer merchantId) {
        BaseResponse<YdMerchantChannelResult> result = BaseResponse.success(null, "00", "操作成功");
        try {
            Integer currMerchantId = getCurrMerchantId(request);
            if (currMerchantId == null || currMerchantId != 10000) {
                throw new BusinessException("err_no_permission", "您没有权限");
            }
            result.setResult(ydMerchantChannelService.getYdMerchantChannelByMerchantId(merchantId));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @MerchantCheck()
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.GET})
    public BaseResponse<String> saveOrUpdate(HttpServletRequest request, YdMerchantChannelResult req) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            Integer currMerchantId = getCurrMerchantId(request);
            if (currMerchantId == null || currMerchantId != 10000) {
                throw new BusinessException("err_no_permission", "您没有权限");
            }
            if (req.getId() == null || req.getId() <= 0) {
                Integer inviteId = ydMerchantChannelService.insertYdMerchantChannel(req);
                req.setId(inviteId);
                // 生成跳转到注册页的二维码，带着邀请人id
                String domain = PropertiesHelp.getProperty("webDomain", "http://prev-saas.guijitech.com");
                String url = domain + "/front/merchant/register/registerPage?inviteId=" + inviteId;
                String qrCode = QrCodeUtil.makeQrCode(url, 500, 500);
                req.setInviteUrl(qrCode);
            }
            ydMerchantChannelService.updateYdMerchantChannel(req);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            result = BaseResponse.fail("err_qr_code", "生成二维码失败");
        }
        return result;
    }

}
