import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.broadinstitute.gatk.utils.jna.drmaa.v1_0.JnaSessionFactory;
import org.ggf.drmaa.*;

public class MultipleJobs {
    public static void main( String[] args ) throws Exception {
        System.out.println("Hello There!");
        JnaSessionFactory jsf = new JnaSessionFactory();
        Session s = jsf.getSession();
        s.init(null);

        for (int i=0; i < 2; i++) {
            SubmitLsfJob(s, i);
        }

        s.exit();

        System.out.println("All Done -- Bye Bye!");
    }

    public static void SubmitLsfJob(Session s, int i) throws Exception {
        JobTemplate jt = s.createJobTemplate();
        jt.setWorkingDirectory(JobTemplate.WORKING_DIRECTORY);
        String outPath = String.format(":%s/genome-strip-test/drmaa-gatk-java/foo-%d.out", System.getenv("HOME"), i);
        String jobName = "Foo-" + i;
        jt.setJobName(jobName);
        jt.setOutputPath(outPath);
        jt.setJoinFiles(true);
        jt.setNativeSpecification("-q short");
      	jt.setRemoteCommand("/bin/echo");
        String[] cmd_args = new String[] { "Hello", "World", "(JAVA)" , " ", String.valueOf(i) };
        List<String> cmd_args_list = Arrays.asList(cmd_args);
      	jt.setArgs(cmd_args_list);
      	String id = s.runJob(jt);
      	System.out.println ("Job " + id + " is now running.");

      	s.deleteJobTemplate(jt);
//      	s.deleteJobTemplate(jt);  // second deleteJobTemplate causes segfault
    }
}
