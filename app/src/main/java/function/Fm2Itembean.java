package function;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenjunfan on 2017/2/18.
 */

public class Fm2Itembean implements Parcelable {
    private String title;
    private int right;
    private boolean ischeck;

    public Fm2Itembean(String title, int right) {
        this.title = title;
        this.right = right;

    }

    public boolean getIscheck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
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
        dest.writeInt(this.right);
        dest.writeByte(this.ischeck ? (byte) 1 : (byte) 0);
    }

    protected Fm2Itembean(Parcel in) {
        this.title = in.readString();
        this.right = in.readInt();
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
