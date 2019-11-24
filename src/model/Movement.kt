package xyz.anilkan.model

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.joda.time.DateTime
import xyz.anilkan.PGEnum
import xyz.anilkan.transactionEnvironment

data class Expense(override val id: Int = 0, override val date: DateTime = DateTime(), override val fromId: Int,
                   override val toId: Int) : FinancialMovement {
    override val type: MovementType = MovementType.EXPENSE
    override val from: Safe by lazy { transactionEnvironment { SafeRepository.find(fromId) } }
    override val to: Firm by lazy { transactionEnvironment { FirmRepository.find(toId) } }
}

private fun Movements.toExpense(row: ResultRow) = Expense(
    row[id].toInt(),
    row[date].toDateTime(),
    row[from].toInt(),
    row[to].toInt()
)

private fun Movements.fromExpense(statement: InsertStatement<Number>, expense: Expense) {
    statement[date] = expense.date
    statement[from] = expense.fromId
    statement[to] = expense.toId
    statement[type] = expense.type
}

data class Income(override val id: Int = 0, override val date: DateTime = DateTime(), override val fromId: Int,
                   override val toId: Int) : FinancialMovement {
    override val type: MovementType = MovementType.INCOME
    override val from: Firm by lazy { transactionEnvironment { FirmRepository.find(fromId) } }
    override val to: Safe by lazy { transactionEnvironment { SafeRepository.find(toId) } }
}

private fun Movements.toIncome(row: ResultRow) = Income(
    row[id].toInt(),
    row[date].toDateTime(),
    row[from].toInt(),
    row[to].toInt()
)

private fun Movements.fromIncome(statement: InsertStatement<Number>, income: Income) {
    statement[date] = income.date
    statement[from] = income.fromId
    statement[to] = income.toId
    statement[type] = income.type
}

internal object Movements : Table("movements") {
    val id: Column<Int> = integer("id").autoIncrement().primaryKey()
    val date: Column<DateTime> = datetime("date")
    val type = customEnumeration(
        "type",
        "MovementType",
        { value -> MovementType.valueOf(value as String) },
        { PGEnum("MovementType", it) })
    val from: Column<Int> = integer("from")
    val to: Column<Int> = integer("to")
}

private fun Movements.toDataObj(row: ResultRow): MovementBase {
    return when(row[type]) {
        MovementType.EXPENSE -> toExpense(row)
        MovementType.INCOME -> toIncome(row)
    }
}

object MovementRepository : Repository<MovementBase> {
    override fun add(movement: MovementBase) = transactionEnvironment {
        Movements.insert {
            x -> when(movement.type) {
                MovementType.INCOME -> Movements.fromIncome(x, movement as Income)
                MovementType.EXPENSE -> Movements.fromExpense(x, movement as Expense)
            }
        } get Movements.id
    }

    fun find(id: Int): MovementBase = transactionEnvironment {
        Movements
            .select { Movements.id eq id }
            .map { x -> Movements.toDataObj(x) }
            .first()
    }
}