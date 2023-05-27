package com.sam.myVault.repositories;

import com.sam.myVault.entities.MyVault;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface MyVaultRepository extends JpaRepository<MyVault, Long>{
    @Transactional
    @Modifying
    @Query("update MyVault u set u.name=?2, u.description=?2 where u.id=?1")
    int updateAddress(long id, String name, String description);
}

