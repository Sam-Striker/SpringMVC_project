package com.sam.myVault.controllers;

import com.sam.myVault.entities.MyVault;
import com.sam.myVault.repositories.MyVaultRepository;
import com.sam.myVault.services.MyVaultService;
import jakarta.servlet.ServletOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class MyVaultController {
	
	@Value("${uploadDir}")
	private String uploadFolder;

	@Autowired
	private MyVaultService todoService;
	@Autowired
	private MyVaultRepository todoRepository;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@GetMapping(value = {"/new"})
	public String addMyVaultPage() {
		return "New";
	}

	@PostMapping("/todo/saveTodoDetails")
	public @ResponseBody ResponseEntity<?> createProduct(@RequestParam("name") String name,
			 @RequestParam("description") String description, Model model, HttpServletRequest request
			,final @RequestParam("image") MultipartFile file) {
		try {
			//String uploadDirectory = System.getProperty("user.dir") + uploadFolder;
			String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
			log.info("uploadDirectory:: " + uploadDirectory);
			String fileName = file.getOriginalFilename();
			String filePath = Paths.get(uploadDirectory, fileName).toString();
			log.info("FileName: " + file.getOriginalFilename());
			if (fileName == null || fileName.contains("..")) {
				model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence \" + fileName");
				return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
			}
			String[] names = name.split(",");
			String[] descriptions = description.split(",");
			Date createDate = new Date();
			log.info("Name: " + names[0]+" "+filePath);
			log.info("description: " + descriptions[0]);
			//log.info("price: " + price);
			try {
				File dir = new File(uploadDirectory);
				if (!dir.exists()) {
					log.info("Folder Created");
					dir.mkdirs();
				}
				// Save the file locally
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
				stream.write(file.getBytes());
				stream.close();
			} catch (Exception e) {
				log.info("in catch");
				e.printStackTrace();
			}
			byte[] imageData = file.getBytes();
			MyVault todo = new MyVault();
			todo.setName(names[0]);
			todo.setImage(imageData);
			//todo.setPrice(price);
			todo.setDescription(descriptions[0]);
			todo.setCreateDate(createDate);
			todoService.saveImage(todo);
			log.info("HttpStatus===" + new ResponseEntity<>(HttpStatus.OK));
			return new ResponseEntity<>("Task Saved With File - " + fileName, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/image/display/{id}")
	@ResponseBody
	void showImage(@PathVariable("id") Long id, HttpServletResponse response, Optional<MyVault> todo)
			throws ServletException, IOException {
		log.info("Id :: " + id);
		todo = todoService.getImageById(id);
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		response.getOutputStream().write(todo.get().getImage());
		response.getOutputStream().close();
	}

	@GetMapping("/taskDetails")
	String showMyVaultDetail(@RequestParam("id") Long id, Optional<MyVault> todo, Model model) {

		try {
			//log.info("Id :: " + id);
			if (id != 0) {
				todo = todoService.getImageById(id);

				//log.info("todos :: " + todo);
				if (todo.isPresent()) {
					model.addAttribute("id", todo.get().getId());
					model.addAttribute("description", todo.get().getDescription());
					model.addAttribute("name", todo.get().getName());
					model.addAttribute("createdDate", todo.get().getCreateDate());
					return "Details";
				}
				return "redirect:/home";
			}
			return "redirect:/home";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/home";
		}

	}

	@GetMapping("/userhome")
	public String userPage(Model map) {
		List<MyVault> images = todoService.getAllActiveImages();
		map.addAttribute("images", images);
		return "UserHome";
	}

	@GetMapping("/edit")
	String editMyVault(@RequestParam("id") Long id, Model model) {
		MyVault myVault = todoRepository.findById(id).get();
		model.addAttribute("todo", myVault);
		return "Edit";
	}

	@GetMapping("/delete")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	String deleteMyVault(@RequestParam("id") Long id, Model model) {

		MyVault myVault = null;
		todoRepository.deleteById(id);
		model.addAttribute("todo", myVault);
		return "Delete";
	}
	@PutMapping("/myvault")
	public MyVault updateTodo(@RequestBody MyVault myVault) {
		return todoService.updateMyVault(myVault);
	}

	@GetMapping("/download_doc")
	public void downloadDocument(HttpServletResponse response) throws IOException {
		Long documentId = 1L;
		MyVault myVault = todoRepository.findById(documentId).get();
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + myVault.getName();
		response.setHeader(headerKey, headerValue);
		ServletOutputStream outputStream = response.getOutputStream();
		//outputStream.write(myVault.getName().getBytes());
        outputStream.write( myVault.getDescription().getBytes());
		//outputStream.write( myVault.getImage());

	}
}	

