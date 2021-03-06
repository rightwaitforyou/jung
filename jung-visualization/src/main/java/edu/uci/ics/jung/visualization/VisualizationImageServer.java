/*
* Copyright (c) 2003, The JUNG Authors 
*
* All rights reserved.
*
* This software is open-source under the BSD license; see either
* "license.txt" or
* https://github.com/jrtom/jung/blob/master/LICENSE for a description.
*/
package edu.uci.ics.jung.visualization;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import edu.uci.ics.jung.algorithms.layout.Layout;

/**
 * A class that could be used on the server side of a thin-client application. It creates the jung
 * visualization, then produces an image of it.
 * @author tom
 *
 * @param <V> the vertex type
 * @param <E> the edge type
 */
@SuppressWarnings("serial")
public class VisualizationImageServer<V,E> extends BasicVisualizationServer<V,E> {

    Map<RenderingHints.Key, Object> renderingHints = new HashMap<RenderingHints.Key, Object>();
    
    /**
     * Creates a new instance with the specified layout and preferred size.
     * 
     * @param layout the Layout instance; provides the vertex locations
     * @param preferredSize the preferred size of the image
     */
    public VisualizationImageServer(Layout<V,E> layout, Dimension preferredSize) {
        super(layout, preferredSize);
        setSize(preferredSize);
        renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        addNotify();
    }
    
    public Image getImage(Point2D center, Dimension d) 
    {
        int width = getWidth();
        int height = getHeight();
        
        float scalex = (float)width/d.width;
        float scaley = (float)height/d.height;
        try 
        {
            renderContext.getMultiLayerTransformer().getTransformer(Layer.VIEW).scale(scalex, scaley, center);
    
            BufferedImage bi = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = bi.createGraphics();
            graphics.setRenderingHints(renderingHints);
            paint(graphics);
            graphics.dispose();
            return bi;
        } finally {
        	renderContext.getMultiLayerTransformer().getTransformer(Layer.VIEW).setToIdentity();
        }
    }
}
