package tests;

import classes.*;
import support.data.Endpoints;
import support.helpers.Specifications;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ReqresTests {
    Endpoints endpoints = new Endpoints();

    @Test
    public void checkUsersAvatarsIdsAndEmailsTest() {
        Specifications.installSpecification(Specifications.requestSpec(endpoints.getBASE_URL()), Specifications.responseSpec(200));

        List<UserData> users = new ArrayList<>();

        int numberOfPages = given()
            .when()
            .get(endpoints.getALL_USERS())
            .then()
            .extract().body().jsonPath().getInt("total_pages");

        given()
            .when().get(endpoints.getALL_USERS())
            .then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/java/support/schemas/allUsersSchema.json")));

        for (int pageNumber = 1; pageNumber <= numberOfPages; pageNumber++) {
            users.addAll(given()
                .when()
                .get(endpoints.getALL_USERS_PAGE() + pageNumber)
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class));
        }

        users.forEach(user -> Assertions.assertTrue(user.getAvatar().contains(user.getId().toString())));

        Assertions.assertTrue(users.stream().allMatch(user -> user.getEmail().endsWith("@reqres.in")));
    }

    @Test
    public void successfulRegistrationTest() {
        Specifications.installSpecification(Specifications.requestSpec(endpoints.getBASE_URL()), Specifications.responseSpec(200));
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        Registration newUser = new Registration("eve.holt@reqres.in", "pistol");

        SuccessfulRegistration successfulRegistration = given()
            .body(newUser)
            .when()
            .post(endpoints.getREGISTER_USER())
            .then().log().all()
            .extract().as(SuccessfulRegistration.class);

        Assertions.assertNotNull(successfulRegistration.getId());
        Assertions.assertNotNull(successfulRegistration.getToken());
        Assertions.assertEquals(id, successfulRegistration.getId());
        Assertions.assertEquals(token, successfulRegistration.getToken());
    }

    @Test
    public void unsuccessfulRegistrationTest() {
        Specifications.installSpecification(Specifications.requestSpec(endpoints.getBASE_URL()), Specifications.responseSpec(400));
        Registration newUser = new Registration("eve.holt@reqres.in", "");

        UnsuccessfulRegistration unsuccessfulRegistration = given()
            .body(newUser)
            .when()
            .post(endpoints.getREGISTER_USER())
            .then().log().all()
            .extract().as(UnsuccessfulRegistration.class);

        Assertions.assertEquals("Missing password", unsuccessfulRegistration.getError());
    }

    @Test
    public void sortedByYearsTest() {
        Specifications.installSpecification(Specifications.requestSpec(endpoints.getBASE_URL()), Specifications.responseSpec(200));
        List<ColorsData> colors = given()
            .when()
            .get(endpoints.getALL_RESOURCES())
            .then().log().all()
            .extract().body().jsonPath().getList("data", ColorsData.class);

        List<Integer> years = colors.stream().map(ColorsData::getYear).collect(Collectors.toList());
        List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());

        Assertions.assertEquals(sortedYears, years);
    }

    @Test
    public void findNonExistingUserTest() {
        Specifications.installSpecification(Specifications.requestSpec(endpoints.getBASE_URL()), Specifications.responseSpec(404));
        given()
            .when()
            .get(endpoints.getNON_EXISTING_USER())
            .then().log().all();
    }

    @Test
    public void deleteUserTest() {
        Specifications.installSpecification(Specifications.requestSpec(endpoints.getBASE_URL()), Specifications.responseSpec(204));
        given()
            .when()
            .delete(endpoints.getUSER_TO_UPDATE())
            .then().log().all();
    }

    @Test
    public void updateTimeTest() {
        Specifications.installSpecification(Specifications.requestSpec(endpoints.getBASE_URL()), Specifications.responseSpec(200));
        UserUpdateTime user = new UserUpdateTime("morpheus", "zion resident");
        UserUpdateTimeResponse response = given()
            .body(user)
            .when()
            .put(endpoints.getUSER_TO_UPDATE())
            .then().log().all()
            .extract().as(UserUpdateTimeResponse.class);

        String regex = "(.{7})$";
        String currentTime = Clock.systemUTC().instant().toString().replaceAll(regex, "");
        Assertions.assertEquals(currentTime, response.getUpdatedAt().replaceAll(regex, ""));
    }
}
