
export SV_DIR=/gscmnt/gc2801/analytics/jeldred/GenomeSTRiP/svtoolkit
HELLO=/gscmnt/gc2801/analytics/jeldred/scripts/Hello.q

JAVA_HOME=/gapp/x64linux/opt/java/jdk/jdk1.8.0_60
export PATH=$JAVA_HOME/bin:/gscmnt/gc2801/analytics/jeldred/GenomeSTRiP/bin:$PATH

# linus261 specific
export LD_LIBRARY_PATH=/tmp/idas-local/lib:$LD_LIBRARY_PATH

echo $PATH
#export PATH=/gscmnt/gc2801/analytics/jeldred/drmaa_versions/lsf_drmaa-1.0.4:/gscmnt/gc2801/analytics/jeldred/GenomeSTRiP/bin:$PATH
IDAS_GATK=/gscmnt/gc2802/halllab/idas/jira/BIO-1662/vendor/local/jars/GenomeAnalysisTK-3.5-idas-experimental-a4da018-2016.03.30.jar
IDAS_QUEUE=${SV_DIR}/lib/gatk/Queue.jar

#classpath="${SV_DIR}/lib/SVToolkit.jar:${SV_DIR}/lib/gatk/GenomeAnalysisTK.jar:${SV_DIR}/lib/gatk/Queue.jar"
classpath="${SV_DIR}/lib/SVToolkit.jar:${IDAS_GATK}:${IDAS_QUEUE}"

java -version
#java -Xmx22g -cp ${classpath} org.broadinstitute.gatk.queue.QCommandLine \
java -cp ${classpath} org.broadinstitute.gatk.queue.QCommandLine \
 -S ${HELLO} \
 -disableJobReport \
 -l DEBUG \
 -jobRunner Drmaa \
 -jobQueue short \
 -run
