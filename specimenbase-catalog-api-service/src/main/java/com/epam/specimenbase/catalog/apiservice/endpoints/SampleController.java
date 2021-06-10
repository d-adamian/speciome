package com.epam.specimenbase.catalog.apiservice.endpoints;

import com.epam.specimenbase.catalog.domain.samples.*;
import com.epam.specimenbase.catalog.domain.users.User;
import com.epam.specimenbase.catalog.ports.UseCaseFactory;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.*;
import org.apache.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.specimenbase.catalog.apiservice.endpoints.UserController.USER_SESSION_ATTRIBUTE;

public class SampleController {
    private final UseCaseFactory useCaseFactory;

    public SampleController(UseCaseFactory useCaseFactory) {
        this.useCaseFactory = useCaseFactory;
    }

    @OpenApi(
            path = "/samples",
            method = HttpMethod.GET,
            description = "Lists all samples accessible to current user",
            summary = "List samples",
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = ListSamplesResponse.class)}),
                    @OpenApiResponse(status = "401", description = "Unauthorized")
            }
    )
    public void listSamples(Context ctx) {
        // TODO: use interceptor for checking authorization
        User user = ctx.sessionAttribute(USER_SESSION_ATTRIBUTE);
        if (user == null) {
            ctx.status(HttpStatus.SC_UNAUTHORIZED);
        } else {
            var response = useCaseFactory.listSamples().listSamples();
            ctx.json(new ListSamplesResponse(response));
        }
    }

    @OpenApi(
            path = "/sample",
            method = HttpMethod.POST,
            description = "Add new sample. Supports only known attributes. " +
                    "Missing attributes are filled with empty values",
            summary = "Add new sample",
            requestBody = @OpenApiRequestBody(content = @OpenApiContent(from = AddSampleRequest.class)),
            responses = {
                    @OpenApiResponse(status = "201", description = "Sample added successfully"),
                    @OpenApiResponse(status = "400", description = "Input is invalid"),
                    @OpenApiResponse(status = "401", description = "Unauthorized")
            }
    )
    public void addSample(Context ctx) {
        User user = ctx.sessionAttribute(USER_SESSION_ATTRIBUTE);
        if (user == null) {
            ctx.status(HttpStatus.SC_UNAUTHORIZED);
        } else {
            try {
                String requestBody = ctx.body();
                AddSample.Result result;
                if (Strings.isNullOrEmpty(requestBody)) {
                    result = useCaseFactory.addSample().addNewSampleWithoutAttributes();
                } else {
                    AddSampleRequest request = ctx.bodyAsClass(AddSampleRequest.class);

                    // TODO: move resolving duplicates to core classes
                    Map<String, String> attributes = new HashMap<>();
                    for (SampleAttributeRequest sampleAttributeRequest: request.getAttributes()) {
                        attributes.put(sampleAttributeRequest.getAttribute(), sampleAttributeRequest.getValue());
                    }
                    result = useCaseFactory.addSample().addNewSample(attributes);
                }

                ctx.json(result.getSampleId());
            } catch (UnexpectedAttributeException e) {
                ctx.status(HttpStatus.SC_BAD_REQUEST);
                ctx.json(e.getMessage());
            }
        }
    }

    @OpenApi(
            path = "/sample/:sampleId",
            pathParams = {
                    @OpenApiParam(name = "sampleId", description = "Sample identifier")
            },
            method = HttpMethod.GET,
            description = "Retrieves information for specific sample",
            summary = "Get sample details",
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = SampleResponse.class)}),
                    @OpenApiResponse(status = "401", description = "Unauthorized")
            }
    )
    public void getSample(Context ctx) {
        // TODO: use interceptor for checking authorization
        User user = ctx.sessionAttribute(USER_SESSION_ATTRIBUTE);
        if (user == null) {
            ctx.status(HttpStatus.SC_UNAUTHORIZED);
        } else {
            String sampleId = ctx.pathParam("sampleId");
            try {
                var result = useCaseFactory.getSample().getSample(sampleId);
                ctx.json(new SampleResponse(result.getSample()));
            } catch (SampleNotFoundException e) {
                ctx.status(HttpStatus.SC_NOT_FOUND);
                ctx.json(e.getMessage());
            }
        }
    }

    @OpenApi(
            path = "/sample/:sampleId",
            pathParams = {
                    @OpenApiParam(name = "sampleId", description = "Sample identifier")
            },
            method = HttpMethod.DELETE,
            description = "Delete sample",
            summary = "Delete sample",
            responses = {
                    @OpenApiResponse(status = "204", description = "Successfully deleted"),
                    @OpenApiResponse(status = "401", description = "Unauthorized")
            }
    )
    public void deleteSample(Context ctx) {
        // TODO: use interceptor for checking authorization
        User user = ctx.sessionAttribute(USER_SESSION_ATTRIBUTE);
        if (user == null) {
            ctx.status(HttpStatus.SC_UNAUTHORIZED);
        } else {
            String sampleId = ctx.pathParam("sampleId");
            try {
                useCaseFactory.deleteSample().deleteSample(sampleId);
                ctx.status(HttpStatus.SC_NO_CONTENT);
            } catch (SampleNotFoundException e) {
                ctx.status(HttpStatus.SC_NOT_FOUND);
                ctx.json(e.getMessage());
            }
        }
    }

    @OpenApi(
            path = "/sample/:sampleId",
            method = HttpMethod.PUT,
            description = "Update existing sample. Supports only known attributes. " +
                    "Missing attributes are filled with empty values",
            summary = "Update existing sample",
            requestBody = @OpenApiRequestBody(content = @OpenApiContent(from = AddSampleRequest.class)),
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "Sample updated successfully",
                            content = @OpenApiContent(from = SampleResponse.class)
                    ),
                    @OpenApiResponse(status = "400", description = "Input is invalid"),
                    @OpenApiResponse(status = "401", description = "Unauthorized")
            }
    )
    public void updateSample(Context ctx) {
        User user = ctx.sessionAttribute(USER_SESSION_ATTRIBUTE);
        if (user == null) {
            ctx.status(HttpStatus.SC_UNAUTHORIZED);
        } else {
            try {
                String sampleId = ctx.pathParam("sampleId");
                String requestBody = ctx.body();
                if (Strings.isNullOrEmpty(requestBody)) {
                    ctx.status(HttpStatus.SC_BAD_REQUEST);
                    ctx.json("Request is empty");
                } else {
                    AddSampleRequest request = ctx.bodyAsClass(AddSampleRequest.class);

                    // TODO: move resolving duplicates to core classes
                    Map<String, String> attributes = new HashMap<>();
                    for (SampleAttributeRequest sampleAttributeRequest: request.getAttributes()) {
                        attributes.put(sampleAttributeRequest.getAttribute(), sampleAttributeRequest.getValue());
                    }
                    try {
                        var result = useCaseFactory.updateSample().updateSample(sampleId, attributes);
                        ctx.status(HttpStatus.SC_OK);
                        ctx.json(new SampleResponse(result.getSample()));
                    } catch (SampleNotFoundException e) {
                        ctx.status(HttpStatus.SC_NOT_FOUND);
                        ctx.json(e.getMessage());
                    }
                }
            } catch (UnexpectedAttributeException e) {
                ctx.status(HttpStatus.SC_BAD_REQUEST);
                ctx.json(e.getMessage());
            }
        }
    }

    public static final class ListSamplesResponse {
        private final int totalCount;
        private final List<SampleResponse> samples;

        private ListSamplesResponse(ListSamples.Result result) {
            this.totalCount = result.getTotalCount();
            this.samples = result.getSamples().stream().map(SampleResponse::new).collect(Collectors.toList());
        }

        @JsonProperty public int getTotalCount() {
            return totalCount;
        }

        @JsonProperty public List<SampleResponse> getSamples() {
            return samples;
        }
    }

    public static final class SampleResponse {
        private final String sampleId;
        private final ZonedDateTime createdAt;
        private final ZonedDateTime updatedAt;
        private final List<SampleAttributeResponse> attributes;

        private SampleResponse(Sample sample) {
            this.sampleId = sample.getSampleId();
            this.createdAt = sample.getCreatedAt();
            this.updatedAt = sample.getUpdatedAt();
            this.attributes = sample.getAttributes().entrySet().stream()
                    .map(entry -> new SampleAttributeResponse(entry.getKey(), entry.getValue()))
                    .sorted(Comparator.comparing(SampleAttributeResponse::getAttribute))
                    .collect(Collectors.toList());
        }

        @JsonProperty public String getSampleId() {
            return sampleId;
        }

        @JsonProperty public ZonedDateTime getCreatedAt() {
            return createdAt;
        }

        @JsonProperty public ZonedDateTime getUpdatedAt() {
            return updatedAt;
        }

        @JsonProperty public List<SampleAttributeResponse> getAttributes() {
            return attributes;
        }
    }

    public static final class SampleAttributeResponse {
        private final String attribute;
        private final String value;

        private SampleAttributeResponse(String attribute, String value) {
            this.attribute = attribute;
            this.value = value;
        }

        @JsonProperty public String getAttribute() {
            return attribute;
        }

        @JsonProperty public String getValue() {
            return value;
        }
    }

    public static final class AddSampleRequest {
        private final List<SampleAttributeRequest> attributes;

        @JsonCreator
        public AddSampleRequest(@JsonProperty("attributes") List<SampleAttributeRequest> attributes) {
            this.attributes = attributes;
        }

        @JsonProperty public List<SampleAttributeRequest> getAttributes() {
            return attributes;
        }
    }

    public static final class SampleAttributeRequest {
        private final String attribute;
        private final String value;

        @JsonCreator
        public SampleAttributeRequest(
                @JsonProperty("attribute") String attribute,
                @JsonProperty("value") String value
        ) {
            this.attribute = attribute;
            this.value = value;
        }

        @JsonProperty public String getAttribute() {
            return attribute;
        }

        @JsonProperty public String getValue() {
            return value;
        }
    }
}
