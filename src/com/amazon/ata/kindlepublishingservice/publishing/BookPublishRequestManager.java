package com.amazon.ata.kindlepublishingservice.publishing;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BookPublishRequestManager {

    private Queue<BookPublishRequest> requestQueue;

    @Inject
    public BookPublishRequestManager(ConcurrentLinkedQueue<BookPublishRequest> queue) {
    this.requestQueue = queue;
    }

    public void addBookPublishRequest(BookPublishRequest request){
        requestQueue.add(request);
    }

    public BookPublishRequest getBookPublishRequestToProcess() {

        return requestQueue.poll();
        }
    }