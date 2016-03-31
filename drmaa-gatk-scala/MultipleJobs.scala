import scala.collection.JavaConversions._
import org.broadinstitute.gatk.utils.jna.drmaa.v1_0.JnaSessionFactory
import org.broadinstitute.gatk.queue.QException
import org.ggf.drmaa._

object MultipleJobs {
  def main(args: Array[String]): Unit = {
    for (i <- 1 to 2) {
      jobExecutor(i)
    }
  }

  def jobExecutor(i: Int): Unit = {
    val job = new Thread(new Runnable {
      def run() {
        println("Entering jobExecutor run (%d)".format(i))
        var session = new JnaSessionFactory().getSession
        session.init(null)
        println("jobExecutor (%d): submitting lsf job".format(i))
        submitLSFJob(session, i)
        session.exit()
      }
    })
    job.start()
  }

  def submitLSFJob(session: org.ggf.drmaa.Session, num: Int): Unit = {
    var jobId: String = ""
    var outFile: String = "%s/%s-%d.txt".format(sys.env("PWD"), "scala-lsf-test", num)
    val drmaaJob: JobTemplate = session.createJobTemplate
    drmaaJob.setJobName("foo")
    drmaaJob.setWorkingDirectory(sys.env("PWD"))
    drmaaJob.setOutputPath(":" + outFile)
    drmaaJob.setJoinFiles(true)
    drmaaJob.setNativeSpecification("-q short")
    drmaaJob.setRemoteCommand("/bin/echo")
    var cmdArgs: List[String] = List("Hello", "World", "(Scala)", num.toString() )
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
