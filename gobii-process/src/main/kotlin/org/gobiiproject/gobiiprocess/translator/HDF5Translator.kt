package org.gobiiproject.gobiiprocess.translator

import org.gobiiproject.gobiiprocess.digester.csv.matrixValidation.NucleotideSeparatorSplitter
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
     */
    fun encodeFile(
        inputFile: File,
        outputFile: File,
        lookupFile: File,
        genotypeSeparator: String
    ) {
        /* TODO: If the inputFile is properly sanitized, alleleSeparator should be known ahead of time.
            This check would then be unnecessary if alleleSeparator was passed as an argument. */
        val alleleSeparator = NucleotideSeparatorSplitter.findSeparator(inputFile.absolutePath, genotypeSeparator) ?: ""
        val alleleFilter: (String) -> Boolean = if (alleleSeparator.isEmpty()) { String::isNotEmpty } else {{ true }}
        outputFile.bufferedOutputStream().use      { outputStream ->
        lookupFile.bufferedWriter()      .use      { lookupWriter ->
        inputFile .bufferedReader()      .useLines { lineSequence ->
            lineSequence.map { it.splitToSequence(genotypeSeparator) }
                .forEachIndexed { rowIndex, row ->
                    if (rowIndex > 0) outputStream.write(hdf5newline)
                    val encodings = mutableListOf<String>()                     // list of allele encodings
                    row.forEachIndexed { genotypeIndex, genotype ->
                        if (genotypeIndex > 0) outputStream.write(hdf5delimiter)
                        genotype.split(alleleSeparator)
                            .filter(alleleFilter)
                            .forEach { allele -> encodeAllele(allele, encodings).let(outputStream::write) }
                    }
                    if (encodings.size > encodingLimit) {
                        throw Exception("More than $encodingLimit alleles (${encodings.size}) on line $rowIndex")
                    } else if (encodings.isNotEmpty()) {
                        encodings.joinToString(separator = encodingDelimiter, prefix = "$rowIndex$encodingSeparator") {
                            if (it.isNotEmpty()) "I$it" else "D"
                        }.let(lookupWriter::appendLine)
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

        val filtering = markerList != null          // null markerList indicates no filtering
        if (filtering && markerList!!.isEmpty())    // empty markerList indicates something went wrong
            throw Exception("Argument to 'markerList' is empty.")

        // read encodings into a map of { rowIndex: [ allele1, allele2, ... ] }
        val markerEncodings = if (lookupFile.exists() && lookupFile.canRead()) {
            lookupFile.bufferedReader().useLines { lines ->
                // can reduce the size of the map and lookup complexity if we are filtering
                lines.filterIndexed { idx, _ -> !filtering || idx in markerList!! }
                    .associate { line ->
                        line.split(encodingSeparator).let { (k, v) ->
                            k.toInt() to v.split(encodingDelimiter).map { it.substring(1) }
                        }
                    }
                }
        } else {
            emptyMap()
        }

        var rowIndex                    = 0                         // row in file
        var colIndex                    = 0                         // column (genotype) in file
        var alleleIndex                 = 0                         // index of allele in column (genotype)
        val markerIndex: () -> Int      =                           // getter for rowIndex if markerFast, colIndex if sampleFast
            if (markerFast) { { rowIndex } } else { { colIndex } }  // used for filtering and encoding lookup
        val atSeparatorIndex: (Int) -> Boolean =                    // if SSR data, write delimiter after 4 bytes
            if (fileIsSSR(inputFile, lookupFile)) {{ it == 3 }} else {{ it > 0 }}

        outputFile.bufferedWriter()     .use { writer       ->
        inputFile .bufferedInputStream().use { encodedBytes ->
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
                        if (atSeparatorIndex(alleleIndex++)) writer.append(alleleSeparator)
                        if (int >= offset) {
                            val encodings = markerEncodings[markerIndex()]
                                ?: throw Exception("No lookup row found at row $rowIndex")
                            encodings.elementAtOrNull(int - offset)
                                ?: throw Exception("No encoding for element ${int - offset} on row $rowIndex")
                        } else {
                            int.toChar().toString()
                        }.let(writer::append)
                    }
                }
            }
        }
        writer.newLine()
        }
    }

    // TODO: we should know at runtime what type of dataset we are reading, so seems pointless to have to check.
    /**
     * Check the first row of [inputFile] to determine if it contains SSR data.
     *
     * @param inputFile encoded data file
     * @param lookupFile lookup file
     * @return true if file contains SSR data, false otherwise
     */
    fun fileIsSSR(inputFile: File, lookupFile: File): Boolean {
        if (lookupFile.exists()) return false
        inputFile.bufferedInputStream().use { inputStream ->
            inputStream.iterator().run {
                var alleleLength = 0
                while (hasNext()) {
                    when (next().toInt() and 0xff) {
                        in 0..9       -> alleleLength++
                        hdf5newline   -> if (alleleLength == 8) return true
                        hdf5delimiter -> {
                            if (alleleLength != 8)              return false
                            alleleLength = 0
                        }
                        else          ->                        return false
                    }
                }
            }
        }
        return false
    }

    private fun File.bufferedOutputStream() = outputStream().buffered()
    private fun File.bufferedInputStream() = inputStream().buffered()
}