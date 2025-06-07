package com.example.demo.repository

import com.example.demo.model.TreeNode
import com.example.demo.model.by
import com.example.demo.model.id
import com.example.demo.model.parentId
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.isNull
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher

interface TreeNodeRepository : KRepository<TreeNode, Long> {
    fun findTree() =
        sql.createQuery(TreeNode::class) {
            where(table.parentId.isNull())
            select(
                table.fetch(
                    newFetcher(TreeNode::class)
                        .by {
                            allTableFields()
                            parentId()
                            `childNodes*`()
                        }
                ))
        }.execute()

    fun findBy(
        id: Long
    ) =
        sql.createQuery(TreeNode::class) {
            where(table.id.eq(id))
            select(
                table.fetch(
                    newFetcher(TreeNode::class)
                        .by {
                            allTableFields()
                            parentId()
                            `childNodes*`()
                        }
                ))
        }.fetchOne()
}