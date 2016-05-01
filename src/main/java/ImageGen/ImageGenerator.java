package ImageGen;

/**
 * Created by Witek on 2016-05-01.
 */
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public abstract class ImageGenerator
{
    public BufferedImage GenerateImage( int width, int height )
    {
        BufferedImage newImage = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );

        for( int x = 0; x < newImage.getWidth(); ++x )
        {
            for( int y = 0; y < newImage.getHeight(); ++y )
            {
                int sampleAlpha = 255;
                int sampleR = GetNextSample( x, y );
                int sampleG = GetNextSample( x, y );
                int sampleB = GetNextSample( x, y );
                newImage.setRGB( x, y, sampleAlpha << 24 | sampleR << 16 | sampleG << 8 | sampleB  );
            }

        }

        return newImage;
    }

    abstract int GetNextSample(int x, int y);
}
