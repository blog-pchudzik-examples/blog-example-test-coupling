package com.pchudzik.testcoupling;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
class Product {
	private final String name;
	private final Set<Category> categories = new HashSet<>();
	private final Set<Tag> tags = new HashSet<>();

	public void addProductCategory(Category category) {
		if(categoryIsAlreadyAssigned(category)) {
			throw new IllegalStateException("Category is already assigned to product");
		}
		categories.add(category);
	}

	private boolean categoryIsAlreadyAssigned(Category category) {
		//intentional bug. Not checking parents
		return categories.contains(category) || category.getChildCategories().stream().anyMatch(this::categoryIsAlreadyAssigned);
	}

	public void removeProductCategory(Category category) {
		categories.remove(category);
		tags.removeAll(category.getTags());
	}

	public void removeTag(Tag tag) {
		tags.remove(tag);
	}

	public void addTag(Tag tag) {
		final boolean canBeAssigned = Stream.concat(getPossibleTags().stream(), getTags().stream()).anyMatch(tag::equals);
		if(!canBeAssigned) {
			throw new IllegalStateException(String.format("Can not assign tag %s to %s. Tag is not possible for category.", tag, this));
		}
		tags.add(tag);
	}

	public Set<Tag> getTags() {
		return Collections.unmodifiableSet(tags);
	}

	public Set<Tag> getPossibleTags() {
		return categories.stream()
				.map(Category::getTags)
				.flatMap(Collection::stream)
				.filter(tag -> !tags.contains(tag))
				.collect(toSet());
	}
}
