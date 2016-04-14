package com.megan.exquisitecorpse.capstone;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "GalleryPictures")
public class GalleryPicture extends Model {

    @Column(name = "FullDrawing")
    public byte[] full_drawing;

    public GalleryPicture(){
        super();
    }

    public GalleryPicture(byte[] full_drawing){
        super();
        this.full_drawing = full_drawing;
    }

}