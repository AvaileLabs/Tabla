package com.availelabs.tabla.files

import java.io.InputStream

interface ObjectStorage {
    fun put(
        key: String,
        contentLength: Long,
        contentType: String,
        openStream: () -> InputStream
    ): Result<Unit>

    fun delete(key: String): Result<Unit>
}

class ObjectStorageException(
    message: String,
    cause: Throwable
) : RuntimeException(message, cause)