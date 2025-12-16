package yjh.ontongsal.api.common.config.jpa

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

interface TransactionRunner {
    fun <T> run(block: () -> T): T
    fun <T> readOnly(block: () -> T): T
    fun <T> runNew(block: () -> T): T
}

@Component
class TransactionAdvice : TransactionRunner {
    @Transactional
    override fun <T> run(block: () -> T): T = block()

    @Transactional(readOnly = true)
    override fun <T> readOnly(block: () -> T): T = block()

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun <T> runNew(block: () -> T): T = block()
}

@Component
class TransactionManager(
    private val transactionAdvice: TransactionAdvice,
) {
    fun <T> run(block: () -> T): T = transactionAdvice.run(block)
    fun <T> readOnly(block: () -> T): T = transactionAdvice.readOnly(block)
    fun <T> runNew(block: () -> T): T = transactionAdvice.runNew(block)
}
