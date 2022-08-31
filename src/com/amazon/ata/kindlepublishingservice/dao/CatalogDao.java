package com.amazon.ata.kindlepublishingservice.dao;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.Book;
import com.amazon.ata.kindlepublishingservice.models.response.RemoveBookFromCatalogResponse;
import com.amazon.ata.kindlepublishingservice.publishing.KindleFormattedBook;
import com.amazon.ata.kindlepublishingservice.utils.KindlePublishingUtils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import javax.inject.Inject;

public class CatalogDao {

    private final DynamoDBMapper dynamoDbMapper;

    /**
     * Instantiates a new CatalogDao object.
     *
     * @param dynamoDbMapper The {@link DynamoDBMapper} used to interact with the catalog table.
     */
    @Inject
    public CatalogDao(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    /**
     * Returns the latest version of the book from the catalog corresponding to the specified book id.
     * Throws a BookNotFoundException if the latest version is not active or no version is found.
     * @param bookId Id associated with the book.
     * @return The corresponding CatalogItem from the catalog table.
     */
    public CatalogItemVersion getBookFromCatalog(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null || book.isInactive()) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }

        return book;
    }


    public CatalogItemVersion removeBookFromCatalog(String bookId) {
     /*-   CatalogItemVersion book = new CatalogItemVersion();
        book.setBookId(bookId);

        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression()
                .withHashKeyValues(book)
                .withScanIndexForward(false)
                .withLimit(1);

        List<CatalogItemVersion> results = dynamoDbMapper.query(CatalogItemVersion.class, queryExpression);

        results.get(0).setInactive(true);

        dynamoDbMapper.save(results.get(0));

        return RemoveBookFromCatalogResponse.builder()
                .withBook()
                .build();


      */

        CatalogItemVersion item = getLatestVersionOfBook(bookId);
        item.setInactive(true);

        dynamoDbMapper.save(item);

        return item;

    }

    // Returns null if no version exists for the provided bookId
    private CatalogItemVersion getLatestVersionOfBook(String bookId) {
        CatalogItemVersion book = new CatalogItemVersion();
        book.setBookId(bookId);

        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression()
            .withHashKeyValues(book)
            .withScanIndexForward(false)
            .withLimit(1);

        List<CatalogItemVersion> results = dynamoDbMapper.query(CatalogItemVersion.class, queryExpression);
        if (results.isEmpty()) {
            throw new BookNotFoundException("Book not found");
        }
        return results.get(0);
    }

    public boolean validateBookExists(String bookId){

        if(bookId==null || bookId.isEmpty()){ return false; }

        CatalogItemVersion book = new CatalogItemVersion();
        book.setBookId(bookId);

        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression()
                .withHashKeyValues(book)
                .withScanIndexForward(false)
                .withLimit(1);

        List<CatalogItemVersion> results = dynamoDbMapper.query(CatalogItemVersion.class, queryExpression);
        if (results.isEmpty()) {
            throw new BookNotFoundException("Book not found");
        }
        return false;
    }

    public CatalogItemVersion createOrUpdateBook(KindleFormattedBook book){
        System.out.println("createOrUpdateBook: start");

     //   CatalogItemVersion bookInCatalog = new CatalogItemVersion();

       //      bookInCatalog = getLatestVersionOfBook(book.getBookId());


        if( book.getBookId()==null){
            System.out.println("Creating Book");
            String bookId = KindlePublishingUtils.generateBookId();
            CatalogItemVersion item = new CatalogItemVersion();
            item.setInactive(false);
            item.setBookId(bookId);
            item.setAuthor(book.getAuthor());
            item.setGenre(book.getGenre());
            item.setText(book.getText());
            item.setTitle(book.getTitle());
            item.setVersion(1);
            dynamoDbMapper.save(item);
            System.out.println("createOrUpdateBook: New item created end");
            return item;

        } else {

            System.out.println("Updating Book");
            String bookId = KindlePublishingUtils.generateBookId();

            CatalogItemVersion item = getLatestVersionOfBook(book.getBookId());
            removeBookFromCatalog(item.getBookId());
            item.setBookId(bookId);
            item.setAuthor(book.getAuthor());
            item.setGenre(book.getGenre());
            item.setText(book.getText());
            item.setTitle(book.getTitle());

            item.setVersion(item.getVersion() + 1);

            item.setInactive(false);
            dynamoDbMapper.save(item);

            System.out.println("createOrUpdateBook: Updated item end");
            return item;
        }


    }
}
