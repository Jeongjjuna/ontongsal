package wemade.ontongsal.springmodulith.item.application

import wemade.ontongsal.springmodulith.item.domain.Item

interface ItemQueryPort {
    fun findById(itemId: String): Item?
}
