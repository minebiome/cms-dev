package com.wangyang.pojo.entity;


import com.wangyang.common.pojo.BaseEntity;
import lombok.Data;
import javax.persistence.*;

@Data
@Entity(name = "t_vocabulary")
public class Vocabulary extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
    @Column(name = "word")
    private String word;
    @Column(name = "explains")
    private String explains;
    @Column(name = "uk_speech")
    private String ukSpeech;
    @Column(name = "uk_phonetic")
    private String ukPhonetic;
    @Column(name = "us_speech")
    private String usSpeech;
    @Column(name = "us_phonetic")
    private String usPhonetic;
}
