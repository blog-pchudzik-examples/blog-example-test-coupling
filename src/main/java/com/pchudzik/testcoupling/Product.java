package com.pchudzik.testcoupling;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class Product {
	private final String name;
	private final Set<Category> categories = new HashSet<>();
	private final Set<Tag> tags = new HashSet<>();

	public void addProductCategory(Category category) {
		categories.add(category);
	}

	public void removeProductCategory(Category category) {
		categories.remove(category);
	}

	public void removeTag(Tag tag) {

	}

	public void addTag(Tag tag) {

	}

	public Set<Tag> getTags() {
		return Collections.unmodifiableSet(tags);
	}

	public Set<Tag> getPossibleTags() {
		return Collections.emptySet();
	}
}
