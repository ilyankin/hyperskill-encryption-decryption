package encryptdecrypt.input

fun interface ArgumentsValidator {
    fun validate()
}

abstract class AbstractArgumentsValidator<T>(open val input: T) : ArgumentsValidator

open class DefaultArgumentsValidator(final override val input: Collection<String>) :
    AbstractArgumentsValidator<Collection<String>>(input) {
    private val argumentsMetadata = ArgumentMetadata.values();

    override fun validate() {
        validateKeys()
        checkDuplicateKeys()
    }

    private fun checkDuplicateKeys() {
        val (index, argument) = input.findFirstDuplicateIndex()
        if (index == -1) return
        val string = input.take(index + 1).joinToString()
        throw IllegalArgumentException("Find '$argument' duplicate argument starting with character ${string.lastIndexOf(argument) - argument.length + 1}")
    }

    private fun validateKeys() {
        val argumentNames = argumentsMetadata.flatMap { it.aliases.toList() }
        input.filter { it.startsWith('-') }.forEach {
            if (!argumentNames.contains(it)) throw IllegalArgumentException("The Encryption-Decryption doesn't support '$it' argument")
        }
    }
}

fun Collection<String>.findFirstDuplicateIndex(): Pair<Int, String> {
    val seen = mutableSetOf<String>()
    this.forEachIndexed { index, item ->
        if (item in seen) {
            return index to item
        } else {
            seen.add(item)
        }
    }
    return -1 to ""
}