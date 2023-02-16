package admin.blogMannager.web;
import admin.blogMannager.user.UserWithCertainInformation;
import admin.blogMannager.util.JSONUtils;
import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;


public class API {
    private static API instance;

    private static final String BASE_URL = "http://localhost:3000/api";
    private final CookieManager cookieManager;
    private final HttpClient client;

    List<HttpCookie> cookies;
    public static API getInstance() {
        if (instance == null) {
            instance = new API();
        }
        return instance;
    }

    private API() {
        this.cookieManager = new CookieManager();

        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NEVER)
                .connectTimeout(Duration.ofSeconds(10))
                .cookieHandler(this.cookieManager)
                .build();
    }

    public List<UserWithCertainInformation> getAllUsersList() throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/users"))
                .setHeader("Accept", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody());

        HttpRequest request = builder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String json = response.body();

        return JSONUtils.toList(json, UserWithCertainInformation.class);
    }

    public int login (String username, String password) throws IOException, InterruptedException {
        String jsonObj = "{\"username\":" + "\""+ username + "\""+ ", "+
                        "\"password\":"+ "\""+ password+ "\"" +"}";

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/login"))
                .setHeader("Content-Type", "application/json").
                method("POST", HttpRequest.BodyPublishers.ofString(jsonObj));

        HttpRequest request = builder.build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

        int statusCode =  response.statusCode();
        return statusCode;
    }

    public String deleteUser (int userId) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/users/"+userId))
                .setHeader("Accept", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.noBody());

        HttpRequest request = builder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String json = response.body();
        return json;
    }

    public int logout () throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/logout"))
                .setHeader("Accept", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody());

        HttpRequest request = builder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        return statusCode;
    }
}
