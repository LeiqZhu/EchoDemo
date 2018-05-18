package pj.dataserver;


import pj.dataserver.main.OperationServer;
import pj.dataserver.server.HttpServer;
import pj.dataserver.server.UrlMap;

public class StartServer {

    public static void main(String[] args) throws Exception {
        HttpServer httpServer = new HttpServer();

        //初始化访问路径
        UrlMap.urlMap.put("/stat/update", OperationServer.UpdateStatHandler.class);
        UrlMap.urlMap.put("/stat/get", OperationServer.GetStatHandler.class);
        UrlMap.urlMap.put("/stat/all", OperationServer.AllStatHandler.class);

        httpServer.start();
    }
}
