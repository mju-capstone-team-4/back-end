package org.example.mjuteam4.myPlant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MyPlant {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String description;

}
