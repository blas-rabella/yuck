package yuck.core

/**
 * Provides an interface for working with typed variables.
 *
 * @author Michael Marte
 */
abstract class Variable
    [Value <: AnyValue]
    (id: Id[AnyVariable], name: String)
    extends AnyVariable(id, name)
{

    override def domain: Domain[Value]

    protected def setDomain(domain: Domain[Value]): Unit

    /**
     * Intersects the variable's domain with the given domain.
     *
     * Returns true iff the variable's domain was actually pruned.
     *
     * Throws a [[yuck.core.DomainWipeOutException DomainWipeOutException]]
     * when the variable's domain became empty.
     */
    final def pruneDomain(restriction: Domain[Value]): Boolean = {
        if (restriction != domain) {
            // We try to avoid useless and expensive intersections.
            if (domain.isSubsetOf(restriction)) {
                false
            } else {
                if (restriction.isSubsetOf(domain)) {
                    setDomain(restriction)
                } else {
                    setDomain(domain.intersect(restriction))
                }
                if (domain.isEmpty) {
                    throw new DomainWipeOutException(this)
                }
                true
            }
        } else {
            false
        }
    }

    /**
     * Replaces the variable's domain with the given domain.
     *
     * Returns true iff the variable's domain was actually relaxed.
     *
     * Throws when the new domain is not a superset of the current domain.
     */
    final def relaxDomain(relaxation: Domain[Value]): Boolean = {
        if (relaxation != domain) {
            require(
                domain.isSubsetOf(relaxation),
                "%s is not a superset of %s".format(relaxation, domain))
            require(domain.isSubsetOf(relaxation))
            setDomain(relaxation)
            true
        } else {
            false
        }
    }

    final override def createDomainRestorer = new Function0[Unit] {
        private val backup = domain
        override def apply() = {
            relaxDomain(backup)
        }
    }

    final override def hasValidValue(space: Space) =
        domain.contains(space.searchState.value(this))

    val reuseableEffect = new ReusableMoveEffectWithFixedVariable[Value](this)

    final override def randomMoveEffect(randomGenerator: RandomGenerator) = {
        reuseableEffect.a = if (domain.isSingleton) domain.singleValue else domain.randomValue(randomGenerator)
        reuseableEffect
    }

    final override def nextRandomMoveEffect(space: Space, randomGenerator: RandomGenerator) = {
        reuseableEffect.a = domain.nextRandomValue(randomGenerator, space.searchState.value(this))
        reuseableEffect
    }

    final override def nextMove(space: Space, randomGenerator: RandomGenerator) =
        new ChangeValues(space.nextMoveId, Some(nextRandomMoveEffect(space, randomGenerator)))

}
