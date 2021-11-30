package org.gobiiproject.gobiiprocess.translator.utils

import java.io.*
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Stream

class TempFiles {
    private val timestamp       = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyMMdd-HHmmssnnnnnn"))
        .dropLastWhile { it == '0' }
    private val tmpDir          = Path.of("src/test/tmp/$timestamp").toFile().also(File::mkdir)
    val inputFile               = File("$tmpDir/input-mf.txt")
    val lookupFile              = File("$tmpDir/encoded.h5.idl")
    val encodedFile             = File("$tmpDir/encoded-mf.txt")
    val decodedFile             = File("$tmpDir/decoded.txt")
    val sampleFastEncodedFile   = File("$tmpDir/encoded-sf.txt")
    val sampleFastInputFile     = File("$tmpDir/input-sf.txt")
    val hdf5File                = File("$tmpDir/encoded.h5")

    private val random = Random()

    override fun toString(): String {
        return """
        ${tmpDir.absolutePath}/
            ${inputFile.name}
            ${sampleFastInputFile.name}
            ${lookupFile.name}
            ${encodedFile.name}
            ${sampleFastEncodedFile.name}
            ${decodedFile.name}
        """.trimIndent()
    }

    fun cleanup() = tmpDir.deleteRecursively()

    fun worstCaseInputFile(
        numMarkers: Int = 40_000,
        numSamples: Int = 20,
        ploidy: Int = 2,
        alleleSeparator: String = "/",
        genotypeSeparator: String = "\t"
    ) {
        val genotypes = alleles.windowed(ploidy) { it.joinToString(alleleSeparator) }
        val colReps = numSamples / genotypes.size + 1
        inputFile.bufferedWriter().use { writer ->
            val genotypeString = genotypes
                .joinToString(separator = genotypeSeparator, postfix = genotypeSeparator)
                .repeat(colReps).dropLast(1)
            repeat(numMarkers) { writer.appendLine(genotypeString) }
        }
        // effectively the transpose of the marker-fast input file
        sampleFastInputFile.bufferedWriter().use { writer ->
            val genotypeString = genotypes
                .joinToString("\n") { "$it$genotypeSeparator".repeat(numMarkers).dropLast(1) }
            repeat(colReps) { writer.appendLine(genotypeString) }
        }
    }

    fun createRandomInputFile(rows: Int, cols: Int) {
        val alleles = arrayOf("A", "C", "AC", "ACG")
        createRandomInputFile(rows, cols, 2, alleles)
    }

    fun createRandomInputFile(rows: Int, cols: Int, ploidy: Int) {
        val alleles = arrayOf(
            "A", "C", "G", "T", "N",
            "A", "C", "G", "T", "N",
            "A", "C", "G", "T", "N",
            "A", "C", "G", "T", "N",
            "", "", "",
            "AC", "ACG", "ACGT", "ACGTA", "ACGTAC", "ACGTACG", "ACGTACGT"
        )
        createRandomInputFile(rows, cols, ploidy, alleles)
    }

    fun createRandomInputFile(rows: Int, cols: Int, ploidy: Int, alleles: Array<String>) {
        val writer = BufferedWriter(FileWriter(inputFile))
        for (i in 0 until rows) {
            val row = createRandomRow(cols, alleles, ploidy, "\t", "/")
            writer.append(row)
            writer.append(System.lineSeparator())
        }
        writer.close()
    }

    fun createSimpleRandomInputFile(rows: Int, cols: Int, ploidy: Int) {
        val alleles = arrayOf(
            "A", "C", "G", "T", "N",
            "A", "C", "G", "T", "N",
            "A", "C", "G", "T", "N",
            "A", "C", "G", "T", "N",
            "", "", "",
            "AC", "ACG", "ACGT", "ACGTA", "ACGTAC", "ACGTACG", "ACGTACGT"
        )
    }

    fun getTransposedFileStream(file: File): Stream<String> {
        var reader = BufferedReader(FileReader(file))
        val nRow = reader.readLine().split("\t".toRegex()).toTypedArray().size
        val nCol = reader.lines().count().toInt() + 1
        val transposed = Array(nRow) {
            arrayOfNulls<String>(
                nCol
            )
        }
        reader.close()
        reader = BufferedReader(FileReader(file))
        var inputLine: String?
        for (j in 0 until nCol) {
            inputLine = reader.readLine()
            if (inputLine == null) throw IOException("Unexpected end of file.")
            val splitLine = inputLine.split("\t".toRegex()).toTypedArray()
            for (i in 0 until nRow) {
                transposed[i][j] = splitLine[i]
            }
        }
        reader.close()
        return Arrays.stream(transposed)
            .map { line: Array<String?> ->
                java.lang.String.join(
                    "\t",
                    *line
                )
            }
//                    .collect(Collectors.toList());
    }

    fun createSampleFastInputFile(inputFile: File = this.inputFile, outputFile: File = this.sampleFastInputFile) {
        outputFile.bufferedWriter().use { getTransposedFileStream(inputFile).forEach(it::appendLine) }
    }

    fun createSampleFastEncodedFile() {
        transposeEncodedFile(encodedFile, sampleFastEncodedFile)
    }

    fun createRandomRow(
        length: Int,
        alleles: Array<String>,
        ploidy: Int,
        elementSeparator: String,
        alleleSeparator: String
    ) = (1..length).joinToString(elementSeparator) { randomGenotype(ploidy, alleles, alleleSeparator) }

    fun randomGenotype(ploidy: Int, alleles: Array<String>, alleleSeparator: String) =
        (1..ploidy).joinToString(alleleSeparator) { alleles[random.nextInt(alleles.size)] }

    companion object {
        fun transposeEncodedFile(inputFile: File, outputFile: File) {
            var bytesPerLine = 0
            var elementsPerLine = 0
            var lines = 0
            inputFile.inputStream().buffered().use {
                it.iterator().forEachRemaining { byte ->
                    val int = byte.toInt()
                    if (lines != 0) {
                        if (int == 10) lines++
                    } else {
                        bytesPerLine++
                        if (int == 9) elementsPerLine++
                        if (int == 10) {
                            elementsPerLine++
                            lines++
                        }
                    }
                }
            }

            if (bytesPerLine % elementsPerLine != 0)
                throw Exception("Bytes per line ($bytesPerLine) is not divisible by elements per line ($elementsPerLine)")
            val bytesPerElement: Int = (bytesPerLine / elementsPerLine) - 1

            // initialize honking array
            val elements = Array(elementsPerLine) { Array(lines) { ByteArray(bytesPerElement) } }
            inputFile.inputStream().buffered().use { inputStream ->
                for (j in 0 until lines) {
                    for (i in 0 until elementsPerLine) {
                        inputStream.read(elements[i][j])
                        inputStream.read()
                    }
                }
            }
            outputFile.outputStream().buffered().use { outputStream ->
                elements.forEach { row ->
                    row.forEachIndexed { idx, bytes ->
                        if (idx > 0) outputStream.write(9)
                        outputStream.write(bytes)
                    }
                    outputStream.write(10)
                }
            }
        }

        private val alleles         by lazy {
            listOf("A", "C", "G", "T").run {
                flatMap { i -> flatMap { j -> flatMap { k -> listOf("$i$j$k", "$i$j${k}N") } } }
            }.dropLast(1)
        }
    }

}