package com.amazon.ata.kindlepublishingservice.controllers.activity;

import com.amazon.ata.kindlepublishingservice.converters.BookPublishRequestConverter;
import com.amazon.ata.kindlepublishingservice.converters.CatalogItemConverter;
import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.models.Book;
import com.amazon.ata.kindlepublishingservice.models.requests.RemoveBookFromCatalogRequest;
import com.amazon.ata.kindlepublishingservice.models.response.RemoveBookFromCatalogResponse;
import com.amazon.ata.kindlepublishingservice.models.response.SubmitBookForPublishingResponse;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublishRequest;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.inject.Inject;

public class RemoveBookFromCatalogActivity {

    private CatalogDao catalogDao;

    @Inject
    RemoveBookFromCatalogActivity(CatalogDao catalogDao) {
        this.catalogDao = catalogDao;
    }

    RemoveBookFromCatalogActivity(){
    }


    public RemoveBookFromCatalogResponse execute(final RemoveBookFromCatalogRequest request) {
     //   final BookPublishRequest bookPublishRequest = BookPublishRequestConverter.toBookPublishRequest(request);

        RemoveBookFromCatalogResponse item =  catalogDao.removeBookFromCatalog(request.getBookId());



        return RemoveBookFromCatalogResponse.builder()
                .withPublishingRecordId(item.getPublishingRecordId())
                .build();
        //catalogDao.removeBookFromCatalog(request.getBookId());
    }

    }

