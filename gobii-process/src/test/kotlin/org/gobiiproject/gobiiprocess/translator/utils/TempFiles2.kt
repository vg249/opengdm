package org.gobiiproject.gobiiprocess.translator.utils

import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Stream

class TempFiles2(verbose: Boolean = false) {
    val inputFile:             File = createTempFile("input-mf")
    val lookupFile:            File = createTempFile("lookup")
    val encodedFile:           File = createTempFile("encoded-mf")
    val decodedFile:           File = createTempFile("decoded")
    val sampleFastEncodedFile: File = createTempFile("encoded-sf")
    val sampleFastInputFile:   File = createTempFile("input-sf")
    private val timestamp:     String =
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd-HHmmssnnnnnn"))
    private val alleles: List<String> by lazy {
        listOf("A", "C", "G", "T").run {
            flatMap { i -> flatMap { j -> flatMap { k -> listOf("$i$j$k", "$i$j${k}N") } } }
        }
            .dropLast(1)
    }

    init {
        if (verbose) {
            System.err.println(
                """
                Input file: $inputFile
                Input file (sf): $sampleFastInputFile
                Lookup file: $lookupFile
                Encoded file: $encodedFile
                Encoded file (sf): $sampleFastEncodedFile
                Decoded file: $decodedFile
                """.trimIndent()
            )
        }
    }

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

    private fun createTempFile(prefix: String): File {
        val tmpDir = System.getProperty("java.io.tmpdir")
        val path = tmpDir + "/" + timestamp + "_" + prefix + ".tmp"
        return File(path)
    }

    fun createLargeInputFile(rows: Int, cols: Int) {
        val writer = BufferedWriter(FileWriter(inputFile))
        var j = 0
        var i = 0
        var sameLine: String? = null
        while (i <= rows) {
            if (sameLine == null) {
                val lineJoiner = StringJoiner("\t")
                while (j <= cols) {
                    lineJoiner.add("A/A")
                    lineJoiner.add("ACGT/ACGT")
                    lineJoiner.add("ACGTA/ACG")
                    lineJoiner.add("TTTT/T")
                    j += 4
                }
                sameLine = lineJoiner.toString()
            }
            writer.append(sameLine)
            writer.append(System.lineSeparator())
            i++
        }
        writer.close()
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

    fun getTransposedFileStream(file: File?): Stream<String> {
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

    fun createSampleFastInputFile() {
        val writer = BufferedWriter(FileWriter(sampleFastInputFile))
        getTransposedFileStream(inputFile).forEach { line: String? ->
            try {
                writer.append(line)
                writer.append(System.lineSeparator())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        writer.close()
    }

    fun createSampleFastEncodedFile() {
        transposeEncodedFile(encodedFile, sampleFastEncodedFile)
    }

    fun createRandomRow(
        length: Int,
        alleles: Array<String>,
        ploidy: Int,
        elementSeparator: String?,
        alleleSeparator: String?
    ): String {
        val row = StringJoiner(elementSeparator)
        val random = Random()
        val n = alleles.size
        for (i in 0 until length) {
            val gt = StringJoiner(alleleSeparator)
            for (j in 0 until ploidy) {
                gt.add(alleles[random.nextInt(n)])
            }
            row.merge(gt)
        }
        return row.toString()
    }

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
    }

}