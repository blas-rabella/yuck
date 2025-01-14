package yuck.flatzinc.test

import org.junit.*
import org.junit.experimental.categories.*

import scala.language.implicitConversions

import yuck.constraints.*
import yuck.core.*
import yuck.flatzinc.compiler.Bool2Costs2
import yuck.flatzinc.test.util.*
import yuck.test.util.ParallelTestRunner

/**
 * Tests to make sure that the global constraints provided by Yuck's library get
 * compiled correctly
 *
 * @author Michael Marte
 */
@runner.RunWith(classOf[ParallelTestRunner])
final class GlobalConstraintCompilationTest extends FrontEndTest {

    private val taskWithImplicitSolving =
        task.copy(
            solverConfiguration =
                task.solverConfiguration.copy(
                    maybeRoundLimit = Some(1)))

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasAlldifferentConstraint]))
    def testAlldifferentInt(): Unit = {
        val result = solveWithResult(taskWithImplicitSolving.copy(problemName = "alldifferent_int_test"))
        assertEq(result.numberOfConstraints[Alldistinct[_]], 1)
        assert(result.neighbourhood.isInstanceOf[AlldistinctNeighbourhood[_]])
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasAlldifferentConstraint]))
    def testAlldifferentIntReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "alldifferent_int_reif_test"))
        assertEq(result.numberOfConstraints[Alldistinct[_]], 2)
        assert(result.neighbourhood.isInstanceOf[RandomReassignmentGenerator])
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasAlldifferentConstraint]))
    def testAlldifferentSet(): Unit = {
        val result = solveWithResult(taskWithImplicitSolving.copy(problemName = "alldifferent_set_test"))
        assertEq(result.numberOfConstraints[Alldistinct[_]], 1)
        assert(result.neighbourhood.isInstanceOf[RandomReassignmentGenerator])
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasAlldifferentConstraint]))
    def testAlldifferentSetReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "alldifferent_set_reif_test"))
        assertEq(result.numberOfConstraints[Alldistinct[_]], 2)
        assert(result.neighbourhood.isInstanceOf[RandomReassignmentGenerator])
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasAlldifferentExceptConstraint]))
    def testAlldifferentExcept0(): Unit = {
        val result = solveWithResult(task.copy(problemName = "alldifferent_except_0_test"))
        assertEq(result.numberOfConstraints[AlldistinctExcept[_]], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasAlldifferentExceptConstraint]))
    def testAlldifferentExcept0Reif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "alldifferent_except_0_reif_test"))
        assertEq(result.numberOfConstraints[AlldistinctExcept[_]], 2)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasAlldifferentExceptConstraint]))
    def testAlldifferentExcept(): Unit = {
        val result = solveWithResult(task.copy(problemName = "alldifferent_except_test"))
        assertEq(result.numberOfConstraints[AlldistinctExcept[_]], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasAlldifferentExceptConstraint]))
    def testAlldifferentExceptReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "alldifferent_except_reif_test"))
        assertEq(result.numberOfConstraints[AlldistinctExcept[_]], 2)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasBinPackingConstraint]))
    def testBinPacking(): Unit = {
        val result = solveWithResult(task.copy(problemName = "bin_packing_test"))
        assertEq(result.numberOfConstraints[BinPacking[_]], 1)
        assertEq(result.searchVariables.size, 6)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasBinPackingConstraint]))
    def testBinPackingReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "bin_packing_reif_test"))
        assertEq(result.numberOfConstraints[BinPacking[_]], 2)
        assertEq(result.searchVariables.size, 6)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasBinPackingConstraint]))
    def testBinPackingCapa(): Unit = {
        val result = solveWithResult(task.copy(problemName = "bin_packing_capa_test"))
        assertEq(result.numberOfConstraints[BinPacking[_]], 1)
        assertEq(result.searchVariables.size, 6)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasBinPackingConstraint]))
    def testBinPackingCapaReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "bin_packing_capa_reif_test"))
        // minizinc eliminates one of the bin_packing_load constraints :-)
        assertEq(result.numberOfConstraints[BinPacking[_]], 1)
        assertEq(result.searchVariables.size, 6)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasBinPackingConstraint]))
    def testBinPackingLoadWithUnboundedLoads(): Unit = {
        val result = solveWithResult(task.copy(problemName = "bin_packing_load_test_with_unbounded_loads"))
        assertEq(result.numberOfConstraints[BinPacking[_]], 1)
        assertEq(result.searchVariables.size, 6)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasBinPackingConstraint]))
    def testBinPackingLoadWithEqualLoads(): Unit = {
        val result = solveWithResult(task.copy(problemName = "bin_packing_load_test_with_equal_loads"))
        assertEq(result.numberOfConstraints[BinPacking[_]], 1)
        assertEq(result.searchVariables.size, 6)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasBinPackingConstraint]))
    def testBinPackingLoadWithSharedLoads(): Unit = {
        val result = solveWithResult(task.copy(problemName = "bin_packing_load_test_with_shared_loads"))
        assertEq(result.numberOfConstraints[BinPacking[_]], 2)
        assertEq(result.searchVariables.size, 6)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasBinPackingConstraint]))
    def testBinPackingLoadWithEqualBins(): Unit = {
        val result = solveWithResult(task.copy(problemName = "bin_packing_load_test_with_equal_bins"))
        assertEq(result.numberOfConstraints[BinPacking[_]], 1)
        assertEq(result.searchVariables.size, 5)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasBinPackingConstraint]))
    def testBinPackingLoadReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "bin_packing_load_reif_test"))
        assertEq(result.numberOfConstraints[BinPacking[_]], 2)
        assertEq(result.searchVariables.size, 6)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasBinPackingConstraint]))
    def testBinPackingLoadFn(): Unit = {
        val result = solveWithResult(task.copy(problemName = "bin_packing_load_fn_test"))
        assertEq(result.numberOfConstraints[BinPacking[_]], 1)
        assertEq(result.searchVariables.size, 6)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCircuitConstraint]))
    def testCircuit(): Unit = {
        val result = solveWithResult(task.copy(problemName = "circuit_test"))
        assertEq(result.numberOfConstraints[Circuit], 1)
        assert(result.neighbourhood.isInstanceOf[CircuitNeighbourhood])
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCircuitConstraint]))
    def testCircuitReif(): Unit = {
        // Gecode does not provide a decomposition for circuit_reif, so we cannot verify the solution.
        val result = solveWithResult(task.copy(problemName = "circuit_reif_test", verifySolution = false))
        assertEq(result.numberOfConstraints[Circuit], 2)
        assert(result.neighbourhood.isInstanceOf[RandomReassignmentGenerator])
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountEqBool(): Unit = {
        testCount("count_eq_bool_test", EqRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountEqBoolReif(): Unit = {
        testCountReif("count_eq_bool_reif_test", EqRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountFnBool(): Unit = {
        testCountFn("count_fn_bool_test")
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountEqInt(): Unit = {
        testCount("count_eq_int_test", EqRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountEqIntReif(): Unit = {
        testCountReif("count_eq_int_reif_test", EqRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountFnInt(): Unit = {
        testCountFn("count_fn_int_test")
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountEqSet(): Unit = {
        testCount("count_eq_set_test", EqRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountEqSetReif(): Unit = {
        testCountReif("count_eq_set_reif_test", EqRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountFnSet(): Unit = {
        testCountFn("count_fn_set_test")
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountNeqBool(): Unit = {
        testCount("count_neq_bool_test", NeRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountNeqBoolReif(): Unit = {
        testCountReif("count_neq_bool_reif_test", NeRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountNeqInt(): Unit = {
        testCount("count_neq_int_test", NeRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountNeqIntReif(): Unit = {
        testCountReif("count_neq_int_reif_test", NeRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountNeqSet(): Unit = {
        testCount("count_neq_set_test", NeRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountNeqSetReif(): Unit = {
        testCountReif("count_neq_set_reif_test", NeRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountLeqBool(): Unit = {
        testCount("count_leq_bool_test", LeRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountLeqBoolReif(): Unit = {
        testCountReif("count_leq_bool_reif_test", LeRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountLeqInt(): Unit = {
        testCount("count_leq_int_test", LeRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountLeqIntReif(): Unit = {
        testCountReif("count_leq_int_reif_test", LeRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountLeqSet(): Unit = {
        testCount("count_leq_set_test", LeRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountLeqSetReif(): Unit = {
        testCountReif("count_leq_set_reif_test", LeRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountLtBool(): Unit = {
        testCount("count_lt_bool_test", LtRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountLtBoolReif(): Unit = {
        testCountReif("count_lt_bool_reif_test", LtRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountLtInt(): Unit = {
        testCount("count_lt_int_test", LtRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountLtIntReif(): Unit = {
        testCountReif("count_lt_int_reif_test", LtRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountLtSet(): Unit = {
        testCount("count_lt_set_test", LtRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountLtSetReif(): Unit = {
        testCountReif("count_lt_set_reif_test", LtRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountGeqBool(): Unit = {
        testCount("count_geq_bool_test", LeRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountGeqBoolReif(): Unit = {
        testCountReif("count_geq_bool_reif_test", LeRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountGeqInt(): Unit = {
        testCount("count_geq_int_test", LeRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountGeqIntReif(): Unit = {
        testCountReif("count_geq_int_reif_test", LeRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountGeqSet(): Unit = {
        testCount("count_geq_set_test", LeRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountGeqSetReif(): Unit = {
        testCountReif("count_geq_set_reif_test", LeRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountGtBool(): Unit = {
        testCount("count_gt_bool_test", LtRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountGtBoolReif(): Unit = {
        testCountReif("count_gt_bool_reif_test", LtRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountGtInt(): Unit = {
        testCount("count_gt_int_test", LtRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountGtIntReif(): Unit = {
        testCountReif("count_gt_int_reif_test", LtRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountGtSet(): Unit = {
        testCount("count_gt_set_test", LtRelation)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCountConstraint]))
    def testCountGtSetReif(): Unit = {
        testCountReif("count_gt_set_reif_test", LtRelation)
    }

    private def testCount(problemName: String, relation: OrderingRelation): Unit = {
        // TODO Enable verification after release of MiniZinc 2.7.0!
        val verifySolution = ! problemName.contains("set")
        val result = solveWithResult(task.copy(problemName = problemName, verifySolution = verifySolution))
        assertEq(result.channelVariables.filter(wasIntroducedByMiniZincCompiler).size, 0)
        assertEq(result.space.numberOfConstraints, 6)
        assertEq(result.numberOfConstraints[CountConst[_]], 1)
        assertEq(result.numberOfConstraints[CountVar[_]], 1)
        relation match {
            case EqRelation => assertEq(result.numberOfConstraints[Contains], 1)
                               assertEq(result.numberOfConstraints[Eq[_]], 1)
            case NeRelation => assertEq(result.numberOfConstraints[Ne[_]], 2)
            case LeRelation => assertEq(result.numberOfConstraints[Le[_]], 2)
            case LtRelation => assertEq(result.numberOfConstraints[Lt[_]], 2)
        }
        assertEq(result.numberOfConstraints[Conjunction], 1)
        assertEq(result.numberOfConstraints[SatisfactionGoalTracker], 1)
    }

    private def testCountReif(problemName: String, relation: OrderingRelation): Unit = {
        // TODO Enable verification after release of MiniZinc 2.7.0!
        val result = solveWithResult(task.copy(problemName = problemName, verifySolution = !problemName.contains("set")))
        assertEq(result.channelVariables.filter(wasIntroducedByMiniZincCompiler).size, 0)
        assertEq(result.space.numberOfConstraints, 7)
        assertEq(result.numberOfConstraints[CountConst[_]], 1)
        assertEq(result.numberOfConstraints[CountVar[_]], 1)
        relation match {
            case EqRelation => assertEq(result.numberOfConstraints[Eq[_]], 2)
            case NeRelation => assertEq(result.numberOfConstraints[Ne[_]], 2)
            case LeRelation => assertEq(result.numberOfConstraints[Le[_]], 2)
            case LtRelation => assertEq(result.numberOfConstraints[Lt[_]], 2)
        }
        assertEq(result.numberOfConstraints[Or], 1)
        assertEq(result.numberOfConstraints[Conjunction], 1)
        assertEq(result.numberOfConstraints[SatisfactionGoalTracker], 1)
    }

    private def testCountFn(problemName: String): Unit = {
        // TODO Enable verification after release of MiniZinc 2.7.0!
        val verifySolution = !problemName.contains("set")
        val result = solveWithResult(task.copy(problemName = problemName, verifySolution = verifySolution))
        assertEq(result.channelVariables.filter(wasIntroducedByMiniZincCompiler).size, 0)
        assertEq(result.space.numberOfConstraints, 6)
        assertEq(result.numberOfConstraints[CountConst[_]], 1)
        assertEq(result.numberOfConstraints[CountVar[_]], 1)
        assertEq(result.numberOfConstraints[Contains], 2)
        assertEq(result.numberOfConstraints[Conjunction], 1)
        assertEq(result.numberOfConstraints[SatisfactionGoalTracker], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCumulativeConstraint]))
    def testCumulative(): Unit = {
        val result = solveWithResult(task.copy(problemName = "cumulative_test"))
        assertEq(result.numberOfConstraints[Cumulative], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCumulativeConstraint]))
    def testCumulativeReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "cumulative_reif_test"))
        assertEq(result.numberOfConstraints[Cumulative], 2)
    }

    @Test
    @Category(Array(classOf[MinimizationProblem], classOf[HasCircuitConstraint], classOf[HasDeliveryConstraint]))
    def testDeliveryWithWaiting(): Unit = {
        val result = solveWithResult(task.copy(problemName = "delivery_test_with_waiting", maybeOptimum = Some(378)))
        assertEq(result.numberOfConstraints[Circuit], 1)
        assertEq(result.numberOfConstraints[Delivery[_]], 1)
        assertEq(result.numberOfConstraints[Eq[_]], 0)
        assert(result.neighbourhood.isInstanceOf[CircuitNeighbourhood])
        assertEq(result.quality.asInstanceOf[IntegerValue].value, 378)
    }

    @Test
    @Category(Array(classOf[MinimizationProblem], classOf[HasCircuitConstraint], classOf[HasDeliveryConstraint]))
    def testDeliveryWithoutWaiting(): Unit = {
        val result = solveWithResult(task.copy(problemName = "delivery_test_without_waiting", dataAssignments = Map(("MaxKToMinKRatio", "1")), maybeOptimum = Some(669), maybeTargetObjectiveValue = Some(700)))
        assertEq(result.numberOfConstraints[Circuit], 1)
        assertEq(result.numberOfConstraints[Delivery[_]], 2)
        assertEq(result.numberOfConstraints[Eq[_]], 0)
        assert(result.neighbourhood.isInstanceOf[CircuitNeighbourhood])
        assertLe(result.quality.asInstanceOf[IntegerValue].value, 700L)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCircuitConstraint], classOf[HasDeliveryConstraint]))
    def testDeliveryReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "delivery_reif_test"))
        assertEq(result.numberOfConstraints[Circuit], 1)
        assertEq(result.numberOfConstraints[Delivery[_]], 1)
        assertEq(result.numberOfConstraints[Eq[_]], 0)
        assert(result.neighbourhood.isInstanceOf[CircuitNeighbourhood])
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCircuitConstraint], classOf[HasDeliveryConstraint]))
    def testDeliveriesWithEqualArrivalTimes(): Unit = {
        val result = solveWithResult(task.copy(problemName = "delivery_test_with_equal_arrival_times", dataAssignments = Map(("MaxKToMinKRatio", "1"))))
        assertEq(result.numberOfConstraints[Circuit], 1)
        assertEq(result.numberOfConstraints[Delivery[_]], 2)
        assertEq(result.numberOfConstraints[Eq[_]], 1)
        assert(result.neighbourhood.isInstanceOf[CircuitNeighbourhood])
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasCircuitConstraint], classOf[HasDeliveryConstraint]))
    def testDeliveriesWithSharedArrivalTimes(): Unit = {
        val result = solveWithResult(task.copy(problemName = "delivery_test_with_shared_arrival_times"))
        assertEq(result.numberOfConstraints[Circuit], 1)
        assertEq(result.numberOfConstraints[Delivery[_]], 2)
        assertEq(result.numberOfConstraints[Eq[_]], 23)
        assert(result.neighbourhood.isInstanceOf[CircuitNeighbourhood])
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasDiffnConstraint]))
    def testDiffnNonstrict(): Unit = {
        val result = solveWithResult(task.copy(problemName = "diffn_nonstrict_test"))
        assertEq(result.numberOfConstraints[Disjoint2], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasDiffnConstraint]))
    def testDiffnNonstrictReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "diffn_nonstrict_reif_test"))
        assertEq(result.numberOfConstraints[Disjoint2], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasDiffnConstraint]))
    def testDiffnStrict(): Unit = {
        val result = solveWithResult(task.copy(problemName = "diffn_strict_test"))
        assertEq(result.numberOfConstraints[Disjoint2], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasDiffnConstraint]))
    def testDiffnStrictReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "diffn_strict_reif_test"))
        assertEq(result.numberOfConstraints[Disjoint2], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasDisjunctiveConstraint]))
    def testDisjunctiveNonstrict(): Unit = {
        val result = solveWithResult(task.copy(problemName = "disjunctive_nonstrict_test"))
        assertEq(result.numberOfConstraints[Disjoint2], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasDisjunctiveConstraint]))
    def testDisjunctiveNonstrictReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "disjunctive_nonstrict_reif_test"))
        assertEq(result.numberOfConstraints[Disjoint2], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasDisjunctiveConstraint]))
    def testDisjunctiveStrict(): Unit = {
        val result = solveWithResult(task.copy(problemName = "disjunctive_strict_test"))
        assertEq(result.numberOfConstraints[Disjoint2], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasDisjunctiveConstraint]))
    def testDisjunctiveStrictReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "disjunctive_strict_reif_test"))
        assertEq(result.numberOfConstraints[Disjoint2], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasGlobalCardinalityConstraint]))
    def testGlobalCardinality(): Unit = {
        val result = solveWithResult(task.copy(problemName = "global_cardinality_test"))
        assertEq(result.numberOfConstraints[BinPacking[_]], 1)
        assertEq(result.searchVariables.size, 3)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasGlobalCardinalityConstraint]))
    def testGlobalCardinalityReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "global_cardinality_reif_test"))
        assertEq(result.numberOfConstraints[BinPacking[_]], 1)
        assertEq(result.searchVariables.size, 3)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasGlobalCardinalityConstraint]))
    def testGlobalCardinalityFn(): Unit = {
        val result = solveWithResult(task.copy(problemName = "global_cardinality_fn_test"))
        assertEq(result.numberOfConstraints[BinPacking[_]], 1)
        assertEq(result.searchVariables.size, 3)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasGlobalCardinalityConstraint]))
    def testGlobalCardinalityClosedFn(): Unit = {
        val result = solveWithResult(task.copy(problemName = "global_cardinality_closed_fn_test"))
        assertEq(result.numberOfConstraints[BinPacking[_]], 1)
        assertEq(result.searchVariables.size, 3)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasGlobalCardinalityConstraint]))
    def testGlobalCardinalityLowUp(): Unit = {
        val result = solveWithResult(task.copy(problemName = "global_cardinality_low_up_test"))
        assertEq(result.numberOfConstraints[BinPacking[_]], 1)
        assertEq(result.searchVariables.size, 3)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasGlobalCardinalityConstraint]))
    def testGlobalCardinalityLowUpReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "global_cardinality_low_up_reif_test"))
        assertEq(result.numberOfConstraints[BinPacking[_]], 1)
        assertEq(result.searchVariables.size, 3)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasInverseConstraint]))
    def testInverse(): Unit = {
        val result = solveWithResult(task.copy(problemName = "inverse_test"))
        assertEq(result.numberOfConstraints[Inverse], 1)
        assert(result.neighbourhood.isInstanceOf[GeneralInverseNeighbourhood])
    }

    // This test verifies that Yuck's definition of fzn_inverse constrains the codomain
    // of one function to be a subset of the other function's domain.
    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasInverseConstraint]))
    def testInverseWithUnboundedSearchVariables(): Unit = {
        val result =
            solveWithResult(
                task.copy(
                    problemName = "inverse_test_with_unbounded_search_variables",
                    solverConfiguration = task.solverConfiguration.copy(runPresolver = false)))
        assertEq(result.numberOfConstraints[Inverse], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasInverseConstraint]))
    def testInverseDecomposition(): Unit = {
        val result = solveWithResult(taskWithImplicitSolving.copy(problemName = "inverse_decomposition_test"))
        assertEq(result.numberOfConstraints[Inverse], 2)
        assert(result.neighbourhood.isInstanceOf[NeighbourhoodCollection])
        assert(result.neighbourhood.asInstanceOf[NeighbourhoodCollection].children.forall(_.isInstanceOf[SimpleInverseNeighbourhood]))
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasInverseConstraint]))
    def testInverseReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "inverse_reif_test"))
        assertEq(result.numberOfConstraints[Inverse], 2)
        assert(result.neighbourhood.isInstanceOf[RandomReassignmentGenerator])
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasLexLessConstraint]))
    def testLexLessBool(): Unit = {
        val result = solveWithResult(task.copy(problemName = "lex_less_bool_test"))
        assertEq(result.numberOfConstraints[LexLess[_]], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasLexLessConstraint]))
    def testLexLessBoolReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "lex_less_bool_reif_test"))
        assertEq(result.numberOfConstraints[LexLess[_]], 2)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasLexLessEqConstraint]))
    def testLexLessEqBool(): Unit = {
        val result = solveWithResult(task.copy(problemName = "lex_lesseq_bool_test"))
        assertEq(result.numberOfConstraints[LexLessEq[_]], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasLexLessEqConstraint]))
    def testLexLessEqBoolReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "lex_lesseq_bool_reif_test"))
        assertEq(result.numberOfConstraints[LexLessEq[_]], 2)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasLexLessConstraint]))
    def testLexLessInt(): Unit = {
        val result = solveWithResult(task.copy(problemName = "lex_less_int_test"))
        assertEq(result.numberOfConstraints[LexLess[_]], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasLexLessConstraint]))
    def testLexLessIntReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "lex_less_int_reif_test"))
        assertEq(result.numberOfConstraints[LexLess[_]], 2)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasLexLessEqConstraint]))
    def testLexLessEqInt(): Unit = {
        val result = solveWithResult(task.copy(problemName = "lex_lesseq_int_test"))
        assertEq(result.numberOfConstraints[LexLessEq[_]], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasLexLessEqConstraint]))
    def testLexLessEqIntReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "lex_lesseq_int_reif_test"))
        assertEq(result.numberOfConstraints[LexLessEq[_]], 2)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasLexLessConstraint]))
    def testLexLessSet(): Unit = {
        val result = solveWithResult(task.copy(problemName = "lex_less_set_test"))
        assertEq(result.numberOfConstraints[LexLess[_]], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasLexLessConstraint]))
    def testLexLessSetReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "lex_less_set_reif_test"))
        assertEq(result.numberOfConstraints[LexLess[_]], 2)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasLexLessEqConstraint]))
    def testLexLessEqSet(): Unit = {
        val result = solveWithResult(task.copy(problemName = "lex_lesseq_set_test"))
        assertEq(result.numberOfConstraints[LexLessEq[_]], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasLexLessEqConstraint]))
    def testLexLessEqSetReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "lex_lesseq_set_reif_test"))
        assertEq(result.numberOfConstraints[LexLessEq[_]], 2)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasMaximumConstraint]))
    def testMaximum(): Unit = {
        solve(task.copy(problemName = "maximum_int_test"))
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasMinimumConstraint]))
    def testMinimum(): Unit = {
        solve(task.copy(problemName = "minimum_int_test"))
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasMemberConstraint]))
    def testMemberBool(): Unit = {
        testMember("member_bool_test")
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasMemberConstraint]))
    def testMemberBoolReif(): Unit = {
        testMemberReif("member_bool_reif_test")
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasMemberConstraint]))
    def testMemberInt(): Unit = {
        testMember("member_int_test")
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasMemberConstraint]))
    def testMemberIntReif(): Unit = {
        testMemberReif("member_int_reif_test")
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasMemberConstraint]))
    def testMemberSet(): Unit = {
        testMember("member_set_test")
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasMemberConstraint]))
    def testMemberSetReif(): Unit = {
        testMemberReif("member_set_reif_test")
    }

    private def testMember(problemName: String): Unit = {
        testCount(problemName, LeRelation)
    }

    private def testMemberReif(problemName: String): Unit = {
        testCountReif(problemName, LeRelation)
    }

    @Test
    @Category(Array(classOf[MaximizationProblem], classOf[HasNValueConstraint]))
    def testNValue(): Unit = {
        val result = solveWithResult(task.copy(problemName = "nvalue_test"))
        assertEq(result.numberOfConstraints[NumberOfDistinctValues[_]], 1)
    }

    @Test
    @Category(Array(classOf[MaximizationProblem], classOf[HasNValueConstraint]))
    def testNValueReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "nvalue_reif_test"))
        assertEq(result.numberOfConstraints[NumberOfDistinctValues[_]], 2)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasRegularConstraint]))
    def testRegular(): Unit = {
        val result = solveWithResult(task.copy(problemName = "regular_test"))
        assertEq(result.numberOfConstraints[Regular], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasRegularConstraint]))
    def testRegularReif(): Unit = {
        // Gecode does not provide a decomposition for regular_reif, so we cannot verify the solution.
        val result = solveWithResult(task.copy(problemName = "regular_reif_test", verifySolution = false))
        assertEq(result.numberOfConstraints[Regular], 1)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasTableConstraint]))
    def testTableBool(): Unit = {
        val result = solveWithResult(task.copy(problemName = "table_bool_test"))
        assertEq(result.numberOfConstraints[Table[_]], 1)
        assert(result.neighbourhood.isInstanceOf[TableNeighbourhood[_]])
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasTableConstraint]))
    def testTableBoolReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "table_bool_reif_test"))
        assertEq(result.numberOfConstraints[Table[_]], 1)
        assert(result.neighbourhood.isInstanceOf[RandomReassignmentGenerator])
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasTableConstraint]))
    def testTableInt(): Unit = {
        val result = solveWithResult(task.copy(problemName = "table_int_test"))
        assertEq(result.numberOfConstraints[Table[_]], 1)
        assert(result.neighbourhood.isInstanceOf[TableNeighbourhood[_]])
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasTableConstraint]))
    def testTableIntReif(): Unit = {
        val result = solveWithResult(task.copy(problemName = "table_int_reif_test"))
        assertEq(result.numberOfConstraints[Table[_]], 1)
        assert(result.neighbourhood.isInstanceOf[RandomReassignmentGenerator])
    }

}
