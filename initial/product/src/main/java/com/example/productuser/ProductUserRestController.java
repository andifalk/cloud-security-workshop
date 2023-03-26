package com.example.productuser;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

/**
 * Rest API for {@link ProductUser users}.
 */
@Tag(name = "User", description = "The User API. Contains all the operations that can be performed on a user.")
@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping("/v1/users")
public class ProductUserRestController {
    private static final Logger LOG = LoggerFactory.getLogger(ProductUserRestController.class.getName());

    private final ProductUserService productUserService;

    public ProductUserRestController(ProductUserService productUserService) {
        this.productUserService = productUserService;
    }

    @Operation(
            summary = "Get users",
            description = "Retrieve all existing users",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Access is only allowed for authenticated users", responseCode = "401"),
                    @ApiResponse(description = "Access is only allowed for ADMIN role", responseCode = "403")
            }
    )
    @GetMapping(path = "/users")
    public List<ProductUser> getAllProductUsers(@AuthenticationPrincipal(errorOnInvalidType = true) ProductUser productUser) {
        LOG.info("Return all registered users using authenticated user '" + productUser.getEmail() + "'");
        return productUserService.findAll();
    }
}
