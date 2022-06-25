package yuck.test

import org.junit._

import yuck.flatzinc.ast.test._
import yuck.flatzinc.parser.test._
import yuck.test.util.ParallelTestSuiteRunner

@runner.RunWith(classOf[ParallelTestSuiteRunner])
@runners.Suite.SuiteClasses(
    Array(
        classOf[FlatZincAstTest],
        classOf[FlatZincParserTest]))
final class UnitTestSuite
