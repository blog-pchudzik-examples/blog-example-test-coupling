package com.pchudzik.testcoupling

import spock.lang.Specification


class ProductGoodTest extends Specification {
	def "product can be assign to only one category in category tree branch"() {
		given:
		final rootCategory = new Category(name: "root")
		final childCategory = new Category(name: "child", parentCategory: rootCategory)
		rootCategory.childCategories.add(childCategory);
		final product = new Product("product")
		product.addProductCategory(childCategory)

		when:
		product.addProductCategory(rootCategory)

		then:
		thrown(IllegalStateException)
	}

	def "should remove all tags from product related to removed category"() {
		given:
		final tag1 = new Tag(name: "1")
		final otherTags = [new Tag(name: "other 1"), new Tag(name: "other 2")]
		final category = new Category(name: "category1", tags: [tag1])
		final otherCategory = new Category(name: "other category", tags: otherTags)
		final product = new Product("product")
		product.addProductCategory(category)
		product.addProductCategory(otherCategory)
		([tag1] + otherTags).each { product.addTag(it) }

		when:
		product.removeProductCategory(otherCategory)

		then:
		product.tags == [tag1] as Set
	}

	def "should reject tag assignment not related to product category"() {
		given:
		final unreleatedTag = new Tag(name: "unrelated tag")
		final product = new Product("product")

		when:
		product.addTag(unreleatedTag)

		then:
		thrown(IllegalStateException)
	}

	def "should find all possible product tags"() {
		given:
		final tag1 = new Tag(name: "1")
		final tag2 = new Tag(name: "2")
		final tag3 = new Tag(name: "3")
		final category1 = new Category(name: "1", tags: [tag1, tag2])
		final category2 = new Category(name: "2", tags: [tag3])
		final product = new Product("product")
		product.addProductCategory(category1)
		product.addProductCategory(category2)

		expect:
		product.possibleTags == [tag1, tag2, tag3] as Set
	}

	def "should exclude already assigned tag from possible tags"() {
		given:
		final alreadyAssignedTag = new Tag(name: "1")
		final notAssignedTag = new Tag(name: "2")
		final category1 = new Category(name: "1", tags: [alreadyAssignedTag, notAssignedTag])
		final product = new Product("product")
		product.addProductCategory(category1)
		product.addTag(alreadyAssignedTag)

		expect:
		product.possibleTags == [notAssignedTag] as Set
	}
}
