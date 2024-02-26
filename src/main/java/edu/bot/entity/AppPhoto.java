package edu.bot.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_photo")
@Entity
public class AppPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String telegramFieldId;
    private String photoName;
    @OneToOne
    private BinaryContent binaryContent;
    private Integer fileSize;
}
