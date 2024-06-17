package org.javaacademy.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "commentary")
public class Commentary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @OneToOne
    @JoinColumn(name = "video_id")
    private Video video;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Commentary(String name, Video video, User user) {
        this.name = name;
        this.video = video;
        this.user = user;
    }
}
