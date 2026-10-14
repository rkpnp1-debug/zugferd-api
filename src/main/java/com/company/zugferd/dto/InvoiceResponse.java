package com.company.zugferd.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response after ZUGFeRD generation attempt")
public class InvoiceResponse {

    private boolean success;
    private String requestId;
    private String invoiceNumber;
    private String outputFileName;
    private String validationStatus;
    private String message;
    private String errorDetails;

    public static InvoiceResponse ok(String requestId, String invoiceNumber, String outputFileName, String validationStatus) {
        InvoiceResponse r = new InvoiceResponse();
        r.success = true;
        r.requestId = requestId;
        r.invoiceNumber = invoiceNumber;
        r.outputFileName = outputFileName;
        r.validationStatus = validationStatus;
        r.message = "ZUGFeRD PDF/A-3 created successfully";
        return r;
    }

    public static InvoiceResponse fail(String requestId, String message, String errorDetails) {
        InvoiceResponse r = new InvoiceResponse();
        r.success = false;
        r.requestId = requestId;
        r.message = message;
        r.errorDetails = errorDetails;
        r.validationStatus = "FAILED";
        return r;
    }

    public boolean isSuccess() { return success; }
    public String getRequestId() { return requestId; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public String getOutputFileName() { return outputFileName; }
    public String getValidationStatus() { return validationStatus; }
    public String getMessage() { return message; }
    public String getErrorDetails() { return errorDetails; }
}
