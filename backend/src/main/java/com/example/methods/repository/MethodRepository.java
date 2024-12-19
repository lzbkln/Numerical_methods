package com.example.methods.repository;

import com.example.methods.model.Method;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MethodRepository extends JpaRepository<Method, Long> {
    @Query("SELECT m FROM Method m WHERE m.name = :name")
    Method findByName(@Param("name") String name);
}
