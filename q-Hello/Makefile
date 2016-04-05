.PHONY: clean

RM := rm -rf

JAVA_HOME := /gapp/x64linux/opt/java/jdk/jdk1.8.0_60
JAVA := $(JAVA_HOME)/bin/java

EXPERIMENTAL_GATK := /gscmnt/gc2802/halllab/idas/jira/BIO-1662/vendor/local/jars
GATK  := $(EXPERIMENTAL_GATK)/GenomeAnalysisTK-3.5-idas-experimental-8904624-2016.04.04.jar
QUEUE := $(EXPERIMENTAL_GATK)/Queue-3.5-idas-experimental-8904624-2016.04.04.jar

DRMAA_LIBS := /tmp/idas-local/lib

SV_DIR := /gscmnt/gc2801/analytics/jeldred/GenomeSTRiP/svtoolkit
CLASSPATH := $(SV_DIR)/lib/SVToolkit.jar:$(GATK):$(QUEUE)

PATH := $(JAVA_HOME)/bin:/gscmnt/gc2801/analytics/jeldred/GenomeSTRiP/bin:$(PATH)

all: run

run: export LD_LIBRARY_PATH := /tmp/idas-local/lib:$(LD_LIBRARY_PATH)
run: Hello.q
	$(JAVA) -version
	$(JAVA) -cp $(CLASSPATH) org.broadinstitute.gatk.queue.QCommandLine \
	 -S $< \
	 -disableJobReport \
	 -l DEBUG \
	 -jobRunner MGI \
	 -jobQueue short \
	 -run

clean:
	$(RM) .*.done .queue/ *.out
