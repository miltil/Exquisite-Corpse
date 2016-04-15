package com.megan.exquisitecorpse.capstone;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "NameAssociation")
public class NameAssociation extends Model {

    @Column(name = "ArtistName")
    public String artistName;

    @Column(name = "DrawingId")
    public long drawingId;

    public NameAssociation(){
        super();
    }

    public NameAssociation(String artistName, long drawingId){
        super();
        this.artistName = artistName;
        this.drawingId = drawingId;
    }
}
