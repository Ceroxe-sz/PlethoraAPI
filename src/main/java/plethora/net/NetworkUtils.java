package plethora.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class NetworkUtils {
    private static String ipinfoToken;

    private NetworkUtils() {
    }

    // 设置IPinfo令牌
    public static void setIpinfoToken(String token) {
        ipinfoToken = token;
    }

    // 获取IP的延迟，默认超时为200毫秒
    public static int getLatency(String ip) {
        return getLatency(ip, 200);
    }

    // 获取IP的延迟，可指定超时时间
    public static int getLatency(String ip, int timeoutMilliseconds) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String pingCommand;

            if (os.contains("win")) {
                // Windows系统
                pingCommand = "ping -n 1 -w " + timeoutMilliseconds + " " + ip;
            } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                // Linux或macOS系统
                pingCommand = "ping -c 1 -W " + timeoutMilliseconds / 1000 + " " + ip;
            } else {
                return -1;
            }

            Process process = Runtime.getRuntime().exec(pingCommand);
            boolean pingSuccessful = process.waitFor(timeoutMilliseconds, java.util.concurrent.TimeUnit.MILLISECONDS);

            if (pingSuccessful) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("ms")) {
                        if (line.contains("<1ms")) {
                            return 0;
                        }
                        String[] a = line.split("ms")[0].split("=");
                        return Integer.parseInt(a[a.length - 1]);
                    }
                }
                return -1; // 未找到合适的延迟信息
            } else {
                return -2; // 超时，未收到响应
            }
        } catch (IOException | InterruptedException e) {
            return -1; // 异常发生，无法访问
        }
    }


    // 获取IP的地理位置信息
    public static String getIpAddressLocation(String ip) {
        return getIpAddressLocation(ip, ipinfoToken);
    }

    // 获取Socket的IP地址的地理位置信息
    public static String getIpAddressLocation(Socket socket) {
        InetAddress address = socket.getInetAddress();
        return getIpAddressLocation(address.getHostAddress(), ipinfoToken);
    }

    public static String getIpAddressLocation(String ip, String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalStateException("IPinfo令牌未设置");
        }

        try {
            String apiUrl = "https://ipinfo.io/" + ip + "?token=" + token;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUrl))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new IOException("API请求失败，响应代码: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
