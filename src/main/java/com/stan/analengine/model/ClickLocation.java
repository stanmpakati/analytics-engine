package com.stan.analengine.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class ClickLocation {
   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE)
   @Column(name = "id", nullable = false)
   private Long id;
   private Long x;
   private Long y;


//   @ManyToOne(cascade = CascadeType.ALL)
//   @JoinColumn(name = "link_clicks")
//   @JsonBackReference
//   private PageEvent browserEvent;
}
