package com.goldensnitch.qudditch.model;

import lombok.Data;

import java.sql.Date;

// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.Table;

@Data
// @Entity
// @Table(name = "user_store")
public class UserStore {
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 순번
    private Integer storeId; // 매장 식별자
    private String email; // 이메일 주소
    private String password; // 비밀번호
    private String name; // 사용자 이름
    private Integer bnNumber; // 사업자 등록 번호
    private Integer state; // 유저 상태 (0: 미인증, 1: 일반, 2: 탈퇴)
    private Date createdAt; // 계정 생성 시간
    private Date modifiedAt; // 계정 수정 시간
    
}
