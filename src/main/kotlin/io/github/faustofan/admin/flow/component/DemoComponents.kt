package io.github.faustofan.admin.flow.component

import com.yomahub.liteflow.core.NodeComponent
import org.springframework.stereotype.Component

/**
 * 示例节点A - 演示普通节点
 *
 * @LiteflowComponent 注解的 value 对应规则文件中的节点 id
 */
@Component("demoNodeA")
class DemoNodeA : NodeComponent() {

    override fun process() {
        // 获取请求数据
        val requestData = this.getRequestData<Any>()

        // 获取上下文（如果有的话）
        // val context = this.getContextBean(YourContext::class.java)

        println("[DemoNodeA] 执行节点A - 这是一个示例节点, requestData=$requestData")

        // 可以设置数据传递给下一个节点
        // this.setIsEnd(true) // 如果需要终止流程
    }
}

@Component("demoNodeB")
class DemoNodeB : NodeComponent() {

    override fun process() {
        println("[DemoNodeB] 执行节点B")
    }
}

@Component("demoNodeC")
class DemoNodeC : NodeComponent() {

    override fun process() {
        println("[DemoNodeC] 执行节点C")
    }
}

