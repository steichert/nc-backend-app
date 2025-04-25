package za.co.newcreation.backendapp.domain.model

@JvmInline
value class EventState(val name: String) {
    override fun toString(): String = name
}

val ACTIVE = EventState("ACTIVE")
val INACTIVE = EventState("INACTIVE")