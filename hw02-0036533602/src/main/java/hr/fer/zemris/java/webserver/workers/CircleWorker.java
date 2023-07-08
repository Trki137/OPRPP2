
package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
/**
 *This class is implementation of {@link IWebWorker} that will show an image of circle.
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class CircleWorker implements IWebWorker {

    @Override
    public void processRequest(RequestContext context) throws Exception {
        BufferedImage bim = new BufferedImage(200,200,BufferedImage.TYPE_3BYTE_BGR);

        Graphics2D graphics2D = bim.createGraphics();

        graphics2D.setColor(Color.RED);
        graphics2D.fillOval(0,0,200,200);

        graphics2D.dispose();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try{
            ImageIO.write(bim,"png",bos);

            context.setMimeType("image/png");
            context.setContentLength((long)bos.toByteArray().length);

            context.write(bos.toByteArray());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
