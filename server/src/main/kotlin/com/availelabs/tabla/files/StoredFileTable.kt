package com.availelabs.tabla.files

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.insert

internal object StoredFileTable : Table("stored_file") {
    val id = uuid("id").databaseGenerated()
    val name = text("name")
    val type = enumerationByName<FileType>("type", 32)
    val size = long("size")

    override val primaryKey = PrimaryKey(id)
}

internal fun insertFileIntoDb(file: StoredFile) {
    StoredFileTable.insert {
        it[name] = file.name
        it[type] = file.type
        it[size] = file.size
    }
}
