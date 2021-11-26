package org.gobiiproject.gobiiprocess.translator

import org.gobiiproject.gobiiprocess.translator.utils.TempFiles
import org.junit.Assert.*
import org.junit.Ignore
import org.junit.Test
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.pow
import kotlin.system.measureTimeMillis

class HDF5TranslatorTest {

    @Test
    fun testGSD125() {
        val inputFileSf = File("src/test/resources/gsd-125/biallelic-sample-fast.tsv")
        val inputFileMf = File("src/test/resources/gsd-125/biallelic-marker-fast.tsv")
        val tf = TempFiles()
        tf.inputFile
        System.err.println(tf)
        HDF5Translator.encodeFile(
            inputFile = inputFileMf,
            outputFile = tf.encodedFile,
            lookupFile = tf.lookupFile,
            genotypeSeparator = "\t",
            alleleSeparator = ""
        )
        HDF5Translator.decodeFile(
            inputFile = tf.encodedFile,
            outputFile = tf.decodedFile,
            lookupFile = tf.lookupFile,
            genotypeSeparator = "\t",
            alleleSeparator = ""
        )
        assertFilesAreEqual(inputFileMf, tf.decodedFile)
        assert(!tf.lookupFile.exists())
        tf.createSampleFastInputFile(inputFileMf, inputFileSf)
        tf.createSampleFastEncodedFile()
        HDF5Translator.decodeFile(
            inputFile = tf.sampleFastEncodedFile,
            outputFile = tf.decodedFile,
            lookupFile = tf.lookupFile,
            genotypeSeparator = "\t",
            alleleSeparator = "",
            markerFast = false
        )
        assertFilesAreEqual(inputFileSf, tf.decodedFile)
        assert(!tf.lookupFile.exists())
    }

    @Test
    fun testEncodeDecode() {
        val tf = TempFiles()
        try {
            FileWriter(tf.inputFile).use { inputwriter ->
                inputwriter.write("A/C\tG/T\tN/N")
                inputwriter.write(System.lineSeparator())
                inputwriter.write("ACAT/GAG\t/\tA/A")
                inputwriter.write(System.lineSeparator())
                inputwriter.write("A/C\tG/T\tN/N")
                inputwriter.write(System.lineSeparator())
                inputwriter.write("A/GAG\tG/GAGA\tA/C")
                inputwriter.write(System.lineSeparator())
                inputwriter.write("A/C\tG/T\tN/N")
                inputwriter.write(System.lineSeparator())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        HDF5Translator.encodeFile(tf.inputFile, tf.encodedFile, tf.lookupFile, "\t", "/")
        HDF5Translator.decodeFile(tf.encodedFile, tf.decodedFile, tf.lookupFile, "\t", "/")
        val encodedReader = tf.encodedFile.bufferedReader()
        val outReader = tf.decodedFile.bufferedReader()
        val line1 = outReader.readLine()
        val line2 = outReader.readLine()
        val line3 = outReader.readLine()
        val line4 = outReader.readLine()
        val line5 = outReader.readLine()
        val lookupReader = tf.lookupFile.bufferedReader()
        assertEquals("1\tIACAT;IGAG;D", lookupReader.readLine())
        assertEquals("3\tIGAG;IGAGA", lookupReader.readLine())
        assertEquals("AC\tGT\tNN", encodedReader.readLine())
        //assertEquals((char)128 + ""+(char)129+"\t"+"\t"+"AA",encodedReader.readLine());
        assertEquals("A/C\tG/T\tN/N", line1)
        //        assertEquals("ACAT/GAG\t/\tA/A",line2);//given there's no 'length' to the missing data, there is no separator
        assertEquals("ACAT/GAG\t/\tA/A", line2) //Should be a separator, no?
        assertEquals("A/C\tG/T\tN/N", line3)
        assertEquals("A/GAG\tG/GAGA\tA/C", line4)
        assertEquals("A/C\tG/T\tN/N", line5)
        encodedReader.close()
        outReader.close()
        lookupReader.close()
        assertFilesAreEqual(tf.inputFile, tf.decodedFile)
    }

    @Test
    fun testEncodeDecode4letRWExample() {
        val tf = TempFiles()
        try {
            FileWriter(tf.inputFile).use { inputwriter ->
                inputwriter.write("A/A/A/A\tA/A/C/T")
                inputwriter.write(System.lineSeparator())
                inputwriter.write("A/C/G/T\tUncallable")
                inputwriter.write(System.lineSeparator())
                inputwriter.write("T/T/T/T\tUNKNOWN")
                inputwriter.write(System.lineSeparator())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        HDF5Translator.encodeFile(tf.inputFile, tf.encodedFile, tf.lookupFile, "\t", "/")
        HDF5Translator.decodeFile(tf.encodedFile, tf.decodedFile, tf.lookupFile, "\t", "/")
        val encodedReader = tf.encodedFile.bufferedReader()
        val outReader = tf.decodedFile.bufferedReader()
        val line1 = outReader.readLine()
        val line2 = outReader.readLine()
        val line3 = outReader.readLine()
        assertEquals("A/A/A/A\tA/A/C/T", line1)
        assertFilesAreEqual(tf.inputFile, tf.decodedFile)
    }

    @Test
    fun testWeirdAlleles() {
        val tf = TempFiles()
        val testLines = listOf(
            "A/A\t01230123\t00000000\tC/T",     //TODO: why are SSRs required to be encoded this way?
            "A/C\t99999999\tG/C\tT/T",
            "A/A\t10091099\tG/G\t09100910",
            "./.\t+/-\t /?"
        )

        val outputLines = sequenceOf(
            "A/A\t0123/0123\t0000/0000\tC/T",   //TODO: seems weird that SSRs are meant to decode to a different format
            "A/C\t9999/9999\tG/C\tT/T",
            "A/A\t1009/1099\tG/G\t0910/0910",
            "./.\t+/-\t /?"
        )

        tf.inputFile.bufferedWriter().use { testLines.forEach(it::appendLine) }
        HDF5Translator.encodeFile(tf.inputFile, tf.encodedFile, tf.lookupFile, "\t", "/")
        HDF5Translator.decodeFile(tf.encodedFile, tf.decodedFile, tf.lookupFile, "\t", "/")
//        assertFilesAreEqual(tf.inputFile, tf.decodedFile)
        tf.decodedFile.useLines { decoded ->
            decoded.zip(outputLines).forEach { (a, b) -> assertEquals(a, b) }
        }
    }

    @Test
    fun testEncodeDecodeRandom() {
        val tf = TempFiles()
        tf.createRandomInputFile(100, 100)
        HDF5Translator.encodeFile(
            inputFile        = tf.inputFile,
            outputFile       = tf.encodedFile,
            lookupFile       = tf.lookupFile,
            genotypeSeparator = "\t",
            alleleSeparator  = "/"
        )
        HDF5Translator.decodeFile(
            inputFile        = tf.encodedFile,
            outputFile       = tf.decodedFile,
            lookupFile       = tf.lookupFile,
            genotypeSeparator = "\t",
            alleleSeparator  = "/"
        )
        tf.inputFile.bufferedReader().useLines { f1 ->
            tf.decodedFile.bufferedReader().useLines { f2 ->
                (f1 zip f2).forEach { (a, b) ->
                    assertEquals(a, b)
                }
            }
        }
    }

    @Test
    fun testSampleFastDecode() {
        val tf = TempFiles()
        tf.createRandomInputFile(100, 100)
        HDF5Translator.encodeFile(
            inputFile        = tf.inputFile,
            outputFile       = tf.encodedFile,
            lookupFile       = tf.lookupFile,
            genotypeSeparator = "\t",
            alleleSeparator  = "/"
        )

        transposeEncodedFile(tf.encodedFile, tf.sampleFastEncodedFile)
        tf.createSampleFastInputFile()      // transpose input file for comparison
        HDF5Translator.decodeFile(
            inputFile        = tf.sampleFastEncodedFile,
            outputFile       = tf.decodedFile,
            lookupFile       = tf.lookupFile,
            genotypeSeparator = "\t",
            alleleSeparator  = "/",
            markerFast       = false
        )
        tf.sampleFastInputFile.bufferedReader().useLines { f1 ->
            tf.decodedFile.bufferedReader().useLines { f2 ->
                (f1 zip f2).forEach { (a, b) ->
                    assertEquals(a, b)
                }
            }
        }
    }

    @Test
    fun testTooManyAlleles() {
        val tf = TempFiles()

        val row = arrayOf("A", "C", "G", "T").run {
            flatMap { i -> flatMap { j -> flatMap { k -> listOf("$i$j$k", "N$i$j$k") } } }
        }.joinToString("\t")

        tf.inputFile.bufferedWriter().use { it.appendLine(row) }
        try {
            HDF5Translator.encodeFile(
                inputFile        = tf.inputFile,
                outputFile       = tf.encodedFile,
                lookupFile       = tf.lookupFile,
                genotypeSeparator = "\t",
                alleleSeparator  = "/"
            )
        } catch (e: Exception) {
            return
        }
        fail("No exception was thrown.")
    }

    @Test
    fun testNoEncodings() {
        val tf = TempFiles()
        tf.createRandomInputFile(rows = 10, cols = 10, ploidy = 2, alleles = arrayOf("A", "C", "T", "G"))
        HDF5Translator.encodeFile(
            inputFile        = tf.inputFile,
            outputFile       = tf.encodedFile,
            lookupFile       = tf.lookupFile,
            genotypeSeparator = "\t",
            alleleSeparator  = "/"
        )
        HDF5Translator.decodeFile(
            inputFile        = tf.encodedFile,
            outputFile       = tf.decodedFile,
            lookupFile       = tf.lookupFile,
            genotypeSeparator = "\t",
            alleleSeparator  = "/"
        )
        assertFilesAreEqual(tf.inputFile, tf.decodedFile)
    }
    
    @Test
    @Ignore
    fun test10M() {
        println(Timing.header)
        println(timeEncoding(10000, 1000, check = true))
        println(timeEncoding(1000, 10000, check = true))
    }

    @Test
    @Ignore
    fun test100M() {
        println(Timing.header)
        println(timeEncoding(20000, 5000, check = true, reps = 1, burn = 0))
        println(timeEncoding(10000, 10000, true, reps = 1, burn = 0))
    }

    @Test
    @Ignore
    fun test1B() {
//        val output = Timing.header
        timeEncoding(200_000, 5_000, check = true, reps = 1, burn = 0, verbose = true)
            .also { println(Timing.header) }
            .let(::println)
    }

    @Test
    @Ignore
    fun testWorstCase() {
        /*
        tests a potential worst-case runtime scenario:
        sample-fast
        pool of 127 encoded alleles
        all alleles are encoded
        markerList includes every line
        100M data points
        */

        fun timeWorstCase(numMarkers: Int, numSamples: Int, ploidy: Int): Timing {
            val markerList = (0 until numMarkers).toSet()
            val tf = TempFiles()
            tf.worstCaseInputFile(
                numMarkers = numMarkers,
                numSamples = numSamples,
                ploidy     = ploidy
            )
            val enc = measureTimeMillis {
                HDF5Translator.encodeFile(
                    inputFile         = tf.inputFile,
                    outputFile        = tf.encodedFile,
                    lookupFile        = tf.lookupFile,
                    genotypeSeparator = "\t",
                    alleleSeparator   = "/"
                )
            }
            tf.createSampleFastEncodedFile()
            val dec = measureTimeMillis {
                HDF5Translator.decodeFile(
                    inputFile         = tf.sampleFastEncodedFile,
                    outputFile        = tf.decodedFile,
                    lookupFile        = tf.lookupFile,
                    genotypeSeparator = "\t",
                    alleleSeparator   = "/",
                    markerFast        = false,
                    markerList        = markerList
                )
            }
            val timing = Timing(
                enc = enc,
                dec = dec,
                rows = numSamples,
                cols = numMarkers,
                markerFast = false,
                filterSize = numMarkers,
                ploidy = ploidy
            )
            assertFilesAreEqual(tf.sampleFastInputFile, tf.decodedFile)
            return timing
        }

        println(Timing.header)
        println(timeWorstCase(400, 250, 2))
        println(timeWorstCase(4_000, 250, 2))
        println(timeWorstCase(400, 2_500, 2))
        println(timeWorstCase(4_000, 2_500, 2))
        println(timeWorstCase(40_000, 2_500, 2))
        println(timeWorstCase(20_000, 5_000, 2))
        println(timeWorstCase(10_000, 10_000, 2))

    }

    data class Timing(
        val enc:        Double,
        val dec:        Double,
        val rows:       Int,
        val cols:       Int,
        val markerFast: Boolean = true,
        val filterSize: Int = 0,
        val ploidy:     Int = 2
    ) {
        constructor(
            enc:        Long,
            dec:        Long,
            rows:       Int,
            cols:       Int,
            markerFast: Boolean = true,
            filterSize: Int     = 0,
            ploidy:     Int     = 2
        ) : this(
            enc        = enc.toDouble(),
            dec        = dec.toDouble(),
            rows       = rows,
            cols       = cols,
            markerFast = markerFast,
            filterSize = filterSize,
            ploidy     = ploidy
        )

        val stringRep = "%12d\t%12d\t%12d\t%12s\t%12d\t%12.2f\t%12.2f"
            .format(
                if (markerFast) rows else cols,
                if (markerFast) cols else rows,
                rows * cols,
                if (markerFast) "marker" else "sample",
                filterSize,
                enc,
                dec
            )

        override fun toString() = stringRep
        companion object {
            val header = "%12s\t%12s\t%12s\t%12s\t%12s\t%12s\t%12s"
                .format("markers", "samples", "datapoints", "orientation", "filtered", "enc(ms)", "dec(ms)")
        }
    }

    private fun timeEncoding(
        rows:       Int,
        cols:       Int,
        markerFast: Boolean     = true,
        markerList: Set<Int>?   = null,
        check:      Boolean     = false,
        reps:       Int         = 3,
        burn:       Int         = 1,
        verbose:    Boolean     = false,
    ): Timing {
        val tf = TempFiles()
        val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        if (verbose) System.err.println("[${LocalDateTime.now().format(dtf)}] Creating input file.")
        tf.createRandomInputFile(rows, cols)
        if (verbose) System.err.println("[${LocalDateTime.now().format(dtf)}] Encoding.")
        val enc = (1..(reps + burn)).map {
            measureTimeMillis {
                HDF5Translator.encodeFile(
                    inputFile        = tf.inputFile,
                    outputFile       = tf.encodedFile,
                    lookupFile       = tf.lookupFile,
                    genotypeSeparator = "\t",
                    alleleSeparator  = "/"
                )
            }
        }.drop(burn).average()
        if (verbose) System.err.println("[${LocalDateTime.now().format(dtf)}] Decoding.")
        val dec = (1..(reps + burn)).map {
            measureTimeMillis {
                HDF5Translator.decodeFile(
                    inputFile        = tf.encodedFile,
                    outputFile       = tf.decodedFile,
                    lookupFile       = tf.lookupFile,
                    genotypeSeparator = "\t",
                    alleleSeparator  = "/",
                    markerFast       = markerFast,
                    markerList       = markerList
                )
            }
        }.drop(burn).average()
        if (verbose) System.err.println("[${LocalDateTime.now().format(dtf)}] Checking.")
        if (check) assertFilesAreEqual(tf.inputFile, tf.decodedFile)
        if (verbose) System.err.println("[${LocalDateTime.now().format(dtf)}] Done.")
        return Timing(enc, dec, rows, cols)
    }

    private fun testEncodingGrid() {
        testEncodingGrid(
            maxPow     = 6,
            check      = true,
            reps       = 9,
            burn       = 3,
            markerFast = true,
            markerList = null
        )
    }

    private fun testEncodingGrid(
        maxPow:     Int     = 6,
        check:      Boolean = false,
        reps:       Int,
        burn:       Int,
        markerFast: Boolean,
        markerList: Set<Int>?
    ) {
        println(Timing.header)
        for (i in 0..maxPow) {
            val rows = (10.0).pow(i).toInt()
            val cols = (10.0).pow(maxPow - i).toInt()
            val timing = timeEncoding(
                rows       = rows,
                cols       = cols,
                check      = check,
                reps       = reps,
                burn       = burn,
                markerFast = markerFast,
                markerList = markerList
            )
            println(timing)
        }
    }

    companion object {
        fun assertFilesAreEqual(file1: File, file2: File) {
            val r1 = file1.bufferedReader()
            val r2 = file2.bufferedReader()
            r1.useLines { lines1 ->
                r2.useLines { lines2 ->
                    lines1.zip(lines2).forEach { (a, b) -> assertEquals(a, b) }
                }
            }
        }

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