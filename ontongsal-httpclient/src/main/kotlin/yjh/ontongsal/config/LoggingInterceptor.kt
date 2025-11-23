package yjh.ontongsal.config

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation


class LoggingInterceptor : MethodInterceptor {
    override fun invoke(invocation: MethodInvocation): Any? {
        println("➡️ 호출: ${invocation.method.name}, args=${invocation.arguments.toList()}")
        val start = System.currentTimeMillis()
        val result = invocation.proceed() // 다음 interceptor 또는 실제 HTTP 호출 수행
        val duration = System.currentTimeMillis() - start
        println("✅ 완료: ${invocation.method.name}, 결과=$result, 소요시간=${duration}ms")
        return result
    }
}
