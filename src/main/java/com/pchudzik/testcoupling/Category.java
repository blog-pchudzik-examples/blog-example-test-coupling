package com.pchudzik.testcoupling;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
class Category {
	private String name;

	private Category parentCategory;
	private List<Category> childCategories = new LinkedList<>();

	private Set<Tag> tags = new HashSet<>();
}
