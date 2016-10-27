package com.pchudzik.testcoupling

import spock.lang.Specification


class ProductGoodTest extends Specification {
	def "product can be assign to only one category in category tree branch"() {
		given:
		final rootCategory = new Category("root")
		final childCategory = new Category("child", rootCategory)
		final product = new Product("product")
		product.addProductCategory(rootCategory)

		when:
		product.addProductCategory(childCategory)

		then:
		thrown(IllegalStateException)
	}

	def "should remove all tags from product related to removed category"() {
		given:
		final tag1 = new Tag("1")
		final otherTags = [new Tag("other 1"), new Tag("other 2")]
		final category = new Category("category1", [tag1])
		final otherCategory = new Category("other category", otherTags)
		final product = new Product("product")
		product.addProductCategory(category)
		product.addProductCategory(otherCategory)
		product.addTag(tag1 + otherTags)

		when:
		product.removeProductCategory(otherCategory)

		then:
		product.tags == [tag1] as Set
	}

	def "should reject tag assignment not related to product category"() {
		given:
		final unreleatedTag = new Tag("unreleated tag")
		final product = new Product("product")

		when:
		product.addTag(unreleatedTag)

		then:
		thrown(IllegalStateException)
	}

	def "should find all possible product tags"() {
		given:
		final tag1 = new Tag("1")
		final tag2 = new Tag("2")
		final tag3 = new Tag("3")
		final category1 = new Category("1", [tag1, tag2])
		final category2 = new Category("2", [tag3])
		final product = new Product("product")
		product.addProductCategory(category1)
		product.addProductCategory(category2)

		expect:
		product.possibleTags == [tag1, tag2, tag3] as Set
	}
}
