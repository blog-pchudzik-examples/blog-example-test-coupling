package com.pchudzik.testcoupling

import org.junit.Before
import spock.lang.Specification

class ProductBadTest extends Specification {
	def rootCategory1
	def rootCategory2
	def child1OfRootCategory1
	def child2OfRootCategory1
	def otherChildOfRootCategory2

	def rootCategory1TagA
	def rootCategory1TagB
	def rootCategory2TagC
	def child1TagD
	def child1TagE
	def child2TagF
	def otherChildTagG

	@Before
	def setupTest() {
		rootCategory1TagA = new Tag("A")
		rootCategory1TagB = new Tag("B")
		rootCategory2TagC = new Tag("C")
		child1TagD = new Tag("D")
		child1TagE = new Tag("E")
		child2TagF = new Tag("F")
		otherChildTagG = new Tag("G")

		rootCategory1 = new Category("root1", [rootCategory1TagA, rootCategory1TagB])
		rootCategory2 = new Category("root2", [rootCategory2TagC])
		child1OfRootCategory1 = new Category("child 1 of root1", rootCategory1, [child1TagD, child1TagE])
		child2OfRootCategory1 = new Category("child 2 of root1", rootCategory1, [child2TagF])
		otherChildOfRootCategory2 = new Category("other child", rootCategory2, [otherChildTagG])
	}


	def "product can be assign to only one category in category tree branch"() {
		given:
		final product = new Product("product")
		product.addProductCategory(rootCategory1)

		when:
		product.addProductCategory(child1OfRootCategory1)

		then:
		thrown(IllegalStateException)
	}

	def "should remove all tags from product related to removed category"() {
		given:
		final product = new Product("product")
		product.addProductCategory(child1OfRootCategory1)
		product.addProductCategory(otherChildOfRootCategory2)
		product.addTag(child1OfRootCategory1.tags + otherChildOfRootCategory2)

		when:
		product.removeProductCategory(child1OfRootCategory1)

		then:
		product.tags == [otherChildTagG] as Set
	}

	def "should reject tag assignment not related to product category"() {
		given:
		final product = new Product("product")

		when:
		product.addTag(otherChildTagG)

		then:
		thrown(IllegalStateException)
	}

	def "should find all possible product tags"() {
		given:
		final product = new Product("product")
		product.addProductCategory(child1OfRootCategory1)
		product.addProductCategory(otherChildOfRootCategory2)

		expect:
		product.possibleTags == [child1TagD, child1TagE, otherChildTagG] as Set
	}
}
