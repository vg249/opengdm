package org.gobiiproject.gobiiprocess.translator

import java.io.File

object Encoder {
    private val reservedAlleles = setOf(
        "A", "C", "G", "T", "N",
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "+", "-",
        ".", "?", " "
    )
    private const val offset = 129
    private const val outputDelimChar = '\t'
    private const val outputNewlineChar = '\n'
    private const val hdf5delimiter = 9
    private const val hdf5newline = 10
    private const val outputDelimUByte: UByte = 9u
    private const val outputNewlineUByte: UByte = 10u

    fun encodeFile(
        inputFile: File,
        outputFile: File,
        lookupFile: File,
        elementSeparator: String,
        alleleSeparator: String
    ) {
        val bos = outputFile.outputStream().buffered()
        val lookupWriter = lookupFile.bufferedWriter()
        inputFile.bufferedReader().useLines { lines ->
            lines.map { it.split(elementSeparator) }
                .forEachIndexed { li, line ->
                    if (li > 0) bos.write(hdf5newline)
                    val encodings = mutableListOf<String>()
                    line.forEachIndexed { gti, gt ->
                        if (gti > 0) bos.write(hdf5delimiter)
                        gt.split(alleleSeparator).forEach { a ->
                            when {
                                reservedAlleles.contains(a) -> a[0].code
                                encodings.contains(a)       -> encodings.indexOf(a) + offset
                                else -> {
                                    encodings.add(a)
                                    encodings.lastIndex + offset
                                }
                            }.let(bos::write)
                        }
                    }
                    if (encodings.isNotEmpty()) {
                        lookupWriter.appendLine(
                            "$li\t${encodings.joinToString(";") { if (it.isEmpty()) "D" else "I$it" }}"
                        )
                    }
                }
        }
        bos.write(hdf5newline)
        bos.close()
        lookupWriter.close()
    }

    fun decodeFile(inputFile: File, outputFile: File, lookupFile: File, elementSeparator: String, alleleSeparator: String) {
        val inputStream = inputFile.inputStream().buffered()
        val lineEncodings = lookupFile.bufferedReader().useLines { lines ->
            lines.associate {
                it.split("\t").run { component1().toInt() to component2().split(";").map { s -> s.substring(1) } }
            }
        }
        outputFile.bufferedWriter().use { writer ->
            var rowIndex = 0
            var alleleIndex = 0
            inputStream.use { encodedBytes ->
                encodedBytes.iterator().forEachRemaining { byte ->
                    var s = byte.toInt().toChar().toString()
                    val bi = byte.toInt() and 0xff
                    if (reservedAlleles.contains(s)) {
                        if (alleleIndex > 0) writer.append(alleleSeparator)
                        writer.append(s)
                        alleleIndex++
                    }
                    else if (bi == hdf5delimiter) {
                        writer.append(elementSeparator)
                        alleleIndex = 0
                    }
                    else if (bi == hdf5newline) {
                        writer.newLine()
                        rowIndex++
                        alleleIndex = 0
                    }
                    else {
                        val encodings = lineEncodings[rowIndex] ?: throw Exception("No lookup row found at row $rowIndex")
                        s = encodings.elementAtOrNull(bi - offset) ?: throw Exception("No encoding for element ${bi - offset} on row $rowIndex")
                        if (alleleIndex > 0) writer.append(alleleSeparator)
                        writer.append(s)
                        alleleIndex++
                    }
//                    when (val int = byte.toInt() and 0xff) {
//                        hdf5delimiter -> {
//                            writer.append(elementSeparator)
//                            alleleIndex = 0
//                        }
//                        hdf5newline -> {
//                            writer.newLine()
//                            rowIndex++
//                            alleleIndex = 0
//                        }
//                        else -> {
//                            if (alleleIndex++ > 0) writer.append(alleleSeparator)
//                            if (int >= offset) {
//                                val encodings = lineEncodings[rowIndex] ?: throw Exception("No lookup row found at row $rowIndex")
//                                encodings.elementAtOrNull(int - offset) ?: throw Exception("No encoding for element ${int - offset} on row $rowIndex")
//                            } else {
//                                int.toChar().toString()
//                            }.let { writer.append(it) }
//                        }
//                    }
                }
            }
            writer.appendLine()
            return@use
        }
    }

}