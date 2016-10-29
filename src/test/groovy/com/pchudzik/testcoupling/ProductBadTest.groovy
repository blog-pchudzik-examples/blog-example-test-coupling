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
		rootCategory1TagA = new Tag(name: "A")
		rootCategory1TagB = new Tag(name: "B")
		rootCategory2TagC = new Tag(name: "C")
		child1TagD = new Tag(name: "D")
		child1TagE = new Tag(name: "E")
		child2TagF = new Tag(name: "F")
		otherChildTagG = new Tag(name: "G")

		rootCategory1 = new Category(name: "root1", tags: [rootCategory1TagA, rootCategory1TagB])
		rootCategory2 = new Category(name: "root2", tags: [rootCategory2TagC])
		child1OfRootCategory1 = new Category(name: "child 1 of root1", parentCategory: rootCategory1, tags: [child1TagD, child1TagE])
		child2OfRootCategory1 = new Category(name: "child 2 of root1", parentCategory: rootCategory1, tags: [child2TagF])
		otherChildOfRootCategory2 = new Category(name: "other child", parentCategory: rootCategory2, tags: [otherChildTagG])
		rootCategory1.childCategories.addAll([child1OfRootCategory1, child2OfRootCategory1])
		rootCategory2.childCategories.addAll(otherChildOfRootCategory2)
	}

	def "product can be assign to only one category in category tree branch"() {
		given:
		final product = new Product("product")
		product.addProductCategory(child2OfRootCategory1)

		when:
		product.addProductCategory(rootCategory1)

		then:
		thrown(IllegalStateException)
	}

	def "should remove all tags from product related to removed category"() {
		given:
		final product = new Product("product")
		product.addProductCategory(child1OfRootCategory1)
		product.addProductCategory(otherChildOfRootCategory2)
		(child1OfRootCategory1.tags + otherChildOfRootCategory2.tags).each { product.addTag(it) }

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

	def "should exclude already assigned tag from possible tags"() {
		given:
		final product = new Product("product")
		product.addProductCategory(child1OfRootCategory1)
		product.addTag(child1TagD)

		expect:
		product.possibleTags == [child1TagE] as Set
	}
}
