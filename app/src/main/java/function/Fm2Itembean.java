package function;

/**
 * Created by chenjunfan on 2017/2/18.
 */

public class Fm2Itembean {
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
}
