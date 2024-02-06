package com.example.musicmetadata.adapter.driven.db.exception

class DatabaseException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
