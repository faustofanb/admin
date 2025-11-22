package io.github.faustofanb.admin.module.system.service.impl;

import io.github.faustofanb.admin.common.exception.BizException;
import io.github.faustofanb.admin.module.system.domain.entity.SysUser;
import io.github.faustofanb.admin.module.system.domain.entity.SysUserDraft;
import io.github.faustofanb.admin.module.system.repository.SysUserRepository;
import io.github.faustofanb.admin.module.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public SysUser selectUserByUserName(String userName) {
        return userRepository.findByUserName(userName).orElse(null);
    }

    @Override
    public boolean checkUserNameUnique(String userName) {
        return userRepository.findByUserName(userName).isEmpty();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long userId, String password) {
        if (!userRepository.existsById(userId)) {
            throw new BizException("用户不存在");
        }
        userRepository.update(
                SysUserDraft.$.produce(draft -> {
                    draft.setId(userId);
                    draft.setPassword(passwordEncoder.encode(password));
                })
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserStatus(Long userId, int status) {
        if (!userRepository.existsById(userId)) {
            throw new BizException("用户不存在");
        }
        userRepository.update(
                SysUserDraft.$.produce(draft -> {
                    draft.setId(userId);
                    draft.setStatus(status);
                })
        );
    }
}
