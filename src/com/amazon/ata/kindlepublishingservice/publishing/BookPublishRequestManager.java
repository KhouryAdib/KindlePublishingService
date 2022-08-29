package com.amazon.ata.kindlepublishingservice.publishing;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.Queue;

public class BookPublishRequestManager {

    private Queue<BookPublishRequest> requestQueue;

    @Inject
    public BookPublishRequestManager() {
    this.requestQueue = new LinkedList<BookPublishRequest>();
    }

    public void addBookPublishRequest(BookPublishRequest request){
        requestQueue.add(request);
    }

    public BookPublishRequest getBookPublishRequestToProcess() {

        if (requestQueue.peek().equals(null)) {
            return null;
        } else {
            return requestQueue.remove();
            }
        }
    }