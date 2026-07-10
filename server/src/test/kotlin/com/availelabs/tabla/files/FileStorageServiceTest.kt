package com.availelabs.tabla.files

import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockMultipartFile
import kotlin.uuid.Uuid

class FileStorageServiceTest {
    private val fixedId = Uuid.parse("00000000-0000-0000-0000-000000000000")

    @Test
    fun `store pdf file`() {
        val file = MockMultipartFile(
            "file",
            "statement.pdf",
            "application/pdf",
            "mock pdf content".toByteArray()
        )
    }
}