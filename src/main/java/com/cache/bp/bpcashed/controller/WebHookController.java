package com.cache.bp.bpcashed.controller;

import com.cache.bp.bpcashed.model.WebHook;
import com.cache.bp.bpcashed.repository.WebhookRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/webhooks")
public class WebHookController {
  //  private final static Logger logger = LoggerFactory.getLogger(WebHookController.class);

    private WebhookRepository webHookRepository;

    @Autowired
    public WebHookController(WebhookRepository webHookRepository) {
        super();
        this.webHookRepository = webHookRepository;
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces=MediaType.TEXT_MARKDOWN_VALUE)
    public ResponseEntity<String> addWebHook(@RequestBody WebHook webHook){
       // logger.info("New webhook for " + webHook.getCompanyName() + " is registered");
        List<WebHook> webhooks = webHookRepository.findByCompanyNameAndType(
                webHook.getCompanyName(),
                webHook.getType());
        if(webhooks != null && webhooks.contains(webHook)){
            return new ResponseEntity<>("Webhook already exists", HttpStatus.OK);
        }
        webHookRepository.save(webHook);
        return new ResponseEntity<>("Successfully", HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WebHook>> getAllWebHooks(){
        List<WebHook> webhooks = new ArrayList<>();
        webHookRepository.findAll().iterator().forEachRemaining(webhooks::add);
        return new ResponseEntity<List<WebHook>>(webhooks, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET,
            value ="/comapnies/{companyName}/types/{type}",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebHook> getWebHooksByCompanyNameAndType(
            @PathVariable String companyName,
            @PathVariable String type){
        List<WebHook> webhooks = webHookRepository.findByCompanyNameAndType(
                companyName, type);
        return new ResponseEntity<WebHook>(webhooks.get(0), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE,
            value ="/comapnies/{companyName}/types/{type}",
            produces=MediaType.TEXT_MARKDOWN_VALUE)
    public ResponseEntity<String> removeWebHook(
            @PathVariable String companyName,
            @PathVariable String type){
        List<WebHook> webhooks = webHookRepository.findByCompanyNameAndType(
                companyName, type);
        if(!webhooks.isEmpty()){
            webHookRepository.delete(webhooks.get(0));
            return new ResponseEntity<>("WebHook was successfully deleted.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Webhook doesn't exist.", HttpStatus.OK);
    }

//    @RequestMapping(method = RequestMethod.DELETE,value ="/ids/{id}",
//            produces=MediaType.TEXT_MARKDOWN_VALUE)
//    public ResponseEntity<String> removeWebHookById(@PathVariable Long id){
//        Optional<WebHook> webhook= webHookRepository.findById(id);
//        if(webhook != null){
//            webHookRepository.delete(webhook);
//            return new ResponseEntity<>("WebHook was successfully deleted.", HttpStatus.OK);
//        }
//        return new ResponseEntity<>("Webhook doesn't exist.", HttpStatus.OK);
//    }
}