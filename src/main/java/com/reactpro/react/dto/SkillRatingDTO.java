package com.reactpro.react.dto;

public class SkillRatingDTO {

    private String skillName;
    private int rating;


    public SkillRatingDTO(String skillName, int rating) {
        this.skillName = skillName;
        this.rating = rating;
    }

    public SkillRatingDTO() {
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
