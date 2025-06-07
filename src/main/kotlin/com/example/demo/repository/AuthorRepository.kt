package com.example.demo.repository

import com.example.demo.model.Author
import com.example.demo.model.by
import com.example.demo.model.id
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher

interface AuthorRepository : KRepository<Author, Long> {

    fun find(
        id: Long
    ) =
        sql.createQuery(Author::class) {
            where(table.id.eq(id))
            select(table.fetch(newFetcher(Author::class).by {
                firstName()
                lastName()
            }))
        }.fetchOne()
}