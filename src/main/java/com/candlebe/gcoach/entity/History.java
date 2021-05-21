package com.candlebe.gcoach.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"member", "content"})
public class History extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hid;

    @ManyToOne(fetch = FetchType.LAZY)
    Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    Member member;
}
