package de.renespeck.cytoprocess;

import giny.view.NodeView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.vaadin.Application;
import com.vaadin.ui.Window;

import csplugins.layout.algorithms.force.ForceDirectedLayout;

public class TestApplication extends Application {

    private static final long serialVersionUID = -2030003806795041928L;

    // adds logger properties
    static {
        String resource = "de/renespeck/cytoprocess/log4j.properties";
        InputStream in = TestApplication.class.getClassLoader().getResourceAsStream(resource);
        Properties properties = null;
        if (in != null) {
            properties = new Properties();
            try {
                properties.load(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PropertyConfigurator.configure(properties);
    }

    // gets a logger
    Logger logger = Logger.getLogger(TestApplication.class);

    final Cytoprocess cp = new Cytoprocess(900, 600);

    @Override
    public void init() {
        logger.info("init");
        // adds css
        setTheme("Cytoprocess");

        // adds Cytoprocess to main window
        setMainWindow(new Window("Cytoprocess Test Application"));
        getMainWindow().addComponent(cp);

        // draws a test graph
        generateTestGraph(2, 4);

        // applies a layout algorithm
        cp.applyLayoutAlgorithm(new ForceDirectedLayout());

        // repaint is need now
        cp.repaintGraph();

    }

    public void generateTestGraph(int n, int k) {

        Integer nodes[] = new Integer[n * k];
        for (int i = 0; i < n * k; i++)
            nodes[i] = cp.addNode("Node", 100, 100, NodeView.DIAMOND, "rgb(255,0,0)", false);

        for (int m = 0; m < k; m++)
            for (int i = n * m; i < n * (m + 1); i++)
                for (int j = i; j < n * (m + 1); j++)
                    if (i != j)
                        cp.addEdge(nodes[i], nodes[j], "1");

        for (int oc = 0; oc < k; oc++)
            for (int ic = oc; ic < k - 1; ic++)
                for (int v = 0; v < n; v++)
                    cp.addEdge(nodes[oc * n + v], nodes[ic * n + v + n], "2");
    }
}
