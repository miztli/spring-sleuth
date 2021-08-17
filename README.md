# Spring Sleuth 

Distributed tracing solution. Integrates with OpenZipkin (Zipkin) Brave Tracer via the bridge that is available in the spring-cloud-sleuth-brave module.
[Architecture diagram](trace-span-diagram.jpeg)

*Keywords:*

- _Span:_ Basic unit of work. E.g. sending an RPC.
- _Tracer:_ When the host makes a request to another application, it passes a few tracing identifiers along with the request to Zipkin so we can later tie the data together into spans. More on tracers: https://zipkin.io/pages/tracers_instrumentation
- _Trace:_ A set of spans forming a tree-like structure. E.g. POST http call.
- _Annotation/Event:_ Use to record the existence of an event in time.
- _Tag:_ key-value annotations to preserve some data
- _MDC (Mapped Diagnostic Context):_ The basic idea of Mapped Diagnostic Context is to provide a way to enrich log messages with pieces of information that could be not available in the scope where the logging actually occurs, but that can be indeed useful to better track the execution of the program. Allows us to fill a map-like structure with pieces of information that are accessible to the appender when the log message is actually written.

*Features:*
- _Context propagation:_

	- B3 propagation (https://github.com/openzipkin/b3-propagation): Specification for 'b3' headers and those who start with 'x-b3-'. These headers are used for trace context propagation accross service boundaries (often via http headers).

		- X-B3-TraceId: 64/128 bits. Overall ID of the trace. Value shared by all the spans in a trace. 
		- X-B3-ParentSpanId: 64 bit. Position of the parent operation in the trace tree. When the span is the root of the trace tree, there is no ParentSpanId.
		- X-B3-SpanId: 64-bit. Indicates the position of the current operation in the trace tree. The value should not be interpreted: it may or may not be derived from the value of the TraceId.
		- X-B3-Sampled: Sampling is a mechanism to reduce the volume of data that ends up in the tracing system. In B3, sampling applies consistently per-trace: once the sampling decision is made, the same value should be consistently sent downstream. This means you will see all spans sharing a trace ID or none.

- _Sampling:_ Spring Cloud Sleuth pushes the sampling decision down to the tracer implementation. However, there are cases where you can change the sampling decision at runtime.

One of such cases is skip reporting of certain client spans. To achieve that you can set the: `spring.sleuth.web.client.skip-pattern` with the path patterns to be skipped. Another option is to provide your own custom `org.springframework.cloud.sleuth.SamplerFunction<org.springframework.cloud.sleuth.http.HttpRequest>` implementation and define when a given HttpRequest should not be sampled.

- _Baggage:_ Distributed tracing works by propagating fields inside and across services that connect the trace together: traceId and spanId notably. The context that holds these fields can optionally push other fields that need to be consistent regardless of many services are touched. The simple name for these extra fields is "Baggage".

 * Baggage vs Tags
Like trace IDs, Baggage is attached to messages or requests, usually as headers. Tags are key value pairs sent in a Span to Zipkin. Baggage values are not added spans by default, which means you can’t search based on Baggage unless you opt-in.


### Zipkin

Distributed tracing system. It helps troubleshooting latency problems in service arquitectures.

### Brave
Brave is a distributed tracing instrumentation library. 
- Brave typically intercepts production requests to gather timing data, correlate and propagate trace contexts. 
- Typically trace data (spans) is sent to Zipkin server, however, third-party plugins are available to send to alternate services such as Amazon X-Ray.
- Export tracing information and visualize latency. Completed spans are reported to Zipkin server asynchronously.
- Instrumentation will not work if bean is defined using the new operator, this means, Spring is not aware of bean instance.
- Log Integration:

(More on Brave: https://github.com/openzipkin/brave)

*Features*

_Query by:_
- trace ID
- Otherwise: service, operation names, tags and duration.

### Demo preparation
- Show Controller with logger and run app. Show how the logs are printed without sleuth.
- Include sleuth into classpath
- Run app again. Show logs.
	- Log format [app-name, traceId, spanId, sample]
	- Then, send http request without headers. Show how default sleuth config creates trancing information.
- Run http req with spanId and traceId headers. See the logs.
- Comment about baggage. Go to properties and set spring.sleuth.baggage.remote-fields property. Make an http call and see how headers are propagated downstream. Uncomment property to see how they stop propagating.
- How to generate custom spans
- How to use zipkin:
  	- [Architecture diagram](zipkin-architecture.png)
	- Start the server: `java -jar zipkin-server-2.23.2-exec.jar`. Navigate to `http://localhost:9411/zipkin/`
	- Add `spring-cloud-sleuth-zipkin` dependency to `pom.xml`
	- Set `spring.zipkin.baseUrl` in application.properties file
	- Run the app. Start sending http requests.
	- Go to Zipkin Server and search by trace ID.
	- Set `X-B3-Sampled` header and check whether trace information is reported to Zipkin or not.
	- Enable `@NewSpan` annotation and see how it displays in zipkin.