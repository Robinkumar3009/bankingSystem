package com.project.bankingsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.squareup.okhttp.MediaType;

@Component
public class SendChampConfig {
    private String sendChampUrl;
    private final MediaType mediaType = MediaType.parse("application/json");
    private String authorization;
    private String senderIdUrl;

    public String getSendChampUrl() {
        return sendChampUrl;
    }

    public void setSendChampUrl(String sendChampUrl) {
        this.sendChampUrl = sendChampUrl;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getSenderIdUrl() {
        return senderIdUrl;
    }

    public void setSenderIdUrl(String senderIdUrl) {
        this.senderIdUrl = senderIdUrl;
    }
}

