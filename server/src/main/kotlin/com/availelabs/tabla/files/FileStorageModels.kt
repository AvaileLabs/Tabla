package com.availelabs.tabla.files

import kotlin.uuid.Uuid

internal enum class FileType(
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

internal data class StoredFile(
    val id: Uuid,
    val name: String,
    val type: FileType,
    val size: Long
)

internal sealed interface FileStorageResult {
    data class Success(val storedFile: StoredFile) : FileStorageResult
    data class Failure(val error: FileStorageError) : FileStorageResult
}

internal enum class FileStorageError(
    val message: String
) {
    EMPTY_FILE("Empty file"),
    MISSING_CONTENT_TYPE("Missing content type"),
    UNSUPPORTED_FILE_TYPE("Unsupported file type"),
}