package yuck.flatzinc.test.vrp

import java.io.File

import org.junit._

import scala.jdk.CollectionConverters._

import yuck.flatzinc.test.util._
import yuck.test.util.ParallelParameterizedTestRunner

/**
 * Runs the Solomon CVRPTW benchmark
 *
 * @author Michael Marte
 */
@Test
@FixMethodOrder(runners.MethodSorters.NAME_ASCENDING)
@runner.RunWith(classOf[ParallelParameterizedTestRunner])
final class SolomonBenchmark(task: MiniZincTestTask) extends MiniZincBasedTest {

    @Test
    def solve: Unit = {
        super.solve(task.copy(dataAssignments = Map(("MaxKToMinKRatio", "4")), maybeMaximumNumberOfThreads = Some(1), keepFlatZincFile = false))
    }

}

/**
 * Generates test tasks from the Solomon CVRPTW benchmark instances
 *
 * @author Michael Marte
 */
object SolomonBenchmark extends VrpTestTaskFactory {

    override protected def modelFilter(file: File) = List("cvrptw_yuck.mzn").contains(file.getName)

    override protected def instanceFilter(file: File) = super.instanceFilter(file) && file.getPath.contains("Solomon")

    override protected val Results = Map(
        ("C101.25.3", 1913), ("C102.25.3", 1903), ("C103.25.3", 1903), ("C104.25.3", 1869), ("C105.25.3", 1913),
        ("C106.25.3", 1913), ("C107.25.3", 1913), ("C108.25.3", 1913), ("C109.25.3", 1913), ("C201.25.2", 2147),
        ("C202.25.2", 2147), ("C203.25.2", 2147), ("C204.25.1", 2131), ("C205.25.2", 2147), ("C206.25.2", 2147),
        ("C207.25.2", 2145), ("C208.25.2", 2145), ("R101.25.8", 6171), ("R102.25.7", 5471), ("R103.25.5", 4546),
        ("R104.25.4", 4169), ("R105.25.6", 5305), ("R106.25.5", 4654), ("R107.25.4", 4243), ("R108.25.4", 3973),
        ("R109.25.5", 4413), ("R110.25.4", 4447), ("R110.25.5", 4441), ("R111.25.4", 4288), ("R112.25.4", 3930),
        ("R201.25.4", 4633), ("R202.25.4", 4105), ("R203.25.3", 3914), ("R204.25.2", 3550), ("R205.25.3", 3930),
        ("R206.25.3", 3744), ("R207.25.3", 3616), ("R208.25.1", 3282), ("R209.25.2", 3707), ("R210.25.3", 4046),
        ("R211.25.2", 3509), ("RC101.25.4", 4611), ("RC102.25.3", 3518), ("RC103.25.3", 3328), ("RC104.25.3", 3066),
        ("RC105.25.4", 4113), ("RC106.25.3", 3455), ("RC107.25.3", 2983), ("RC108.25.3", 2945), ("RC201.25.3", 3602),
        ("RC202.25.3", 3380), ("RC203.25.3", 3269), ("RC204.25.3", 2997), ("RC205.25.3", 3380), ("RC206.25.3", 3240),
        ("RC207.25.3", 2983), ("RC208.25.2", 2691),
        ("C101.50.5", 3624), ("C102.50.5", 3614), ("C103.50.5", 3614), ("C104.50.5", 3580), ("C105.50.5", 3624),
        ("C106.50.5", 3624), ("C107.50.5", 3624), ("C108.50.5", 3624), ("C109.50.5", 3624), ("C201.50.3", 3602),
        ("C202.50.3", 3602), ("C203.50.3", 3598), ("C204.50.2", 3501), ("C205.50.3", 3598), ("C206.50.3", 3598),
        ("C207.50.3", 3596), ("C208.50.2", 3505), ("R101.50.12", 10440), ("R102.50.11", 9090), ("R103.50.9", 7729),
        ("R104.50.6", 6254), ("R105.50.9", 8993), ("R106.50.8", 7930), ("R107.50.7", 7111), ("R108.50.6", 6177),
        ("R109.50.8", 7868), ("R110.50.7", 6970), ("R111.50.7", 7072), ("R112.50.6", 6302), ("R201.50.6", 7919),
        ("R202.50.5", 6985), ("R203.50.5", 6053), ("R204.50.2", 5064), ("R205.50.4", 6901), ("R206.50.4", 6324),
        ("R207.50.3", 5755), ("R208.50.2", 4877), ("R209.50.4", 6003), ("R210.50.4", 6456), ("R211.50.3", 5355),
        ("RC101.50.8", 9440), ("RC102.50.7", 8225), ("RC103.50.6", 7109), ("RC104.50.5", 5458), ("RC105.50.8", 8553),
        ("RC106.50.6", 7232), ("RC107.50.6", 6427), ("RC108.50.6", 5981), ("RC201.50.5", 6848), ("RC202.50.5", 6136),
        ("RC203.50.4", 5553), ("RC204.50.3", 4442), ("RC205.50.5", 6302), ("RC206.50.5", 6100), ("RC207.50.4", 5586),
        ("RC208.50.3", 4767),
        ("C101.100.10", 8273), ("C102.100.10", 8273), ("C103.100.10", 8263), ("C104.100.10", 8229),
        ("C105.100.10", 8273), ("C106.100.10", 8273), ("C107.100.10", 8273), ("C108.100.10", 8273),
        ("C109.100.10", 8273), ("C201.100.3", 5891), ("C202.100.3", 5891), ("C203.100.3", 5887),
        ("C204.100.3", 5858), ("C205.100.3", 5864), ("C206.100.3", 5860), ("C207.100.3", 5858),
        ("C208.100.3", 5858), ("R101.100.20", 16377), ("R102.100.18", 14666), ("R103.100.14", 12087),
        ("R104.100.11", 9715), ("R105.100.15", 13553), ("R106.100.13", 12346), ("R107.100.11", 10646),
        ("R108.100.10", 9321), ("R109.100.13", 11469), ("R110.100.12", 10680), ("R111.100.12", 10487),
        ("R112.100.10", 9486), ("R201.100.8", 11432), ("R202.100.8", 10296), ("R203.100.6", 8708),
        ("R204.100.5", 7313), ("R205.100.5", 9498), ("R206.100.5", 8759), ("R207.100.4", 7940),
        ("R208.100.4", 7012), ("R209.100.5", 8548), ("R210.100.6", 9005), ("R211.100.4", 7467),
        ("RC101.100.15", 16198), ("RC102.100.14", 14574), ("RC103.100.11", 12580), ("RC104.100.10", 11323),
        ("RC105.100.15", 15137), ("RC106.100.12", 13727), ("RC107.100.12", 12078), ("RC108.100.11", 11142),
        ("RC201.100.9", 12618), ("RC202.100.8", 10923), ("RC203.100.5", 9237), ("RC204.100.4", 7835),
        ("RC205.100.7", 11540), ("RC206.100.7", 10511), ("RC207.100.6", 9629), ("RC208.100.4", 7761))
        .map{case (instanceName, value) => ("Solomon/" + instanceName, ObjectiveValue(value, false))}

    // keep models aligned
    private def verifyAgainstCpModel(task: MiniZincTestTask) = task.copy(verificationModelName = "cvrptw_cp")

    @runners.Parameterized.Parameters(name = "{index}: {0}")
    def parameters = tasks.map(amendKnownBestResult).map(verifyAgainstCpModel).map(Array(_)).asJava

}
