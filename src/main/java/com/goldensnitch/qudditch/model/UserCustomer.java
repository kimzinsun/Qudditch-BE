package com.goldensnitch.qudditch.model;

import jakarta.persistence.*; // Jakarta Persistence 사용
import lombok.Data;

import java.sql.Date;

@Data
@Entity
@Table(name = "user_customer")
public class UserCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 순번
    private String email; // 이메일 주소
    private String password; // 비밀번호
    private String name; // 사용자 이름
    private Integer state; // 유저 상태 (0: 미인증, 1: 일반, 2: 탈퇴)
    private String verficationCode; // 이메일 인증 코드 필드 추가
    private String collectionArn; // AWS 컬렉션 ARN
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt; // 계정 생성 시간
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt; // 계정 수정 시간
}