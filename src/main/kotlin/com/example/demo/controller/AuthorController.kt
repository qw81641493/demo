package com.example.demo.controller

import com.example.demo.repository.AuthorRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/author")
class AuthorController(
    private val authorRepository: AuthorRepository
) {

    @GetMapping("/{id}")
    @Cacheable(cacheNames = ["author"], key = "#id")
    fun getAuthor(@PathVariable("id") id: Long) =
        authorRepository.find(id)

}