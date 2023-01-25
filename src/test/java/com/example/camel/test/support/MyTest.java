package com.example.camel.test.support;

import javax.inject.Inject;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class MyTest extends CamelQuarkusTestSupport {

    @Inject
    CamelContext context;

    @Inject
    ProducerTemplate producerTemplate;

    @EndpointInject("mock:kafka")
    MockEndpoint mockKafka;

    @BeforeEach
    void beforeEach() throws Exception {
        AdviceWith.adviceWith(context, "myKafka", a ->
                a.weaveById("kafka").replace().to("mock:kafka")
        );
    }

    @Test
    void testMockKafka() throws Exception {
        var message = "HelloWorld from Timer!";
        // testing the Timer
        mockKafka.expectedBodiesReceived(message);
        MockEndpoint.assertIsSatisfied(context);
        MockEndpoint.resetMocks(context);

        // testing the Direct Route
        message = "Let's try again!";
        mockKafka.expectedBodiesReceived(message);
        producerTemplate.sendBody("direct:toKafka", message);
        MockEndpoint.assertIsSatisfied(context);
    }

}
