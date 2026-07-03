package com.availelabs.tabla.files

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import kotlin.uuid.Uuid

internal sealed interface FileUploadResponse {
    data class Success(
        val id: Uuid,
        val name: String,
        val type: FileType,
        val size: Long
    ) : FileUploadResponse

    data class Failure(
        val message: String
    ) : FileUploadResponse
}

@RestController
internal class FileUploadController(
    private val fileStorageService: FileStorageService
) {
    @PostMapping("/upload", consumes = ["multipart/form-data"])
    fun uploadFile(@RequestParam("file") file: MultipartFile): ResponseEntity<FileUploadResponse> {
        return when (val result = fileStorageService.store(file)) {
            is FileStorageResult.Success -> {
                val storedFile = result.storedFile

                ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(FileUploadResponse.Success(storedFile.id, storedFile.name, storedFile.type, storedFile.size))
            }

            is FileStorageResult.Failure -> {
                val status = when (result.error) {
                    FileStorageError.EMPTY_FILE -> HttpStatus.BAD_REQUEST
                    FileStorageError.MISSING_CONTENT_TYPE -> HttpStatus.BAD_REQUEST
                    FileStorageError.UNSUPPORTED_FILE_TYPE -> HttpStatus.UNSUPPORTED_MEDIA_TYPE
                }

                ResponseEntity
                    .status(status)
                    .body(FileUploadResponse.Failure(result.error.message))
            }
        }
    }
}
