package moe.chionlab.wechatmomentstat.Model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenjunfan on 2017/2/18.
 */

public class Fm2Itembean implements Parcelable {
    private String title;
    private int right;
    private String property;
    private boolean ischeck;

    public Fm2Itembean(String title, int right,String property) {
        this.title = title;
        this.right = right;
        this.property = property;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public boolean getIscheck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeInt(this.right);
        dest.writeString(this.property);
        dest.writeByte(this.ischeck ? (byte) 1 : (byte) 0);
    }

    protected Fm2Itembean(Parcel in) {
        this.title = in.readString();
        this.right = in.readInt();
        this.property = in.readString();
        this.ischeck = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Fm2Itembean> CREATOR = new Parcelable.Creator<Fm2Itembean>() {
        @Override
        public Fm2Itembean createFromParcel(Parcel source) {
            return new Fm2Itembean(source);
        }

        @Override
        public Fm2Itembean[] newArray(int size) {
            return new Fm2Itembean[size];
        }
    };
}
