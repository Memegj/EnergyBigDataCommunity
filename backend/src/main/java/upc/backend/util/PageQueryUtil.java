package upc.backend.util;
import java.util.LinkedHashMap;
import java.util.Map;


public class PageQueryUtil extends LinkedHashMap<String, Object> {
    //当前页码
    private int page;
    //每页条数
    private int limit;
    //private int userid;
    //private String searchQuery;

    public PageQueryUtil(Map<String, Object> params) {
        this.putAll(params);
        //分页参数
        this.page = Integer.parseInt(params.get("page").toString());
        this.limit = Integer.parseInt(params.get("limit").toString());
        /*if (params.get("userid") != null) {
            this.userid = Integer.parseInt(params.get("userid").toString());
        }
        if (params.get("searchQuery") != null) {
            this.searchQuery = params.get("searchQuery").toString();
        }
         */
        this.put("start", (page - 1) * limit);
        this.put("page", page);
        this.put("limit", limit);
        //this.put("userid",userid);
        //this.put("searchQuery",searchQuery);
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "PageUtil{" +
                "page=" + page +
                ", limit=" + limit +
                '}';
    }
}
