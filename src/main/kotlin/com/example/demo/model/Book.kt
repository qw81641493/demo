package com.example.demo.model

import org.babyfish.jimmer.sql.*
import java.math.BigDecimal

@Entity
@Table(name = "book")
interface Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long

    @Key
    val name: String

    @Key
    val edition: Int

    val price: BigDecimal

    @ManyToOne
    val store: BookStore?

    @ManyToMany
    @JoinTable(
        name = "book_author_mapping",
        joinColumnName = "book_id",
        inverseJoinColumnName = "author_id"
    )
    val authors: List<Author>
}