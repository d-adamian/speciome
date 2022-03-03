package com.epam.speciome.catalog.webservice.controllers;

import com.epam.speciome.catalog.domain.samples.Attributes;
import com.epam.speciome.catalog.webservice.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Sample attributes", description = "Sample attributes")
@RestController
public class AttributesListController {

    @Operation(
            summary = "List sample attributes",
            description = "Returns list of all possible sample attributes",
            responses = {@ApiResponse(
                    description = "OK", responseCode = "200"
            )}
    )
    @GetMapping(path = ApiConstants.ATTRIBUTES)
    public @ResponseBody
    List<String> listAttributes() {
        return Attributes.ALL;
    }
}
