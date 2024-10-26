package com.example.RuleEnginAST.dto;

import java.util.Map;

import com.example.RuleEnginAST.entity.Node;

public class RuleEvaluationRequestDTO {
    private Node node; // The AST root node
    private Map<String, Object> data; // The data for evaluation

    // Getters and setters
    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
