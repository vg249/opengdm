package org.gobiiproject.gobiiprocess.translator

import java.io.File

object HDF5Translator {
    private val reservedAlleles = setOf(
        "A", "C", "G", "T", "N",
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "+", "-",
        ".", "?", " "
    )
    private const val offset        = 129 // offset for encoded values
    private const val hdf5delimiter = 9   // delimiter between hdf5 columns
    private const val hdf5newline   = 10  // delimiter between hdf5 rows
    private const val encodingLimit = 127 // max number of alleles we can encode

    fun encodeFile(
        inputFile:        File,
        outputFile:       File,
        lookupFile:       File,
        elementSeparator: String,
        alleleSeparator:  String
    ) {
        val outputStream = outputFile.outputStream().buffered()
        val lookupWriter = lookupFile.bufferedWriter()
        inputFile.bufferedReader().useLines { lines ->
            lines.map { it.split(elementSeparator) }
                .forEachIndexed { rowIndex, row ->
                    if (rowIndex > 0) outputStream.write(hdf5newline)
                    val encodings = mutableListOf<String>()     // list of allele encodings
                    row.forEachIndexed { genotypeIndex, genotype ->
                        if (genotypeIndex > 0) outputStream.write(hdf5delimiter)
                        genotype.split(alleleSeparator)
                            // here is the actual encoding step
                            .forEach { allele ->
                                when {
                                    // write character as-is
                                    reservedAlleles.contains(allele) -> allele[0].code
                                    encodings.contains(allele)       -> encodings.indexOf(allele) + offset
                                    else -> {
                                        encodings.add(allele)
                                        encodings.lastIndex + offset
                                    }
                                }
                                    .let(outputStream::write)
                        }
                    }
                    if (encodings.size > encodingLimit) {
                        throw Exception(
                            "Too many alleles to encode on line $rowIndex. (${encodings.size} > $encodingLimit)"
                        )
                    } else if (encodings.isNotEmpty()) {
                        lookupWriter.appendLine(
                            "$rowIndex\t${encodings.joinToString(";") { if (it.isEmpty()) "D" else "I$it" }}"
                        )
                    }
                }
        }
        outputStream.write(hdf5newline)
        outputStream.close()
        lookupWriter.close()
    }

    fun decodeFile(
        inputFile:        File,
        outputFile:       File,
        lookupFile:       File,
        elementSeparator: String,
        alleleSeparator:  String,
        markerFast:       Boolean = true,
        markerList:       Set<Int>? = null
    ) {
        val inputStream     = inputFile.inputStream().buffered()
        var rowIndex = 0                    // row in file
        var colIndex = 0                    // column (genotype) in file
        var alleleIndex = 0                 // index of allele in column (genotype)
        val markerIndex: () -> Int =        // gets to rowIndex if markerFast, colIndex if sampleFast
            if (markerFast) { { rowIndex } } else { { colIndex } }
        val filtering = markerList != null  // whether we are filtering on a markerList
        if (filtering && markerList!!.isEmpty()) throw Exception("Argument to 'markerList' is empty.")
        val markerEncodings = lookupFile.bufferedReader().useLines { lines ->
            lines.filterIndexed { idx, _ -> !filtering || idx in markerList!! }
                .associate { line ->
                val (k, v) = line.split("\t")
                k.toInt() to v.split(";").map { it.substring(1) }
            }
        }

        outputFile.bufferedWriter().use { writer ->
            inputStream.use { encodedBytes ->
                encodedBytes.iterator().forEachRemaining { byte ->
                    val int = byte.toInt() and 0xff
                    if (filtering) {
                        if (markerIndex() !in markerList!!) {
                            if (int == hdf5delimiter) {
                                colIndex++
                                alleleIndex = 0
                            } else if (int == hdf5newline) {
                                rowIndex++
                                colIndex    = 0
                                alleleIndex = 0
                            }
                            return@forEachRemaining // TODO: check if this proceeds to next element of iterator or returns entirely
                        }
                    }
                    // this whole logic chain assumes file is correctly encoded
                    when (int) {
                        hdf5delimiter -> {
                            writer.append(elementSeparator)
                            colIndex++
                            alleleIndex = 0
                        }
                        hdf5newline -> {
                            writer.newLine()
                            rowIndex++
                            colIndex    = 0
                            alleleIndex = 0
                        }
                        else -> {
                            if (alleleIndex++ > 0) writer.append(alleleSeparator)
                            if (int >= offset) {
                                val encodings = markerEncodings[markerIndex()]
                                    ?: throw Exception("No lookup row found at row $rowIndex")
                                encodings.elementAtOrNull(int - offset)
                                    ?: throw Exception("No encoding for element ${int - offset} on row $rowIndex")
                            } else {
                                int.toChar().toString()
                            }.let { writer.append(it) }
                        }
                    }
                }
            }
            writer.appendLine()
            return@use
        }
    }
}