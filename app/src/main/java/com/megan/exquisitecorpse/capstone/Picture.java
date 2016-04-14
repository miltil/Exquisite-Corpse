package com.megan.exquisitecorpse.capstone;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Pictures")
public class Picture extends Model {

    @Column(name = "Artist")
    public String artist;

    @Column(name = "Segment")
    public String segment;

    @Column(name = "Drawing")
    public byte[] drawing;

    public Picture(){
        super();
    }

    public Picture(String artist, String segment, byte[] drawing){
        super();
        this.artist = artist;
        this.segment = segment;
        this.drawing = drawing;
    }

}