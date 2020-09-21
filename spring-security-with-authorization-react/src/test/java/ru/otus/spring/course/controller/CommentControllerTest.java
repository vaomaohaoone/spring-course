package ru.otus.spring.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.spring.course.config.security.UserRoles;
import ru.otus.spring.course.data.CommentBody;
import ru.otus.spring.course.documents.Book;
import ru.otus.spring.course.documents.Comment;
import ru.otus.spring.course.migration.MigrationConfig;
import ru.otus.spring.course.repository.BookRepository;
import ru.otus.spring.course.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CommentControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    private MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void before() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(roles = UserRoles.USER_ROLE)
    void getAllCommentsWithUserRole() throws Exception {
        String result = mvc.perform(get(Endpoints.COMMENTS))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Comment> comments = objectMapper.readValue(result, new TypeReference<List<Comment>>() {
        });
        assertThat(comments.size()).isEqualTo(2);
    }

    @Test
    @WithMockUser(roles = UserRoles.COMMENTER_ROLE)
    void getAllCommentsWithCommenterRole() throws Exception {
        String result = mvc.perform(get(Endpoints.COMMENTS))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Comment> comments = objectMapper.readValue(result, new TypeReference<List<Comment>>() {
        });
        assertThat(comments.size()).isEqualTo(2);
    }

    @Test
    @WithMockUser(roles = "OTHER")
    void getAllCommentsWithOtherRoleShouldReturn403Status() throws Exception {
        mvc.perform(get(Endpoints.COMMENTS))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = UserRoles.COMMENTER_ROLE)
    void saveCommentWithCommenterRoleShouldOk() throws Exception {
        Comment comment = new Comment().setText("Very well!!!");
        String result = mvc.perform(post(Endpoints.COMMENTS).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(comment)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(objectMapper.readValue(result, Comment.class).getText()).isEqualTo(comment.getText());
    }

    @Test
    @WithMockUser(roles = {UserRoles.USER_ROLE, "OTHER"})
    void saveCommentWithUserAndOtherRolesShouldReturn403Status() throws Exception {
        Comment comment = new Comment().setText("Very well!!!");
        mvc.perform(post(Endpoints.COMMENTS).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(comment)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = UserRoles.COMMENTER_ROLE)
    void getCommentWithCommenterRoleShouldReturnComment() throws Exception {
        Optional<Comment> comment = commentRepository.findByText(MigrationConfig.positiveComment);
        assertThat(comment).isNotEmpty();
        mvc.perform(get(Endpoints.COMMENTS + "/" + comment.get().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(comment.get().getId()));
    }

    @Test
    @WithMockUser(roles = UserRoles.USER_ROLE)
    void getCommentWithUserRoleShouldReturnComment() throws Exception {
        Optional<Comment> comment = commentRepository.findByText(MigrationConfig.positiveComment);
        assertThat(comment).isNotEmpty();
        mvc.perform(get(Endpoints.COMMENTS + "/" + comment.get().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(comment.get().getId()));
    }

    @Test
    @WithMockUser(roles = "OTHER")
    void getCommentWithOtherRoleShouldReturn403Status() throws Exception {
        Optional<Comment> comment = commentRepository.findByText(MigrationConfig.positiveComment);
        assertThat(comment).isNotEmpty();
        mvc.perform(get(Endpoints.COMMENTS, comment.get().getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = UserRoles.COMMENTER_ROLE)
    void deleteCommentWithCommenterRoleShouldPassSuccessfully() throws Exception {
        Optional<Comment> comment = commentRepository.findByText(MigrationConfig.positiveComment);
        assertThat(comment).isNotEmpty();
        mvc.perform(delete(Endpoints.COMMENTS + "/" + comment.get().getId()))
                .andExpect(status().isOk());

        comment = commentRepository.findByText(MigrationConfig.positiveComment);
        assertThat(comment).isEmpty();
    }

    @Test
    @WithMockUser(roles = {UserRoles.USER_ROLE, "OTHER"})
    void deleteCommentWithUserAndOtherRolesShouldReturn403Status() throws Exception {
        Optional<Comment> comment = commentRepository.findByText(MigrationConfig.positiveComment);
        assertThat(comment).isNotEmpty();
        mvc.perform(delete(Endpoints.COMMENTS + "/" + comment.get().getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = UserRoles.COMMENTER_ROLE)
    void addCommentToBookWithCommenterRoleShouldPassSuccessfully() throws Exception {
        Optional<Book> book = bookRepository.findByName(MigrationConfig.defaultBook.getName());
        assertThat(book).isNotEmpty();
        CommentBody commentBody = new CommentBody().setIsbn(book.get().getId()).setText("Legendary book!");
        mvc.perform(post(Endpoints.COMMENTS + "/" + "add").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(commentBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(commentBody.getText()));
    }

    @Test
    @WithMockUser(roles = {UserRoles.USER_ROLE, "OTHER"})
    void addCommentToBookWithUserAndOtherRolesShouldReturn403Status() throws Exception {
        Optional<Book> book = bookRepository.findByName(MigrationConfig.defaultBook.getName());
        assertThat(book).isNotEmpty();
        CommentBody commentBody = new CommentBody().setIsbn(book.get().getId()).setText("Legendary book!");
        mvc.perform(post(Endpoints.COMMENTS + "/" + "add").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(commentBody)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = UserRoles.COMMENTER_ROLE)
    void getCommentsByIsbnWithCommenterRoleShouldReturnListOfComments() throws Exception {
        Optional<Book> book = bookRepository.findByName(MigrationConfig.defaultBook.getName());
        assertThat(book).isNotEmpty();
        mvc.perform(get(Endpoints.COMMENTS + "/isbn/" + book.get().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(roles = UserRoles.USER_ROLE)
    void getCommentsByIsbnWithUserRoleShouldReturnListOfComments() throws Exception {
        Optional<Book> book = bookRepository.findByName(MigrationConfig.defaultBook.getName());
        assertThat(book).isNotEmpty();
        mvc.perform(get(Endpoints.COMMENTS + "/isbn/" + book.get().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(roles = "OTHER")
    void getCommentsByIsbnWithOtherRoleShouldReturn403Status() throws Exception {
        Optional<Book> book = bookRepository.findByName(MigrationConfig.defaultBook.getName());
        assertThat(book).isNotEmpty();
        mvc.perform(get(Endpoints.COMMENTS + "/isbn/" + book.get().getId()))
                .andExpect(status().isForbidden());
    }
}
