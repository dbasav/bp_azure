package com.cache.bp.bpcashed.repository;

import com.cache.bp.bpcashed.model.Application;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface  ApplicationRepository extends CrudRepository<Application, Long> {

    @Modifying
    @Transactional
    @Query("update Application d set d.online = true where d.id = :id")
    int setDestinationOnline(@Param("id") Long id) ;

    @Modifying
    @Transactional
    @Query("update Application d set d.online = false where d.id = :id")
    int setDestinationOffline(@Param("id") Long id) ;

}
