package com.availelabs.tabla.files

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.services.s3.S3Client
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import kotlin.uuid.Uuid

@Configuration
class S3Configuration {
    @Bean
    fun s3Client(): S3Client = S3Client.builder().build()
}

@Service
class FileStorageService(private val s3Client: S3Client) {
    fun store(file: MultipartFile) {
        if (file.isEmpty)
            return Fi

        FileStorageResult.Failure(FileStorageError.EMPTY_FILE)

        val contentType = file.contentType
            ?: return FileStorageResult.Failure(FileStorageError.MISSING_CONTENT_TYPE)

        val fileType = FileType.fromContentType(contentType)
            ?: return FileStorageResult.Failure(FileStorageError.UNSUPPORTED_FILE_TYPE)

        val fileName = file.originalFilename ?: "unknown"

        val storedFile = StoredFile(
            // database should generate the UUID, not the app
            id = Uuid.random(),
            name = fileName,
            type = fileType,
            size = file.size
        )

        val destination = uploadDir.resolve(storedFile.id.toString())

        file.inputStream.use { input ->
            Files.newOutputStream(destination, StandardOpenOption.CREATE_NEW).use { output ->
                input.copyTo(output)
            }
        }

        return FileStorageResult.Success(storedFile)
    }
}