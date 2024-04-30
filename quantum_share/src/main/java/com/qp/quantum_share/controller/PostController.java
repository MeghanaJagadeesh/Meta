package com.qp.quantum_share.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.qp.quantum_share.configuration.ConfigurationClass;
import com.qp.quantum_share.dao.FacebookUserDao;
import com.qp.quantum_share.dto.MediaPost;
import com.qp.quantum_share.dto.QuantumShareUser;
import com.qp.quantum_share.exception.CommonException;
import com.qp.quantum_share.response.ResponseStructure;
import com.qp.quantum_share.response.ResponseWrapper;
import com.qp.quantum_share.services.PostService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/quantum-share")
public class PostController {

	@Autowired
	ResponseStructure<String> structure;

	@Autowired
	PostService postServices;

	@Autowired
	FacebookUserDao facebookUserDao;

	@Autowired
	ConfigurationClass configuration;

	@PostMapping("/post/file/facebook")
	public ResponseEntity<ResponseWrapper> postToFacebook(MultipartFile mediaFile, @ModelAttribute MediaPost mediaPost,
			HttpSession session) {
		QuantumShareUser user = (QuantumShareUser) session.getAttribute("qsuser");
		if (user == null) {
			structure.setCode(HttpStatus.NOT_FOUND.value());
			structure.setMessage("Session has expired, Please login");
			structure.setStatus("error");
			structure.setData(null);
			return new ResponseEntity<ResponseWrapper>(configuration.getResponseWrapper(structure),
					HttpStatus.NOT_FOUND);
		}

		try {
			System.out.println(mediaPost.getMediaPlatform());
			if (mediaPost.getMediaPlatform() == null ) {
				structure.setCode(HttpStatus.BAD_REQUEST.value());
				structure.setStatus("error");
				structure.setMessage("select social media platforms");
				return new ResponseEntity<ResponseWrapper>(configuration.getResponseWrapper(structure),
						HttpStatus.BAD_REQUEST);
			} else {
				return postServices.postOnFb(mediaPost, mediaFile, user.getSocialAccounts());
			}
		} catch (NullPointerException e) {
			throw new NullPointerException(e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new CommonException(e.getMessage());
		}
	}
	
	@PostMapping("/post/file/instagram")
	public ResponseEntity<ResponseWrapper> postToInsta(MultipartFile mediaFile, @ModelAttribute MediaPost mediaPost,
			HttpSession session) {
		QuantumShareUser user = (QuantumShareUser) session.getAttribute("qsuser");
		if (user == null) {
			structure.setCode(HttpStatus.NOT_FOUND.value());
			structure.setMessage("Session has expired, Please login");
			structure.setStatus("error");
			structure.setData(null);
			return new ResponseEntity<ResponseWrapper>(configuration.getResponseWrapper(structure),
					HttpStatus.NOT_FOUND);
		}
		System.out.println("conteoller");
		try {
			System.out.println(mediaPost.getMediaPlatform());
			if (mediaPost.getMediaPlatform() == null ) {
				structure.setCode(HttpStatus.BAD_REQUEST.value());
				structure.setStatus("error");
				structure.setMessage("select social media platforms");
				return new ResponseEntity<ResponseWrapper>(configuration.getResponseWrapper(structure),
						HttpStatus.BAD_REQUEST);
			} else {
				return postServices.postOnInsta(mediaPost, mediaFile, user.getSocialAccounts());
			}
		} catch (NullPointerException e) {
			throw new NullPointerException(e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new CommonException(e.getMessage());
		}
	}

}