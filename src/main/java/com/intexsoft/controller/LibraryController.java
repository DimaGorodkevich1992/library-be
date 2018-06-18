package com.intexsoft.controller;

import com.intexsoft.controller.dtomapper.BookDtoMapper;
import com.intexsoft.controller.dtomapper.LibraryDtoMapper;
import com.intexsoft.dto.BookDto;
import com.intexsoft.dto.LibraryDto;
import com.intexsoft.service.LibraryService;
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


@Api("/api/v1/client/library")
@RestController()
@RequestMapping("/api/v1/client/library")
public class LibraryController extends CommonController {

    @Autowired
    private LibraryService libraryService;
    @Autowired
    private LibraryDtoMapper libraryDtoMapper;
    @Autowired
    private BookDtoMapper bookDtoMapper;

    @ApiOperation(value = "store library", notes = "Store new library in database", response = LibraryDto.class)
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public DeferredResult<LibraryDto> storeLibrary(
            @ApiParam(required = true, name = "libraryDto", value = "The parameter (INFO,ID,VERSION) does not need to be transmitted")
            @RequestBody LibraryDto libraryDto) {
        DeferredResult<LibraryDto> result = getDeferredResult();
        Subscription subscription = libraryService.store(libraryDtoMapper.fromDto(libraryDto))
                .map(libraryDtoMapper::toDto)
                .compose(convertToDeferredResult(result))
                .subscribe();
        result.onCompletion(subscription::unsubscribe);
        return result;
    }

    @ApiOperation(value = "get library by ID", notes = "Get library by Id")
    @GetMapping("/{id}")
    public DeferredResult<LibraryDto> getById(
            @ApiParam(required = true, name = "id", value = "ID of the library you want to get")
            @PathVariable("id") UUID id) {
        DeferredResult<LibraryDto> result = getDeferredResult();
        Subscription subscription = libraryService.getById(id)
                .map(libraryDtoMapper::toDto)
                .compose(convertToDeferredResult(result))
                .subscribe();
        result.onCompletion(subscription::unsubscribe);
        return result;
    }

    @GetMapping("/{id}/books")
    public DeferredResult<List<BookDto>> getBooksForLibrary(
            @ApiParam(required = true, name = "id", value = "ID from the library to find books")
            @PathVariable("id") UUID id) {
        DeferredResult<List<BookDto>> result = getDeferredResult();
        Subscription subscription = libraryService.searchBooks(id)
                .map(bookDtoMapper::toDto)
                .toList()
                .compose(convertToDeferredResult(result))
                .subscribe();
        result.onCompletion(subscription::unsubscribe);
        return result;
    }


    @ApiOperation(value = "search Library", notes = "Search library. If the args is null return all library")
    @GetMapping()
    public DeferredResult<List<LibraryDto>> searchLibrary(
            @ApiParam(name = "name")
            @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "address")
            @RequestParam(value = "address", required = false) String address) {
        DeferredResult<List<LibraryDto>> result = getDeferredResult();
        Subscription subscription = libraryService.searchLibrary(name, address)
                .map(libraryDtoMapper::toDto)
                .toList()
                .compose(convertToDeferredResult(result))
                .subscribe();
        result.onCompletion(subscription::unsubscribe);

        return result;
    }

    @ApiOperation(value = "update library", notes = "Update library")
    @PutMapping()
    public DeferredResult<LibraryDto> updateLibrary(
            @ApiParam(required = true, name = "libraryDto")
            @RequestBody LibraryDto libraryDto) {
        DeferredResult<LibraryDto> result = getDeferredResult();
        Subscription subscription = libraryService.update(libraryDtoMapper.fromDto(libraryDto))
                .map(libraryDtoMapper::toDto)
                .compose(convertToDeferredResult(result))
                .subscribe();
        result.onCompletion(subscription::unsubscribe);
        return result;
    }

    @ApiOperation(value = "delete library by ID", notes = "Delete library by Id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DeferredResult<UUID> deleteLibrary(
            @ApiParam(required = true, name = "id", value = "ID of the library you want to delete")
            @PathVariable("id") UUID id) {
        DeferredResult<UUID> result = getDeferredResult();
        Subscription subscription = libraryService.delete(id)
                .compose(convertToDeferredResult(result))
                .subscribe();
        result.onCompletion(subscription::unsubscribe);
        return result;
    }


}
