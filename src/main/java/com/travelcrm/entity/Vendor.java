package com.travelcrm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vendors")
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "업체명은 필수 항목입니다.")
    @Size(max = 100, message = "업체명은 100자를 초과할 수 없습니다.")
    @Column(nullable = false)
    private String name;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "vendor_handled_items", joinColumns = @JoinColumn(name = "vendor_id"))
    @Column(name = "item")
    private Set<String> handledItems = new HashSet<>();

    @Size(max = 100, message = "담당자명은 100자를 초과할 수 없습니다.")
    private String contactPerson;

    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    @Size(max = 100, message = "담당자 이메일은 100자를 초과할 수 없습니다.")
    private String contactEmail;

    @Size(max = 20, message = "담당자 전화번호는 20자를 초과할 수 없습니다.")
    private String contactPhone;

    @Size(max = 255, message = "주소는 255자를 초과할 수 없습니다.")
    private String address;

    @Size(max = 255, message = "웹사이트는 255자를 초과할 수 없습니다.")
    private String website;

    @Column(columnDefinition = "TEXT")
    private String notes;
} 