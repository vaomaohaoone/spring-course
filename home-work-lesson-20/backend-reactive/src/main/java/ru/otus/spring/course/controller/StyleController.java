package ru.otus.spring.course.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.course.documents.Style;
import ru.otus.spring.course.service.StyleService;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(value = Endpoints.STYLES)
public class StyleController {

    private final StyleService styleService;

    @PostMapping
    public Mono<Style> saveStyle(@RequestBody Style style) {
        return ResponseEntity.ok(styleService.saveStyle(style));
    }

    @GetMapping("/{id}")
    public Mono<Style> geStyle(@PathVariable("id") String id) {
        return ResponseEntity.of(styleService.getStyleById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") String id) {
        styleService.deleteById(id);
    }

    @GetMapping
    public Flux<Style> getAllStyles() {
        return styleService.getAll();
    }

    @PostMapping("/name/{name}")
    public Mono<Style> createStyle(@PathVariable("name") String name) {
        return ResponseEntity.ok(styleService.createStyle(name));
    }

    @PutMapping("/{styleId}")
    public Mono<Style> updateStyle(@PathVariable String styleId, @RequestBody Style style) {
        if (styleService.getStyleById(styleId).isPresent()) {
            Style result = styleService.saveStyle(style);
            return ResponseEntity.ok(result);
        }
        else return ResponseEntity.of(Optional.empty());
    }
}
