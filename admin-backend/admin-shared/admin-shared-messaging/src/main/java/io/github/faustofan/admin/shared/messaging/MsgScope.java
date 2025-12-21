package io.github.faustofan.admin.shared.messaging;

public enum MsgScope {
    /** 仅 Spring ApplicationContext 内部传播 */
    LOCAL,
    /** 仅发送到 MQ */
    REMOTE,
    /** 先本地广播，再发送到 MQ */
    GLOBAL
}
