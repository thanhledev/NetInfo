package de.uniba.mobi.netinfo.opencellid;

public class OpenCellIdHelper {

    // public properties
    public static String mobileCountryCode;
    public static String mobileNetworkCode;
    public static int locationAreaCode;
    public static int cellIdentity;
    public static String apiToken = "ad64f825b45bb0";
    public static String apiServer = "https://eu1.unwiredlabs.com/v2/process.php";

    // instance property
    private static OpenCellIdHelper instance;

    // private properties

    // constructors
    private OpenCellIdHelper() {

    }

    public static OpenCellIdHelper getInstance() {
        if(instance == null) {
            synchronized (OpenCellIdHelper.class) {
                if(instance == null) {
                    instance = new OpenCellIdHelper();
                }
            }
        }

        return instance;
    }

    public String getJsonBody() {
        return null;
    }
}
