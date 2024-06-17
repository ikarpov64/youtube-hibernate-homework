package org.javaacademy;

import java.util.List;
import java.util.Properties;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.javaacademy.entity.Commentary;
import org.javaacademy.entity.User;
import org.javaacademy.entity.Video;

public class Main {
    private static final String RICK_NAME = "rick";
    private static final String JOHN_NAME = "john";
    private static final String FIRST_VIDEO_NAME = "Мое первое интервью";
    private static final String SECOND_VIDEO_NAME = "Мое второе интервью";

    public static void main(String[] args) {

        @Cleanup SessionFactory sessionFactory = new Configuration().addProperties(getProperties())
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Video.class)
                .addAnnotatedClass(Commentary.class)
                .buildSessionFactory();

        @Cleanup Session session = sessionFactory.openSession();

        session.beginTransaction();
        User johnUser = new User(JOHN_NAME);
        User rickUser = new User(RICK_NAME);
        session.persist(johnUser);
        session.persist(rickUser);

        Video first_video = new Video(FIRST_VIDEO_NAME, "Описание первого интервью", johnUser);
        Video second_video = new Video(SECOND_VIDEO_NAME, "Описание второго интервью", johnUser);
        session.persist(first_video);
        session.persist(second_video);

        Commentary commentary = new Commentary("Классное интервью", first_video, rickUser);
        session.persist(commentary);
        session.getTransaction().commit();

        session.clear();

        User foundUser = getUser(session, JOHN_NAME);

        Video foundVideo = getVideo(foundUser, FIRST_VIDEO_NAME);

        List<Commentary> commentaries= getCommentaries(session, foundVideo);

        commentaries.forEach(comment -> System.out.printf("Комментарий от %s к видео '%s': %s\n",
                comment.getUser().getNickname(),
                comment.getVideo().getName(),
                comment.getName()));

    }

    private static List<Commentary> getCommentaries(Session session, Video foundVideo) {
        return session.createQuery("FROM Commentary c WHERE c.video = :video", Commentary.class)
                .setParameter("video", foundVideo)
                .getResultList();
    }

    private static Video getVideo(User foundUser, String name) {
        return foundUser.getVideos().stream()
                .filter(video -> video.getName().equals(name))
                .findFirst().orElseThrow();
    }

    private static User getUser(Session session, String name) {
        return session.createQuery("FROM User u WHERE u.nickname = :nickname", User.class)
                .setParameter("nickname", name)
                .getSingleResult();
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/youtube");
        properties.put("hibernate.connection.username", "postgres");
        properties.put("hibernate.connection.password", "postgres");
        properties.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        properties.put("hibernate.hbm2ddl.auto", "create");
        properties.put(Environment.SHOW_SQL, true);
        properties.put(Environment.FORMAT_SQL, true);
        return properties;
    }

}