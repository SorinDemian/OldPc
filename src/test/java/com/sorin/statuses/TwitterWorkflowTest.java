package com.sorin.statuses;

import com.sorin.common.RestUtilities;
import com.sorin.constants.EndPoints;
import com.sorin.constants.Path;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class TwitterWorkflowTest {

    RequestSpecification reqSpec;
    ResponseSpecification resSpec;
    String tweetId = "";

    @BeforeClass
    public void setup(){
        reqSpec = RestUtilities.getRequestSpecification();
        reqSpec.basePath(Path.STATUSES);

        resSpec = RestUtilities.getResponseSpecification();
    }
    @Test
    public void postTweet(){
        Response response =
                given()
                        .spec(RestUtilities.createQuerryParam(reqSpec, "status", "Hello Moon!"))
                .when()
                        .post(EndPoints.STATUSES_TWEET_POST)
                .then()
                        .spec(resSpec)
                        .extract()
                        .response();
        JsonPath jsonPath = RestUtilities.getJsonPath(response);
        tweetId = jsonPath.get("id_str");
        System.out.println("The response.path: " + tweetId);
    }
    @Test(dependsOnMethods = "postTweet")
    public void readTweet(){
        RestUtilities.setEndPoint(EndPoints.STATUSES_TWEET_READ_SINGLE);
        Response res = RestUtilities.getResponse(RestUtilities.createQuerryParam(reqSpec, "id", tweetId), "get");
        String text = res.path("text");
        System.out.println("The tweet text is: " + text);
    }
    @Test(dependsOnMethods = "readTweet")
    public void deleteTweet(){
        given()
                .spec(RestUtilities.createPathParam(reqSpec, "id", tweetId))
        .when()
                .post(EndPoints.STATUSES_TWEET_DESTROY)
        .then()
                .spec(resSpec);
    }
}
