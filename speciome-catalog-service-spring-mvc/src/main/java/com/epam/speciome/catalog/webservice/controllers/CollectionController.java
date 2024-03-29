package com.epam.speciome.catalog.webservice.controllers;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.domain.collections.Collection;
import com.epam.speciome.catalog.domain.collections.CollectionAttributes;
import com.epam.speciome.catalog.domain.exceptions.AbsentCollectionNameException;
import com.epam.speciome.catalog.domain.exceptions.CollectionNotArchivedException;
import com.epam.speciome.catalog.domain.exceptions.CollectionNotFoundException;
import com.epam.speciome.catalog.domain.exceptions.SortAttributeException;
import com.epam.speciome.catalog.webservice.ApiConstants;
import com.epam.speciome.catalog.webservice.exceptions.ForbiddenException;
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

import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
    @GetMapping(ApiConstants.COLLECTIONS)
    @ResponseBody
    public ListCollectionsResponse collectionsResponse(@RequestParam(value = "sortBy", required = false) String sortBy,
                                                       @RequestParam(value = "orderBy", required = false) String orderBy) {
        try {
            List<Collection> collections = useCaseFactory.listCollections().listCollections(sortBy, orderBy);
            List<CollectionResponse> collectionsToOutput =
                    collections.stream().map(CollectionResponse::new).toList();
            return new ListCollectionsResponse(collections.size(), collectionsToOutput);
        } catch (SortAttributeException e) {
            throw new InvalidInputException(e.getMessage());
        }
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
    @PostMapping(ApiConstants.COLLECTION)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public CreateCollectionResponse createCollection(Principal principal, @RequestBody CollectionRequest request) {
        try {
            long collectionId = useCaseFactory.addCollection()
                    .addCollection(new CollectionAttributes(request.getCollectionName(), principal.getName()));
            return new CreateCollectionResponse(collectionId);
        } catch (AbsentCollectionNameException e) {
            throw new InvalidInputException(e.getMessage());
        }
    }

    @Operation(
            summary = "Update collection",
            description = "Update collection's name with given ID"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Updated successfully", responseCode = "204"
            ),
            @ApiResponse(
                    description = "Collection not found", responseCode = "404"
            ),
            @ApiResponse(
                    description = "Not authenticated", responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @PutMapping("/collection/{collectionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCollection(
            @PathVariable("collectionId") @Parameter(description = "Collection identifier", example = "1") long collectionId,
            @RequestBody(required = false) CollectionRequest request) {

        CollectionRequest collectionRequest = Optional.ofNullable(request).orElse(new CollectionRequest(""));
        String collectionName = collectionRequest.getCollectionName();
        try {
            useCaseFactory.updateCollection().updateCollection(collectionId, collectionName);
        } catch (CollectionNotFoundException e) {
            throw new NotFoundException(e.getMessage());
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
    @GetMapping(ApiConstants.COLLECTION_BY_ID)
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

    @Operation(
            summary = "Archive collection",
            description = "Archive given collection"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Archived successfully", responseCode = "204"
            ),
            @ApiResponse(
                    description = "Collection not found", responseCode = "404"
            ),
            @ApiResponse(
                    description = "Not authenticated", responseCode = "401"
            )
    })
    @PutMapping(ApiConstants.COLLECTION_BY_ID_ARCHIVE)
    public CollectionResponse archiveCollection(
            @PathVariable("collectionId") @Parameter(description = "Collection identifier", example = "1") Long collectionId
    ) {
        try {
            Collection archivedCollection = useCaseFactory.archiveCollection().archiveCollection(collectionId);
            return new CollectionResponse(archivedCollection);
        } catch (CollectionNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Operation(
            summary = "UnArchive collection",
            description = "UnArchive given collection"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "UnArchived successfully", responseCode = "204"
            ),
            @ApiResponse(
                    description = "Collection not found", responseCode = "404"
            ),
            @ApiResponse(
                    description = "Not authenticated", responseCode = "401"
            )
    })
    @PutMapping(ApiConstants.COLLECTION_BY_ID_UNARCHIVE)
    public CollectionResponse unArchiveCollection(
            @PathVariable("collectionId") @Parameter(description = "Collection identifier", example = "1") Long collectionId
    ) {
        try {
            Collection unArchivedCollection = useCaseFactory.unArchiveCollection().unArchiveCollection(collectionId);
            return new CollectionResponse(unArchivedCollection);
        } catch (CollectionNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Operation(
            summary = "Delete not archived collection",
            description = "Deletes given not archived collection"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Deleted successfully", responseCode = "200"
            ),
            @ApiResponse(
                    description = "Sample not found", responseCode = "404"
            ),
            @ApiResponse(
                    description = "Collection is archived and cannot be deleted", responseCode = "403"
            ),
            @ApiResponse(
                    description = "Not authenticated", responseCode = "401"
            )
    })
    @DeleteMapping(ApiConstants.COLLECTION_BY_ID)
    @ResponseStatus(HttpStatus.OK)
    public void removeCollection(
            @PathVariable("collectionId") @Parameter(description = "Collection identifier", example = "1") Long collectionId
    ) {
        try {
            useCaseFactory.removeCollection().removeCollection(collectionId);
        } catch (CollectionNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (CollectionNotArchivedException e) {
            throw new ForbiddenException(e.getMessage());
        }
    }
}
