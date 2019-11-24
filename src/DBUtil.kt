package xyz.anilkan

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PGobject


// FIXME: Başka bir isim veya yöntem bulmalı
fun <T> transactionEnvironment(closure: () -> T): T {
    return transaction { closure() }
}

// TODO: Uygulama başladıktan sonra otomatik çağırılmalı
fun connectDatabase() {
    val datasource = hikari()
    Database.connect(datasource)

    val flyway = Flyway.configure().dataSource(datasource).load()
    //flyway.clean()
    flyway.migrate()
}

private fun hikari(): HikariDataSource {
    val config = HikariConfig()
    config.driverClassName = "org.postgresql.Driver"
    config.jdbcUrl = "jdbc:postgresql://localhost:5432/test"
    config.username = "postgres"
    config.password = "1234"
    config.maximumPoolSize = 3
    config.isAutoCommit = false
    config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    config.validate()
    return HikariDataSource(config)
}

class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}