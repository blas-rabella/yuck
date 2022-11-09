package yuck.constraints.test

import org.junit.*

import yuck.constraints.*
import yuck.constraints.test.util.ConstraintTestTooling
import yuck.core.{given, *}
import yuck.test.util.UnitTest

/**
 * @author Michael Marte
 *
 */
@FixMethodOrder(runners.MethodSorters.NAME_ASCENDING)
final class MaximumTest extends UnitTest with ConstraintTestTooling {

    private val space = new Space(logger, sigint)

    private val baseDomain = IntegerRange(0, 9)
    private val xs = for (i <- 1 to 3) yield new IntegerVariable(space.nextVariableId(), "x%d".format(i), baseDomain)
    private val Seq(x1, x2, x3) = xs
    private val y = new IntegerVariable(space.nextVariableId(), "y", CompleteIntegerRange)

    @Test
    def testBasics(): Unit = {
        val constraint = new Maximum(space.nextConstraintId(), null, xs, y)
        assertEq(constraint.toString, "y = max([x1, x2, x3])")
        assertEq(constraint.inVariables.size, xs.size)
        assertEq(constraint.inVariables.toSet, xs.toSet)
        assertEq(constraint.outVariables.size, 1)
        assertEq(constraint.outVariables.head, y)
    }

    private def testPropagation(xs: Seq[IntegerVariable]): Unit = {
        space.post(new Maximum(space.nextConstraintId(), null, xs, y))
        runScenario(
            TestScenario(
                space,
                Propagate("root-node propagation", Nil, List(y << baseDomain)),
                Propagate("reduce domains of x1 and x2", List(x1 << (1, 3), x2 << (2, 5), x3 << (0, 6)), List(y << (2, 6))),
                Propagate("reduce domain of y", List(y << (0, 2)), List(x1 << (1, 2), x2 << (2, 2), x3 << (0, 2)))))
    }

    @Test
    def testPropagation(): Unit = {
        testPropagation(xs)
    }

    @Test
    def testHandlingOfDuplicateVariablesInPropagation(): Unit = {
        testPropagation(List(x1, x2, x2, x3))
    }

    private def testCostComputation(xs: Seq[IntegerVariable]): Unit = {
        space.post(new Maximum(space.nextConstraintId(), null, xs, y))
        runScenario(
            TestScenario(
                space,
                Initialize("1", x1 << 1, x2 << 2, x3 << 1, y << 2),
                Initialize("2", x1 << 1, x2 << 2, x3 << 3, y << 3),
                Consult("1", x1 << 4, y << 4),
                Consult("2", x1 << 5, x3 << 6, y << 6),
                ConsultAndCommit("1", x3 << 2, y << 2),
                ConsultAndCommit("2", x1 << 6, x2 << 5, y << 6)))
    }

    @Test
    def testCostComputation(): Unit = {
        testCostComputation(xs)
    }

    @Test
    def testHandlingOfDuplicateVariablesInCostComputation(): Unit = {
        testCostComputation(List(x1, x2, x2, x3))
    }

}
