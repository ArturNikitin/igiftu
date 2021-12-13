package com.svetka.igiftu.entity

import javax.persistence.AttributeOverride
import javax.persistence.AttributeOverrides
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
@AttributeOverrides(
    AttributeOverride(name = "currencyCode", column = Column(name = "currency_code")),
    AttributeOverride(name = "value", column = Column(name = "price")))
class Price(
    var currencyCode: Int? = 0,

    var value: Double?
) {
    override fun toString(): String {
        return "Price(currencyCode=$currencyCode, value=$value)"
    }
}