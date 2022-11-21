package com.d108.sduty.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d108.sduty.dto.Admin;

public interface AdminRepo extends JpaRepository<Admin, Integer>{
	public Optional<Admin> findByid(String adminId);
}
