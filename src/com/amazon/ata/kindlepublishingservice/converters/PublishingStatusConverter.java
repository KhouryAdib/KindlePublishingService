package com.amazon.ata.kindlepublishingservice.converters;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatusRecord;

import java.util.ArrayList;
import java.util.List;

public class PublishingStatusConverter {

    private  PublishingStatusConverter(){}

    public static List<PublishingStatusRecord>
            toPublishingStatusRecordList(List<PublishingStatusItem> publishingStatusItemList){

        List<PublishingStatusRecord> recordList = new ArrayList<>();

        for (PublishingStatusItem item : publishingStatusItemList) {

            recordList.add(PublishingStatusRecord.builder()
                            .withBookId(item.getBookId())
                            .withStatus(item.getStatus().toString())
                            .withStatusMessage(item.getStatusMessage())
                            .build()
            );
        }

        return recordList;


    }
}
