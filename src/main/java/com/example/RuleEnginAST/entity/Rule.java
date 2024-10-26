package com.example.RuleEnginAST.entity;

import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Rule {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String ruleString;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "node_id", referencedColumnName = "id")
    private Node ruleNode;
    
    public Rule(String ruleString, Node ruleNode) {
        this.ruleString = ruleString;
        this.ruleNode = ruleNode;
    }


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRuleString() {
		return ruleString;
	}

	public void setRuleString(String ruleString) {
		this.ruleString = ruleString;
	}

	public Node getRuleNode() {
		return ruleNode;
	}

	public void setRuleNode(Node ruleNode) {
		this.ruleNode = ruleNode;
	}

    
   
}
