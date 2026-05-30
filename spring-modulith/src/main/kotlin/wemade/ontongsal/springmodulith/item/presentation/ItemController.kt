package wemade.ontongsal.springmodulith.item.presentation

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import wemade.ontongsal.springmodulith.item.application.ItemService

@RestController
@RequestMapping("/items")
class ItemController(
    private val itemService: ItemService,
) {

    @GetMapping("/{itemId}/snapshot")
    fun getItemSnapshot(
        @PathVariable itemId: String,
    ): ItemResponse {

        val snapshot = itemService.getItemSnapshot(itemId)

        return ItemResponse.from(snapshot)
    }
}
