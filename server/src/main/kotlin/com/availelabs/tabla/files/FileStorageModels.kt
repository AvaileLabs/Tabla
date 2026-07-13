package com.availelabs.tabla.files

import kotlin.uuid.Uuid

enum class FileType(
    val contentTypes: Set<String>
) {
    IMAGE(setOf("image/jpeg", "image/png", "image/webp")),
    TEXT(setOf("text/plain")),
    PDF(setOf("application/pdf"));

    companion object {
        fun fromContentType(contentType: String): FileType? {
            return entries.find { it.contentTypes.contains(contentType) }
        }
    }
}

data class StoredFile(
    val id: Uuid,
    val name: String,
    val type: FileType,
    val size: Long
) {
    val objectKey: String
        get() = "files/$id"
}