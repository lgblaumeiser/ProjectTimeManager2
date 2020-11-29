package de.lgblaumeiser.ptm.rest

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

@QuarkusTest
class BookingResourceTest {

    @Test
    fun testHelloEndpoint() {
        given()
          .`when`().get("/booking")
          .then()
             .statusCode(200)
             .body(`is`("Hello RESTEasy"))
    }

}