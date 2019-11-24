package xyz.anilkan

import org.joda.time.DateTime
import xyz.anilkan.model.Expense
import xyz.anilkan.model.MovementRepository
import xyz.anilkan.model.SafeRepository

fun main() {
    connectDatabase()

    //println(SafeRepository.find(18))

    val expense = Expense(0, DateTime(), 99, 99)

    MovementRepository.add(expense)
}