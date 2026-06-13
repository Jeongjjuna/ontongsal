package wemade.ontongsal.springmodulith.event.presentation

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import wemade.ontongsal.springmodulith.event.application.EventAdminService

@RestController
@RequestMapping("/admin/events")
class EventAdminController(
    private val eventAdminService: EventAdminService,
) {

    @GetMapping
    fun list(): List<EventResponse> =
        eventAdminService.findAll().map(EventResponse::from)

    // 운영툴: DB 컬럼 전부, 필터 없이.
    @GetMapping("/raw")
    fun listRaw(): List<EventMasterTreeResponse> =
        eventAdminService.findAllMasterTrees().map(EventMasterTreeResponse::from)

    @PostMapping
    fun createParent(@RequestBody request: CreateParentEventRequest): Map<String, Long> {
        val id = eventAdminService.createParent(request.toCommand())
        return mapOf("eventId" to id)
    }

    @PostMapping("/with-items")
    fun createWithItems(@RequestBody request: CreateEventWithItemsRequest): Map<String, Long> {
        val id = eventAdminService.createWithItems(request.toParentCommand(), request.toChildCommands())
        return mapOf("eventId" to id)
    }

    @PutMapping("/{eventId}")
    fun updateParent(
        @PathVariable eventId: Long,
        @RequestBody request: UpdateParentEventRequest,
    ) {
        eventAdminService.updateParent(eventId, request.toCommand())
    }

    @DeleteMapping("/{eventId}")
    fun deleteParent(@PathVariable eventId: Long) {
        eventAdminService.deleteParent(eventId)
    }

    @PostMapping("/{parentId}/items")
    fun createChild(
        @PathVariable parentId: Long,
        @RequestBody request: CreateChildEventRequest,
    ): Map<String, Long> {
        val id = eventAdminService.createChild(parentId, request.toCommand())
        return mapOf("eventId" to id)
    }

    @PutMapping("/items/{childEventId}")
    fun updateChild(
        @PathVariable childEventId: Long,
        @RequestBody request: UpdateChildEventRequest,
    ) {
        eventAdminService.updateChild(childEventId, request.toCommand())
    }

    @DeleteMapping("/items/{childEventId}")
    fun deleteChild(@PathVariable childEventId: Long) {
        eventAdminService.deleteChild(childEventId)
    }
}
