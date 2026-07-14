package com.availelabs.tabla.files

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.services.s3.S3Client
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.isWritable
import kotlin.uuid.Uuid

@Configuration
class S3ClientContainer {
    @Bean
    fun s3ClientBean(): S3Client = S3Client.builder().build()
}

@Service
class FileStorageService(private val s3Client: S3Client) {
    private val uploadDir: Path = Path.of("uploads")

    init {
        Files.createDirectories(uploadDir)

        require(Files.isDirectory(uploadDir)) {
            "Upload path is not a directory: $uploadDir"
        }

        require(uploadDir.isWritable()) { "Upload directory is not writable" }
    }

    fun store(file: MultipartFile): FileStorageResult {
        if (file.isEmpty)
            return FileStorageResult.Failure(FileStorageError.EMPTY_FILE)

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