package com.example.RuleEnginAST.service;

import com.example.RuleEnginAST.entity.Node;
import com.example.RuleEnginAST.entity.Rule;
import com.example.RuleEnginAST.repository.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RuleService {

    @Autowired
    private RuleRepository ruleRepository;
    
    // Method to create AST from rule string
    public Node createRule(String ruleString) {
        ruleString = ruleString.trim();
        if (ruleString.isEmpty()) {
            throw new IllegalArgumentException("Rule string cannot be empty.");
        }

        // Split the rule string by logical operators (OR)
        Node combinedOrNode = null;
        String[] orParts = ruleString.split("(?i)\\s+OR\\s+"); // Case insensitive OR

        for (String orPart : orParts) {
            Node combinedAndNode = null;
            String[] andParts = orPart.split("(?i)\\s+AND\\s+"); // Case insensitive AND

            for (String andPart : andParts) {
                Node conditionNode = createConditionNode(andPart.trim());
                combinedAndNode = combineNodes(combinedAndNode, conditionNode, "AND"); // Combine conditions with AND
            }

            combinedOrNode = combineNodes(combinedOrNode, combinedAndNode, "OR"); // Combine conditions with OR
        }

        return combinedOrNode; // Return the complete AST
    }

    // Helper method to combine nodes using a logical operator (AND/OR)
    private Node combineNodes(Node existingNode, Node newNode, String operator) {
        if (existingNode == null) {
            return newNode; // If no existing node, return the new node
        }
        return new Node(operator, existingNode, newNode); // Combine existing and new nodes
    }

    // Helper method to create a condition node from a condition string (like 'age > 18')
    private Node createConditionNode(String condition) {
        String[] parts = condition.split(" ");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid condition format: " + condition);
        }

        // Extract attribute, operator, and value from the condition string
        String attribute = parts[0]; // e.g., "age"
        String operator = parts[1];  // e.g., ">"
        String value = String.join(" ", java.util.Arrays.copyOfRange(parts, 2, parts.length)); // e.g., "30"

        // Return a node for the condition
        return new Node("operand", condition.trim()); // Return a node with the condition as its value
    }
	//Combine Rule method
    public Node combineRules(List<String> rules) {
        Node combinedAst = null;
        for (String rule : rules) {
            Node ast = createRule(rule); // Assume this method creates a Node from a rule string
            combinedAst = combineASTs(combinedAst, ast); // Combine the current AST with the new one
        }
        return combinedAst;
    }

    private Node combineASTs(Node existingAst, Node newAst) {
        if (existingAst == null) {
            return newAst; 
        }
        return new Node("OR", existingAst, newAst); // Combine using OR, you can change this based on your logic
    }
    
    
    
    //Evaluate Rule method  
        // Parses the ruleString into an AST (Node)
        public Node parseRuleString(String ruleString) {
            if (ruleString == null || ruleString.isEmpty()) {
                throw new IllegalArgumentException("Rule string cannot be null or empty.");
            }

            // Implement your logic to parse the rule string into an AST
            // Here is a simple hardcoded AST for demonstration purposes:
            Node root = new Node("operator", "AND");

            String[] conditions = ruleString.split("AND|OR");
            for (String condition : conditions) {
                Node conditionNode = new Node("operand", condition.trim());
                // For simplicity, let's just attach them directly as children
                // You would likely need a more sophisticated structure here
                if (root.getLeft() == null) {
                    root.setLeft(conditionNode);
                } else {
                    root.setRight(conditionNode);
                }
            }

            return root;
        }

        // Evaluates the rule using the provided user attributes
        public boolean evaluateRule(Rule rule, Map<String, Object> userAttributes) {
            Node ruleNode = rule.getRuleNode();
            return evaluateNode(ruleNode, userAttributes);
        }

        // Recursively evaluates each node in the AST
        private boolean evaluateNode(Node node, Map<String, Object> userAttributes) {
            if (node.getType().equals("operand")) {
                return evaluateCondition(node.getValue(), userAttributes);
            } else if (node.getType().equals("operator")) {
                boolean leftResult = evaluateNode(node.getLeft(), userAttributes);
                boolean rightResult = evaluateNode(node.getRight(), userAttributes);

                if (node.getValue().equals("AND")) {
                    return leftResult && rightResult;
                } else if (node.getValue().equals("OR")) {
                    return leftResult || rightResult;
                }
            }
            return false;
        }

        // Evaluates a single condition (operand) based on user attributes
        private boolean evaluateCondition(String condition, Map<String, Object> userAttributes) {
            if (condition.contains(">")) {
                String[] parts = condition.split(">");
                String attribute = parts[0].trim();
                int value = Integer.parseInt(parts[1].trim());
                return (int) userAttributes.get(attribute) > value;
            } else if (condition.contains("<")) {
                String[] parts = condition.split("<");
                String attribute = parts[0].trim();
                int value = Integer.parseInt(parts[1].trim());
                return (int) userAttributes.get(attribute) < value;
            } else if (condition.contains("==")) {
                String[] parts = condition.split("==");
                String attribute = parts[0].trim();
                int value = Integer.parseInt(parts[1].trim());
                return (int) userAttributes.get(attribute) == value;
            } else if (condition.contains("=")) { // String comparison for department
                String[] parts = condition.split("=");
                String attribute = parts[0].trim();
                String value = parts[1].trim().replace("'", ""); // Remove quotes
                return value.equals(userAttributes.get(attribute).toString());
            }
            return false;
        }

}
