package cocodas.prier.project.feedback.question;

import cocodas.prier.project.feedback.response.Response;
import cocodas.prier.project.project.Project;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Question {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long questionId;
    private String content;
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Response> responses = new ArrayList<>();
}
