package yuck.flatzinc.test

import org.junit.*
import org.junit.experimental.categories.*

import scala.language.implicitConversions
import scala.reflect.ClassTag

import yuck.constraints.*
import yuck.core.{*, given}
import yuck.flatzinc.compiler.{Bool2Int1, LevelWeightMaintainer, VariableWithInfiniteDomainException}
import yuck.flatzinc.test.util.*
import yuck.test.util.ParallelTestRunner

/**
 * Tests that cover edge cases and rarely used features of the FlatZinc language
 *
 * @author Michael Marte
 */
@runner.RunWith(classOf[ParallelTestRunner])
final class FlatZincBaseTest extends FrontEndTest {

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testArrayBoolAndWithDuplicateVariable(): Unit = {
        val result = solveWithResult(task.copy(sourceFormat = FlatZinc, problemName = "array_bool_and_test_with_duplicate_variable", solverConfiguration = task.solverConfiguration.copy(runPresolver = false)))
        assertEq(result.space.searchVariables.map(_.name), Set("x", "y"))
        assert(result.space.problemParameters.isEmpty)
        assertEq(result.space.channelVariables.size, 2)
        assertEq(result.space.channelVariables.count(wasIntroducedByYuck), 2)
        assertEq(result.space.numberOfConstraints, 3)
        assertEq(result.space.numberOfConstraints[And], 1)
        assertEq(result.space.numberOfConstraints[Conjunction], 1)
        assertEq(result.space.numberOfConstraints[SatisfactionGoalTracker], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testArrayBoolOrWithDuplicateVariable(): Unit = {
        val result = solveWithResult(task.copy(sourceFormat = FlatZinc, problemName = "array_bool_or_test_with_duplicate_variable", solverConfiguration = task.solverConfiguration.copy(runPresolver = false)))
        assertEq(result.space.searchVariables.map(_.name), Set("x", "y"))
        assert(result.space.problemParameters.isEmpty)
        assertEq(result.space.channelVariables.size, 2)
        assertEq(result.space.channelVariables.count(wasIntroducedByYuck), 2)
        assertEq(result.space.numberOfConstraints, 3)
        assertEq(result.space.numberOfConstraints[Or], 1)
        assertEq(result.space.numberOfConstraints[Conjunction], 1)
        assertEq(result.space.numberOfConstraints[SatisfactionGoalTracker], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testVarArrayAccess(): Unit = {
        val result = solveWithResult(task.copy(problemName = "var_array_access"))
        assertEq(result.space.numberOfConstraints[ElementVar[_]], 3)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testConstArrayAccess(): Unit = {
        val result = solveWithResult(task.copy(problemName = "const_array_access"))
        assertEq(result.space.numberOfConstraints[ElementConst[_]], 3)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testArrayAccessWhereResultMustEqualIndex(): Unit = {
        val result = solveWithResult(task.copy(problemName = "array_access_where_result_must_equal_index"))
        assertEq(result.space.numberOfConstraints[ElementVar[_]], 1)
        assertEq(result.space.numberOfConstraints[Eq[_]], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testArrayAccessWhereIndexIsChannelVariable(): Unit = {
        solve(task.copy(problemName = "array_access_where_index_is_channel_variable"))
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testArrayAccessWithConstrainedIndexVariables(): Unit = {
        val result = solveWithResult(task.copy(problemName = "array_access_with_constrained_index_variables"))
        assertEq(result.space.searchVariables.size, 12)
        assert(! result.space.searchVariables.exists(_.name == "x[5]"))
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testIfThenElseBool(): Unit = {
        testIfThenElse("if_then_else_bool_test")
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testIfThenElseBoolWithConstCondition(): Unit = {
        testIfThenElseWithConstCondition("if_then_else_bool_test_with_const_condition")
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testIfThenElseBoolWithEqualAlternatives(): Unit = {
        testIfThenElseWithEqualAlternatives("if_then_else_bool_test_with_equal_alternatives")
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testIfThenElseVarBool(): Unit = {
        testIfThenElseVar[BooleanValue]("if_then_else_var_bool_test")
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testIfThenElseVarBoolWithConstCondition(): Unit = {
        testIfThenElseVarWithConstCondition("if_then_else_var_bool_test_with_const_condition")
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testIfThenElseInt(): Unit = {
        testIfThenElse("if_then_else_int_test")
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testIfThenElseIntToBool2Int(): Unit = {
        val result = solveWithResult(task.copy(problemName = "if_then_else_int_to_bool2int_test"))
        assertEq(result.space.searchVariables.map(_.name), Set("c", "y"))
        assertEq(result.space.channelVariables.size, 3)
        assertEq(result.space.channelVariables.count(wasIntroducedByMiniZincCompiler), 1)
        assertEq(result.space.channelVariables.count(wasIntroducedByYuck), 2)
        assertEq(result.space.numberOfConstraints, 4)
        assertEq(result.space.numberOfConstraints[Bool2Int1], 1)
        assertEq(result.space.numberOfConstraints[LinearConstraint[_]], 1)
        assertEq(result.space.numberOfConstraints[Conjunction], 1)
        assertEq(result.space.numberOfConstraints[SatisfactionGoalTracker], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testIfThenElseIntWithConstCondition(): Unit = {
        testIfThenElseWithConstCondition("if_then_else_int_test_with_const_condition")
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testIfThenElseIntWithEqualAlternatives(): Unit = {
        testIfThenElseWithEqualAlternatives("if_then_else_int_test_with_equal_alternatives")
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testIfThenElseVarInt(): Unit = {
        testIfThenElseVar[IntegerValue]("if_then_else_var_int_test")
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testIfThenElseVarIntWithConstCondition(): Unit = {
        testIfThenElseVarWithConstCondition("if_then_else_var_int_test_with_const_condition")
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testIfThenElseSet(): Unit = {
        testIfThenElse("if_then_else_set_test")
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testIfThenElseSetWithConstCondition(): Unit = {
        testIfThenElseWithConstCondition("if_then_else_set_test_with_const_condition")
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testIfThenElseSetWithEqualAlternatives(): Unit = {
        testIfThenElseWithEqualAlternatives("if_then_else_set_test_with_equal_alternatives")
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testIfThenElseVarSet(): Unit = {
        testIfThenElseVar[IntegerSetValue]("if_then_else_var_set_test")
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testIfThenElseVarSetWithConstCondition(): Unit = {
        testIfThenElseVarWithConstCondition("if_then_else_var_set_test_with_const_condition")
    }

    private def testIfThenElse(problemName: String): Unit = {
        val result = solveWithResult(task.copy(problemName = problemName))
        assertEq(result.space.searchVariables.map(_.name), Set("c"))
        assertEq(result.space.channelVariables.size, 5)
        assertEq(result.space.channelVariables.filter(isUserDefined).map(_.name), Set("x", "y"))
        assertEq(result.space.channelVariables.count(wasIntroducedByMiniZincCompiler), 1)
        assertEq(result.space.channelVariables.count(wasIntroducedByYuck), 2)
        assertEq(result.space.numberOfConstraints, 6)
        assertEq(result.space.numberOfConstraints[Conjunction], 1)
        assertEq(result.space.numberOfConstraints[IfThenElse[_]], 2)
        assertEq(result.space.numberOfConstraints[Ne[_]], 1)
        assertEq(result.space.numberOfConstraints[Not], 1)
        assertEq(result.space.numberOfConstraints[SatisfactionGoalTracker], 1)
    }

    private def testIfThenElseWithConstCondition(problemName: String): Unit = {
        val result = solveWithResult(task.copy(problemName = problemName))
        assertEq(result.space.searchVariables, Set())
        assertEq(result.space.channelVariables.size, 1)
        assertEq(result.space.channelVariables.count(wasIntroducedByYuck), 1)
        assertEq(result.space.numberOfConstraints, 1)
        assertEq(result.space.numberOfConstraints[Conjunction], 1)
    }

    private def testIfThenElseWithEqualAlternatives(problemName: String): Unit = {
        val result = solveWithResult(task.copy(problemName = problemName))
        assertEq(result.space.searchVariables, Set())
        assertEq(result.space.channelVariables.size, 1)
        assertEq(result.space.channelVariables.count(wasIntroducedByYuck), 1)
        assertEq(result.space.numberOfConstraints, 1)
        assertEq(result.space.numberOfConstraints[Conjunction], 1)
    }

    private def testIfThenElseVar[V <: Value[V]](problemName: String)(using valueTraits: ValueTraits[V]): Unit = {
        val booleanCase = valueTraits == BooleanValueTraits
        val result = solveWithResult(task.copy(problemName = problemName))
        assertEq(result.space.searchVariables.map(_.name), Set("c", "u", "v"))
        assertEq(result.space.channelVariables.size, if booleanCase then 5 else 6)
        assertEq(result.space.channelVariables.filter(isUserDefined).map(_.name), Set("x", "y"))
        assertEq(result.space.channelVariables.count(wasIntroducedByMiniZincCompiler), 1)
        assertEq(result.space.channelVariables.count(wasIntroducedByYuck), if booleanCase then 2 else 3)
        assertEq(result.space.numberOfConstraints, if booleanCase then 6 else 7)
        assertEq(result.space.numberOfConstraints[Conjunction], 1)
        assertEq(result.space.numberOfConstraints[IfThenElse[_]], 2)
        assertEq(result.space.numberOfConstraints[Ne[_]], 1)
        assertEq(result.space.numberOfConstraints[Not], 1)
        assertEq(result.space.numberOfConstraints[SatisfactionGoalTracker], 1)
        valueTraits match {
            case BooleanValueTraits =>
            case IntegerValueTraits =>
                assertEq(result.space.numberOfConstraints[Contains], 1)
            case IntegerSetValueTraits =>
                assertEq(result.space.numberOfConstraints[Subset], 1)
        }
    }

    private def testIfThenElseVarWithConstCondition(problemName: String): Unit = {
        val result = solveWithResult(task.copy(problemName = problemName))
        assertEq(result.space.searchVariables.map(_.name), Set("u", "v"))
        assertEq(result.space.channelVariables.size, 2)
        assertEq(result.space.channelVariables.count(wasIntroducedByYuck), 2)
        assertEq(result.space.numberOfConstraints, 3)
        assertEq(result.space.numberOfConstraints[Conjunction], 1)
        assertEq(result.space.numberOfConstraints[Ne[_]], 1)
        assertEq(result.space.numberOfConstraints[SatisfactionGoalTracker], 1)
    }

    @Test
    @Category(Array(classOf[MinimizationProblem]))
    def testSetIntersection(): Unit = {
        testSetOperation[SetIntersection]("set_intersect_test")
    }

    @Test
    @Category(Array(classOf[MinimizationProblem]))
    def testSetUnion(): Unit = {
        testSetOperation[SetUnion]("set_union_test")
    }

    @Test
    @Category(Array(classOf[MinimizationProblem]))
    def testSetDiff(): Unit = {
        testSetOperation[SetDifference]("set_diff_test")
    }

    @Test
    @Category(Array(classOf[MinimizationProblem]))
    def testSetSymdiff(): Unit = {
        testSetOperation[SymmetricalSetDifference]("set_symdiff_test")
    }

    private def testSetOperation[T <: Constraint](problemName: String)(using classTag: ClassTag[T]): Unit = {
        val result = solveWithResult(task.copy(problemName = problemName, verificationFrequency = VerifyEverySolution))
        assertEq(result.space.searchVariables.filterNot(wasIntroducedByYuck).map(_.name), Set("u", "v", "w"))
        assertEq(result.space.channelVariables.size, 8)
        assertEq(result.space.channelVariables.filter(isUserDefined).map(_.name), Set("uv", "vw"))
        assertEq(result.space.channelVariables.count(wasIntroducedByMiniZincCompiler), 3)
        assertEq(result.space.channelVariables.count(wasIntroducedByYuck), 3)
        assertEq(result.space.numberOfConstraints, 10)
        assertEq(result.space.numberOfConstraints[Conjunction], 1)
        assertEq(result.space.numberOfConstraints[LevelWeightMaintainer], 1)
        assertEq(result.space.numberOfConstraints[Plus[_]], 1)
        assertEq(result.space.numberOfConstraints[SatisfactionGoalTracker], 1)
        assertEq(result.space.numberOfConstraints[SetCardinality], 2)
        assertEq(
            result.space.numberOfConstraints(
                constraint => constraint.isInstanceOf[Subset] && constraint.inVariables.count(_.name == "vw") == 1),
            1)
        assertEq(result.space.numberOfConstraints(classTag.runtimeClass.isInstance), 2)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasTableConstraint]))
    def testInconsistentProblem(): Unit = {
        assertEx(
            solve(task.copy(problemName = "empty_table_int_test")),
            classOf[InconsistentProblemException])
    }

    @Test
    @Category(Array(classOf[MinimizationProblem]))
    def testMinimizationOfSumWithNegativeAddends(): Unit = {
        val result = solveWithResult(task.copy(problemName = "minimization_of_sum_with_negative_addends"))
        assertEq(result.quality, Zero)
    }

    @Test
    @Category(Array(classOf[MinimizationProblem]))
    def testMinimizationProblemWithBoundedDanglingObjectiveVariable(): Unit = {
        val result = solveWithResult(task.copy(problemName = "minimization_problem_with_bounded_dangling_objective_variable"))
        val x = result.objective.objectiveVariables(1).asInstanceOf[IntegerVariable]
        assert(result.space.isDanglingVariable(x))
        assertEq(result.assignment.value(x), IntegerValue(-1000))
    }

    @Test
    @Category(Array(classOf[MinimizationProblem]))
    def testMinimizationProblemWithUnboundedDanglingObjectiveVariable(): Unit = {
        // We cannot verify the solution because Gecode does not support 64 bit integers.
        val result = solveWithResult(task.copy(problemName = "minimization_problem_with_unbounded_dangling_objective_variable", verificationFrequency = NoVerification))
        val x = result.objective.objectiveVariables(1).asInstanceOf[IntegerVariable]
        assert(result.space.isDanglingVariable(x))
        assertEq(result.assignment.value(x), IntegerValueTraits.minValue)
    }

    @Test
    @Category(Array(classOf[MinimizationProblem], classOf[HasAlldifferentConstraint]))
    def testMinimizationProblemWithImplicitlyConstrainedObjectiveVariable(): Unit = {
        val result = solveWithResult(task.copy(problemName = "minimization_problem_with_implicitly_constrained_objective_variable"))
        val x = result.objective.objectiveVariables(1).asInstanceOf[IntegerVariable]
        assert(result.space.isImplicitlyConstrainedSearchVariable(x))
        assertEq(result.assignment.value(x), One)
    }

    @Test
    @Category(Array(classOf[MaximizationProblem]))
    def testMaximizationProblemWithBoundedDanglingObjectiveVariable(): Unit = {
        val result = solveWithResult(task.copy(problemName = "maximization_problem_with_bounded_dangling_objective_variable"))
        val x = result.objective.objectiveVariables(1).asInstanceOf[IntegerVariable]
        assert(result.space.isDanglingVariable(x))
        assertEq(result.assignment.value(x), IntegerValue(1000))
    }

    @Test
    @Category(Array(classOf[MaximizationProblem]))
    def testMaximizationProblemWithUnboundedDanglingObjectiveVariable(): Unit = {
        // We cannot verify the solution because Gecode does not support 64 bit integers.
        val result = solveWithResult(task.copy(problemName = "maximization_problem_with_unbounded_dangling_objective_variable", verificationFrequency = NoVerification))
        val x = result.objective.objectiveVariables(1).asInstanceOf[IntegerVariable]
        assert(result.space.isDanglingVariable(x))
        assertEq(result.assignment.value(x), IntegerValueTraits.maxValue)
    }

    @Test
    @Category(Array(classOf[MaximizationProblem], classOf[HasAlldifferentConstraint]))
    def testMaximizationProblemWithImplicitlyConstrainedObjectiveVariable(): Unit = {
        val result = solveWithResult(task.copy(problemName = "maximization_problem_with_implicitly_constrained_objective_variable"))
        val x = result.objective.objectiveVariables(1).asInstanceOf[IntegerVariable]
        assert(result.space.isImplicitlyConstrainedSearchVariable(x))
        assertEq(result.assignment.value(x), IntegerValue(512))
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testProblemWithBoundedDanglingVariable(): Unit = {
        val result = solveWithResult(task.copy(problemName = "problem_with_bounded_dangling_variable"))
        val x = result.compilerResult.arrays("x")
        assertEq(x.size, 4)
        assertEq(result.space.searchVariables, Set(x(0), x(1)))
        assertEq(result.space.channelVariables.filter(isUserDefined), Set(x(2)))
        assert(x(3).domain.isFinite)
        assertEq(result.space.numberOfConstraints, 4)
        assertEq(result.space.numberOfConstraints[Conjunction], 1)
        assertEq(result.space.numberOfConstraints[Contains], 1)
        assertEq(result.space.numberOfConstraints[Plus[_]], 1)
        assertEq(result.space.numberOfConstraints[SatisfactionGoalTracker], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testProblemWithUnboundedDanglingVariable(): Unit = {
        assertEx(
            solve(task.copy(problemName = "problem_with_unbounded_dangling_variable")),
            classOf[VariableWithInfiniteDomainException])
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testProblemWithBoundedIrrelevantSearchVariable(): Unit = {
        val result = solveWithResult(task.copy(problemName = "problem_with_bounded_irrelevant_search_variable"))
        assertEq(result.space.searchVariables.map(_.name), Set("x"))
        assertEq(result.space.channelVariables.filter(isUserDefined).map(_.name), Set("r"))
        assertEq(result.space.numberOfConstraints, 2)
        assertEq(result.space.numberOfConstraints[Conjunction], 1)
        assertEq(result.space.numberOfConstraints[Eq[_]], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testProblemWithUnboundedIrrelevantSearchVariable(): Unit = {
        assertEx(
            solve(task.copy(problemName = "problem_with_unbounded_irrelevant_search_variable")),
            classOf[VariableWithInfiniteDomainException])
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testProblemWithUnboundedRelevantSearchVariable(): Unit = {
        assertEx(
            solve(task.copy(problemName = "problem_with_unbounded_relevant_search_variable")),
            classOf[VariableWithInfiniteDomainException])
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testBitSetCompilation(): Unit = {
        val result = solveWithResult(task.copy(problemName = "bitset_compilation_test"))
        val l = result.compilerResult.arrays("l")
        assertEq(l.size, 4)
        assert(l(0).domain.isInstanceOf[SingletonIntegerSetDomain])
        assertEq(l(0).domain.asInstanceOf[SingletonIntegerSetDomain].base.getClass, classOf[SixtyFourBitSet])
        assertEq(l(0).domain.asInstanceOf[SingletonIntegerSetDomain].base, IntegerRange(1, 62))
        assert(l(1).domain.isInstanceOf[IntegerPowersetDomain])
        assertEq(l(1).domain.asInstanceOf[IntegerPowersetDomain].base.getClass, classOf[SixtyFourBitSet])
        assertEq(l(1).domain.asInstanceOf[IntegerPowersetDomain].base, IntegerRange(0, 63))
        assert(l(2).domain.isInstanceOf[IntegerPowersetDomain])
        assertEq(l(2).domain.asInstanceOf[IntegerPowersetDomain].base.getClass, classOf[SixtyFourBitSet])
        assertEq(l(2).domain.asInstanceOf[IntegerPowersetDomain].base, IntegerDomain(10, 11, 12, 27, 28, 29))
        assert(l(3).domain.isInstanceOf[SingletonIntegerSetDomain])
        assertEq(l(3).domain.asInstanceOf[SingletonIntegerSetDomain].base.getClass, classOf[SixtyFourBitSet])
        assertEq(l(3).domain.asInstanceOf[SingletonIntegerSetDomain].base, IntegerDomain(1, 2, 3, 7, 8, 9))
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testBitSetConversion(): Unit = {
        val result = solveWithResult(task.copy(problemName = "bitset_conversion_test"))
        val l = result.compilerResult.arrays("l")
        assertEq(l.size, 4)
        assert(l(0).domain.isInstanceOf[SingletonIntegerSetDomain])
        assertEq(l(0).domain.asInstanceOf[SingletonIntegerSetDomain].base.getClass, classOf[IntegerRange])
        assertEq(l(0).domain.asInstanceOf[SingletonIntegerSetDomain].base, IntegerRange(1, 62))
        assert(l(1).domain.isInstanceOf[IntegerPowersetDomain])
        assertEq(l(1).domain.asInstanceOf[IntegerPowersetDomain].base.getClass, classOf[IntegerRange])
        assertEq(l(1).domain.asInstanceOf[IntegerPowersetDomain].base, IntegerRange(0, 64))
        assert(l(2).domain.isInstanceOf[IntegerPowersetDomain])
        assertEq(l(2).domain.asInstanceOf[IntegerPowersetDomain].base.getClass, classOf[IntegerRangeList])
        assertEq(l(2).domain.asInstanceOf[IntegerPowersetDomain].base, IntegerDomain(10, 11, 12, 27, 28, 29))
        assert(l(3).domain.isInstanceOf[SingletonIntegerSetDomain])
        assertEq(l(3).domain.asInstanceOf[SingletonIntegerSetDomain].base.getClass, classOf[IntegerRangeList])
        assertEq(l(3).domain.asInstanceOf[SingletonIntegerSetDomain].base, IntegerDomain(1, 2, 3, 7, 8, 9))
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testProblemWithDuplicateCostVariable(): Unit = {
        val result = solveWithResult(task.copy(sourceFormat = FlatZinc, problemName = "problem_with_duplicate_cost_variable"))
        assertEq(result.space.searchVariables, Set())
        assertEq(result.space.problemParameters.size, 1)
        assertEq(result.space.channelVariables.size, 1)
        assertEq(result.space.channelVariables.count(wasIntroducedByYuck), 1)
        assertEq(result.space.numberOfConstraints, 1)
        assertEq(result.space.numberOfConstraints[Conjunction], 1)
    }

}
