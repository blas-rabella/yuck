package yuck.flatzinc.compiler

import yuck.core.*
import yuck.flatzinc.ast.{Annotation, ArrayConst, Expr, Term}

/**
 * Parses warm-start annotations and assigns the variables accordingly.
 *
 * @author Michael Marte
 */
class WarmStartAnnotationParser
    (override protected val cc: CompilationContext)
    extends CompilationPhase
{

    override def run() = {
        cc.ast.solveGoal.annotations.foreach(parseAnnotation)
        logWarmStartAssignments()
    }

    private def parseAnnotation(annotation: Annotation): Unit = {
        parseTerm(annotation.term)
    }

    private def parseTerm(term: Term): Unit = {
        term match {
            case Term("warm_start", List(varArray, valArray)) =>
                val xs = compileAnyArray(varArray)
                val ys = compileAnyArray(valArray)
                require(xs.size == ys.size)
                for ((x, y) <- xs.zip(ys)) {
                    (x, y) match {
                        case (x: BooleanVariable, y: BooleanVariable) => setValue(x, y)
                        case (x: IntegerVariable, y: IntegerVariable) => setValue(x, y)
                        case (x: IntegerSetVariable, y: IntegerSetVariable) => setValue(x, y)
                    }
                }
            case Term("warm_start_array", List(ArrayConst(array))) =>
                array.foreach(parseExpr)
            case Term("seq_search", List(ArrayConst(array))) =>
                array.foreach(parseExpr)
            case _ =>
        }
    }

    private def parseExpr(expr: Expr): Unit = {
        expr match {
            case term @ Term(_, _) => parseTerm(term)
            case _ =>
        }
    }

    private def setValue[V <: AnyValue](x: Variable[V], y: Variable[V]): Unit = {
        cc.warmStartAssignment += x -> y
        cc.space.setValue(x, y.domain.singleValue)
    }

    private def logWarmStartAssignments(): Unit = {
        cc.logger.criticalSection {
            cc.logger.withRootLogLevel(yuck.util.logging.FineLogLevel) {
                cc.logger.withLogScope("Warm-start assignments") {
                    for ((x, y) <- cc.warmStartAssignment) {
                        cc.logger.log("%s = %s".format(x, y.domain.singleValue))
                    }
                }
            }
        }
    }

}
