package com.kt.api_phonebook_svc.controller;

import com.kt.api_phonebook_svc.dto.PhonebookCreateRequest;
import com.kt.api_phonebook_svc.dto.PhonebookUpdateRequest;
import com.kt.api_phonebook_svc.entity.Phonebook;
import com.kt.api_phonebook_svc.service.PhonebookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phonebook")
@RequiredArgsConstructor
@Slf4j
public class PhonebookController {
    
    private final PhonebookService phonebookService;
    
    /**
     * 전화번호부 생성
     */
    @PostMapping
    public ResponseEntity<Phonebook> createPhonebook(@Valid @RequestBody PhonebookCreateRequest request) {
        try {
            Phonebook phonebook = phonebookService.createPhonebook(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(phonebook);
        } catch (Exception e) {
            log.error("Error creating phonebook entry", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 특정 사용자의 전체 전화번호부 조회
     */
    @GetMapping("/owner/{ownerEmail}")
    public ResponseEntity<List<Phonebook>> getPhonebooksByOwner(@PathVariable String ownerEmail) {
        try {
            List<Phonebook> phonebooks = phonebookService.getPhonebooksByOwner(ownerEmail);
            return ResponseEntity.ok(phonebooks);
        } catch (Exception e) {
            log.error("Error fetching phonebooks for owner: {}", ownerEmail, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 전화번호부 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<Phonebook> getPhonebookById(@PathVariable Long id) {
        try {
            Phonebook phonebook = phonebookService.getPhonebookById(id);
            return ResponseEntity.ok(phonebook);
        } catch (RuntimeException e) {
            log.warn("Phonebook entry not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Error fetching phonebook entry with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 전화번호부 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<Phonebook> updatePhonebook(
            @PathVariable Long id,
            @Valid @RequestBody PhonebookUpdateRequest request,
            @RequestParam String ownerEmail) {
        try {
            Phonebook phonebook = phonebookService.updatePhonebook(id, request, ownerEmail);
            return ResponseEntity.ok(phonebook);
        } catch (RuntimeException e) {
            log.warn("Error updating phonebook entry: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Error updating phonebook entry with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 전화번호부 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhonebook(@PathVariable Long id, @RequestParam String ownerEmail) {
        try {
            phonebookService.deletePhonebook(id, ownerEmail);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.warn("Error deleting phonebook entry: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Error deleting phonebook entry with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 연락처 이름으로 검색
     */
    @GetMapping("/search/name")
    public ResponseEntity<List<Phonebook>> searchByContactName(
            @RequestParam String ownerEmail,
            @RequestParam String contactName) {
        try {
            List<Phonebook> phonebooks = phonebookService.searchByContactName(ownerEmail, contactName);
            return ResponseEntity.ok(phonebooks);
        } catch (Exception e) {
            log.error("Error searching phonebook by contact name", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 전화번호로 검색
     */
    @GetMapping("/search/phone")
    public ResponseEntity<List<Phonebook>> searchByPhoneNumber(
            @RequestParam String ownerEmail,
            @RequestParam String phoneNumber) {
        try {
            List<Phonebook> phonebooks = phonebookService.searchByPhoneNumber(ownerEmail, phoneNumber);
            return ResponseEntity.ok(phonebooks);
        } catch (Exception e) {
            log.error("Error searching phonebook by phone number", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 통신사별 조회
     */
    @GetMapping("/carrier/{carrier}")
    public ResponseEntity<List<Phonebook>> getPhonebooksByCarrier(
            @PathVariable Phonebook.Carrier carrier,
            @RequestParam String ownerEmail) {
        try {
            List<Phonebook> phonebooks = phonebookService.getPhonebooksByCarrier(ownerEmail, carrier);
            return ResponseEntity.ok(phonebooks);
        } catch (Exception e) {
            log.error("Error fetching phonebooks by carrier", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
