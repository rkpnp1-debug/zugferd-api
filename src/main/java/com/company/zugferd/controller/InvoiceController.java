package com.company.zugferd.controller;

import com.company.zugferd.dto.InvoiceRequest;
import com.company.zugferd.dto.InvoiceResponse;
import com.company.zugferd.service.ZugferdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invoices")
@Tag(name = "ZUGFeRD", description = "Generate CII-compliant ZUGFeRD PDF/A-3 invoices")
public class InvoiceController {

    private static final Logger log = LoggerFactory.getLogger(InvoiceController.class);

    private final ZugferdService service;

    public InvoiceController(ZugferdService service) {
        this.service = service;
    }

    /**
     * POST /api/v1/invoices/zugferd
     * Requires scope "zugferd.generate" (JWT) or valid API-Key.
     */
    @PostMapping(
            value = "/zugferd",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('SCOPE_zugferd.generate')")
    @Operation(
            summary = "Create ZUGFeRD PDF/A-3",
            description = "Accepts invoice data + PDF file name (already present in input share). "
                    + "Returns the generated ZUGFeRD file name and validation status.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    public ResponseEntity<InvoiceResponse> createZugferd(@Valid @RequestBody InvoiceRequest request) {
        log.info("ZUGFeRD request received. requestId={} invoice={} pdf={}",
                request.getRequestId(), request.getInvoiceNumber(), request.getPdfFileName());

        InvoiceResponse response = service.createZugferdInvoice(request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }
}
