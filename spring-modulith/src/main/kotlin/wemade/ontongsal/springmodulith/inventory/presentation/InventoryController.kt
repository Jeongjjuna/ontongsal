package wemade.ontongsal.springmodulith.inventory.presentation

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import wemade.ontongsal.springmodulith.inventory.application.InventoryService

@RestController
@RequestMapping("/inventories")
class InventoryController(
    private val inventoryService: InventoryService
) {

    @GetMapping("/{itemId}")
    fun getInventory(
        @PathVariable itemId: String
    ): InventoryResponse {

        val inventory = inventoryService.findByItemId(itemId)

        return InventoryResponse.from(inventory)
    }
}
