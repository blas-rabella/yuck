package yuck.core

/**
 * Provides an interface for working with domains that contain integer
 * sets as elements.
 *
 * @author Michael Marte
 */
abstract class IntegerSetDomain extends OrderedDomain[IntegerSetValue] {
    final override def valueType = classOf[IntegerSetValue]
    final override def hasLb = true
    final override def hasUb = true
    final override def compare(that: OrderedDomain[IntegerSetValue]) =
        if (this.lb < that.lb) -1
        else if (this.lb > that.lb) +1
        else if (this.ub < that.ub) -1
        else if (this.ub > that.ub) +1
        else 0
    final override def ==(that: Domain[IntegerSetValue]) = (this, that) match {
        case (lhs: EmptyIntegerSetDomain.type, rhs: EmptyIntegerSetDomain.type) => true
        case (lhs: EmptyIntegerSetDomain.type, _) => false
        case (_, rhs: EmptyIntegerSetDomain.type) => false
        case (lhs: SingletonIntegerSetDomain, rhs: SingletonIntegerSetDomain) => lhs == rhs
        case (lhs: SingletonIntegerSetDomain, rhs: IntegerPowersetDomain) => lhs.base.isEmpty && rhs.base.isEmpty
        case (lhs: IntegerPowersetDomain, rhs: IntegerPowersetDomain) => lhs == rhs
        case (lhs: IntegerPowersetDomain, rhs: SingletonIntegerSetDomain) => lhs.base.isEmpty && rhs.base.isEmpty
        case _ => ???
    }
    final override def randomSubdomain(randomGenerator: RandomGenerator): IntegerSetDomain = ???
    final override def isSubsetOf(that: Domain[IntegerSetValue]): Boolean = (this, that) match {
        case (lhs: EmptyIntegerSetDomain.type, _) => true
        case (_, rhs: EmptyIntegerSetDomain.type) => false
        case (lhs: SingletonIntegerSetDomain, rhs: SingletonIntegerSetDomain) => lhs.isSubsetOf(rhs)
        case (lhs: SingletonIntegerSetDomain, rhs: IntegerPowersetDomain) => lhs.base.isSubsetOf(rhs.base)
        case (lhs: IntegerPowersetDomain, rhs: IntegerPowersetDomain) => lhs.isSubsetOf(rhs)
        case (lhs: IntegerPowersetDomain, rhs: SingletonIntegerSetDomain) => lhs.base.isEmpty && rhs.base.isEmpty
        case _ => ???
    }
    final override def intersects(that: Domain[IntegerSetValue]): Boolean = (this, that) match {
        case (lhs: EmptyIntegerSetDomain.type, _) => false
        case (_, rhs: EmptyIntegerSetDomain.type) => false
        case (lhs: SingletonIntegerSetDomain, rhs: SingletonIntegerSetDomain) => lhs.intersects(rhs)
        case (lhs: SingletonIntegerSetDomain, rhs: IntegerPowersetDomain) => lhs.base.isSubsetOf(rhs.base)
        case (lhs: IntegerPowersetDomain, rhs: IntegerPowersetDomain) => lhs.intersects(rhs)
        case (lhs: IntegerPowersetDomain, rhs: SingletonIntegerSetDomain) => rhs.base.isSubsetOf(lhs.base)
        case _ => ???
    }
    final override def intersect(that: Domain[IntegerSetValue]): IntegerSetDomain = (this, that) match {
        case (lhs: EmptyIntegerSetDomain.type, _) => lhs
        case (_, rhs: EmptyIntegerSetDomain.type) => rhs
        case (lhs: IntegerPowersetDomain, rhs: IntegerPowersetDomain) =>
            lhs.intersect(rhs)
        case (lhs: IntegerPowersetDomain, rhs: SingletonIntegerSetDomain) =>
            if (rhs.singleValue.set.isSubsetOf(lhs.base)) rhs else ???
        case (lhs: SingletonIntegerSetDomain, rhs: IntegerPowersetDomain) =>
            if (lhs.singleValue.set.isSubsetOf(rhs.base)) lhs else ???
        case (lhs: SingletonIntegerSetDomain, rhs: SingletonIntegerSetDomain) =>
            if (lhs == rhs) lhs else ???
        case _ => ???
    }
    final override def union(that: Domain[IntegerSetValue]): IntegerSetDomain = (this, that) match {
        case (lhs: EmptyIntegerSetDomain.type, rhs: IntegerSetDomain) => rhs
        case (lhs, rhs: EmptyIntegerSetDomain.type) => lhs
        case _ => ???
    }
    final override def diff(that: Domain[IntegerSetValue]): IntegerSetDomain = (this, that) match {
        case (lhs: EmptyIntegerSetDomain.type, _) => lhs
        case (lhs, rhs: EmptyIntegerSetDomain.type) => lhs
        case _ => ???
    }
}

/**
 * Companion object to IntegerSetDomain.
 *
 * @author Michael Marte
 */
object IntegerSetDomain {
}
