package com.amazon.ata.kindlepublishingservice.dagger;

import com.amazon.ata.kindlepublishingservice.BookPublishTask;
import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublishRequestManager;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublisher;

import com.amazon.ata.kindlepublishingservice.publishing.NoOpTask;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dagger.Module;
import dagger.Provides;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public class PublishingModule {

    @Provides
    @Singleton
    public BookPublisher provideBookPublisher(ScheduledExecutorService scheduledExecutorService, BookPublishTask publishTask) {
        return new BookPublisher(scheduledExecutorService, publishTask);
    }


   /* @Provides
    @Singleton
    public BookPublisher provideBookPublisher(ScheduledExecutorService scheduledExecutorService) {

        return new BookPublisher(scheduledExecutorService, new BookPublishTask());
    }
*/
    @Provides
    @Singleton
    public ScheduledExecutorService provideBookPublisherScheduler() {

        return Executors.newScheduledThreadPool(4);
    }

    @Provides
    @Singleton
    public BookPublishTask provideBookPublishTask(BookPublishRequestManager bookPublishRequestManager,
                                                  PublishingStatusDao statusDao,
                                                  CatalogDao catalogDao){

        return new BookPublishTask(bookPublishRequestManager, statusDao,catalogDao);
    }

    @Provides
    @Singleton
    public BookPublishRequestManager provideBookPublishRequestManager(){
        return new BookPublishRequestManager(new ConcurrentLinkedQueue<>());
    }
}
