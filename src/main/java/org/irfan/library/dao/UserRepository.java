package org.irfan.library.dao;

import org.irfan.library.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsername(String userName);
}
