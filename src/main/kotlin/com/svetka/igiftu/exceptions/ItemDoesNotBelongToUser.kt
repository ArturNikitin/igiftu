package com.svetka.igiftu.exceptions

import javax.security.sasl.AuthenticationException

class ItemDoesNotBelongToUser(msg: String) : AuthenticationException(msg)