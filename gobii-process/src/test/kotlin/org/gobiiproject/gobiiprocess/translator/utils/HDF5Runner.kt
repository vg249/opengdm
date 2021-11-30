package org.gobiiproject.gobiiprocess.translator.utils

import java.io.File

class HDF5Runner(private val bin: String) {

    fun dumpDataset(markerFast: Boolean, hdf5: String, output: String): Int {
        val cmd = arrayOf("$bin/dumpdataset", if (markerFast) "markers-fast" else "samples-fast", hdf5, output)
        return exec(cmd)
    }

    fun loadHDF5(datasize: Int, inputFile: String, hdf5File: String): Int {
        val cmd = arrayOf("$bin/loadHDF5", "$datasize", inputFile, hdf5File)
        return exec(cmd)
    }

    init {
        File(bin).run {
            assert(exists())
            assert(isDirectory)
            assert(canExecute())
        }
    }

    companion object {
        fun exec(args: Array<String>): Int {
            System.err.println(args.toList())
            val process = Runtime.getRuntime().exec(args)
            val retVal = process.waitFor()
            System.err.println(retVal)
            return retVal
        }
    }

}