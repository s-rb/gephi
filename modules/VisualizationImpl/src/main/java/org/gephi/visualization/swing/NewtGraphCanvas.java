/*
 Copyright 2008-2010 Gephi
 Authors : Mathieu Bastian <mathieu.bastian@gephi.org>
 Website : http://www.gephi.org

 This file is part of Gephi.

 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 2011 Gephi Consortium. All rights reserved.

 The contents of this file are subject to the terms of either the GNU
 General Public License Version 3 only ("GPL") or the Common
 Development and Distribution License("CDDL") (collectively, the
 "License"). You may not use this file except in compliance with the
 License. You can obtain a copy of the License at
 http://gephi.org/about/legal/license-notice/
 or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
 specific language governing permissions and limitations under the
 License.  When distributing the software, include this License Header
 Notice in each file and include the License files at
 /cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
 License Header, with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"

 If you wish your version of this file to be governed by only the CDDL
 or only the GPL Version 3, indicate your decision by adding
 "[Contributor] elects to include this software in this distribution
 under the [CDDL or GPL Version 3] license." If you do not indicate a
 single choice of license, a recipient has the option to distribute
 your version of this file under either the CDDL, the GPL Version 3 or
 to extend the choice of license to its licensees as provided above.
 However, if you add GPL Version 3 code and therefore, elected the GPL
 Version 3 license, then the option applies only if the new code is
 made subject to such option by the copyright holder.

 Contributor(s):

 Portions Copyrighted 2011 Gephi Consortium.
 */

package org.gephi.visualization.swing;

import com.jogamp.newt.awt.NewtCanvasAWT;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

import java.awt.*;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import org.gephi.ui.utils.UIUtils;

/**
 * @author Mathieu Bastian
 */
public class NewtGraphCanvas extends GLAbstractListener {

    private final GLUT glut = new GLUT();

    private NewtCanvasAWT glCanvas;

    @Override
    protected GLAutoDrawable initDrawable() {
        GLWindow glWindow = GLWindow.create(getCaps());
//        glWindow.setSurfaceScale(new float[]{ScalableSurface.AUTOMAX_PIXELSCALE, ScalableSurface.AUTOMAX_PIXELSCALE});
        glCanvas = new HighDPIFixCanvas(glWindow);
//        glCanvas = new NewtCanvasAWT(glWindow);

//        glCanvas.setFocusable(true);
//        glCanvas.setIgnoreRepaint(true);
//        glCanvas.setMinimumSize(new Dimension(0, 0));   //Fix Canvas resize Issue

//        glCanvas.setMinimumSize(new Dimension(0, 0));   //Fix Canvas resize Issue
        //Basic init
        graphComponent = (Component) glCanvas;
        window = glWindow;
//        graphComponent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        return glWindow;
    }

    @Override
    protected void init(GL2 gl) {
//        globalScale = glWindow.getCurrentSurfaceScale(new float[2])[0];
        globalScale = (float) glCanvas.getGraphicsConfiguration().getDefaultTransform().getScaleX();
        engine.startDisplay();
    }

    @Override
    public void reinitWindow() {
        if (UIUtils.isAquaLookAndFeel()) {
            // Only used when collapse panel is set visible
            // Workaround for JOGL bug 1274

//            glCanvas.setNEWTChild(null);
//            glCanvas.setNEWTChild(glWindow);
        } else {
            // Fix issue when closing the collapse panel
            Container c = graphComponent.getParent();
            if (c != null) {
                c.remove(graphComponent);
                c.add(graphComponent, BorderLayout.CENTER);
            }
        }
    }

    @Override
    protected void render3DScene(GL2 gl, GLU glu) {
        if (vizController.getVizConfig().isShowFPS()) {
            gl.glPushMatrix();
            gl.glLoadIdentity();
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glPushMatrix();
            gl.glLoadIdentity();

            gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport);
            glu.gluOrtho2D(0, viewport.get(2), viewport.get(3), 0);
            gl.glDepthFunc(GL2.GL_ALWAYS);
            gl.glColor3i(192, 192, 192);
            gl.glRasterPos2f(10, 15 + (getGlobalScale() > 1f ? 8 : 0));
            String fpsRound = String.valueOf((int) fps);
            if (getGlobalScale() > 1f) {
                glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, fpsRound);
            } else {
                glut.glutBitmapString(GLUT.BITMAP_HELVETICA_10, fpsRound);
            }

            gl.glDepthFunc(GL2.GL_LESS);
            gl.glPopMatrix();
            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glPopMatrix();
        }
    }

    @Override
    protected void reshape3DScene(GL2 gl) {

    }

    @Override
    public void destroy() {
        super.destroy();
        glCanvas.getNEWTChild().destroy();
        glCanvas = null;
    }

    public class HighDPIFixCanvas extends NewtCanvasAWT {


        public HighDPIFixCanvas(GLWindow glWindow) {
            super(glWindow);
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            return new Dimension(d.width, d.height);
        }

        @Override
        public int getWidth() {
            return (int) (super.getWidth() * getGlobalScale());
        }

        @Override
        public int getHeight() {
            return (int) (super.getHeight() * getGlobalScale());
        }
    }
}
