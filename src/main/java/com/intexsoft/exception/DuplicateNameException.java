package com.intexsoft.exception;

public class DuplicateNameException extends RuntimeException {

    private static final long serialVersionUID = 6747436261529821824L;

    public DuplicateNameException(String message) {
        super(message);
    }
}
