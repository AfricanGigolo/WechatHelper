package moe.chionlab.wechatmomentstat.Model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenjunfan on 2017/2/16.
 */

public class Fm1Itembean implements Parcelable {
    private String title;
    private boolean ischeck;

    public boolean getIscheck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    public Fm1Itembean(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeByte(this.ischeck ? (byte) 1 : (byte) 0);
    }

    protected Fm1Itembean(Parcel in) {
        this.title = in.readString();
        this.ischeck = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Fm1Itembean> CREATOR = new Parcelable.Creator<Fm1Itembean>() {
        @Override
        public Fm1Itembean createFromParcel(Parcel source) {
            return new Fm1Itembean(source);
        }

        @Override
        public Fm1Itembean[] newArray(int size) {
            return new Fm1Itembean[size];
        }
    };
}
