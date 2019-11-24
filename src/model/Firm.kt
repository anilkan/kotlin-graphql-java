package xyz.anilkan.model

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import xyz.anilkan.transactionEnvironment

data class Firm(override val id: Int = 0, val name: String) : ModelBase

private object Firms : Table("firms") {
    val id: Column<Int> = integer("id").autoIncrement().primaryKey()
    val name: Column<String> = varchar("name", 255)
}

private fun Firms.toDataObj(row: ResultRow) = Firm(
    row[id],
    row[name]
)

object FirmRepository {
    fun find(id: Int) = transactionEnvironment {
        Firms
            .select { Firms.id eq id }
            .map { x -> Firms.toDataObj(x) }
            .first()
    }
}