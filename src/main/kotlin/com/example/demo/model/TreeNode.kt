package com.example.demo.model

import org.babyfish.jimmer.sql.*

@Entity
@Table(name = "tree_node")
interface TreeNode {

    @Id
    @Column(name = "node_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long

    val name: String

    @ManyToOne
    val parent: TreeNode?

    @IdView
    val parentId: Long?

    @OneToMany(mappedBy = "parent")
    val childNodes: List<TreeNode>
}