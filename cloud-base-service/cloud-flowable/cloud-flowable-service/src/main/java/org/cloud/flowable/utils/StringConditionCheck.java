package org.cloud.flowable.utils;

import org.apache.commons.jexl3.*;

public class StringConditionCheck {

    public static void main(String[] args) {

        // 创建 GraalVM 的 JavaScript 上下文
        try {
            // 创建 JexlEngine 实例
            JexlEngine jexl = new JexlBuilder().create();

            // 创建 JexlExpression 实例，这里的表达式是 "${day > 1}"
            JexlExpression expression = jexl.createExpression("outcome=='通过'");

            // 准备一个包含变量的上下文
            JexlContext context = new MapContext();
            context.set("day", 1);  // 设置变量 day 的值
            context.set("outcome", "不通过");  // 设置变量 day 的值

            // 执行表达式并获取结果
            Boolean result = (Boolean) expression.evaluate(context);

            // 根据结果进行相应的操作
            if (result) {
                System.out.println("Day is greater than 1.");
            } else {
                System.out.println("Day is not greater than 1.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
