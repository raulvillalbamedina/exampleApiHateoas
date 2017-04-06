package com.rvillalba.exampleApiHateoas;

import org.junit.Assert;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.rvillalba.clientresttemplate.ClientRestTemplate;
import com.rvillalba.clientresttemplate.jwt.LoginRequest;
import com.rvillalba.clientresttemplate.jwt.LoginResponse;

public interface CrudTestInterface<T> {

    default void defaultCrudTest(LoginResponse loginResponse, ParameterizedTypeReference parameterizedTypeReference) {
        ResponseEntity<Resource> created = createAndValidate(loginResponse, getEntityClass(), parameterizedTypeReference);
        ResponseEntity<Resource> readed = readAndValidate(loginResponse, findId(created.getBody().getId().getHref().split("/")),
                parameterizedTypeReference);
        updateAndValidate(loginResponse, findId(created.getBody().getId().getHref().split("/")), modifyObject((T) readed.getBody().getContent()),
                parameterizedTypeReference);
        deleteAndValidate(loginResponse, findId(created.getBody().getId().getHref().split("/")));
    }

    default Integer findId(String[] split) {
        return Integer.valueOf(split[split.length - 1]);
    }

    default void validateAllCreate(ResponseEntity<Resource> result) {
        validateCreateDefault(result);
        validateSpecificCreate(result);
    }

    default void validateCreateDefault(ResponseEntity<Resource> result) {
        Assert.assertNotNull(result.getBody().getId());
        Assert.assertNotNull(result.getBody().getContent());
        Assert.assertNotNull(result.getBody().getLinks());
    }

    default LoginRequest generateLoginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("UserTest");
        loginRequest.setPassword("passwordTest");
        return loginRequest;
    }

    void validateSpecificCreate(ResponseEntity<Resource> result);

    default void validateRead(ResponseEntity<Resource> result) {
        validateReadDefault(result);
        validateSpecificRead(result);
    }

    void validateSpecificRead(ResponseEntity<Resource> result);

    default void validateReadDefault(ResponseEntity<Resource> result) {
        Assert.assertNotNull(result.getBody().getContent());
        Assert.assertNotNull(result.getBody().getId());
        Assert.assertNotNull(result.getBody().getContent());
        Assert.assertNotNull(result.getBody().getLinks());
    }

    void validateUpdate(ResponseEntity<Resource> result);

    default void validateDelete(ResponseEntity<String> result) {
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getStatusCode().value() == HttpStatus.NO_CONTENT.value());
    }

    default ResponseEntity<Resource> create(LoginResponse loginResponse, Class entityClass, ParameterizedTypeReference parameterizedTypeReference) {
        return getClientRestTemplate().create(getEntityClass(), loginResponse, fillObject(), parameterizedTypeReference);
    }

    default ResponseEntity<Resource> read(LoginResponse loginResponse, Integer id, ParameterizedTypeReference parameterizedTypeReference) {
        return getClientRestTemplate().readOne(loginResponse, getEntityClass(), id, parameterizedTypeReference);
    }

    default ResponseEntity<Resource> update(LoginResponse loginResponse, T entity, Integer id,
            ParameterizedTypeReference parameterizedTypeReference) {
        return getClientRestTemplate().update(loginResponse, entity, getEntityClass(), id, parameterizedTypeReference);
    }

    default ResponseEntity<String> delete(LoginResponse loginResponse, Integer id) {
        return getClientRestTemplate().delete(loginResponse, getEntityClass(), id);
    }

    T modifyObject(T entity);

    T fillObject();

    Class<?> getEntityClass();

    ClientRestTemplate getClientRestTemplate();

    default ResponseEntity<Resource> createAndValidate(LoginResponse loginResponse, Class entityClass,
            ParameterizedTypeReference parameterizedTypeReference) {
        ResponseEntity<Resource> result = create(loginResponse, entityClass, parameterizedTypeReference);
        validateAllCreate(result);
        return result;
    }

    default ResponseEntity<Resource> readAndValidate(LoginResponse loginResponse, Integer id, ParameterizedTypeReference parameterizedTypeReference) {
        ResponseEntity<Resource> result = read(loginResponse, id, parameterizedTypeReference);
        validateRead(result);
        return result;
    }

    default void updateAndValidate(LoginResponse loginResponse, Integer id, T entity, ParameterizedTypeReference parameterizedTypeReference) {
        ResponseEntity<Resource> result = update(loginResponse, entity, id, parameterizedTypeReference);
        validateUpdate(result);
    }

    default void deleteAndValidate(LoginResponse loginResponse, Integer id) {
        ResponseEntity<String> result = delete(loginResponse, id);
        validateDelete(result);
    }

    default LoginResponse login(LoginRequest loginRequest) {
        return getClientRestTemplate().login(loginRequest);
    }

}
