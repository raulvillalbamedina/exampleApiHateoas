package com.rvillalba.exampleApiHateoas.resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.rvillalba.clientresttemplate.ClientRestTemplate;
import com.rvillalba.clientresttemplate.jwt.LoginResponse;
import com.rvillalba.exampleApiHateoas.CrudTestInterface;
import com.rvillalba.exampleApiHateoas.entity.Example;

import lombok.Data;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Data
public class ExampleCrudTest implements CrudTestInterface<Example> {

    @Value("${local.server.port}")
    private int port;
    private LoginResponse loginResponse;
    private ClientRestTemplate clientRestTemplate;

    @Before
    public void before() {
        clientRestTemplate = new ClientRestTemplate();
        clientRestTemplate.setPort(port);
        loginResponse = login(generateLoginRequest());
    }

    @Test
    public void crudTest() {
        defaultCrudTest(loginResponse, new ParameterizedTypeReference<Resource<Example>>() {
        });
    }

    @Override
    public Example fillObject() {
        Example entity = new Example();
        entity.setName("testExample");
        return entity;
    }

    @Override
    public Example modifyObject(Example entity) {
        entity.setName("testUpdate");
        return entity;
    }

    @Override
    public Class<?> getEntityClass() {
        return Example.class;
    }

    @Override
    public void validateSpecificCreate(ResponseEntity<Resource> result) {
        Assert.assertNotNull(((Example) result.getBody().getContent()).getName());
        Assert.assertTrue(((Example) result.getBody().getContent()).getName().equals("testExample"));
    }

    @Override
    public void validateSpecificRead(ResponseEntity<Resource> result) {
        Assert.assertNotNull(((Example) result.getBody().getContent()).getName());
    }

    @Override
    public void validateUpdate(ResponseEntity<Resource> result) {
        Assert.assertEquals("testUpdate", ((Example) result.getBody().getContent()).getName());
    }

}
