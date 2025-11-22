package io.github.faustofanb.admin.module.system.service;

import io.github.faustofanb.admin.common.exception.BizException;
import io.github.faustofanb.admin.module.system.model.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysLoginService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public String login(String username, String password) {
        // TODO: Captcha validation

        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                throw new BizException("User or password incorrect");
            } else {
                throw new BizException(e.getMessage());
            }
        }

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // TODO: Record login log
        
        return tokenService.createToken(loginUser);
    }
}
