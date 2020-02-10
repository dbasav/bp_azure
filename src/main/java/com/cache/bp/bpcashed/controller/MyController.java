package com.cache.bp.bpcashed.controller;

import com.cache.bp.bpcashed.exception.BpRunTimeException;
import com.cache.bp.bpcashed.model.BPCustomer;
import com.cache.bp.bpcashed.repository.BPCustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bp")
public class MyController {

    @Autowired
    private BPCustomerRepository bpCustomerRepository;

    @GetMapping("/get/bpcustomers")
    public List<BPCustomer> getAllCustomers(){
        return bpCustomerRepository.findAll();
    }

    @GetMapping("/get/bpcustomer/{id}")
    public ResponseEntity<BPCustomer> getcustomerById(@PathVariable(value = "id") Long customID)
            throws BpRunTimeException {
        BPCustomer bpCustomer = bpCustomerRepository.findById(customID)
                .orElseThrow(() -> new BpRunTimeException("Customer not found for this id :: " + customID));
        return ResponseEntity.ok().body(bpCustomer);
    }

    @PutMapping("/add/bpcustomer/{id}")
    public ResponseEntity<BPCustomer> updateCustomer(@PathVariable(value = "id") Long bpcustomerID,
                                                   @Valid @RequestBody BPCustomer customerDetails) throws BpRunTimeException {
        BPCustomer bpCustomer = bpCustomerRepository.findById(bpcustomerID)
                .orElseThrow(() -> new BpRunTimeException("Customer not found for this id :: " + bpcustomerID));

        bpCustomer.setCust_id(customerDetails.getCust_id());
        bpCustomer.setCust_name(customerDetails.getCust_name());
        bpCustomer.setCust_address1(customerDetails.getCust_address1());
        bpCustomer.setCust_address2(customerDetails.getCust_address2());
        bpCustomer.setCust_city(customerDetails.getCust_city());
        bpCustomer.setCust_zip(customerDetails.getCust_zip());
        bpCustomer.setCust_category(customerDetails.getCust_category());

        final BPCustomer updatedCustomer = bpCustomerRepository.save(bpCustomer);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/del/bpcustomer/{id}")
    public Map<String, Boolean> deleteCustomer(@PathVariable(value = "id") Long custid)
            throws BpRunTimeException {
        BPCustomer customer = bpCustomerRepository.findById(custid)
                .orElseThrow(() -> new BpRunTimeException("Customer not found for this id :: " + custid));

        bpCustomerRepository.delete(customer);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
    @PostMapping("/add/bpcustomer")
    public BPCustomer createCustomer(@Valid @RequestBody BPCustomer bpCustomer){
        return bpCustomerRepository.save(bpCustomer);
    }
}