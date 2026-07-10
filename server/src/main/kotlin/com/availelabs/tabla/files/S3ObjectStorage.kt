package com.availelabs.tabla.files

import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.InputStream

class S3ObjectStorage(
    private val s3Client: S3Client,
    private val bucketName: String
) : ObjectStorage {
    init {
        require(bucketName.isNotBlank()) { "Bucket name must not be blank" }

        try {
            s3Client.headBucket { it.bucket(bucketName) }
        } catch (exception: Exception) {
            throw IllegalStateException(
                """
                Failed to initialize S3ObjectStorage.
                Cannot access bucket: '$bucketName'
                Please verify the bucket exists and your IAM permissions are correct.
                """.trimIndent(),
                exception
            )
        }
    }

    override fun put(
        key: String,
        contentLength: Long,
        contentType: String,
        openStream: () -> InputStream
    ): Result<Unit> {
        require(key.isNotBlank()) { "Object key must not be blank" }
        require(contentLength >= 0) { "Content length must not be negative" }
        require(contentType.isNotBlank()) { "Content type must not be blank" }

        return try {
            val request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .contentLength(contentLength)
                .build()

            openStream().use { stream ->
                val body = RequestBody.fromInputStream(stream, contentLength)
                s3Client.putObject(request, body)
            }

            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(ObjectStorageException("Could not store object '$key' in S3", exception))
        }
    }

    override fun delete(key: String): Result<Unit> {
        require(key.isNotBlank()) { "Object key must not be blank" }

        return try {
            require(key.isNotBlank()) { "Object key must not be blank" }

            val request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build()

            s3Client.deleteObject(request)

            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(ObjectStorageException("Could not delete object '$key' from S3", exception))
        }
    }
}