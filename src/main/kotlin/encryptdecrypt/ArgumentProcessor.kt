package encryptdecrypt

import encryptdecrypt.input.Arguments
import encryptdecrypt.input.EncryptionAlgorithm
import encryptdecrypt.input.EncryptionMode
import encryptdecrypt.input.Key
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.name

fun interface Processing {
    fun process()
}

class ArgumentProcessor(private val arguments: Arguments) : Processing {
    override fun process() {
        val (mode, key, data, `in`, out, algorithm) = arguments
        val inputPath = `in`.getValue()

        val content = if (inputPath.name.isEmpty()) {
            data.getValue()
        } else {
            inputPath.readFile()
        }

        val outPath = out.getValue()
        val encryptionMode = mode.getValue()
        val cipher = getCipher(algorithm.getValue())
        if (outPath.name.isEmpty()) {
            println(cipher.crypt(encryptionMode, key, content))
        } else {
            outPath.writeFile(cipher.crypt(encryptionMode, key, content))
        }
    }

    private fun Cipher.crypt(mode: EncryptionMode, key: Key, content: String) = when (mode) {
        EncryptionMode.ENC -> this.encrypt(content, key.getValue().toInt())
        EncryptionMode.DEC -> this.decrypt(content, key.getValue().toInt())
    }

    fun getCipher(algorithm: EncryptionAlgorithm) = when (algorithm) {
        EncryptionAlgorithm.SHIFT -> ShiftCipher()
        EncryptionAlgorithm.UNICODE -> UnicodeCipher()
    }
}


fun Path.readFile(): String {
    val file = this.toFile()
    return try {
        file.readText()
    } catch (e: IOException) {
        throw RuntimeException("Cannot read file '${file.canonicalPath}': ${e.message}")
    } catch (e: SecurityException) {
        throw RuntimeException("Insufficient permissions to read file '${file.canonicalPath}': ${e.message}")
    } catch (e: Exception) {
        throw RuntimeException("Unexpected error occurred while reading file '${file.canonicalPath}': ${e.message}")
    }
}

fun Path.writeFile(content: String) {
    try {
        Files.newBufferedWriter(this).use { writer ->
            writer.write(content)
        }
    } catch (e: IOException) {
        throw RuntimeException("Cannot write to file '$${this.fileName}': ${e.message}")
    } catch (e: SecurityException) {
        throw RuntimeException("Insufficient permissions to write to file '$${this.fileName}': ${e.message}")
    } catch (e: UnsupportedOperationException) {
        throw RuntimeException("Unsupported operation while writing to file '$${this.fileName}': ${e.message}")
    } catch (e: Exception) {
        throw RuntimeException("Unexpected error occurred while writing to file '$${this.fileName}': ${e.message}")
    }
}