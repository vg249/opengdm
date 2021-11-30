package org.gobiiproject.gobiiprocess.translator

import java.io.File


/**
 * HDF5translator
 *
 * TODO
 */
object HDF5Translator {
    //TODO: create custom exceptions
    private val reservedAlleles = setOf(
        "A", "C", "G", "T", "N",
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "+", "-",
        ".", "?", " "
    )
    private const val offset            = 129   // offset for encoded values
    private const val hdf5delimiter     = 9     // delimiter between hdf5 columns
    private const val hdf5newline       = 10    // delimiter between hdf5 rows
    private const val encodingLimit     = 127   // max number of alleles we can encode
    private const val encodingSeparator = "\t"  // between index and values in lookup table
    private const val encodingDelimiter = ";"   // between values in lookup table

    /**
     * Transform a temporary genotype matrix into a fixed-width, ubyte encoded file suitable for conversion to HDF5 format.
     *
     * @param inputFile          Temporary genotype matrix to encode
     * @param outputFile         Where to write encoded file
     * @param lookupFile         Where to write lookup table
     * @param genotypeSeparator  Separator between line elements (genotypes) in [inputFile]
     * @param alleleSeparator    Separator between alleles in each element (genotype) in [inputFile]
     */
    fun encodeFile(
        inputFile:         File,
        outputFile:        File,
        lookupFile:        File,
        genotypeSeparator: String,
        alleleSeparator:   String
    ) {
        val alleleFilter: (String) -> Boolean = if (alleleSeparator.isEmpty()) {{ it.isNotEmpty() }} else {{ true }}
        outputFile.outputStream().buffered().use { outputStream ->
        lookupFile.bufferedWriter().use          { lookupWriter ->
        inputFile.bufferedReader().useLines      { lineSequence ->
            lineSequence.map { it.splitToSequence(genotypeSeparator) }
                .forEachIndexed { rowIndex, row ->
                    if (rowIndex > 0) outputStream.write(hdf5newline)
                    val encodings = mutableListOf<String>()                     // list of allele encodings
                    row.forEachIndexed { genotypeIndex, genotype ->
                        if (genotypeIndex > 0) outputStream.write(hdf5delimiter)
                        genotype.split(alleleSeparator).filter(alleleFilter)
                            .forEach { allele ->     // this is the actual encoding step
                                if (allele.length != 8 || allele.toIntOrNull() == null) encodeAllele(allele, encodings)
                                else {
                                    encodeAllele(allele.take(4), encodings).let(outputStream::write)
                                    encodeAllele(allele.takeLast(4), encodings)
                                }
                                    .let(outputStream::write)
                        }
                    }
                    if (encodings.size > encodingLimit) {
                        throw Exception("More than $encodingLimit alleles (${encodings.size}) on line $rowIndex")
                    } else if (encodings.isNotEmpty()) {
                        lookupWriter.appendLine(
                            "$rowIndex\t${encodings.joinToString(";") { if (it.isNotEmpty()) "I$it" else "D" }}"
                        )
                    }
                }
        }
        }
        outputStream.write(hdf5newline) // not sure if this is necessary, but an extra newline shouldn't hurt???
        }
        if (lookupFile.length() == 0L) lookupFile.delete()
    }

    fun encodeAllele(allele: String, encodings: MutableList<String>) = when {
        reservedAlleles.contains(allele) -> allele[0].code                      // write character as-is
        encodings.contains(allele)       -> encodings.indexOf(allele) + offset  // previously encoded
        else                             ->                                     // new encoding
            encodings.apply { add(allele) }.lastIndex + offset
    }

    /**
     * Decode a fixed-width, ubyte encoded genotype matrix output from HDF5 format to standard UTF-8.
     *
     * @param inputFile         File to decode. Fixed width, ubyte-encoded.
     * @param outputFile        Where to write decoded file.
     * @param lookupFile        Lookup file for encoded [inputFile].
     * @param genotypeSeparator Separator to append between elements (genotypes) in [outputFile].
     * @param alleleSeparator   Separator to append between alleles in [outputFile].
     * @param markerFast        Whether [inputFile] is marker-oriented.
     * @param markerList        Set of markers to include in [outputFile].
     */
    fun decodeFile(
        inputFile:         File,
        outputFile:        File,
        lookupFile:        File,
        genotypeSeparator: String,
        alleleSeparator:   String,
        markerFast:        Boolean = true,
        markerList:        Set<Int>? = null
    ) {

        val filtering = markerList != null          // null markerlist indicates no filtering
        if (filtering && markerList!!.isEmpty())    // empty markerlist indicates something went wrong
            throw Exception("Argument to 'markerList' is empty.")

        // read encodings into a map of { rowIndex: [ allele1, allele2, ... ] }
        val markerEncodings: Map<Int, List<String>> = try {
            lookupFile.bufferedReader().useLines { lines ->
                // can reduce the size of the map and lookup complexity if we are filtering
                lines.filterIndexed { idx, _ -> !filtering || idx in markerList!! }
                    .associate { line ->
                        line.split("\t").let { (k, v) ->
                            k.toInt() to v.split(";").map { it.substring(1) }
                        }
                    }
            }
        } catch(e: Exception) { emptyMap() }

        var rowIndex               = 0                              // row in file
        var colIndex               = 0                              // column (genotype) in file
        var alleleIndex            = 0                              // index of allele in column (genotype)
        val markerIndex: () -> Int =                                // getter for rowIndex if markerFast, colIndex if sampleFast
            if (markerFast) { { rowIndex } } else { { colIndex } }  // used for filtering and encoding lookup

        outputFile.bufferedWriter().use        { writer       ->
        inputFile.inputStream().buffered().use { encodedBytes ->
            encodedBytes.iterator().forEachRemaining { byte ->
                val int = byte.toInt() and 0xff             // effectively an unsigned byte
                if (filtering) {
                    if (markerIndex() !in markerList!!) {   // not null assertion is safe where filtering == true
                        if (int == hdf5delimiter) {
                            colIndex++                      // needed in case of sample-fast
                            alleleIndex = 0
                        } else if (int == hdf5newline) {
                            rowIndex++
                            colIndex    = 0
                            alleleIndex = 0
                        }
                        return@forEachRemaining             // can skip processing the rest of the row
                    }
                }
                // this whole logic chain assumes file is correctly encoded
                when (int) {
                    hdf5delimiter -> {
                        writer.append(genotypeSeparator)
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
        writer.newLine()
        }
    }
}