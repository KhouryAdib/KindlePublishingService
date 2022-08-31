package com.amazon.ata.kindlepublishingservice;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublishRequest;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublishRequestManager;
import com.amazon.ata.kindlepublishingservice.publishing.KindleFormatConverter;
import com.amazon.ata.kindlepublishingservice.publishing.KindleFormattedBook;

import javax.inject.Inject;

public class BookPublishTask implements Runnable{

    private BookPublishRequestManager bookPublishRequestManager;
    private PublishingStatusDao publishingstatusDao;
    private CatalogDao catalogDao;

    @Inject
    public BookPublishTask(BookPublishRequestManager bookPublishRequestManager,
                           PublishingStatusDao publishingstatusDao,
                           CatalogDao catalogDao){

        this.bookPublishRequestManager = bookPublishRequestManager;
        this.publishingstatusDao = publishingstatusDao;
        this.catalogDao = catalogDao;
    }

    public BookPublishTask() {

    }

    @Override
    public void run() {
        System.out.println("BookPublishTask.getBookPublishRequestToProcess()");
        BookPublishRequest request = bookPublishRequestManager.getBookPublishRequestToProcess();

        try {
            System.out.println("0");
            if(request!=null) {
                System.out.println("1");

                publishingstatusDao.setPublishingStatus(request.getPublishingRecordId(),
                        PublishingRecordStatus.IN_PROGRESS, request.getBookId());
                System.out.println("3");
                KindleFormattedBook formattedBook = KindleFormatConverter.format(request);
                System.out.println("4");
                CatalogItemVersion version = catalogDao.createOrUpdateBook(formattedBook);
                System.out.println("5");

                publishingstatusDao.setPublishingStatus(request.getPublishingRecordId(),
                        PublishingRecordStatus.SUCCESSFUL, version.getBookId());
            }
            else {
                System.out.println("queue empty");
            }
        }catch (Exception e){
            System.out.println("Error: "+e);
            publishingstatusDao.setPublishingStatus(request.getPublishingRecordId(),
                    PublishingRecordStatus.FAILED, request.getBookId());
        }
    }



    public BookPublishRequest getBookPublishRequestToProcess()
    {
        System.out.println("BookPublishTask.getBookPublishRequestToProcess()");
        BookPublishRequest request = bookPublishRequestManager.getBookPublishRequestToProcess();
        System.out.println("0");
        if(request==null){
            System.out.println("1");
             return null;}
        System.out.println("2");

        publishingstatusDao.setPublishingStatus(request.getPublishingRecordId(), PublishingRecordStatus.IN_PROGRESS,request.getBookId());
        System.out.println("3");
        KindleFormattedBook formattedBook = KindleFormatConverter.format(request);
        System.out.println("4");
        CatalogItemVersion version = catalogDao.createOrUpdateBook(formattedBook);
        System.out.println("5");
        if(version!=null) {
            publishingstatusDao.setPublishingStatus(request.getPublishingRecordId(), PublishingRecordStatus.SUCCESSFUL, request.getBookId());
        }
        else {
            publishingstatusDao.setPublishingStatus(request.getPublishingRecordId(), PublishingRecordStatus.FAILED,request.getBookId());
        }


        return BookPublishRequest.builder()
                .withBookId(version.getBookId())
                .withAuthor(version.getAuthor())
                .withGenre(version.getGenre())
                .withPublishingRecordId(request.getPublishingRecordId())
                .withText(version.getTitle())
                .withTitle(version.getTitle())
                .build();
    }


}
