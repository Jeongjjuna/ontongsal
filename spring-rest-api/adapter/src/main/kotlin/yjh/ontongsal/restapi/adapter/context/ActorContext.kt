package yjh.ontongsal.restapi.adapter.context

import yjh.ontongsal.restapi.domain.Actor

object ActorContext {

    private val holder = ThreadLocal<Actor>()

    fun set(actor: Actor) {
        holder.set(actor)
    }

    fun get(): Actor? = holder.get()

    fun getOrSystem(): Actor =
        holder.get() ?: Actor.system()

    fun clear() {
        holder.remove()
    }
}
