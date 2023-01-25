package com.example.camel.test.support;

import org.apache.camel.builder.RouteBuilder;

public class Routes extends RouteBuilder {
    @Override
    public void configure(){
        from("timer:toto?delay=1000&repeatCount=1")
                .id("myname")
                .transform().constant("HelloWorld from Timer!")
                .log("Body is :: ${body}")
                        .to("direct:toKafka");

        from("direct:toKafka")
                .id("myKafka")
                .log("Sending to kafka topic test, message body : ${body}")
                .to("kafka:test").id("kafka");

    }
}
