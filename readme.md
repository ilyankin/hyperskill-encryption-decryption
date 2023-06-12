# Encryption-Decryption

## Introduction

This project is an implementation of encrypting and decrypting messages and texts using _Caesar_ or _Unicode_ _cipher_ on Kotlin.
The [task](https://hyperskill.org/projects/279) was taken from the Hyperskill (JetBrains Academy) educational platform.

## Description

The Encryption-Decryption is a project that provide command-line interface to **encrypt** or **decrypt** a message
using _Caesar_ or _Unicode_ cipher. The message can be passed as text via program arguments or as text in a file.

**Caesar cipher** - shifts each letter by the specified number (integer key) according to its order in the alphabet.
If the shift reaches the end of the alphabet, then the shift continues from the beginning of the alphabet.

**Unicode cipher** - is a simpler version of the _Caesar cipher_. It shifts each letter by the specified number (integer key) 
according to its order in the alphabet.

List of supported arguments:

1. `-mode` - determine the program's mode (`enc` for encryption, `dec` for decryption).
2. `-key` - set integer key to modify the message.
3. `-data`- define text or ciphertext to encrypt/decrypt.
4. `-in` - specify the path to the input file.
5. `-out` - specify the path to the output file.
6. `-alg` -  determine the cipher's algorithm (`shift` for _Caesar cipher_ , `unicode` for _Unicode cipher_)

## Features

### 1. `-mode` argument

- Take the following argument as a value of this argument:
  - `enc` for encryption
  - `dec` for decryption
- If the value of argument is **missing** or the argument itself, the program works in the **encryption mode**.
- If the value of argument is **invalid**, the following error outputs to the terminal:
```
Error: There's no such alias for the '-mode' argument!
Passed argument: '[your incorret passed value]'
Valid arguments: '', 'enc', 'encryption', 'dec', 'decryption'
```

### 2. `-key` argument

- Take the following argument as a value of this argument. The value **must** be non-negative number.
- If the value of argument is **missing** or the argument itself, the program considers that it is `0`;
- If the value of argument is **invalid**, the following error outputs to the terminal:
```
Error: Unable to parse '[your incorret passed value]' as non-negative number for the -key value!
```

### 3. `-data` argument

- Take the following argument as a value of this argument. This value defines text or ciphertext to encrypt/decrypt.
- If there is no `-data` and no `-in`, the program assumes that encrypted/decrypted message is an empty string - `''`.
- If there are both `-data` and `-in` arguments, the program prefers `-data` over `-in`.

### 4. `-in` argument

- Take the following argument as a value of this argument. This value is a path of an input file with encrypted/decrypted message.
- If there is no `-data` and if the value of argument is **missing**, the program assumes that encrypted/decrypted message is an empty string - `''`.

### 5. `-out` argument

- Take the following argument as a value of this argument. This value is a path of an output file with encrypted/decrypted message.
- If there is no `-out` or if the value of argument is **missing**, the program prints data to the **standard output**.

## Examples

**Example 1:** reading and writing to files; the arguments are: 
`-mode enc -in road_to_treasure.txt -out protected.txt -key 5 -alg unicode`

This command must get data from _road_to_treasure.txt_, encrypt the data with the key of 5, create _protected.txt_, and write ciphertext into it.

**Example 2:** encryption with the unicode algorithm; the arguments are: 
`-mode enc -key 5 -data "Welcome to hyperskill!" -alg unicode`

```text
\jqhtrj%yt%m~ujwxpnqq&
```

**Example 3:** decryption with the unicode algorithm; the arguments are: 
`-key 5 -alg unicode -data "\jqhtrj%yt%m~ujwxpnqq&" -mode dec`

```text
Welcome to hyperskill!
```

**Example 4:** encryption with the shift algorithm; the arguments are: `-key 5 -alg shift -data "Welcome to hyperskill!" -mode enc`

```text
Bjqhtrj yt mdujwxpnqq!
```

**Example 5:** decryption with the shift algorithm; the arguments are: `-key 5 -alg shift -data "Bjqhtrj yt mdujwxpnqq!" -mode dec`

```text
Welcome to hyperskill!
```