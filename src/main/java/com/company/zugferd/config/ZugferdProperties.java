package com.company.zugferd.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Component
@ConfigurationProperties(prefix = "zugferd")
@Validated
public class ZugferdProperties {

    @NotBlank
    private String inputRoot = "/data/zugferd/input";

    @NotBlank
    private String outputRoot = "/data/zugferd/output";

    @Min(1)
    @Max(50)
    private int maxFileSizeMb = 20;

    @NotBlank
    private String allowedExtensions = "pdf";

    @NotBlank
    private String profile = "EN16931";

    private String producer = "Endur-ZUGFeRD-API";
    private String creator = "ZUGFeRD-Service";

    @Min(1)
    @Max(100)
    private int maxConcurrentRequests = 10;

    public String getInputRoot() { return inputRoot; }
    public void setInputRoot(String inputRoot) { this.inputRoot = inputRoot; }
    public String getOutputRoot() { return outputRoot; }
    public void setOutputRoot(String outputRoot) { this.outputRoot = outputRoot; }
    public int getMaxFileSizeMb() { return maxFileSizeMb; }
    public void setMaxFileSizeMb(int maxFileSizeMb) { this.maxFileSizeMb = maxFileSizeMb; }
    public String getAllowedExtensions() { return allowedExtensions; }
    public void setAllowedExtensions(String allowedExtensions) { this.allowedExtensions = allowedExtensions; }
    public String getProfile() { return profile; }
    public void setProfile(String profile) { this.profile = profile; }
    public String getProducer() { return producer; }
    public void setProducer(String producer) { this.producer = producer; }
    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }
    public int getMaxConcurrentRequests() { return maxConcurrentRequests; }
    public void setMaxConcurrentRequests(int maxConcurrentRequests) { this.maxConcurrentRequests = maxConcurrentRequests; }
}
