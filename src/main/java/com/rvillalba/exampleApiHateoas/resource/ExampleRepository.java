package com.rvillalba.exampleApiHateoas.resource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import com.rvillalba.exampleApiHateoas.entity.Example;

@RepositoryRestResource(path = "examples")
@Transactional
public interface ExampleRepository extends JpaRepository<Example, Integer> {

}
