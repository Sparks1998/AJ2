package com.aj2.aj2.modules.document.infrastructure

import org.springframework.web.multipart.MultipartFile

data class StoredFile(
    val fileKey: String,
    val fileName: String,
    val mimeType: String,
    val sizeBytes: Long,
)

interface FileStorage {
    fun store(file: MultipartFile): StoredFile
}
