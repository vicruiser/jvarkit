# BamMatrix

![Last commit](https://img.shields.io/github/last-commit/lindenb/jvarkit.png)

Bam matrix, inspired from 10x/loupe 


## Usage

```
Usage: bammatrix [options] Files
  Options:
    --color_-scale
      Color scale
      Default: LOG
      Possible Values: [LINEAR, LOG]
    -h, --help
      print help and exit
    --helpFormat
      What kind of help. One of [usage,markdown,xml].
    --higligth, -B
      Optional Bed file to hightlight regions of interest
    --kg, -k
      Optional 'known gene' file to draw the exons. UCSC knownGene File/URL. 
      The knowGene format is a compact alternative to GFF/GTF because one 
      transcript is described using only one line.	Beware chromosome names are 
      formatted the same as your REFERENCE. A typical KnownGene file is 
      http://hgdownload.cse.ucsc.edu/goldenPath/hg19/database/knownGene.txt.gz 
      .If you only have a gff file, you can try to generate a knownGene file 
      with [http://lindenb.github.io/jvarkit/Gff2KnownGene.html](http://lindenb.github.io/jvarkit/Gff2KnownGene.html)
    --mapq
      minimal mapping quality
      Default: 30
    --name, -name
      use 'BX:Z:' attribute from 10x genomics  as the read name. "Chromium 
      barcode sequence that is error-corrected and confirmed against a list of 
      known-good barcode sequences.". See https://support.10xgenomics.com/genome-exome/software/pipelines/latest/output/bam
      Default: READ_NAME
      Possible Values: [READ_NAME, BX, MI]
    -o, --output
      Output file. Optional . Default: stdout
    -R, --reference
      Indexed fasta Reference file. This file must be indexed with samtools 
      faidx and with picard CreateSequenceDictionary
  * -r, -r1, --region
      first region.An interval as the following syntax : "chrom:start-end" or 
      "chrom:middle+extend"  or "chrom:start-end+extend" or 
      "chrom:start-end+extend-percent%".A program might use a Reference 
      sequence to fix the chromosome name (e.g: 1->chr1)
    -r2, --region2
      2nd region. Default: use first region. An interval as the following 
      syntax : "chrom:start-end" or "chrom:middle+extend"  or 
      "chrom:start-end+extend" or "chrom:start-end+extend-percent%".A program 
      might use a Reference sequence to fix the chromosome name (e.g: 1->chr1)
    -sa, --sa
      Use other canonical alignemts from the 'SA:Z:*' attribute
      Default: false
    -s, --size
      matrix size
      Default: 1000
    --version
      print version and exit

```


## Keywords

 * sam
 * bam
 * compare
 * matrix


## Compilation

### Requirements / Dependencies

* java [compiler SDK 11](https://jdk.java.net/11/). Please check that this java is in the `${PATH}`. Setting JAVA_HOME is not enough : (e.g: https://github.com/lindenb/jvarkit/issues/23 )


### Download and Compile

```bash
$ git clone "https://github.com/lindenb/jvarkit.git"
$ cd jvarkit
$ ./gradlew bammatrix
```

The java jar file will be installed in the `dist` directory.


## Creation Date

20190620

## Source code 

[https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/cmpbams/BamMatrix.java](https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/cmpbams/BamMatrix.java)


## Contribute

- Issue Tracker: [http://github.com/lindenb/jvarkit/issues](http://github.com/lindenb/jvarkit/issues)
- Source Code: [http://github.com/lindenb/jvarkit](http://github.com/lindenb/jvarkit)

## License

The project is licensed under the MIT license.

## Citing

Should you cite **bammatrix** ? [https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md](https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md)

The current reference is:

[http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)

> Lindenbaum, Pierre (2015): JVarkit: java-based utilities for Bioinformatics. figshare.
> [http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)


## Example

```
java -jar dist/bammatrix.jar -o out.png \
	--kg "http://hgdownload.cse.ucsc.edu/goldenPath/hg19/database/knownGene.txt.gz" \
	-r "chr1:2345-6789" -B cnv.bed --name BX \
	NOVASEQ/Sample/outs/phased_possorted_bam.bam
```

https://twitter.com/yokofakun/status/1142088565326843904

![https://twitter.com/yokofakun/status/1142088565326843904](https://pbs.twimg.com/media/D9mDYo4WsAAOaSK.jpg)


https://twitter.com/yokofakun/status/1038060108373286912

![https://twitter.com/yokofakun/status/1038060108373286912](https://pbs.twimg.com/media/Dmft0cSXoAAp78l.jpg)
