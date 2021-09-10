package com.epam.speciome.catalog.persistence.spring.samples;

import org.hibernate.annotations.Proxy;

import javax.persistence.*;

@Entity(name = "SampleAttribute")
@Table(name = "sample_attribute")
@Proxy(lazy = false)
public class SampleAttributeEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String attribute;
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
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
