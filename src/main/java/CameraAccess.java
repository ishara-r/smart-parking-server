import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.util.List;

import static java.lang.System.out;

class CameraAccess {

    public static void main(String[] args) {
        new CameraAccess().getNumberPlate("entranceCam");
//        new Main().getNumberPlate("exitCam");
    }

    private static Webcam entranceWebCam;

//    private static Webcam exitWebcam;

    CameraAccess() {
        if (entranceWebCam == null) {
            List<Webcam> webcams = Webcam.getWebcams();
            entranceWebCam = webcams.get(1);
            entranceWebCam.setViewSize(new Dimension(640, 480));
        }

//        if (exitWebcam == null) {
//            List<Webcam> webcams = Webcam.getWebcams();
//            exitWebcam = webcams.get(0);
//            exitWebcam.setViewSize(new Dimension(640, 480));
//        }
    }

    public String getNumberPlate(String cam) {
        Webcam webCam = null;
        if (cam.equals("entranceCam")) {
            webCam = entranceWebCam;
        }
//        else if (cam.equals("exitCam")) {
//            webCam = exitWebcam;
//        }

        try {
            out.println("Webcam: " + webCam.getName());
            webCam.open(true);

            ImageIO.write(webCam.getImage(), "jpeg", new File(cam + ".jpeg"));

            String filePath = "D:\\SMB_APP\\NewProjects\\oscar_river\\smartparkingserver\\" + cam + ".jpeg";

            return GV.detectText(filePath, System.out);
//                try {
//                    String secret_key = "sk_e8e01290138b439a7dfab1cc";
//
//                    // Read image file to byte array
//                    Path path = Paths.get(filePath);
//                    byte[] data = Files.readAllBytes(path);
//
//                    // Encode file bytes to base64
//                    byte[] encoded = Base64.getEncoder().encode(data);
//
//                    // Setup the HTTPS connection to api.openalpr.com
//                    URL url = new URL("https://api.openalpr.com/v2/recognize_bytes?recognize_vehicle=1&country=au&secret_key=" + secret_key);
//                    URLConnection con = url.openConnection();
//                    HttpURLConnection http = (HttpURLConnection) con;
//                    http.setRequestMethod("POST"); // PUT is another valid option
//                    http.setFixedLengthStreamingMode(encoded.length);
//                    http.setDoOutput(true);
//
//                    // Send our Base64 content over the stream
//                    try (OutputStream os = http.getOutputStream()) {
//                        os.write(encoded);
//                    }
//
//                    int status_code = http.getResponseCode();
//                    if (status_code == 200) {
//                        // Read the response
//                        BufferedReader in = new BufferedReader(new InputStreamReader(
//                                http.getInputStream()));
//                        StringBuilder json_content = new StringBuilder();
//                        String inputLine;
//                        while ((inputLine = in.readLine()) != null)
//                            json_content.append(inputLine);
//                        in.close();
//
//                        if(new JSONObject(json_content.toString()).getJSONArray("results").length()>0){
//                            String plate=new JSONObject(json_content.toString()).getJSONArray("results").getJSONObject(0).getString("plate");
//                            System.out.println(plate);
//                            return plate;
//                        }else{
//                            System.out.println("No plate found");
//                            return null;
//                        }
//
//                    } else {
//                        System.out.println("Got non-200 response: " + status_code);
//                    }
//
//                } catch (MalformedURLException e) {
//                    System.out.println("Bad URL");
//                } catch (IOException e) {
//                    System.out.println("Failed to open connection");
//                }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
