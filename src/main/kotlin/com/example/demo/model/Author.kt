package com.example.demo.model

import org.babyfish.jimmer.Formula
import org.babyfish.jimmer.sql.*

@Entity
@Table(name = "author")
interface Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long

    @Key
    val firstName: String?

    @Key
    val lastName: String

    /*
     * 这里，Gender是一个枚举，，代码稍后给出
     */
    val gender: Gender

    @ManyToMany(mappedBy = "authors")
    val books: List<Book>

    @Formula(dependencies = ["books.store.name"])
    val bookStoreName: List<String>
        get() = books.mapNotNull { it.store?.name }

    @Formula(dependencies = ["books.authors.firstName"])
    val bookAuthorFirstName: List<String>
        get() = books
            .flatMap { it.authors }
            .mapNotNull { it.firstName }
            .filter(String::isNotBlank)
            .distinct()
}