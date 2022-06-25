package yuck.test
import org.junit._
import yuck.test.util.ParallelTestSuiteRunner
import yuck.util.alg.rtree.test.{RTreeIntakeTest, RTreeTransactionTest}
@runner.RunWith(classOf[ParallelTestSuiteRunner])
@runners.Suite.SuiteClasses(
  Array(
    classOf[RTreeIntakeTest],
    classOf[RTreeTransactionTest]))
final class UnitTestSuite