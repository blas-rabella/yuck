package yuck.constraints

import scala.collection.*

import yuck.core.*


/**
 * Given a linear combination in terms of scalars a[1], ..., a[n] and variables x[1], ..., x[n],
 * this constraint maintains, for all 1 <= i <= n, the distance d[i] between the current value of
 * a[i] * x[i] and its lower or upper bound, respectively, depending on the optimization mode.
 *
 * @author Michael Marte
 */
final class OptimizationGoalTracker
    [V <: NumericalValue[V]]
    (id: Id[Constraint], override val maybeGoal: Option[Goal],
     mode: OptimizationMode.Value,
     axs: immutable.IndexedSeq[AX[V]], distribution: Distribution)
    (implicit valueTraits: NumericalValueTraits[V])
    extends Constraint(id)
{

    require(distribution.size == axs.size)
    require(axs.forall(_.a.toLong > Long.MinValue)) // see computeFrequency

    override def toString = "optimizationGoalTracker([%s], %s)".format(axs.mkString(", "), distribution)

    override def inVariables = axs.view.map(_.x)
    override def outVariables = Nil

    private val indexMap: immutable.Map[AnyVariable, (Int, AX[V])] =
        axs.indices.iterator.map(i => (axs(i).x, (i, axs(i)))).toMap

    override def initialize(now: SearchState) = {
        distribution.clear()
        for (i <- axs.indices) {
            distribution.setFrequency(i, computeFrequency(axs(i), now))
        }
        Nil
    }

    override def consult(before: SearchState, after: SearchState, move: Move) =
        Nil

    override def commit(before: SearchState, after: SearchState, move: Move) = {
        for (x <- move) {
            val (i, ax) = indexMap(x)
            distribution.setFrequency(i, computeFrequency(ax, after))
        }
        Nil
    }

    private def computeFrequency(ax: AX[V], searchState: SearchState): Long = {
        val a = ax.a.toLong
        val b = searchState.value(ax.x).toLong
        val dx = ax.x.domain
        val delta = mode match {
            case OptimizationMode.Min =>
                if (ax.a < valueTraits.zero) safeMul(-a, safeSub(dx.ub.toLong, b)) // minimize -a * (dx.ub - x)
                else safeMul(a, safeSub(b, dx.lb.toLong)) // minimize a * (x - dx.lb)
            case OptimizationMode.Max =>
                if (ax.a < valueTraits.zero) safeMul(-a, safeSub(b, dx.lb.toLong)) // minimize -a * (x - dx.lb)
                else safeMul(a, safeSub(dx.ub.toLong, b)) // minimize a * (dx.ub - x)
        }
        // delta may become negative when ax.x takes a value outside of its domain!
        abs(delta)
    }

}
