package ru.otus.spring.course.migration;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.*;

import java.util.HashSet;

@ChangeLog
public class MigrationConfig {

    @ChangeSet(order = "001", id = "addPushkin", author = "timofeev")
    public void insertPushkin(DB db) {
        DBCollection dbCollection = db.getCollection("authors");
        BasicDBObject doc = new BasicDBObject()
                .append("name", "Alex")
                .append("surname", "Pushkin")
                .append("books", new HashSet<>());
        dbCollection.insert(doc);
    }

    @ChangeSet(order = "002", id = "addCaptainsDaughter", author = "timofeev")
    public void insertCaptainsDaughterBook(DB db) {
        DBCollection dbCollection = db.getCollection("books");
        BasicDBObject doc = new BasicDBObject()
                .append("name", "Captain's daughter")
                .append("publishedYear", 1836)
                .append("authors", new HashSet<>())
                .append("styles", new HashSet<>());
        dbCollection.insert(doc);
    }

    @ChangeSet(order = "003", id = "addNovel", author = "timofeev")
    public void insertNovel(DB db) {
        DBCollection dbCollection = db.getCollection("styles");
        BasicDBObject doc = new BasicDBObject()
                .append("style", "novel")
                .append("books", new HashSet<>());
        dbCollection.insert(doc);
    }

    @ChangeSet(order = "004", id = "linkPushkinAndCaptainDaughter", author = "timofeev")
    public void linkPushkinAndCaptainDaughter(DB db) {
        DBCollection authorsCollection = db.getCollection("authors");
        DBCollection booksCollection = db.getCollection("books");

        DBObject bookDbObject = booksCollection.findOne((DBObject) new BasicDBObject().put("name", "Captain's daughter"));
        DBObject authorDbObject = authorsCollection.findOne((DBObject) new BasicDBObject().put("surname", "Pushkin"));
        BasicDBObject updatedBookDbObject = new BasicDBObject();
        BasicDBObject newBookDbObject = new BasicDBObject();
        newBookDbObject.put("authors", new HashSet<>() {{
            add(new DBRef("authors", authorDbObject.get("_id")));
        }});
        updatedBookDbObject.put("$set", newBookDbObject);

        BasicDBObject updatedAuthorDbObject = new BasicDBObject();
        BasicDBObject newAuthorDbObject = new BasicDBObject();
        newAuthorDbObject.put("books", new HashSet<>() {{
            add(new DBRef("books", bookDbObject.get("_id")));
        }});
        updatedAuthorDbObject.put("$set", newAuthorDbObject);

        BasicDBObject queryBook = new BasicDBObject();
        queryBook.put("name", "Captain's daughter");

        BasicDBObject queryAuthor = new BasicDBObject();
        queryAuthor.put("surname", "Pushkin");

        booksCollection.update(queryBook, updatedBookDbObject);
        authorsCollection.update(queryAuthor, updatedAuthorDbObject);
    }

    @ChangeSet(order = "005", id = "linkNovelAndCaptainDaughter", author = "timofeev")
    public void linkNovelAndCaptainDaughter(DB db) {
        DBCollection stylesCollection = db.getCollection("styles");
        DBCollection booksCollection = db.getCollection("books");

        DBObject bookDbObject = booksCollection.findOne((DBObject) new BasicDBObject().put("name", "Captain's daughter"));
        DBObject styleDbObject = stylesCollection.findOne((DBObject) new BasicDBObject().put("style", "novel"));
        BasicDBObject updatedBookDbObject = new BasicDBObject();
        BasicDBObject newBookDbObject = new BasicDBObject();
        newBookDbObject.put("styles", new HashSet<>() {{
            add(new DBRef("styles", styleDbObject.get("_id")));
        }});
        updatedBookDbObject.put("$set", newBookDbObject);

        BasicDBObject updatedStyleDbObject = new BasicDBObject();
        BasicDBObject newStyleDbObject = new BasicDBObject();
        newStyleDbObject.put("books", new HashSet<>() {{
            add(new DBRef("books", bookDbObject.get("_id")));
        }});
        updatedStyleDbObject.put("$set", newStyleDbObject);

        BasicDBObject queryBook = new BasicDBObject();
        queryBook.put("name", "Captain's daughter");

        BasicDBObject queryStyle = new BasicDBObject();
        queryStyle.put("style", "novel");

        booksCollection.update(queryBook, updatedBookDbObject);
        stylesCollection.update(queryStyle, updatedStyleDbObject);
    }

    @ChangeSet(order = "006", id = "insertCommentsForPushkinBook", author = "timofeev")
    public void insertCommentsForPushkinBook(DB db) {
        DBCollection dbCollection = db.getCollection("comment");
        DBCollection booksCollection = db.getCollection("books");
        DBObject bookDbObject = booksCollection.findOne((DBObject) new BasicDBObject().put("name", "Captain's daughter"));

        BasicDBObject comment1 = new BasicDBObject()
                .append("text", "Very cool book!!!")
                .append("book", new DBRef("books", bookDbObject.get("_id")));
        dbCollection.insert(comment1);

        BasicDBObject comment2 = new BasicDBObject()
                .append("text", "Greatest book! Recommend!")
                .append("book", new DBRef("books", bookDbObject.get("_id")));
        dbCollection.insert(comment2);
    }

}
