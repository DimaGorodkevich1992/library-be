package com.intexsoft.controller;

import com.intexsoft.controller.dtomapper.BookDtoMapper;
import com.intexsoft.controller.dtomapper.LibraryDtoMapper;
import com.intexsoft.dto.BookDto;
import com.intexsoft.dto.LibraryDto;
import com.intexsoft.model.LibraryBookId;
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
    private LibraryDtoMapper libraryDtoMapper;



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
            @PathVariable("id") UUID id) {
        DeferredResult<BookDto> result = getDeferredResult();
        Subscription subscription = bookService.getById(id)
                .map(bookDtoMapper::toDto)
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

    @GetMapping("/{id}/libraries")
    public DeferredResult<List<LibraryDto>> getLibraryForBook(
            @ApiParam(required = true, name = "id", value = "ID from the book to find libraries")
            @PathVariable("id") UUID id) {
        DeferredResult<List<LibraryDto>> result = getDeferredResult();
        Subscription subscription = bookService.searchLibraries(id)
                .map(libraryDtoMapper::toDto)
                .toList()
                .compose(convertToDeferredResult(result))
                .subscribe();
        result.onCompletion(subscription::unsubscribe);
        return result;
    }

    @ApiOperation(value = "add Library")
    @PostMapping("/library")
    public DeferredResult<List<BookDto>> addLibrary(
            @ApiParam(required = true, name = "libraryBookId", value = "Add library to book")
            @RequestBody LibraryBookId libraryBookId) {
        DeferredResult<List<BookDto>> result = getDeferredResult();
        Subscription subscription = bookService.addLibrary(libraryBookId)
                .map(bookDtoMapper::toDto)
                .toList()
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
