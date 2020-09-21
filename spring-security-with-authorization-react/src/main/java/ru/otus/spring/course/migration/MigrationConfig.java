package ru.otus.spring.course.migration;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.otus.spring.course.config.security.UserRoles;
import ru.otus.spring.course.documents.Author;
import ru.otus.spring.course.documents.Book;
import ru.otus.spring.course.documents.Style;
import ru.otus.spring.course.documents.User;

import java.time.Year;
import java.util.HashSet;

@ChangeLog
public class MigrationConfig {

    private static final Author defaultAuthor = new Author().setName("Alex").setSurname("Pushkin");
    private static final Style defaultStyle = new Style().setStyle("novel");
    private static final User user = new User().setName("user").setPassword("user");
    private static final User commenter = new User().setName("commenter").setPassword("commenter");
    public static final Book defaultBook = new Book().setName("Captain's daughter").setPublishedYear(Year.of(1836));
    public static final String positiveComment = "Greatest book! Recommend!";
    public static final String negativeComment = "Very difficult to read";

    @ChangeSet(order = "001", id = "addPushkin", author = "timofeev")
    public void insertPushkin(DB db) {
        DBCollection dbCollection = db.getCollection("authors");
        BasicDBObject doc = new BasicDBObject()
                .append("name", defaultAuthor.getName())
                .append("surname", defaultAuthor.getSurname())
                .append("books", new HashSet<>());
        dbCollection.insert(doc);
    }

    @ChangeSet(order = "002", id = "addCaptainsDaughter", author = "timofeev")
    public void insertCaptainsDaughterBook(DB db) {
        DBCollection dbCollection = db.getCollection("books");
        BasicDBObject doc = new BasicDBObject()
                .append("name", defaultBook.getName())
                .append("publishedYear", defaultBook.getPublishedYear().getValue())
                .append("authors", new HashSet<>())
                .append("styles", new HashSet<>());
        dbCollection.insert(doc);
    }

    @ChangeSet(order = "003", id = "addNovel", author = "timofeev")
    public void insertNovel(DB db) {
        DBCollection dbCollection = db.getCollection("styles");
        BasicDBObject doc = new BasicDBObject()
                .append("style", defaultStyle.getStyle())
                .append("books", new HashSet<>());
        dbCollection.insert(doc);
    }

    @ChangeSet(order = "004", id = "linkPushkinAndCaptainDaughter", author = "timofeev")
    public void linkPushkinAndCaptainDaughter(DB db) {
        DBCollection authorsCollection = db.getCollection("authors");
        DBCollection booksCollection = db.getCollection("books");

        DBObject bookDbObject = booksCollection.findOne((DBObject) new BasicDBObject().put("name", defaultBook.getName()));
        DBObject authorDbObject = authorsCollection.findOne((DBObject) new BasicDBObject().put("surname", defaultAuthor.getSurname()));
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
        queryBook.put("name", defaultBook.getName());

        BasicDBObject queryAuthor = new BasicDBObject();
        queryAuthor.put("surname", defaultAuthor.getSurname());

        booksCollection.update(queryBook, updatedBookDbObject);
        authorsCollection.update(queryAuthor, updatedAuthorDbObject);
    }

    @ChangeSet(order = "005", id = "linkNovelAndCaptainDaughter", author = "timofeev")
    public void linkNovelAndCaptainDaughter(DB db) {
        DBCollection stylesCollection = db.getCollection("styles");
        DBCollection booksCollection = db.getCollection("books");

        DBObject bookDbObject = booksCollection.findOne((DBObject) new BasicDBObject().put("name", defaultBook.getName()));
        DBObject styleDbObject = stylesCollection.findOne((DBObject) new BasicDBObject().put("style", defaultStyle.getStyle()));
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
        queryBook.put("name", defaultBook.getName());

        BasicDBObject queryStyle = new BasicDBObject();
        queryStyle.put("style", defaultStyle.getStyle());

        booksCollection.update(queryBook, updatedBookDbObject);
        stylesCollection.update(queryStyle, updatedStyleDbObject);
    }

    @ChangeSet(order = "006", id = "insertCommentsForPushkinBook", author = "timofeev")
    public void insertCommentsForPushkinBook(DB db) {
        DBCollection dbCollection = db.getCollection("comments");
        DBCollection booksCollection = db.getCollection("books");
        DBObject bookDbObject = booksCollection.findOne((DBObject) new BasicDBObject().append("name", defaultBook.getName()));

        BasicDBObject comment1 = new BasicDBObject()
                .append("text", positiveComment)
                .append("book", new DBRef("books", bookDbObject.get("_id")));
        dbCollection.insert(comment1);

        BasicDBObject comment2 = new BasicDBObject()
                .append("text", negativeComment)
                .append("book", new DBRef("books", bookDbObject.get("_id")));
        dbCollection.insert(comment2);
    }

    @ChangeSet(order = "007", id = "insertRoles", author = "timofeev")
    public void insertRoles(DB db) {
        DBCollection dbCollection = db.getCollection("roles");
        BasicDBObject firstRole = new BasicDBObject()
                .append("name", UserRoles.USER_ROLE);
        dbCollection.insert(firstRole);
        BasicDBObject secondRole = new BasicDBObject()
                .append("name", UserRoles.COMMENTER_ROLE);
        dbCollection.insert(secondRole);
    }

    @ChangeSet(order = "008", id = "insertSimpleUser", author = "timofeev")
    public void insertSimpleUser(DB db) {
        DBCollection dbCollection = db.getCollection("users");
        DBObject userRole = db.getCollection("roles").findOne((DBObject) new BasicDBObject().append("name", UserRoles.USER_ROLE));

        BasicDBObject doc = new BasicDBObject()
                .append("name", user.getName())
                .append("password", new BCryptPasswordEncoder().encode(user.getPassword()))
                .append("roles", new HashSet<>() {{
                    add(new DBRef("roles", userRole.get("_id")));
                }});
        dbCollection.insert(doc);
    }

    @ChangeSet(order = "009", id = "insertCommenter", author = "timofeev")
    public void insertCommenter(DB db) {
        DBCollection dbCollection = db.getCollection("users");
        DBObject commenterRole = db.getCollection("roles").findOne((DBObject) new BasicDBObject().append("name", UserRoles.COMMENTER_ROLE));

        BasicDBObject doc = new BasicDBObject()
                .append("name", commenter.getName())
                .append("password", new BCryptPasswordEncoder().encode(commenter.getPassword()))
                .append("roles", new HashSet<>() {{
                    add(new DBRef("roles", commenterRole.get("_id")));
                }});
        dbCollection.insert(doc);
    }

}
