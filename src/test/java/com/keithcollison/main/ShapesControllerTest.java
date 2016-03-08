package com.keithcollison.main;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRule;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class ShapesControllerTest {


    @Rule
    public PactProviderRule provider = new PactProviderRule("test_provider", "localhost", 8085, this);

    @Pact(provider = "test_provider", consumer = "test_consumer")
    public PactFragment createFragment(PactDslWithProvider builder) {
        return builder
                .uponReceiving("ExampleJavaConsumerPactRuleTest test interaction")
                .path("/shape/circle")
                .method("GET")
                .willRespondWith()
                .body("{\"name\":\"circle\"}")
                .status(200)
                .toFragment();
    }

    @Test
    @PactVerification("test_provider")
    public void runTest() throws IOException {
        Map expectedResponse = new HashMap();

        expectedResponse.put("name","circle");
        assertEquals(new ConsumerClient("http://localhost:8085").getAsMap("/shape/circle"), expectedResponse);
    }
}