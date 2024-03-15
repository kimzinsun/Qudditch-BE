// File: src/main/java/com/goldensnitch/qudditch/repository/UserCustomerRepository.java

package com.goldensnitch.qudditch.repository;

// import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goldensnitch.qudditch.model.UserCustomer;

// @Repository
// public interface UserCustomerRepository extends JpaRepository<UserCustomer, Integer> {
//     UserCustomer findByEmail(String email);
// }
@Repository
public interface UserCustomerRepository {
    UserCustomer selectUserByEmail(String email);
    Integer insertUserCustomer(UserCustomer user);
}