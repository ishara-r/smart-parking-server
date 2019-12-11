import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;


public class GV {
    public static void main(String[] args) throws Exception {

//        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", "C:\\Users\\ASUS\\Downloads\\smart-parking-2dc45288791b.json");
        detectText("D:\\SMB_APP\\NewProjects\\oscar_river\\smartparkingserver\\unnamed.jpg",System.out);
    }

    public static String detectText(String filePath, PrintStream out) throws Exception, IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    out.printf("Error: %s\n", res.getError().getMessage());
                    return filePath;
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
//                    out.printf("%s\n", annotation.getDescription());
                    String number = annotation.getDescription();
                    number = number.replace("-", "");
                    System.out.println(number);
                    return number;
                }
                break;
            }
        }
        return filePath;
    }
}
