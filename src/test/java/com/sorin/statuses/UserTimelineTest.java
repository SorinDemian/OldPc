package com.sorin.statuses;

import com.sorin.common.RestUtilities;
import com.sorin.constants.EndPoints;
import com.sorin.constants.Path;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.hasItem;

public class UserTimelineTest {

    RequestSpecification reqSpec;
    ResponseSpecification resSpec;

    @BeforeClass
    public void setup(){
        reqSpec = RestUtilities.getRequestSpecification();
        reqSpec.queryParam("user_id", "DemianSorin08");
        reqSpec.basePath(Path.STATUSES);

        resSpec = RestUtilities.getResponseSpecification();
    }

    @Test
    public void readTweets1(){
        given()
                .spec(RestUtilities.createQuerryParam(reqSpec, "count", "1"))
        .when()
                .get(EndPoints.STATUSES_USER_TIMELINE)
        .then()
                .log().all()
                .spec(resSpec)
                .body("user.screen_name", hasItem("DemianSorin08"));
    }
    @Test
    public void readTweets2(){
        RestUtilities.setEndPoint(EndPoints.STATUSES_USER_TIMELINE);
        Response res = RestUtilities.getResponse(RestUtilities.createQuerryParam(reqSpec, "count", "1"), "get");
        ArrayList<String> screenNameList = res.path("user.screen_name");
        Assert.assertTrue(screenNameList.contains("DemianSorin08"));
    }

}
