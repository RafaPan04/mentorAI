package com.fiap.gs.demo.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    
    boolean existsByEmail(String email);
    
    boolean existsByCpf(String cpf);
}

