package com.epam.speciome.catalog.persistence.spring.samples;

import com.epam.speciome.catalog.persistence.api.samples.SampleData;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity(name = "sample")
@Table(name = "sample", schema = "catalog")
@Proxy(lazy = false)
public class SampleEntity {
    private static final ZoneId ZONE_ID_UTC = ZoneId.of("UTC");

    @SuppressWarnings("unused") // assigned by Hibernate
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String createdAtZone;
    private LocalDateTime createdAtUtc;
    private String updatedAtZone;
    private LocalDateTime updatedAtUtc;
    private boolean archived;

    @OneToMany(
            mappedBy = "sample",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final List<SampleAttributeEntity> attributes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static SampleData asSampleData(SampleEntity entity) {
        Map<String, String> attributes = entity.attributes.stream().collect(
                Collectors.toMap(SampleAttributeEntity::getAttribute, SampleAttributeEntity::getValue)
        );
        ZonedDateTime createdAt = entity.createdAtUtc.atZone(ZoneId.of(entity.createdAtZone)).withNano(0);
        ZonedDateTime updatedAt = entity.updatedAtUtc.atZone(ZoneId.of(entity.updatedAtZone)).withNano(0);
        boolean archived = entity.archived;
        return new SampleData(createdAt, updatedAt, attributes, archived);
    }

    public static SampleEntity fromSampleData(SampleData sampleData) {
        SampleEntity sampleEntity = new SampleEntity();
        sampleEntity.createdAtUtc = sampleData.createdAt().withZoneSameLocal(ZONE_ID_UTC).toLocalDateTime().withNano(0);
        sampleEntity.createdAtZone = sampleData.createdAt().getZone().getId();
        sampleEntity.updatedAtUtc = sampleData.updatedAt().withZoneSameLocal(ZONE_ID_UTC).toLocalDateTime().withNano(0);
        sampleEntity.updatedAtZone = sampleData.updatedAt().getZone().getId();
        sampleEntity.archived = sampleData.isArchived();

        sampleData.attributes().forEach((attribute, value) -> {
            SampleAttributeEntity attributeEntity = new SampleAttributeEntity();
            attributeEntity.setAttribute(attribute);
            attributeEntity.setValue(value);
            attributeEntity.setSample(sampleEntity);
            sampleEntity.attributes.add(attributeEntity);
        });
        return sampleEntity;
    }
}
