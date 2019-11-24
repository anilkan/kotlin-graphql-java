package xyz.anilkan.model

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import xyz.anilkan.transactionEnvironment

data class MovementItem(override val id: Int = 0, val movementId: Int, val name: String,
                        val quantity: Double, val price: Double) : ModelBase

private object MovementItems : Table("movement_items") {
    val id: Column<Int> = integer("id").autoIncrement().primaryKey()
    val movementId: Column<Int> = integer("movement_id") references Movements.id
    val name: Column<String> = varchar("name", 255)
    val quantity: Column<Double> = double("quantity").default(0.0)
    val price: Column<Double> = double("price").default(0.0)
}

private fun MovementItems.toDataObj(row : ResultRow) = MovementItem (
    row[id].toInt(),
    row[movementId].toInt(),
    row[name].toString(),
    row[quantity].toDouble(),
    row[price].toDouble()
)

object MovementItemRepository {
    fun add(movementItem : MovementItem) = transactionEnvironment {
        MovementItems.insert {
            it[movementId] = movementItem.movementId
            it[name] = movementItem.name
            it[quantity] = movementItem.quantity
            it[price] = movementItem.price
        }
    }
}