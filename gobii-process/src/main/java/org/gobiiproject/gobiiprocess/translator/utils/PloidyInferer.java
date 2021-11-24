package org.gobiiproject.gobiiprocess.translator.utils;

import java.io.*;

public final class PloidyInferer {

    public int inferFromTempGeno(File file) throws IOException {
        return inferFromTempGeno(file, 10);
    }

    public int inferFromTempGeno(File file, int n) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        int lineCount = 0;
        int max = -1;
        int maxCount = 0;
        while ((line = br.readLine()) != null && lineCount++ < n) {
            for (String gt: line.split("\t", -1)) {
                for (String al: gt.split("/", -1)) {
                    int l = al.length();
                    if (l > max) {
                        max = l;
                        maxCount = 1;
                    } else if (l == max) {
                        maxCount++;
                    }
                }
            }
        }
        return max;
    }
}
