package encryptdecrypt

fun interface Encryptable {
    fun encrypt(data: String, key: Int): String
}

fun interface Decryptable {
    fun decrypt(data: String, key: Int): String
}

interface Cipher : Encryptable, Decryptable {
    val encryptionAlgorithm: (Char, Int) -> Char
    val decryptionAlgorithm: (Char, Int) -> Char

    override fun encrypt(data: String, key: Int) = data.map { encryptionAlgorithm(it, key) }.joinToString("")
    override fun decrypt(data: String, key: Int) = data.map { decryptionAlgorithm(it, key) }.joinToString("")
}

class ShiftCipher : Cipher {
    override val encryptionAlgorithm: (Char, Int) -> Char = { char, key -> encryptChar(char, key) }
    override val decryptionAlgorithm: (Char, Int) -> Char = { char, key -> decryptChar(char, key) }

    private fun encryptChar(char: Char, key: Int): Char {
        if (char in 'a'..'z') {
            return if ((char + key) > 'z') 'a' + (key - ('z' - char) - 1) else char + key
        }
        if (char in 'A'..'Z') {
            return if ((char + key) > 'Z') 'A' + (key - ('Z' - char) - 1) else char + key
        }
        return char
    }

    private fun decryptChar(char: Char, key: Int): Char {
        if (char in 'a'..'z') {
            return if ((char - key) < 'a') 'z' - (key - (char + 1 - 'a')) else char - key
        }
        if (char in 'A'..'Z') {
            return if ((char - key) < 'A') 'Z' - (key - (char + 1 - 'A')) else char - key
        }
        return char
    }
}

class UnicodeCipher : Cipher {
    override val encryptionAlgorithm: (Char, Int) -> Char = { char, key -> char + key }
    override val decryptionAlgorithm: (Char, Int) -> Char = { char, key -> char - key }
}