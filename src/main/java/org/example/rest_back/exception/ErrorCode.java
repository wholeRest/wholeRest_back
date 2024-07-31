package org.example.rest_back.exception;

public enum ErrorCode {
    EMPTY_FILE_EXCEPTION("File is empty or filename is null."),
    IO_EXCEPTION_ON_IMAGE_UPLOAD("IOException occurred during image upload."),
    NO_FILE_EXTENTION("No file extension found."),
    INVALID_FILE_EXTENTION("Invalid file extension."),
    PUT_OBJECT_EXCEPTION("Exception occurred while putting object to S3."),
    IO_EXCEPTION_ON_IMAGE_DELETE("IOException occurred during image delete.");

    private String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
