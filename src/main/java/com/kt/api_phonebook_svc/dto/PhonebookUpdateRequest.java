package com.kt.api_phonebook_svc.dto;

import com.kt.api_phonebook_svc.entity.Phonebook;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PhonebookUpdateRequest {
    
    @NotBlank(message = "연락처 이름은 필수입니다")
    private String contactName;
    
    @Pattern(regexp = "^01[016789]-?\\d{3,4}-?\\d{4}$", message = "올바른 휴대폰 번호 형식이 아닙니다")
    @NotBlank(message = "전화번호는 필수입니다")
    private String phoneNumber;
    
    @NotNull(message = "통신사는 필수입니다")
    private Phonebook.Carrier carrier;
}
