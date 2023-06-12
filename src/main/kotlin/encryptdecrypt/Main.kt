package encryptdecrypt

import encryptdecrypt.input.ArgumentsParser
import encryptdecrypt.input.DefaultArgumentsValidator

fun main(args: Array<String>) {
    val input = args.toList()
    try {
        val validator = DefaultArgumentsValidator(input)
        validator.validate()
        val parser = ArgumentsParser(input)
        val argumentProcessor = ArgumentProcessor(parser.parsedArguments())
        argumentProcessor.process()
    } catch (e: Throwable) {
        println("Error: ${e.message}")
    }
}