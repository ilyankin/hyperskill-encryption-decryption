package encryptdecrypt.input

import java.nio.file.Path
import kotlin.reflect.KClass

enum class ArgumentMetadata(val valueType: KClass<*>, vararg val aliases: String) {
    MODE(EncryptionMode::class, "-mode"),
    KEY(UInt::class, "-key"),
    DATA(String::class, "-data"),
    IN(Path::class, "-in"),
    OUT(Path::class, "-out"),
    ALGORITHM(EncryptionAlgorithm::class, "-alg"),
    UNDEFINED(Any::class)
    ;

    companion object {
        fun enumOf(alias: String): ArgumentMetadata {
            values().forEach { if (it.aliases.contains(alias)) return it }
            return UNDEFINED
        }
    }
}

enum class EncryptionMode(vararg val aliases: String) {
    ENC("", "enc", "encryption"),
    DEC("dec", "decryption"),
    ;

    companion object {
        fun enumOf(alias: String): EncryptionMode {
            values().forEach { if (it.aliases.contains(alias)) return it }
            throw IllegalArgumentException("""
                |There's no such alias for the '-mode' argument! 
                |Passed argument: '$alias'
                |Valid arguments: ${values().flatMap { it.aliases.toList() }.joinToString { "'$it'" }}
                """.trimMargin()
            )
        }
    }
}

enum class EncryptionAlgorithm(vararg val aliases: String) {
    SHIFT("", "shift", "sh"),
    UNICODE("unicode", "un"),
    ;

    companion object {
        fun enumOf(alias: String): EncryptionAlgorithm {
            values().forEach { if (it.aliases.contains(alias)) return it }
            throw IllegalArgumentException("There's no such EncryptionAlgorithm for the alias: '$alias'")
        }
    }
}

interface Argument<T> {
    val argument: Pair<ArgumentMetadata, T>
    fun getKey() = argument.first
    fun getValue() = argument.second
}

abstract class AbstractArgument<T>(protected open val input: Collection<String>) : Argument<T> {
    protected fun getValue(arguments: Collection<String>, name: ArgumentMetadata): String {
        val keyToValue = arguments.zipWithNext().find { ArgumentMetadata.enumOf(it.first) == name }
        val value = keyToValue?.second ?: return ""
        if (value[0] == '-') return ""
        return value.removeSurrounding("\'").removeSurrounding("\"")
    }
}

class Mode(override val input: Collection<String>) : AbstractArgument<EncryptionMode>(input) {
    override val argument: Pair<ArgumentMetadata, EncryptionMode> = ArgumentMetadata.MODE to
            EncryptionMode.enumOf(getValue(input, ArgumentMetadata.MODE))
}

class Key(override val input: Collection<String>) : AbstractArgument<UInt>(input) {
    private val value = getValue(input, ArgumentMetadata.KEY)
    override val argument: Pair<ArgumentMetadata, UInt> = ArgumentMetadata.KEY to
            if (value.isBlank()) 0u else try {
                value.toUInt()
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException(
                    "Unable to parse '$value'" +
                            " as non-negative number for the ${ArgumentMetadata.KEY.aliases.first()} argument!"
                )
            }
}

class Data(override val input: Collection<String>) : AbstractArgument<String>(input) {
    override var argument: Pair<ArgumentMetadata, String> =
        ArgumentMetadata.DATA to getValue(input, ArgumentMetadata.DATA)
}

class In(override val input: Collection<String>) : AbstractArgument<Path>(input) {
    override var argument: Pair<ArgumentMetadata, Path> =
        ArgumentMetadata.IN to Path.of(getValue(input, ArgumentMetadata.IN))
}

class Out(override val input: Collection<String>) : AbstractArgument<Path>(input) {
    override var argument: Pair<ArgumentMetadata, Path> =
        ArgumentMetadata.OUT to Path.of(getValue(input, ArgumentMetadata.OUT))
}

class Algorithm(override val input: Collection<String>) : AbstractArgument<EncryptionAlgorithm>(input) {
    override var argument: Pair<ArgumentMetadata, EncryptionAlgorithm> =
        ArgumentMetadata.ALGORITHM to EncryptionAlgorithm.enumOf(getValue(input, ArgumentMetadata.ALGORITHM))
}