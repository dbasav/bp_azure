package com.cache.bp.bpcashed.repository;

import com.cache.bp.bpcashed.model.WebHook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WebhookRepository extends JpaRepository<WebHook,Long> {
    public List<WebHook> findByCompanyNameAndType(String companyName,String type);
    public List<WebHook> findByCompanyName(String companyName);
   // public WebHook findOne(Long id);
}
