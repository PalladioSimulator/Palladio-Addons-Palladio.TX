package org.palladiosimulator.transactions.ccsim;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class WaitForGraph {

    private DirectedGraph<ITransaction, DefaultEdge> graph;

    public WaitForGraph() {
        graph = new DefaultDirectedGraph<ITransaction, DefaultEdge>(DefaultEdge.class);
    }

    public void add(LockQueue queue) {
        // nothing to do for empty wait list
        if (!queue.hasWaiting()) {
            return;
        }

        // create edge tx1 -> tx2 if tx1 waits for tx2 because tx2 is active but stronger than tx1's
        // lock mode
        for (LockDescriptor granted : queue.getActive()) {
            for (LockDescriptor waiting : queue.getWaiting()) {
                boolean compatible = LockMode.isCompatible(waiting.getType(), granted.getType());
                if (!compatible) {
                    // no edge for lock conversions (upgrades)
                    if (!waiting.getTx().equals(granted.getTx())) {
                        addEdge(waiting.getTx(), granted.getTx());
                    } else {
                        // lock conversion
                    }
                }
            }
        }
        if (queue.getWaiting().size() >= 2) {
            Iterator<LockDescriptor> itWaiting1 = queue.getWaiting().iterator();
            for (int i = 0; i < queue.getWaiting().size(); i++) {
                LockDescriptor waiting1 = itWaiting1.next();

                Iterator<LockDescriptor> itWaiting2 = queue.getWaiting().iterator();
                // skip first i+1 elements
                for (int j = 0; j < i + 1; j++) {
                    itWaiting2.next();
                }
                while (itWaiting2.hasNext()) {
                    LockDescriptor waiting2 = itWaiting2.next();
                    boolean compatible = LockMode.isCompatible(waiting2.getType(), waiting1.getType());
                    if (!compatible) {
                        addEdge(waiting2.getTx(), waiting1.getTx());
                    }
                }
            }
            Iterator<LockDescriptor> itWaiting2 = queue.getWaiting().iterator();

            itWaiting2.next();
        }
    }

    private void addEdge(ITransaction source, ITransaction target) {
        // ensure source vertex is part of the graph
        if (!graph.containsVertex(source)) {
            graph.addVertex(source);
        }
        // ensure target vertex is part of the graph
        if (!graph.containsVertex(target)) {
            graph.addVertex(target);
        }
        // create edge only if not yet present
        if (!graph.containsEdge(source, target)) {
            graph.addEdge(source, target);
        }
    }

    /**
     * @return the set of transactions that participate in at least one cycle in this graph
     */
    public Set<ITransaction> findCycles() {
        CycleDetector<ITransaction, DefaultEdge> cycleDetector = new CycleDetector<>(graph);
        return cycleDetector.findCycles();
    }

    public String toDOT() {
        VertexNameProvider<ITransaction> vertexIdProvider = new VertexNameProvider<ITransaction>() {
            @Override
            public String getVertexName(ITransaction tx) {
                return Integer.toString(tx.getId());
            }
        };

        DOTExporter<ITransaction, DefaultEdge> exporter = new DOTExporter<>(vertexIdProvider, null, null);
        Writer writer = new StringWriter();
        exporter.export(writer, graph);
        return writer.toString();
    }

}
