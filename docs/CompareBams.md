# CompareBams

Compare two or more BAM files


## Usage

```
Usage: cmpbams [options] Files
  Options:
    -c, --cigar
      use cigar String for comparaison
      Default: false
    -d, --distance
      distance tolerance between two alignments
      Default: 10
    -h, --help
      print help and exit
    --helpFormat
      What kind of help
      Possible Values: [usage, markdown, xml]
    -Q, --mapq
      min MAPQ
      Default: 0
    --maxRecordsInRam
      When writing  files that need to be sorted, this will specify the number 
      of records stored in RAM before spilling to disk. Increasing this number 
      reduces the number of file  handles needed to sort a file, and increases 
      the amount of RAM needed
      Default: 50000
    -o, --output
      Output file. Optional . Default: stdout
    -r, --region
      An interval as the following syntax : "chrom:start-end" or 
      "chrom:middle+extend"  or "chrom:start-end+extend".A program might use a 
      Reference sequence to fix the chromosome name (e.g: 1->chr1)
      Default: <empty string>
    -F, --sam
      use sam flag for comparaison
      Default: false
    --tmpDir
      tmp working directory. Default: java.io.tmpDir
      Default: []
    --version
      print version and exit

```


## Keywords

 * sam
 * bam
 * compare


## Compilation

### Requirements / Dependencies

* java compiler SDK 1.8 http://www.oracle.com/technetwork/java/index.html (**NOT the old java 1.7 or 1.6**) . Please check that this java is in the `${PATH}`. Setting JAVA_HOME is not enough : (e.g: https://github.com/lindenb/jvarkit/issues/23 )
* GNU Make >= 3.81
* curl/wget
* git
* xsltproc http://xmlsoft.org/XSLT/xsltproc2.html (tested with "libxml 20706, libxslt 10126 and libexslt 815")


### Download and Compile

```bash
$ git clone "https://github.com/lindenb/jvarkit.git"
$ cd jvarkit
$ make cmpbams
```

The *.jar libraries are not included in the main jar file, so you shouldn't move them (https://github.com/lindenb/jvarkit/issues/15#issuecomment-140099011 ).
The required libraries will be downloaded and installed in the `dist` directory.

### edit 'local.mk' (optional)

The a file **local.mk** can be created edited to override/add some definitions.

For example it can be used to set the HTTP proxy:

```
http.proxy.host=your.host.com
http.proxy.port=124567
```
## Source code 

[https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/cmpbams/CompareBams.java](https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/cmpbams/CompareBams.java)


<details>
<summary>Git History</summary>

```
Mon Aug 7 09:53:19 2017 +0200 ; fixed unicode problems after https://github.com/lindenb/jvarkit/issues/82 ; https://github.com/lindenb/jvarkit/commit/68254c69b027a9ce81d8b211447f1c0bf02dc626
Fri Jun 2 16:31:30 2017 +0200 ; circos / lumpy ; https://github.com/lindenb/jvarkit/commit/7bddffca3899196e568fb5e1a479300c0038f74f
Wed May 17 14:09:36 2017 +0200 ; fix typo bioalcidae ; https://github.com/lindenb/jvarkit/commit/9db2344e7ce840df02c5a7b4e2a91d6f1a5f2e8d
Tue Apr 18 13:24:50 2017 +0200 ; cont-cleanup ; https://github.com/lindenb/jvarkit/commit/a86c8971fe5ebb3f8de175c75e78f2d0e5325cfd
Thu Oct 31 17:13:55 2013 +0100 ; stuff for comparing bams ; https://github.com/lindenb/jvarkit/commit/fc2598c96eaa7b3001aac99b2c8d6026f78facdd
Mon May 6 18:56:46 2013 +0200 ; moving to git ; https://github.com/lindenb/jvarkit/commit/55158d13f0950f16c4a3cc3edb92a87905346ee1
```

</details>

## Contribute

- Issue Tracker: [http://github.com/lindenb/jvarkit/issues](http://github.com/lindenb/jvarkit/issues)
- Source Code: [http://github.com/lindenb/jvarkit](http://github.com/lindenb/jvarkit)

## License

The project is licensed under the MIT license.

## Citing

Should you cite **cmpbams** ? [https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md](https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md)

The current reference is:

[http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)

> Lindenbaum, Pierre (2015): JVarkit: java-based utilities for Bioinformatics. figshare.
> [http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)


## Example

Question was : "mapping with BWA produced a variation one year ago. We then mapped the same fastq with two different sets of parameters, but we cannot find the variant anymore. Has the mapping changed ?"

Extract the read names from the original BAM:
```
samtools view  file1.bam K01:2179-2179 |\
 cut -d '	' -f 1  | sort | uniq > names.txt
```
 
Use [[SamGrep]] to retieve the reads in the 3 bams:

```
java -jar dist/samgrep.jar -f names.txt file1.bam &gt; tmp1.sam
java -jar dist/samgrep.jar -f names.txt file2.bam &gt; tmp2.sam
java -jar dist/samgrep.jar -f names.txt file3.bam &gt; tmp3.sam
```

Run CmpBams

```
$ java -jar dist/cmpbams.jar -F -C tmp1.sam tmp2.sam tmp3.sam

#READ-Name	tmp1.sam tmp2.sam|tmp1.sam tmp3.sam|tmp2.sam tmp3.sam	tmp1.sam	tmp2.sam	tmp3.sam
HWI-1KL149:20:C1CU7ACXX:1:1101:17626:32431/1	EQ|EQ|EQ	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M
HWI-1KL149:20:C1CU7ACXX:1:1101:17626:32431/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:1:1102:16831:71728/1	EQ|EQ|EQ	K01:2133=83/100M	K01:2133=83/100M	K01:2133=83/100M
HWI-1KL149:20:C1CU7ACXX:1:1102:16831:71728/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:1:1105:3309:27760/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:1:1105:3309:27760/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:1:1106:2914:12111/1	EQ|EQ|EQ	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M
HWI-1KL149:20:C1CU7ACXX:1:1106:2914:12111/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:1:1107:11589:17295/1	EQ|EQ|EQ	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M
HWI-1KL149:20:C1CU7ACXX:1:1107:11589:17295/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:1:1110:14096:95943/1	EQ|EQ|EQ	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M
HWI-1KL149:20:C1CU7ACXX:1:1110:14096:95943/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:1:1110:15369:59046/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:1:1110:15369:59046/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:1:1111:8599:97362/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:1:1111:8599:97362/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:1:1113:10490:30873/1	EQ|EQ|EQ	K01:2120=83/100M	K01:2120=83/100M	K01:2120=83/100M
HWI-1KL149:20:C1CU7ACXX:1:1113:10490:30873/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:1:1113:12360:36316/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:1:1113:12360:36316/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:1:1113:4589:62685/1	EQ|EQ|EQ	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M
HWI-1KL149:20:C1CU7ACXX:1:1113:4589:62685/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:1:1115:7288:99676/1	EQ|EQ|EQ	K01:2120=83/100M	K01:2120=83/100M	K01:2120=83/100M
HWI-1KL149:20:C1CU7ACXX:1:1115:7288:99676/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:1:1116:8136:52921/1	EQ|EQ|EQ	K01:2133=83/100M	K01:2133=83/100M	K01:2133=83/100M
HWI-1KL149:20:C1CU7ACXX:1:1116:8136:52921/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:1:1202:11809:66877/1	EQ|EQ|EQ	K01:2104=83/100M	K01:2104=83/100M	K01:2104=83/100M
HWI-1KL149:20:C1CU7ACXX:1:1202:11809:66877/2	EQ|EQ|EQ	K01:2043=163/100M	K01:2043=163/100M	K01:2043=163/100M
HWI-1KL149:20:C1CU7ACXX:1:1202:18844:98575/1	EQ|EQ|EQ	K01:2133=83/100M	K01:2133=83/100M	K01:2133=83/100M
HWI-1KL149:20:C1CU7ACXX:1:1202:18844:98575/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:1:1205:20782:28689/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:1:1205:20782:28689/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:1:1206:10108:83718/1	EQ|EQ|EQ	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M
HWI-1KL149:20:C1CU7ACXX:1:1206:10108:83718/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:1:1212:17964:23344/1	EQ|EQ|EQ	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M
HWI-1KL149:20:C1CU7ACXX:1:1212:17964:23344/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:1:1213:9111:56546/1	EQ|EQ|EQ	K01:2133=83/100M	K01:2133=83/100M	K01:2133=83/100M
HWI-1KL149:20:C1CU7ACXX:1:1213:9111:56546/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:1:1216:4380:98965/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:1:1216:4380:98965/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:1:1216:4493:85995/1	EQ|NE|NE	K01:2143=83/36S36M3I25M	K01:2143=83/36S36M3I25M	K01:2107=83/72M3I25M
HWI-1KL149:20:C1CU7ACXX:1:1216:4493:85995/2	EQ|EQ|EQ	K01:2043=163/100M	K01:2043=163/100M	K01:2043=163/100M
HWI-1KL149:20:C1CU7ACXX:1:1216:8034:78319/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:1:1216:8034:78319/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:1:1316:14751:4679/1	EQ|EQ|EQ	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M
HWI-1KL149:20:C1CU7ACXX:1:1316:14751:4679/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2102:4725:60173/1	EQ|EQ|EQ	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M
HWI-1KL149:20:C1CU7ACXX:1:2102:4725:60173/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2104:19271:24502/1	EQ|EQ|EQ	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M
HWI-1KL149:20:C1CU7ACXX:1:2104:19271:24502/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2104:2016:81735/1	EQ|EQ|EQ	K01:2133=83/100M	K01:2133=83/100M	K01:2133=83/100M
HWI-1KL149:20:C1CU7ACXX:1:2104:2016:81735/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2110:4445:72697/1	EQ|EQ|EQ	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M
HWI-1KL149:20:C1CU7ACXX:1:2110:4445:72697/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2111:2256:47748/1	EQ|EQ|EQ	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M
HWI-1KL149:20:C1CU7ACXX:1:2111:2256:47748/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2115:12497:79931/1	EQ|EQ|EQ	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M
HWI-1KL149:20:C1CU7ACXX:1:2115:12497:79931/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2115:17576:9737/1	EQ|EQ|EQ	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M
HWI-1KL149:20:C1CU7ACXX:1:2115:17576:9737/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2116:7977:30610/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:1:2116:7977:30610/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2201:16984:100451/1	EQ|EQ|EQ	K01:2133=83/100M	K01:2133=83/100M	K01:2133=83/100M
HWI-1KL149:20:C1CU7ACXX:1:2201:16984:100451/2	EQ|EQ|EQ	K01:2059=163/87M13S	K01:2059=163/87M13S	K01:2059=163/87M13S
HWI-1KL149:20:C1CU7ACXX:1:2203:19912:68616/1	EQ|EQ|EQ	K01:2120=83/100M	K01:2120=83/100M	K01:2120=83/100M
HWI-1KL149:20:C1CU7ACXX:1:2203:19912:68616/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2204:13318:18341/1	EQ|EQ|EQ	K01:2133=83/100M	K01:2133=83/100M	K01:2133=83/100M
HWI-1KL149:20:C1CU7ACXX:1:2204:13318:18341/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2206:10726:12303/1	EQ|EQ|EQ	K01:2133=83/100M	K01:2133=83/100M	K01:2133=83/100M
HWI-1KL149:20:C1CU7ACXX:1:2206:10726:12303/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2206:11557:78671/1	EQ|EQ|EQ	K01:2120=83/100M	K01:2120=83/100M	K01:2120=83/100M
HWI-1KL149:20:C1CU7ACXX:1:2206:11557:78671/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2211:12806:63973/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:1:2211:12806:63973/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2212:17602:62052/1	EQ|EQ|EQ	K01:2104=83/100M	K01:2104=83/100M	K01:2104=83/100M
HWI-1KL149:20:C1CU7ACXX:1:2212:17602:62052/2	EQ|EQ|EQ	K01:2043=163/100M	K01:2043=163/100M	K01:2043=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2212:19408:52552/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:1:2212:19408:52552/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2303:8733:45438/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:1:2303:8733:45438/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2304:9806:12935/1	EQ|EQ|EQ	K01:2120=83/100M	K01:2120=83/100M	K01:2120=83/100M
HWI-1KL149:20:C1CU7ACXX:1:2304:9806:12935/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2305:12165:42334/1	EQ|EQ|EQ	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M
HWI-1KL149:20:C1CU7ACXX:1:2305:12165:42334/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2305:2388:67842/1	EQ|EQ|EQ	K01:2133=83/100M	K01:2133=83/100M	K01:2133=83/100M
HWI-1KL149:20:C1CU7ACXX:1:2305:2388:67842/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2307:14199:91258/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:1:2307:14199:91258/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2307:3121:93985/1	EQ|EQ|EQ	K01:2133=83/100M	K01:2133=83/100M	K01:2133=83/100M
HWI-1KL149:20:C1CU7ACXX:1:2307:3121:93985/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2309:13907:13532/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:1:2309:13907:13532/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2309:20396:57002/1	EQ|EQ|EQ	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M
HWI-1KL149:20:C1CU7ACXX:1:2309:20396:57002/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2312:11602:6630/1	EQ|EQ|EQ	K01:2120=83/100M	K01:2120=83/100M	K01:2120=83/100M
HWI-1KL149:20:C1CU7ACXX:1:2312:11602:6630/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2313:11868:31327/1	EQ|EQ|EQ	K01:2120=83/100M	K01:2120=83/100M	K01:2120=83/100M
HWI-1KL149:20:C1CU7ACXX:1:2313:11868:31327/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2313:9555:94108/1	EQ|EQ|EQ	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M
HWI-1KL149:20:C1CU7ACXX:1:2313:9555:94108/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:1:2315:19820:15046/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:1:2315:19820:15046/2	EQ|EQ|EQ	K01:2081=163/99M1S	K01:2081=163/99M1S	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:2:1101:18362:28315/1	EQ|EQ|EQ	K01:2120=83/100M	K01:2120=83/100M	K01:2120=83/100M
HWI-1KL149:20:C1CU7ACXX:2:1101:18362:28315/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:2:1105:18846:45527/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:2:1105:18846:45527/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:2:1105:5659:65125/1	EQ|EQ|EQ	K01:2120=83/100M	K01:2120=83/100M	K01:2120=83/100M
HWI-1KL149:20:C1CU7ACXX:2:1105:5659:65125/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:2:1108:9609:39170/1	EQ|EQ|EQ	K01:2120=83/100M	K01:2120=83/100M	K01:2120=83/100M
HWI-1KL149:20:C1CU7ACXX:2:1108:9609:39170/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:2:1110:2262:8369/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:2:1110:2262:8369/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:2:1111:18496:5547/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:2:1111:18496:5547/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:2:1112:10132:23322/1	EQ|EQ|EQ	K01:2133=83/100M	K01:2133=83/100M	K01:2133=83/100M
HWI-1KL149:20:C1CU7ACXX:2:1112:10132:23322/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:2:1112:7260:56414/1	EQ|EQ|EQ	K01:2133=83/100M	K01:2133=83/100M	K01:2133=83/100M
HWI-1KL149:20:C1CU7ACXX:2:1112:7260:56414/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:2:1201:6906:82750/1	EQ|EQ|EQ	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M
HWI-1KL149:20:C1CU7ACXX:2:1201:6906:82750/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:2:1202:16231:100362/1	EQ|EQ|EQ	K01:2133=83/100M	K01:2133=83/100M	K01:2133=83/100M
HWI-1KL149:20:C1CU7ACXX:2:1202:16231:100362/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:2:1213:12574:89489/1	EQ|EQ|EQ	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M
HWI-1KL149:20:C1CU7ACXX:2:1213:12574:89489/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:2:1214:20898:3105/1	EQ|EQ|EQ	K01:2120=83/100M	K01:2120=83/100M	K01:2120=83/100M
HWI-1KL149:20:C1CU7ACXX:2:1214:20898:3105/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:```20:C1CU7ACXX:2:1214:7035:46585/1	EQ|EQ|EQ	K01:2133=83/100M	K01:2133=83/100M	K01:2133=83/100M
HWI-1KL149:20:C1CU7ACXX:2:1214:7035:46585/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:2:1215:19107:31048/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:2:1215:19107:31048/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:2:1216:15500:73171/1	EQ|EQ|EQ	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M
HWI-1KL149:20:C1CU7ACXX:2:1216:15500:73171/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:2:1216:6409:43952/1	EQ|EQ|EQ	K01:2133=83/100M	K01:2133=83/100M	K01:2133=83/100M
HWI-1KL149:20:C1CU7ACXX:2:1216:6409:43952/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:2:1301:16595:88662/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:2:1301:16595:88662/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:2:1306:12619:24138/1	EQ|NE|NE	K01:2143=83/36S36M3I25M	K01:2143=83/36S36M3I25M	K01:2107=83/72M3I25M
HWI-1KL149:20:C1CU7ACXX:2:1306:12619:24138/2	EQ|EQ|EQ	K01:2043=163/100M	K01:2043=163/100M	K01:2043=163/100M
HWI-1KL149:20:C1CU7ACXX:2:1308:8618:21991/1	EQ|EQ|EQ	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M
HWI-1KL149:20:C1CU7ACXX:2:1308:8618:21991/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:2:1309:15540:69632/1	EQ|EQ|EQ	K01:2133=83/100M	K01:2133=83/100M	K01:2133=83/100M
HWI-1KL149:20:C1CU7ACXX:2:1309:15540:69632/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:2:1314:9489:93274/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:2:1314:9489:93274/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:2:1316:5692:99314/1	EQ|EQ|EQ	K01:2120=83/100M	K01:2120=83/100M	K01:2120=83/100M
HWI-1KL149:20:C1CU7ACXX:2:1316:5692:99314/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:2:2108:14003:59876/1	EQ|EQ|EQ	K01:2136=83/44M3I53M	K01:2136=83/44M3I53M	K01:2136=83/44M3I53M
HWI-1KL149:20:C1CU7ACXX:2:2108:14003:59876/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:2:2113:14713:81195/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:2:2113:14713:81195/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:2:2114:13002:89288/1	EQ|EQ|EQ	K01:2133=83/100M	K01:2133=83/100M	K01:2133=83/100M
HWI-1KL149:20:C1CU7ACXX:2:2114:13002:89288/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:2:2201:11170:94334/1	EQ|EQ|EQ	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M
HWI-1KL149:20:C1CU7ACXX:2:2201:11170:94334/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:2:2202:4380:42920/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:2:2202:4380:42920/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:2:2213:14141:87844/1	EQ|EQ|EQ	K01:2120=83/100M	K01:2120=83/100M	K01:2120=83/100M
HWI-1KL149:20:C1CU7ACXX:2:2213:14141:87844/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:2:2213:15510:90052/1	EQ|EQ|EQ	K01:2122=83/57M3I23M1D17M	K01:2122=83/57M3I23M1D17M	K01:2122=83/57M3I23M1D17M
HWI-1KL149:20:C1CU7ACXX:2:2213:15510:90052/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:2:2213:1759:92031/1	EQ|EQ|EQ	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M	K01:2123=83/56M3I41M
HWI-1KL149:20:C1CU7ACXX:2:2213:1759:92031/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:2:2215:14279:12564/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:2:2215:14279:12564/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:2:2215:16028:26845/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:2:2215:16028:26845/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:2:2215:16774:44335/1	EQ|EQ|EQ	K01:2120=83/100M	K01:2120=83/100M	K01:2120=83/100M
HWI-1KL149:20:C1CU7ACXX:2:2215:16774:44335/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:2:2301:16926:82641/1	EQ|EQ|EQ	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M	K01:2136=83/43M3I54M
HWI-1KL149:20:C1CU7ACXX:2:2301:16926:82641/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M
HWI-1KL149:20:C1CU7ACXX:2:2309:5711:82879/1	EQ|EQ|EQ	K01:2120=83/100M	K01:2120=83/100M	K01:2120=83/100M
HWI-1KL149:20:C1CU7ACXX:2:2309:5711:82879/2	EQ|EQ|EQ	K01:1990=163/100M	K01:1990=163/100M	K01:1990=163/100M
HWI-1KL149:20:C1CU7ACXX:2:2310:13450:61828/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:2:2310:13450:61828/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:2:2311:14082:99026/1	EQ|EQ|EQ	K01:2213=83/100M	K01:2213=83/100M	K01:2213=83/100M
HWI-1KL149:20:C1CU7ACXX:2:2311:14082:99026/2	EQ|EQ|EQ	K01:2081=163/100M	K01:2081=163/100M	K01:2081=163/100M
HWI-1KL149:20:C1CU7ACXX:2:2315:4940:7934/1	EQ|EQ|EQ	K01:2133=83/100M	K01:2133=83/100M	K01:2133=83/100M
HWI-1KL149:20:C1CU7ACXX:2:2315:4940:7934/2	EQ|EQ|EQ	K01:2059=163/100M	K01:2059=163/100M	K01:2059=163/100M</h:pre>
```
