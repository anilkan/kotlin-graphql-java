package xyz.anilkan.repository

import org.jetbrains.exposed.sql.*
import xyz.anilkan.model.Safe
import xyz.anilkan.transactionEnviroment

object Safes : Table() {
    val id: Column<Int> = integer("id").autoIncrement().primaryKey()
    val code: Column<String> = varchar("code", 24)
    val name: Column<String> = varchar("name", 255)
    val balance: Column<Double> = double("balance").default(0.0)
}

fun Safes.toDataObj(row: ResultRow) = Safe(
    row.getOrNull(id)?.toInt(),
    row.getOrNull(code).toString(),
    row.getOrNull(name).toString(),
    row.getOrNull(balance)?.toDouble()
)

object SafeRepository : Repository<Safe> {
    override fun add(element: Safe): Int = 0

    override fun getElement(indexer: Int): Safe =
        transactionEnviroment {
            Safes
                .select { Safes.id eq indexer }
                .map { x -> Safes.toDataObj(x) }
                .first()
        }

    fun getElement(indexer: Int, columns: List<Column<*>>) : Safe =
        transactionEnviroment {
            Safes
                .slice(columns)
                .select { Safes.id eq indexer }
                .map { x -> Safes.toDataObj(x) }
                .first()
        }
}