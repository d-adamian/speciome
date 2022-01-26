package com.epam.speciome.catalog.webservice.controllers;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.domain.collections.Collection;
import com.epam.speciome.catalog.domain.exceptions.AbsentCollectionNameException;
import com.epam.speciome.catalog.domain.exceptions.CollectionNotFoundException;
import com.epam.speciome.catalog.webservice.exceptions.InvalidInputException;
import com.epam.speciome.catalog.webservice.exceptions.NotFoundException;
import com.epam.speciome.catalog.webservice.models.CollectionRequest;
import com.epam.speciome.catalog.webservice.models.CollectionResponse;
import com.epam.speciome.catalog.webservice.models.ListCollectionsResponse;
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

@Tag(name = "Collections", description = "Collection Management API")
@RestController
public class CollectionController {
    @Autowired
    public UseCaseFactory useCaseFactory;

    @Operation(
            summary = "List collections",
            description = "Returns all available collections"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "OK", responseCode = "200", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ListCollectionsResponse.class)
            )),
            @ApiResponse(
                    description = "Not authenticated", responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @GetMapping("/collections")
    @ResponseBody
    public ListCollectionsResponse collectionsResponse() {
        List<Collection> collections = useCaseFactory.listCollections().listCollections();
        List<CollectionResponse> collectionsToOutput =
                collections.stream().map(CollectionResponse::new).toList();

        return new ListCollectionsResponse(collections.size(), collectionsToOutput);
    }

    @Operation(
            summary = "Create collection",
            description = "Creates new collection with given name and returns collection identifier"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "OK", responseCode = "201", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = CollectionController.CreateCollectionResponse.class)
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
    @PostMapping("/collection")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public CreateCollectionResponse createCollection(@RequestBody CollectionRequest request) {
        try {
            long collectionId = useCaseFactory.addCollection().addCollection(request.getCollectionName());
            return new CreateCollectionResponse(collectionId);
        } catch (AbsentCollectionNameException e) {
            throw new InvalidInputException(e.getMessage());
        }
    }

    @Operation(
            summary = "Get collection details",
            description = "Returns details for requested collection"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "OK", responseCode = "200", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = CollectionResponse.class)
            )),
            @ApiResponse(
                    description = "Collection not found", responseCode = "404",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    description = "Not authenticated", responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @GetMapping("/collection/{collectionId}")
    @ResponseBody
    public CollectionResponse getCollection(
            @PathVariable("collectionId") @Parameter(description = "Collection identifier", example = "1") long collectionId
    ) {
        try {
            Collection collection = useCaseFactory.getCollection().getCollection(collectionId);
            return new CollectionResponse(collection);
        } catch (CollectionNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    private static final class CreateCollectionResponse {
        private final long collectionId;

        CreateCollectionResponse(long collectionId) {
            this.collectionId = collectionId;
        }

        @SuppressWarnings("unused") // getter is used for JSON
        @Schema(description = "Internal collection identifier", example = "1")
        @JsonProperty
        long getCollectionId() {
            return collectionId;
        }
    }
}