package org.broadinstitute.sv.HelloWorld

import org.broadinstitute.gatk.queue.QScript

import org.broadinstitute.gatk.utils.jna.lsf.v7_0_6.LibBat
import org.broadinstitute.gatk.utils.jna.lsf.v7_0_6.LibBat.submit
import org.broadinstitute.gatk.utils.jna.lsf.v7_0_6.LibLsf

class HelloWorld extends QScript {
  def script = {
    add(new CommandLineFunction {
      def commandLine = "sleep 25; echo hello world"
    })

    add(new CommandLineFunction {
      def commandLine = "echo hello world 2; sleep 25"
    })
  }
}
