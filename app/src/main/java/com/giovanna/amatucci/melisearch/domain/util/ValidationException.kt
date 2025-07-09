package com.giovanna.amatucci.melisearch.domain.util

sealed class ValidationException(message: String) : Exception(message)

class EmptySearchQueryException : ValidationException("O termo de busca não pode ser vazio.")
class ShortSearchQueryException(val minLength: Int) : ValidationException(
    "O termo de busca deve ter no mínimo $minLength caracteres."
)
class InvalidProductIdException : ValidationException("O formato do ID do produto é inválido.")