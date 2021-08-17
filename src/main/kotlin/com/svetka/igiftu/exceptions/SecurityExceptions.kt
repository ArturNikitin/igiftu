package com.svetka.igiftu.exceptions


open class SecurityException(msg: String) : Exception(msg)

class SecurityModificationException(msg: String) : SecurityException(msg)

class SecurityAccessException(msg: String) : SecurityException(msg)

class SecurityCreationException(msg: String) : SecurityException(msg)
