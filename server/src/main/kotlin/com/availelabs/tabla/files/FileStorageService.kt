package com.availelabs.tabla.files

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.services.s3.S3Client
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Configuration
class S3Configuration {
    @Bean
    fun s3Client(): S3Client = S3Client.builder().build()
}

@Service
class FileStorageService(private val s3Client: S3Client) {
    @OptIn(ExperimentalUuidApi::class)
    fun store(file: MultipartFile): FileStorageResult {
        if (file.isEmpty)
            return FileStorageResult.Failure(FileStorageError.EMPTY_FILE)

        FileStorageResult.Failure(FileStorageError.EMPTY_FILE)

        val contentType = file.contentType
            ?: return FileStorageResult.Failure(FileStorageError.MISSING_CONTENT_TYPE)

        val fileType = FileType.fromContentType(contentType)
            ?: return FileStorageResult.Failure(FileStorageError.UNSUPPORTED_FILE_TYPE)

        val fileName = file.originalFilename ?: "unknown"

        val storedFile = StoredFile(
            id = Uuid.generateV7(),
            name = fileName,
            type = fileType,
            size = file.size
        )
        
        return FileStorageResult.Success(storedFile)
    }
}