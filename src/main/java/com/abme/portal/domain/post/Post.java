package com.abme.portal.domain.post;

import com.abme.portal.domain.label.Label;
import com.abme.portal.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name="post_table", schema = "public")
public class Post
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, updatable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    private String URL;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @ManyToMany
    private Set<Label> labels;

    public Post(long id, UUID uuid) {
        this.id = id;
        this.uuid = uuid;
    }

    public static Post from(AddPostDto addPostDto) {
        return new Post(
                null,
                addPostDto.getUuid(),
                null,
                addPostDto.getURL(),
                addPostDto.getDescription(),
                new HashSet<>()
        );
    }
}
