package actuatorcustomendpoint;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@RestControllerEndpoint(id = "custom-rest-endpoint")
public class ActuatorCustomEndPoint {
	/*
	 * the below endpoint can be called using below curl command
	 * 
	 * curl -X GET \
  http://localhost:8080/actuator/custom-rest-endpoint
  
the o/p will be as below
{"server.date":"2019-02-22","server.time":"09:31:06.892"}
	 * 
	 * */
	@GetMapping
    public Map<String, String> get() {
        Map<String, String> map = new HashMap();
        map.put("server.date", LocalDate.now().toString());
        map.put("server.time", LocalDate.now().toString());
        return map;
    }

    @PostMapping
    public String post(@RequestBody  String request) {
        return "We have received your request: " + request;
    }

}
