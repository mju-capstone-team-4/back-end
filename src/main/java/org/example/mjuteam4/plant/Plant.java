package org.example.mjuteam4.plant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Plant {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String description;
}
