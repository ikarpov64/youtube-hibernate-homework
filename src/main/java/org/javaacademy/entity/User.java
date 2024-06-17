package org.javaacademy.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "app_user")
@ToString(exclude = "videos")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String nickname;
    @OneToMany(mappedBy = "user")
    private List<Video> videos;

    public User(String nickname) {
        this.nickname = nickname;
    }
}
