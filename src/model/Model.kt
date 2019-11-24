package xyz.anilkan.model

import org.joda.time.DateTime

interface ModelBase {
    val id: Int
}

interface MovementBase : ModelBase {
    val date: DateTime
    val fromId: Int
    val toId: Int

    val type: MovementType
    val from: Any
    val to: Any
}

interface FinancialMovement : MovementBase

enum class MovementType {
    EXPENSE, INCOME
}

interface Repository<D> {
    fun add(element: D): Int
    //fun getAll(): Sequence<D>
    //fun remove(indexer: Int): Int
    //fun replace(indexer: Int, element: D): Int
    //fun getElement(indexer: Int): D
    //fun filter(predicate: SqlExpressionBuilder.() -> Op<Boolean>): Sequence<D>
}
