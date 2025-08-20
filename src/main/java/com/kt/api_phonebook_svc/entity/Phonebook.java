package com.kt.api_phonebook_svc.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "phonebook")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Phonebook {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "owner_email", nullable = false)
    @Email(message = "올바른 이메일 형식이 아닙니다")
    @NotBlank(message = "전화번호부 주인 이메일은 필수입니다")
    private String ownerEmail;
    
    @Column(name = "contact_name", nullable = false)
    @NotBlank(message = "연락처 이름은 필수입니다")
    private String contactName;
    
    @Column(name = "phone_number", nullable = false)
    @Pattern(regexp = "^01[016789]-?\\d{3,4}-?\\d{4}$", message = "올바른 휴대폰 번호 형식이 아닙니다")
    @NotBlank(message = "전화번호는 필수입니다")
    private String phoneNumber;
    
    @Column(name = "carrier", nullable = false)
    @Enumerated(EnumType.STRING)
    private Carrier carrier;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum Carrier {
        SKT("SKT"),
        KT("KT"), 
        LG("LG"),
        MVNO("알뜰폰");
        
        private final String description;
        
        Carrier(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}
