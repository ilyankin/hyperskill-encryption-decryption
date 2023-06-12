package encryptdecrypt.input

class Arguments(
    private val mode: Mode,
    private val key: Key,
    private val data: Data,
    private val `in`: In,
    private val out: Out,
    private val algorithm: Algorithm
) {
    operator fun component1() = mode
    operator fun component2() = key
    operator fun component3() = data
    operator fun component4() = `in`
    operator fun component5() = out
    operator fun component6() = algorithm
}


class ArgumentsParser(private val input: Collection<String>) {
    fun parsedArguments(): Arguments = Arguments(
        Mode(input),
        Key(input),
        Data(input),
        In(input),
        Out(input),
        Algorithm(input)
    )
}