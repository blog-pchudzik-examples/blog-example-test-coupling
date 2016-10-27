package com.pchudzik.testcoupling;

import java.util.*;

import static java.util.Collections.emptyList;

public class Category {
	private final String name;

	private Category parentCategory;
	private final List<Category> childCategories = new LinkedList<>();

	private final Set<Tag> tags = new HashSet<>();

	public Category(String name, Collection<Tag> tags) {
		this(name, null, tags);
	}

	public Category(String name, Category parent) {
		this(name, parent, emptyList());
	}

	public Category(String name, Category parent, Collection<Tag> tags) {
		this.name = name;
		this.tags.addAll(tags);
		this.parentCategory = parent;
		parent.childCategories.add(this);
	}

	public Set<Tag> getTags() {
		return Collections.unmodifiableSet(tags);
	}
}
