package com.example.RuleEnginAST.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name = "nodes")
@Entity
public class Node {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private String type;  // "operator" or "operand"
    private String value; // actual condition or operator ("AND", "OR")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "left_node_id")
    private Node left; 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "right_node_id")// left child for binary operations
    private Node right;   // right child for binary operations

    // Constructor for operand nodes (no children, just a condition)
    public Node(String type, String value) {
        this.type = type;
        this.value = value;
        this.left = null;  // No children for operands
        this.right = null;
    }

    // Constructor for operator nodes (with left and right children)
    public Node(String type, Node left, Node right) {
        this.type = type;
        this.value = type;  // Assign the operator value (e.g., "AND", "OR")
        this.left = left;
        this.right = right;
    }

	// Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }
}