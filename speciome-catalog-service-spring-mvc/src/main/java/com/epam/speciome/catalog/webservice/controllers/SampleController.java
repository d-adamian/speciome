package com.epam.speciome.catalog.webservice.controllers;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.domain.samples.*;
import com.epam.speciome.catalog.webservice.exceptions.InvalidInputException;
import com.epam.speciome.catalog.webservice.exceptions.NotFoundException;
import com.epam.speciome.catalog.webservice.models.ListSamplesResponse;
import com.epam.speciome.catalog.webservice.models.SampleAttribute;
import com.epam.speciome.catalog.webservice.models.SampleRequest;
import com.epam.speciome.catalog.webservice.models.SampleResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Samples", description = "Sample Management API")
@RestController
public class SampleController {
    @Autowired
    public UseCaseFactory useCaseFactory;

    @Operation(
            summary = "List samples",
            description = "Returns all available samples"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "OK", responseCode = "200", content = @Content(
                            mediaType = "application/json", schema = @Schema(implementation = ListSamplesResponse.class)
            )),
            @ApiResponse(
                    description = "Not authenticated", responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @GetMapping("/samples")
    @ResponseBody
    public ListSamplesResponse listSamples() {
        ListSamples.Result result = useCaseFactory.listSamples().listSamples();
        List<SampleResponse> samples = result.getSamples().stream()
                .map(SampleResponse::new)
                .collect(Collectors.toList());
        return new ListSamplesResponse(result.getTotalCount(), samples);
    }

    @Operation(
            summary = "Create sample",
            description = "Creates new sample with given attributes and returns sample identifier"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "OK", responseCode = "201", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = CreateSampleResponse.class)
            )),
            @ApiResponse(
                    description = "Invalid input parameters", responseCode = "400",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    description = "Not authenticated", responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @PostMapping("/sample")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public CreateSampleResponse createSample(@RequestBody(required = false) SampleRequest request) {
        try {
            AddSample.Result result;
            List<SampleAttribute> attributes = request == null ? List.of() : request.getAttributes();
            if (attributes == null || attributes.isEmpty()) {
                result = useCaseFactory.addSample().addNewSampleWithoutAttributes();
            } else {
                result = useCaseFactory.addSample().addNewSample(convertAttributes(attributes));
            }
            return new CreateSampleResponse(result.getSampleId());
        } catch (UnexpectedAttributeException e) {
            throw new InvalidInputException(e.getMessage());
        }
    }

    @Operation(
            summary = "Get sample details",
            description = "Returns details for requested sample"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "OK", responseCode = "200", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = SampleResponse.class)
            )),
            @ApiResponse(
                    description = "Sample not found", responseCode = "404",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    description = "Not authenticated", responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @GetMapping("/sample/{sampleId}")
    @ResponseBody
    public SampleResponse getSample(
            @PathVariable("sampleId") @Parameter(description = "Sample identifier", example = "1") Long sampleId
    ) {
        try {
            GetSample.Result result = useCaseFactory.getSample().getSample(sampleId);
            return new SampleResponse(result.getSample());
        } catch (SampleNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Operation(
            summary = "Delete sample",
            description = "Deletes given sample"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Deleted successfully", responseCode = "204"
            ),
            @ApiResponse(
                    description = "Sample not found", responseCode = "404"
            ),
            @ApiResponse(
                    description = "Not authenticated", responseCode = "401"
            )
    })
    @DeleteMapping("/sample/{sampleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSample(
            @PathVariable("sampleId") @Parameter(description = "Sample identifier", example = "1") Long sampleId
    ) {
        try {
            useCaseFactory.deleteSample().deleteSample(sampleId);
        } catch (SampleNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Operation(
            summary = "Update sample",
            description = "Updates given sample"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Updated successfully", responseCode = "204"
            ),
            @ApiResponse(
                    description = "Sample not found", responseCode = "404"
            ),
            @ApiResponse(
                    description = "Not authenticated", responseCode = "401"
            )
    })
    @PutMapping("/sample/{sampleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateSample(
            @PathVariable("sampleId") @Parameter(description = "Sample identifier", example = "1") Long sampleId,
            @RequestBody(required = false) SampleRequest request
    ) {
        List<SampleAttribute> attributes = Optional.ofNullable(request)
                .map(SampleRequest::getAttributes).orElse(List.of());
        try {
            useCaseFactory.updateSample().updateSample(sampleId, convertAttributes(attributes));
        } catch (SampleNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    private static Map<String, String> convertAttributes(List<SampleAttribute> sampleAttributes) {
        return sampleAttributes.stream()
                .collect(Collectors.toMap(SampleAttribute::getAttribute, SampleAttribute::getValue));
    }

    private static final class CreateSampleResponse {
        private final Long sampleId;

        CreateSampleResponse(Long sampleId) {
            this.sampleId = sampleId;
        }

        @Schema(description = "Internal sample identifier", example = "1")
        @JsonProperty
        Long getSampleId() {
            return sampleId;
        }
    }
}
