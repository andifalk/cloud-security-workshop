package com.example.product;

import com.example.productuser.ProductUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Rest API for {@link ProductEntity products}.
 */
@Tag(name = "Product", description = "The Product API. Contains all the operations that can be performed on a product.")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/v1/products")
public class ProductRestController {
    private static final Logger LOG = LoggerFactory.getLogger(ProductRestController.class.getName());

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "Get products", description = "Retrieve all existing products",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Access is only allowed for authenticated users", responseCode = "401")
            }
    )
    @GetMapping
    public List<Product> getAllProducts(@AuthenticationPrincipal(errorOnInvalidType = true) ProductUser productUser) {
        LOG.info("Return all products using authenticated user '{}'", productUser.getDisplayName());
        return productService.findAll();
    }
}
