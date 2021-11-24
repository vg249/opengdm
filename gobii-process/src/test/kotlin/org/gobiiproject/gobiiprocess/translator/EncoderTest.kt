package org.gobiiproject.gobiiprocess.translator

import org.junit.Assert
import org.junit.Test
import java.io.*
import kotlin.math.pow
import kotlin.system.measureTimeMillis
import kotlin.test.assertEquals

class EncoderTest {
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
        Encoder.encodeFile(tf.inputFile, tf.encodedFile, tf.lookupFile, "\t", "/")
        Encoder.decodeFile(tf.encodedFile, tf.decodedFile, tf.lookupFile, "\t", "/")
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
        Encoder.encodeFile(tf.inputFile, tf.encodedFile, tf.lookupFile, "\t", "/")
        Encoder.decodeFile(tf.encodedFile, tf.decodedFile, tf.lookupFile, "\t", "/")
        val encodedReader = tf.encodedFile.bufferedReader()
        val outReader = tf.decodedFile.bufferedReader()
        val line1 = outReader.readLine()
        val line2 = outReader.readLine()
        val line3 = outReader.readLine()
        Assert.assertEquals("A/A/A/A\tA/A/C/T", line1)
    }

    // Purely informational.
    data class Timing(val enc: Double, val dec: Double, val rows: Int, val cols: Int) {
        override fun toString(): String {
            return "%10d\t%10d\t%10.2f\t%10.2f".format(rows, cols, enc, dec)
        }
        companion object {
            val header = "%10s\t%10s\t%10s\t%10s".format("rows", "cols", "enc(ms)", "dec(ms)")
        }
    }

    fun timeEncoding(rows: Int, cols: Int, check: Boolean = false, reps: Int = 3, burn: Int = 1): Timing {
        val tf = TempFiles()
        tf.createRandomInputFile(rows, cols)
        val enc = (1..(reps + burn)).map {
            measureTimeMillis {
                Encoder.encodeFile(tf.inputFile, tf.encodedFile, tf.lookupFile, "\t", "/")
            }
        }.drop(burn).average()
        val dec = (1..(reps + burn)).map {
            measureTimeMillis {
                Encoder.decodeFile(tf.encodedFile, tf.decodedFile, tf.lookupFile, "\t", "/")
            }
        }.drop(burn).average()
        if (check) assertFilesAreEqual(tf.inputFile, tf.decodedFile)
        return Timing(enc, dec, rows, cols)
    }

    @Test
    fun testEncodingGrid() {
        testEncodingGrid(
            maxPow = 6,
            check = true,
            reps = 10,
            burn = 3
        )
    }

    @Test
    fun test10M() {
        println(Timing.header)
        println(timeEncoding(10000, 1000))
        println(timeEncoding(1000, 10000))
    }

    @Test
    fun test100M() {
        println(HDF5TranslatorTest.Timing.header)
        println(timeEncoding(20000, 5000, true, 1, 0))
        println(timeEncoding(10000, 10000, true, 1, 0))
    }

    fun testEncodingGrid(maxPow: Int = 6, check: Boolean = false, reps: Int = 3, burn: Int = 1) {
        println(Timing.header)
        for (i in 0..maxPow) {
            val rows = (10.0).pow(i).toInt()
            val cols = (10.0).pow(maxPow - i).toInt()
            val timing = timeEncoding(rows, cols, check, reps, burn)
            println(timing)
        }
    }

    fun assertFilesAreEqual(file1: File, file2: File) {
        val r1 = file1.bufferedReader()
        val r2 = file2.bufferedReader()
        r1.useLines { lines1 ->
            r2.useLines { lines2 ->
                lines1.zip(lines2).forEach { (a, b) -> assertEquals(a, b) }
            }
        }
    }

}