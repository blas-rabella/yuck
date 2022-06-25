package yuck.core

/**
 * Objective for maximizing the value of a variable.
 *
 * @author Michael Marte
 */
final class MaximizationObjective
    [Value <: NumericalValue[Value]]
    (override val x: NumericalVariable[Value],
     maybeTargetCosts: Option[Value],
     override protected val maybeY: Option[NumericalVariable[Value]])
    (implicit valueTraits: NumericalValueTraits[Value])
    extends NumericalObjective[Value]
{
    override def toString =
        "max %s".format(x)
    override def targetCosts: Value = {
        val dx = x.domain
        if (dx.hasUb && maybeTargetCosts.isDefined) valueTraits.valueOrdering.min(dx.ub, maybeTargetCosts.get)
        else if (dx.hasUb) dx.ub
        else if (maybeTargetCosts.isDefined) maybeTargetCosts.get
        else valueTraits.maxValue
    }
    override def isOptimal(costs: Costs) = {
        val dx = x.domain
        dx.hasUb && costs.asInstanceOf[Value] >= dx.ub
    }
    override def compareCosts(lhs: Costs, rhs: Costs) =
        rhs.asInstanceOf[Value].compare(lhs.asInstanceOf[Value])
    override protected def computeDelta(before: SearchState, after: SearchState) =
        costs(before).toDouble - costs(after).toDouble
    override val optimizationMode = OptimizationMode.Max
}
