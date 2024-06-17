package org.javaacademy.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "video")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Video(String name, String description, User user) {
        this.name = name;
        this.description = description;
        this.user = user;
    }
}
