package org.example.mjuteam4.plant;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Plant {

    @Id @GeneratedValue
    private Long id;
}
