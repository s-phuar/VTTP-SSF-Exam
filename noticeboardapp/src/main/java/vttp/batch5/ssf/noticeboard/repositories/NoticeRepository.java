package vttp.batch5.ssf.noticeboard.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonObject;

@Repository
public class NoticeRepository {

	@Autowired @Qualifier("notice")
	private RedisTemplate <String, Object> template;

	// TODO: Task 4
	// You can change the signature of this method by adding any number of parameters
	// and return any type
	// 
	/*
	 * Write the redis-cli command that you use in this method in the comment. 
	 * For example if this method deletes a field from a hash, then write the following
	 * redis-cli command 
	 * 	hdel myhashmap a_key
	 *
	 *
	 */
	//HSET {"notice"} {id} {json String}
	public void insertNotices(JsonObject jObj) {
		HashOperations<String, String, Object> hashOps = template.opsForHash();
        hashOps.put("notice", jObj.getString("id"), jObj.toString());
	}

	public String getRandomKey(){
		return template.randomKey();
	}


}
