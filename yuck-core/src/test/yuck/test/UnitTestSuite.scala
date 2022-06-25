package yuck.test

import org.junit._
import yuck.annealing.test.ProgressiveTighteningTest
import yuck.constraints.test.ConstraintTestSuite
import yuck.core.test.{DistributionTest, DomainTestSuite, FenwickTreeTest, NeighbourhoodTestSuite, ObjectiveTestSuite, ProbabilityTest, PropagationEffectsTest, RandomGeneratorTest, SolverTest, SpaceTest, ValueTestSuite, VariableTest}
import yuck.test.util.ParallelTestSuiteRunner
import yuck.util.alg.rtree.test.{RTreeIntakeTest, RTreeTransactionTest}

@runner.RunWith(classOf[ParallelTestSuiteRunner])
@runners.Suite.SuiteClasses(
    Array(
        classOf[ProbabilityTest],
        classOf[RandomGeneratorTest],
        classOf[ValueTestSuite],
        classOf[DomainTestSuite],
        classOf[VariableTest],
        classOf[PropagationEffectsTest],
        classOf[SpaceTest],
        classOf[ObjectiveTestSuite],
        classOf[SolverTest],
        classOf[FenwickTreeTest],
        classOf[DistributionTest],
        classOf[ConstraintTestSuite],
        classOf[NeighbourhoodTestSuite],
        classOf[ProgressiveTighteningTest],
        classOf[RTreeIntakeTest],
        classOf[RTreeTransactionTest]))
final class UnitTestSuite