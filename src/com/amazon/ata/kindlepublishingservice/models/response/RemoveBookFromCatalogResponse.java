package com.amazon.ata.kindlepublishingservice.models.response;

import com.amazon.ata.kindlepublishingservice.models.Book;

import java.util.Objects;

public class RemoveBookFromCatalogResponse {
    private String publishingRecordId;

    public RemoveBookFromCatalogResponse(Builder builder) {
        this.publishingRecordId = builder.publishingRecordId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RemoveBookFromCatalogResponse)) return false;
        RemoveBookFromCatalogResponse that = (RemoveBookFromCatalogResponse) o;
        return getPublishingRecordId().equals(that.getPublishingRecordId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPublishingRecordId());
    }

    public String getPublishingRecordId() {
        return publishingRecordId;
    }

    public void setPublishingRecordId(String publishingRecordId) {
        this.publishingRecordId = publishingRecordId;
    }

    public static Builder builder() {return new Builder();}

    public static final class Builder {
        private String publishingRecordId;

        public Builder withPublishingRecordId(String publishingRecordId) {
            this.publishingRecordId = publishingRecordId;
            return this;
        }

        public RemoveBookFromCatalogResponse build() {return new RemoveBookFromCatalogResponse(this);}
    }
}
