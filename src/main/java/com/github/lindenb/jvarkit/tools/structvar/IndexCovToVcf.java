/*
The MIT License (MIT)

Copyright (c) 2018 Pierre Lindenbaum

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

*/
package com.github.lindenb.jvarkit.tools.structvar;
import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.beust.jcommander.Parameter;
import com.github.lindenb.jvarkit.lang.JvarkitException;
import com.github.lindenb.jvarkit.util.jcommander.Launcher;
import com.github.lindenb.jvarkit.util.jcommander.Program;
import com.github.lindenb.jvarkit.util.log.Logger;

import htsjdk.samtools.util.CloserUtil;
import htsjdk.samtools.util.StringUtil;
import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.Genotype;
import htsjdk.variant.variantcontext.GenotypeBuilder;
import htsjdk.variant.variantcontext.VariantContextBuilder;
import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.vcf.VCFConstants;
import htsjdk.variant.vcf.VCFFilterHeaderLine;
import htsjdk.variant.vcf.VCFFormatHeaderLine;
import htsjdk.variant.vcf.VCFHeader;
import htsjdk.variant.vcf.VCFHeaderLine;
import htsjdk.variant.vcf.VCFHeaderLineType;
import htsjdk.variant.vcf.VCFInfoHeaderLine;
import htsjdk.variant.vcf.VCFStandardHeaderLines;
/**
BEGIN_DOC

## Input

input is a tab-delimited file created by e.g: indexcov (https://github.com/brentp/goleft/tree/master/indexcov)

```
#chrom  start  end     SampleBB  SampleBC  SampleBD  SampleBE  SampleBF  SampleBG  SampleBH
chr1    23778  40778   1.59      1.31      1.67      1.61      1.83      1.52      1.48
chr1    29106  46106   1.9       1.54      1.72      1.97      1.88      1.53      1.95
chr1    84581  101581  0.764     0.841     1.2       1.16      1.18      1.13      1.23
chr1    15220  32220   0.355     0.704     1.09      0.784     0.81      1.37      0.954
chr1    58553  75553   0.353     0.436     0.912     0.836     1.16      1.09      0.611
chr1    19347  36347   0.381     0.411     0.811     0.795     1.16      1.22      0.495
chr1    81062  98062   1.09      0.972     1.35      1.22      1.66      1.76      1.1
chr1    17353  34353   1.06      1.06      1.23      1.26      1.44      1.43      1.03
chr1    48498  65498   1.08      0.996     1.28      1.44      1.52      1.57      1.05
```

output:

```
##fileformat=VCFv4.2
##FILTER=<ID=ALL_DEL,Description="number of samples >1 and all are deletions">
##FILTER=<ID=ALL_DUP,Description="number of samples >1 and all are duplication">
##FILTER=<ID=NO_SV,Description="There is no DUP or DEL in this variant">
##FORMAT=<ID=DEL,Number=1,Type=Integer,Description="set to 1 if relative number of copy <= 0.6">
##FORMAT=<ID=DUP,Number=1,Type=Integer,Description="set to 1 if relative number of copy >= 1.9">
##FORMAT=<ID=F,Number=1,Type=Float,Description="Relative number of copy: 0.5 deletion 1 normal 2.0 duplication">
##FORMAT=<ID=GT,Number=1,Type=String,Description="Genotype">
##INFO=<ID=END,Number=1,Type=Integer,Description="Stop position of the interval">
##INFO=<ID=NDEL,Number=1,Type=Integer,Description="Number of samples being deleted">
##INFO=<ID=NDUP,Number=1,Type=Integer,Description="Number of samples being duplicated">
#CHROM	POS	ID	REF	ALT	QUAL	FILTER	INFO	FORMAT	SampleBB	SampleBC	SampleBD	SampleBE	SampleBF	(...)
chr1	0	.	N	<DUP>	.	.	END=16384;NDEL=0;NDUP=8	GT:DUP:F	0:0:1.59	0:0:1.31	0:0:1.67	0:0:1.61	0:0:1.83 (...)
```



END_DOC
 */
@Program(
		name="indexcov2vcf",
		description="convert indexcov data to vcf",
		keywords={"cnv","jfx","duplication","deletion","sv"}
		)
public class IndexCovToVcf extends Launcher {
	private static final Logger LOG = Logger.build(IndexCovToVcf.class).make();
	@Parameter(names={"-o","--output"},description=OPT_OUPUT_FILE_OR_STDOUT)
	private File outputFile = null;
	@Parameter(names={"-dup","--duplication"},description="Duplication treshold")
	private float duplicationTreshold = 1.9f;
	@Parameter(names={"-del","--deletion"},description="Deletion treshold")
	private float deletionTreshold = 0.6f;
	
	
	public IndexCovToVcf() {
	}
	
	
	@Override
	public int doWork(final List<String> args) {
		if(this.deletionTreshold>=1.0f) {
			LOG.error("Bad deletion treshold >=1.0");
			return -1;
		}
		if(this.duplicationTreshold<=1.0f) {
			LOG.error("Bad duplication treshold <=1.0");
			return -1;
		}
		if(this.deletionTreshold>=this.duplicationTreshold) {
			LOG.error("Bad tresholds del>=dup");
			return -1;
		}
		final Pattern tab = Pattern.compile("[\t]");
		BufferedReader r = null;
		VariantContextWriter vcw  = null;
		try {
			r = super.openBufferedReader(oneFileOrNull(args));
			String line = r.readLine();
			if(line==null) {		
				
				LOG.error( "Cannot read first line of input");
				return -1;
				}
			String tokens[] = tab.split(line);
			if(tokens.length<4 ||
				!tokens[0].equals("#chrom") ||
				!tokens[1].equals("start") ||
				!tokens[2].equals("end")) {
				LOG.error( "bad first line "+line );
				return -1;
				}
			
			final Set<VCFHeaderLine> metaData = new HashSet<>();
			VCFStandardHeaderLines.addStandardFormatLines(metaData, true, "GT");
			VCFStandardHeaderLines.addStandardInfoLines(metaData, true, "END");
			
			final VCFFormatHeaderLine foldHeader = new VCFFormatHeaderLine("F", 1, VCFHeaderLineType.Float,"Relative number of copy: 0.5 deletion 1 normal 2.0 duplication");
			metaData.add(foldHeader);
			final VCFFormatHeaderLine formatIsDeletion = new VCFFormatHeaderLine("DEL", 1, VCFHeaderLineType.Integer,"set to 1 if relative number of copy <= " + this.deletionTreshold );
			metaData.add(formatIsDeletion);
			final VCFFormatHeaderLine formatIsDuplication = new VCFFormatHeaderLine("DUP", 1, VCFHeaderLineType.Integer,"set to 1 if relative number of copy >= " + this.duplicationTreshold );
			metaData.add(formatIsDuplication);
			final VCFFilterHeaderLine filterAllDel = new VCFFilterHeaderLine("ALL_DEL", "number of samples greater than 1 and all are deletions");
			metaData.add(filterAllDel);
			final VCFFilterHeaderLine filterAllDup = new VCFFilterHeaderLine("ALL_DUP", "number of samples  greater than  1 and all are duplication");
			metaData.add(filterAllDup);
			final VCFFilterHeaderLine filterNoSV= new VCFFilterHeaderLine("NO_SV", "There is no DUP or DEL in this variant");
			metaData.add(filterNoSV);
			final VCFInfoHeaderLine infoNumDup = new VCFInfoHeaderLine("NDUP", 1, VCFHeaderLineType.Integer,"Number of samples being duplicated");
			metaData.add(infoNumDup);
			final VCFInfoHeaderLine infoNumDel = new VCFInfoHeaderLine("NDEL", 1, VCFHeaderLineType.Integer,"Number of samples being deleted");
			metaData.add(infoNumDel);

			
			final List<String> samples = Arrays.asList(tokens). subList(3,tokens.length);
			final VCFHeader vcfHeader = new VCFHeader(metaData, samples);
			
			vcw = super.openVariantContextWriter(outputFile);
			vcw.writeHeader(vcfHeader);
			
			//final List<Allele> NO_CALL_NO_CALL = Arrays.asList(Allele.NO_CALL,Allele.NO_CALL);
			final Allele DUP_ALLELE =Allele.create("<DUP>",false);
			final Allele DEL_ALLELE =Allele.create("<DEL>",false);
			final Allele REF_ALLELE =Allele.create("N",true);

			while((line=r.readLine())!=null) {
				if(StringUtil.isBlank(line)) continue;
				tokens =  tab.split(line);
				if(tokens.length!=3+samples.size()) {
					throw new JvarkitException.TokenErrors("expected "+(samples.size()+3)+ "columns.", tokens);
				}
				
				final Set<Allele> alleles =  new HashSet<>();
				alleles.add(REF_ALLELE);
				
				final VariantContextBuilder vcb = new VariantContextBuilder();
				vcb.chr(tokens[0]);
				vcb.start(Integer.parseInt(tokens[1]));
				final int chromEnd = Integer.parseInt(tokens[2]);
				vcb.stop(chromEnd);
				vcb.attribute(VCFConstants.END_KEY, chromEnd);
				
				int count_dup=0;
				int count_del=0;
				final List<Genotype> genotypes= new ArrayList<>(samples.size());
				for(int i=3;i<tokens.length;i++) {
					final float f = Float.parseFloat(tokens[i]);
					 if(f<0 || Float.isNaN(f) ||! Float.isFinite(f)) {
						 LOG.error("Bad fold "+f+" in "+line);
					 	}
					
					
					final GenotypeBuilder gb;
					
					if(f<=this.deletionTreshold) {
						gb = new GenotypeBuilder(samples.get(i-3),Collections.singletonList(DEL_ALLELE));
						alleles.add(DEL_ALLELE);
						gb.attribute(formatIsDeletion.getID(),1);
						count_del++;
						}
					else if(f>=this.duplicationTreshold) {
						gb = new GenotypeBuilder(samples.get(i-3),Collections.singletonList(DUP_ALLELE));
						alleles.add(DUP_ALLELE);
						gb.attribute(formatIsDuplication.getID(),1);
						count_dup++;
						}
					else
						{
						gb = new GenotypeBuilder(samples.get(i-3),Collections.singletonList(REF_ALLELE));
						gb.attribute(formatIsDuplication.getID(),0);
						}	
					gb.attribute(foldHeader.getID(),f);
					genotypes.add(gb.make());
					}
				vcb.alleles(alleles);
				vcb.genotypes(genotypes);
				
				if(count_dup == samples.size() && samples.size()!=1) {
					vcb.filter(filterAllDup.getID());
				}
				if(count_del == samples.size() && samples.size()!=1) {
					vcb.filter(filterAllDel.getID());
				}
				
				if(count_dup == 0 && count_del==0) {
					vcb.filter(filterNoSV.getID());
				}
				
				vcb.attribute(infoNumDel.getID(), count_del);
				vcb.attribute(infoNumDup.getID(), count_dup);
				
				vcw.add(vcb.make());
				}
			vcw.close();
			vcw=null;
			r.close();
			r=null;
			
			return 0;
		} catch(final Exception err) {
			LOG.error(err);
			return -1;
			}
		finally
			{
			CloserUtil.close(r);
			CloserUtil.close(vcw);
			}
		}

		public static void main(final String[] args) {
			new IndexCovToVcf().instanceMainWithExit(args);
			}

}
