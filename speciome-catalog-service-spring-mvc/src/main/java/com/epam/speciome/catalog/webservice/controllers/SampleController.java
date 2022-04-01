package com.epam.speciome.catalog.webservice.controllers;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.domain.exceptions.*;
import com.epam.speciome.catalog.domain.samples.*;
import com.epam.speciome.catalog.webservice.ApiConstants;
import com.epam.speciome.catalog.webservice.exceptions.*;
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
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "Samples", description = "Sample Management API")
@RestController
public class SampleController {
    @Autowired
    public UseCaseFactory useCaseFactory;

    @Operation(
            summary = "List samples",
            description = "Returns samples with required archival status and sorted by attributes if needed"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "OK", responseCode = "200", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ListSamplesResponse.class)
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
    @GetMapping(ApiConstants.SAMPLES)
    @ResponseBody
    public ListSamplesResponse listSamples(
            @Schema(allowableValues = {"ALL", "UNARCHIVED", "ARCHIVED"}, defaultValue = "ALL")
            @RequestParam(value = "archivalStatus", required = false)
            @Parameter(description = "Which samples to list") String archivalStatus,
            @RequestParam(value = "sortby", required = false)
            @Parameter(description = "By which attribute will be sorted list") String sortAttribute,
            @RequestParam(value = "orderby", required = false)
            @Parameter(description = "By which order will be sorted list") String orderAttribute
    ) {
        try {
            SortSampleListParams params = new SortSampleListParams(archivalStatus, sortAttribute, orderAttribute);
            ListSamples.Result result = useCaseFactory.listSamples().listSamples(params);
            List<SampleResponse> samples = result.samples().stream()
                    .map(SampleResponse::new)
                    .collect(Collectors.toList());
            return new ListSamplesResponse(result.totalCount(), samples);
        } catch (ArchivalStatusException | SortedAttributeException e) {
            throw new InvalidInputException(e.getMessage());
        }
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
    @PostMapping(ApiConstants.SAMPLE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public CreateSampleResponse createSample(@RequestBody(required = false) SampleRequest request) {
        try {
            AddSample.Result result;
            List<SampleAttribute> attributes = request == null ? List.of() : request.getAttributes();
            if (attributes == null || attributes.isEmpty()) {
                result = useCaseFactory.addSample().addSampleWithoutAttributes();
            } else {
                result = useCaseFactory.addSample().addSample(convertAttributes(attributes));
            }
            return new CreateSampleResponse(result.sampleId());
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
    @GetMapping(ApiConstants.SAMPLE_BY_ID)
    @ResponseBody
    public SampleResponse getSample(
            @PathVariable("sampleId") @Parameter(description = "Sample identifier", example = "1") Long sampleId
    ) {
        try {
            GetSample.Result result = useCaseFactory.getSample().getSample(sampleId);
            return new SampleResponse(result.sample());
        } catch (SampleNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Operation(
            summary = "Delete not archived sample",
            description = "Deletes given not archived sample"

    )
    @ApiResponses({
            @ApiResponse(
                    description = "Deleted successfully", responseCode = "204"
            ),
            @ApiResponse(
                    description = "Sample not found", responseCode = "404"
            ),
            @ApiResponse(
                    description = "Sample is archived and cannot be deleted", responseCode = "403"
            ),
            @ApiResponse(
                    description = "Not authenticated", responseCode = "401"
            )
    })
    @DeleteMapping(ApiConstants.SAMPLE_BY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSample(
            @PathVariable("sampleId") @Parameter(description = "Sample identifier", example = "1") Long sampleId
    ) {
        try {
            useCaseFactory.deleteSample().deleteSample(sampleId);
        } catch (SampleNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (SampleNotArchivedException e) {
            throw new ForbiddenException(e.getMessage());
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
    @PutMapping(ApiConstants.SAMPLE_BY_ID)
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

    @Operation(
            summary = "Archive sample",
            description = "Archive given sample"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Archived successfully", responseCode = "204"
            ),
            @ApiResponse(
                    description = "Sample not found", responseCode = "404"
            ),
            @ApiResponse(
                    description = "Not authenticated", responseCode = "401"
            )
    })
    @PutMapping(ApiConstants.SAMPLE_BY_ID_ARCHIVE)
    public SampleResponse archiveSample(
            @PathVariable("sampleId") @Parameter(description = "Sample identifier", example = "1") Long sampleId
    ) {
        try {
            Sample archivedSample = useCaseFactory.archiveSample().archiveSample(sampleId);
            return new SampleResponse(archivedSample);
        } catch (SampleNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Operation(
            summary = "UnArchive sample",
            description = "UnArchive given sample"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "UnArchived successfully", responseCode = "204"
            ),
            @ApiResponse(
                    description = "Sample not found", responseCode = "404"
            ),
            @ApiResponse(
                    description = "Not authenticated", responseCode = "401"
            )
    })
    @PutMapping(ApiConstants.SAMPLE_BY_ID_UNARCHIVE)
    public SampleResponse unArchiveSample(
            @PathVariable("sampleId") @Parameter(description = "Sample identifier", example = "1") Long sampleId
    ) {
        try {
            Sample unArchivedSample = useCaseFactory.unArchiveSample().unArchiveSample(sampleId);
            return new SampleResponse(unArchivedSample);
        } catch (SampleNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Operation(
            summary = "Download samples",
            description = "Download a CSV file with available samples"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "OK", responseCode = "200", content = @Content(
                    mediaType = "application/csv")
            ),
            @ApiResponse(
                    description = "Not authenticated", responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @GetMapping(ApiConstants.SAMPLES_DOWNLOAD)
    public ResponseEntity<Resource> getFile() throws IOException {
        String filename = "samples.csv";
        InputStreamResource file = new InputStreamResource(useCaseFactory.exportSamples().exportSamples());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }

    @Operation(
            summary = "Import samples",
            description = "Import samples from CSV file"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Import is successful", responseCode = "207"
            ),
            @ApiResponse(
                    description = "Invalid file content", responseCode = "400",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    description = "Not authenticated", responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    description = "Unsupported media type", responseCode = "415",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @PostMapping(value = ApiConstants.SAMPLES_UPLOAD_CSV, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public void uploadFile(@RequestPart("file") MultipartFile file) throws Exception {
        try {
            Set<String> ALLOWED_IMPORT_TYPES = Set.of("application/vnd.ms-excel", "text/csv");
            String contentType = file.getContentType();
            if (!ALLOWED_IMPORT_TYPES.contains(contentType)) {
                throw new UnsupportedMediaTypeException();
            }
            useCaseFactory.importSamples().saveSamples(file.getInputStream());
        } catch (ImportFileWithMissingColumnsException e) {
            throw new UnsupportedFileContentException(e.getMessage());
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

        @SuppressWarnings("unused") // getter is used for JSON
        @Schema(description = "Internal sample identifier", example = "1")
        @JsonProperty
        Long getSampleId() {
            return sampleId;
        }
    }
}
