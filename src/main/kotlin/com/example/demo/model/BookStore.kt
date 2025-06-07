package com.example.demo.model

import org.babyfish.jimmer.sql.*

@Entity
@Table(name = "book_store")
interface BookStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long

    @Key
    val name: String

    val website: String?

    @OneToMany(mappedBy = "store")
    val books: List<Book>
}