package nl.coffeeit.aroma.pincode.extension

fun String.digits() = filter { it.isDigit() }

fun String.digitsAndLetters() = filter { it.isDigit() || it.isLetter() }