package com.jspp.devoka.term.damain;

import com.jspp.devoka.history.dto.RankData;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@Table(name = "popular_search_info")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PopularSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "rank_data", columnDefinition = "jsonb")
    @Type(value = JsonType.class)
    private List<RankData> rankDataList;

    @PrePersist
    private void onCreate() {
        this.createDate = LocalDateTime.now();
    }

    public static PopularSearch create(List<RankData> list) {
        return PopularSearch.builder()
                .rankDataList(list).build();
    }
}