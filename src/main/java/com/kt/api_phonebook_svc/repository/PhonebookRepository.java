package com.kt.api_phonebook_svc.repository;

import com.kt.api_phonebook_svc.entity.Phonebook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhonebookRepository extends JpaRepository<Phonebook, Long> {
    
    /**
     * 특정 사용자의 전화번호부 조회
     */
    List<Phonebook> findByOwnerEmailOrderByContactNameAsc(String ownerEmail);
    
    /**
     * 특정 사용자의 전화번호부에서 연락처 이름으로 검색
     */
    List<Phonebook> findByOwnerEmailAndContactNameContainingIgnoreCaseOrderByContactNameAsc(
            String ownerEmail, String contactName);
    
    /**
     * 특정 사용자의 전화번호부에서 전화번호로 검색
     */
    List<Phonebook> findByOwnerEmailAndPhoneNumberContaining(String ownerEmail, String phoneNumber);
    
    /**
     * 특정 사용자의 전화번호부에서 통신사별 조회
     */
    List<Phonebook> findByOwnerEmailAndCarrierOrderByContactNameAsc(
            String ownerEmail, Phonebook.Carrier carrier);
}
