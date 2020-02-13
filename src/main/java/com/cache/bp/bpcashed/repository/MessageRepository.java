package com.cache.bp.bpcashed.repository;

import com.cache.bp.bpcashed.model.Application;
import com.cache.bp.bpcashed.model.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {
    List<Message> findAllByApplicationOrderByIdAsc(Application destination);
}
