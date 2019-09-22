package xyz.anilkan.model

data class Safe(
    override val id: Int?,
    val code: String?,
    val name: String?,
    val balance: Double?
) : Base()