package com.intexsoft.controller;

import com.intexsoft.controller.dtomapper.BookDtoMapper;
import com.intexsoft.controller.dtomapper.BookDtoMapperWithLibraries;
import com.intexsoft.dto.BookDto;
import com.intexsoft.dto.BookDtoWithLibraries;
import com.intexsoft.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Subscription;

import java.util.List;
import java.util.UUID;

@Api("/api/v1/client/book")
@RestController()
@RequestMapping("/api/v1/client/book")
public class BookController extends CommonController {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookDtoMapper bookDtoMapper;
    @Autowired
    private BookDtoMapperWithLibraries bookDtoMapperWithLibraries;

    @ApiOperation(value = "store book", notes = "Store new book in database", response = BookDto.class)
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public DeferredResult<BookDto> storeBook(
            @ApiParam(required = true, name = "bookDto", value = "The parameter (INFO,ID,VERSION) does not need to be transmitted")
            @RequestBody BookDto bookDto) {
        DeferredResult<BookDto> result = getDeferredResult();
        Subscription subscription = bookService.store(bookDtoMapper.fromDto(bookDto))
                .map(bookDtoMapper::toDto)
                .compose(convertToDeferredResult(result))
                .subscribe();
        result.onCompletion(subscription::unsubscribe);
        return result;
    }

    @ApiOperation(value = "get book by ID", notes = "Get book by Id")
    @GetMapping("/{id}")
    public DeferredResult<BookDto> getById(
            @ApiParam(required = true, name = "id", value = "ID of the book you want to get")
            @PathVariable("id") UUID id,
            @ApiParam(name = "libraries")
            @RequestParam(name = "libraries") Boolean libraries) {
        DeferredResult<BookDto> result = getDeferredResult();
        Subscription subscription = (libraries ?
                bookService.getByIdWithLibrary(id)
                        .map(bookDtoMapperWithLibraries::toDto) :
                bookService.getById(id)
                        .map(bookDtoMapper::toDto))
                .compose(convertToDeferredResult(result))
                .subscribe();
        result.onCompletion(subscription::unsubscribe);
        return result;
    }

    @ApiOperation(value = "search Book", notes = "Search book . If the args is null return all books")
    @GetMapping()
    public DeferredResult<List<BookDto>> searchBook(
            @ApiParam(name = "name")
            @RequestParam(value = "name", required = false) String bookName,
            @ApiParam(name = "author")
            @RequestParam(value = "author", required = false) String bookAuthor) {
        DeferredResult<List<BookDto>> result = getDeferredResult();
        Subscription subscription = bookService.searchBook(bookName, bookAuthor)
                .map(bookDtoMapper::toDto)
                .toList()
                .compose(convertToDeferredResult(result))
                .subscribe();
        result.onCompletion(subscription::unsubscribe);
        return result;
    }

    @ApiOperation(value = "add Library")
    @PostMapping("/{id}/library/{libraryid}")
    public DeferredResult<BookDtoWithLibraries> addLibrary(
            @ApiParam(required = true, name = "id", value = "ID book to add library")
            @PathVariable("id") UUID id,
            @ApiParam(required = true, name = "libraryId", value = "ID library to add in book")
            @PathVariable("libraryid") UUID libraryId) {
        DeferredResult<BookDtoWithLibraries> result = getDeferredResult();
        Subscription subscription = bookService.addLibrary(id, libraryId)
                .map(bookDtoMapperWithLibraries::toDto)
                .compose(convertToDeferredResult(result))
                .subscribe();
        result.onCompletion(subscription::unsubscribe);
        return result;
    }

    @ApiOperation(value = "update book", notes = "Update book")
    @PutMapping()
    public DeferredResult<BookDto> updateBook(
            @ApiParam(required = true, name = "bookDto", value = "The parameter (INFO) does not need to be transmitted")
            @RequestBody BookDto bookDto) {
        DeferredResult<BookDto> result = getDeferredResult();
        Subscription subscription = bookService.update(bookDtoMapper.fromDto(bookDto))
                .map(bookDtoMapper::toDto)
                .compose(convertToDeferredResult(result))
                .subscribe();
        result.onCompletion(subscription::unsubscribe);
        return result;
    }

    @ApiOperation(value = "delete book by ID", notes = "Delete book by Id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DeferredResult<UUID> deleteBook(
            @ApiParam(required = true, name = "id", value = "ID of the book you want to delete")
            @PathVariable("id") UUID id) {
        DeferredResult<UUID> result = getDeferredResult();
        Subscription subscription = bookService.delete(id)
                .compose(convertToDeferredResult(result))
                .subscribe();
        result.onCompletion(subscription::unsubscribe);
        return result;
    }

}
