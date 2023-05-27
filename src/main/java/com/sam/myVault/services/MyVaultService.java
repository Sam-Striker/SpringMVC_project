package com.sam.myVault.services;

import com.sam.myVault.entities.MyVault;
import com.sam.myVault.repositories.MyVaultRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class MyVaultService {
	private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private MyVaultRepository myVaultRepository;
	
	public void saveImage(MyVault myVault) {
		myVaultRepository.save(myVault);
	}

	public List<MyVault> getAllActiveImages() {
		return myVaultRepository.findAll();
	}
	@Cacheable(cacheNames = "myvault", key="#id")
	public Optional<MyVault> getImageById(Long id) {
		log.info("fetching myvault from db");
		return myVaultRepository.findById(id);
	}

	public MyVault getById(Long id)
	{

		Optional<MyVault> optional = myVaultRepository.findById(id);
		MyVault myVault = null;
		if (optional.isPresent())
			myVault = optional.get();
		else
			throw new RuntimeException(
					"MyVault not found for id : " + id);
		return myVault;
	}
	@CacheEvict(cacheNames = "myVault", key = "#id")
	public void deleteViaId(long id)
	{
		myVaultRepository.deleteById(id);
	}

	@CachePut(cacheNames = "myVaults", key="#todo.id")
	public MyVault updateMyVault(MyVault myVault) {
		myVaultRepository.updateAddress(myVault.getId(), myVault.getName(), myVault.getDescription());
		log.info("MyVault updated with new name");
		return myVault;
	}

}

