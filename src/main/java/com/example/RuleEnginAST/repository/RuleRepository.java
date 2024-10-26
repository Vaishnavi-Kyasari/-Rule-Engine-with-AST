package com.example.RuleEnginAST.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.RuleEnginAST.entity.Rule;

public interface RuleRepository extends JpaRepository<Rule, Long>{

}
