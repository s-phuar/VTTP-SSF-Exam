package vttp.batch5.ssf.noticeboard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.JsonObject;
import jakarta.validation.Valid;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.services.NoticeService;

// Use this class to write your request handlers

@Controller
public class NoticeController {

    @Autowired
    private NoticeService noticeSvc;


    @GetMapping("/")
    public String landingPage( Model model){
        model.addAttribute("notice", new Notice());
        return "notice";
    }

    @GetMapping(path = "/status", produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> healthCheck(){

        try{
            noticeSvc.getRandomKey();
            return ResponseEntity.ok("{}");

        } catch(Exception e){
            return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("{}");
        }

    }

    @PostMapping("/notice")
    public String postNotice(@Valid @ModelAttribute("notice") Notice notice, BindingResult binding,  Model model)throws NullPointerException{
        if(binding.hasErrors()){
            model.addAttribute("notice", notice);
            return "notice";
        }

        //exchange payload
        String jsonStrResp = noticeSvc.postToNoticeServer(notice);
        JsonObject jObj =  Notice.toJson(jsonStrResp);

        if(jObj.getString("message", null)==null ){
            String id = noticeSvc.saveToRedis(jObj);
            model.addAttribute("id", id);
            return "posted";
            //return view to show succesful response is saved
        }else{
            model.addAttribute("message", jObj.getString("message"));
            return "error";
        }
    }


    


}
