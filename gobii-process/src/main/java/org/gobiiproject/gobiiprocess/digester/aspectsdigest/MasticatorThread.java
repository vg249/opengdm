package org.gobiiproject.gobiiprocess.digester.aspectsdigest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.gobii.masticator.AspectMapper;
import org.gobii.masticator.aspects.FileAspect;
import org.gobii.masticator.reader.ReaderResult;
import org.gobii.masticator.reader.TableReader;
import org.gobii.masticator.reader.result.End;
import org.gobii.masticator.reader.result.Val;
import org.gobiiproject.gobiimodel.config.GobiiException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MasticatorThread extends Thread {
    
    private File dataFile;

    private File outputFile;

    private FileAspect fileAspect;

    private String tableName;

    private int totalLinesWritten = 0;

    MasticatorThread(String tableName, 
                     FileAspect fileAspect,
                     File dataFile,
                     File outputFile) {
        this.tableName = tableName;
        this.dataFile = dataFile;
        this.outputFile = outputFile;
        this.fileAspect = fileAspect;
    }

    @Override
    public void run() {
                
        try (FileWriter fileWriter = new FileWriter(outputFile, true);
             BufferedWriter writer = new BufferedWriter(fileWriter);) {
                         
		    log.info("Masticating {}", tableName);

                TableReader reader = 
                    AspectMapper.map(fileAspect.getAspects().get(tableName)).build(dataFile);

                // Matrix file don't need header
                if(outputFile.length() == 0 && !tableName.equals("matrix")) {
                    writer.write(String.join(reader.getDelimiter(), reader.getHeader()) + "\n");
                }

                for (ReaderResult read = reader.read(); 
                    !(read instanceof End); read = reader.read()) {

		            if (read instanceof Val) {
		            	writer.write(read.value());
		            	writer.write('\n');
                        totalLinesWritten++;
		            }
		        }

		        writer.close();
        } catch (IOException e) {
            throw new GobiiException(
                String.format("IOException while processing {}", tableName),
                e);
        }

    }

    public int getTotalLinesWritten() {
        return this.totalLinesWritten;
    }

    public File getOutFile() {
        return this.outputFile;
    }
    public String getTableName() {
        return this.tableName;
    }

}
