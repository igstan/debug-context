package com.eloquentix

import com.eloquentix.debug.DebugContext
import org.scalatest.{ Assertions, FunSuite }

class DebugContextSuite extends FunSuite {
  case class Foo(field1: String, field2: Int)
  case class Bar(foo: Foo)

  test("interpolates case classes") {
    val a = Foo(field1="a", field2=1)
    assert(d"$a" === "Foo(field1 = a, field2 = 1)")
  }

  test("interpolates text before") {
    val a = Foo(field1="a", field2=1)
    assert(d"before $a" === "before Foo(field1 = a, field2 = 1)")
  }

  test("interpolates text after") {
    val a = Foo(field1="a", field2=1)
    assert(d"$a after" === "Foo(field1 = a, field2 = 1) after")
  }

  test("interpolates multiple variables") {
    val a = Foo(field1="a", field2=1)
    val b = Foo(field1="b", field2=2)
    assert(d"before $a middle $b after" === "before Foo(field1 = a, field2 = 1) middle Foo(field1 = b, field2 = 2) after")
  }

  test("interpolates integers") {
    val a = 42
    assert(d"foo $a bar" === "foo 42 bar")
  }

  test("interpolates nested case classes") {
    val a = Foo(field1="a", field2=1)
    val b = Bar(foo = a)
    assert(d"$b" === "Bar(foo = Foo(field1 = a, field2 = 1))")
  }
}
