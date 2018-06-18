package com.intexsoft.controller.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intexsoft.controller.dtomapper.BookDtoMapper;
import com.intexsoft.model.Book;
import com.intexsoft.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@WebServlet(value = "/api/v2/client/book/*")
public class BookServlet extends HttpServlet {


    private static final long serialVersionUID = 1L;

    @Autowired
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookDtoMapper bookDtoMapper;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {

            String[] splitsRequest = request.getPathInfo().split("/");

            if (splitsRequest.length != 2) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "no id specified");
                return;
            }

            UUID id = UUID.fromString(splitsRequest[splitsRequest.length - 1]);
            Book tmpBook = bookService.getById(id)
                    .toBlocking()
                    .first();

            out.write(objectMapper.writeValueAsString(bookDtoMapper.toDto(tmpBook)));

        } catch (EmptyResultDataAccessException ex) {
            log.error("Unable to get result, wrong ID", ex);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unable to get result, wrong ID");
        } catch (NoSuchElementException ex) {
            log.error("File not found",ex);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}

