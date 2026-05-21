package pl.malgorzata.galera;

public class LocalMockServer {
    public static void main(String[] args) throws Exception {

        ServerBuilder serverBuilder = new ServerBuilder();
        serverBuilder.registerCustomerEndpoint();
        serverBuilder.logInEndpoint();
        serverBuilder.openAccountAndGetBalanceEndpoints();
        serverBuilder.runServer();
    }
}
