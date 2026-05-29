package wemade.ontongsal.springmodulith.item.infrastructure

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Primary
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import wemade.ontongsal.springmodulith.item.application.ItemQueryPort
import wemade.ontongsal.springmodulith.item.domain.ItemStatus
import wemade.ontongsal.springmodulith.shared.TransactionRunner
import java.math.BigDecimal

@Primary
@Repository
class ExposedItemAdapter(
    private val transaction: TransactionRunner,
) : ItemQueryPort {

    @Transactional
    @EventListener(ApplicationReadyEvent::class)
    fun init() {

        if (ItemTable.selectAll().empty()) {

            ItemTable.insert {
                it[id] = "1"
                it[name] = "Keyboard"
                it[price] = BigDecimal("30000")
                it[status] = ItemStatus.ON_SALE.name
            }

            ItemTable.insert {
                it[id] = "2"
                it[name] = "Mouse"
                it[price] = BigDecimal("15000")
                it[status] = ItemStatus.SOLD_OUT.name
            }
        }
    }

    override fun findById(itemId: String) = transaction.run {
        ItemTable
            .selectAll()
            .where { ItemTable.id eq itemId }
            .singleOrNull()
            ?.toItem()
    }
}
