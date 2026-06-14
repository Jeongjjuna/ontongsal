package wemade.ontongsal.springmodulith.shared.transation

interface TransactionRunner {
    fun <T> run(block: () -> T): T
    fun <T> readOnly(block: () -> T): T
    fun <T> runNew(block: () -> T): T
}