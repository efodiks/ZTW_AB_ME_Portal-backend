package com.abme.portal.domain.post;

import com.abme.portal.domain.label.Label;
import com.abme.portal.domain.user.UserStubDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = {"uuid"})
@AllArgsConstructor
public class PostDto {

    private UUID uuid;

    private String URL;

    private String description;

    UserStubDto author;

    private Set<Label> labels;

    public static PostDto fromPost(Post post) {
        return new PostDto(
                post.getUuid(),
                post.getURL(),
                post.getDescription(),
                post.getAuthor() != null ? UserStubDto.fromUser(post.getAuthor()) : null,
                post.getLabels()
        );
    }
}