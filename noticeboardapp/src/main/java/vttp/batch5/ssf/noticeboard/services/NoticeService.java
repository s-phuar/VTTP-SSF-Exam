package vttp.batch5.ssf.noticeboard.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.json.JsonObject;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.repositories.NoticeRepository;

@Service
public class NoticeService {

	@Autowired
	private NoticeRepository noticeRepo;


	@Value("${noticeboard.db.publishinghost}")
    private String url;


	// TODO: Task 3
	// You can change the signature of this method by adding any number of parameters
	// and return any type
	public String postToNoticeServer(Notice notice) {
		JsonObject jObj = Notice.objToJson(notice);

		//constructing request to send to server
		RequestEntity<String> req = RequestEntity
			.post(url+"notice")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body(jObj.toString(), String.class);

		System.out.println(req.getBody().toString());

		try{
			RestTemplate template = new RestTemplate();
			ResponseEntity<String> resp = template.exchange(req,String.class);
			String payload = resp.getBody();
			System.out.println(payload); //debugging
			return payload; //succesful payload
		}catch(HttpClientErrorException e){
			System.out.println("Error requesting from server");
			System.out.println(e.getResponseBodyAsString()); //debugging

			return e.getResponseBodyAsString(); //unsuccesful payload
		}
	}


	public String saveToRedis(JsonObject jObj){
		noticeRepo.insertNotices(jObj);
		return jObj.getString("id");
	}


	public String getRandomKey(){
		return noticeRepo.getRandomKey();
	}


}
