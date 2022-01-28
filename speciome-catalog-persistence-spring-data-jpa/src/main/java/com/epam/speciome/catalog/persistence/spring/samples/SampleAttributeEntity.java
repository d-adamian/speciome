package com.epam.speciome.catalog.persistence.spring.samples;

import org.hibernate.annotations.Proxy;

import javax.persistence.*;

@Entity(name = "SampleAttribute")
@Table(name = "sample_attributes", schema = "catalog")
@Proxy(lazy = false)
public class SampleAttributeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sample_attribute_id")
    private Long id;
    private String attribute;
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sample_id")
    private SampleEntity sample;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SampleEntity getSample() {
        return sample;
    }

    public void setSample(SampleEntity sample) {
        this.sample = sample;
    }
}
