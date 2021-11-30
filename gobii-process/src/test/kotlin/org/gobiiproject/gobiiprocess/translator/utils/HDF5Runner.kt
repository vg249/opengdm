package org.gobiiproject.gobiiprocess.translator.utils

import java.io.File
import java.io.FileNotFoundException

class HDF5Runner(private val bin: String) {

    private val binMap = mapOf(
        "HDF5info"         to "$bin/HDF5info",
        "dumpdataset"      to "$bin/dumpdataset",
        "fetchmarker"      to "$bin/fetchmarker",
        "fetchmarkerlist"  to "$bin/fetchmarkerlist",
        "fetchpoints"      to "$bin/fetchpoints",
        "fetchsample"      to "$bin/fetchsample",
        "fetchsamplelist"  to "$bin/fetchsamplelist",
        "loadHDF5"         to "$bin/loadHDF5",
        "loadHDF5_chunked" to "$bin/loadHDF5_chunked"
    )

    private val HDF5info         by binMap
    private val dumpdataset      by binMap
    private val fetchmarker      by binMap
    private val fetchmarkerlist  by binMap
    private val fetchpoints      by binMap
    private val fetchsample      by binMap
    private val fetchsamplelist  by binMap
    private val loadHDF5         by binMap
    private val loadHDF5_chunked by binMap

    fun dumpDataset(markerFast: Boolean, hdf5: String, output: String) =
        runProcess(dumpdataset, orientation(markerFast), hdf5, output)
    fun loadHDF5(datasize: Int, inputFile: String, hdf5File: String) =
        runProcess(loadHDF5, "$datasize", inputFile, hdf5File)
    fun loadHDF5Chunked(datasize: Int, inputFile: String, hdf5File: String) =
        runProcess(loadHDF5_chunked, "$datasize", inputFile, hdf5File)
    fun hdf5Info(hdf5File: String) = runProcess(HDF5info, hdf5File)
    fun fetchmarker(hdf5File: String, markerNumber: Int, outputFile: String) =
        runProcess(fetchmarker, hdf5File, "$markerNumber", outputFile)
    fun fetchmarkerlist(hdf5File: String, markerFast: Boolean, markerFile: String, outputFile: String) =
        runProcess(fetchmarkerlist, orientation(markerFast), hdf5File, markerFile, outputFile)
    fun fetchpoints(hdf5File: String, outputFile: String, vararg points: Pair<Int, Int>) =
        points.flatMap { (x, y) -> listOf("$x", "$y") }.toTypedArray().let { runProcess(fetchpoints, hdf5File, outputFile, *it) }
    fun fetchsample(hdf5File: String, sampleNumber: Int, outputFile: String) =
        runProcess(fetchsample, hdf5File, "$sampleNumber", outputFile)
    fun fetchsamplelist(hdf5File: String, sampleFile: String, outputFile: String) =
        runProcess(fetchsamplelist, hdf5File, sampleFile, outputFile)

    init {
        File(bin).run {
            if (!exists()) throw FileNotFoundException(bin)
            binMap.values.map(::File)
                .forEach {
                    it.run {
                        if (!exists()) throw FileNotFoundException("$it")
                        if (!canExecute()) throw AccessDeniedException(this)
                    }
            }
        }
    }

    companion object {

        fun orientation(markerFast: Boolean) = if (markerFast) "markers-fast" else "samples-fast"

        fun runProcess(vararg commandLine: String) {
            val process = Runtime.getRuntime().exec(commandLine)
                .also { it.errorStream.transferTo(System.err) }
                .also { it.inputStream.transferTo(System.out) }
            val exitValue = process.waitFor()
            if (exitValue != 0) throw Exception("Process ${commandLine.joinToString(" ")} returned non-zero exit value: $exitValue")
        }


        fun setup(hdf5Dir: String) {
            cloneRepo(hdf5Dir)
            compile(hdf5Dir)
        }

        fun cloneRepo(hdf5Dir: String) = cloneRepo(File(hdf5Dir))
        fun cloneRepo(hdf5dir: File) {
            if (!hdf5dir.exists()) {
                runProcess("git", "clone", "--single-branch", "--branch", "standalone_develop",
                    "git@bitbucket.org:gobiiproject/gobii.hdf5.git", "$hdf5dir")
            }
            File("$hdf5dir/production/bin").listFiles()?.forEach(File::delete)
        }

        fun compile(hdf5dir: String) = compile(File(hdf5dir))
        fun compile(hdf5dir: File) {
            val sourceDir = File("$hdf5dir/production")
            val sourceFiles = sourceDir.listFiles { it -> it.extension == "c" }
            if (sourceFiles.isNullOrEmpty()) {
                throw Exception("HDF5 source files not found: $sourceDir")
            }
            sourceFiles.forEach {
                runProcess("h5cc", "-o", "$sourceDir/bin/${it.nameWithoutExtension}", "-O", "$it")
            }
        }

    }

}