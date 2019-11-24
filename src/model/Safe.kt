package xyz.anilkan.model

import org.jetbrains.exposed.sql.*
import xyz.anilkan.transactionEnvironment

data class Safe(override val id: Int = 0, val code: String, val name: String, val balance: Double = 0.0) : ModelBase

object Safes : Table("safes") {
    val id: Column<Int> = integer("id").autoIncrement().primaryKey()
    val code: Column<String> = varchar("code", 24)
    val name: Column<String> = varchar("name", 255)
    val balance: Column<Double> = double("balance").default(0.0)
}

private fun Safes.toDataObj(row: ResultRow) = Safe(
    row[id].toInt(),
    row[code].toString(),
    row[name].toString(),
    row[balance].toDouble()
)

object SafeRepository {
    fun add(safe: Safe): Int = transactionEnvironment {
        Safes.insert {
            it[code] = safe.code
            it[name] = safe.name
            it[balance] = safe.balance
        } get Safes.id
    }

    fun find(id: Int): Safe = transactionEnvironment {
        Safes
            .select { Safes.id eq id }
            .map { x -> Safes.toDataObj(x) }
            .first()
    }

    fun findAll(): List<Safe> = transactionEnvironment {
        Safes
            .selectAll()
            .map { x -> Safes.toDataObj(x) }
    }
}