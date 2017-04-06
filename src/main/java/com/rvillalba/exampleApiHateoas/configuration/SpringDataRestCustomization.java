package com.rvillalba.exampleApiHateoas.configuration;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.stereotype.Component;

import com.rvillalba.exampleApiHateoas.entity.Example;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SpringDataRestCustomization extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        listMatchingClasses(Entity.class).forEach(entity -> config.exposeIdsFor(entity));
    }

    public List<Class> listMatchingClasses(Class annotationClass) {
        List<Class> classes = new LinkedList<Class>();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(true);
        scanner.addIncludeFilter(new AnnotationTypeFilter(annotationClass));
        for (BeanDefinition bd : scanner.findCandidateComponents(Example.class.getPackage().getName())) {
            try {
                classes.add(Class.forName(bd.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                log.error("listMatchingClasses problem", e);
            }
        }
        return classes;
    }

}