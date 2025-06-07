package com.example.demo.controller

import com.example.demo.model.TreeNode
import com.example.demo.repository.TreeNodeRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tree")
class TreeNodeController(
    private val treeNodeRepository: TreeNodeRepository
) {

    @GetMapping()
    @Cacheable(cacheNames = ["tree"], key = "'all'")
    fun getTree(): List<TreeNode> {
        return treeNodeRepository.findTree()
    }

    @GetMapping("/{id}")
    @Cacheable(cacheNames = ["tree"], key = "#id")
    fun getTreeById(@PathVariable id: Long): TreeNode {
        return treeNodeRepository.findBy(id)
    }
}