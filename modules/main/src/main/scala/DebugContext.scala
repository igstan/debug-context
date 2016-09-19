package com.eloquentix

import scala.reflect.macros.blackbox.Context

object debug {
  implicit class DebugContext(val sc: StringContext) extends AnyVal {
    def d(args: Any*): String = macro Macros.d
  }

  class Macros(val c: Context { type PrefixType = DebugContext }) {
    import c.universe._

    def d(args: c.Expr[Any]*): c.Tree = {
      // Find out the String parts surrounding the interpolated values.
      val stringContextParts = c.prefix.tree match {
        case Apply(_, List(Apply(_, parts))) => parts
        case _ => c.abort(c.enclosingPosition, "Invalid StringContext call")
      }

      val tree = stringContextParts.zip(args).foldLeft(q"${""}") { case (acc, (part, arg)) =>
        def printField(field: Symbol, parents: List[Symbol]): c.Tree = {
          val parentPath = parents
            .map { parent =>
              q"$parent"
            }
            .reduceRightOption {
              (parent, acc) => q"$parent.$acc"
            }

          println(s"parentPath: $parents $parentPath")

          val names = if (field.typeSignature.typeSymbol.asClass.isCaseClass) {
            val fields = field.typeSignature.decls.filter { sym =>
              sym.isMethod && sym.asTerm.isParamAccessor
            }

            val joinedFields = fields
              .map(f => printField(f, field :: parents))
              .reduceLeftOption((acc, f) => q"$acc + ${", "} + $f")
              .getOrElse(q"${""}")

            q"$parentPath.productPrefix + ${"("} + $joinedFields + ${")"}"
          } else {
            q"$arg.$field"
          }

          q"${field.name.decodedName.toString + " = "} + $names"
        }

        if (arg.actualType.typeSymbol.asClass.isCaseClass) {
          val fields = arg.actualType.decls.filter { sym =>
            sym.isMethod && sym.asTerm.isParamAccessor
          }

          val joinedFields = fields.map { field =>
            printField(field, List(arg.tree.symbol))
          }.reduceLeftOption { (acc, field) =>
            q"$acc + ${", "} + $field"
          }.getOrElse {
            q"${""}"
          }

          q"$acc + $part + $arg.productPrefix + ${"("} + $joinedFields + ${")"}"
        } else {
          q"$acc + $part + $arg"
        }
      }

      val a = q"$tree + ${stringContextParts.last}"

      println(show(a))

      a
    }
  }
}
