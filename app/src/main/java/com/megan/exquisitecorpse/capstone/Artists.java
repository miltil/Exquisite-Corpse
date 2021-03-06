package com.megan.exquisitecorpse.capstone;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Artists", id = BaseColumns._ID)
public class Artists extends Model {

    @Column(name = "ArtistName")
    public String artistName;

    public Artists(){
        super();
    }

    public Artists(String artistName){
        super();
        this.artistName = artistName;
    }
}
