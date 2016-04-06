import scala.collection.JavaConversions._

import org.broadinstitute.gatk.queue.engine.drmaa
import org.broadinstitute.gatk.utils.jna.drmaa.v1_0.JnaSessionFactory
import org.broadinstitute.gatk.queue.QException
import org.ggf.drmaa._

class MultipleJobs2 extends drmaa.DrmaaJobManager

object MultipleJobs2 {
  def main(args: Array[String]): Unit = {
    var manager = new MultipleJobs2()
    manager.init()
    var jobId: String = ""
    var outFile: String = "%s/%s-%d.txt".format(sys.env("PWD"), "scala-lsf-test", 1)
    val drmaaJob: JobTemplate = manager.session.createJobTemplate
    drmaaJob.setJobName("foo")
    drmaaJob.setWorkingDirectory(sys.env("PWD"))
    drmaaJob.setOutputPath(":" + outFile)
    drmaaJob.setJoinFiles(true)
    drmaaJob.setNativeSpecification("-q short")
    drmaaJob.setRemoteCommand("/bin/echo")
    var cmdArgs: List[String] = List("Hello", "World", "(Scala)", 1.toString() )
    drmaaJob.setArgs(cmdArgs)
    try {
      jobId = manager.session.runJob(drmaaJob)
    } catch {
      case de: DrmaaException => throw new QException("Unable to submit job: " + de.getLocalizedMessage)
    } finally {
      manager.session.deleteJobTemplate(drmaaJob)
    }
    println("Submitted job id: " + jobId)
    manager.session.exit()
  }
}
