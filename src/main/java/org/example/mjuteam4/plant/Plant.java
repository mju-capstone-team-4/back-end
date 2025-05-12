package org.example.mjuteam4.plant;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Plant {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private Integer waterCycle;

    private Integer repottingCycle;

    private Integer fertilizingCycle;

    private String plantPilbkNo; // 도감번호 (PK)

    @Column(unique = true, nullable = false)
    private String plantGnrlNm;       // 국명

    private String plantSpecsScnm;    // 학명

    private String familyKorNm;       // 과명(한글)
    private String genusKorNm;        // 속명(한글)

    private String leafDesc;          // 잎 설명
    private String flwrDesc;          // 꽃 설명
    private String fritDesc;          // 열매 설명

    @Lob
    private String shpe;              // 형태
    private String sz;                // 크기

    private String grwEvrntDesc;      // 생육환경
    private String dstrb;             // 분포정보

    @Lob
    private String useMthdDesc;       // 사용법
    private String imgUrl;            // 이미지

    @Builder
    public Plant(String name, String description, Integer waterCycle, Integer repottingCycle, Integer fertilizingCycle, String plantPilbkNo, String plantGnrlNm, String plantSpecsScnm, String familyKorNm, String genusKorNm, String leafDesc, String flwrDesc, String fritDesc, String shpe, String sz, String grwEvrntDesc, String dstrb, String useMthdDesc, String imgUrl) {
        this.description = description;
        this.waterCycle = waterCycle;
        this.repottingCycle = repottingCycle;
        this.fertilizingCycle = fertilizingCycle;
        this.plantPilbkNo = plantPilbkNo;
        this.plantGnrlNm = plantGnrlNm;
        this.plantSpecsScnm = plantSpecsScnm;
        this.familyKorNm = familyKorNm;
        this.genusKorNm = genusKorNm;
        this.leafDesc = leafDesc;
        this.flwrDesc = flwrDesc;
        this.fritDesc = fritDesc;
        this.shpe = shpe;
        this.sz = sz;
        this.grwEvrntDesc = grwEvrntDesc;
        this.dstrb = dstrb;
        this.useMthdDesc = useMthdDesc;
        this.imgUrl = imgUrl;
    }
}
