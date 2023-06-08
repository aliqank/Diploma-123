package com.autoparts.repository.order;

import com.autoparts.entity.User;
import com.autoparts.entity.order.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository  extends JpaRepository<Address, Long> {

    List<Address> findAllByUser(User user);
    Optional<Address> findTopByUserAndIsDefault(User user, boolean isDefault);
}
