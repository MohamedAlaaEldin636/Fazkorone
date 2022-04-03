@file:Suppress("BlockingMethodInNonBlockingContext")

package com.grand.ezkorone.core.customTypes

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

object DownloadingUtils {

    private const val CONNECTION_TIMEOUT = 30_000

    /**
     * @return `true` if downloaded successfully isa.
     */
    suspend fun downloadFile(link: String, destinationFile: File, publishProgress: (Int) -> Unit = {}) = withContext(Dispatchers.IO) {
        val downloaded = try {
            var count: Int

            val url = URL(link)

            val connection = url.openConnection()
            connection.connectTimeout = CONNECTION_TIMEOUT
            connection.readTimeout = CONNECTION_TIMEOUT
            connection.connect()

            val lengthOfFile = connection.contentLength

            val input = BufferedInputStream(url.openStream())
            val output = FileOutputStream(destinationFile)

            val data = ByteArray(1024)

            var total = 0
            while (input.read(data).also { count = it } != -1) {
                total += count
                withContext(Dispatchers.Main) {
                    publishProgress(total * 100 / lengthOfFile)
                }
                output.write(data, 0, count)
            }

            output.flush()
            output.close()
            input.close()

            true
        }catch (throwable: Throwable) {
            false
        }

        if (!downloaded) {
            destinationFile.delete()
        }

        downloaded
    }

}
