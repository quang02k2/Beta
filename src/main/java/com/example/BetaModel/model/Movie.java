package com.example.BetaModel.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private int movieDuration;

    private Timestamp endTime;

    private Timestamp premiereDate;

    private String description;

    private String director;

    private String image;

    private String heroImage;

    private String language;

    private String name;

    private String trailer;

    private boolean isActive;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Schedule> schedule;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "movieTypeId", foreignKey = @ForeignKey(name = "fk_movie_movieType"), nullable = false)
    @JsonManagedReference
    private MovieType movieType;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "rateId", foreignKey = @ForeignKey(name = "fk_movie_rate"), nullable = false)
    @JsonManagedReference
    private Rate rate;


}
