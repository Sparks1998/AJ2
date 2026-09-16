package com.aj2.aj2.modules.document.infrastructure

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.util.UUID

/**
 * Stub local-disk storage adapter. Swap for an S3/object-storage-backed
 * implementation when a real backing store is available.
 */
@Component
class LocalFileStorage(
    @Value($$"${app.file-storage.base-dir}") baseDir: String,
) : FileStorage {
    private val root: Path = Path.of(baseDir).toAbsolutePath().normalize().also {
        Files.createDirectories(it)
    }

    override fun store(file: MultipartFile): StoredFile {
        val fileKey = "${UUID.randomUUID()}-${file.originalFilename ?: "file"}"
        val target = root.resolve(fileKey)
        file.inputStream.use { input -> Files.copy(input, target) }

        return StoredFile(
            fileKey = fileKey,
            fileName = file.originalFilename ?: fileKey,
            mimeType = file.contentType ?: "application/octet-stream",
            sizeBytes = file.size,
        )
    }
}
