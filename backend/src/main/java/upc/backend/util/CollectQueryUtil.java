package upc.backend.util;
import java.util.LinkedHashMap;
import java.util.Map;


public class CollectQueryUtil extends LinkedHashMap<String, Object> {
    //当前页码
    private int userid;
    private int dataid;
    private int codeid;
    private int videoid;
    private int literid;


    public CollectQueryUtil(Map<String, Object> params) {
        this.putAll(params);
        //分页参数
        this.userid = Integer.parseInt(params.get("userid").toString());
        if (params.get("dataid") != null) {
            this.dataid = Integer.parseInt(params.get("dataid").toString());
        }
        if (params.get("codeid") != null) {
            this.codeid = Integer.parseInt(params.get("codeid").toString());
        }
        if (params.get("literid") != null) {
            this.literid = Integer.parseInt(params.get("literid").toString());
        }
        if (params.get("videoid") != null) {
            this.videoid = Integer.parseInt(params.get("videoid").toString());
        }
        this.put("userid",userid);
        this.put("dataid",dataid);
        this.put("literid",literid);
        this.put("videoid",videoid);
        this.put("codeid",codeid);
    }

}
