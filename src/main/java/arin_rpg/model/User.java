package arin_rpg.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.service.annotation.GetExchange;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String userName;

    private String email;

    private String password;

    private LocalDate dateOfBirth;

    private final LocalDateTime createAt = LocalDateTime.now();


}
