package com.qp.quantum_share.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.qp.quantum_share.configuration.ConfigurationClass;
import com.qp.quantum_share.dao.FacebookUserDao;
import com.qp.quantum_share.dao.InstagramUserDao;
import com.qp.quantum_share.dao.QuantumShareUserDao;
import com.qp.quantum_share.dto.MediaPost;
import com.qp.quantum_share.dto.SocialAccounts;
import com.qp.quantum_share.response.ErrorResponse;
import com.qp.quantum_share.response.ResponseStructure;
import com.qp.quantum_share.response.ResponseWrapper;

@Service
public class PostService {

	@Autowired
	ResponseStructure<String> structure;

	@Autowired
	FacebookPostService facebookPostService;

	@Autowired
	InstagramService instagramService;

	@Autowired
	FacebookUserDao facebookUserDao;

	@Autowired
	QuantumShareUserDao userDao;

	@Autowired
	ErrorResponse errorResponse;

	@Autowired
	ConfigurationClass config;

	@Autowired
	InstagramUserDao instagramUserDao;

	public ResponseEntity<ResponseWrapper> postOnFb(MediaPost mediaPost, MultipartFile mediaFile,
			SocialAccounts socialAccounts) {

		if (mediaPost.getMediaPlatform().contains("facebook")) {
			if (socialAccounts == null || socialAccounts.getFacebookUser().getFbId() != null)
				return facebookPostService.postMediaToPage(mediaPost, mediaFile,
						facebookUserDao.findById(socialAccounts.getFacebookUser().getFbId()));
			else {
				structure.setMessage("Please connect your facebook account");
				structure.setCode(HttpStatus.NOT_FOUND.value());
				structure.setPlatform("facebook");
				structure.setStatus("error");
				structure.setData(null);
				return new ResponseEntity<ResponseWrapper>(config.getResponseWrapper(structure), HttpStatus.NOT_FOUND);
			}
		}
		return null;
	}

	public ResponseEntity<ResponseWrapper> postOnInsta(MediaPost mediaPost, MultipartFile mediaFile,
			SocialAccounts socialAccounts) {
		System.out.println("main service");
		if (mediaPost.getMediaPlatform().contains("instagram")) {
			if (socialAccounts == null || socialAccounts.getInstagramUser().getInstaId() != null)
				return instagramService.postMediaToPage(mediaPost, mediaFile,
						instagramUserDao.findById(socialAccounts.getInstagramUser().getInstaId()));
			else {
				structure.setMessage("Please connect your Instagram account");
				structure.setCode(HttpStatus.NOT_FOUND.value());
				structure.setPlatform("facebook");
				structure.setStatus("error");
				structure.setData(null);
				return new ResponseEntity<ResponseWrapper>(config.getResponseWrapper(structure), HttpStatus.NOT_FOUND);
			}
		}
		return null;
	}
}
