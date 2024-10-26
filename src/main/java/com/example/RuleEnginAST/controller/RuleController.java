package com.example.RuleEnginAST.controller;

import com.example.RuleEnginAST.dto.RuleRequestDTO;
import com.example.RuleEnginAST.dto.RuleEvaluationRequestDTO;
import com.example.RuleEnginAST.entity.Node;
import com.example.RuleEnginAST.entity.Rule;
import com.example.RuleEnginAST.service.RuleService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.RuleNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rules")
public class RuleController {

    @Autowired
    private RuleService ruleService;

    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    // Endpoint to create an AST from the rule string
    @PostMapping("/create")
    public ResponseEntity<Node> createRule(@RequestBody Map<String, String> request) {
        // Extract the rule string from the request body
        String ruleString = request.get("ruleString");

        if (ruleString == null || ruleString.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null); // Return 400 if the rule string is invalid
        }

        try {
            // Call the createRule method from the service class to generate the AST
            Node ruleAST = ruleService.createRule(ruleString);

            // Return the created AST
            return ResponseEntity.ok(ruleAST);
        } catch (Exception e) {
            // Handle any errors that occur during rule creation
            return ResponseEntity.status(500).body(null);
        }
    }
    //Combine rule method

    @PostMapping("/combine")
    public ResponseEntity<Node> combineRules(@RequestBody CombineRequest request) {
        List<String> rules = request.getRules();
        if (rules == null || rules.isEmpty()) {
            return ResponseEntity.badRequest().body(null); // Return a bad request if no rules provided
        }
        
        // Combine the rules and create the AST
        Node combinedAST = ruleService.combineRules(rules);
        
        // Return the response with the combined AST
        return ResponseEntity.ok(combinedAST); // Return the AST directly
    }

    //Rul Ecaluate method
    
    @PostMapping("/evaluate")
    public boolean evaluateRule(@RequestBody Map<String, Object> request) {
        // Extract ruleString and user attributes from the request body
        String ruleString = (String) request.get("ruleString");
        Map<String, Object> userAttributes = (Map<String, Object>) request.get("userAttributes");

        // Ensure ruleString and userAttributes are not null
        if (ruleString == null || userAttributes == null) {
            throw new IllegalArgumentException("Rule string or user attributes cannot be null.");
        }

        try {
            // Parse ruleString into an AST (Node)
            Node ruleNode = ruleService.parseRuleString(ruleString);

            // Create the Rule object based on ruleString and the parsed AST (ruleNode)
            Rule rule = new Rule(ruleString, ruleNode);

            // Evaluate the rule with the provided user attributes
            return ruleService.evaluateRule(rule, userAttributes);
        } catch (Exception e) {
            // Log the exception and return an appropriate response
            e.printStackTrace(); // You may want to use a logger
            throw new RuntimeException("Error evaluating the rule: " + e.getMessage());
        }
    }


}
