package org.gobii.masticator;

import org.gobii.masticator.reader.transform.compilers.Transformation;
import org.gobii.masticator.reader.transform.types.IUPACTransform;
import org.gobii.masticator.reader.transform.types.NucleotideSeparatorSplitter;

public class Transformers {

	private static IUPACTransform iupac = new IUPACTransform();
	private static NucleotideSeparatorSplitter fourLetter = new NucleotideSeparatorSplitter(4);
	private static NucleotideSeparatorSplitter twoLetter = new NucleotideSeparatorSplitter(2);
	@Transformation
	public String asdf(String value) {
		return "asdf";
	}
	@Transformation
	public String IUPAC2BI(String value){
			String retVal = iupac.process(value);
			if(retVal==null){
				throw new RuntimeException("Unable to process IUPAC transformation for value " + value);
			}
			return retVal;
	}
	@Transformation
	public String VCF2BI(String value){
		//Todo - need to run externally
		throw new RuntimeException("Unable to process VCF. Upstream failed to convert this request");
	}

	@Transformation
	public String FOURLETTERNUCLEOTIDE(String value){
		String retVal = fourLetter.process(value);
		if(retVal==null){
			throw new RuntimeException("Unable to process four letter nucleotide cleaning transformation for value " + value);
		}
		return retVal;
	}

	@Transformation
	public String TWOLETTERNUCLEOTIDE(String value){
		String retVal = twoLetter.process(value);
		if(retVal==null){
			throw new RuntimeException("Unable to process two letter nucleotide cleaning transformation for value " + value);
		}
		return retVal;
	}



}
