import scala.collection.JavaConversions._
import org.broadinstitute.gatk.utils.jna.drmaa.v1_0.JnaSessionFactory
import org.broadinstitute.gatk.queue.QException
import org.ggf.drmaa._

class MultipleJobsThreaded {
  @volatile
  var session: org.ggf.drmaa.Session = _

  var setup: Int = 0

  def init(): Unit = {
    session = new JnaSessionFactory().getSession
    session.init(null)
    setup = setup + 1
    println("initing for the %d time".format(setup))
  }

  def exit(): Unit = session.exit()

  def submitLSFJob(num: Int): Unit = {
    var jobId: String = ""
    var outFile: String = "%s/%s-%d.txt".format(sys.env("PWD"), "scala-lsf-test", num)
    session.synchronized {
      if (setup < 2) {
        session.init(null)
        setup = setup + 1
        println("initing for the %d time".format(setup))
      }
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

  def execute(i: Int) {
    println("Entering jobExecutor run (%d)".format(i))
    println("jobExecutor (%d): submitting lsf job".format(i))
    submitLSFJob(i)
  }

  def jobExecutor(i: Int): Unit = {
    val self = this
    val job = new Thread(new Runnable {
      def run() = {
        self.execute(i)
      }
    })
    job.start()
  }
}

object MultipleJobsThreaded {
  def main(args: Array[String]): Unit = {
    val mj = new MultipleJobsThreaded()
    mj.init()
    for (i <- 1 to 2) {
      mj.jobExecutor(i)
    }
    mj.exit()
  }
}
