package com.rexel.framework.web.service;

import com.rexel.common.constant.Constants;
import com.rexel.common.core.domain.model.LoginUser;
import com.rexel.common.core.redis.RedisCache;
import com.rexel.common.exception.ServiceException;
import com.rexel.common.exception.user.CaptchaException;
import com.rexel.common.exception.user.CaptchaExpireException;
import com.rexel.common.exception.user.TenantException;
import com.rexel.common.utils.MessageUtils;
import com.rexel.common.utils.ServletUtils;
import com.rexel.common.utils.ip.IpUtils;
import com.rexel.config.ISysConfigServiceFrameworkApi;
import com.rexel.framework.manager.AsyncTtlManager;
import com.rexel.framework.manager.factory.AsyncFactory;
import com.rexel.user.ISysTenantServiceFrameworkApi;
import com.rexel.user.ISysUserServiceFrameworkApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 登录校验方法
 *
 * @author ids-admin
 */
@Component
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserServiceFrameworkApi userService;

    @Autowired
    private ISysConfigServiceFrameworkApi configService;

    @Autowired
    private ISysTenantServiceFrameworkApi tenantService;

    public String login(String phoneNumber, String password) {
        return authentication(phoneNumber, password);
    }


    /**
     * 登录验证
     *
     * @param phoneNumber 手机号
     * @param password    密码
     * @param code        验证码
     * @param uuid        唯一标识
     * @return 结果
     */
    public String login(String phoneNumber, String password, String code, String uuid) {
        boolean captchaOnOff = configService.selectCaptchaOnOff();
        // 验证码开关
        if (captchaOnOff) {
            validateCaptcha(phoneNumber, code, uuid);
        }
        return authentication(phoneNumber, password);
    }



    /**
     * 校验验证码
     *
     * @param phoneNumber 用户名
     * @param code        验证码
     * @param uuid        唯一标识
     * @return 结果
     */
    public void validateCaptcha(String phoneNumber, String code, String uuid) {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            AsyncTtlManager.me().execute(AsyncFactory.recordLogininfor(phoneNumber, null, null, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            AsyncTtlManager.me().execute(AsyncFactory.recordLogininfor(phoneNumber, null, null, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        userService.updateUserLogin(userId, IpUtils.getIpAddr(ServletUtils.getRequest()));
    }

    private String authentication(String phoneNumber, String password) {
        // 用户验证
        Authentication authentication;
        try {
            //UserPrincipal userPrincipal;
            //如果要添加多种登录方式需要配置 不同登录接口对应不同Filter  extends AbstractAuthenticationProcessingFilter 实现
//            userPrincipal = new UserPrincipal(phoneNumber);
//            if (isPhone(username)) {
//                userPrincipal = new UserPrincipal(engName, UserPrincipal.Type.MOBILE, username);
//            } else {
//                userPrincipal = new UserPrincipal(engName, UserPrincipal.Type.USERNAME, username);
//            }
            //该方法会去调用 UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(phoneNumber, password));
            // 该方法会去调用 GrassAuthenticationProvider.authenticate
//            authentication = authenticationManager
//                    .authenticate(new GrassAuthenticationToken(userPrincipal, password));
        } catch (Exception e) {
            if (e instanceof TenantException) {
                throw e;
            } else if (e instanceof BadCredentialsException) {
                AsyncTtlManager.me().execute(AsyncFactory.recordLogininfor(phoneNumber, null, null, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new ServiceException(MessageUtils.message("user.password.not.match"));
            }else {
                AsyncTtlManager.me().execute(AsyncFactory.recordLogininfor(phoneNumber, null, null, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userName = userService.selectUserNameByPhoneNumber(phoneNumber);
        AsyncTtlManager.me().execute(AsyncFactory.recordLogininfor(phoneNumber, userName, loginUser.getUserId(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    private boolean isPhone(String value) {
        Pattern pattern = Pattern.compile("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$");
        Matcher matcher = pattern.matcher(value);
        return matcher.find();
    }
}
