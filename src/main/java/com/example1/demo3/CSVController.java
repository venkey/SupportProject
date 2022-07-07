package com.example1.demo3;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CSVController {

	@GetMapping("/hello")
	public String helloWorld() {
		return "Hello World Welcome";
	}

	@GetMapping("/jsonObj")
	public String testJson(@RequestBody String jsonObj) throws JSONException, IOException {

		String status = jsonToCvConversion(jsonObj);

		return status;
	}

	@GetMapping("/jsonTocv")
	public String jsonTocv(@RequestParam String jsonUrl) throws JSONException, IOException, CustomException {
		final String uri = jsonUrl;
		RestTemplate restTemplate = new RestTemplate();
		String jsonObj = restTemplate.getForObject(uri, String.class);
		String status = jsonToCvConversion(jsonObj.toString());
		return status;
	}

	private String jsonToCvConversion(String jsonObj) throws IOException, JSONException {
		JSONObject output;
		String status = null;
		char firstChar = jsonObj.trim().charAt(0);
		try {
			if (String.valueOf(firstChar).equalsIgnoreCase("{")) {
				String jsonarrayValue = jsonObj.substring(1, jsonObj.indexOf(":")).substring(0).replace("{", "").trim();
				String arrayName = jsonObj.replaceFirst(jsonarrayValue, "data");
				output = new JSONObject(arrayName);
				JSONArray docs = output.getJSONArray("data");
				File file = new File("D:/jsonObj/jsonTocv.csv");
				String csv = CDL.toString(docs);
				FileUtils.writeStringToFile(file, csv);
				status = "success";
				return status;
			} else {
				status = " its not a json array";
				return status;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
}
