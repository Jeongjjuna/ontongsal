package yjh.ontongsal.restapi.adapter.persistence.jpa.auditing

import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import yjh.ontongsal.restapi.adapter.context.ActorContext
import java.util.*

@Component
class ContextAuditorAware : AuditorAware<String> {

    override fun getCurrentAuditor(): Optional<String> {
        return Optional.of(
            ActorContext.getOrSystem().userName
        )
    }
}
