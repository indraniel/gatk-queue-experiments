.PHONY: clean

CLASSPATH := .:/gscmnt/gc2802/halllab/idas/jira/BIO-1662/vendor/local/jars/GenomeAnalysisTK-3.5-idas-experimental-9420569-2016.03.31.jar:/gscmnt/gc2802/halllab/idas/jira/BIO-1662/vendor/github/gatk-protected/protected/gatk-queue-package-distribution/target/gatk-queue-package-distribution-3.5.jar

MultipleJobsSimple: export LD_LIBRARY_PATH := /tmp/idas-local/lib:$(LD_LIBRARY_PATH)
MultipleJobsSimple: MultipleJobsSimple.class
	scala -classpath $(CLASSPATH) MultipleJobsSimple

MultipleJobsSimple.class: MultipleJobsSimple.scala
	scalac -classpath $(CLASSPATH) $<

clean:
	rm *.class *.txt
