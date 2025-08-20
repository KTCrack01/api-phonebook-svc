package com.kt.api_phonebook_svc.service;

import com.kt.api_phonebook_svc.dto.PhonebookCreateRequest;
import com.kt.api_phonebook_svc.dto.PhonebookUpdateRequest;
import com.kt.api_phonebook_svc.entity.Phonebook;
import com.kt.api_phonebook_svc.repository.PhonebookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PhonebookService {
    
    private final PhonebookRepository phonebookRepository;
    
    /**
     * 전화번호부 생성
     */
    @Transactional
    public Phonebook createPhonebook(PhonebookCreateRequest request) {
        log.info("Creating phonebook entry for owner: {}, contact: {}", 
                request.getOwnerEmail(), request.getContactName());
        
        Phonebook phonebook = request.toEntity();
        return phonebookRepository.save(phonebook);
    }
    
    /**
     * 특정 사용자의 전체 전화번호부 조회
     */
    public List<Phonebook> getPhonebooksByOwner(String ownerEmail) {
        log.info("Fetching phonebook for owner: {}", ownerEmail);
        return phonebookRepository.findByOwnerEmailOrderByContactNameAsc(ownerEmail);
    }
    
    /**
     * 전화번호부 상세 조회
     */
    public Phonebook getPhonebookById(Long id) {
        log.info("Fetching phonebook entry with id: {}", id);
        return phonebookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("전화번호부 항목을 찾을 수 없습니다. ID: " + id));
    }
    
    /**
     * 전화번호부 수정
     */
    @Transactional
    public Phonebook updatePhonebook(Long id, PhonebookUpdateRequest request, String ownerEmail) {
        log.info("Updating phonebook entry with id: {} for owner: {}", id, ownerEmail);
        
        Phonebook phonebook = phonebookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("전화번호부 항목을 찾을 수 없습니다. ID: " + id));
        
        // 소유자 확인
        if (!phonebook.getOwnerEmail().equals(ownerEmail)) {
            throw new RuntimeException("해당 전화번호부 항목을 수정할 권한이 없습니다.");
        }
        
        phonebook.setContactName(request.getContactName());
        phonebook.setPhoneNumber(request.getPhoneNumber());
        phonebook.setCarrier(request.getCarrier());
        
        return phonebookRepository.save(phonebook);
    }
    
    /**
     * 전화번호부 삭제
     */
    @Transactional
    public void deletePhonebook(Long id, String ownerEmail) {
        log.info("Deleting phonebook entry with id: {} for owner: {}", id, ownerEmail);
        
        Phonebook phonebook = phonebookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("전화번호부 항목을 찾을 수 없습니다. ID: " + id));
        
        // 소유자 확인
        if (!phonebook.getOwnerEmail().equals(ownerEmail)) {
            throw new RuntimeException("해당 전화번호부 항목을 삭제할 권한이 없습니다.");
        }
        
        phonebookRepository.delete(phonebook);
    }
    
    /**
     * 연락처 이름으로 검색
     */
    public List<Phonebook> searchByContactName(String ownerEmail, String contactName) {
        log.info("Searching phonebook by contact name: {} for owner: {}", contactName, ownerEmail);
        return phonebookRepository.findByOwnerEmailAndContactNameContainingIgnoreCaseOrderByContactNameAsc(
                ownerEmail, contactName);
    }
    
    /**
     * 전화번호로 검색
     */
    public List<Phonebook> searchByPhoneNumber(String ownerEmail, String phoneNumber) {
        log.info("Searching phonebook by phone number: {} for owner: {}", phoneNumber, ownerEmail);
        return phonebookRepository.findByOwnerEmailAndPhoneNumberContaining(ownerEmail, phoneNumber);
    }
    
    /**
     * 통신사별 조회
     */
    public List<Phonebook> getPhonebooksByCarrier(String ownerEmail, Phonebook.Carrier carrier) {
        log.info("Fetching phonebook by carrier: {} for owner: {}", carrier, ownerEmail);
        return phonebookRepository.findByOwnerEmailAndCarrierOrderByContactNameAsc(ownerEmail, carrier);
    }
}
