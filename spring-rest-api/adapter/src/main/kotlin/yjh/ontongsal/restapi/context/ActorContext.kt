package yjh.ontongsal.restapi.context

import yjh.ontongsal.restapi.Actor

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
