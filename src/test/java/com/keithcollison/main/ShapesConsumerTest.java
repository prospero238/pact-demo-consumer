package com.keithcollison.main;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRule;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class ShapesConsumerTest {


    private static final int ALT_PORT = 8086;
    private static final int ORIG_PORT = 8085;
    @Rule
    public PactProviderRule provider = new PactProviderRule("test_provider", "localhost", ORIG_PORT, this);

    @Rule
    public PactProviderRule altProvider = new PactProviderRule("alt_provider", "localhost", ALT_PORT, this);


    @Pact(provider = "alt_provider", consumer = "test_consumer")
    public PactFragment createAltFragment(PactDslWithProvider builder) {

        return builder
                .uponReceiving("ExampleJavaConsumerPactRuleTest test interaction")
                .path("/shape/circle")
                .method("GET")
                .willRespondWith()
                .body("{\"name\":\"circle\"}")
                .status(200)
                .toFragment();
    }
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
        assertEquals(new ConsumerClient("http://localhost:" + ORIG_PORT).getAsMap("/shape/circle"), expectedResponse);
    }
    @Test
    @PactVerification("alt_provider")
    public void runAltTest() throws IOException {
        Map expectedResponse = new HashMap();

        expectedResponse.put("name", "circle");
        assertEquals(new ConsumerClient("http://localhost:" + ALT_PORT).getAsMap("/shape/circle"), expectedResponse);

    }
}