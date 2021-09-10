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
@Table(name = "sample")
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
        ZonedDateTime createdAt = entity.createdAtUtc.atZone(ZoneId.of(entity.createdAtZone));
        ZonedDateTime updatedAt = entity.updatedAtUtc.atZone(ZoneId.of(entity.updatedAtZone));
        return new SampleData(createdAt, updatedAt, attributes);
    }

    public static SampleEntity fromSampleData(SampleData sampleData) {
        SampleEntity sampleEntity = new SampleEntity();
        sampleEntity.createdAtUtc = sampleData.getCreatedAt().withZoneSameLocal(ZONE_ID_UTC).toLocalDateTime();
        sampleEntity.createdAtZone = sampleData.getCreatedAt().getZone().getId();
        sampleEntity.updatedAtUtc = sampleData.getUpdatedAt().withZoneSameLocal(ZONE_ID_UTC).toLocalDateTime();
        sampleEntity.updatedAtZone = sampleData.getUpdatedAt().getZone().getId();

        sampleData.getAttributes().forEach((attribute, value) -> {
            SampleAttributeEntity attributeEntity = new SampleAttributeEntity();
            attributeEntity.setAttribute(attribute);
            attributeEntity.setValue(value);
            attributeEntity.setSample(sampleEntity);
            sampleEntity.attributes.add(attributeEntity);
        });
        return sampleEntity;
    }
}
