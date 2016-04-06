import scala.collection.JavaConversions._
import org.broadinstitute.gatk.utils.jna.drmaa.v1_0.JnaSessionFactory
import org.broadinstitute.gatk.queue.QException
import org.ggf.drmaa._

object MultipleJobsSimple {
  def main(args: Array[String]): Unit = {
    var session = new JnaSessionFactory().getSession
    session.init(null)
    for (i <- 1 to 2) {
      submitLSFJobs(session, i)
    }
    session.exit()
  }

  def submitLSFJobs(session: org.ggf.drmaa.Session, i: Int): Unit = {
    var jobId: String = ""
    var outFile: String = "%s/%s-%s.txt".format(sys.env("PWD"), "scala-simple-lsf-test", i)
    val drmaaJob: JobTemplate = session.createJobTemplate
    drmaaJob.setJobName("foo")
    drmaaJob.setWorkingDirectory(sys.env("PWD"))
    drmaaJob.setOutputPath(":" + outFile)
    drmaaJob.setJoinFiles(true)
    drmaaJob.setNativeSpecification("-q short")
    drmaaJob.setRemoteCommand("/bin/echo")
    var cmdArgs: List[String] = List("Hello", "World", "(Scala)", i.toString)
    drmaaJob.setArgs(cmdArgs)
    try {
      jobId = session.runJob(drmaaJob)
    } catch {
      case de: DrmaaException => throw new QException("Unable to submit job: " + de.getLocalizedMessage)
    } finally {
      session.deleteJobTemplate(drmaaJob)
    }
    println("Submitted job id: " + jobId)
  }
}
