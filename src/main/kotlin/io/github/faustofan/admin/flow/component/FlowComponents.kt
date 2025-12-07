package io.github.faustofan.admin.flow.component

import com.yomahub.liteflow.core.NodeBooleanComponent
import com.yomahub.liteflow.core.NodeComponent
import org.springframework.stereotype.Component

/**
 * 并行执行节点示例
 */
@Component("parallelNodeA")
class ParallelNodeA : NodeComponent() {

    override fun process() {
        println("[ParallelNodeA] 并行执行节点A - 当前线程: ${Thread.currentThread().name}")
        // 模拟耗时操作
        Thread.sleep(100)
    }
}

@Component("parallelNodeB")
class ParallelNodeB : NodeComponent() {

    override fun process() {
        println("[ParallelNodeB] 并行执行节点B - 当前线程: ${Thread.currentThread().name}")
        Thread.sleep(100)
    }
}

/**
 * 条件判断节点示例 - 用于 IF 表达式
 */
@Component("conditionNode")
class ConditionNode : NodeBooleanComponent() {

    override fun processBoolean(): Boolean {
        // 根据业务逻辑返回 true/false
        val shouldExecuteTrue = true // 这里可以根据上下文或请求数据判断
        println("[ConditionNode] 条件判断结果: $shouldExecuteTrue")
        return shouldExecuteTrue
    }
}

@Component("trueNode")
class TrueNode : NodeComponent() {

    override fun process() {
        println("[TrueNode] 条件为真时执行")
    }
}

@Component("falseNode")
class FalseNode : NodeComponent() {

    override fun process() {
        println("[FalseNode] 条件为假时执行")
    }
}

/**
 * 复杂流程节点示例
 */
@Component("startNode")
class StartNode : NodeComponent() {

    override fun process() {
        println("[StartNode] 流程开始")
    }
}

@Component("taskNodeA")
class TaskNodeA : NodeComponent() {

    override fun process() {
        println("[TaskNodeA] 执行任务A")
    }
}

@Component("taskNodeB")
class TaskNodeB : NodeComponent() {

    override fun process() {
        println("[TaskNodeB] 执行任务B")
    }
}

@Component("taskNodeC")
class TaskNodeC : NodeComponent() {

    override fun process() {
        println("[TaskNodeC] 执行任务C")
    }
}

@Component("endNode")
class EndNode : NodeComponent() {

    override fun process() {
        println("[EndNode] 流程结束")
    }
}

