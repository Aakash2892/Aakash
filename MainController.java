package com.example.uploadAndDownload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MainController {
	
	@RequestMapping(value="/upload" , method = RequestMethod.POST,consumes=org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) throws IOException
	{
		
		File convertfile = new File("C:\\Users\\Dell\\Desktop\\test\\Testing\\"+file.getOriginalFilename());
		convertfile.createNewFile();
		FileOutputStream fout = new FileOutputStream(convertfile);
		fout.write(file.getBytes());
		fout.close();
		return new ResponseEntity<>("FileUploaded Successfully",HttpStatus.OK);
	}
	
	@RequestMapping(value="/download",method = RequestMethod.POST)
	public ResponseEntity<Object> downloadFile()
	{
		FileWriter filewriter =  null;
		try {
		CSVData csv1 = new CSVData();
		csv1.setId("1");
		csv1.setName("Aa");
		csv1.setNumber("5601");
		
		CSVData csv2 = new CSVData();
		csv2.setId("2");
		csv2.setName("bvc");
		csv2.setNumber("8710");
		
		List<CSVData> csvDataList = new ArrayList<>();
		csvDataList.add(csv1);
		csvDataList.add(csv2);
		
		StringBuilder filecontent = new StringBuilder("ID, NAME, NUMBER\n");
		for(CSVData csv:csvDataList) {
			filecontent.append(csv.getId()).append(",").append(csv.getName()).append(",").append(csv.getNumber()).append("\n");
		}
		
		String filename = "C:\\\\Users\\\\Dell\\\\Desktop\\\\Test\\\\Testing\\csvdata.csv";
		
	 filewriter = new FileWriter(filename);
		filewriter.write(filecontent.toString());
		filewriter.flush();
		
		File file = new File(filename);
		
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		
		ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/txt")).body(resource);
		return responseEntity;
		} catch (Exception e ) {
			return new ResponseEntity<>("error occurred", HttpStatus.INTERNAL_SERVER_ERROR);	
		} finally {
			if(filewriter!=null)
				filewriter.close();
		}
	}
	}

}
