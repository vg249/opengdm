package org.gobiiproject.gobiiprocess.vcfinterface;
import htsjdk.tribble.AbstractFeatureReader;
import htsjdk.tribble.CloseableTribbleIterator;
import htsjdk.tribble.FeatureCodec;
import htsjdk.tribble.TribbleException;
import htsjdk.tribble.example.CountRecords;
import htsjdk.tribble.index.Index;
import htsjdk.tribble.readers.LineIterator;
import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.Genotype;
import htsjdk.variant.variantcontext.GenotypesContext;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFCodec;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class HTSInterface {
    public static void main(String[] args) throws IOException {
        String filetoread="C:\\Users\\jdl232.RS-BTOM1YJDL232\\Downloads\\" +"notareal.vcf";
        if(args.length > 0){
            filetoread=args[0];
        }
        File f = new File( filetoread);
        getAllVariantsFromFile(f);
    }
    static void getAllVariantsFromFile(File f) throws IOException {
        FeatureCodec<VariantContext, LineIterator> codec = new VCFCodec();
        Index index = CountRecords.loadIndex(f, codec);
       AbstractFeatureReader<VariantContext, LineIterator> reader = AbstractFeatureReader.getFeatureReader(f.getAbsolutePath(),codec, index);
       CloseableTribbleIterator<VariantContext> ctr = reader.iterator();
       for(VariantContext vc:ctr){
           System.out.print(vc.getID() + "\t");
           System.out.print(vc.getReference() + "|" + Arrays.deepToString(vc.getAlternateAlleles().toArray()) + "\t");
            for(Genotype gc:vc.getGenotypes()) {
                boolean innerfirst=true;
                for (Allele a : gc.getAlleles()) {
                    if(innerfirst)innerfirst=false;
                    else System.out.print("\\");
                    System.out.print(a.getBaseString());
                }
                System.out.print("\t");
            }
           System.out.println();
       }

    }

    static List<String> getRowFromVariantContext(VariantContext vc)
    static void writeVariantOnlyFile(File inFile, File outFile) throws Exception{
        writeVariantOnlyFile(inFile,outFile,"/","\t");
    }

   public static void writeVariantOnlyFile(File inFile, File outFile,String variantSeparator, String segmentSeparator) throws IOException, TribbleException {
        FeatureCodec<VariantContext, LineIterator> codec = new VCFCodec();
        Index index = CountRecords.loadIndex(inFile, codec);
        AbstractFeatureReader<VariantContext, LineIterator> reader = AbstractFeatureReader.getFeatureReader(inFile.getAbsolutePath(),codec, index);
        CloseableTribbleIterator<VariantContext> ctr = reader.iterator();
        FileOutputStream fos = new FileOutputStream(outFile,false);//Append=false, clobber existing file
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        PrintWriter printWriter= new PrintWriter(bos);
        boolean firstLine=true;
        for(VariantContext vc:ctr){
            if(firstLine){
                firstLine=false;
            }
            else{
                printWriter.println();
            }
            boolean lineFirst=true;
            for(Genotype gc:vc.getGenotypes()) {
                if(lineFirst){
                    lineFirst=false;
                }
                else{
                    printWriter.print(segmentSeparator);
                }
                printWriter.print(formattedAlleleString(gc,variantSeparator));
            }
        }
        printWriter.flush();
        printWriter.close();
    }


    /**
     * Returns a string representation of the alleles with separators, separated by the 'variantseparator'
     * Example: If the genotype has four alleles, ACGT  A  A  . , and the separator is '/', the resulting
     * string would be ACGT/A/A/.
     * @param variantSeparator Separator between genotypes
     * @param gc Genotype object to convert alleles from
     */
    private static String formattedAlleleString(Genotype gc,String variantSeparator) {
      return gc.getAlleles().stream()
              .map(Allele::getBaseString)
              .collect(Collectors.joining(variantSeparator));
    }


    /**
     * Convenience method for converting an entire variant context into formattedAlleleStringLists.
     * Useful to pretend to be the 'input row' of a CSVFileReader read.
     * @param vc VariantContext HTSJDK object
     * @param variantSeparator separator used by the formattedAlleleString() call
     * @return List of formatted allele strings representing an entire variant context 'row'
     */
    private static List<String> formattedAlleleStringList(VariantContext vc,String variantSeparator){
        return vc.getGenotypes().stream().map(x -> formattedAlleleString(x,variantSeparator)).collect(Collectors.toList());
    }


    static void attemptToReadFile(File inFile) throws IOException {
        try {
            FeatureCodec<VariantContext, LineIterator> codec = new VCFCodec();
            Index index = CountRecords.loadIndex(inFile, codec);
            AbstractFeatureReader.getFeatureReader(inFile.getAbsolutePath(), codec, index);
        }catch(Exception e){
            System.out.println("Error found while parsing:  " + e.getMessage());
        }
        System.out.println("Input file seems to be in order");
    }
}

class HTSRowReader{
    List<String> nonstandardAlleles = new ArrayList<String>();
    HashSet<String> nonstandardAlleleMap = new HashSet<String>();
    HashSet<String> unencodedAlleles = new HashSet<String>(Arrays.asList("A","C","G","T"));

    HTSRowReader(VariantContext variantContext){
        variantContext.getAlleles().stream();
    }

    HTSRowReader(String markerRow){
        decode(markerRow);
    }

    //decodes a string encoded with 'encode'
    void decode(String markerRow){
        String namelessRow = markerRow.substring(markerRow.indexOf('\t'));//remove everything before first tab
        String[] segments= namelessRow.split(";",-1);
        for(int i = 0; i < segments.length; i++){
            String cleanSegment = cleanSegment(segments[i]);
            orderNonstandardAllele(cleanSegment);
        }
    }

    //returns any insertion segment
    static String cleanSegment(String segment){
        switch(segment.charAt(0)){
            case 'I':
                if(segment.length()>1) {
                    return segment.substring(1);
                }
                else{
                    return null;
                }
                break;
            case 'D':{
                return "";
            }
            break;
            default:return null;
        }
    }

    //Encodes the current state of the rowreader into a 'marker row' for data encoded in this marker
    String encode(){
        //Every nonstandard allele is prepended with an "I" for futureproofing
        return nonstandardAlleles.stream().map(s->"I"+s).collect(Collectors.joining(";I"));
    }

    //Add a cannonical number to a novel base string
    void orderNonstandardAllele(Allele a){
        orderNonstandardAllele(a.getBaseString());
    }
    void orderNonstandardAllele(String baseString){
        if(unencodedAlleles.contains(baseString) || nonstandardAlleleMap.contains(baseString)){
            return;
        }
        nonstandardAlleles.add(baseString);
        nonstandardAlleleMap.add(baseString);
    }

    String getEncodedValue(Allele)


}
