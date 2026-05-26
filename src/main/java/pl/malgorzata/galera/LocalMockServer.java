package pl.malgorzata.galera;

public class LocalMockServer {
    public static void main(String[] args) throws Exception {

        ServerBuilder serverBuilder = new ServerBuilder();
        serverBuilder.registerCustomerEndpoint();
        serverBuilder.loginEndpoint();
        serverBuilder.openAccountAndGetBalanceEndpoints();
        serverBuilder.runServer();
    }
}
