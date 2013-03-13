package au.com.iglooit.winerymap.android.exception;

public class AppX extends RuntimeException {
    public AppX() {

    }

    public AppX(String errorMessage) {
        super(errorMessage);
    }
}
