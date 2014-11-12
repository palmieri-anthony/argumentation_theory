package fr.unice.ic.argumentation.ui;

 
import javax.swing.JFrame;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;
 
public class JGraphExemple1 extends JFrame {
	String edgeSupportStyle = mxConstants.STYLE_DASHED + "=1;" +
            mxConstants.STYLE_DASH_PATTERN + mxConstants.STYLE_RESIZABLE +"=0;" +
                    mxConstants.STYLE_MOVABLE+"=10"; 
  /** Pour éviter un warning venant du JFrame */
  private static final long serialVersionUID = -8123406571694511514L;
  private Object parent ;
  mxGraphComponent graphComponent ;
  public JGraphExemple1() {
    super("JGrapghX tutoriel: Exemple 1");
 
    mxGraph graph = new mxGraph();
    parent = graph.getDefaultParent();
 
    graph.getModel().beginUpdate();
    try {
    	String nodeStyle=mxConstants.STYLE_RESIZABLE +"=0";
      Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80, 30,nodeStyle);
      Object v2 = graph.insertVertex(parent, null, "World!", 240, 150, 80, 30);
      graph.insertEdge(parent, null, "Edge", v1, v2);
      
      graph.addListener(mxEvent.LABEL_CHANGED, new mxIEventListener() {
          @Override
          public void invoke(Object sender, mxEventObject evt) {
            System.out.println("labelChange");
                                  /* source == true when source changed, source == false, when target changed). */
          }
      });
      graph.setCellStyle( "edgeSupportStyle");
      String style = mxConstants.STYLE_RESIZABLE +"=0;" +
              mxConstants.STYLE_MOVABLE+"=0";
//Object level1 = graph.insertVertex(parent, null, "Bloc1", 10, 10, 350, 120,style); 
//Object level2 = graph.insertVertex(parent, null, "Bloc2", 10, 150, 350, 120,style); 
//Object edge11 = graph.insertEdge(level1, 
//               null, 
//               "lien11_12", 
//               level1,
//               level2,
//               edgeSupportStyle);
    } finally {
      graph.getModel().endUpdate();
    }
    graph.setCellsResizable(true);
    graph.setResetEdgesOnConnect(false);
    graph.setAllowLoops(false);
    graph.setAllowDanglingEdges(true);
    graph.setCellsDeletable(true);
    graph.setResetEdgesOnMove(true);
    graphComponent = new mxGraphComponent(graph);
    graphComponent.setConnectable(false);
    graphComponent.setDragEnabled(true);
    graphComponent.addListener(mxEvent.LABEL_CHANGED, new mxIEventListener() {
    	
		@Override
		public void invoke(Object sender, mxEventObject evt) {
//			 mxCell connectionCell = (mxCell) evt.getProperty("edge");
//			connectionCell.setStyle(edgeSupportStyle);
			System.out.println("ok");
			System.out.println(sender.getClass());
			mxCell k = (mxCell) sender;
			System.out.println(k.getValue());
		}
	});
    getContentPane().add(graphComponent);
  }
 
  /**
   * @param args
   */
  public static void main(String[] args) {
    JGraphExemple1 frame = new JGraphExemple1();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 320);
    frame.setVisible(true);
  }
}