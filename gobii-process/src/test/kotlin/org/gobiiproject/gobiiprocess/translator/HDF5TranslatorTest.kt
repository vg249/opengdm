package org.gobiiproject.gobiiprocess.translator

import junit.framework.TestCase
import org.gobiiproject.gobiiprocess.translator.utils.TempFiles2
import org.junit.Assert
import java.io.File
import java.io.FileWriter
import java.io.IOException
import kotlin.math.pow
import kotlin.system.measureTimeMillis

class HDF5TranslatorTest : TestCase() {

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
        Assert.assertEquals("1\tIACAT;IGAG;D", lookupReader.readLine())
        Assert.assertEquals("3\tIGAG;IGAGA", lookupReader.readLine())
        Assert.assertEquals("AC\tGT\tNN", encodedReader.readLine())
        //Assert.assertEquals((char)128 + ""+(char)129+"\t"+"\t"+"AA",encodedReader.readLine());
        Assert.assertEquals("A/C\tG/T\tN/N", line1)
        //        Assert.assertEquals("ACAT/GAG\t/\tA/A",line2);//given there's no 'length' to the missing data, there is no separator
        Assert.assertEquals("ACAT/GAG\t/\tA/A", line2) //Should be a separator, no?
        Assert.assertEquals("A/C\tG/T\tN/N", line3)
        Assert.assertEquals("A/GAG\tG/GAGA\tA/C", line4)
        Assert.assertEquals("A/C\tG/T\tN/N", line5)
        encodedReader.close()
        outReader.close()
        lookupReader.close()
        assertFilesAreEqual(tf.inputFile, tf.decodedFile)
    }

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
        Assert.assertEquals("A/A/A/A\tA/A/C/T", line1)
        assertFilesAreEqual(tf.inputFile, tf.decodedFile)
    }

    fun testEncodeDecodeFile() {
        val tf = TempFiles()
        tf.createRandomInputFile(1, 1000000)
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

    fun testWorstCase() {
        // tests a potential worst-case runtime scenario:
        //      sample-fast
        //      pool of 127 encoded alleles
        //      all alleles are encoded
        //      markerList includes every line
        //      100M data points

        fun timeWorstCase(numMarkers: Int, numSamples: Int, ploidy: Int): Timing {
            val markerList = (0 until numMarkers).toSet()
            val tf = TempFiles2()
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
        markerFast: Boolean   = true,
        markerList: Set<Int>? = null,
        check:      Boolean   = false,
        reps:       Int       = 3,
        burn:       Int       = 1
    ): Timing {
        val tf = TempFiles()
        tf.createRandomInputFile(rows, cols)
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
        if (check) assertFilesAreEqual(tf.inputFile, tf.decodedFile)
        return Timing(enc, dec, rows, cols)
    }

    fun testEncodingGrid() {
        testEncodingGrid(
            maxPow     = 7,
            check      = true,
            reps       = 3,
            burn       = 2,
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

    fun testEncodeFile() {
        val tf = TempFiles()
    }

    fun testDecodeFile() {
        val tf = TempFiles()
    }

    fun test10M() {
        println(Timing.header)
        println(timeEncoding(10000, 1000))
        println(timeEncoding(1000, 10000))
    }

    fun test100M() {
        println(Timing.header)
        println(timeEncoding(20000, 5000, check = true, reps = 1, burn = 0))
        println(timeEncoding(10000, 10000, true, reps = 1, burn = 0))
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