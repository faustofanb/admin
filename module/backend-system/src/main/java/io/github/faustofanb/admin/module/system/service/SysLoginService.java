package io.github.faustofanb.admin.module.system.service;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.github.faustofanb.admin.common.exception.BizException;
import io.github.faustofanb.admin.module.audit.domain.entity.SysLogininforDraft;
import io.github.faustofanb.admin.module.audit.service.SysLogininforService;
import io.github.faustofanb.admin.module.system.model.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SysLoginService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final SysLogininforService sysLogininforService;

    public String login(String username, String password) {
        // TODO: Captcha validation

        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                    password);
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                recordLogininfor(username, "1", "User or password incorrect");
                throw new BizException("User or password incorrect");
            } else {
                recordLogininfor(username, "1", e.getMessage());
                throw new BizException(e.getMessage());
            }
        }

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLogininfor(username, "0", "Login success");

        return tokenService.createToken(loginUser);
    }

    private void recordLogininfor(String username, String status, String message) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        String ip = request != null ? request.getRemoteAddr() : "unknown"; // Simplified IP retrieval

        sysLogininforService.saveLogininfor(SysLogininforDraft.$.produce(draft -> {
            draft.setUserName(username);
            draft.setIpaddr(ip);
            draft.setStatus(status);
            draft.setMsg(message);
            draft.setAccessTime(LocalDateTime.now());
        }));
    }
}
